package org.ghost.notes.ui.screen.home


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.ghost.notes.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showSystemUi = true)
fun NotesTopAppBar(
    modifier: Modifier = Modifier,
    search: String = "",
    onSearchChange: (String?) -> Unit = {},
    onFilterClick: () -> Unit = {}
) {
    var isSearching by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
    ) {
        LargeTopAppBar(
            title = {
                Column(
                    modifier.padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "Hello, \nCaroline",
                        style = MaterialTheme.typography.headlineLarge,
                    )
                    Text(
                        text = "Optimize you\n daily life with Notes",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                }
            },
            navigationIcon = {
                when (isSearching) {
                    true -> {
                        IconButton(onClick = { isSearching = false }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }

                    false -> {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                }
            },
            actions = {
                when (isSearching) {
                    true -> {
                        TextField(
                            value = search,
                            onValueChange = onSearchChange,
                            placeholder = {
                                Text("Search")
                            },
                            trailingIcon = {
                                IconButton(onClick = {
                                    if (search.isNotEmpty()) onSearchChange(null)
                                    else isSearching = false
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear"
                                    )
                                }
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent

                            ),
                            modifier = Modifier
                                .padding(start = 40.dp)
                                .weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                showKeyboardOnFocus = true,
                            )
                        )
                    }

                    false -> {
                        IconButton(onClick = { isSearching = true }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                    }
                }


                IconButton(onClick = onFilterClick) {
                    Icon(
                        painter = painterResource(R.drawable.rounded_filter_list_24),
                        contentDescription = "Filter"
                    )
                }
            },
            expandedHeight = 216.dp,
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier
                .clip(
                    shape = RoundedCornerShape(
                        bottomStart = MaterialTheme.shapes.extraLarge.bottomStart,
                        bottomEnd = MaterialTheme.shapes.extraLarge.bottomEnd,
                        topStart = CornerSize(0.dp),
                        topEnd = CornerSize(0.dp)
                    )
                )
        )
        Image(
            painter = painterResource(id = R.drawable.notes),
            contentDescription = "Notes",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .height(200.dp)
                .padding(end = 10.dp)

        )
    }

}