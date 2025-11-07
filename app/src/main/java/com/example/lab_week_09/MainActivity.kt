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
// import androidx.compose.material3.Button // Ini mungkin ada di file lain, pastikan tidak error
// import com.example.lab_week_09.ui.elements.* // Impor elemen UI kustom Anda
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme

// Pindahkan data class ke level atas (top-level)
data class Student(
    var name: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LAB_WEEK_09Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Home()
                }
            }
        }
    }
}

/**
 * Composable yang mengelola state untuk daftar siswa dan input field.
 */
@Composable
fun Home() {
    val listData = remember {
        mutableStateListOf(
            Student("Tanu"),
            Student("Tina"),
            Student("Tono")
        )
    }
    val inputField = remember { mutableStateOf("") }

    HomeContent(
        listData = listData,
        inputFieldValue = inputField.value,
        onInputValueChange = { newValue ->
            inputField.value = newValue
        },
        onButtonClick = {
            if (inputField.value.isNotBlank()) {
                listData.add(Student(inputField.value))
                inputField.value = ""
            }
        }
    )
}

/**
 * Composable yang hanya bertanggung jawab untuk menampilkan UI.
 */
@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputFieldValue: String,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit
) {
    // Terapkan padding dan alignment pada LazyColumn itu sendiri
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Bungkus semua konten header (judul, input, tombol) dalam satu 'item'
        item {
            // Gunakan Column biasa untuk mengatur elemen header secara vertikal
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // TODO: Pastikan Anda sudah membuat composable `OnBackgroundTitleText`
                // Jika belum, untuk sementara ganti dengan `Text` biasa
                Text(
                    text = stringResource(id = R.string.enter_item),
                    style = MaterialTheme.typography.titleLarge
                )

                TextField(
                    value = inputFieldValue,
                    onValueChange = onInputValueChange,
                    label = { Text("Nama Siswa Baru") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    )
                )

                // TODO: Pastikan Anda sudah membuat composable `PrimaryTextButton`
                // Jika belum, ganti dengan `Button` biasa
                androidx.compose.material3.Button(onClick = onButtonClick) {
                    Text(text = stringResource(id = R.string.button_click))
                }
            }
        }

        // Gunakan 'items' untuk menampilkan daftar data
        items(listData) { item ->
            // TODO: Pastikan Anda sudah membuat composable `OnBackgroundItemText`
            // Jika belum, ganti dengan `Text` biasa
            Text(
                text = item.name,
                modifier = Modifier.padding(vertical = 4.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


/**
 * Fungsi Preview untuk melihat tampilan Home di Android Studio.
 */
@Preview(showBackground = true, name = "Home Preview")
@Composable
fun PreviewHome() {
    LAB_WEEK_09Theme {
        Home()
    }
}
