package com.example.sportspot.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Retorna l'icona de Material corresponent al tipus de pista esportiva.
 *
 * @param type Cadena amb el tipus de pista, tal com arriba del backend.
 * @return [ImageVector] de Material Icons que representa el tipus de pista.
 *
 * @author Jesús Ramos
 */
fun courtTypeIcon(type: String): ImageVector = when {
    type.contains("Tennis",  ignoreCase = true) -> Icons.Default.SportsTennis
    type.contains("Pàdel",   ignoreCase = true) -> Icons.Default.SportsTennis
    type.contains("Bàsquet", ignoreCase = true) -> Icons.Default.SportsBasketball
    type.contains("Futbol",  ignoreCase = true) -> Icons.Default.SportsSoccer
    else                                        -> Icons.Default.SportsTennis
}