package cu.karellgz.formulae.utils

import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView

class MyClient(val onProgress: (Int) -> Unit = {}, val onKaTexError: (String) -> Unit = {}) :
    WebChromeClient() {

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        Log.i("FORMULAE", "Progress updated -> $newProgress")
        this.onProgress(newProgress)
    }

    override fun onConsoleMessage(cm: ConsoleMessage?): Boolean {
        Log.i("FORMULAE-CONSOLE", "[${cm?.sourceId()}:${cm?.lineNumber()}] ${cm?.message()}")
        this.onKaTexError(cm?.message() ?: "(Unknown error)")
        return super.onConsoleMessage(cm)
    }
}