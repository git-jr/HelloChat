package com.paradoxo.hellochat.mlkit

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class ImageLabeling(private val context: Context) {

    fun classifyImage(
        uri: Uri,
        onSuccessful: (MutableList<String>) -> Unit,
        onFailiure: () -> Unit
    ) {
        val image: InputImage = InputImage.fromFilePath(context, uri)
        val listlabels = mutableListOf<String>()

        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
        labeler.process(image)
            .addOnSuccessListener { labels ->
                for (label in labels) {
                    val text = label.text
                    val confidence = label.confidence
                    val index = label.index
                    Log.i("label", "$text $confidence $index")

                    listlabels.add(text)
                }

                if (labels.isNotEmpty()) {
                    onSuccessful(listlabels)
                } else {
                    onFailiure()
                }
            }
            .addOnFailureListener { e ->
                Log.i("label", e.toString())
            }
    }
}