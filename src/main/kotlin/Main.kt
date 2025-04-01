import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.google.gson.GsonBuilder
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val firebaseDatabaseAPI: FirebaseDatabaseAPI =
    Retrofit.Builder().baseUrl(Constants.DB_BASE_URL).client(
        OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().setLevel(Constants.LOGLEVEL)
        ).build()
    ).addConverterFactory(
        GsonConverterFactory.create(
            GsonBuilder().registerTypeAdapter(Announcement::class.java, AnnouncementAdapter())
                .setLenient().serializeNulls().create()
        )
    ).build().create(FirebaseDatabaseAPI::class.java);

val firebaseAuthAPI: FirebaseAuthApi =
    Retrofit.Builder().baseUrl(Constants.AUTH_BASE_URL).client(
        OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().setLevel(Constants.LOGLEVEL)
        ).build()
    ).addConverterFactory(GsonConverterFactory.create())
        .build().create(FirebaseAuthApi::class.java);

val logger: Logger = LoggerFactory.getLogger("DigitalSignageAdmin")

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        val mainViewModel = remember { MainViewModel() }

        var isAuthenticated by remember { mutableStateOf(mainViewModel.getAuthToken() != null) }

        if (isAuthenticated) {
            App(mainViewModel)
        } else {
            AuthScreen(mainViewModel) { isAuthenticated = true }
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
private fun publishDummyData() {
    GlobalScope.launch {
        firebaseDatabaseAPI.setAnnouncements(
            listOf(
                Announcement(
                    "College buses depart at 14:30 today",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas blandit vitae augue sed gravida. In condimentum pharetra placerat. Nulla at metus efficitur, tristique enim et, cursus nulla."
                ),
                Announcement(
                    "Classes are suspended from 15:00",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas blandit vitae augue sed gravida. In condimentum pharetra placerat. Nulla at metus efficitur, tristique enim et, cursus nulla."
                ),
                Announcement(
                    "Library will be open till 21:30 until exams are over",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas blandit vitae augue sed gravida. In condimentum pharetra placerat. Nulla at metus efficitur, tristique enim et, cursus nulla."
                ),
                Announcement(
                    "Knimbus Digital Library registration ends tomorrow",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas blandit vitae augue sed gravida. In condimentum pharetra placerat. Nulla at metus efficitur, tristique enim et, cursus nulla."
                ),
                Announcement(
                    "S2 exam registration deadline extended",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas blandit vitae augue sed gravida. In condimentum pharetra placerat. Nulla at metus efficitur, tristique enim et, cursus nulla."
                ),
                Announcement(
                    "Digital Signage system back up again!",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas blandit vitae augue sed gravida. In condimentum pharetra placerat. Nulla at metus efficitur, tristique enim et, cursus nulla."
                ),
                Announcement(
                    "Tomorrow is a holiday!",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas blandit vitae augue sed gravida. In condimentum pharetra placerat. Nulla at metus efficitur, tristique enim et, cursus nulla."
                ),
            )
        )
    }
}