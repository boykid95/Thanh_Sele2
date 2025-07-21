# 🧪 Selenide Automation Framework

This project is an **Automation Test Framework** built with **Selenide** and **TestNG**, applying the **Page Object Model (POM)** design pattern and Java best practices.

---

## 📁 Project Structure

```
src/
├── main/
│   └── java/
│       ├── common.helpers/
│       │   └── ElementHelper.java
│       └── pages/
│           ├── HomePage.java
│           └── ShopPage.java
├── test/
│   └── java/
│       ├── base/
│       │   └── BaseTest.java
│       └── testcase/
│           └── TC_01_VerifyShopPage.java
```

---

## 🛠️ Technologies Used

- **Java 17**
- **Selenide** `6.19.1`
- **TestNG** `7.8.0`
- **Maven** `17`
- **Allure Report** (optional)

---

## ✅ Features

- Page Object Model
- Clean folder structure
- Element helper class
- TestNG suite execution via `testRun.xml`
- Maven lifecycle support

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/selenide-test-project.git
cd selenide-test-project
```

### 2. Run Tests with Maven

```bash
mvn clean test
```

### 3. Generate Allure Report (optional)

```bash
allure serve target/allure-results
```

---

## 📦 Prerequisites

Make sure the following are installed and configured:

- **Java 17+ (JDK)**  
  👉 [Download JDK](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- **Maven 17**  
  👉 [Download Maven](https://maven.apache.org/download.cgi)
- **IDE:** IntelliJ IDEA, Eclipse, or VS Code

---

## 👨‍💻 Author

Thanh Dang – Automation Engineer