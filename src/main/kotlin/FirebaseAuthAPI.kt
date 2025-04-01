import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

data class AuthRequest(
    val email: String,
    val password: String,
    val returnSecureToken: Boolean = true
)

data class AuthResponse(
    val idToken: String,
    val email: String,
    val refreshToken: String,
    val localId: String
)

interface FirebaseAuthApi {
    @POST("v1/accounts:signInWithPassword")
    suspend fun signIn(
        @Query("key") apiKey: String,
        @Body request: AuthRequest
    ): Response<AuthResponse>
}