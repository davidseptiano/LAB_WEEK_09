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
                    // Panggil fungsi Home() yang benar (tanpa parameter)
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
    // State untuk menampung daftar siswa, akan diingat selama recomposition.
    val listData = remember {
        mutableStateListOf(
            Student("Tanu"),
            Student("Tina"),
            Student("Tono")
        )
    }
    // State untuk menampung teks dari input field.
    val inputField = remember { mutableStateOf("") }

    // Memanggil Composable yang bertanggung jawab untuk UI (presentasi)
    HomeContent(
        listData = listData,
        inputFieldValue = inputField.value,
        onInputValueChange = { newValue ->
            inputField.value = newValue
        },
        onButtonClick = {
            if (inputField.value.isNotBlank()) {
                // Tambahkan siswa baru ke list
                listData.add(Student(inputField.value))
                // Kosongkan kembali input field
                inputField.value = ""
            }
        }
    )
}

/**
 * Composable yang hanya bertanggung jawab untuk menampilkan UI.
 * Ini adalah praktik yang baik untuk memisahkan logika state dari UI.
 */
@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputFieldValue: String,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Item pertama dalam list: header dengan input dan tombol
        item {
            Text(text = stringResource(id = R.string.enter_item))

            TextField(
                value = inputFieldValue,
                onValueChange = onInputValueChange,
                label = { Text("Nama Siswa Baru") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )

            Button(onClick = onButtonClick) {
                Text(text = stringResource(id = R.string.button_click))
            }
        }

        // Tampilkan daftar siswa dari state
        items(listData) { student ->
            Text(
                text = student.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 4.dp)
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
