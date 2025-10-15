package com.bor3y.dictation_detail.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bor3y.text_accuracy_lib.TextAccuracy

@Composable
fun AccuracyResultDialog(
    state: DictationDetailState,
    onDismiss: () -> Unit
) {
    state.accuracyResult?.let {
        val accuracy = it.keys.first()
        val operations = it.values.first()

        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Close")
                }
            },
            title = {
                Text(
                    text = "Accuracy: ${"%.2f".format(accuracy)}%",
                    style = MaterialTheme.typography.titleLarge,
                    color = if (accuracy > 80) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
            },
            text = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp) // scrollable inside dialog
                ) {
                    items(operations.size) { index ->
                        OperationRow(operations[index])
                        VerticalDivider()
                    }
                }
            }
        )
    }
}

@Composable
fun OperationRow(op: TextAccuracy.Operation) {
    val (label, color) = when (op.type) {
        TextAccuracy.OperationType.MATCH ->
            "Match ✅" to Color(0xFF4CAF50)
        TextAccuracy.OperationType.ALMOST ->
            "Almost (typo) ✏️" to Color(0xFFFFC107)
        TextAccuracy.OperationType.SUBSTITUTE ->
            "Substitute ❌" to Color(0xFFF44336)
        TextAccuracy.OperationType.DELETE ->
            "Deleted 🗑" to Color(0xFF9E9E9E)
        TextAccuracy.OperationType.INSERT ->
            "Inserted ➕" to Color(0xFF2196F3)
        TextAccuracy.OperationType.SWAPPED ->
            "Swapped 🔄" to Color(0xFF9C27B0)
        TextAccuracy.OperationType.PLURAL ->
            "Plural 📚" to Color(0xFF795548)
    }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            color = color,
            style = MaterialTheme.typography.bodyLarge
        )

        when (op.type) {
            TextAccuracy.OperationType.SWAPPED -> {
                Text("Actual: ${op.actualWords?.first} ${op.actualWords?.second}")
                Text("User: ${op.userWords?.first} ${op.userWords?.second}")
            }
            else -> {
                op.actualWord?.let { Text("Actual: $it") }
                op.userWord?.let { Text("User: $it") }
            }
        }
    }
}
