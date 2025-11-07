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
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme
// Pastikan Anda sudah membuat dan mengimpor elemen UI kustom Anda
// import com.example.lab_week_09.ui.theme.OnBackgroundItemText
// import com.example.lab_week_09.ui.theme.OnBackgroundTitleText
// import com.example.lab_week_09.ui.theme.PrimaryTextButton

// --- DATA CLASS DAN ROUTES ---
data class Student(val name: String)

object AppRoutes {
    const val HOME = "home"
    const val RESULT = "result"
    const val LIST_DATA_ARG = "listData"
    fun getResultRouteWithArgs() = "$RESULT/?$LIST_DATA_ARG={$LIST_DATA_ARG}"
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
                    // Panggil App Composable sebagai root
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
                onNavigateToResult = { listDataAsString ->
                    // Encode data sebelum mengirim untuk keamanan URL
                    navController.navigate("${AppRoutes.RESULT}/?$listDataAsString")
                }
            )
        }

        // Rute untuk Halaman Result dengan argumen opsional
        composable(
            route = AppRoutes.getResultRouteWithArgs(),
            arguments = listOf(
                navArgument(AppRoutes.LIST_DATA_ARG) {
                    type = NavType.StringType
                    nullable = true // Argumen bisa null
                }
            )
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            ResultContent(
                listData = arguments.getString(AppRoutes.LIST_DATA_ARG).orEmpty()
            )
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
            if (inputField.value.isNotBlank()) {
                listData.add(Student(inputField.value))
                inputField.value = "" // Reset input field
            }
        },
        onNavigateButtonClick = {
            // Ubah list menjadi string dan kirim
            val listString = listData.joinToString(separator = ", ") { it.name }
            onNavigateToResult("${AppRoutes.LIST_DATA_ARG}=$listString")
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
                // TODO: Ganti dengan OnBackgroundTitleText jika sudah ada
                Text(text = stringResource(id = R.string.enter_item), style = MaterialTheme.typography.titleLarge)

                TextField(
                    value = inputFieldValue,
                    onValueChange = onInputValueChange,
                    label = { Text("Nama Siswa Baru") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // TODO: Ganti dengan PrimaryTextButton jika sudah ada
                    Button(onClick = onAddButtonClick) {
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
            // TODO: Ganti dengan OnBackgroundItemText jika sudah ada
            Text(text = student.name, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}

// --- LAYAR RESULT ---
@Composable
fun ResultContent(listData: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Data yang Dikirim:", style = MaterialTheme.typography.titleMedium)
        // TODO: Ganti dengan OnBackgroundItemText jika sudah ada
        Text(text = listData.ifEmpty { "Tidak ada data yang dikirim." }, style = MaterialTheme.typography.bodyLarge)
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
        ResultContent(listData = "Tanu, Tina, Tono")
    }
}
