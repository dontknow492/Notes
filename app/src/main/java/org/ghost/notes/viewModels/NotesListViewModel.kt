package org.ghost.notes.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ghost.notes.entity.Note
import org.ghost.notes.enums.SortBy
import org.ghost.notes.enums.SortOrder
import org.ghost.notes.repository.NotesRepository
import org.ghost.notes.state.NotesFilter
import javax.inject.Inject


@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val notesRepository: NotesRepository
) : ViewModel() {
    private val _filter = MutableStateFlow(NotesFilter())
    val filter = _filter.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val notes = filter.flatMapLatest { filter ->
        notesRepository.filterNotes(
            query = filter.query,
            tagId = filter.tagId,
            sortOrder = filter.sortOrder,
            sortBy = filter.sortBy
        ).cachedIn(viewModelScope)
    }


    fun deleteNote(note: Note) {
        viewModelScope.launch {
            notesRepository.deleteNote(note)
        }
    }

    fun updateQuery(query: String?) {
        updateFilter(filter.value.copy(query = query))
    }

    fun updateTagId(tagId: Int?) {
        updateFilter(filter.value.copy(tagId = tagId))
    }

    fun updateSortOrder(sortOrder: SortOrder) {
        updateFilter(filter.value.copy())
    }

    fun updateSortBy(sortBy: SortBy) {
        updateFilter(filter.value.copy())
    }

    fun updateOrder(sortOrder: SortOrder, sortBy: SortBy) {
        updateFilter(filter.value.copy(sortOrder = sortOrder, sortBy = sortBy))
    }

    private fun updateFilter(filter: NotesFilter) {
        _filter.update {
            filter
        }
    }


}

