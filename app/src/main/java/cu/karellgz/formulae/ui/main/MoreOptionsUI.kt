package cu.karellgz.formulae.ui.main

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreOptionsUI(ctl: NavController) {
    AlertDialog(onDismissRequest = { ctl.popBackStack() }) {}
}