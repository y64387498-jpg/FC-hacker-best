# FC Power Shot

Minimal Android helper panel inspired by the supplied reference.

## Demo activation key
`FC-1D`

This is a local demo key, not a secure online licensing system. Change the check in
`MainActivity.kt` or connect a licensing backend before public distribution.

## What it does
- Minimal black/teal login screen.
- Requests Android's "Display over other apps" permission.
- Launches the installed FC Mobile app.
- Shows a small floating Power Shot reminder chip. Tap to toggle its label; long-press to close.

## Important limitation
The floating chip is a visual reminder only. It does not automate shots, modify FC Mobile,
read game memory, or provide an unfair gameplay advantage.

## Build
Open this folder in Android Studio, allow Gradle sync, then Build > Build Bundle(s) / APK(s) > Build APK(s).
