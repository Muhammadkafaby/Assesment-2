package org.d3if0149.myapplication.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if0149.myapplication.R
import org.d3if0149.myapplication.database.AbsensiDb
import org.d3if0149.myapplication.model.Absensi
import org.d3if0149.myapplication.ui.theme.myapplicationTheme
import org.d3if0149.myapplication.util.ViewModelFactory

const val KEY_ID_Absensi = "idAbsensi"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {
    val  context = LocalContext.current
    val db = AbsensiDb.AbsensiDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)
    var nama by remember { mutableStateOf("") }
    var absensi by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }


    LaunchedEffect(true){
        if (id == null) return@LaunchedEffect
        val data = viewModel.getAbsensi(id) ?: return@LaunchedEffect
        nama = data.nama
        absensi = data.absen
    }
//
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    if (id == null)
                        Text(text = stringResource(id = R.string.tambah_Absensi))
                    else
                        Text(text = stringResource(id = R.string.edit_Absensi))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        if (nama == "" || absensi == ""){
                            Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                            return@IconButton
                        }
                        if (id == null){
                            viewModel.insert(nama, absensi)
                        } else {
                            viewModel.update(id, nama, absensi)
                        }
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(R.string.simpan),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (id != null){
                        DeleteAction {
                            showDialog = true
                        }
                        DisplayAlertDialog(
                            openDialog = showDialog,
                            onDismissRequest = { showDialog = false }) {
                            showDialog = false
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    ) { padding ->
        FormAbsensi(
            title = nama,
            onTitleChange = { nama = it },
            desc = absensi,
            onDescChange = { absensi = it }, // pass the setter function as a parameter
            absensi = absensi, // pass the absensi variable as a parameter
            modifier = Modifier.padding(padding)
        )
    }
}
@Composable
fun DeleteAction(delete: () -> Unit){
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false}
        ) {
            DropdownMenuItem(text = {
                Text(text = stringResource(id = R.string.hapus))
                                    },
                onClick = { expanded = false
                            delete()
                }
            )
        }
    }
}
//
@Composable
fun FormAbsensi(
    title: String, onTitleChange: (String) -> Unit,
    desc: String, onDescChange: (String) -> Unit,
    absensi: String, // add a new parameter for the absensi variable
    modifier: Modifier,
) {
    Column(
    modifier = modifier
        .fillMaxSize()
        .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    OutlinedTextField(
        value = title,
        onValueChange = { onTitleChange(it) },
        label = { Text(text = stringResource(R.string.judul)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next
        ),
        modifier = Modifier.fillMaxWidth().padding(6.dp)
        )

    AbsensiRadioButton(
        selectedOption = absensi, // use the passed absensi variable
        onOptionSelected = {
            onDescChange(it) // use the passed setter function
        }
    )
}
}



@Composable
fun AbsensiRadioButton(
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    val options = listOf(
    stringResource(R.string.hadir),
    stringResource(R.string.sakit),
    stringResource(R.string.tanpa_keterangan)
)

    Column {
        options.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = { onOptionSelected(text) }
                )

                Text(
                    text = text,
                    style = MaterialTheme.typography.labelMedium.plus(MaterialTheme.typography.bodyMedium),
                    modifier = Modifier.padding(start = 8.dp) // Mengurangi padding menjadi 8.dp
                )

            }
            Divider()
        }

    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenPreview() {
    myapplicationTheme {
        DetailScreen(rememberNavController())
    }
}