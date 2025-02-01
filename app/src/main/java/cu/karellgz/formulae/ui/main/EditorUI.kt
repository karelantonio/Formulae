package cu.karellgz.formulae.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cu.karellgz.formulae.R
import cu.karellgz.formulae.ui.theme.FormulaeTheme


@Composable
@Preview(name = "Preview the editor")
fun EditorPreview() {
    FormulaeTheme {
        Surface {
            EditorUI(rememberNavController(), "This should be some string idk") {}
        }
    }
}


@Composable
fun EditorUI(ctl: NavHostController, value: String, onChange: (String) -> Unit) {

    val scroll = rememberScrollState()

    Column {

        TextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            textStyle = TextStyle.Default.copy(fontFamily = FontFamily.Monospace),
            value = value,
            placeholder = {
                Text(stringResource(R.string.editorui_placeholder), fontFamily = FontFamily.Monospace)
            },
            onValueChange = onChange,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(0.dp)
                .verticalScroll(scroll),
            readOnly = false
        )
    }
}