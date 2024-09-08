package com.example.textntalk;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class AboutDev extends AppCompatActivity {
    private static final String Instagram_URL = "https://www.instagram.com/itz__your__akil?igsh=eTlmaXF2NnlzZjhu";
    private static final String Linkedin_URL = "https://www.linkedin.com/in/akhil-adam-104886319/";
    private static final String Github_URL="https://github.com/Hacker-Akhil";
    ImageButton linkedin,github,instagram;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_dev);
        linkedin=findViewById(R.id.linkedin);
        github=findViewById(R.id.github);
        instagram=findViewById(R.id.instagram);

        linkedin.setOnClickListener(view -> {
        openLinkedin();
        });
        github.setOnClickListener(view -> {
            openGitHub();
        });
        instagram.setOnClickListener(view -> {
            openInstagram();
        });
    }
    private void openInstagram() {
        Uri uri = Uri.parse(Instagram_URL);
        Intent insta = new Intent(Intent.ACTION_VIEW, uri);

        insta.setPackage("com.instagram.android");

        try {
            startActivity(insta);
        } catch (ActivityNotFoundException e) {
            // If the Instagram app is not installed, open the URL in a web browser
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Instagram_URL)));
        }
    }
    private void openLinkedin() {
        Uri uri = Uri.parse(Linkedin_URL);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Linkedin_URL)));
        }
    }
    private void openGitHub() {
        Uri uri = Uri.parse(Github_URL);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Github_URL)));
        }
    }
}