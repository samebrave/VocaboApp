package sam.projects.vocaboapp

import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApiService {
    @GET("entries/en/{word}")
    suspend fun getDefinition(@Path("word") word: String): List<DictionaryResponse>
}