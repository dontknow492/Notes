package org.ghost.notes.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface ScreenNavigation {
    @Serializable
    object Home : ScreenNavigation

    @Serializable
    data class DetailScreenNavigation(val noteId: Int) : ScreenNavigation

    @Serializable
    object TagScreenNavigation : ScreenNavigation

}