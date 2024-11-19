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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.hashem.bugit.Utils
import com.hashem.bugit.ui.ui.theme.BugItTheme
import kotlinx.coroutines.launch


internal class BugItActivity : ComponentActivity() {
    private val viewModel by viewModels<BugItViewModel>(factoryProducer = {
        BugItViewModel.Factory
    })

    companion object {
        private const val EXTRA_BUG_IMAGE = "EXTRA_BUG_IMAGE"

        fun start(context: Context, images: ArrayList<Uri>) {
            val startIntent = Intent(context, BugItActivity::class.java)
                .putExtra(EXTRA_BUG_IMAGE, images)
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(startIntent)
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val images = intent.getSerializableExtra(EXTRA_BUG_IMAGE) as ArrayList<Uri>


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
                        images,
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

@Composable
internal fun MainView(
    modifier: Modifier = Modifier,
    images: List<Uri>,
    viewModel: BugItViewModel,
    onCloseClicked: () -> Unit,
) {
    val context = LocalContext.current

    val fields = remember {
        val array = viewModel.initialFields.map {
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
            HorizontalScrollScreen(viewModel.allowMultipleImage, images)

            fields.toSortedMap(
                compareBy { it.key }
            ).forEach { (key, value) ->
                OutlinedTextField(
                    value = value,
                    singleLine = true,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    onValueChange = {
                        fields[key] = it
                    },
                    label = { Text(key.value) },
                    isError = false,
                )
            }

            Button(
//            enabled = buttonEnabled,
                onClick = {
                    viewModel.reportBug(
                        Utils.getFilePathFromURI(context, images.first()) ?: "",
                        fields.map {
                            it.key.value to it.value
                        }.toMap()
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
fun HorizontalScrollScreen(allowMultipleImage: Boolean, uris: List<Uri>) {
    // a wrapper to fill the entire screen
    Box(modifier = Modifier.fillMaxWidth()) {
        // BowWithConstraints will provide the maxWidth used below
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                state = rememberLazyListState()
            ) {
                items(uris) { item ->
                    Card(
                        modifier = Modifier
                            .width(if (uris.size > 1) maxWidth - (maxWidth / 8) else maxWidth)
                            .padding(8.dp)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(item)
                                .crossfade(true)
                                .build(),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.8f)
                                .clip(RoundedCornerShape(8.dp)),
                        )
                    }
                }
            }
        }
    }
}
