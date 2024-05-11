package org.d3if0149.myapplication.ui.screen


import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if0149.myapplication.R
import org.d3if0149.myapplication.database.AbsensiDb
import org.d3if0149.myapplication.model.Absensi
import org.d3if0149.myapplication.navigation.Screen
import org.d3if0149.myapplication.ui.theme.myapplicationTheme
import org.d3if0149.myapplication.util.SettingsDataStore
import org.d3if0149.myapplication.util.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val dataStore = SettingsDataStore(LocalContext.current)
    val showList by dataStore.layoutFlow.collectAsState(true)
    val isDarkModeEnabled by dataStore.darkModeFlow.collectAsState(false)
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = stringResource(id = R.string.app_name))
        }, colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ), actions = {
            IconButton(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    dataStore.saveLayout(
                        !showList
                    )
                }
            }) {
                Icon(
                    painter = painterResource(
                        if (showList) R.drawable.baseline_grid_view_24
                        else R.drawable.baseline_view_list_24
                    ), contentDescription = stringResource(
                        if (showList) R.string.grid
                        else R.string.list
                    ), tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    dataStore.saveDarkMode(
                        !isDarkModeEnabled
                    )
                    AppCompatDelegate.setDefaultNightMode(
                        if (isDarkModeEnabled) AppCompatDelegate.MODE_NIGHT_NO
                        else AppCompatDelegate.MODE_NIGHT_YES
                    )
                }
            }) {
                Icon(
                    painter = painterResource(
                        if (isDarkModeEnabled) R.drawable.baseline_light_mode_24
                        else R.drawable.baseline_dark_mode_24
                    ), contentDescription = stringResource(
                        if (isDarkModeEnabled) R.string.dark_mode_on
                        else R.string.dark_mode_off
                    ), tint = MaterialTheme.colorScheme.primary
                )
            }

        })

    },


        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.FormBaru.route)
            }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.tambah_Absensi),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }) { padding ->
        ScreenContent(showList, Modifier.padding(padding), navController)
    }
}

@Composable
fun ScreenContent(showList: Boolean, modifier: Modifier, navController: NavHostController) {
    val context = LocalContext.current
    val db = AbsensiDb.AbsensiDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: MainViewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()

    if (data.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
           Image(
    painter = painterResource(id = R.drawable.baseline_report_gmailerrorred_24),
    contentDescription = R.string.list_kosong.toString(),
    modifier = Modifier.size(100.dp) // Change the value to your desired size
)
            Text(text = stringResource(id = R.string.list_kosong))
        }
    } else {
        if (showList) {
            LazyColumn(
                modifier = modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 84.dp)
            ) {
                items(data) {
                    ListItem(Absensi = it) {
                        navController.navigate(Screen.FormUbah.withId(it.id))
                    }
                    Divider()
                }
            }
        } else {
            LazyVerticalStaggeredGrid(
                modifier = modifier.fillMaxSize(),
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 84.dp)
            ) {
                items(data) {
                    GridItem(Absensi = it) {
                        navController.navigate(Screen.FormUbah.withId(it.id))
                    }
                }
            }
        }
    }
}


@Composable
fun ListItem(Absensi: Absensi, onClick: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = Absensi.nama,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = Absensi.absen, maxLines = 2, overflow = TextOverflow.Ellipsis
        )
        Text(text = Absensi.tanggal)
    }
}

@Composable
fun GridItem(Absensi: Absensi, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = Absensi.nama,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
//
            )
            Text(
                text = Absensi.absen,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )
            Text(text = Absensi.tanggal)
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreenPreview() {
    myapplicationTheme {
        MainScreen(rememberNavController())
    }
}
//