plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.santotomas_lauragarcia.lk_dramas"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.santotomas_lauragarcia.lk_dramas"
        minSdk = 23
        targetSdk = 33
        versionCode = 2
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures{
        viewBinding = true
    }
}

dependencies {


    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:20.0.0")
    implementation("org.osmdroid:osmdroid-android:6.1.8")
    implementation("org.osmdroid:osmdroid-wms:6.1.11")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //dependencias de mqtt
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.1.0")

    implementation("com.android suppont:suppont-v4:28.0.01")
    implementation("com.android.suppont:Localbroadcastmanagen:28.0.0")

    implementation("org.eclipse.paho:org.eclipse.paho.android.service:1.1.1") {
        exclude(group = "com.android.support")
        exclude(module = "appcompat-v7")
        exclude(module = "support-v4")

    }
}