package org.ghost.notes.ui.screen

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import org.ghost.notes.R
import org.ghost.notes.entity.Tag
import org.ghost.notes.viewModels.DetailedNoteViewModel


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailedNoteScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailedNoteViewModel = hiltViewModel(),
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBack: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { DetailedScreenTopAppBar(onBack = onBack) },
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
            heading = state.heading,
            title = state.title,
            body = state.body,
            date = state.date, // Example date: March 15, 2023
            image = state.image,
            tags = state.tags,
            onBodyChange = viewModel::onBodyChange,
            onHeadingChange = viewModel::onHeadingChange,
            onTitleChange = viewModel::onTitleChange,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedScreenTopAppBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = "Theme"
                )
            }
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Add"
                )
            }
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More"
                )
            }


        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier
    )
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailedNote(
    modifier: Modifier = Modifier,
    heading: String? = null,
    title: String? = null,
    body: String? = null,
    date: Long = System.currentTimeMillis(),
    image: String? = null,
    tags: List<Tag> = emptyList(),
    onHeadingChange: (String) -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onBodyChange: (String) -> Unit = {},
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState(0))
    ) {
        with(sharedTransitionScope) {
            TextField(
                value = heading ?: "",
                onValueChange = onHeadingChange,
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
                        rememberSharedContentState("heading"),
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
            Text(
                "10 October, 2023",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 18.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(horizontal = 18.dp)
            ) {
                items(
                    items = tags,
                    key = { it.id }
                ) { item ->
                    TagItem(tag = item, onClick = {})
                }
            }

            image?.let {
                AsyncImage(
                    model = R.drawable.notes,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(18.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .fillMaxWidth()
                        .sharedElement(
                            rememberSharedContentState("image"),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
                    contentScale = ContentScale.FillWidth
                )
            }



            TextField(
                value = title ?: "",
                onValueChange = onTitleChange,
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
                        rememberSharedContentState("title"),
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
            TextField(
                value = body ?: "",
                onValueChange = onBodyChange,
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
                        rememberSharedContentState("body"),
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