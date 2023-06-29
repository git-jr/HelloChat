package com.paradoxo.hellochat.mlkit

import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

private const val LANGUAGE_TAG = "LanguageIdentification"
private const val TRANSLATE_TAG = "Translate"

class TextTranslate {

    fun translateText(
        text: String,
        targetLanguage: String,
        sourceLanguage: String,
        onSuccessful: (String) -> Unit = {},
        onFailiure: () -> Unit = {},
    ) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguage)
            .setTargetLanguage(targetLanguage)
            .build()

        val translator = Translation.getClient(options)

        with(translator) {
            verifyIfModelAreAvaliable(
                sourceLanguage = targetLanguage,
                targetLanguage = sourceLanguage,
                onMoldelAvaliable = {
                    translate(text)
                        .addOnSuccessListener { translatedText ->
                            onSuccessful(translatedText)
                        }
                        .addOnFailureListener { exception ->
                            Log.e(TRANSLATE_TAG, "Translation failed: $exception")
                            onFailiure()
                        }
                },
                onModelNotAvaliable = {
                    downloadLanguageModel(
                        sourceLanguage = targetLanguage,
                        targetLanguage = sourceLanguage,
                        onSuccessful = {
                            translate(text)
                                .addOnSuccessListener { translatedText ->
                                    onSuccessful(translatedText)
                                }
                                .addOnFailureListener { exception ->
                                    Log.e(TRANSLATE_TAG, "Translation failed: $exception")
                                    onFailiure()
                                }
                        },
                        onFailiure = {
                            onFailiure()
                        }
                    )
                }
            )

        }

    }

    private fun verifyIfModelAreAvaliable(
        sourceLanguage: String,
        targetLanguage: String,
        onMoldelAvaliable: () -> Unit = {},
        onModelNotAvaliable: () -> Unit
    ) {

        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguage)
            .setTargetLanguage(targetLanguage)
            .build()
        val translator = Translation.getClient(options)

        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                onMoldelAvaliable()
            }
            .addOnFailureListener { exception ->
                onModelNotAvaliable()
                Log.e(TRANSLATE_TAG, "Model not avaliable: $exception")
            }

    }


    private fun downloadLanguageModel(
        sourceLanguage: String,
        targetLanguage: String,
        onSuccessful: () -> Unit = {},
        onFailiure: () -> Unit = {}
    ) {
        val modelManager = RemoteModelManager.getInstance()

        val sourceModel = TranslateRemoteModel.Builder(sourceLanguage).build()
        val targetModel = TranslateRemoteModel.Builder(targetLanguage).build()

        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        modelManager.download(sourceModel, conditions)
            .addOnSuccessListener {
                modelManager.download(targetModel, conditions)
                    .addOnSuccessListener {
                        onSuccessful()
                    }
                    .addOnFailureListener {
                        onFailiure()
                        Log.e(TRANSLATE_TAG, "Error downloading targetModel: $it")
                    }
            }
            .addOnFailureListener {
                onFailiure()
                Log.e(TRANSLATE_TAG, "Error downloading model: $it")
            }
    }

    fun indentifyLanguage(
        text: String,
        onSuccessful: (String) -> Unit = {},
        onFailiure: () -> Unit = {}
    ) {
        val logTag = LANGUAGE_TAG

        val languageIdentifier = LanguageIdentification
            .getClient()

        languageIdentifier.identifyLanguage(text)
            .addOnSuccessListener { languageCode ->
                if (languageCode == "und") {
                    Log.i(logTag, "Can't identify language.")
                    onFailiure()
                } else {
                    onSuccessful(languageCode)
                }
            }
            .addOnFailureListener {
                onFailiure()
            }

        languageIdentifier.identifyPossibleLanguages(text)
            .addOnSuccessListener { identifiedLanguages ->
                for (identifiedLanguage in identifiedLanguages) {
                    val language = identifiedLanguage.languageTag
                    val confidence = (identifiedLanguage.confidence * 100).toInt()

                    Log.i(logTag, "Detected language: $language - Probability: $confidence%")
                }
            }
    }

}