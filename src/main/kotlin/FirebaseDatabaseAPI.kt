import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface FirebaseDatabaseAPI {
    @GET("/notifications.json/")
    suspend fun getNotifications(): Response<List<String>>

    @PUT("/notifications.json/")
    suspend fun setNotifications(
        @Body classList: List<String>
    )
}