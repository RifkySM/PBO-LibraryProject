# 📚 Library Management System

A modern, cross-platform library management application built with JavaFX. Features a clean white and green UI with a collapsible sidebar navigation.

## ✨ Features

### 📊 Dashboard
- Real-time statistics overview
- Total books, members, and loans
- Quick action buttons
- Recent activity tracking

### 👥 Member Management
- Add, edit, and delete members
- Track member details (name, email, phone, join date)
- Active/inactive status management
- Search and filter capabilities

### 📖 Book Management
- Complete book inventory system
- ISBN, publisher, and year tracking
- Stock management
- Availability status
- Search functionality

### 📋 Loan Management
- Create and process loans
- Track loan dates and due dates
- Automatic overdue detection
- Return processing with stock updates
- Loan history

### 💾 Data Persistence
- CSV file storage
- Automatic data saving
- Portable data files
- Easy backup and migration

### 🎨 User Interface
- Modern, clean design
- Collapsible sidebar navigation
- Responsive layout
- White and green color scheme
- Intuitive controls

## 🖥️ Screenshots

*Add screenshots here*

## 🚀 Getting Started

### Prerequisites
- Java JDK 14 or higher (JDK 25 recommended)
- Maven 3.6+

### Installation

#### Option 1: Run from Source (Development)
```bash
# Clone the repository
git clone https://github.com/yourusername/library-management-system.git
cd library-management-system

# Run with Maven
mvn clean javafx:run
```

#### Option 2: Run JAR File
```bash
# Build the JAR
mvn clean package

# Run the application
java -jar target/PBOLibraryProject-1.0-SNAPSHOT.jar
```

#### Option 3: Use the Run Script
```bash
# Make script executable
chmod +x run.sh

# Run
./run.sh
```

### Building Executables

#### For Windows (.exe)
```bash
# On Windows
build-exe.bat

# On Linux (cross-compile)
./build-exe.sh
```

#### For Linux
```bash
# Build native package
./build-linux.sh

# Choose:
# 1) RPM (Fedora/RHEL)
# 2) DEB (Ubuntu/Debian)
# 3) App Image (portable)
```

See [BUILD_EXE_GUIDE.md](BUILD_EXE_GUIDE.md) and [BUILD_LINUX_GUIDE.md](BUILD_LINUX_GUIDE.md) for detailed instructions.

## 📁 Project Structure

```
src/main/java/com/example/pbolibraryproject/
├── controllers/          # UI controllers
│   ├── MainController.java
│   ├── DashboardController.java
│   ├── MemberListController.java
│   ├── BookListController.java
│   └── LoanListController.java
├── models/              # Data models
│   ├── Person.java
│   ├── Member.java
│   ├── Book.java
│   ├── Loan.java
│   └── TransactionProcess.java
├── service/             # Business logic
│   ├── MemberService.java
│   ├── BookService.java
│   └── LoanService.java
└── util/               # Utilities
    └── CSVUtil.java

src/main/resources/com/example/pbolibraryproject/
├── main-view.fxml      # Main layout
├── dashboard.fxml      # Dashboard view
├── member-list.fxml    # Members view
├── book-list.fxml      # Books view
├── loan-list.fxml      # Loans view
└── styles.css          # Global styles
```

## 💾 Data Storage

The application stores data in CSV files located in the `data/` directory:
- `members.csv` - Member information
- `books.csv` - Book inventory
- `loans.csv` - Loan records

The `data/` folder is created automatically on first run.

## 🛠️ Technologies Used

- **Java 25** - Programming language
- **JavaFX 21** - UI framework
- **Maven** - Build tool
- **CSS** - Styling
- **CSV** - Data storage

### Dependencies
- javafx-controls
- javafx-fxml
- javafx-web
- controlsfx
- formsfx
- validatorfx
- bootstrapfx
- tilesfx

## 🎯 Use Cases

- School libraries
- University libraries
- Community libraries
- Small bookstores
- Personal book collections

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.


## 🔮 Future Enhancements

- [ ] Database support (MySQL, PostgreSQL)
- [ ] Email notifications for overdue books
- [ ] Book reservation system
- [ ] Barcode scanning
- [ ] Report generation (PDF, Excel)
- [ ] Multi-user support with authentication
- [ ] Book categories and tags
- [ ] Fine calculation for overdue books
- [ ] Book recommendations

## 🙏 Acknowledgments

- JavaFX community
- ControlsFX library
- Maven project
