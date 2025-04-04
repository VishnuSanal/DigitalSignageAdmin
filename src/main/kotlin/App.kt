import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val fontFamily = FontFamily(Font(resource = "poppins.ttf"))

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun App(mainViewModel: MainViewModel) {
    val coroutineScope = rememberCoroutineScope()

    val contentList by mainViewModel.announcements.collectAsState()

    var showAddEditDialog by remember { mutableStateOf(false) }
    var addEditDialogEditItem by remember { mutableStateOf<Announcement?>(null) }

    MaterialTheme {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row {
                Text(
                    modifier = Modifier.fillMaxWidth(0.9f).background(Constants.COLOR_BG),
                    text = "GEC Palakkad Digital Signage",
                    fontFamily = fontFamily,
                    fontSize = 60.sp,
                    fontWeight = FontWeight.W400,
                    lineHeight = 128.sp,
                    textAlign = TextAlign.Center,
                    color = Constants.COLOR_TEXT
                )

                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically).wrapContentWidth()
                        .fillMaxWidth().size(96.dp).aspectRatio(1f).background(Constants.COLOR_BG)
                        .padding(8.dp), onClick = {
                        addEditDialogEditItem = null
                        showAddEditDialog = true
                    }, content = {
                        Icon(
                            modifier = Modifier.wrapContentWidth().fillMaxWidth(),
                            painter = painterResource("add.png"),
                            tint = Constants.COLOR_TEXT,
                            contentDescription = "add icon"
                        )
                    })
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().align(Alignment.CenterHorizontally)
                    .background(Constants.COLOR_BG),
            ) {
                items(contentList) {
                    Card(
                        modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(8.dp),
                        backgroundColor = Constants.COLOR_CARD,
                        elevation = 36.dp,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row {
                            Column {
                                Text(
                                    modifier = Modifier.wrapContentHeight().fillMaxWidth(0.9f)
                                        .padding(vertical = 16.dp, horizontal = 32.dp),
                                    text = it.title,
                                    fontFamily = fontFamily,
                                    fontSize = 44.sp,
                                    fontWeight = FontWeight.W200,
                                    lineHeight = 64.sp,
                                    textAlign = TextAlign.Start,
                                    color = Constants.COLOR_TEXT
                                )

                                if (it.message != null)
                                    Text(
                                        modifier = Modifier.wrapContentHeight().fillMaxWidth(0.9f)
                                            .padding(vertical = 8.dp, horizontal = 32.dp),
                                        text = it.message!!,
                                        fontFamily = fontFamily,
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.W200,
                                        lineHeight = 44.sp,
                                        textAlign = TextAlign.Start,
                                        color = Constants.COLOR_TEXT
                                    )

                                if (it.imagePath != null)
                                    AsyncImage(
                                        modifier = Modifier
                                            .wrapContentHeight()
                                            .wrapContentWidth()
                                            .padding(16.dp),
                                        model = it.imagePath,
                                        contentDescription = "Image Listing",
                                        placeholder = painterResource("error.png"),
                                        error = painterResource("error.png"),
                                    )
                            }

                            IconButton(
                                modifier = Modifier.align(Alignment.CenterVertically)
                                    .padding(16.dp).size(48.dp).aspectRatio(1f), onClick = {
                                    coroutineScope.launch(Dispatchers.Default) {
                                        mainViewModel.delete(it)
                                    }
                                }, content = {
                                    Icon(
                                        modifier = Modifier.wrapContentWidth().fillMaxWidth(),
                                        painter = painterResource("delete.png"),
                                        tint = Constants.COLOR_TEXT,
                                        contentDescription = "delete icon"
                                    )
                                })

                            IconButton(
                                modifier = Modifier.align(Alignment.CenterVertically)
                                    .padding(16.dp).size(48.dp).aspectRatio(1f), onClick = {
                                    coroutineScope.launch(Dispatchers.Default) {
                                        addEditDialogEditItem = it
                                        showAddEditDialog = true
                                    }
                                }, content = {
                                    Icon(
                                        modifier = Modifier.wrapContentWidth().fillMaxWidth(),
                                        painter = painterResource("edit.png"),
                                        tint = Constants.COLOR_TEXT,
                                        contentDescription = "edit icon"
                                    )
                                })
                        }
                    }
                }
            }
        }
    }

    if (showAddEditDialog) {
        Dialog(
            onDismissRequest = {
                addEditDialogEditItem = null
                showAddEditDialog = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnClickOutside = true,
                dismissOnBackPress = true,
            ),
        ) {

            var announcementType by remember { mutableStateOf("Text") }

            var announcementTitle by remember { mutableStateOf(addEditDialogEditItem?.title ?: "") }
            var announcementMessage by remember {
                mutableStateOf(
                    addEditDialogEditItem?.message ?: ""
                )
            }
            var announcementImageLink by remember {
                mutableStateOf(
                    addEditDialogEditItem?.imagePath ?: ""
                )
            }
            var isImageValid by remember { mutableStateOf(false) }

            var buttonText by remember {
                mutableStateOf(
                    if (announcementType == "Image")
                        "Preview"
                    else
                        "Confirm"
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(0.5f).wrapContentHeight()
                    .padding(vertical = 64.dp, horizontal = 32.dp),
                shape = RoundedCornerShape(16.dp),
                backgroundColor = Constants.COLOR_BG,
            ) {
                Column(
                    modifier = Modifier.wrapContentWidth().wrapContentHeight().padding(16.dp),
                ) {

                    Text(
                        modifier = Modifier,
                        text = "New Announcement",
                        fontFamily = fontFamily,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.W200,
                        lineHeight = 48.sp,
                        textAlign = TextAlign.Center,
                        color = Constants.COLOR_TEXT
                    )

                    OutlinedTextField(
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp),
                        value = announcementTitle,
                        label = {
                            Text(
                                modifier = Modifier,
                                text = "Title",
                                fontFamily = fontFamily,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W100,
                                lineHeight = 16.sp,
                                textAlign = TextAlign.Center,
                                color = Constants.COLOR_TEXT
                            )
                        },
                        textStyle = TextStyle(
                            fontFamily = fontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W100,
                            lineHeight = 16.sp,
                            textAlign = TextAlign.Start,
                            color = Constants.COLOR_TEXT
                        ),
                        onValueChange = {
                            announcementTitle = it
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            backgroundColor = Constants.COLOR_BG,
                            textColor = Constants.COLOR_TEXT,
                            unfocusedLabelColor = Constants.COLOR_TEXT,
                            focusedLabelColor = Constants.COLOR_TEXT,
                            placeholderColor = Constants.COLOR_TEXT,
                            leadingIconColor = Constants.COLOR_TEXT,
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    )

                    // todo: handle edits for images and videos
                    Row(Modifier.align(Alignment.CenterHorizontally)) {
                        listOf("Text", "Image", "Video").forEach {
                            FilterChip(
                                modifier = Modifier.padding(8.dp),
                                onClick = {
                                    announcementType = it
                                },
                                selected = announcementType.equals(it),
                                leadingIcon = if (announcementType.equals(it)) {
                                    {
                                        Icon(
                                            modifier = Modifier
                                                .padding(4.dp)
                                                .clip(CircleShape)
                                                .background(Constants.COLOR_BG)
                                                .padding(4.dp)
                                                .align(Alignment.CenterVertically),
                                            imageVector = Icons.Filled.Done,
                                            contentDescription = "Done icon",
                                        )
                                    }
                                } else {
                                    null
                                },
                                colors = ChipDefaults.filterChipColors(
                                    backgroundColor = Constants.COLOR_CARD,
                                    contentColor = Constants.COLOR_TEXT,
                                ),
                                content = {
                                    Text(
                                        modifier = Modifier,
                                        text = it,
                                        fontFamily = fontFamily,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.W200,
                                        lineHeight = 24.sp,
                                        textAlign = TextAlign.Center,
                                        color = Constants.COLOR_TEXT
                                    )
                                },
                            )
                        }
                    }

                    when (announcementType) {
                        "Text" -> {
                            OutlinedTextField(
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                                    .padding(16.dp),
                                value = announcementMessage,
                                label = {
                                    Text(
                                        modifier = Modifier,
                                        text = "Message",
                                        fontFamily = fontFamily,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.W100,
                                        lineHeight = 16.sp,
                                        textAlign = TextAlign.Center,
                                        color = Constants.COLOR_TEXT
                                    )
                                },
                                textStyle = TextStyle(
                                    fontFamily = fontFamily,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W100,
                                    lineHeight = 16.sp,
                                    textAlign = TextAlign.Start,
                                    color = Constants.COLOR_TEXT
                                ),
                                onValueChange = {
                                    announcementMessage = it
                                },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    backgroundColor = Constants.COLOR_BG,
                                    textColor = Constants.COLOR_TEXT,
                                    unfocusedLabelColor = Constants.COLOR_TEXT,
                                    focusedLabelColor = Constants.COLOR_TEXT,
                                    placeholderColor = Constants.COLOR_TEXT,
                                    leadingIconColor = Constants.COLOR_TEXT,
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            )
                        }

                        "Image" -> {
                            OutlinedTextField(
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                                    .padding(16.dp),
                                value = announcementImageLink,
                                label = {
                                    Text(
                                        modifier = Modifier,
                                        text = "Valid Image Link",
                                        fontFamily = fontFamily,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.W100,
                                        lineHeight = 16.sp,
                                        textAlign = TextAlign.Center,
                                        color = Constants.COLOR_TEXT,
                                    )
                                },
                                textStyle = TextStyle(
                                    fontFamily = fontFamily,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W100,
                                    lineHeight = 16.sp,
                                    textAlign = TextAlign.Start,
                                    color = Constants.COLOR_TEXT
                                ),
                                onValueChange = {
                                    announcementImageLink = it
                                },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    backgroundColor = Constants.COLOR_BG,
                                    textColor = Constants.COLOR_TEXT,
                                    unfocusedLabelColor = Constants.COLOR_TEXT,
                                    focusedLabelColor = Constants.COLOR_TEXT,
                                    placeholderColor = Constants.COLOR_TEXT,
                                    leadingIconColor = Constants.COLOR_TEXT,
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                            )

                            if (announcementType == "Image")
                                AsyncImage(
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                        .padding(16.dp),
                                    model = announcementImageLink,
                                    contentDescription = "Image Preview",
                                    placeholder = painterResource("error.png"),
                                    error = painterResource("error.png"),
                                    onSuccess = { isImageValid = true },
                                    onError = { isImageValid = false },
                                    onLoading = { isImageValid = false },
                                )
                        }
                    }

                    TextButton(
                        modifier = Modifier.align(Alignment.End).padding(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = Constants.COLOR_CARD
                        ),
                        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
                        onClick = {
                            coroutineScope.launch(Dispatchers.Default) {

                                if (
                                    announcementTitle.isBlank() ||
                                    (announcementType == "Image" && (announcementImageLink.isBlank() || !isImageValid))
                                ) {
                                    buttonText = "Invalid Inputs!!"
                                    delay(1000)
                                    buttonText = "Submit"

                                    return@launch
                                }

                                val announcement = Announcement(title = announcementTitle.trim())

                                when (announcementType) {
                                    "Text" -> announcement.message = announcementMessage.trim()
                                    "Image" -> announcement.imagePath = announcementImageLink.trim()
                                }

                                if (addEditDialogEditItem != null)
                                    mainViewModel.edit(announcement, addEditDialogEditItem!!)
                                else
                                    mainViewModel.add(announcement)

                                showAddEditDialog = false
                            }
                        },
                        content = {
                            Text(
                                modifier = Modifier,
                                text = buttonText,
                                fontFamily = fontFamily,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W200,
                                lineHeight = 24.sp,
                                textAlign = TextAlign.Center,
                                color = Constants.COLOR_TEXT
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun AuthScreen(mainViewModel: MainViewModel, onAuthSuccess: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().fillMaxHeight(), verticalArrangement = Arrangement.Center
    ) {

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Sign in",
            fontSize = 32.sp,
            fontWeight = FontWeight.Medium
        )

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordVisibility: Boolean by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()

        OutlinedTextField(
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .padding(16.dp, 16.dp, 16.dp, 0.dp).fillMaxWidth(0.5f),
            value = email,
            onValueChange = {
                email = it
            },
            label = { Text("Email ID") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = @Composable { Icon(painterResource("badge.png"), "message icon") }
        )

        OutlinedTextField(
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .padding(16.dp, 16.dp, 16.dp, 0.dp).fillMaxWidth(0.5f),
            value = password,
            onValueChange = {
                password = it
            },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            leadingIcon = @Composable { Icon(painterResource("padlock.png"), "padlock icon") },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = @Composable {
                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    if (passwordVisibility) Icon(painterResource("pass_on.png"), "")
                    else Icon(painterResource("pass_off.png"), "")
                }
            }
        )

        var buttonText by remember { mutableStateOf("Login") }

        TextButton(
            modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth(0.5f)
                .padding(16.dp, 16.dp, 16.dp, 0.dp),
            onClick = {
                buttonText = "Please Wait..."

                coroutineScope.launch {
                    val success = mainViewModel.signIn(email, password).await()
                    if (success) {
                        onAuthSuccess()
                        buttonText = "Success!"
                    } else {
                        buttonText = "Authentication Failed"
                        delay(1000)
                        buttonText = "Login"
                    }
                }
            },
            content = {
                Text(text = buildAnnotatedString {
                    withStyle(SpanStyle(color = Color.White)) {
                        append(buttonText)
                    }
                }, fontSize = 16.sp, fontWeight = FontWeight.Light)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF292D32)),
            shape = RoundedCornerShape(32.dp)
        )
    }
}