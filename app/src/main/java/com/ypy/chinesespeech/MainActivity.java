package com.ypy.chinesespeech;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import utils.TTSUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnRead=findViewById(R.id.btn_read);
        final EditText editTextContent=findViewById(R.id.et_content);
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TTSUtils.getInstance().speak(editTextContent.getText().toString());
            }
        });
    }
}
