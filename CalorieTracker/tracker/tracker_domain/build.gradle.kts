plugins {
    `android-library`
    `kotlin-android`
}

apply(from = "$rootDir/base-module.gradle")

android {
    namespace = "com.andreeailie.tracker_domain"
}

dependencies {
    implementation(project(Modules.core))
}