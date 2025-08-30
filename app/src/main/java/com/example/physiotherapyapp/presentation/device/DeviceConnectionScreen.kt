package com.example.physiotherapyapp.presentation.device

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.physiotherapyapp.navigation.Screen

@Composable
fun DeviceConnectionScreen(navController: NavController) {
    var isScanning by remember { mutableStateOf(false) }
    var connectionStatus by remember { mutableStateOf("Bağlantı Yok") }
    
    val mockDevices = listOf(
        "PhysioDevice-001",
        "PhysioDevice-002", 
        "PhysioDevice-003"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Cihaz Bağlantısı",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Fizik tedavi cihazınızı bağlayın",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        // Connection Status
        ConnectionStatusCard(connectionStatus)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Scan Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { 
                    isScanning = !isScanning
                    if (isScanning) {
                        connectionStatus = "Aranıyor..."
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                if (isScanning) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(if (isScanning) "Durdurun" else "Cihaz Ara")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Device List
        if (isScanning) {
            Text(
                text = "Bulunan Cihazlar",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(mockDevices) { device ->
                    DeviceCard(
                        deviceName = device,
                        onConnect = { 
                            connectionStatus = "Bağlandı"
                            isScanning = false
                            navController.navigate(Screen.DevicePlacement.route)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ConnectionStatusCard(status: String) {
    val statusColor = when (status) {
        "Bağlandı" -> Color.Green
        "Aranıyor..." -> Color.Orange
        else -> Color.Red
    }
    
    Card(
        colors = CardDefaults.cardColors(
            containerColor = statusColor.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (status) {
                    "Bağlandı" -> Icons.Default.CheckCircle
                    "Aranıyor..." -> Icons.Default.Search
                    else -> Icons.Default.Error
                },
                contentDescription = null,
                tint = statusColor
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Bağlantı Durumu",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = status,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceCard(
    deviceName: String,
    onConnect: () -> Unit
) {
    Card(
        onClick = onConnect
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DeviceHub,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = deviceName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Fizik Tedavi Cihazı",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Bağlan"
            )
        }
    }
}
