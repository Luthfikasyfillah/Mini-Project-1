package org.d3if3063.miniproject1.ui.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
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
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3063.miniproject1.R
import org.d3if3063.miniproject1.navigation.Screen
import org.d3if3063.miniproject1.ui.theme.MiniProject1Theme
import androidx.compose.foundation.Image
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

fun String.isNumeric(): Boolean {
    return this.matches("-?\\d+(\\.\\d+)?".toRegex())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.About.route)
                        }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.tentang_aplikasi),
                            tint = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        }
    ) { padding ->
        ScreenContent(Modifier.padding(padding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier) {
    var hargaAwal by rememberSaveable { mutableStateOf("") }
    var hargaAkhir by rememberSaveable { mutableStateOf("") }
    var diskonError by rememberSaveable { mutableStateOf(false) }
    var diskonPercent by rememberSaveable { mutableFloatStateOf(0.25f) }
    var customDiskon by rememberSaveable { mutableStateOf("") }
    var isCustomDiskonSelected by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.konversi_waktu_intro),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )

        // Inputan harga awal
        OutlinedTextField(
            value = hargaAwal,
            onValueChange = { hargaAwal = it },
            label = { Text(text = stringResource(R.string.harga_awal)) },
            trailingIcon = { IconPicker(hargaAwal.isEmpty() && diskonError, "") },
            supportingText = { ErrorHint(hargaAwal.isEmpty() && diskonError, stringResource(R.string.error_input_empty)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Radio button untuk memilih besar diskon
        Row(
            modifier = Modifier
                .padding(top = 6.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        ) {
            listOf(0.25f, 0.5f, 0.75f).forEach { percent ->
                DiskonOption(
                    percent = percent,
                    isSelected = diskonPercent == percent,
                    onSelected = {
                        diskonPercent = it
                        isCustomDiskonSelected = false
                        customDiskon = ""
                    }
                )
            }
            RadioButton(
                selected = isCustomDiskonSelected,
                onClick = {
                    isCustomDiskonSelected = true
                    diskonPercent = 0f // Reset other options
                }
            )
            Text(
                text = stringResource(R.string.custom_diskon),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // Input field untuk memasukkan jumlah diskon sendiri
        if (isCustomDiskonSelected) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = customDiskon,
                    onValueChange = {
                        customDiskon = it
                        diskonPercent = 0f
                    },
                    label = { Text(text = stringResource(R.string.custom_diskon)) },
                    trailingIcon = { IconPicker(customDiskon.isEmpty() && diskonError, "") },
                    supportingText = { ErrorHint(customDiskon.isEmpty() && diskonError, stringResource(R.string.error_input_empty)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
        }

        // Inputan harga akhir
        OutlinedTextField(
            value = hargaAkhir,
            onValueChange = { /* Do nothing */ },
            label = { Text(text = stringResource(R.string.harga_akhir)) },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    hargaAwal = ""
                    hargaAkhir = ""
                    diskonError = false
                    diskonPercent = 0.25f
                    customDiskon = ""
                    isCustomDiskonSelected = false
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(R.string.reset))
            }
            Button(
                onClick = {
                    if (hargaAwal.isEmpty()) {
                        diskonError = true
                        return@Button
                    }
                    val diskonAmount = when {
                        customDiskon.isNotEmpty() && customDiskon.isNumeric() -> {
                            customDiskon.toFloat() / 100 * hargaAwal.toFloat()
                        }
                        else -> hargaAwal.toFloat() * diskonPercent
                    }
                    val hargaDiskon = hargaAwal.toFloat() - diskonAmount
                    hargaAkhir = if (hargaDiskon.toInt().toFloat() == hargaDiskon) {
                        hargaDiskon.toInt().toString()
                    } else {
                        hargaDiskon.toString()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(R.string.cek))
            }
        }

        if (hargaAwal.isNotEmpty() && hargaAkhir.isNotEmpty()) {
            val sisaHargaAwal = (hargaAwal.toFloat() - hargaAkhir.toFloat()).let {
                if (it.toInt().toFloat() == it) {
                    it.toInt().toString()
                } else {
                    it.toString()
                }
            }
            Text(
                text = stringResource(R.string.hasil_diskon, sisaHargaAwal),
                style = MaterialTheme.typography.bodyLarge
            )

            // Menambahkan gambar
            Image(
                painter = painterResource(id = R.drawable.good),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .size(100.dp) // Ubah ukuran gambar sesuai kebutuhan Anda
            )

            Button(
                onClick = {
                    sharedata(
                        context = context,
                        message = context.getString(R.string.bagikan_template, sisaHargaAwal)
                    )
                },
                modifier= Modifier.padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(R.string.bagikan))
            }
        }
    }
}

@Composable
fun DiskonOption(
    percent: Float,
    isSelected: Boolean,
    onSelected: (Float) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(8.dp)
            .selectable(
                selected = isSelected,
                onClick = { onSelected(percent) },
                role = Role.RadioButton
            )
    ) {
        RadioButton(selected = isSelected, onClick = null)
        Text(
            text = "${(percent * 100).toInt()}%",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun IconPicker(isError: Boolean, unit: String) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else {
        // Placeholder untuk unit
        Text(unit)
    }
}


@Composable
fun ErrorHint(isError: Boolean, errorText: String) {
    if (isError) {
        Text(text = errorText)
    }
}

private fun  sharedata(context: Context, message:String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
         type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreenPreview() {
    MiniProject1Theme {
        MainScreen(rememberNavController())
    }
}
