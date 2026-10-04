package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var textInput by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = textInput,
            onValueChange = { textInput = it },
            label = { Text("Введите текст") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка 1: Открыть вторую Activity (ЯВНЫЙ ИНТЕНТ)
        Button(
            onClick = {
                if (textInput.isBlank()) {
                    Toast.makeText(context, "Поле не может быть пустым", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                val intent = Intent(context, SecondActivity::class.java)
                intent.putExtra("EXTRA_TEXT", textInput)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Открыть вторую Activity")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Кнопка 2: Позвонить другу (НЕЯВНЫЙ ИНТЕНТ)
        Button(
            onClick = {
                if (textInput.isBlank()) {
                    Toast.makeText(context, "Введите номер телефона!", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val cleanPhone = textInput.replace(Regex("[\\s\\-()]"), "")

                if (!cleanPhone.matches(Regex("^[+]?[0-9]{10,15}$"))) {
                    Toast.makeText(context, "Некорректный номер. Пример: +79001234567", Toast.LENGTH_LONG).show()
                    return@Button
                }

                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = android.net.Uri.parse("tel:$cleanPhone")
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Позвонить другу")
        }

        // Кнопка 3: Поделиться текстом (СИСТЕМНЫЙ ИНТЕНТ)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (textInput.isBlank()) {
                    Toast.makeText(context, "Введите текст!", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, textInput)
                }
                context.startActivity(Intent.createChooser(intent, "Поделиться через..."))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Поделиться текстом")
        }
    }
}