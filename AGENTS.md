# Repository Guidelines

## Project Structure & Module Organization
This repository is a single-module Android app built with Gradle Kotlin DSL.
- `app/`: application module.
- `app/src/main/`: production code and Android resources (`res/`, `AndroidManifest.xml`).
- `app/src/test/`: local JVM unit tests.
- `app/src/androidTest/`: instrumented tests that run on a device/emulator.
- `gradle/libs.versions.toml`: dependency and plugin versions.
- Root Gradle files: `settings.gradle.kts`, `build.gradle.kts`, `gradle.properties`.

## Build, Test, and Development Commands
Run from repository root:
- `./gradlew assembleDebug`: builds the debug APK.
- `./gradlew testDebugUnitTest`: runs local unit tests in `app/src/test`.
- `./gradlew connectedDebugAndroidTest`: runs instrumented tests on a connected device/emulator.
- `./gradlew lintDebug`: runs Android lint checks.
- `./gradlew clean`: removes build artifacts.

## Coding Style & Naming Conventions
- Language: Kotlin + AndroidX.
- Indentation: 4 spaces; avoid tabs.
- Classes/objects: `PascalCase` (for example, `HdmiThemeManager`).
- Functions/variables: `camelCase` (for example, `loadThemeConfig`).
- Constants: `UPPER_SNAKE_CASE`.
- Resource files/IDs: `lower_snake_case` (for example, `activity_main.xml`, `ic_launcher_round`).
- Keep package names lowercase and consistent with `br.com.joseasousa.theme.hdmi`.

## Testing Guidelines
- Frameworks: JUnit4 (`test`) and AndroidX Test + Espresso (`androidTest`).
- Test classes should end with `Test`.
- Prefer descriptive test names like `whenInputIsValid_returnsTheme()`.
- Add or update tests for behavior changes before opening a PR.

## Commit & Pull Request Guidelines
Git history is not available in this workspace checkout, so use a consistent convention:
- Commit format: imperative, concise subject (or Conventional Commits), e.g. `feat: add HDMI theme selection screen`.
- Keep commits focused; avoid mixing refactors with feature changes.
- PRs should include: summary, testing performed (`./gradlew testDebugUnitTest` output), linked issue (if any), and screenshots/video for UI changes.

## Security & Configuration Tips
- Do not commit secrets, keystores, or machine-specific files.
- Keep `local.properties` local to your machine.
- Review dependency updates in `gradle/libs.versions.toml` for compatibility and security impact.
