#!/bin/bash
# Optimize MariaDB configuration for T2.Medium EC2 instance
# Run this script after installing MariaDB on EC2

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
echo "MariaDB Optimization for T2.Medium"
echo "========================================="

# Backup current config
CONFIG_FILE="/etc/mysql/mariadb.conf.d/50-server.cnf"
if [ -f "$CONFIG_FILE" ]; then
    print_info "Backing up current config..."
    sudo cp "$CONFIG_FILE" "${CONFIG_FILE}.backup.$(date +%Y%m%d_%H%M%S)"
    print_success "Backup created"
fi

# Get available memory (in MB)
TOTAL_MEM=$(free -m | awk '/^Mem:/{print $2}')
print_info "Total system memory: ${TOTAL_MEM}MB"

# Calculate optimal buffer pool size (50% of available RAM, but max 512MB for T2.Medium)
# Reserve 2GB for OS + applications, use rest for MariaDB
INNODB_BUFFER_POOL_SIZE=$(( (TOTAL_MEM - 2048) / 2 ))
if [ $INNODB_BUFFER_POOL_SIZE -gt 512 ]; then
    INNODB_BUFFER_POOL_SIZE=512
fi
if [ $INNODB_BUFFER_POOL_SIZE -lt 256 ]; then
    INNODB_BUFFER_POOL_SIZE=256
fi

print_info "Optimized innodb_buffer_pool_size: ${INNODB_BUFFER_POOL_SIZE}M"

# Create optimized config
print_info "Creating optimized MariaDB configuration..."

sudo tee -a "$CONFIG_FILE" > /dev/null << EOF

# ========================================
# T2.Medium Optimization (Added by script)
# ========================================

[mysqld]
# Buffer Pool (Optimized for 4GB RAM)
innodb_buffer_pool_size = ${INNODB_BUFFER_POOL_SIZE}M
innodb_buffer_pool_instances = 1

# Connection settings (Reduced for T2.Medium)
max_connections = 50
max_allowed_packet = 64M

# Thread settings
thread_cache_size = 8
thread_stack = 256K

# Query cache (Disabled - deprecated in MySQL 8.0+)
# query_cache_type = 0
# query_cache_size = 0

# Table cache
table_open_cache = 2000
table_definition_cache = 1400

# InnoDB settings
innodb_log_file_size = 128M
innodb_log_buffer_size = 16M
innodb_flush_log_at_trx_commit = 2
innodb_flush_method = O_DIRECT

# Performance Schema (Disable to save memory)
performance_schema = OFF

# Temporary tables
tmp_table_size = 32M
max_heap_table_size = 32M

# Sort buffer
sort_buffer_size = 2M
read_buffer_size = 1M
read_rnd_buffer_size = 2M
join_buffer_size = 2M

# Key buffer (for MyISAM, not used but kept)
key_buffer_size = 16M

EOF

print_success "Configuration added"

# Restart MariaDB
print_info "Restarting MariaDB..."
sudo systemctl restart mariadb

# Wait for MariaDB to be ready
sleep 5

# Verify MariaDB is running
if sudo systemctl is-active --quiet mariadb; then
    print_success "MariaDB restarted successfully"
    
    # Show current settings
    print_info "Current MariaDB settings:"
    mysql -u root -p -e "SHOW VARIABLES LIKE 'innodb_buffer_pool_size';" 2>/dev/null || \
    echo "Note: Run 'mysql -u root -p' to verify settings manually"
else
    print_error "MariaDB failed to restart!"
    print_info "Check logs: sudo journalctl -u mariadb -n 50"
    exit 1
fi

echo ""
echo "========================================="
print_success "MariaDB Optimization Completed!"
echo "========================================="
echo ""
echo "Optimized settings:"
echo "  - innodb_buffer_pool_size: ${INNODB_BUFFER_POOL_SIZE}M"
echo "  - max_connections: 50"
echo "  - performance_schema: OFF"
echo ""
echo "To verify:"
echo "  mysql -u root -p -e \"SHOW VARIABLES LIKE 'innodb_buffer_pool_size';\""

