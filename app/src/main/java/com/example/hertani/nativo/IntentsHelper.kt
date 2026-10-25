package com.example.hertani.nativo

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

object IntentsHelper {

    fun abrirEnlace(context: Context, url: String): Boolean {
        if (!url.trim().startsWith("https://", ignoreCase = true)) return false
        return iniciar(context, Intent(Intent.ACTION_VIEW, Uri.parse(url.trim())))
    }

    fun abrirDocumento(context: Context, uriString: String?): Boolean {
        if (uriString.isNullOrBlank()) return false
        return try {
            val uri = Uri.parse(uriString)
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                clipData = android.content.ClipData.newRawUri("", uri)
            }
            iniciar(context, intent)
        } catch (e: Exception) {
            false
        }
    }

    fun compartirTexto(context: Context, titulo: String, texto: String): Boolean {
        val envio = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, titulo)
            putExtra(Intent.EXTRA_TEXT, texto)
        }
        return iniciar(context, Intent.createChooser(envio, titulo))
    }

    private fun iniciar(context: Context, intent: Intent): Boolean = try {
        context.startActivity(intent)
        true
    } catch (e: ActivityNotFoundException) {
        false
    }
}