#!/bin/bash

# Script to rename Android package and project names
# Run this script from the root of your Android project

# Set locale to UTF-8 to handle special characters
export LC_ALL=C
export LANG=C

# Enable error handling
set -e

update_package_name() {
    echo "Starting package rename process..."
    echo "Using OLD_PACKAGE: $OLD_PACKAGE"
    echo "Using NEW_PACKAGE: $NEW_PACKAGE"
    echo "Using OLD_PATH: $OLD_PATH"
    echo "Using NEW_PATH: $NEW_PATH"

    # Verify that the old package structure exists
    if [ ! -d "app/src/main/java/$OLD_PATH" ]; then
        echo "Error: Old package structure not found at app/src/main/java/$OLD_PATH"
        echo "Please verify the old package name is correct"
        exit 1
    fi

    # Create new directory structure first
    echo "Creating new directory structure..."
    mkdir -p "app/src/main/java/$NEW_PATH"
    mkdir -p "app/src/androidTest/java/$NEW_PATH" 2>/dev/null || true
    mkdir -p "app/src/test/java/$NEW_PATH" 2>/dev/null || true

    # 1. Update build.gradle files
    echo "Updating build.gradle files..."
    find . -name "build.gradle" -type f -exec sed -i '' \
        -e "s/namespace '$OLD_PACKAGE'/namespace '$NEW_PACKAGE'/g" \
        -e "s/applicationId \"$OLD_PACKAGE\"/applicationId \"$NEW_PACKAGE\"/g" {} \;

    # 2. Update all import statements and package declarations in Kotlin/Java files
    echo "Updating Kotlin/Java files..."
    find . -type f \( -name "*.kt" -o -name "*.java" \) -exec sed -i '' \
        -e "s/package $OLD_PACKAGE/package $NEW_PACKAGE/g" \
        -e "s/import $OLD_PACKAGE/import $NEW_PACKAGE/g" {} \;

    # 3. Update AndroidManifest.xml
    echo "Updating AndroidManifest.xml files..."
    find . -name "AndroidManifest.xml" -type f -exec sed -i '' \
        -e "s/package=\"$OLD_PACKAGE\"/package=\"$NEW_PACKAGE\"/g" \
        -e "s/android:authorities=\"$OLD_PACKAGE\"/android:authorities=\"$NEW_PACKAGE\"/g" {} \;

    # 4. Carefully move files to new package structure
    echo "Moving files to new package structure..."
    
    # Main source files
    if [ -d "app/src/main/java/$OLD_PATH" ]; then
        echo "Moving main source files..."
        # First copy files to ensure we don't lose anything
        cp -r "app/src/main/java/$OLD_PATH"/* "app/src/main/java/$NEW_PATH/"
        # Verify the copy was successful
        if [ $? -eq 0 ]; then
            # Only remove old files if copy was successful
            rm -r "app/src/main/java/$OLD_PATH"
            # Clean up empty directories but only if they're empty
            old_base_dir="app/src/main/java/${OLD_PATH%%/*}"
            find "$old_base_dir" -type d -empty -delete 2>/dev/null || true
        else
            echo "Error: Failed to copy main source files. Keeping original files intact."
            exit 1
        fi
    fi

    # Android test files
    if [ -d "app/src/androidTest/java/$OLD_PATH" ]; then
        echo "Moving androidTest files..."
        cp -r "app/src/androidTest/java/$OLD_PATH"/* "app/src/androidTest/java/$NEW_PATH/"
        if [ $? -eq 0 ]; then
            rm -r "app/src/androidTest/java/$OLD_PATH"
            old_base_dir="app/src/androidTest/java/${OLD_PATH%%/*}"
            find "$old_base_dir" -type d -empty -delete 2>/dev/null || true
        fi
    fi

    # Unit test files
    if [ -d "app/src/test/java/$OLD_PATH" ]; then
        echo "Moving test files..."
        cp -r "app/src/test/java/$OLD_PATH"/* "app/src/test/java/$NEW_PATH/"
        if [ $? -eq 0 ]; then
            rm -r "app/src/test/java/$OLD_PATH"
            old_base_dir="app/src/test/java/${OLD_PATH%%/*}"
            find "$old_base_dir" -type d -empty -delete 2>/dev/null || true
        fi
    fi

    echo "Package rename process completed successfully!"
    echo "New package structure is at: app/src/main/java/$NEW_PATH"
}

update_project_name() {
    echo "Project name update functionality not implemented yet"
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
        
        # Validate input
        if [ -z "$OLD_PACKAGE" ] || [ -z "$NEW_PACKAGE" ]; then
            echo "Error: Package names cannot be empty"
            exit 1
        fi
        
        # Convert package names to paths with explicit locale setting
        echo "Converting package names to paths..."
        OLD_PATH=$(printf "%s" "$OLD_PACKAGE" | LC_ALL=C sed 's/\./\//g')
        NEW_PATH=$(printf "%s" "$NEW_PACKAGE" | LC_ALL=C sed 's/\./\//g')
        
        # Verify the conversion
        echo "Converted paths:"
        echo "OLD_PATH: $OLD_PATH"
        echo "NEW_PATH: $NEW_PATH"
        
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
