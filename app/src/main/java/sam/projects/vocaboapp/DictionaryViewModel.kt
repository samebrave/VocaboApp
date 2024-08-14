package sam.projects.vocaboapp

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DictionaryViewModel : ViewModel() {
    var word by mutableStateOf("")
    var definition by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf("")
    var previousSearches by mutableStateOf(listOf<String>())
    var imageUri by mutableStateOf<String?>(null)
    private val cache = mutableMapOf<String, String>()
    var bookmarks by mutableStateOf(listOf<String>())

    fun searchWord() {
        if (word.isBlank()) {
            error = "Please enter a word to search."
            return
        }

        if (cache.containsKey(word)) {
            definition = cache[word]!!
            return
        }

        viewModelScope.launch {
            isLoading = true
            error = ""
            try {
                val response = RetrofitInstance.api.getDefinition(word)
                if (response.isNotEmpty()) {
                    val meanings = response[0].meanings
                    var result = ""

                    meanings.take(2).forEachIndexed { index, meaning ->
                        if (index > 0) result += "\n\n"
                        result += "Meaning ${index + 1} (${meaning.partOfSpeech}):\n"
                        meaning.definitions.take(2).forEachIndexed { defIndex, def ->
                            result += "${defIndex + 1}. ${def.definition}\n"
                            if (def.example != null) {
                                result += "   Example: ${def.example}\n"
                            }
                        }
                    }

                    definition = result.trim()
                    cache[word] = definition
                    previousSearches = previousSearches.toMutableList().apply {
                        add(0, word)
                        if (size > 10) removeAt(size - 1)  // Keep only last 10 searches
                    }
                } else {
                    definition = "No definition found."
                }
            } catch (e: Exception) {
                error = "An error occurred: ${e.message}\nPlease check your internet connection or try again later."
                definition = ""
            }
            isLoading = false
        }
    }

    fun addBookmark(word: String) {
        bookmarks = bookmarks.toMutableList().apply {
            if (!contains(word)) add(word)
        }
    }

    fun removeBookmark(word: String) {
        bookmarks = bookmarks.toMutableList().apply {
            remove(word)
        }
    }
}