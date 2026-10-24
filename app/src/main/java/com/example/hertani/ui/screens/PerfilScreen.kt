package com.example.hertani.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hertani.model.Sesion
import com.example.hertani.ui.components.Avatar
import com.example.hertani.ui.components.EstadoChipTexto
import com.example.hertani.ui.components.TituloSeccion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    sesion: Sesion,
    onCerrarSesion: () -> Unit
) {
    Scaffold(topBar = { TopAppBar(title = { Text("Mi perfil") }) }) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ElevatedCard(Modifier.fillMaxWidth()) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Avatar(sesion.nombre, tamano = 64.dp)
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(sesion.nombre, style = MaterialTheme.typography.titleLarge)
                        Text(sesion.rol.etiqueta, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                        Text(sesion.rol.descripcion, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            TituloSeccion("Recursos nativos y permisos")

            TarjetaRecurso(
                icono = Icons.Filled.FolderOpen,
                titulo = "Selector de documentos",
                descripcion = "Adjunta el CV en PDF con el selector del sistema. No requiere permisos de almacenamiento: solo se accede al archivo elegido.",
                estado = "Sin permiso requerido",
                activo = true
            )

            TarjetaRecurso(
                icono = Icons.Filled.Share,
                titulo = "Intents del sistema",
                descripcion = "Abrir el perfil profesional en el navegador y compartir el resumen o los resultados.",
                estado = "Sin permiso requerido",
                activo = true
            )

            Spacer(Modifier.width(8.dp))
            OutlinedButton(
                onClick = onCerrarSesion,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Cerrar sesión")
            }
            Text(
                "Hertani · MVP académico DSY1105 · Solo datos ficticios",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun TarjetaRecurso(
    icono: ImageVector,
    titulo: String,
    descripcion: String,
    estado: String,
    activo: Boolean,
    acciones: @Composable ColumnScope.() -> Unit = {}
) {
    OutlinedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(12.dp))
                Text(titulo, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
                EstadoChipTexto(estado, activo)
            }
            Text(descripcion, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            acciones()
        }
    }
}