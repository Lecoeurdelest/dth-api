#!/usr/bin/env python3
"""
Cross-platform deployment wrapper script
Auto-setup dependencies and run Dagger pipeline
Supports: Windows, macOS, Linux
"""

import os
import sys
import platform
import subprocess
import shutil
from pathlib import Path

# ANSI color codes for cross-platform support
class Colors:
    GREEN = '\033[92m'
    YELLOW = '\033[93m'
    RED = '\033[91m'
    BLUE = '\033[94m'
    CYAN = '\033[96m'
    RESET = '\033[0m'
    BOLD = '\033[1m'

def print_colored(message, color=Colors.RESET):
    """Print colored message (works on Unix, stripped on Windows)"""
    if platform.system() == "Windows":
        # Windows CMD doesn't support ANSI by default, but PowerShell does
        # Try to enable ANSI support, fallback to plain text
        try:
            import ctypes
            kernel32 = ctypes.windll.kernel32
            kernel32.SetConsoleMode(kernel32.GetStdHandle(-11), 7)
        except:
            pass
    print(f"{color}{message}{Colors.RESET}")


def print_success(msg):
    print_colored(f"✅ {msg}", Colors.GREEN)


def print_warning(msg):
    print_colored(f"⚠️  {msg}", Colors.YELLOW)


def print_error(msg):
    print_colored(f"❌ {msg}", Colors.RED)


def print_info(msg):
    print_colored(f"ℹ️  {msg}", Colors.CYAN)


def check_command(cmd, install_instructions=None):
    """Check if a command exists, return True if found"""
    if shutil.which(cmd):
        return True
    if install_instructions:
        print_warning(f"{cmd} not found. {install_instructions}")
    return False


def install_python_dependencies():
    """Install Python dependencies if missing (auto-install)"""
    script_dir = Path(__file__).parent
    requirements_file = script_dir / "requirements.txt"
    
    if not requirements_file.exists():
        print_error(f"requirements.txt not found in {script_dir}")
        return False
    
    print_info("Checking Python dependencies...")
    
    try:
        import dagger
        from dotenv import load_dotenv
        print_success("Python dependencies already installed")
        return True
    except ImportError:
        print_warning("Python dependencies missing, auto-installing...")
        try:
            # Auto-install without asking
            result = subprocess.run(
                [sys.executable, "-m", "pip", "install", "-r", str(requirements_file), "--quiet"],
                check=False,
                capture_output=True,
                text=True
            )
            if result.returncode == 0:
                print_success("Python dependencies installed successfully")
            return True
            else:
                print_error(f"Failed to install dependencies")
                print_info(f"Error: {result.stderr}")
                print_info(f"Try running manually: {sys.executable} -m pip install -r {requirements_file}")
                return False
        except Exception as e:
            print_error(f"Failed to install dependencies: {e}")
            print_info(f"Try running manually: {sys.executable} -m pip install -r {requirements_file}")
            return False


def check_dagger_cli():
    """Check if Dagger CLI is installed, provide installation instructions"""
    if check_command("dagger"):
        print_success("Dagger CLI found")
        return True
    
    system = platform.system().lower()
    print_error("Dagger CLI not found")
    
    print_colored("\n📥 Installation instructions:", Colors.BOLD)
    if system == "darwin":  # macOS
        print_info("Run: brew install dagger/tap/dagger")
        print_info("Or download from: https://docs.dagger.io/install")
    elif system == "linux":
        print_info("Install from: https://docs.dagger.io/install")
        print_info("Or use: curl -L https://dl.dagger.io/dagger/install.sh | sh")
    elif system == "windows":
        print_info("Run: winget install dagger --source winget")
        print_info("Or download from: https://docs.dagger.io/install")
    
    print()
    print_error("Dagger CLI is required. Please install it and try again.")
    return False


def setup_permissions():
    """Setup file permissions (Unix only)"""
    if platform.system() == "Windows":
        return  # No chmod needed on Windows
    
    script_dir = Path(__file__).parent
    main_py = script_dir / "main.py"
    
    # Make main.py executable
    if main_py.exists():
        try:
            os.chmod(main_py, 0o755)
        except Exception as e:
            print_warning(f"Could not set permissions on main.py: {e}")
    
    # Make pdth.pem readable only by owner
    pdth_pem = script_dir / "pdth.pem"
    if pdth_pem.exists():
        try:
            os.chmod(pdth_pem, 0o600)
            print_success("SSH key permissions set (600)")
        except Exception as e:
            print_warning(f"Could not set permissions on pdth.pem: {e}")


