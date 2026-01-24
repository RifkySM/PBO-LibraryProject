# How to Build and Run on Linux (Fedora)

## Quick Start - Just Run It!

The easiest way to run on Linux:

```bash
./run.sh
```

This will build (if needed) and run the application directly.

---

## Method 1: Build RPM Package for Fedora (Recommended)

### Step 1: Run the build script
```bash
./build-linux.sh
```

Select option **1** for RPM package.

### Step 2: Install the RPM
```bash
sudo rpm -i LibraryManagementSystem-1.0-1.x86_64.rpm
```

### Step 3: Run from Applications Menu
You can now find "Library Management System" in your applications menu, or run:
```bash
LibraryManagementSystem
```

### To Uninstall:
```bash
sudo rpm -e LibraryManagementSystem
```

---

## Method 2: Build Portable App Image (No Installation)

### Step 1: Run the build script
```bash
./build-linux.sh
```

Select option **3** for App Image.

### Step 2: Run the application
```bash
./LibraryManagementSystem/bin/LibraryManagementSystem
```

You can move the entire `LibraryManagementSystem/` folder anywhere and it will still work!

---

## Method 3: Run JAR Directly (Requires Java)

### Build the JAR:
```bash
mvn clean package
```

### Run:
```bash
java -jar target/PBOLibraryProject-1.0-SNAPSHOT.jar
```

Or use the provided script:
```bash
./run.sh
```

---

## Method 4: Create Desktop Shortcut

Create a `.desktop` file for easy launching:

```bash
cat > ~/.local/share/applications/library-management.desktop << EOF
[Desktop Entry]
Type=Application
Name=Library Management System
Comment=Manage library books, members, and loans
Exec=/home/lordrevan/IdeaProjects/PBOLibraryProject/run.sh
Icon=applications-office
Terminal=false
Categories=Office;Education;
EOF
```

Now you can launch it from your applications menu!

---

## Cross-Platform Distribution

### For Windows Users:
Build on Linux:
```bash
./build-exe.sh  # Creates Windows .exe (requires wine or cross-compile setup)
```

Or build on Windows:
```bash
build-exe.bat
```

### For Linux Users:
```bash
./build-linux.sh  # Choose RPM, DEB, or App Image
```

### For All Platforms (Universal JAR):
```bash
mvn clean package
```
Distribute `target/PBOLibraryProject-1.0-SNAPSHOT.jar`

---

## System Requirements

- **Java**: JDK 14 or higher (you have JDK 25 ✓)
- **RAM**: 512 MB minimum, 1 GB recommended
- **Disk Space**: ~200 MB for packaged app, ~50 MB for JAR
- **Display**: 1024x768 minimum resolution

---

## Data Storage

The application creates a `data/` folder in the directory where it runs:
- **RPM Install**: `~/.local/share/LibraryManagementSystem/data/`
- **JAR/Script**: `./data/` (in project directory)
- **Portable**: `./data/` (next to the executable)

This folder contains:
- `members.csv`
- `books.csv`
- `loans.csv`

---

## Troubleshooting

### "jpackage: command not found"
Make sure you're using JDK 14+:
```bash
java --version
jpackage --version
```

### "JavaFX runtime components are missing"
Your JDK might not include JavaFX. Use the JAR method instead:
```bash
./run.sh
```

### Maven build fails
Update Maven dependencies:
```bash
mvn clean install -U
```

### Permission denied
Make scripts executable:
```bash
chmod +x build-linux.sh run.sh
```

---

## Development Mode

For development, you can run directly with Maven:
```bash
mvn clean javafx:run
```

Or from IntelliJ IDEA:
- Right-click on `Launcher.java`
- Select "Run 'Launcher.main()'"
