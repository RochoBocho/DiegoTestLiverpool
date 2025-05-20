package com.example.diegorochintest.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.diegorochintest.dom.models.SortOption

@Composable
fun SortOptionDialog(
    showDialog: Boolean,
    availableSortOptions: List<SortOption>,
    selectedSortOption: SortOption,
    onSortOptionSelected: (SortOption) -> Unit,
    onDismiss: () -> Unit,
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "Ordenar por") },
            text = {
                Column {
                    availableSortOptions.forEach { sortOption ->
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = (sortOption == selectedSortOption),
                                        onClick = { onSortOptionSelected(sortOption) },
                                    ).padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = (sortOption == selectedSortOption),
                                onClick = { onSortOptionSelected(sortOption) },
                            )
                            Text(
                                text = sortOption.label,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 8.dp),
                            )
                        }
                        if (sortOption != availableSortOptions.last()) {
                            HorizontalDivider()
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            },
            properties =
                DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                ),
            shape = RoundedCornerShape(16.dp),
        )
    }
}
