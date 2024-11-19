package com.hashem.app

import android.Manifest
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
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
        val bugit = BugIt.init(
            BugIt.Config()
                .allowMultipleImage(true)
                .addExtraField("02_priority", "Priority")
//                .addExtraField("03_department", "Department")
                .addExtraField("04_assignee", "Assignee")
        ).getInstance()

        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            BugItTheme {
                Scaffold(topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                "Demo App",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }, modifier = Modifier
                            .background(Color.Black)
                            .shadow(6.dp)
                    )
                }, floatingActionButtonPosition = FabPosition.End, floatingActionButton = {
                    Column(
                        modifier = Modifier, verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        val galleryLauncher =
                            if (!bugit.config.allowMultipleImage)
                                rememberLauncherForActivityResult(
                                    ActivityResultContracts.GetContent()
                                ) { uri: Uri? ->
                                    uri?.let { bugit.show(baseContext, arrayListOf(it)) }
                                }
                            else
                                rememberLauncherForActivityResult(
                                    ActivityResultContracts.GetMultipleContents()
                                ) { uris: List<Uri>? ->
                                    uris?.let { bugit.show(baseContext, ArrayList(it)) }
                                }

                        val permissionLauncher = rememberLauncherForActivityResult(
                            ActivityResultContracts.RequestPermission()
                        ) { isGranted ->
                            if (isGranted) {
                                galleryLauncher.launch("image/*")
                            }
                        }

                        ExtendedFloatingActionButton(onClick = {
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }) {
                            Text("Pick from Gallery")
                        }

//                        ExtendedFloatingActionButton(onClick = {
//                                bugit.show(baseContext)
//                        }) {
//                            Text("Take Screenshot")
//                        }
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
        text = "This screen has a bug", modifier = modifier
    )
}
