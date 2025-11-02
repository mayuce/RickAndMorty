# RickAndMorty (Android)

This repository is an Android application that appears to be a Rick and Morty themed app built with modern Android tooling.

## Summary

- Kotlin-based Android app (Compose UI).
- Uses Jetpack Compose, Navigation Compose, and Hilt for dependency injection.
- Project structure follows a single `app` module layout.

## Key technologies

- Kotlin
- Jetpack Compose
- Navigation Compose
- Hilt (Dagger) for DI
- Gradle (wrapper)
- Ktlint for code formatting
- Detekt for static analysis (configuration present in `config/detekt`)

## Quickstart

Requirements
- Android Studio (recommended) or command-line Gradle
- JDK 11+ (match the project's Gradle/JDK configuration)
- An Android device or emulator

Open in Android Studio (recommended)
1. Open the project folder in Android Studio.
2. Let Gradle sync and the IDE index the project.
3. Run the `app` configuration on a connected device or emulator.

Command-line build
- Build: `./gradlew build`
- Assemble debug APK: `./gradlew :app:assembleDebug`
- Install debug APK to a connected device: `./gradlew :app:installDebug`

Static analysis
- Run detekt: `./gradlew detekt`

## Project layout (high level)

- `app/` — main Android app module
  - `src/main/java/com/aliyuce/rickandmorty` — application code
    - `MainActivity.kt` — app entrypoint and host for Compose
    - `di/` — Hilt dependency injection modules
    - `screens/` — Compose screens
    - `components/` — reusable Compose UI components
    - `navigation/` — navigation host and navigation graph
    - `ui/theme/` — Compose theme
  - `build.gradle.kts` — module build configuration

Top-level Gradle files and wrappers are present for reproducible builds.

## How to contribute
- Follow existing code style and architecture.
- Run `./gradlew build` and `./gradlew detekt` before submitting changes.

## Troubleshooting
- If Gradle sync fails, try `File -> Sync Project with Gradle Files` in Android Studio.
- If build issues persist, run `./gradlew clean build --stacktrace` to get more details.

## Notes
- No license file is included in the repository. Add `LICENSE` if you want to set one.
- If you want me to extend this README with module-level docs, testing instructions, or CI setup, tell me what to include and I'll add it.


