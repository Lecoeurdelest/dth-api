#!/usr/bin/env python3
"""
Dagger CI/CD Pipeline for DTH Backend
Simplified version with deployment support
"""

import os
import sys
from pathlib import Path
from typing import Optional

import dagger
from dotenv import load_dotenv

# Load environment variables from .env file
# Tự động tìm .env trong thư mục infra/ hoặc parent directory
script_dir = Path(__file__).parent
env_file = script_dir / ".env"
if env_file.exists():
    load_dotenv(env_file)
else:
    # Try parent directory
    load_dotenv(script_dir.parent / ".env")

# Auto-load SSH key from pdth.pem if SSH_PRIVATE_KEY not set
if not os.getenv("SSH_PRIVATE_KEY"):
    pdth_pem = script_dir / "pdth.pem"
    if pdth_pem.exists():
        os.environ["SSH_PRIVATE_KEY"] = pdth_pem.read_text()


async def build(client: dagger.Client) -> dagger.File:
    """Build Spring Boot application and return JAR file"""
    print("🔨 Building application...")
    
    # Get configuration from environment variables
    gradle_version = os.getenv("GRADLE_VERSION", "8.5")
    java_version = os.getenv("JAVA_VERSION", "17")
    build_profile = os.getenv("BUILD_PROFILE", "prod")
    skip_tests = os.getenv("SKIP_TESTS", "true").lower() == "true"
    
    # Get source code - use relative path from infra/ directory (parent = project root)
    # Since script runs from infra/, ".." points to project root
    source = client.host().directory(
        "..",
        exclude=[".git", "build", ".gradle", ".idea", "*.iml", "node_modules", "infra"]
    )
    
    # Build with Gradle
    build_cmd = ["gradle", "clean", "build", "--no-daemon"]
    if skip_tests:
        build_cmd.append("-x")
        build_cmd.append("test")
    
    builder = (
        client.container()
        .from_(f"gradle:{gradle_version}-jdk{java_version}")
        .with_mounted_directory("/app", source)
        .with_workdir("/app")
        .with_mounted_cache(
            "/root/.gradle/caches",
            client.cache_volume("gradle-cache")
        )
        .with_env_variable("SPRING_PROFILES_ACTIVE", build_profile)
        .with_exec(build_cmd)
    )
    
    # Extract JAR file
    jar_filename = os.getenv("JAR_FILENAME", "app-0.0.1-SNAPSHOT.jar")
    jar_file = builder.file(f"build/libs/{jar_filename}")
    
    print("✅ Build completed")
    return jar_file


async def deploy_jar(
    client: dagger.Client,
    jar_file: dagger.File
) -> None:
    """Deploy JAR file to EC2 server via SSH"""
    # Get configuration from environment variables
    server_ip = os.getenv("SERVER_IP")
    server_user = os.getenv("SERVER_USER", "ec2-user")
    deploy_path = os.getenv("DEPLOY_PATH", "/opt/dth/dth-api")
    ssh_key_path = os.getenv("SSH_KEY_PATH", "pdth.pem")
    jar_filename = os.getenv("JAR_FILENAME", "app-0.0.1-SNAPSHOT.jar")
    
    if not server_ip:
        raise ValueError(
            "SERVER_IP environment variable is required. "
            "Set it in .env file or export SERVER_IP=your-server-ip"
        )
    
    print(f"🚀 Deploying to {server_user}@{server_ip}...")
    
    # Read SSH key content from environment variable or file
    ssh_key_content = os.getenv("SSH_PRIVATE_KEY")
    if not ssh_key_content:
        # Try to read from file
        script_dir = Path(__file__).parent
        ssh_key_file = script_dir / ssh_key_path
        if ssh_key_file.exists():
            ssh_key_content = ssh_key_file.read_text()
        else:
            # Try absolute path
            if os.path.exists(ssh_key_path):
                ssh_key_content = Path(ssh_key_path).read_text()
    
    if not ssh_key_content:
        raise ValueError(
            f"SSH key not found. Set SSH_PRIVATE_KEY env var or place key file at {ssh_key_path}"
        )
    
    # Create container with SSH tools
    deployer = (
        client.container()
        .from_("alpine:latest")
        .with_exec(["apk", "add", "--no-cache", "openssh-client", "openssh"])
        .with_new_file("/root/.ssh/id_rsa", contents=ssh_key_content, permissions=0o600)
        .with_file("/app/app.jar", jar_file)
    )
    
    # Create directory structure on server
    deployer = deployer.with_exec([
        "sh", "-c",
        f"ssh -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null "
        f"{server_user}@{server_ip} "
        f"'sudo mkdir -p {deploy_path}/build/libs && sudo chown -R {server_user}:{server_user} {deploy_path}'"
    ])
    
    # Copy JAR to server
    remote_jar_path = f"{deploy_path}/build/libs/{jar_filename}"
    deployer = deployer.with_exec([
        "sh", "-c",
        f"scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null "
        f"/app/app.jar {server_user}@{server_ip}:{remote_jar_path}"
    ])
    
    # Get service name from env or use default
    service_name = os.getenv("SERVICE_NAME", "dth-api")
    
    # Restart service on server
    deployer = deployer.with_exec([
        "sh", "-c",
        f"ssh -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null "
        f"{server_user}@{server_ip} "
        f"'sudo systemctl restart {service_name} || sudo systemctl start {service_name}'"
    ])
    
    await deployer.stdout()
    print("✅ Deployment completed")


async def main():
    """Main pipeline"""
    # Initialize Dagger connection
    async with dagger.Connection() as client:
        action = sys.argv[1] if len(sys.argv) > 1 else "build"
        
        if action == "build":
            jar_file = await build(client)
            print(f"✅ JAR file ready: {jar_file}")
            
            # Save JAR to local if requested
            if os.getenv("SAVE_JAR", "false").lower() == "true":
                output_path = os.getenv("JAR_OUTPUT_PATH", "build/output/app.jar")
                os.makedirs(os.path.dirname(output_path), exist_ok=True)
                jar_content = await jar_file.contents()
                with open(output_path, "wb") as f:
                    f.write(jar_content)
                print(f"📁 Saved to: {output_path}")
        
        elif action == "deploy":
            # Build first
            jar_file = await build(client)
            
            # Deploy (all config comes from environment variables)
            await deploy_jar(client, jar_file)
        
        else:
            print(f"Unknown action: {action}")
            print("Available: build, deploy")
            sys.exit(1)


if __name__ == "__main__":
    from pathlib import Path
    import asyncio
    asyncio.run(main())

