package com.example.hertani.nativo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns

data class InfoArchivo(val nombre: String?, val tamano: Long?)

object ArchivoHelper {

    fun leerInfo(context: Context, uri: Uri): InfoArchivo {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val iNombre = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val iTamano = cursor.getColumnIndex(OpenableColumns.SIZE)
                val nombre = if (iNombre >= 0) cursor.getString(iNombre) else null
                val tamano = if (iTamano >= 0 && !cursor.isNull(iTamano)) cursor.getLong(iTamano) else null
                return InfoArchivo(nombre, tamano)
            }
        }
        return InfoArchivo(uri.lastPathSegment, null)
    }

    fun persistirPermiso(context: Context, uri: Uri) {
        try {
            context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } catch (e: SecurityException) {
        }
    }
}