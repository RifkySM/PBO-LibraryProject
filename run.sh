#!/bin/bash

# Simple launcher script for Library Management System
# This runs the application directly without packaging

echo "Starting Library Management System..."

# Check if JAR exists
if [ ! -f "target/PBOLibraryProject-1.0-SNAPSHOT.jar" ]; then
    echo "JAR file not found. Building project..."
    mvn clean package

    if [ $? -ne 0 ]; then
        echo "Build failed!"
        exit 1
    fi
fi

# Run the application
java -jar target/PBOLibraryProject-1.0-SNAPSHOT.jar
