# TileBase - MOBILE APP TEMPLATE

TileBase is a mobile application template designed using Kotlin, Jetpack Compose, and the MVVM architecture. The app provides essential functionalities such as registration, login, user session management, account handling, and a main view with editable tiles.

---

[Screencast from 2025-02-05 00-02-18.webm](https://github.com/user-attachments/assets/3dd20773-4eaf-4dc6-a3e2-43d821670b94)

---
### Features:
- **Registration** (validation of Email, Password, and acceptance of Terms and Privacy Policy)
- **Login**
- **User session persistence**
- **Settings button** to open the settings view
- **Settings:** Logout, Account deletion
- **Navigation menu** with access to the Main View and Profile View
- **Profile View:**
  - Username
  - Registered Email
- **Main View:**
  - List of "tiles" with options to add, remove, and assign colors

---

### Architecture Plan
1. **Presentation Layer (UI):**
   - Jetpack Compose for UI development.
   - Screens: Registration, Login, Main View, Profile, Settings.

2. **ViewModel Layer (MVVM):**
   - Each screen has its own ViewModel to handle logic and state management.
   - Uses `StateFlow` or `LiveData` for UI state handling.

3. **Data Layer:**
   - Room database (SQLite) for local data storage.
   - If no server is available, API simulation is handled via a Repository class with local data storage.
   - Firebase integration for authentication and real-time data storage.

4. **Routing and Navigation:**
   - Jetpack Navigation Compose for managing screen transitions.

---

### Libraries:
```md
1. **Jetpack Compose** – for UI development.
2. **Navigation Compose** – for managing screen navigation.
3. **Material3** – for app styling.
4. **Room** – for local database storage.
5. **Firebase Authentication** – for user authentication.
6. **Firebase Firestore** – for cloud-based real-time data storage.
```

