package com.example.us.awesomespace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display();
    }

    public void display() {

        APOD apod = QueryUtils.extractAPODrequest();

        TextView imgTitle = findViewById(R.id.imageTitle);
        TextView imgExplanation = findViewById(R.id.imageExplanation);
        TextView imgHdurl = findViewById(R.id.imageHdurl);

        imgTitle.setText(apod.getImageTitle());
        imgExplanation.setText(apod.getExplanation());
        imgHdurl.setText(apod.getImageHDURL());
    }


}
