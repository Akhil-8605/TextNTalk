package com.example.textntalk;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class AvailableModels extends AppCompatActivity {
private final String[] downloadedModels={"English to English","English to Hindi","English to Bengali","English to Telugu","English to Marathi","English to Tamil",
                                    "Hindi to English","Hindi to Hindi","Hindi to Bengali","Hindi to Telugu","Hindi to Marathi","Hindi to Tamil",
                                    "Bengali to English","Bengali to Hindi","Bengali to Bengali","Bengali to Telugu","Bengali to Marathi","Bengali to Tamil",
                                    "Telugu to English","Telugu to Hindi","Telugu to Bengali","Telugu to Telugu","Telugu to Marathi","Telugu to Tamil",
                                    "Marathi to English","Marathi to Hindi","Marathi to Bengali","Marathi to Telugu","Marathi to Marathi","Marathi to Tamil",
                                    "Tamil to English","Tamil to Hindi","Tamil to Bengali","Tamil to Telugu","Tamil to Marathi","Tamil to Tamil",};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_models);

        ListView listViewItems = findViewById(R.id.list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, downloadedModels);
        listViewItems.setAdapter(adapter);
    }
}
