package org.ghost.notes.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.ghost.notes.enums.SortBy
import org.ghost.notes.enums.SortOrder

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    selectedOrder: SortOrder = SortOrder.ASCENDING,
    selectedBy: SortBy = SortBy.UPDATED_AT,
    onDismissRequest: () -> Unit = {},
    onOrderChange: (SortOrder, SortBy) -> Unit = { _, _ -> }
) {
    val sortOrder = SortOrder.entries.toTypedArray()
    val sortBy = SortBy.entries.toTypedArray()

    var selectedOrderIndex = sortOrder.indexOf(selectedOrder)
    var selectedByIndex = sortBy.indexOf(selectedBy)





    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = "Sort By",
                style = MaterialTheme.typography.titleLarge
            )
            sortOrder.forEachIndexed { index, sortOrder ->
                RadioButtonWithText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    selected = index == selectedOrderIndex,
                    text = sortOrder.name
                ) {
                    onOrderChange(sortOrder, sortBy[selectedByIndex])
                }
            }

            HorizontalDivider()

            Text(
                text = "Sort Order",
                style = MaterialTheme.typography.titleLarge
            )
            sortBy.forEachIndexed { index, sortBy ->
                RadioButtonWithText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    selected = index == selectedByIndex,
                    text = sortBy.name.replace("_", " ")
                ) {
                    onOrderChange(sortOrder[selectedOrderIndex], sortBy)
                }
            }
        }
    }
}

@Composable
fun RadioButtonWithText(
    modifier: Modifier = Modifier,
    selected: Boolean,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
        RadioButton(
            selected = selected,
            onClick = onClick
        )

    }
}