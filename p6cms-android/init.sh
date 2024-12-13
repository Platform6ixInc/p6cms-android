#!/bin/bash

# Script to rename Android package from ai.offside.fr8proapp to com.platform6ix.p6cms
# Run this script from the root of your Android project

OLD_PACKAGE="ai.offside.fr8proapp"
NEW_PACKAGE="com.platform6ix.p6cms"

OLD_PATH="ai/offside/fr8proapp"
NEW_PATH="com/platform6ix/p6cms"

echo "Starting package rename process..."

# 1. Update build.gradle files
find . -name "build.gradle" -type f -exec sed -i '' \
    -e "s/namespace '$OLD_PACKAGE'/namespace '$NEW_PACKAGE'/g" \
    -e "s/applicationId \"$OLD_PACKAGE\"/applicationId \"$NEW_PACKAGE\"/g" {} +

# 2. Update all import statements and package declarations in Kotlin/Java files
find . -type f \( -name "*.kt" -o -name "*.java" \) -exec sed -i '' \
    -e "s/package $OLD_PACKAGE/package $NEW_PACKAGE/g" \
    -e "s/import $OLD_PACKAGE/import $NEW_PACKAGE/g" {} +

# 3. Update AndroidManifest.xml
find . -name "AndroidManifest.xml" -type f -exec sed -i '' \
    -e "s/package=\"$OLD_PACKAGE\"/package=\"$NEW_PACKAGE\"/g" \
    -e "s/android:authorities=\"$OLD_PACKAGE\"/android:authorities=\"$NEW_PACKAGE\"/g" {} +

# 4. Create new directory structure
mkdir -p "app/src/main/java/$NEW_PATH"
mkdir -p "app/src/androidTest/java/$NEW_PATH"
mkdir -p "app/src/test/java/$NEW_PATH"

# 5. Move files to new package structure
if [ -d "app/src/main/java/$OLD_PATH" ]; then
    mv "app/src/main/java/$OLD_PATH"/* "app/src/main/java/$NEW_PATH/"
    rm -r "app/src/main/java/ai"
fi

if [ -d "app/src/androidTest/java/$OLD_PATH" ]; then
    mv "app/src/androidTest/java/$OLD_PATH"/* "app/src/androidTest/java/$NEW_PATH/"
    rm -r "app/src/androidTest/java/ai"
fi

if [ -d "app/src/test/java/$OLD_PATH" ]; then
    mv "app/src/test/java/$OLD_PATH"/* "app/src/test/java/$NEW_PATH/"
    rm -r "app/src/test/java/ai"
fi

# 6. Update settings.gradle if needed
find . -name "settings.gradle" -type f -exec sed -i '' \
    -e "s/$OLD_PACKAGE/$NEW_PACKAGE/g" {} +

# 7. Update any resource files that might contain the package name
find . -path "*/res/*" -type f -exec sed -i '' \
    -e "s/$OLD_PACKAGE/$NEW_PACKAGE/g" {} +

echo "Package rename completed!"
echo "Please rebuild your project and verify all changes."
echo "Note: You may need to sync your project with Gradle files in Android Studio."
