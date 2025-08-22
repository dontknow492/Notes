package org.ghost.notes.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import org.ghost.notes.ui.screen.CreateTagDialog
import org.ghost.notes.ui.screen.detail.DetailedNoteScreen
import org.ghost.notes.ui.screen.home.NoteListScreen

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MyAppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: ScreenNavigation = ScreenNavigation.Home
) {
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            composable<ScreenNavigation.Home> {
                val onNoteClick: (Int) -> Unit = {
                    navController.navigate(ScreenNavigation.DetailScreenNavigation(it))
                }
                val onAddNewClick: () -> Unit = {
                    navController.navigate(ScreenNavigation.DetailScreenNavigation(-1))
                }

                NoteListScreen(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    onNoteClick = onNoteClick,
                    onAddNewClick = onAddNewClick
                )
            }
            composable<ScreenNavigation.DetailScreenNavigation> {
                DetailedNoteScreen(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this
                ) {
                    navController.popBackStack()
                }
            }

            dialog<ScreenNavigation.TagScreenNavigation> {
                CreateTagDialog()
            }
        }
    }

}