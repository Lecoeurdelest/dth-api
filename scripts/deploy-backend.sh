#!/bin/bash
# Deploy Backend (Spring Boot) to EC2
# This script builds and deploys the Spring Boot application

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

# Configuration
APP_DIR="/opt/dth/dth-api"
SERVICE_NAME="dth-api"
JAR_NAME="app-0.0.1-SNAPSHOT.jar"
SPRING_PROFILE="prod"

echo "========================================="
echo "Backend Deployment Script"
echo "========================================="

# Check if directory exists
if [ ! -d "$APP_DIR" ]; then
    print_error "Application directory not found: $APP_DIR"
    exit 1
fi

cd "$APP_DIR"

# Check if application.properties exists
if [ ! -f "src/main/resources/application-prod.properties" ]; then
    print_error "application-prod.properties not found!"
    print_info "Please create application-prod.properties before deploying"
    exit 1
fi

# Stop service if running
print_info "Stopping service if running..."
sudo systemctl stop $SERVICE_NAME 2>/dev/null || true
print_success "Service stopped"

# Clean old build
print_info "Cleaning old build..."
./gradlew clean --no-daemon || true
print_success "Clean completed"

# Build application
print_info "Building application..."
./gradlew build -x test --no-daemon

if [ ! -f "build/libs/$JAR_NAME" ]; then
    print_error "Build failed! JAR file not found"
    exit 1
fi

print_success "Build completed: build/libs/$JAR_NAME"

# Create logs directory
mkdir -p /opt/dth/logs

# Detect instance type and set optimized JAVA_OPTS
INSTANCE_TYPE=$(curl -s http://169.254.169.254/latest/meta-data/instance-type 2>/dev/null || echo "unknown")
if [[ "$INSTANCE_TYPE" == *"t2.medium"* ]] || [[ "$INSTANCE_TYPE" == *"t3.medium"* ]]; then
    print_info "Detected instance type: $INSTANCE_TYPE - Using optimized memory settings for medium instances"
    JAVA_OPTS="-Xms256m -Xmx768m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
    MEMORY_LIMIT="1536M"
    CPU_QUOTA="180%"
else
    print_info "Instance type: $INSTANCE_TYPE - Using standard memory settings"
    JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"
    MEMORY_LIMIT="2048M"
    CPU_QUOTA="200%"
fi

# Create systemd service file
print_info "Creating systemd service with optimized settings..."
sudo tee /etc/systemd/system/$SERVICE_NAME.service > /dev/null << EOF
[Unit]
Description=DTH API Service
After=network.target mariadb.service

[Service]
Type=simple
User=$USER
WorkingDirectory=$APP_DIR
ExecStart=/usr/bin/java \$JAVA_OPTS -jar -Dspring.profiles.active=$SPRING_PROFILE $APP_DIR/build/libs/$JAR_NAME
Restart=always
RestartSec=10
StandardOutput=append:/opt/dth/logs/api.log
StandardError=append:/opt/dth/logs/api-error.log
Environment="SPRING_PROFILES_ACTIVE=$SPRING_PROFILE"
Environment="JAVA_OPTS=$JAVA_OPTS"

# Resource limits for optimization
MemoryLimit=$MEMORY_LIMIT
CPUQuota=$CPU_QUOTA

[Install]
WantedBy=multi-user.target
EOF

print_success "Systemd service created"

# Reload systemd and start service
print_info "Starting service..."
sudo systemctl daemon-reload
sudo systemctl enable $SERVICE_NAME
sudo systemctl start $SERVICE_NAME

# Wait a bit for service to start
sleep 5

# Check service status
if sudo systemctl is-active --quiet $SERVICE_NAME; then
    print_success "Service started successfully!"
    print_info "Service status:"
    sudo systemctl status $SERVICE_NAME --no-pager -l
else
    print_error "Service failed to start!"
    print_info "Check logs with: sudo journalctl -u $SERVICE_NAME -n 50"
    exit 1
fi

echo ""
echo "========================================="
print_success "Backend Deployment Completed!"
echo "========================================="
echo ""
echo "Service: $SERVICE_NAME"
echo "Status: sudo systemctl status $SERVICE_NAME"
echo "Logs: sudo journalctl -u $SERVICE_NAME -f"
echo "Or: tail -f /opt/dth/logs/api.log"
echo ""
echo "API should be available at: http://localhost:8080"

