#!/bin/bash

echo "====================================="
echo "Building Library Management System"
echo "For Linux (Fedora)"
echo "====================================="

# Step 1: Clean and build JAR
echo ""
echo "Step 1: Building JAR file..."
mvn clean package

if [ $? -ne 0 ]; then
    echo "Error: Maven build failed!"
    exit 1
fi

# Step 2: Create Linux package using jpackage
echo ""
echo "Step 2: Creating Linux package..."
echo "Choose package type:"
echo "1) RPM (for Fedora/RHEL)"
echo "2) DEB (for Ubuntu/Debian)"
echo "3) App Image (portable, works on all distros)"
echo ""
read -p "Enter choice (1-3): " choice

case $choice in
    1)
        PACKAGE_TYPE="rpm"
        echo "Creating RPM package for Fedora..."
        ;;
    2)
        PACKAGE_TYPE="deb"
        echo "Creating DEB package..."
        ;;
    3)
        PACKAGE_TYPE="app-image"
        echo "Creating portable App Image..."
        ;;
    *)
        echo "Invalid choice, defaulting to app-image"
        PACKAGE_TYPE="app-image"
        ;;
esac

jpackage \
  --input target \
  --name "LibraryManagementSystem" \
  --main-jar PBOLibraryProject-1.0-SNAPSHOT.jar \
  --main-class com.example.pbolibraryproject.Launcher \
  --type $PACKAGE_TYPE \
  --app-version 1.0 \
  --vendor "PBO Library Project" \
  --description "Library Management System with JavaFX" \
  --linux-shortcut

if [ $? -ne 0 ]; then
    echo "Error: jpackage failed!"
    echo "Make sure you're using JDK 14 or higher"
    exit 1
fi

echo ""
echo "====================================="
echo "Build completed successfully!"
if [ "$PACKAGE_TYPE" = "rpm" ]; then
    echo "Your package is: LibraryManagementSystem-1.0-1.x86_64.rpm"
    echo ""
    echo "To install, run:"
    echo "sudo rpm -i LibraryManagementSystem-1.0-1.x86_64.rpm"
elif [ "$PACKAGE_TYPE" = "deb" ]; then
    echo "Your package is: librarymanagement system_1.0-1_amd64.deb"
    echo ""
    echo "To install, run:"
    echo "sudo dpkg -i librarymanagementsystem_1.0-1_amd64.deb"
else
    echo "Your app is in: LibraryManagementSystem/"
    echo ""
    echo "To run, execute:"
    echo "./LibraryManagementSystem/bin/LibraryManagementSystem"
fi
echo "====================================="
