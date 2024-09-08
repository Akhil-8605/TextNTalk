package com.example.textntalk;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.mlkit.nl.translate.TranslateLanguage;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private EditText edtLanguage;
    private TextView translateLanguageTV;
    private Spinner sourceLanguageSpinner, targetLanguageSpinner;
    private Map<String, String> languageMap;
    private String sourceLanguageCode, targetLanguageCode;

    private LanguageTranslator languageTranslator;
    private TextToSpeechUtil textToSpeechUtil;
    private SpeechToTextUtil speechToTextUtil;

    private ImageToText imageToText;

    private boolean shouldExit = false;

    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=findViewById(R.id.TopToolbar);
        setSupportActionBar(toolbar);

        drawer=findViewById(R.id.drawer_layout);
        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        startService(new Intent(this, ModelDownloadService.class));

        edtLanguage = findViewById(R.id.idEdtLanguage);
        translateLanguageTV = findViewById(R.id.idTVTranslatedLanguage);
        Button translateLanguageBtn = findViewById(R.id.idBtnTranslateLanguage);
        sourceLanguageSpinner = findViewById(R.id.sourceLanguageSpinner);
        targetLanguageSpinner = findViewById(R.id.targetLanguageSpinner);
        ImageButton swapLanguages = findViewById(R.id.swapLanguages);
        ImageButton mic = findViewById(R.id.micIcon);
        ImageButton speaker = findViewById(R.id.speaker);
        ImageButton cut = findViewById(R.id.cut);
        ImageButton copy = findViewById(R.id.copy);

        FloatingActionButton camera = findViewById(R.id.camera);
        imageToText = new ImageToText(this, edtLanguage);

        languageMap = new HashMap<>();
        languageMap.put("English", TranslateLanguage.ENGLISH);
        languageMap.put("Hindi", TranslateLanguage.HINDI);
        languageMap.put("Bengali", TranslateLanguage.BENGALI);
        languageMap.put("Telugu", TranslateLanguage.TELUGU);
        languageMap.put("Marathi", TranslateLanguage.MARATHI);
        languageMap.put("Tamil", TranslateLanguage.TAMIL);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languageMap.keySet().toArray(new String[0]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceLanguageSpinner.setAdapter(adapter);
        targetLanguageSpinner.setAdapter(adapter);

        sourceLanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sourceLanguageCode = languageMap.get(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        targetLanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                targetLanguageCode = languageMap.get(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        translateLanguageBtn.setOnClickListener(v -> {
            String textToTranslate = edtLanguage.getText().toString();
            if (sourceLanguageCode == null || targetLanguageCode == null) {
                Toast.makeText(MainActivity.this, "Please select both source and target languages", Toast.LENGTH_SHORT).show();
                return;
            }
            languageTranslator = new LanguageTranslator(MainActivity.this, sourceLanguageCode, targetLanguageCode);
            translateLanguageTV.setText("Translating...");
            languageTranslator.translateText(textToTranslate, new LanguageTranslator.TranslationCallback() {
                @Override
                public void onSuccess(String translatedText) {
                    translateLanguageTV.setText(translatedText);
                }
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(MainActivity.this, "Translation failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        swapLanguages.setOnClickListener(v -> {
            int sourcePosition = sourceLanguageSpinner.getSelectedItemPosition();
            int targetPosition = targetLanguageSpinner.getSelectedItemPosition();

            sourceLanguageSpinner.setSelection(targetPosition);
            targetLanguageSpinner.setSelection(sourcePosition);
        });

        cut.setOnClickListener(view -> edtLanguage.setText(""));

        speechToTextUtil = new SpeechToTextUtil(this, edtLanguage);

        mic.setOnClickListener(v -> speechToTextUtil.promptSpeechInput());

        copy.setOnClickListener(view -> {
            String translatedText = translateLanguageTV.getText().toString();
            if (!translatedText.isEmpty()) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Translated Text", translatedText);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "No text to copy", Toast.LENGTH_SHORT).show();
            }
        });

        textToSpeechUtil = new TextToSpeechUtil(this);

        speaker.setOnClickListener(view -> {
            String translatedText = translateLanguageTV.getText().toString();
            if (!translatedText.isEmpty()) {
                textToSpeechUtil.speak(translatedText);
            } else {
                Toast.makeText(MainActivity.this, "No text to speak", Toast.LENGTH_SHORT).show();
            }
        });
        camera.setOnClickListener(view -> imageToText.showImagePickerDialog());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        speechToTextUtil.onActivityResult(requestCode, resultCode, data);
        imageToText.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeechUtil != null) {
            textToSpeechUtil.shutdown();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.AboutApp){
            Intent intent=new Intent(MainActivity.this, AboutApp.class);
            startActivity(intent);
        }
        if(item.getItemId()==R.id.models){
            Intent intent=new Intent(MainActivity.this, AvailableModels.class);
            startActivity(intent);
        }
        if(item.getItemId()==R.id.AboutDev){
            Intent intent=new Intent(MainActivity.this,AboutDev.class);
            startActivity(intent);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (shouldExit) {
                super.onBackPressed();
            } else {
                showExitConfirmationDialog();
            }
        }
    }
    private void showExitConfirmationDialog() {
        CustomDialogueBox.showExitConfirmationDialog(
                this,
                "Exit Application",
                "Yes",
                "No",
                (dialog, which) -> {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        shouldExit = true;
                        onBackPressed();
                    } else {
                        dialog.dismiss();
                        shouldExit = false;
                    }
                },
                (dialog, which) -> {
                    dialog.dismiss();
                    shouldExit = false;
                }
        );
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imageToText.onRequestPermissionsResult(requestCode, grantResults);
    }
}
