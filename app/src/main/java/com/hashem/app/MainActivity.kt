package com.hashem.app

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.hashem.app.ui.theme.BugItTheme
import com.hashem.bugit.BugIt
import com.hashem.bugit.Utils
import com.smarttoolfactory.screenshot.ScreenshotBox
import com.smarttoolfactory.screenshot.rememberScreenshotState

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bugConfig = BugIt.Config()
//            .useExternalConnector(externalConnector = object : BugDataSource {
//                override suspend fun report(
//                    imagePath: String,
//                    fields: Map<String, String>
//                ): BugData {
//                    return BugData(imagePath, fields)
//                }
//            })
            .allowMultipleImage(true)
            .addExtraField("02_priority", "Priority")
//                .addExtraField("03_department", "Department")
            .addExtraField("04_assignee", "Assignee")

        val bugit = BugIt.init(bugConfig).getInstance()

        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            BugItTheme {
                val context = LocalContext.current

                val screenshotState = rememberScreenshotState()
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
                }, floatingActionButtonPosition = FabPosition.Center, floatingActionButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
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
                            } else {
                                // Handle permission not granted
                                openAppSettings(context)
                            }
                        }

                        ExtendedFloatingActionButton(onClick = {
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
                                permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            else
                                galleryLauncher.launch("image/*")
                        }) {
                            Text("Pick from Gallery")
                        }

                        ExtendedFloatingActionButton(onClick = {
                            screenshotState.capture()

                        }) {
                            Text("Take Screenshot")
                        }

                        screenshotState.bitmapState.value?.let { bitmap ->
                            Utils.bitmapToUri(context, bitmap)?.let {
                                bugit.show(baseContext, arrayListOf(it))
                            }
                        }
                    }
                }) { padding ->
                    ScreenshotBox(screenshotState = screenshotState) {
                        Column(
                            modifier = Modifier
                                .background(Color.LightGray)
                                .fillMaxSize()
                                .padding(padding),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            MainView()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainView() {
    Text(
        text = "This screen has a bug", modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        textAlign = TextAlign.Center
    )

    Box(contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            modifier = Modifier
                .width(200.dp)
                .height(150.dp),
            contentDescription = null,
            alignment = Alignment.Center
        )
        Icon(Icons.Filled.Warning, contentDescription = "Close")
    }
}

fun openAppSettings(context: Context) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    )
    context.startActivity(intent)
}
