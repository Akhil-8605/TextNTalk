package com.example.textntalk;

import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class TextToSpeechUtil{

    private TextToSpeech textToSpeech;

    public TextToSpeechUtil(Activity activity) {
        textToSpeech = new TextToSpeech(activity, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.getDefault());
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TextToSpeechUtil", "Language not supported");
                }
            } else {
                Log.e("TextToSpeechUtil", "Initialization failed");
            }
        });
    }

    public void speak(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    public void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
