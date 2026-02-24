<!-- TABLE OF CONTENTS -->
## Table of Contents

* [About the Project](#about-the-project)
  * [Architecture](#architecture)
* [Tech Stacks](#tech-stacks)
* [Features](#features)
* [Project Structure](#project-structure)
* [Environment Setup](#environment-setup)
* [Contact](#contact)

<!-- ABOUT THE PROJECT -->
## About The Project

ToriNotes is a minimal notes application built as an assessment for Torilab. It demonstrates modern Android development practices using Jetpack Compose, Clean Architecture, and MVVM. The app allows users to create, edit, delete, and share notes — all stored locally on the device using Room Database with Paging 3 for efficient data loading.

## Architecture

The project follows **Clean Architecture** combined with **MVVM (Model-View-ViewModel)** pattern, ensuring clear separation of concerns across three layers:

* **Domain Layer** — Contains business logic: models, repository interfaces, and use cases.
* **Data Layer** — Implements repositories, local database (Room), DAOs, entity mappers, and preferences.
* **Presentation Layer** — Compose UI screens, ViewModels, navigation, and theming.

Key architecture components:
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) — Manages UI-related data in a lifecycle-conscious way
* [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) — Dependency injection
* [Navigation Compose](https://developer.android.com/guide/navigation) — Type-safe navigation with bottom navigation bar
* [Room](https://developer.android.com/training/data-storage/room) — Local database with offline-first approach
* [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) — Efficient paginated data loading

## Tech Stacks

This project uses many of the popular libraries, plugins, and tools of the modern Android ecosystem.

- [Compose](https://developer.android.com/jetpack/compose)

    - [Material3](https://developer.android.com/jetpack/androidx/releases/compose-material3) - Build Jetpack Compose UIs with Material Design 3 components.
    - [UI](https://developer.android.com/jetpack/androidx/releases/compose-ui) - Fundamental components of Compose UI needed to interact with the device, including layout, drawing, and input.
    - [UI Graphics](https://developer.android.com/jetpack/androidx/releases/compose-ui) - Compose UI primitives for graphics and rendering.
    - [UI Tooling Preview](https://developer.android.com/jetpack/compose/tooling) - Tooling support for Compose previews in Android Studio.
    - [Activity Compose](https://developer.android.com/jetpack/androidx/releases/activity) - Compose integration with Activity.
    - [Lifecycle ViewModel](https://developer.android.com/jetpack/androidx/releases/lifecycle) - Perform actions in response to lifecycle changes of components.
    - [Paging Compose](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) - Paging 3 integration with Compose for efficient list rendering.

- [Jetpack](https://developer.android.com/jetpack)

    - [AndroidX Core KTX](https://developer.android.com/kotlin/ktx) - Kotlin extensions for core Android APIs.
    - [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) - Perform actions in response to lifecycle status changes.
    - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Designed to store and manage UI-related data in a lifecycle-conscious way.
    - [Room](https://developer.android.com/training/data-storage/room) - Provides an abstraction layer over SQLite for local data persistence.
    - [Room Paging](https://developer.android.com/training/data-storage/room) - Room integration with Paging 3 for paginated database queries.
    - [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) - Load data gradually and gracefully within lists.
    - [Navigation Compose](https://developer.android.com/guide/navigation/get-started) - Type-safe navigation with Compose integration.
    - [AppCompat](https://developer.android.com/jetpack/androidx/releases/appcompat) - Backward-compatible versions of Android framework APIs.

- [Dependency Injection](https://developer.android.com/training/dependency-injection)

    - [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - Dependency injection library built on Dagger for Android.
    - [Hilt Navigation Compose](https://developer.android.com/jetpack/compose/libraries#hilt-navigation) - Hilt integration with Navigation Compose for scoped ViewModels.

- Others

    - [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines) - Library support for coroutines, providing asynchronous programming.
    - [Kotlin Flow](https://developer.android.com/kotlin/flow) - Reactive streams built on top of coroutines for async data.
    - [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization) - Kotlin-native serialization library for type-safe route arguments.
    - [Coil 3](https://coil-kt.github.io/coil/compose/) - An image loading library for Android backed by Kotlin Coroutines.
    - [Gson](https://github.com/google/gson) - A JSON library for Kotlin and Java.
    - [KSP](https://developer.android.com/build/migrate-to-ksp) - Kotlin Symbol Processing for annotation processing (Room, Hilt).
    - [SharedPreferences](https://developer.android.com/training/data-storage/shared-preferences) - Key-value storage for app settings (dark mode).

## Features

- Create new notes with a title and content.
- Edit existing notes with real-time updates.
- Delete notes with a confirmation dialog.
- Share notes to other apps via Android's share intent.
- Toggle between light and dark theme.
- Paginated note list for smooth performance with large datasets.
- Bulk note generation (1000 notes) for testing pagination and performance.
- Bottom navigation between Home and Settings screens.
- Offline-first — all data stored locally on device using Room Database.

## Project Structure

```
app/src/main/java/torilab/assessment/notes/
├── common/                  # Shared utilities (Routes, DateUtils)
├── data/
│   ├── local/
│   │   ├── dao/             # Room DAO interfaces
│   │   ├── database/        # Room Database definition
│   │   ├── entity/          # Database entities
│   │   ├── mapper/          # Entity ↔ Domain model mappers
│   │   └── preferences/     # SharedPreferences wrapper
│   └── repository/          # Repository implementations
├── di/                      # Hilt DI modules
├── domain/
│   ├── base/                # Base UseCase interface
│   ├── model/               # Domain models
│   ├── repository/          # Repository interfaces
│   ├── usecase/             # Business logic use cases
│   └── viewstate/           # ViewState & ViewEvent interfaces
├── ui/
│   ├── base/                # Base ViewModel
│   ├── navigation/          # NavGraph & BottomNav
│   ├── screen/
│   │   ├── addeditnote/     # Add/Edit note screen & ViewModel
│   │   ├── component/       # Shared UI components (NoteCard, Dialog)
│   │   ├── home/            # Home screen & ViewModel
│   │   └── settings/        # Settings screen & ViewModel
│   └── theme/               # Theme, Colors, Typography, Shapes
├── MainActivity.kt
└── ToriNotesApplication.kt
```

## Environment Setup

- Android Studio **Ladybug** or newer recommended.
- **JDK 11** or higher.
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36

```bash
# Clone the repository
git clone git@github.com:quangchien99/torilab-assessment-notes-app.git

# Open in Android Studio and sync Gradle
# Run on emulator or physical device
```

<!-- CONTACT -->
## Contact

- [GitHub](https://github.com/quangchien99)

```
Designed and developed by quangchien99

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
