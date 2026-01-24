# Git Guide for Library Management System

## Files to COMMIT (Push to Git)

### ✅ Source Code
```
src/
├── main/
│   ├── java/
│   │   ├── com/example/pbolibraryproject/
│   │   │   ├── controllers/
│   │   │   │   ├── MainController.java
│   │   │   │   ├── DashboardController.java
│   │   │   │   ├── MemberListController.java
│   │   │   │   ├── BookListController.java
│   │   │   │   └── LoanListController.java
│   │   │   ├── models/
│   │   │   │   ├── Person.java
│   │   │   │   ├── Member.java
│   │   │   │   ├── Book.java
│   │   │   │   ├── Loan.java
│   │   │   │   └── TransactionProcess.java
│   │   │   ├── service/
│   │   │   │   ├── MemberService.java
│   │   │   │   ├── BookService.java
│   │   │   │   └── LoanService.java
│   │   │   ├── util/
│   │   │   │   └── CSVUtil.java
│   │   │   ├── HelloApplication.java
│   │   │   ├── HelloController.java
│   │   │   └── Launcher.java
│   │   └── module-info.java
│   └── resources/
│       └── com/example/pbolibraryproject/
│           ├── main-view.fxml
│           ├── dashboard.fxml
│           ├── member-list.fxml
│           ├── book-list.fxml
│           ├── loan-list.fxml
│           ├── hello-view.fxml
│           └── styles.css
```

### ✅ Configuration Files
```
pom.xml
module-info.java
.gitignore
```

### ✅ Build Scripts
```
build-exe.bat
build-exe.sh
build-linux.sh
run.sh
```

### ✅ Documentation
```
README.md
BUILD_EXE_GUIDE.md
BUILD_LINUX_GUIDE.md
GIT_GUIDE.md (this file)
```

### ✅ Maven Wrapper (Optional)
```
.mvn/
mvnw
mvnw.cmd
```

---

## Files to IGNORE (Not Push to Git)

### ❌ Build Outputs
```
target/                    # Maven build directory
*.class                    # Compiled Java files
*.jar                      # JAR files (except maven-wrapper.jar)
*.war, *.ear              # Package files
```

### ❌ IDE Files
```
.idea/                     # IntelliJ IDEA settings
*.iml                      # IntelliJ module files
*.iws, *.ipr              # IntelliJ workspace files
out/                       # IDE output directory
```

### ❌ Application Data
```
data/                      # CSV data directory
├── members.csv           # User data
├── books.csv            # Book inventory
└── loans.csv            # Loan records
```

### ❌ Package Outputs
```
*.exe                      # Windows executable
*.msi                      # Windows installer
*.rpm                      # Fedora/RHEL package
*.deb                      # Debian/Ubuntu package
LibraryManagementSystem/   # jpackage app image folder
```

### ❌ Logs & Temporary Files
```
logs/
*.log
*.tmp
*.bak
*.swp
*~
```

### ❌ OS Files
```
.DS_Store                  # macOS
Thumbs.db                  # Windows
Desktop.ini                # Windows
```

---

## Before Your First Commit

### 1. Check Git Status
```bash
git status
```

This shows what will be committed.

### 2. Review Changed Files
```bash
git diff
```

### 3. Check if .gitignore is Working
```bash
# These should NOT appear in git status:
# - data/
# - target/
# - .idea/
# - *.class
```

---

## Initial Git Setup

### 1. Initialize Git (if not done)
```bash
cd /home/lordrevan/IdeaProjects/PBOLibraryProject
git init
```

### 2. Add All Files
```bash
git add .
```

### 3. Create First Commit
```bash
git commit -m "Initial commit: Library Management System with JavaFX

Features:
- Dashboard with statistics
- Member management (CRUD)
- Book management (CRUD)
- Loan management (CRUD)
- CSV data persistence
- Collapsible sidebar navigation
- White and green theme
- Cross-platform support (Windows/Linux)"
```

### 4. Add Remote Repository
```bash
git remote add origin https://github.com/yourusername/library-management-system.git
```

### 5. Push to GitHub
```bash
git branch -M main
git push -u origin main
```

---

## Creating a README for GitHub

Create a `README.md` with:
- Project description
- Screenshots
- Features list
- Installation instructions
- How to run
- Technologies used
- License

---

## Recommended .gitattributes

Create `.gitattributes` for consistent line endings:

```
# Auto detect text files and normalize line endings to LF
* text=auto

# Java source files
*.java text diff=java
*.xml text
*.properties text
*.fxml text
*.css text

# Scripts
*.sh text eol=lf
*.bat text eol=crlf

# Binary files
*.jar binary
*.exe binary
*.dll binary
*.so binary
*.png binary
*.jpg binary
*.jpeg binary
*.gif binary
*.ico binary
*.class binary
```

---

## Example Git Workflow

### Daily Development:
```bash
# Check status
git status

# Add changes
git add .

# Commit
git commit -m "Add feature: XYZ"

# Push
git push
```

### Before Pushing:
```bash
# Make sure you're not committing data/ or target/
git status

# If you see data/ or target/, they should be red (ignored)
# If they're green, add them to .gitignore
```

---

## What Users Will Clone

When someone clones your repository, they get:
- ✅ All source code
- ✅ Build scripts
- ✅ Documentation
- ❌ NO user data (data/ is ignored)
- ❌ NO build outputs (target/ is ignored)
- ❌ NO IDE settings (.idea/ is ignored)

They can then:
1. Run `mvn clean package` to build
2. Run `./run.sh` or `mvn javafx:run` to start
3. The app will create a fresh `data/` folder on first run

---

## File Size Check

Before pushing, check repository size:
```bash
du -sh .git
```

Should be under 10 MB for this project (just source code).

If it's larger, you might have accidentally committed:
- target/ directory
- data/ directory
- .jar files
- IDE files

To remove from git history:
```bash
git rm -r --cached target/
git rm -r --cached data/
git commit -m "Remove ignored files from git"
```
