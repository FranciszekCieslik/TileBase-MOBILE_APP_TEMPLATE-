# TileBase-MOBILE_APP_TEMPLATE-

TileBase to szablon aplikacji mobilnej, zaprojektowany z użyciem Kotlin, Jetpack Compose i architektury MVVM. Aplikacja oferuje podstawowe funkcjonalności, takie jak rejestracja, logowanie, zapamiętywanie stanu użytkownika, zarządzanie kontem oraz główny widok z edytowalnymi kafelkami. 

---

### Funkcjonalności: 
- Rejestracja (sprawdzenie poprawności E-Mail, Hasła oraz akceptacji regulaminu i polityki prywatności) 
- Logowanie 
- Zapamiętanie stanu zalogowania 
- Przycisk otwierający widok ustawień 
- Ustawienia: wylogowywanie,  usuwanie konta 
- Menu z głównym widokiem aplikacji i widokiem profilu 
- Widok Profilu: 
- Nazwa użytkownika 
- Używany e-mail 
- Główny widok Aplikacji: 
- Lista “kafelków” z opcją dodawania i usuwania, oraz nadawania koloru 

---

### Plan architektury
1. **Warstwa prezentacji (UI)**:
   - Jetpack Compose dla tworzenia interfejsu użytkownika.
   - Podział na ekrany: Rejestracja, Logowanie, Główny widok, Profil, Ustawienia.

2. **Warstwa ViewModel (MVVM)**:
   - Każdy ekran ma swój ViewModel zarządzający logiką i stanem ekranu.
   - Użycie `StateFlow` lub `LiveData` do zarządzania stanem UI.

3. **Warstwa danych**:
   - Symulacja bazy danych z wykorzystaniem Room (lokalna baza danych SQLite).
   - Przy braku serwera można symulować API, używając klasy Repository z lokalnym przechowywaniem danych.

4. **Routing i nawigacja**:
   - Użycie Jetpack Navigation Compose do zarządzania nawigacją między ekranami.

---

### Biblioteki
1. **Jetpack Compose** – do tworzenia interfejsu użytkownika.
2. **Navigation Compose** – do nawigacji między ekranami.
3. **Room** – lokalna baza danych do przechowywania danych użytkowników.
4. **Hilt** – do Dependency Injection, co ułatwia zarządzanie zależnościami.
5. **DataStore** – do zapisywania stanu zalogowania.
6. **Material3** – do stylizacji aplikacji.
