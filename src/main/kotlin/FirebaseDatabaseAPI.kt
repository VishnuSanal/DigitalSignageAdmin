import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface FirebaseDatabaseAPI {
    @GET("/announcements.json/")
    suspend fun getAnnouncements(): Response<List<Announcement>>

    @PUT("/announcements.json/")
    suspend fun setAnnouncements(
        @Query("auth") authToken: String,
        @Body announcements: List<Announcement>
    )
}