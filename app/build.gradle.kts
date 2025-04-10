plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.edufun.music"
    compileSdk = 35

    viewBinding {
        enable=true
    }
    defaultConfig {
        applicationId = "com.edufun.music"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity:1.10.1")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


    implementation(libs.retrofit)

    implementation(libs.converter.gson)
    implementation(libs.gson)

    //for set image
    implementation ("com.squareup.picasso:picasso:2.8")


///circleView///
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation("androidx.legacy:legacy-support-v4:1.0.0")

}