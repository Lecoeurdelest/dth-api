#!/bin/bash
# Setup script for EC2 instance
# Run this script on a fresh Ubuntu 22.04 EC2 instance

set -e

echo "========================================="
echo "DTH Project EC2 Setup Script"
echo "========================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_info() {
    echo -e "${YELLOW}→ $1${NC}"
}

# Check if running as root
if [ "$EUID" -eq 0 ]; then 
    print_error "Please do not run as root. Script will use sudo when needed."
    exit 1
fi

# Update system
print_info "Updating system packages..."
sudo apt update && sudo apt upgrade -y
print_success "System updated"

# Install basic tools
print_info "Installing basic tools..."
sudo apt install -y git curl wget unzip build-essential
print_success "Basic tools installed"

# Install Java 17
print_info "Installing Java 17..."
sudo apt install -y openjdk-17-jdk
java_version=$(java -version 2>&1 | head -n 1)
print_success "Java installed: $java_version"

# Install Node.js 20
print_info "Installing Node.js 20..."
curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
sudo apt install -y nodejs
node_version=$(node -v)
npm_version=$(npm -v)
print_success "Node.js installed: $node_version, npm: $npm_version"

# Install PM2
print_info "Installing PM2..."
sudo npm install -g pm2
print_success "PM2 installed"

# Install MariaDB
print_info "Installing MariaDB..."
sudo apt install -y mariadb-server mariadb-client
sudo systemctl start mariadb
sudo systemctl enable mariadb
print_success "MariaDB installed and started"

# Install Nginx
print_info "Installing Nginx..."
sudo apt install -y nginx
sudo systemctl start nginx
sudo systemctl enable nginx
print_success "Nginx installed and started"

# Install UFW (Firewall)
print_info "Configuring firewall..."
sudo apt install -y ufw
sudo ufw allow 22/tcp
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw --force enable
print_success "Firewall configured"

# Create application directories
print_info "Creating application directories..."
sudo mkdir -p /opt/dth/{logs,backups,scripts}
sudo chown -R $USER:$USER /opt/dth
print_success "Directories created"

# Create backup script
print_info "Creating backup script..."
cat > /opt/dth/scripts/backup-db.sh << 'EOF'
#!/bin/bash
BACKUP_DIR="/opt/dth/backups"
DATE=$(date +%Y%m%d_%H%M%S)
DB_NAME="app_db"
DB_USER="app_user"
DB_PASS="${DB_PASSWORD}"

mkdir -p $BACKUP_DIR

mysqldump -u $DB_USER -p$DB_PASS $DB_NAME > $BACKUP_DIR/db_backup_$DATE.sql 2>/dev/null

if [ $? -eq 0 ]; then
    gzip $BACKUP_DIR/db_backup_$DATE.sql
    find $BACKUP_DIR -name "db_backup_*.sql.gz" -mtime +30 -delete
    echo "Backup completed: db_backup_$DATE.sql.gz"
else
    echo "Backup failed!"
    exit 1
fi
EOF
chmod +x /opt/dth/scripts/backup-db.sh
print_success "Backup script created"

echo ""
echo "========================================="
print_success "EC2 Setup Completed!"
echo "========================================="
echo ""
echo "Next steps:"
echo "1. Secure MariaDB: sudo mysql_secure_installation"
echo "2. Create database and user:"
echo "   sudo mysql -u root -p"
echo "   CREATE DATABASE app_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
echo "   CREATE USER 'app_user'@'localhost' IDENTIFIED BY 'your_password';"
echo "   GRANT ALL PRIVILEGES ON app_db.* TO 'app_user'@'localhost';"
echo "   FLUSH PRIVILEGES;"
echo "3. Clone your repository to /opt/dth/"
echo "4. Configure application properties"
echo "5. Build and deploy backend and frontend"
echo ""
echo "See DEPLOYMENT.md for detailed instructions."

