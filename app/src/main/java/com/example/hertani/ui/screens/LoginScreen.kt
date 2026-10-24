package com.example.hertani.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hertani.model.Rol
import com.example.hertani.ui.components.CampoValidado
import com.example.hertani.ui.components.sacudir
import com.example.hertani.ui.viewmodel.SesionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: SesionViewModel,
    onIngreso: () -> Unit
) {
    val estado by viewModel.loginState.collectAsStateWithLifecycle()

    val ingresar: () -> Unit = {
        if (viewModel.validar()) {
            viewModel.iniciarSesion()
            onIngreso()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.tertiary,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LogoAnimado()
            Spacer(Modifier.height(16.dp))
            Text(
                "Hertani",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                "Postulación y evaluación WIM 100",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(28.dp))

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 480.dp) // se adapta a pantallas grandes
            ) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Ingresa a tu perfil", style = MaterialTheme.typography.titleLarge)

                    SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                        Rol.entries.forEachIndexed { indice, rol ->
                            SegmentedButton(
                                selected = estado.rol == rol,
                                onClick = { viewModel.onRol(rol) },
                                shape = SegmentedButtonDefaults.itemShape(indice, Rol.entries.size)
                            ) { Text(rol.etiqueta) }
                        }
                    }
                    Text(
                        estado.rol.descripcion,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    CampoValidado(
                        valor = estado.nombre.valor,
                        onValorChange = viewModel::onNombre,
                        etiqueta = if (estado.rol == Rol.EVALUADORA) "Nombre de la evaluadora" else "Nombre (Dirección)",
                        icono = Icons.Filled.Person,
                        error = estado.nombre.error,
                        placeholder = "Ej: Evaluadora Demo Tres",
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Done
                    )

                    Button(
                        onClick = ingresar,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .sacudir(estado.intentosFallidos)
                    ) {
                        Text("Ingresar")
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "MVP académico · Solo datos ficticios",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LogoAnimado() {
    val transicion = rememberInfiniteTransition(label = "pulso")
    val escala by transicion.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(1400), RepeatMode.Reverse),
        label = "escalaLogo"
    )
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.15f),
        modifier = Modifier
            .size(104.dp)
            .graphicsLayer {
                scaleX = escala
                scaleY = escala
            }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                Icons.Filled.Shield,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(56.dp)
            )
        }
    }
}