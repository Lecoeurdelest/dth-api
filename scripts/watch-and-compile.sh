#!/bin/bash
# Auto-compile script for hot reload
# Watches source files and automatically compiles when they change
# This enables DevTools hot reload without manual compilation

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_ROOT="$( cd "$SCRIPT_DIR/.." && pwd )"
cd "$PROJECT_ROOT"

# Find Java 17
JAVA17_HOME=""
if [ -f "/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home/bin/java" ]; then
    JAVA17_HOME="/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"
elif [ -f "/usr/local/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home/bin/java" ]; then
    JAVA17_HOME="/usr/local/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"
fi

if [ -n "$JAVA17_HOME" ]; then
    export JAVA_HOME="$JAVA17_HOME"
fi

echo "🔍 Watching source files for changes..."
echo "   - Auto-compile on file save"
echo "   - DevTools will auto-restart app"
echo "   Press Ctrl+C to stop"
echo ""

# Use Gradle continuous build to watch and compile
if [ -f "./gradlew" ]; then
    JAVA_HOME="$JAVA17_HOME" ./gradlew build --continuous -x test
elif command -v gradle > /dev/null 2>&1; then
    JAVA_HOME="$JAVA17_HOME" gradle build --continuous -x test
else
    echo "❌ Error: gradlew or gradle not found!"
    exit 1
fi

