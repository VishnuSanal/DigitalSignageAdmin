import androidx.compose.ui.graphics.Color
import okhttp3.logging.HttpLoggingInterceptor

object Constants {

    val COLOR_BG = Color(0xFF202020)
    val COLOR_CARD = Color(0xFF121214)
    val COLOR_TEXT = Color.White

    val LOGLEVEL: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BASIC

    const val AUTH_BASE_URL = "https://identitytoolkit.googleapis.com/"

    const val DB_BASE_URL = "https://digital-signage-gec-pkd-default-rtdb.firebaseio.com/"
    const val BAZOOKA = "AIzaSyBZeG7q88OTGWZgaT4UiqOIqW0NR8xaKwQ"

//    const val BAZOOKA = "AIzaSyCrY-F5OneuA-RqmcAVQ76MZYahePCTtqQ" // DEBUG
//    const val DB_BASE_URL = "https://gedigitalsignage-gecpkd-default-rtdb.firebaseio.com/" // DEBUG
}