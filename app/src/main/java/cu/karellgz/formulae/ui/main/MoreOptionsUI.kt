package cu.karellgz.formulae.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cu.karellgz.formulae.ui.theme.FormulaeTheme


@Preview("More options preview")
@Composable
fun MoreOptionsPreview() {
    FormulaeTheme {
        Card(modifier = Modifier.fillMaxSize()) {
            MoreOptionsUI(ctl = rememberNavController())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreOptionsUI(ctl: NavController) {
    AlertDialog(onDismissRequest = { ctl.popBackStack() }) {
        Card {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    "More options",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp)
                )
            }
        }
    }
}