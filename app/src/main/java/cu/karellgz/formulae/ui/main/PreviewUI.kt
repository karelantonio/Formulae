package cu.karellgz.formulae.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Create
import androidx.compose.material.icons.twotone.Share
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cu.karellgz.formulae.latex.LaTeX
import cu.karellgz.formulae.ui.theme.FormulaeTheme
import cu.karellgz.formulae.utils.Theme
import java.io.File


@Composable
@Preview(name = "Preview the preview lol", showBackground = true, showSystemUi = true)
fun PreviewPreview() {
    FormulaeTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            PreviewUI(
                ctl = rememberNavController(),
                value = "x^2 - 2abx + ab = (x-a)(x-b)",
                isPreview = true
            )
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PreviewUI(
    ctl: NavHostController,
    value: String,
    theme: Theme = Theme.BLACK_ON_WHITE,
    isPreview: Boolean = false
) {

    val scroll = rememberScrollState()
    val error = rememberSaveable { mutableStateOf<String?>(null) }
    val saveClicked = rememberSaveable { mutableStateOf(false) }
    val ctx = LocalContext.current

    Dialog(
        onDismissRequest = { ctl.popBackStack() },
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scroll)
        ) {

            Text(
                "Preview Formula",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(24.dp, 24.dp, 24.dp, 16.dp)
            )

            if (error.value != null || isPreview) {
                Text(
                    "${error.value}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Red)
                )
            } else {

                // The webview
                Surface {
                    LaTeX(
                        formula = value,
                        save = saveClicked.value,
                        onError = { error.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        theme = theme,
                        onSaved = {
                            saveClicked.value = false
                            val uri = FileProvider.getUriForFile(
                                ctx,
                                "${ctx.packageName}.fileprovider",
                                File(it)
                            )

                            // The intent
                            val share = Intent(Intent.ACTION_SEND)
                            share.type = "image/png"
                            share.putExtra(Intent.EXTRA_STREAM, uri)
                            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            ctx.startActivity(Intent.createChooser(share, "Share with..."))
                        }
                    )
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 16.dp, 24.dp, 24.dp)
            ) {


                Spacer(modifier = Modifier.weight(1f))

                TextButton(onClick = { saveClicked.value = true }) {
                    Icon(
                        imageVector = Icons.TwoTone.Share,
                        contentDescription = "Share",
                    )
                    Text("Share")
                }
            }
        }
    }
}