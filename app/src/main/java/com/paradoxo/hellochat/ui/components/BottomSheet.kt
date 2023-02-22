package com.paradoxo.hellochat.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.paradoxo.hellochat.R

@Composable
fun BottomSheetFiles(showAnotherSheet: () -> Unit = {}) {
    Column(
        Modifier
            .fillMaxWidth()
            .heightIn(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "File type", textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            IconButton(
                onClick = showAnotherSheet,
                Modifier.background(
                    color = Color(android.graphics.Color.MAGENTA),
                    shape = CircleShape
                )
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_action_gallery),
                    null,
                    modifier = Modifier
                        .weight(1f),
                    tint = androidx.compose.ui.graphics.Color.White
                )
            }
            IconButton(
                onClick = showAnotherSheet,
                Modifier.background(color = Color("#7600bc".toColorInt()), shape = CircleShape)
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_action_document),
                    null,
                    modifier = Modifier
                        .weight(1f),
                    tint = androidx.compose.ui.graphics.Color.White
                )
            }
            IconButton(
                onClick = showAnotherSheet,
                Modifier.background(color = Color("#ee6002".toColorInt()), shape = CircleShape)
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_action_audio),
                    null,
                    modifier = Modifier
                        .weight(1f),
                    tint = androidx.compose.ui.graphics.Color.White,
                )
            }
        }
    }
}

@Preview()
@Composable
fun BottomSheetFilesPreview() {
    BottomSheetFiles(showAnotherSheet = { })
}