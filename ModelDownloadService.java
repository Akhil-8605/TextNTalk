package com.example.textntalk;
import android.app.Service;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.HashMap;
import java.util.Map;

public class ModelDownloadService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isNetworkQualityGood()) {
            downloadAllModels();
        } else {
            Toast.makeText(this, "No Internet\nCheck network connection", Toast.LENGTH_SHORT).show();
        }
        return START_NOT_STICKY;
    }

    private void downloadAllModels() {
        Map<String, String> languagePairs = new HashMap<>();
        languagePairs.put("English", TranslateLanguage.ENGLISH);
        languagePairs.put("Hindi", TranslateLanguage.HINDI);
        languagePairs.put("Bengali", TranslateLanguage.BENGALI);
        languagePairs.put("Telugu", TranslateLanguage.TELUGU);
        languagePairs.put("Marathi", TranslateLanguage.MARATHI);
        languagePairs.put("Tamil", TranslateLanguage.TAMIL);

        for (String sourceLanguageName : languagePairs.keySet()) {
            String sourceLanguageCode = languagePairs.get(sourceLanguageName);

            for (String targetLanguageName : languagePairs.keySet()) {
                if (!sourceLanguageName.equals(targetLanguageName)) {
                    String targetLanguageCode = languagePairs.get(targetLanguageName);

                    TranslatorOptions options = new TranslatorOptions.Builder()
                            .setSourceLanguage(sourceLanguageCode)
                            .setTargetLanguage(targetLanguageCode)
                            .build();
                    Translator translator = com.google.mlkit.nl.translate.Translation.getClient(options);

                    DownloadConditions conditions = new DownloadConditions.Builder()
                            .requireWifi()
                            .build();

                    translator.downloadModelIfNeeded(conditions)
                            .addOnSuccessListener(aVoid -> {

                            })
                            .addOnFailureListener(e -> Toast.makeText(ModelDownloadService.this,
                                    "Failed to download model for " + sourceLanguageName + " to " + targetLanguageName + ": " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show());
                }
            }
        }
    }

    private boolean isNetworkQualityGood() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        if (capabilities == null) {
            return false;
        }

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }
}
