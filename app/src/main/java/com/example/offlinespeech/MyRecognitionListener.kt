package com.example.offlinespeech

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.util.Log
import android.content.Context

class MyRecognitionListener(cbu: CallBackUpdate, ctx: Context) : RecognitionListener {

    private var data = String()
    private val callBackUpdate = cbu
    private val context = ctx


    override fun onReadyForSpeech(params: Bundle) {
        Log.d("tag", "onReadyForSpeech")
        data = String()
    }

    override fun onBeginningOfSpeech() {
        Log.d("tag", "onBeginningOfSpeech")
    }

    override fun onRmsChanged(rmsdB: Float) {}

    override fun onBufferReceived(buffer: ByteArray?) {
        Log.d("tag", "onBufferReceived")
    }

    override fun onEndOfSpeech() {
        Log.d("tag", "onEndOfSpeech")
        callBackUpdate.onFinished()
    }

    override fun onError(error: Int) {
        val tmp = errorToString(error)
        Log.d("tag", "onError:$tmp")
        callBackUpdate.onError("Errore: $tmp")
        callBackUpdate.onFinished()
    }

    override fun onResults(results: Bundle?) {

        Log.d("tag", "onResults")

        //con onPartialResult l'ultima parola viene saltata (da testare meglio?)
        val stringResults = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        val size = stringResults?.size

        data = size?.let { stringResults[it-1] } ?: ""
        callBackUpdate.onUpdate(data)

    }

    override fun onPartialResults(partialResults: Bundle?) {

        Log.d("tag", "onPartialResults")

        val stringResults = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (stringResults != null) {
            for( str in stringResults){
                callBackUpdate.onUpdate(str)
            }
        }

    }

    override fun onEvent(eventType: Int, params: Bundle?) {
        Log.d("tag", "onEvent")
    }

    private fun errorToString(code: Int): String {
        val error = context.resources.getStringArray(R.array.ListenerErrors)
        return when (code) {
            SpeechRecognizer.ERROR_AUDIO -> error[0]
            SpeechRecognizer.ERROR_CLIENT -> error[1]
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> error[2]
            SpeechRecognizer.ERROR_NETWORK -> error[3]
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> error[4]
            SpeechRecognizer.ERROR_NO_MATCH -> error[5]
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> error[6]
            SpeechRecognizer.ERROR_SERVER -> error[7]
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> error[8]
            else -> error[9]
        }
    }
}