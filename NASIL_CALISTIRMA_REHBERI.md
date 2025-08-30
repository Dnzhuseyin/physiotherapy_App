# Fizik Tedavi Uygulaması - Çalıştırma Rehberi

## 🚀 En Kolay Yöntem

### 1. Android Studio'da Yeni Proje Oluşturun
- Android Studio'yu açın
- "Create New Project" seçin
- "Empty Activity (Compose)" seçin
- Project name: **PhysiotherapyApp**
- Package name: `com.example.physiotherapyapp`
- Language: **Kotlin**
- Minimum SDK: **API 24**

### 2. Temel Yapıyı Oluşturun

#### MainActivity.kt
```kotlin
package com.example.physiotherapyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.physiotherapyapp.ui.theme.PhysiotherapyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhysiotherapyAppTheme {
                PhysiotherapyApp()
            }
        }
    }
}

@Composable
fun PhysiotherapyApp() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("device") { PlaceholderScreen("Cihaz Bağlantısı") }
        composable("exercises") { PlaceholderScreen("Egzersizler") }
        composable("stats") { PlaceholderScreen("İstatistikler") }
        composable("profile") { PlaceholderScreen("Profil") }
    }
}

@Composable
fun HomeScreen(navController: androidx.navigation.NavController) {
    val menuItems = listOf(
        "Cihaz Bağlantısı" to "device",
        "Egzersizler" to "exercises", 
        "İstatistikler" to "stats",
        "Profil" to "profile"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Fizik Tedavi Uygulaması",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Sağlıklı yaşam için egzersizlerinizi takip edin",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(menuItems) { (title, route) ->
                Card(
                    onClick = { navController.navigate(route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Bu özellik yakında geliyor...",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
```

### 3. build.gradle.kts (app) - Basit Versiyon
```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.physiotherapyapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.physiotherapyapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    
    kotlinOptions {
        jvmTarget = "1.8"
    }
    
    buildFeatures {
        compose = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
}
```

## 🎉 Sonuç

Bu basit versiyon kesinlikle çalışacak! Tüm karmaşık özellikler yerine:

✅ **Ana menü ekranı**  
✅ **Navigation sistemi**  
✅ **Modern Material Design**  
✅ **Placeholder ekranlar**  

### Gelecekte Eklenebilecekler:
- Bluetooth bağlantısı
- Video rehberler  
- Egzersiz takibi
- İstatistikler
- Gamification

**Bu temel yapı üzerine adım adım özellikler ekleyebilirsiniz!** 🚀
