import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val fontFamily = FontFamily(Font(resource = "poppins.ttf"))

@Composable
@Preview
fun App() {

    val coroutineScope = rememberCoroutineScope()

    val contentList = remember { mutableStateListOf<String>("Loading...") }

    coroutineScope.launch(Dispatchers.Default) {
        val response = firebaseDatabaseAPI.getNotifications()

        if (response.isSuccessful && response.body() != null) {
            contentList.clear()
            contentList.addAll(response.body()!!)
        }
    }

    MaterialTheme {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally)
                    .background(Constants.COLOR_BG),
            ) {
                items(contentList) {
                    Card(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .padding(8.dp),
                        backgroundColor = Constants.COLOR_CARD,
                        elevation = 36.dp,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row {
                            Text(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .fillMaxWidth(0.9f)
                                    .padding(32.dp),
                                text = it,
                                fontFamily = fontFamily,
                                fontSize = 44.sp,
                                fontWeight = FontWeight.W200,
                                lineHeight = 64.sp,
                                textAlign = TextAlign.Center,
                                color = Constants.COLOR_TEXT
                            )

                            IconButton(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .wrapContentWidth()
                                    .fillMaxWidth()
                                    .size(48.dp)
                                    .aspectRatio(1f)
                                    .padding(8.dp),
                                onClick = {
                                    coroutineScope.launch(Dispatchers.Default) {
                                        contentList.remove(it)

                                        firebaseDatabaseAPI.setNotifications(contentList)

                                        val response = firebaseDatabaseAPI.getNotifications()

                                        if (response.isSuccessful && response.body() != null) {
                                            contentList.clear()
                                            contentList.addAll(response.body()!!)
                                        }
                                    }
                                },
                                content = {
                                    Icon(
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .fillMaxWidth(),
                                        painter = painterResource("delete.png"),
                                        tint = Constants.COLOR_TEXT,
                                        contentDescription = "delete icon"
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}