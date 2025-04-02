plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) version "1.9.24" apply false // Hoặc phiên bản mới hơn
    id("com.google.gms.google-services") version "4.4.2" apply false
}