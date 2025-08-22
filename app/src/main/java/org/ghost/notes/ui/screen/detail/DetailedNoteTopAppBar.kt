package org.ghost.notes.ui.screen.detail

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedScreenTopAppBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onImageChange: () -> Unit = {},
    onAddNewTag: () -> Unit = {}
) {
    var showAddMenu by remember { mutableStateOf(false) }
    var showMoreMenu by remember { mutableStateOf(false) }

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
                onClick = {
                    showAddMenu = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Add"
                )
                DropdownMenu(
                    showAddMenu,
                    onDismissRequest = {
                        showAddMenu = false
                    }
                ) {
                    DropdownMenuItem(
                        text = { Text("Add tag") },
                        leadingIcon = { Icon(Icons.Default.Info, "add tag") },
                        onClick = {
                            onAddNewTag()
                            showAddMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Add image") },
                        leadingIcon = { Icon(Icons.Default.AccountBox, "add image") },
                        onClick = {
                            onImageChange()
                            showAddMenu = false
                        }
                    )
                }
            }
            IconButton(
                onClick = {
                    showMoreMenu = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More"
                )
                DropdownMenu(
                    showMoreMenu,
                    onDismissRequest = {
                        showMoreMenu = false
                    }
                ) {
                    DropdownMenuItem(
                        text = { Text("Export") },
                        leadingIcon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, "export") },
                        onClick = {}
                    )
                    DropdownMenuItem(
                        text = { Text("Share") },
                        leadingIcon = { Icon(Icons.Default.Share, "share") },
                        onClick = {}
                    )
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        leadingIcon = { Icon(Icons.Default.Delete, "delete") },
                        onClick = {}
                    )
                }
            }


        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier
    )
}