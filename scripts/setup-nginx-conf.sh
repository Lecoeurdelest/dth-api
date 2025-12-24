#!/bin/bash
# Setup Nginx configuration for DTH Project
# This script creates a complete nginx.conf file

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
echo "Nginx Configuration Setup"
echo "========================================="
echo ""

# Check if running as root
if [ "$EUID" -ne 0 ]; then 
    print_error "Please run as root (use sudo)"
    exit 1
fi

# Backup existing nginx.conf
NGINX_CONF="/etc/nginx/nginx.conf"
if [ -f "$NGINX_CONF" ]; then
    BACKUP_FILE="/etc/nginx/nginx.conf.backup.$(date +%Y%m%d_%H%M%S)"
    print_info "Backing up existing nginx.conf to $BACKUP_FILE"
    cp "$NGINX_CONF" "$BACKUP_FILE"
    print_success "Backup created"
fi

# Get script directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}")" && pwd )"
EXAMPLE_FILE="$SCRIPT_DIR/nginx.conf.example"

if [ ! -f "$EXAMPLE_FILE" ]; then
    print_error "nginx.conf.example not found in $SCRIPT_DIR"
    exit 1
fi

# Prompt for domain name
read -p "Enter your domain name (or press Enter for default): " DOMAIN

if [ -z "$DOMAIN" ]; then
    SERVER_NAME="_"
    print_info "Using default server_name: _"
else
    SERVER_NAME="$DOMAIN www.$DOMAIN"
    print_info "Using server_name: $SERVER_NAME"
fi

# Create nginx.conf from example
print_info "Creating nginx.conf..."

# Read example file and replace server_name
sed "s/server_name _;/server_name $SERVER_NAME;/" "$EXAMPLE_FILE" > "$NGINX_CONF"

print_success "nginx.conf created"

# Test configuration
print_info "Testing Nginx configuration..."
if nginx -t; then
    print_success "Nginx configuration is valid"
    
    # Ask to restart
    read -p "Restart Nginx now? (y/n): " RESTART
    if [ "$RESTART" = "y" ] || [ "$RESTART" = "Y" ]; then
        print_info "Restarting Nginx..."
        systemctl restart nginx
        print_success "Nginx restarted"
    else
        print_info "Nginx not restarted. Run 'sudo systemctl restart nginx' when ready."
    fi
else
    print_error "Nginx configuration test failed!"
    print_info "Restoring backup..."
    if [ -f "$BACKUP_FILE" ]; then
        cp "$BACKUP_FILE" "$NGINX_CONF"
        print_info "Backup restored"
    fi
    exit 1
fi

echo ""
echo "========================================="
print_success "Nginx Configuration Setup Completed!"
echo "========================================="
echo ""
echo "Configuration file: $NGINX_CONF"
if [ -f "$BACKUP_FILE" ]; then
    echo "Backup file: $BACKUP_FILE"
fi
echo ""
echo "URLs:"
echo "  - Frontend: http://$SERVER_NAME/"
echo "  - API: http://$SERVER_NAME/api/*"
echo "  - Swagger UI: http://$SERVER_NAME/api/swagger-ui.html"
echo ""

