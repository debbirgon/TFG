package com.example.windows.gymapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.example.windows.gymapp.R;
import com.example.windows.gymapp.util.Constants;
import com.squareup.picasso.Picasso;

public class ViewImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        String url = getIntent().getExtras().getString(Constants.IMAGE_URL);

        AppCompatImageView image = findViewById(R.id.image);
        AppCompatImageView back = findViewById(R.id.ic_back);

        Picasso.get().load(url).into(image);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
