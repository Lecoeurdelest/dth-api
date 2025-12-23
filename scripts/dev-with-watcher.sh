#!/bin/bash
# Start dev mode with auto-compile watcher in background
# This provides seamless hot reload experience

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_ROOT="$( cd "$SCRIPT_DIR/.." && pwd )"
cd "$PROJECT_ROOT"

echo "🚀 Starting development mode with auto-compile watcher..."
echo ""
# Note: MariaDB is already started by 'make dev'

# Start auto-compile watcher in background
echo "🔍 Starting auto-compile watcher (background)..."
bash "$SCRIPT_DIR/watch-and-compile.sh" > /tmp/dth-compile.log 2>&1 &
WATCHER_PID=$!

# Wait a moment for initial compile
sleep 2

# Find Java 17
JAVA17_HOME=""
if [ -f "/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home/bin/java" ]; then
    JAVA17_HOME="/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"
elif [ -f "/usr/local/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home/bin/java" ]; then
    JAVA17_HOME="/usr/local/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"
fi

# Start Spring Boot
echo "🚀 Starting Spring Boot (profile: dev)..."
echo "   Hot reload: Save file -> Auto compile -> DevTools restart (~2-5s)"
echo "   Compile logs: tail -f /tmp/dth-compile.log"
echo "   Stop watcher: kill $WATCHER_PID"
echo ""

# Cleanup function
cleanup() {
    echo ""
    echo "🛑 Stopping..."
    kill $WATCHER_PID 2>/dev/null || true
    exit 0
}

trap cleanup INT TERM

# Start bootRun
if [ -n "$JAVA17_HOME" ]; then
    SPRING_PROFILES_ACTIVE=dev JAVA_HOME="$JAVA17_HOME" ./gradlew bootRun
else
    SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun
fi

