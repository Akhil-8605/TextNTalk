package com.example.textntalk;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class LanguageTranslator {
    private Translator translator;
    private Context context;

    public LanguageTranslator(Context context, String sourceLanguage, String targetLanguage) {
        this.context = context;
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(sourceLanguage)
                .setTargetLanguage(targetLanguage)
                .build();
        translator = com.google.mlkit.nl.translate.Translation.getClient(options);
    }

    public void translateText(String input, final TranslationCallback callback) {
        if (!isNetworkAvailable()) {
            Toast.makeText(context, "No internet connection.", Toast.LENGTH_SHORT).show();
        }

        translator.translate(input)
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String translatedText) {
                        callback.onSuccess(translatedText);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure(e);
                        Toast.makeText(context, "Translation failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public interface TranslationCallback {
        void onSuccess(String translatedText);
        void onFailure(Exception e);
    }
}
