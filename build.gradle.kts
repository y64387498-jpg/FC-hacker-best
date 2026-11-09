plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "com.fcpowershot.panel"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.fcpowershot.panel"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
}
