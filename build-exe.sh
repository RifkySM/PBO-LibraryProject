#!/bin/bash

echo "====================================="
echo "Building Library Management System"
echo "====================================="

# Step 1: Clean and build JAR
echo ""
echo "Step 1: Building JAR file..."
mvn clean package

if [ $? -ne 0 ]; then
    echo "Error: Maven build failed!"
    exit 1
fi

# Step 2: Create .exe using jpackage
echo ""
echo "Step 2: Creating Windows installer..."

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

if [ $? -ne 0 ]; then
    echo "Error: jpackage failed!"
    echo "Make sure you're using JDK 14 or higher"
    exit 1
fi

echo ""
echo "====================================="
echo "Build completed successfully!"
echo "Your installer is: LibraryManagementSystem-1.0.exe"
echo "====================================="
