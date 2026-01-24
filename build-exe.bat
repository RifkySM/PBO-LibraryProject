@echo off
echo =====================================
echo Building Library Management System
echo =====================================

REM Step 1: Clean and build JAR
echo.
echo Step 1: Building JAR file...
call mvn clean package

if %ERRORLEVEL% NEQ 0 (
    echo Error: Maven build failed!
    pause
    exit /b 1
)

REM Step 2: Create .exe using jpackage
echo.
echo Step 2: Creating Windows installer...

jpackage ^
  --input target ^
  --name "LibraryManagementSystem" ^
  --main-jar PBOLibraryProject-1.0-SNAPSHOT.jar ^
  --main-class com.example.pbolibraryproject.Launcher ^
  --type exe ^
  --app-version 1.0 ^
  --vendor "PBO Library Project" ^
  --win-dir-chooser ^
  --win-menu ^
  --win-shortcut ^
  --description "Library Management System with JavaFX"

if %ERRORLEVEL% NEQ 0 (
    echo Error: jpackage failed!
    echo Make sure you're using JDK 14 or higher
    pause
    exit /b 1
)

echo.
echo =====================================
echo Build completed successfully!
echo Your installer is: LibraryManagementSystem-1.0.exe
echo =====================================
pause
