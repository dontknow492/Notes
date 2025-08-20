package org.ghost.notes.ui.screen

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.ghost.notes.entity.Note

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NoteItemCard(
    modifier: Modifier = Modifier,
    note: Note,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    onDelete: (Int) -> Unit = {},
    onClick: (Int) -> Unit = {}
) {
    val imageModifier = Modifier
    val titleModifier = Modifier
    val bodyModifier = Modifier
    val headingModifier = Modifier
    if(sharedTransitionScope != null){
        with(sharedTransitionScope){
            imageModifier.sharedElement(
                rememberSharedContentState("image/${note.id}"),
                animatedVisibilityScope = animatedVisibilityScope!!
            )
            titleModifier.sharedElement(
                rememberSharedContentState("title/${note.id}"),
                animatedVisibilityScope = animatedVisibilityScope
            )
            bodyModifier.sharedElement(
                rememberSharedContentState("body/${note.id}"),
                animatedVisibilityScope = animatedVisibilityScope
            )
            headingModifier.sharedElement(
                rememberSharedContentState("heading/${note.id}"),
                animatedVisibilityScope = animatedVisibilityScope
            )
        }
    }


    Card(
        modifier = modifier
            .clickable(onClick = { onClick(note.id) })
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){
            note.image?.let{path ->
                AsyncImage(
                    model = path,
                    contentDescription = null,
                    modifier = imageModifier.weight(1f).height(64.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(3f),
//                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = note.heading,
                    modifier = headingModifier,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                val text = note.title ?: note.body
                text?.let{
                    Text(
                        it,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = if(text == note.body) bodyModifier else titleModifier,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Text(
                text = "10 min\nago",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                minLines = 2,
                maxLines = 2,
                textAlign = TextAlign.Center
            )

            IconButton(
                onClick = { onDelete(note.id) }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete"
                )
            }

        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview()
@Composable
private fun NoteItemCardPreview() {
    val note = Note(
        id = 1,
        heading = "Feed the cat on monday before anything",
        title = "Don't forget to feed  so see want starve at all cost",
        body = "give food at 8.00am to cat.",
        createdAt = 23,
        updatedAt = 23,
        image = "asdf", // Provide a sample image path or keep it null
        color = null, // Provide a sample color or keep it null
        themeId = 1
    )
    NoteItemCard(note = note)
}