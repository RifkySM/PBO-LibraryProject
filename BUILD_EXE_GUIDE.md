# How to Build Windows .exe for Library Management System

## Method 1: Using jpackage (Recommended - Creates Native Installer)

### Prerequisites
- JDK 14 or higher (you have JDK 25, so you're good!)
- Maven installed

### Steps:

#### 1. Build the JAR file
```bash
mvn clean package
```

This will create a JAR file in the `target/` directory.

#### 2. Create the .exe using jpackage
Run this command from your project root:

```bash
jpackage \
  --input target \
  --name "LibraryManagementSystem" \
  --main-jar PBOLibraryProject-1.0-SNAPSHOT.jar \
  --main-class com.example.pbolibraryproject.Launcher \
  --type exe \
  --icon src/main/resources/icon.ico \
  --app-version 1.0 \
  --vendor "Your Name" \
  --win-dir-chooser \
  --win-menu \
  --win-shortcut
```

**Note:** Remove the `--icon` line if you don't have an icon file.

#### 3. Find your .exe
The installer will be created in the project root directory as `LibraryManagementSystem-1.0.exe`

---

## Method 2: Using jlink + jpackage (Smaller Size)

This creates a custom JRE with only the modules you need.

### Step 1: Create custom runtime with jlink
```bash
jlink \
  --module-path "$JAVA_HOME/jmods:target/classes" \
  --add-modules com.example.pbolibraryproject,javafx.controls,javafx.fxml,javafx.web \
  --output target/runtime \
  --strip-debug \
  --no-header-files \
  --no-man-pages \
  --compress=2
```

### Step 2: Create .exe with jpackage
```bash
jpackage \
  --runtime-image target/runtime \
  --name "LibraryManagementSystem" \
  --input target \
  --main-jar PBOLibraryProject-1.0-SNAPSHOT.jar \
  --main-class com.example.pbolibraryproject.Launcher \
  --type exe \
  --app-version 1.0 \
  --vendor "Your Name" \
  --win-dir-chooser \
  --win-menu \
  --win-shortcut
```

---

## Method 3: Using launch4j (Alternative)

### Prerequisites
- Download launch4j from: http://launch4j.sourceforge.net/

### Steps:
1. Build JAR: `mvn clean package`
2. Open launch4j
3. Configure:
   - **Output file**: `LibraryManagementSystem.exe`
   - **Jar**: `target/PBOLibraryProject-1.0-SNAPSHOT.jar`
   - **Icon**: (optional) path to .ico file
   - **JRE minimum version**: 14
   - **Main class**: `com.example.pbolibraryproject.Launcher`
4. Click the gear icon to build

---

## Quick Command (Linux/Mac)

For Linux, you can use the `app-image` type instead of `exe`:

```bash
# Build JAR
mvn clean package

# Create executable
jpackage \
  --input target \
  --name "LibraryManagementSystem" \
  --main-jar PBOLibraryProject-1.0-SNAPSHOT.jar \
  --main-class com.example.pbolibraryproject.Launcher \
  --type app-image
```

---

## Important Notes:

1. **Data Directory**: The `data/` folder with CSV files will be created in the directory where the .exe runs
2. **FXML Resources**: All FXML and CSS files are packaged inside the JAR
3. **Distribution**: When distributing, you can either:
   - Use the installer (.exe) which bundles the JRE
   - Or distribute just the JAR and require users to have Java installed

## Troubleshooting:

If you get module errors, make sure `module-info.java` exports all necessary packages.

If jpackage command is not found:
```bash
# Check if jpackage is available
jpackage --version

# If not, make sure you're using JDK 14+
java --version
```