def load_env_file():
    """Load .env file if exists"""
    script_dir = Path(__file__).parent
    env_file = script_dir / ".env"
    
    if env_file.exists():
        print_success(f"Loading environment from .env file...")
        
        # Use python-dotenv to load .env
        try:
            from dotenv import load_dotenv
            load_dotenv(env_file, override=True)
            return True
        except ImportError:
            # Fallback: manual parsing (simple version)
            print_warning("python-dotenv not found, using basic .env parsing")
            try:
                with open(env_file, 'r', encoding='utf-8') as f:
                    for line in f:
                        line = line.strip()
                        if line and not line.startswith('#') and '=' in line:
                            key, value = line.split('=', 1)
                            # Remove quotes if present
                            value = value.strip().strip('"').strip("'")
                            os.environ[key.strip()] = value
                return True
            except Exception as e:
                print_error(f"Error loading .env: {e}")
                return False
    else:
        print_warning(".env file not found - using environment variables only")
        print_info("Create .env file from .env.example for easier configuration")
        return False


def load_ssh_key():
    """Auto-load SSH key from pdth.pem if SSH_PRIVATE_KEY not set"""
    if os.getenv("SSH_PRIVATE_KEY"):
        print_success("SSH_PRIVATE_KEY already set (from environment)")
        return True
    
    script_dir = Path(__file__).parent
    pdth_pem = script_dir / "pdth.pem"
    
    if pdth_pem.exists():
        try:
            ssh_key_content = pdth_pem.read_text(encoding='utf-8')
            # Validate it looks like a key
            if "BEGIN" in ssh_key_content and "PRIVATE KEY" in ssh_key_content:
            os.environ["SSH_PRIVATE_KEY"] = ssh_key_content
            print_success("Auto-loaded SSH key from pdth.pem")
            return True
            else:
                print_error("pdth.pem does not appear to be a valid SSH private key")
                return False
        except Exception as e:
            print_error(f"Error reading pdth.pem: {e}")
            return False
    else:
        print_warning("pdth.pem not found and SSH_PRIVATE_KEY not set")
        print_info("SSH key is required for deployment")
        return False


def validate_required_env():
    """Validate required environment variables"""
    server_ip = os.getenv("SERVER_IP")
    ssh_key = os.getenv("SSH_PRIVATE_KEY")
    
    errors = []
    
    if not server_ip:
        errors.append("SERVER_IP is required (set in .env file or export)")
    else:
        print_success(f"SERVER_IP: {server_ip}")
    
    if not ssh_key:
        errors.append("SSH_PRIVATE_KEY is required (set pdth.pem file or export)")
    else:
        print_success("SSH_PRIVATE_KEY: Set")
    
    if errors:
        print_error("Missing required configuration:")
        for error in errors:
            print_colored(f"  - {error}", Colors.RED)
        print()
        print_info("Quick fix:")
        print_info("  1. Create .env file: cp .env.example .env")
        print_info("  2. Edit .env and set SERVER_IP=your-server-ip")
        print_info("  3. Place pdth.pem file in infra/ directory")
        return False
    
    return True


def main():
    """Main entry point"""
    print_colored("🚀 Dagger CI/CD Deployment Script", Colors.BOLD + Colors.CYAN)
    print_colored("=" * 50, Colors.CYAN)
    print()
    
    # Detect OS
    system = platform.system()
    print_info(f"Platform: {system} {platform.machine()}")
    print()
    
    # Step 1: Setup permissions (Unix only)
    print_colored("📋 Step 1: Setting up permissions...", Colors.BOLD)
    setup_permissions()
    print()
    
    # Step 2: Check Python dependencies
    print_colored("📋 Step 2: Checking Python dependencies...", Colors.BOLD)
    if not install_python_dependencies():
        print_error("Failed to install Python dependencies")
        sys.exit(1)
    print()
    
    # Step 3: Check Dagger CLI
    print_colored("📋 Step 3: Checking Dagger CLI...", Colors.BOLD)
    if not check_dagger_cli():
        print_error("Dagger CLI is required")
        sys.exit(1)
    print()
    
    # Step 4: Load .env file
    print_colored("📋 Step 4: Loading environment configuration...", Colors.BOLD)
    load_env_file()
    print()
    
    # Step 5: Load SSH key
    print_colored("📋 Step 5: Loading SSH key...", Colors.BOLD)
    load_ssh_key()
    print()
    
    # Step 6: Validate required env vars
    print_colored("📋 Step 6: Validating configuration...", Colors.BOLD)
    if not validate_required_env():
        sys.exit(1)
    print()
    
    # Step 7: Run main.py
    script_dir = Path(__file__).parent
    main_py = script_dir / "main.py"
    
    if not main_py.exists():
        print_error(f"main.py not found in {script_dir}")
        sys.exit(1)
    
    # Get action from command line args
    action = sys.argv[1] if len(sys.argv) > 1 else "deploy"
    
    print_colored(f"🚀 Running: python main.py {action}", Colors.BOLD + Colors.GREEN)
    print_colored("=" * 50, Colors.CYAN)
    print()
    
    # Run main.py with remaining arguments
    try:
        result = subprocess.run(
            [sys.executable, str(main_py)] + sys.argv[1:],
            cwd=str(script_dir),
            check=False
        )
        sys.exit(result.returncode)
    except KeyboardInterrupt:
        print()
        print_warning("Interrupted by user")
        sys.exit(130)
    except Exception as e:
        print_error(f"Error running main.py: {e}")
        sys.exit(1)


if __name__ == "__main__":
    main()

