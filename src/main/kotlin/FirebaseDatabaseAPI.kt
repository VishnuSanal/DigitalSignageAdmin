import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface FirebaseDatabaseAPI {
    @GET("/announcements.json/")
    suspend fun getAnnouncements(): Response<List<Announcement>>

    @PUT("/announcements.json/")
    suspend fun setAnnouncements(
        @Body announcements: List<Announcement>
    )
}