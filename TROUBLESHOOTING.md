# Troubleshooting Guide

## Lombok Annotation Processing Issues

If you're seeing errors like "cannot find symbol: method builder()" or "cannot find symbol: method getXxx()", it means Lombok annotation processor is not running.

### Solution 1: Enable Annotation Processing in IntelliJ IDEA

1. Go to **File → Settings** (or **IntelliJ IDEA → Preferences** on Mac)
2. Navigate to **Build, Execution, Deployment → Compiler → Annotation Processors**
3. Check **Enable annotation processing**
4. Click **Apply** and **OK**
5. **File → Invalidate Caches / Restart → Invalidate and Restart**

### Solution 2: Install Lombok Plugin

1. Go to **File → Settings → Plugins**
2. Search for **"Lombok"**
3. Install the **Lombok** plugin
4. Restart IntelliJ IDEA

### Solution 3: Use Gradle Build (Recommended)

1. Go to **File → Settings → Build, Execution, Deployment → Build Tools → Gradle**
2. Under **Build and run using**, select **Gradle**
3. Under **Run tests using**, select **Gradle**
4. Click **Apply** and **OK**
5. Build the project using Gradle: **Build → Build Project** or run `./gradlew build`

### Solution 4: Clean and Rebuild

```bash
# In terminal, from the be/ directory
./gradlew clean build
```

Or in IntelliJ IDEA:
- **Build → Clean Project**
- **Build → Rebuild Project**

### Service Class Name Conflict

The `Service` entity class conflicts with Spring's `@Service` annotation. We've fixed this by using fully qualified names in the affected files:

- `ServiceApplicationService.java` - uses `@org.springframework.stereotype.Service` and fully qualified `com.example.app.services.domain.Service`

If you see ambiguous reference errors, ensure you're using the fully qualified class name.

## Common Build Errors

### "reference to Service is ambiguous"

This happens because both `Service` entity and `@Service` annotation exist. The code uses fully qualified names to resolve this. If you still see this error, rebuild the project.

### "cannot find symbol: method builder()"

Lombok annotation processor is not running. Follow Solution 1, 2, or 3 above.

### "cannot find symbol: method getXxx()"

Same as above - Lombok is not generating getters/setters. Enable annotation processing.

## Verification

After applying fixes, verify Lombok is working:

1. Open any entity class (e.g., `User.java`)
2. Place cursor on a field
3. Press **Alt+Enter** (or **Option+Enter** on Mac)
4. You should see Lombok suggestions like "Add getter" or "Add setter"
5. If you don't see these, Lombok plugin is not installed/enabled

## Build Configuration

The `build.gradle.kts` is correctly configured with:
- Lombok as `compileOnly` and `annotationProcessor`
- MapStruct with `lombok-mapstruct-binding` for compatibility
- Proper annotation processor configuration

If issues persist, ensure:
1. Gradle version is 8.5+ (check `gradle/wrapper/gradle-wrapper.properties`)
2. Java version is 17+ (`java -version`)
3. IntelliJ IDEA is using Gradle build system (not IntelliJ's built-in)


