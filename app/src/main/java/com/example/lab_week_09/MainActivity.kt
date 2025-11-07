package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme

// --- DATA CLASS DAN ROUTES ---
data class Student(val name: String)

object AppRoutes {
    const val HOME = "home"
    const val RESULT = "result"
    // [BONUS] Ganti nama argumen menjadi lebih deskriptif
    const val LIST_DATA_JSON_ARG = "listDataJson"
    // [BONUS] Perbarui route untuk menerima JSON
    fun getResultRouteWithArgs() = "$RESULT/{$LIST_DATA_JSON_ARG}"
}

// --- ACTIVITY UTAMA ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LAB_WEEK_09Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

// --- GRAF NAVIGASI ---
@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppRoutes.HOME
    ) {
        // Rute untuk Halaman Home
        composable(AppRoutes.HOME) {
            Home(
                onNavigateToResult = { jsonData ->
                    // [BONUS] Kirim data JSON yang sudah di-encode
                    navController.navigate("${AppRoutes.RESULT}/$jsonData")
                }
            )
        }

        // Rute untuk Halaman Result dengan argumen JSON
        composable(
            route = AppRoutes.getResultRouteWithArgs(),
            arguments = listOf(
                navArgument(AppRoutes.LIST_DATA_JSON_ARG) {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            // [BONUS] Ambil JSON dan konversi kembali ke List<Student>
            val json = backStackEntry.arguments?.getString(AppRoutes.LIST_DATA_JSON_ARG).orEmpty()
            val studentList = JsonConverter.toStudentList(json)
            ResultContent(students = studentList)
        }
    }
}

// --- LAYAR HOME ---
@Composable
fun Home(onNavigateToResult: (String) -> Unit) {
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
        onAddButtonClick = {
            // Logika ini sudah benar, data kosong tidak akan ditambahkan
            if (inputField.value.isNotBlank()) {
                listData.add(Student(inputField.value))
                inputField.value = "" // Reset input field
            }
        },
        onNavigateButtonClick = {
            // [BONUS] Ubah list menjadi JSON menggunakan converter dan kirim
            val jsonString = JsonConverter.fromStudentList(listData.toList())
            onNavigateToResult(jsonString)
        }
    )
}

// --- UI UNTUK LAYAR HOME ---
@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputFieldValue: String,
    onInputValueChange: (String) -> Unit,
    onAddButtonClick: () -> Unit,
    onNavigateButtonClick: () -> Unit
) {
    // [FIX 1] Tentukan status tombol berdasarkan input field
    val isAddButtonEnabled = inputFieldValue.isNotBlank()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Header: Judul, Input, dan Tombol-tombol
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = stringResource(id = R.string.enter_item), style = MaterialTheme.typography.titleLarge)

                TextField(
                    value = inputFieldValue,
                    onValueChange = onInputValueChange,
                    label = { Text("Nama Siswa Baru") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // [FIX 1] Terapkan status 'enabled' pada tombol
                    Button(
                        onClick = onAddButtonClick,
                        enabled = isAddButtonEnabled
                    ) {
                        Text(text = stringResource(id = R.string.button_click))
                    }
                    Button(onClick = onNavigateButtonClick) {
                        Text(text = stringResource(id = R.string.button_navigate))
                    }
                }
            }
        }

        // Daftar nama siswa
        items(listData) { student ->
            Text(text = student.name, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}

// --- [BONUS] LAYAR RESULT YANG DIPERBARUI ---
@Composable
fun ResultContent(students: List<Student>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Data Siswa yang Dikirim:", style = MaterialTheme.typography.titleMedium)

        if (students.isEmpty()) {
            Text(text = "Tidak ada data yang dikirim.")
        } else {
            // [BONUS] Tampilkan daftar menggunakan LazyColumn
            LazyColumn(
                modifier = Modifier.padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(students) { student ->
                    Text(
                        text = student.name,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

// --- PREVIEWS ---
@Preview(showBackground = true, name = "Home Screen Preview")
@Composable
fun PreviewHome() {
    LAB_WEEK_09Theme {
        Home(onNavigateToResult = {}) // Beri lambda kosong untuk preview
    }
}

@Preview(showBackground = true, name = "Result Screen Preview")
@Composable
fun PreviewResult() {
    LAB_WEEK_09Theme {
        // [BONUS] Perbarui preview untuk menggunakan data list
        ResultContent(students = listOf(Student("Preview Tanu"), Student("Preview Tina")))
    }
}
