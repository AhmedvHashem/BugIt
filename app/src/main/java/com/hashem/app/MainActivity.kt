package com.hashem.app

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.hashem.app.ui.theme.BugItTheme
import com.hashem.bugit.BugIt

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bugit = BugIt.init(BugIt.Config().addExtraField("Priority")).getInstance()

        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            BugItTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    "Demo App",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            },
                            modifier = Modifier
                                .background(Color.Black)
                                .shadow(6.dp)
                        )
                    },
                    floatingActionButtonPosition = FabPosition.End,
                    floatingActionButton = {
                        Column(
                            modifier = Modifier,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            val galleryLauncher =
                                rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                                    uri?.let { bugit.show(baseContext, it) }
                                }
                            ExtendedFloatingActionButton(onClick = {
//                                bugit.show(baseContext)
                            }) {
                                Text("Take Screenshot")
                            }
                            ExtendedFloatingActionButton(onClick = {
                                galleryLauncher.launch("image/*")
                            }) {
                                Text("Pick from Gallery")
                            }
                        }
                    }) { padding ->
                    Column(modifier = Modifier.padding(padding)) {
                        MainView()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun MainViewPreview() {
    BugItTheme {
        MainView()
    }
}

@Composable
fun MainView(modifier: Modifier = Modifier.fillMaxWidth()) {
    Text(
        text = "This screen has a bug",
        modifier = modifier
    )
}
