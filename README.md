# 🧪 Selenide Automation Framework

This project is an **Automation Test Framework** built with **Selenide** and **TestNG**, applying the **Page Object Model (POM)** design pattern and Java best practices.

---

## 📁 Project Structure

```
src
├── main
│ └── java
│ ├── common
│ │ └── helpers
│ │ └── ElementHelper.java
│ ├── pageObjects
│ │ ├── HomePage.java
│ │ ├── ProductCategoryPage.java
│ │ ├── CheckoutPage.java
│ │ └── OrderStatusPage.java
│ └── common
│ └── constants
│ └── AppConstants.java
└── test
└── java
├── base
│ └── BaseTest.java
├── tests
│ ├── TC_01_VerifyPurchaseTest.java
│ ├── TC_02_VerifyMultipleItemsPurchaseTest.java
│ ├── TC_03_VerifyLoginTest.java
│ └── TC_10_PostReviewTest.java
└── utils
└── TestDataReader.java
```

---

## 🛠️ Technologies Used

- **Java 17**
- **Selenide** `6.19.1`
- **TestNG** `7.8.0`
- **Maven** `17`
- **Allure Report** (optional)

---

# ✨ The features are applied

- 🔀 **Github**: Gitflow  
- ⚙️ **Configuration**: pom.xml (Dependencies, Commandline)  
- 💻 **Code**: Name convention, OOP, JSON, Data driven  
- 📊 **Report**: Allure report  

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/boykid95/Thanh_Sele2
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

# 📊 How to get the results  

### ▶️ Run tests  
- Run tests by **command line** or using **UI**  

### 📂 After running the program  
- Point to the current project  
- Enter the following in Terminal:  
  ```bash
  allure serve allure-results

# 🏆 FINAL RESULT
<img width="1920" height="922" alt="image" src="https://github.com/user-attachments/assets/5042c276-3676-4d5b-bcf2-2f97c356d9c2" />
<img width="1920" height="922" alt="image" src="https://github.com/user-attachments/assets/6cc92342-c462-4356-9c35-49ac964a7a36" />


---

## 👨‍💻 Author


Thanh Dang – Automation Engineer
