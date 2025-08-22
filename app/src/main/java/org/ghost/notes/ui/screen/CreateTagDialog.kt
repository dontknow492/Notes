package org.ghost.notes.ui.screen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog


@Composable
fun CreateTagDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onAccept: (String) -> Unit = {}
) {
    var text by remember { mutableStateOf("") }
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = modifier
//                .width(300.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    label = { Text("Tag name") },
                    placeholder = { Text("cooking, etc") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = {
                            onAccept(text)
                            onDismiss()
                        }
                    ) {
                        Text("Accept")
                    }
                }
            }
        }
    }

}