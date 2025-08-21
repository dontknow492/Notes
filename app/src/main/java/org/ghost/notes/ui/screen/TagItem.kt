package org.ghost.notes.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.ghost.notes.entity.Tag

@Composable
fun TagItem(
    modifier: Modifier = Modifier,
    tag: Tag,
    color: Color = Color.Magenta.copy(alpha = 0.5f),
    onClick: (Int) -> Unit
) {
    AngularChip(
        modifier,
        text = tag.name,
        backgroundColor = color
    ) {
        onClick(tag.id)
    }
}

@Preview
@Composable
fun AngularChip(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    text: String = "Angular Chip",
    onClick: () -> Unit = {}
) {
    AssistChip(
        onClick = onClick,
        label = {
            Text(
                text,
                modifier = Modifier.padding(end = 10.dp)
            )
        },
        shape = CutCornerShape(
            0.dp,
            20.dp,
            20.dp,
            0.dp
        ),
        colors = AssistChipDefaults.assistChipColors(
            containerColor = backgroundColor
        ),
        modifier = modifier
    )
}