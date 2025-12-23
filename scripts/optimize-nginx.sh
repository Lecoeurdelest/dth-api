#!/bin/bash
# Optimize Nginx configuration for T2.Medium EC2 instance

set -e

echo "🔧 Optimizing Nginx configuration for T2.Medium..."

# Backup original config
if [ -f "/etc/nginx/nginx.conf" ]; then
    sudo cp /etc/nginx/nginx.conf /etc/nginx/nginx.conf.backup
fi

# Get CPU count
CPU_COUNT=$(nproc)
WORKER_PROCESSES=$((CPU_COUNT < 2 ? 1 : 2))

# Update nginx.conf
sudo sed -i.bak "s/worker_processes auto;/worker_processes $WORKER_PROCESSES;/" /etc/nginx/nginx.conf

# Add optimized settings to nginx.conf if not present
if ! grep -q "worker_rlimit_nofile" /etc/nginx/nginx.conf; then
    sudo sed -i "/worker_processes/a\\worker_rlimit_nofile 2048;" /etc/nginx/nginx.conf
fi

# Update events block
sudo sed -i.bak 's/worker_connections 768;/worker_connections 512;/' /etc/nginx/nginx.conf 2>/dev/null || \
sudo sed -i '/events {/a\\    worker_connections 512;' /etc/nginx/nginx.conf

# Enable gzip compression
if ! grep -q "gzip on" /etc/nginx/nginx.conf; then
    sudo tee -a /etc/nginx/nginx.conf > /dev/null << 'EOF'

# Gzip compression
gzip on;
gzip_vary on;
gzip_proxied any;
gzip_comp_level 6;
gzip_types text/plain text/css text/xml text/javascript application/json application/javascript application/xml+rss application/rss+xml font/truetype font/opentype application/vnd.ms-fontobject image/svg+xml;
gzip_disable "msie6";
EOF
fi

# Test configuration
echo "🧪 Testing Nginx configuration..."
if sudo nginx -t; then
    echo "✅ Nginx configuration is valid"
    
    # Reload Nginx
    echo "🔄 Reloading Nginx..."
    sudo systemctl reload nginx
    echo "✅ Nginx optimized and reloaded"
else
    echo "❌ Nginx configuration test failed!"
    echo "Restoring backup..."
    sudo cp /etc/nginx/nginx.conf.backup /etc/nginx/nginx.conf
    sudo nginx -t
    exit 1
fi

echo ""
echo "✅ Nginx optimization complete!"
echo "Configuration: /etc/nginx/nginx.conf"
echo "Backup saved at: /etc/nginx/nginx.conf.backup"
echo ""
echo "Optimizations applied:"
echo "  - worker_processes: $WORKER_PROCESSES"
echo "  - worker_connections: 512"
echo "  - worker_rlimit_nofile: 2048"
echo "  - gzip compression: enabled"

