package com.example.textntalk;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechToTextUtil {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private Activity activity;
    private EditText targetEditText;

    public SpeechToTextUtil(Activity activity, EditText targetEditText) {
        this.activity = activity;
        this.targetEditText = targetEditText;
    }

    public void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");

        try {
            activity.startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(activity, "Speech recognition is not supported on this device.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_CODE_SPEECH_INPUT && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                targetEditText.setText(result.get(0));
            }
        }
    }
}
