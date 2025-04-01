import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _announcements = MutableStateFlow<List<Announcement>>(
        listOf(
            Announcement(
                title = "Loading..."
            )
        )
    )
    val announcements: StateFlow<List<Announcement>> = _announcements

    private var authToken: String? = null

    init {
        logger.info("init")
        fetch()
    }

    fun signIn(email: String, password: String): Deferred<Boolean> {
        return viewModelScope.async(Dispatchers.Default) {
            try {
                val response =
                    firebaseAuthAPI.signIn(Constants.BAZOOKA, AuthRequest(email, password))

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val body = response.body()!!
                        authToken = body.idToken

                        logger.info("User signed in: ${body.idToken}")

                        return@async true
                    } else {
                        logger.error("Sign-in failed: ${response.errorBody()}")
                    }
                }
            } catch (e: Exception) {
                logger.error("Sign-in failed: ${e.message}")
            }
            return@async false
        }
    }

    fun getAuthToken(): String? = authToken

    fun fetch() {
        viewModelScope.launch(Dispatchers.Default) {
            try {

                val response = firebaseDatabaseAPI.getAnnouncements()

                if (response.isSuccessful) {
                    if (response.body() != null) {

                        _announcements.value = response.body()!!

                        logger.info("Announcements fetched: " + _announcements.value.toList())
                    } else {
                        _announcements.value = listOf(
                            Announcement(
                                title = "No Announcements"
                            )
                        )

                        logger.info("Announcements empty")
                    }
                }
            } catch (e: Exception) {
                logger.error("Network fetch failed", e)

                _announcements.value = listOf(
                    Announcement(
                        title = "No connection",
                        message = "Please connect to the internet."
                    )
                )
            }
        }
    }

    fun add(announcement: Announcement) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val newList = ArrayList(_announcements.value.toList())

                // terrible hack, fix it if you see this!
                newList.remove(Announcement(title = "No Announcements"))
                newList.remove(
                    Announcement(
                        title = "No connection",
                        message = "Please connect to the internet."
                    )
                )

                newList.add(announcement)

                if (getAuthToken() != null)
                    firebaseDatabaseAPI.setAnnouncements(newList)
                else
                    logger.error("Add: User not authenticated")

                fetch()
            } catch (e: Exception) {
                logger.error("Network fetch failed", e)
            }
        }
    }

    fun edit(announcement: Announcement, oldAnnouncement: Announcement) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val newList = ArrayList(_announcements.value.toList())

                // terrible hack, fix it if you see this!
                newList.remove(Announcement(title = "No Announcements"))
                newList.remove(
                    Announcement(
                        title = "No connection",
                        message = "Please connect to the internet."
                    )
                )

                newList.remove(oldAnnouncement)
                newList.add(announcement)

                if (getAuthToken() != null)
                    firebaseDatabaseAPI.setAnnouncements(newList)
                else
                    logger.error("Edit: User not authenticated")

                fetch()
            } catch (e: Exception) {
                logger.error("Network fetch failed", e)
            }
        }
    }

    fun delete(announcement: Announcement) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val newList = ArrayList(_announcements.value.toList())

                // terrible hack, fix it if you see this!
                newList.remove(Announcement(title = "No Announcements"))
                newList.remove(
                    Announcement(
                        title = "No connection",
                        message = "Please connect to the internet."
                    )
                )

                newList.remove(announcement)

                if (getAuthToken() != null)
                    firebaseDatabaseAPI.setAnnouncements(newList)
                else
                    logger.error("Delete: User not authenticated")

                fetch()
            } catch (e: Exception) {
                logger.error("Network fetch failed", e)
            }
        }
    }
}
