#!/bin/bash

# Script to rename Android package and project names
# Run this script from the root of your Android project

update_package_name() {
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
}

update_project_name() {
    echo -e "\nRoot Project Name Replacement"
    echo "Please enter the existing root project name (e.g., FR8ProApp):"
    read OLD_ROOT_PROJECT
    echo "Please enter the new root project name (e.g., Platform6ixCMS):"
    read NEW_ROOT_PROJECT

    echo "Starting root project name replacement..."

    # Update settings.gradle and other build files
    find . -name "settings.gradle" -o -name "*.gradle" -type f -exec sed -i '' \
        -e "s/rootProject.name = ['\"']$OLD_ROOT_PROJECT['\"']/rootProject.name = '$NEW_ROOT_PROJECT'/g" \
        -e "s/project(':$OLD_ROOT_PROJECT')/project(':$NEW_ROOT_PROJECT')/g" {} +

    # Update any other potential references in build configuration files
    find . -name "*.properties" -o -name "*.xml" -o -name "*.gradle" -type f -exec sed -i '' \
        -e "s/$OLD_ROOT_PROJECT/$NEW_ROOT_PROJECT/g" {} +

    echo "Root project name replacement completed!"
    echo "Please rebuild your project and verify all changes."
}

# Main menu
echo "Welcome to InitAI"
echo "================="
echo "Please select an option:"
echo "1. Update package name"
echo "2. Update project name"
echo "3. Exit"

read -p "Enter your choice (1-3): " choice

case $choice in
    1)
        # Get package names from user input
        read -p "Enter the old package name (e.g., ai.offside.fr8proapp): " OLD_PACKAGE
        read -p "Enter the new package name (e.g., com.platform6ix.p6cms): " NEW_PACKAGE
        
        # Convert package names to paths
        OLD_PATH=$(echo $OLD_PACKAGE | sed 's/\./\//g')
        NEW_PATH=$(echo $NEW_PACKAGE | sed 's/\./\//g')
        
        # Call update_package_name with the new values
        update_package_name
        ;;
    2)
        update_project_name
        ;;
    3)
        echo "Exiting..."
        exit 0
        ;;
    *)
        echo "Invalid choice"
        exit 1
        ;;
esac
