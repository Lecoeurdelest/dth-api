#!/bin/bash
# System-wide optimizations for T2.Medium EC2 instance
# Run this script once after EC2 setup

set -e

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_info() {
    echo -e "${YELLOW}→ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

echo "========================================="
echo "System Optimization for T2.Medium"
echo "========================================="

# 1. Optimize swappiness (reduce swap usage)
print_info "Optimizing swappiness..."
if ! grep -q "vm.swappiness" /etc/sysctl.conf; then
    echo "vm.swappiness = 10" | sudo tee -a /etc/sysctl.conf
    sudo sysctl -w vm.swappiness=10
    print_success "Swappiness set to 10 (default: 60)"
else
    print_info "Swappiness already configured"
fi

# 2. Optimize file limits
print_info "Optimizing file descriptor limits..."
if ! grep -q "* soft nofile" /etc/security/limits.conf; then
    sudo tee -a /etc/security/limits.conf > /dev/null << EOF
* soft nofile 65535
* hard nofile 65535
EOF
    print_success "File descriptor limits increased"
else
    print_info "File descriptor limits already configured"
fi

# 3. Optimize kernel parameters
print_info "Optimizing kernel parameters..."
if ! grep -q "net.core.somaxconn" /etc/sysctl.conf; then
    sudo tee -a /etc/sysctl.conf > /dev/null << EOF

# Network optimizations
net.core.somaxconn = 1024
net.ipv4.tcp_max_syn_backlog = 2048
net.ipv4.ip_local_port_range = 10000 65535

# Memory optimizations
vm.dirty_ratio = 15
vm.dirty_background_ratio = 5
EOF
    sudo sysctl -p
    print_success "Kernel parameters optimized"
else
    print_info "Kernel parameters already configured"
fi

# 4. Disable unnecessary services (optional - for minimal setup)
print_info "Checking unnecessary services..."
# Uncomment if you want to disable services
# sudo systemctl disable snapd 2>/dev/null || true
# sudo systemctl disable unattended-upgrades 2>/dev/null || true
print_info "Services check completed"

# 5. Configure log rotation
print_info "Configuring log rotation..."
sudo tee /etc/logrotate.d/dth-apps > /dev/null << EOF
/opt/dth/logs/*.log {
    daily
    rotate 7
    compress
    delaycompress
    missingok
    notifempty
    create 0644 ubuntu ubuntu
    sharedscripts
    postrotate
        systemctl reload dth-api > /dev/null 2>&1 || true
        systemctl reload dth-frontend > /dev/null 2>&1 || true
    endscript
}
EOF
print_success "Log rotation configured (7 days retention)"

# 6. Setup swap file (if not exists and memory < 4GB)
TOTAL_MEM=$(free -m | awk '/^Mem:/{print $2}')
if [ $TOTAL_MEM -lt 4096 ] && [ ! -f /swapfile ]; then
    print_info "Creating swap file (1GB)..."
    sudo fallocate -l 1G /swapfile
    sudo chmod 600 /swapfile
    sudo mkswap /swapfile
    sudo swapon /swapfile
    echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab
    print_success "Swap file created (1GB)"
else
    print_info "Swap file already exists or memory sufficient"
fi

# 7. Optimize Nginx worker processes
if command -v nginx > /dev/null 2>&1; then
    print_info "Optimizing Nginx configuration..."
    CPU_CORES=$(nproc)
    NGINX_CONF="/etc/nginx/nginx.conf"
    
    if [ -f "$NGINX_CONF" ]; then
        # Update worker_processes
        sudo sed -i "s/worker_processes.*/worker_processes $CPU_CORES;/" "$NGINX_CONF"
        
        # Add or update worker_connections if not exists
        if ! grep -q "worker_connections" "$NGINX_CONF"; then
            sudo sed -i '/worker_processes/a\    worker_connections 512;' "$NGINX_CONF"
        else
            sudo sed -i 's/worker_connections.*/worker_connections 512;/' "$NGINX_CONF"
        fi
        
        print_success "Nginx optimized (worker_processes: $CPU_CORES, worker_connections: 512)"
    fi
fi

echo ""
echo "========================================="
print_success "System Optimization Completed!"
echo "========================================="
echo ""
echo "Applied optimizations:"
echo "  ✓ Swappiness: 10"
echo "  ✓ File descriptor limits: 65535"
echo "  ✓ Kernel network parameters"
echo "  ✓ Log rotation: 7 days"
echo "  ✓ Nginx worker processes optimized"
echo ""
echo "Note: Some changes require reboot to take full effect"
echo "      Run: sudo reboot (if needed)"
