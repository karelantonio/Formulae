package cu.karellgz.formulae.ui

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import cu.karellgz.formulae.R
import cu.karellgz.formulae.ui.main.AboutUI
import cu.karellgz.formulae.ui.main.EditorUI
import cu.karellgz.formulae.ui.main.MoreOptionsUI
import cu.karellgz.formulae.ui.main.PreviewUI
import cu.karellgz.formulae.ui.main.SelectedTheme
import cu.karellgz.formulae.ui.main.ThemePicker
import cu.karellgz.formulae.ui.theme.FormulaeTheme
import cu.karellgz.formulae.utils.Theme
import cu.karellgz.formulae.utils.mutableStateThemeSaver

@Composable
@Preview(name = "Main UI preview")
fun MainUI() {
    FormulaeTheme {

        val ctl = rememberNavController()
        val content = rememberSaveable { mutableStateOf("") }
        val ctx = LocalContext.current
        val isDark = isSystemInDarkTheme()
        val selectedTheme = remember {
            val prefs = ctx.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val theme = prefs.getString("theme", null) ?: "auto"
            mutableStateOf(
                when (theme) {
                    "auto" -> {
                        SelectedTheme.Auto
                    }

                    "light" -> {
                        SelectedTheme.Light
                    }

                    else -> {
                        SelectedTheme.Dark
                    }
                }
            )
        }
        val theme = remember {
            mutableStateOf(
                if (selectedTheme.value == SelectedTheme.Auto) {
                    if (isDark) {
                        Theme.WHITE_ON_BLACK
                    } else {
                        Theme.BLACK_ON_WHITE
                    }
                } else if (selectedTheme.value == SelectedTheme.Light) {
                    Theme.BLACK_ON_WHITE
                } else {
                    Theme.WHITE_ON_BLACK
                }
            )
        }

        Scaffold(
            topBar = { AppBar(ctl) },
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {

            NavHost(
                modifier = Modifier.padding(it),
                navController = ctl,
                startDestination = "editor"
            ) {
                composable("editor") {
                    EditorUI(
                        ctl,
                        value = content.value,
                        onChange = { new -> content.value = new })
                }

                dialog("themePicker") {
                    ThemePicker(ctl, selectedTheme.value, onSelected = { thm ->
                        // Save the theme and change
                        ctx.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().apply {
                            putString("theme", thm.toShortForm())
                            commit()
                        }

                        selectedTheme.value = thm
                        theme.value = thm.toTheme(isDark)
                    })
                }

                dialog("preview") {
                    PreviewUI(ctl, value = content.value, theme = theme.value)
                }

                dialog("moreOptions") {
                    MoreOptionsUI(ctl)
                }

                dialog("about") {
                    AboutUI(ctl)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(ctl: NavController) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = { ctl.navigate("moreOptions") }) {
                Icon(imageVector = Icons.TwoTone.MoreVert, contentDescription = "More options")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ), title = {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.equation),
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 6.dp, 0.dp)
                        .width(24.dp)
                        .height(24.dp),
                    contentDescription = "Euler raised to the power of I times Pi"
                )

                Text(
                    "Editor",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        actions = {
            ElevatedButton(onClick = { ctl.navigate("preview") }) {
                Text("Preview")
            }
        }
    )
}
