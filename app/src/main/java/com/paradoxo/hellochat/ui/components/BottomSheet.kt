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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paradoxo.hellochat.R

@Composable
fun BottomSheetFiles(
    showAnotherSheet: () -> Unit = {},
    onSelectImage: () -> Unit = {},
) {
    Column(
        Modifier
            .fillMaxWidth()
            .heightIn(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "File type",
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.dark_blue),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            IconButton(
                onClick = onSelectImage,
                Modifier.background(
                    color = colorResource(id = R.color.medium_blue),
                    shape = CircleShape
                )
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_action_gallery),
                    null,
                    modifier = Modifier
                        .weight(1f),
                    tint = Color.White,
                )
            }
            IconButton(
                onClick = showAnotherSheet,
                Modifier.background(
                    color = colorResource(id = R.color.medium_blue),
                    shape = CircleShape
                )
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_action_document),
                    null,
                    modifier = Modifier
                        .weight(1f),
                    tint = Color.White,
                )
            }
            IconButton(
                onClick = showAnotherSheet,
                Modifier.background(
                    color = colorResource(id = R.color.medium_blue),
                    shape = CircleShape
                )
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_action_audio),
                    null,
                    modifier = Modifier
                        .weight(1f),
                    tint = Color.White,
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetFilesPreview() {
    BottomSheetFiles(showAnotherSheet = { })
}