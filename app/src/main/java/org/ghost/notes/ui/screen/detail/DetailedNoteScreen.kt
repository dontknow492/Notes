package org.ghost.notes.ui.screen.detail

import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import org.ghost.notes.R
import org.ghost.notes.helper.formatEpochMilliToString
import org.ghost.notes.ui.screen.CreateTagDialog
import org.ghost.notes.ui.screen.TagItem
import org.ghost.notes.viewModels.DetailedNoteViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailedNoteScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailedNoteViewModel = hiltViewModel(),
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                context.contentResolver.takePersistableUriPermission(uri, takeFlags)
                viewModel.onImageChange(uri.toString())
            } else {
                viewModel.onImageChange(null)
            }


        }
    )

    var showTagDialog by remember { mutableStateOf(false) }

    val onImageChange = {
        galleryLauncher.launch("image/*")
    }
    val onRemoveImage = {
        viewModel.onImageChange(null)
    }
    val addTag: (String) -> Unit = viewModel::addTag

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            DetailedScreenTopAppBar(
                onBack = onBack,
                onImageChange = onImageChange,
                onAddNewTag = { showTagDialog = true }
            )
        },
        floatingActionButton = {
            when (state.isEditing) {
                false -> FloatingActionButton(
                    onClick = { viewModel.onEditModeChanged(true) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit"
                    )
                }

                true -> FloatingActionButton(
                    onClick = viewModel::saveNote
                ) {
                    Icon(
                        painter = painterResource(R.drawable.rounded_save_as_24),
                        contentDescription = "Save"
                    )
                }
            }

        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        DetailedNote(
            modifier = modifier,
            viewModel = viewModel,
            onImageChange = onImageChange,
            onImageDelete = onRemoveImage,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope
        )
    }
    if (showTagDialog) {
        CreateTagDialog(
            onDismiss = { showTagDialog = false },
            onAccept = addTag
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailedNote(
    modifier: Modifier = Modifier,
    viewModel: DetailedNoteViewModel,
    onImageChange: () -> Unit = {},
    onImageDelete: () -> Unit = {},
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val state by viewModel.uiState.collectAsState()
    val noteId = state.originalNote?.id ?: -1
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState(0))
    ) {
        with(sharedTransitionScope) {
            TextField(
                value = state.heading,
                onValueChange = viewModel::onHeadingChange,
                placeholder = {
                    Text(
                        "Heading here...",
                        style = MaterialTheme.typography.headlineLarge
                    )
                },
                textStyle = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .sharedElement(
                        rememberSharedContentState("heading/${noteId}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                isError = state.validationState.isHeadingEmpty || state.validationState.isHeadingTooLong
            )
            Text(
                text = "Created: ${formatEpochMilliToString(state.createdAt)}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 18.dp)
            )

            Text(
                text = "Last modified: ${formatEpochMilliToString(state.updatedAt)}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 18.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(horizontal = 18.dp)
            ) {
                items(
                    items = state.tags,
                    key = { it.id }
                ) { item ->
                    TagItem(tag = item, onClick = {})
                }
            }

            state.image?.let {
                if (it.isEmpty()) return@let
                // Display the image using AsyncImage
                Box {
                    AsyncImage(
                        model = it.toUri(),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(18.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .fillMaxWidth()
                            .sharedElement(
                                rememberSharedContentState("image/${noteId}"),
                                animatedVisibilityScope = animatedVisibilityScope
                            ),
                        contentScale = ContentScale.FillWidth
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                    ) {
                        IconButton(
                            onClick = onImageDelete
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                "delete image",
                                tint = Color.Red
                            )
                        }
                        IconButton(
                            onClick = onImageChange
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                "edit image",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }



            TextField(
                value = state.title,
                onValueChange = viewModel::onTitleChange,
                textStyle = MaterialTheme.typography.titleLarge,
                placeholder = {
                    Text(
                        "Title here...",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .sharedElement(
                        rememberSharedContentState("title/${noteId}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                isError = state.validationState.isTitleTooLong
            )
            TextField(
                value = state.body,
                onValueChange = viewModel::onBodyChange,
                textStyle = MaterialTheme.typography.bodyLarge,
                placeholder = {
                    Text(
                        "Body here...",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .sharedElement(
                        rememberSharedContentState("body/${noteId}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }

    }
}