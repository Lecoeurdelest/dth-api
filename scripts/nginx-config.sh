#!/bin/bash
# Configure Nginx for DTH project
# This script creates Nginx configuration file

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
echo "Nginx Configuration Script"
echo "========================================="

# Prompt for domain name
read -p "Enter your domain name (e.g., example.com) or press Enter for localhost: " DOMAIN

if [ -z "$DOMAIN" ]; then
    DOMAIN="localhost"
    SERVER_NAME="_"
else
    SERVER_NAME="$DOMAIN www.$DOMAIN"
fi

# Create Nginx config
CONFIG_FILE="/etc/nginx/sites-available/dth"

print_info "Creating Nginx configuration..."

sudo tee $CONFIG_FILE > /dev/null << EOF
server {
    listen 80;
    server_name $SERVER_NAME;
    
    client_max_body_size 10M;
    
    # Frontend (Next.js)
    location / {
        proxy_pass http://localhost:3000;
        proxy_http_version 1.1;
        proxy_set_header Upgrade \$http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
        proxy_cache_bypass \$http_upgrade;
        
        # Timeouts
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }
    
    # Backend API (includes all /api endpoints including Swagger UI at /api/swagger-ui.html)
    location /api {
        proxy_pass http://localhost:8080;
        proxy_http_version 1.1;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
        
        # Timeouts
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
        
        # WebSocket support (if needed)
        proxy_set_header Upgrade \$http_upgrade;
        proxy_set_header Connection "upgrade";
        
        # CORS headers (adjust as needed)
        add_header 'Access-Control-Allow-Origin' '*' always;
        add_header 'Access-Control-Allow-Methods' 'GET, POST, PUT, DELETE, OPTIONS' always;
        add_header 'Access-Control-Allow-Headers' 'Authorization, Content-Type' always;
        
        if (\$request_method = 'OPTIONS') {
            return 204;
        }
    }
    
    # Static files caching
    location ~* \.(jpg|jpeg|png|gif|ico|css|js|svg|woff|woff2|ttf|eot)$ {
        proxy_pass http://localhost:3000;
        expires 30d;
        add_header Cache-Control "public, immutable";
    }
}
EOF

print_success "Nginx configuration created"

# Enable site
print_info "Enabling site..."
sudo ln -sf $CONFIG_FILE /etc/nginx/sites-enabled/dth
sudo rm -f /etc/nginx/sites-enabled/default

# Test configuration
print_info "Testing Nginx configuration..."
if sudo nginx -t; then
    print_success "Nginx configuration is valid"
    
    # Restart Nginx
    print_info "Restarting Nginx..."
    sudo systemctl restart nginx
    print_success "Nginx restarted"
else
    print_error "Nginx configuration test failed!"
    exit 1
fi

echo ""
echo "========================================="
print_success "Nginx Configuration Completed!"
echo "========================================="
echo ""
echo "Configuration file: $CONFIG_FILE"
echo "Domain: $SERVER_NAME"
echo ""
echo "Next steps:"
echo "1. If you have a domain, configure DNS to point to this server"
echo "2. Install SSL certificate: sudo certbot --nginx -d $DOMAIN"
echo "3. Test: curl http://$DOMAIN"
echo ""

