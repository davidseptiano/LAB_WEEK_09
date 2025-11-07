package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LAB_WEEK_09Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val list = listOf("Tanu", "Tina", "Tono")
                    // Panggil Home composable dengan data list
                    Home(items = list)
                }
            }
        }
    }
}

/**
 * Composable utama yang menampilkan input field dan list data.
 * @param items List string yang akan ditampilkan.
 */
@Composable
fun Home(items: List<String>) {
    // LazyColumn lebih efisien untuk menampilkan list karena hanya merender
    // item yang terlihat di layar, mirip seperti RecyclerView.
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp) // Memberi jarak antar elemen
    ) {
        // Blok 'item' pertama untuk menampilkan header (input dan tombol)
        item {
            // Kita bungkus header dalam Column agar teratur secara vertikal
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = stringResource(id = R.string.enter_item))

                TextField(
                    value = "", // Seharusnya dihubungkan dengan state
                    onValueChange = { /* TODO: Handle state change */ },
                    label = { Text("Input Angka") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )

                Button(onClick = { /* TODO: Handle click */ }) {
                    Text(text = stringResource(id = R.string.button_click))
                }
            }
        }

        // Blok 'items' untuk menampilkan list data dari parameter
        items(items) { item ->
            Text(text = item, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

/**
 * Fungsi Preview untuk menampilkan composable Home di Android Studio.
 * Ini hanya untuk keperluan development dan tidak akan masuk ke dalam aplikasi final.
 */
@Preview(showBackground = true, name = "Home Preview")
@Composable
fun PreviewHome() {
    LAB_WEEK_09Theme {
        Home(listOf("Tanu", "Tina", "Tono", "Tini", "Tutu"))
    }
}
