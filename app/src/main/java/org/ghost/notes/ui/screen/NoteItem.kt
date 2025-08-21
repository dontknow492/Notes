package org.ghost.notes.ui.screen

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.ghost.notes.entity.Note

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NoteItemCard(
    modifier: Modifier = Modifier,
    note: Note,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onDelete: (Note) -> Unit = {},
    onClick: (Int) -> Unit = {}
) {

    Card(
        modifier = modifier
            .clickable(onClick = { onClick(note.id) })
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            with(sharedTransitionScope) {
                note.image?.let { path ->
                    AsyncImage(
                        model = path,
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .height(64.dp)
                            .sharedElement(
                                rememberSharedContentState("image"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(3f),
//                verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = note.heading,
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState("heading"),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    val text = note.title ?: note.body
                    text?.let {
                        Text(
                            it,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.sharedElement(
                                rememberSharedContentState(if (it == note.title) "title" else "body"),
                                animatedVisibilityScope = animatedVisibilityScope
                            ),
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
                    onClick = { onDelete(note) }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete"
                    )
                }
            }


        }
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NoteItemWideCard(
    modifier: Modifier = Modifier,
    note: Note,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    onClick: (Int) -> Unit = {}
) {
    val headingModifier = Modifier
    if (sharedTransitionScope != null) {
        with(sharedTransitionScope) {
            headingModifier.sharedElement(
                rememberSharedContentState("heading"),
                animatedVisibilityScope = animatedVisibilityScope!!
            )
        }
    }
    BorderBox(
        modifier = modifier
            .size(150.dp)
            .clickable(onClick = { onClick(note.id) }),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = note.heading,
            modifier = headingModifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.headlineSmall,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun BorderBox(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    borderColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
    cornerRadius: Dp = 6.dp,
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(
        modifier = modifier
            .drawBehind {
                val radius = cornerRadius.toPx()
                drawRoundRect(
                    color = backgroundColor,
                    size = size,
                    cornerRadius = CornerRadius(
                        x = radius,
                        y = radius
                    )
                )
                drawRoundRect(
                    color = borderColor,
                    size = Size(radius * 2, size.height),
                    cornerRadius = CornerRadius(
                        x = radius,
                        y = radius
                    ),

                    )
            }
            .padding(start = cornerRadius.times(1.5f)),
        contentAlignment = contentAlignment,
        propagateMinConstraints = propagateMinConstraints
    ) {

        content()
    }
}