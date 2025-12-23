#!/bin/bash
# Optimize MariaDB configuration for T2.Medium EC2 instance
# Run this script after MariaDB installation

set -e

echo "🔧 Optimizing MariaDB configuration for T2.Medium (4GB RAM)..."

# Backup original config
if [ -f "/etc/mysql/mariadb.conf.d/50-server.cnf" ]; then
    sudo cp /etc/mysql/mariadb.conf.d/50-server.cnf /etc/mysql/mariadb.conf.d/50-server.cnf.backup
fi

# Create optimized configuration
sudo tee -a /etc/mysql/mariadb.conf.d/99-optimize-t2medium.cnf > /dev/null << 'EOF'
# Optimized configuration for T2.Medium (4GB RAM)
# Created by optimize-mariadb.sh

[mysqld]
# InnoDB Buffer Pool - Use ~25% of available RAM (1GB for 4GB system)
innodb_buffer_pool_size = 512M
innodb_buffer_pool_instances = 1
innodb_log_file_size = 128M
innodb_log_buffer_size = 16M
innodb_flush_log_at_trx_commit = 2
innodb_flush_method = O_DIRECT

# Connection settings - Reduced for small instance
max_connections = 50
thread_cache_size = 8
table_open_cache = 200
table_definition_cache = 400

# Query cache - Disabled (deprecated in newer versions)
query_cache_type = 0
query_cache_size = 0

# Temporary tables
tmp_table_size = 32M
max_heap_table_size = 32M

# Other optimizations
skip_name_resolve = 1
max_allowed_packet = 16M
sort_buffer_size = 2M
read_buffer_size = 1M
read_rnd_buffer_size = 2M
join_buffer_size = 2M

# Performance schema - Disable for small instances
performance_schema = OFF
EOF

echo "✅ MariaDB optimization config created"

# Restart MariaDB to apply changes
echo "🔄 Restarting MariaDB..."
sudo systemctl restart mariadb

# Wait for MariaDB to start
sleep 5

# Verify MariaDB is running
if sudo systemctl is-active --quiet mariadb; then
    echo "✅ MariaDB restarted successfully"
    
    # Show current configuration
    echo ""
    echo "📊 Current MariaDB configuration:"
    sudo mysql -u root -p -e "SHOW VARIABLES LIKE 'innodb_buffer_pool_size';" 2>/dev/null || \
    echo "Run manually: mysql -u root -p -e \"SHOW VARIABLES LIKE 'innodb_buffer_pool_size';\""
else
    echo "❌ MariaDB failed to start!"
    echo "Restoring backup..."
    sudo cp /etc/mysql/mariadb.conf.d/50-server.cnf.backup /etc/mysql/mariadb.conf.d/50-server.cnf
    sudo systemctl restart mariadb
    exit 1
fi

echo ""
echo "✅ MariaDB optimization complete!"
echo "Configuration file: /etc/mysql/mariadb.conf.d/99-optimize-t2medium.cnf"
echo "Backup saved at: /etc/mysql/mariadb.conf.d/50-server.cnf.backup"

