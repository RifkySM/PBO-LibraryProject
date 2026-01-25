#!/bin/bash

echo "====================================="
echo "Building Library Management System"
echo "====================================="

# Detect OS
OS_TYPE=$(uname -s)
echo "Detected OS: $OS_TYPE"

# Step 1: Clean and build JAR
echo ""
echo "Step 1: Building JAR file..."
mvn clean package

if [ $? -ne 0 ]; then
    echo "Error: Maven build failed!"
    exit 1
fi

# Step 2: Create installer using jpackage (platform-specific)
echo ""
echo "Step 2: Creating installer for $OS_TYPE..."

if [[ "$OS_TYPE" == "MINGW"* ]] || [[ "$OS_TYPE" == "MSYS"* ]] || [[ "$OS_TYPE" == "CYGWIN"* ]]; then
    # Windows platform
    echo "Building Windows installer..."
    jpackage \
      --input target \
      --name "LibraryManagementSystem" \
      --main-jar PBOLibraryProject-1.0-SNAPSHOT.jar \
      --main-class com.example.pbolibraryproject.Launcher \
      --type exe \
      --app-version 1.0 \
      --vendor "PBO Library Project" \
      --win-dir-chooser \
      --win-menu \
      --win-shortcut \
      --description "Library Management System with JavaFX"

    INSTALLER_FILE="LibraryManagementSystem-1.0.exe"
else
    # Linux platform
    echo "Building Linux RPM installer..."
    jpackage \
      --input target \
      --name "LibraryManagementSystem" \
      --main-jar PBOLibraryProject-1.0-SNAPSHOT.jar \
      --main-class com.example.pbolibraryproject.Launcher \
      --type rpm \
      --app-version 1.0 \
      --vendor "PBO Library Project" \
      --description "Library Management System with JavaFX" \
      --linux-menu-group "Office" \
      --linux-shortcut

    INSTALLER_FILE="librarymanagmentsystem-1.0-1.x86_64.rpm"
fi

if [ $? -ne 0 ]; then
    echo "Error: jpackage failed!"
    echo "Make sure you're using JDK 14 or higher"
    exit 1
fi

echo ""
echo "====================================="
echo "Build completed successfully!"
echo "Your installer is: $INSTALLER_FILE"
echo "====================================="
