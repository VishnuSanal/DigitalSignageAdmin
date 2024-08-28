import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val firebaseDatabaseAPI: FirebaseDatabaseAPI =
    Retrofit.Builder().baseUrl(Constants.DB_BASE_URL).client(
        OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().setLevel(Constants.LOGLEVEL)
        ).build()
    ).addConverterFactory(
        GsonConverterFactory.create(
            GsonBuilder().setLenient().serializeNulls().create()
        )
    ).build().create(FirebaseDatabaseAPI::class.java);

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()

        /*
        GlobalScope.launch {
            firebaseDatabaseAPI.setAnnouncements(
                listOf(
                    Announcement("College buses depart at 14:30 today"),
                    Announcement("Classes are suspended from 15:00"),
                    Announcement("Library will be open till 21:30 until exams are over"),
                    Announcement("Knimbus Digital Library registration ends tomorrow"),
                    Announcement("S2 exam registration deadline extended"),
                    Announcement("Digital Signage system back up again!"),
                    Announcement("Tomorrow is a holiday!"),
                )
            )
        }
        */

    }
}
