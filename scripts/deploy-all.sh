#!/bin/bash
# Complete deployment script for DTH project
# This script orchestrates the full deployment process

set -e

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
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

print_header() {
    echo -e "${BLUE}$1${NC}"
}

echo ""
print_header "========================================="
print_header "DTH Project - Complete Deployment"
print_header "========================================="
echo ""

# Check if running on EC2
if [ ! -d "/opt/dth" ]; then
    print_error "Directory /opt/dth not found. Is this EC2 instance set up?"
    print_info "Run setup-ec2.sh first, or create /opt/dth directory"
    exit 1
fi

# Step 0: System optimization (optional)
print_header "Step 0: System Optimization (Recommended for T2.Medium)"
INSTANCE_TYPE=$(curl -s http://169.254.169.254/latest/meta-data/instance-type 2>/dev/null || echo "unknown")
if [[ "$INSTANCE_TYPE" == *"t2.medium"* ]] || [[ "$INSTANCE_TYPE" == *"t3.medium"* ]]; then
    print_info "Detected instance type: $INSTANCE_TYPE"
    echo "Recommended optimizations:"
    echo "  - System parameters (swappiness, file descriptors, kernel)"
    echo "  - MariaDB buffer pool and connections"
    echo ""
    read -p "Run system optimizations? (y/n): " OPTIMIZE_SYSTEM
    if [ "$OPTIMIZE_SYSTEM" = "y" ]; then
        cd /opt/dth/dth-api
        if [ -f "scripts/optimize-system.sh" ]; then
            bash scripts/optimize-system.sh
        fi
        if [ -f "scripts/mariadb-optimize.sh" ]; then
            bash scripts/mariadb-optimize.sh
        fi
    fi
fi

# Step 1: Setup database (interactive)
print_header "Step 1: Database Setup"
echo "Make sure MariaDB is configured:"
echo "  1. Database 'app_db' exists"
echo "  2. User 'app_user' exists with proper permissions"
echo ""
read -p "Is database configured? (y/n): " DB_CONFIGURED

if [ "$DB_CONFIGURED" != "y" ]; then
    print_info "Please configure database first:"
    echo "  sudo mysql -u root -p"
    echo "  CREATE DATABASE app_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
    echo "  CREATE USER 'app_user'@'localhost' IDENTIFIED BY 'your_password';"
    echo "  GRANT ALL PRIVILEGES ON app_db.* TO 'app_user'@'localhost';"
    echo "  FLUSH PRIVILEGES;"
    exit 1
fi

# Step 2: Configure application properties
print_header "Step 2: Application Configuration"
if [ ! -f "/opt/dth/dth-api/src/main/resources/application-prod.properties" ]; then
    print_error "application-prod.properties not found!"
    print_info "Please create and configure application-prod.properties"
    exit 1
fi

print_info "Please verify application-prod.properties has correct:"
echo "  - Database credentials"
echo "  - JWT secret key"
read -p "Continue? (y/n): " CONTINUE
if [ "$CONTINUE" != "y" ]; then
    exit 1
fi

# Step 3: Deploy backend
print_header "Step 3: Deploying Backend..."
cd /opt/dth/dth-api
if [ -f "scripts/deploy-backend.sh" ]; then
    bash scripts/deploy-backend.sh
else
    print_error "deploy-backend.sh not found"
    exit 1
fi

# Wait for backend to be ready
print_info "Waiting for backend to start..."
sleep 10

# Check backend health
if curl -f http://localhost:8080/actuator/health > /dev/null 2>&1 || curl -f http://localhost:8080/ > /dev/null 2>&1; then
    print_success "Backend is running"
else
    print_error "Backend health check failed"
    print_info "Check logs: sudo journalctl -u dth-api -n 50"
fi

# Step 4: Deploy frontend
print_header "Step 4: Deploying Frontend..."
cd /opt/dth/dth-ssr
if [ -f "scripts/deploy-frontend.sh" ]; then
    bash scripts/deploy-frontend.sh
else
    print_error "deploy-frontend.sh not found"
    exit 1
fi

# Wait for frontend to be ready
print_info "Waiting for frontend to start..."
sleep 10

# Check frontend health
if curl -f http://localhost:3000 > /dev/null 2>&1; then
    print_success "Frontend is running"
else
    print_error "Frontend health check failed"
    print_info "Check logs: sudo journalctl -u dth-frontend -n 50"
fi

# Step 5: Configure Nginx
print_header "Step 5: Configuring Nginx..."

# Optimize Nginx
read -p "Optimize Nginx for T2.Medium? (y/n): " OPTIMIZE_NGINX
if [ "$OPTIMIZE_NGINX" = "y" ]; then
    if [ -f "scripts/optimize-nginx.sh" ]; then
        bash scripts/optimize-nginx.sh
    fi
fi
if [ -f "/opt/dth/dth-api/scripts/nginx-config.sh" ]; then
    bash /opt/dth/dth-api/scripts/nginx-config.sh
else
    print_error "nginx-config.sh not found"
    print_info "Please configure Nginx manually"
fi

echo ""
print_header "========================================="
print_success "Deployment Completed!"
print_header "========================================="
echo ""
echo "Services Status:"
echo "  Backend:  sudo systemctl status dth-api"
echo "  Frontend: sudo systemctl status dth-frontend"
echo "  Nginx:    sudo systemctl status nginx"
echo ""
echo "View Logs:"
echo "  Backend:  sudo journalctl -u dth-api -f"
echo "  Frontend: sudo journalctl -u dth-frontend -f"
echo ""
echo "Test URLs:"
echo "  Frontend: http://your-domain.com"
echo "  Backend:  http://your-domain.com/api"
echo "  Swagger:  http://your-domain.com/swagger-ui.html"
echo ""
echo "Next Steps:"
echo "  1. Configure DNS to point to this server (if using domain)"
echo "  2. Install SSL certificate: sudo certbot --nginx -d your-domain.com"
echo "  3. Test all endpoints"
echo ""

