package com.example.hertani.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.hertani.model.Acciones
import com.example.hertani.model.RegistroHistorial
import com.example.hertani.util.formatearFecha

@Composable
fun ItemHistorial(registro: RegistroHistorial, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(40.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    iconoAccion(registro.accion),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(registro.accion, style = MaterialTheme.typography.titleSmall)
            Text(registro.detalle, style = MaterialTheme.typography.bodyMedium)
            Text(
                "${registro.usuario} · ${formatearFecha(registro.fecha)}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun iconoAccion(accion: String): ImageVector = when (accion) {
    Acciones.INICIO_SESION -> Icons.AutoMirrored.Filled.Login
    Acciones.CIERRE_SESION -> Icons.AutoMirrored.Filled.Logout
    Acciones.REGISTRO_CANDIDATA -> Icons.Filled.PersonAdd
    Acciones.EVALUACION_REGISTRADA -> Icons.AutoMirrored.Filled.Assignment
    Acciones.EVALUACION_DUPLICADA -> Icons.Filled.Block
    Acciones.CANDIDATA_RESUELTA -> Icons.Filled.Gavel
    Acciones.CONSULTA_CV -> Icons.Filled.PictureAsPdf
    Acciones.CONSULTA_PERFIL -> Icons.Filled.Link
    Acciones.COMPARTIR -> Icons.Filled.Share
    else -> Icons.Filled.History
}