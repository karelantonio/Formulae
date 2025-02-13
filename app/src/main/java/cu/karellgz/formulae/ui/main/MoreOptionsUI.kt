package cu.karellgz.formulae.ui.main

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cu.karellgz.formulae.BuildConfig
import cu.karellgz.formulae.R
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

    val ctx = LocalContext.current

    AlertDialog(onDismissRequest = { ctl.popBackStack() }) {
        Card {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    stringResource(R.string.moreoptionui_header_more_opts),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp)
                )

                TextButton(onClick = {
                    ctl.navigate("themePicker")
                }) {

                    Icon(
                        painter = painterResource(id = R.drawable.baseline_dark_mode_24),
                        contentDescription = stringResource(R.string.moreoptionsui_choosetheme_contentdesc)
                    )

                    Text(stringResource(R.string.moreoptionsui_choosetheme))
                }

                TextButton(onClick = {
                    // Launch the telegram site
                    val intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/formulae_app_es"))
                    ctx.startActivity(Intent.createChooser(intent,
                        ctx.getString(R.string.moreoptionsui_choose_an_app)))
                }) {

                    Icon(
                        painter = painterResource(id = R.drawable.baseline_menu_book_24),
                        contentDescription = stringResource(R.string.moreoptionsui_tutorials_and_more_contentdesc)
                    )

                    Text(stringResource(R.string.moreoptionsui_tutorials_and_more))
                }

                Text(
                    text = "Formulae ${BuildConfig.VERSION_NAME} (commit ${
                        BuildConfig.COMMIT.substring(
                            0..10
                        )
                    })",
                    modifier = Modifier.padding(0.dp, 24.dp, 0.dp, 0.dp)
                )
            }
        }
    }
}