# RickAndMorty (Android)

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

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
    - `RMApp.kt` — application class for Hilt setup
    - `di/` — Hilt dependency injection modules
    - `data/` — data layer (repositories, data sources, models)
    - `domain/` — domain layer (use cases, business logic)
    - `features/` — Compose screens
    - `navigation/` — navigation host and navigation graph
    - `work/` — background work 
    - `ui/` — Compose theme and common UI components
  - `build.gradle.kts` — module build configuration

Top-level Gradle files and wrappers are present for reproducible builds.

## UI

UI is built with Jetpack Compose. The app contains an episode list, an episode detail view (character ids), and a character detail screen with an Export action. Keep the visuals simple and accessible — the assessment focuses on functionality.

### Included demo video

A short demo/asset video is included in the repository for reference:

- Path: `./rickAndMorty.mp4`

<!-- Inline video preview -->
<video src="./rickAndMorty.mp4" controls style="max-width:100%;height:auto;" poster="" >
  Your browser does not support the video tag. You can download and play the file directly: 
  <a href="./rickAndMorty.mp4">rickAndMorty.mp4</a>
</video>

Note: The video is included as a demo asset — verify any reuse or redistribution against the original source/rights before publishing.


## Testing

Recommended tests: unit tests for ViewModels/use-cases (missing use-case tests) and Compose UI tests for screens. Typical commands:

```bash
# unit tests
./gradlew test

# instrumentation / UI tests (on device/emulator)
./gradlew :app:connectedAndroidTest
```


## How to develop and run tests locally

1. Start an emulator or connect an Android device.
2. Build and install the debug APK:

```bash
./gradlew :app:assembleDebug
./gradlew :app:installDebug
```

3. Run connected instrumentation tests (UI tests):

```bash
./gradlew :app:connectedAndroidTest
```

4. Run unit tests locally without a device:

```bash
./gradlew test
```


## How to contribute
- Follow existing code style and architecture.
- Run `./gradlew build` and `./gradlew detekt` before submitting changes.
- Add tests for your new features and ensure existing tests pass.


## Troubleshooting
- If Gradle sync fails, try `File -> Sync Project with Gradle Files` in Android Studio.
- If build issues persist, run `./gradlew clean build --stacktrace` to get more details.

## License

This project is licensed under the Apache License 2.0 — see the `LICENSE` file for details.
