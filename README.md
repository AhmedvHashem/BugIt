# BugIt

**BugIt** is a Kotlin-based bug tracking application, designed for modern development teams to manage and resolve bugs efficiently.

---
## 📚 Screenshots from demo-app
| Bug screen    | Single Image  | Multi-Image   |
| ------------- | ------------- | ------------- |
| ![Screenshot_20241120_034309](https://github.com/user-attachments/assets/b49f172a-62b7-458a-ace6-f7fc731809a4)  | ![Screenshot_20241120_034430](https://github.com/user-attachments/assets/2f34988b-6756-49eb-9f63-df6fc3101671)  | ![Screenshot_20241120_034642](https://github.com/user-attachments/assets/ad1dd62d-24df-4340-aca8-faa3daac1cde) |

---

## 📚 Tech Stack
- **Programming Language**: Kotlin
- **Architecture**: Clean Architecture with MVVM (Model-View-ViewModel) pattern
- **UI**: Jetpack Compose for UI
- **Networking**: Kotlin Coroutines for asynchronous operations
- **Build Tool**: Gradle with Kotlin DSL

---

## 📦 Third-Party Libraries
- **[Coil](https://coil-kt.github.io/coil/)**: Image loading library for Jetpack Compose
- **[Compose-Screenshot](https://github.com/SmartToolFactory/Compose-Screenshot/)**: Screenshot library for Jetpack Compose

---

## 🚀 How to Use the `bugit` Module
  Clone the repo
  ```sh
  # terminal or powershell
  git clone https://github.com/AhmedvHashem/BugIt.git
  ```
  Include the `bugit` module in your project via Gradle by adding the dependency in `settings.gradle.kts`:
  ```kotlin
  // settings.gradle.kts
  include(":bugit")
  ```

  Then you simply config it before using like below
  ```kotlin
  val bugit = BugIt.init(
      BugIt.Config()
          .allowMultipleImage(true)
          .addExtraField("02_priority", "Priority")
          .addExtraField("03_assignee", "Assignee")
  ).getInstance()

  // logic for pick from gallery or screenshot
  bugit.show(context, /* list of images uri */)
  ```

## 📦 Database
- [Link](https://drive.google.com/drive/folders/1meSFbpvbwgKAJ2kAKBThQF400X8JOcPp?usp=drive_link)
---

## 📊 Class Diagram
**a high-level overview**

<img width="655" alt="Screenshot 2024-11-20 at 3 40 25 AM" src="https://github.com/user-attachments/assets/a2029f02-a3e8-4a3e-9eb4-179cddc894e6">



   
