package com.hashem.bugit.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hashem.bugit.Utils
import com.hashem.bugit.ui.ui.theme.BugItTheme
import kotlinx.coroutines.launch


internal class BugItActivity : ComponentActivity() {
    private val viewModel by viewModels<BugItViewModel>(factoryProducer = {
        BugItViewModel.Factory
    })

    companion object {
        private const val EXTRA_BUG_IMAGE = "EXTRA_BUG_IMAGE"
        private const val EXTRA_BUG_FIELDS = "EXTRA_BUG_FIELDS"

        fun start(context: Context, image: Uri, fields: List<String>) {
            val startIntent = Intent(context, BugItActivity::class.java)
                .putExtra(EXTRA_BUG_IMAGE, image)
                .putExtra(EXTRA_BUG_FIELDS, fields.toTypedArray())
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(startIntent)
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val image = intent.getParcelableExtra<Uri>(EXTRA_BUG_IMAGE)
        val fields = intent.getStringArrayExtra(EXTRA_BUG_FIELDS)
        if (image == null || fields == null) {
            finish()
            return
        }

        enableEdgeToEdge()
        setContent {
            BugItTheme {
                val keyboardController = LocalSoftwareKeyboardController.current

                val scrollState = rememberScrollState()
                val coroutineScope = rememberCoroutineScope()
                val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

                LaunchedEffect(key1 = keyboardHeight) {
                    coroutineScope.launch {
                        scrollState.scrollBy(keyboardHeight.toFloat())
                    }
                }

                Scaffold(modifier = Modifier
                    .imePadding()
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = null,
                        indication = null
                    ) {
                        keyboardController?.hide()
                    }) {
                    MainView(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(16.dp),
                        image,
                        fields,
                        viewModel,
                        onCloseClicked = {
                            finish()
                        }
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainViewPreview() {
    BugItTheme {
//        MainView()
    }
}

@Composable
internal fun MainView(
    modifier: Modifier = Modifier,
    image: Uri,
    initialFields: Array<String>,
    viewModel: BugItViewModel,
    onCloseClicked: () -> Unit,
) {
    val context = LocalContext.current

    val fields = remember {
        val array = initialFields.map {
            it to ""
        }.toTypedArray()
        mutableStateMapOf(*array)
    }

    Column(
        Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Box(
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { onCloseClicked() }) {
                Icon(Icons.Filled.Close, contentDescription = "Close")
            }
            Text(
                "Report Bug",
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Column(modifier) {
            HorizontalScrollScreen(listOf(image, image, image))
//            BugImage(image)

            fields.toSortedMap().forEach { (key, value) ->
                OutlinedTextField(
                    value = value,
                    singleLine = true,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    onValueChange = {
                        fields[key] = it
                    },
                    label = { Text(key) },
                    isError = false,
                )
            }

            Button(
//            enabled = buttonEnabled,
                onClick = {
                    viewModel.reportBug(
                        Utils.getFilePathFromURI(context, image) ?: "",
                        fields
                    )
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp, bottom = 8.dp)
            ) {
                Text("Report Bug")
            }
        }
    }
}

@Composable
fun HorizontalScrollScreen(uri: List<Uri>) {
    // a wrapper to fill the entire screen
    Box(modifier = Modifier.fillMaxWidth()) {
        // BowWithConstraints will provide the maxWidth used below
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                state = rememberLazyListState()
            ) {
                items(uri) { item ->
                    Card(
                        modifier = Modifier
                            .width(maxWidth - (maxWidth / 8))
                            .padding(8.dp)
                    ) {
                        BugImage(item)
                    }
                }
            }
        }
    }
}

@Composable
fun BugImage(uri: Uri) {
    val context = LocalContext.current
    val bitmap = remember(uri) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            null
        }
    }

    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
        )
    }
}
