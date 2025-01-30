package cu.karellgz.formulae.latex

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import cu.karellgz.formulae.utils.MyClient
import cu.karellgz.formulae.utils.Theme
import kotlinx.coroutines.coroutineScope
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.coroutineContext

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LaTeX(
    formula: String,
    modifier: Modifier = Modifier,
    save: Boolean = false,
    theme: Theme = Theme.BLACK_ON_WHITE,
    onError: (String?) -> Unit = {},
    onLoading: (LoadState) -> Unit = {},
    onSaved: (String) -> Unit = {},
) {

    val pageProgress = rememberSaveable { mutableStateOf(LoadState.DONE) }
    val alreadySaved = rememberSaveable { mutableStateOf(true) }
    val externalFiles = LocalContext.current.getExternalFilesDir(null)!!

    AndroidView(factory = {
        WebView(it).apply {
            isClickable = false
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            webChromeClient = MyClient(onProgress = { prog ->
                pageProgress.value = if (prog == 100) {
                    LoadState.DONE
                } else {
                    LoadState.LOADING
                }
                onLoading(pageProgress.value)
            }, onKaTexError = { err ->
                onError(err)
            })
            settings.javaScriptEnabled = true
            pageProgress.value = LoadState.LOADING
            onLoading(pageProgress.value)
            loadUrl("file:///android_asset/index.html")
            WebView.enableSlowWholeDocumentDraw()
        }
    }, update = {
        if (pageProgress.value == LoadState.DONE) {
            onError(null)
            val encoded = Base64.encodeToString(formula.toByteArray(), Base64.NO_WRAP).trim()
            val expr =
                "loadEncoded(\"${encoded}\", \"${theme.background}\", \"${theme.foreground}\")";
            Log.i(
                "FORMULAE", "Sending $encoded to the webview ($formula) [expr=$expr]"
            )
            it.evaluateJavascript(expr) {}

            if (save) {
                val bm = Bitmap.createBitmap(it.width, it.height, Bitmap.Config.ARGB_8888)
                val canvas = android.graphics.Canvas(bm)
                it.draw(canvas)

                // Save the bitmap
                val file = File(externalFiles, "last_image.png")
                with(FileOutputStream(file)) {
                    bm.compress(Bitmap.CompressFormat.PNG, 100, this)
                    close()
                }

                // TODO:Catch errors here
                onSaved(file.absolutePath)
            }
        }

    }, modifier = modifier
    )
}

// Load state
enum class LoadState {
    DONE,
    LOADING,
}