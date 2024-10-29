package com.miriandor.hackathonapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.UUID;

public class salePage extends AppCompatActivity {

    ImageView imageView;
    TextView title;
    TextView category;
    TextView address;
    TextView description;
    TextView date;
    Button join;
    ProgressBar progressBar;

    //todo: send order_id, user_id, event_id, amount, date

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_page);
        Intent intent = getIntent();
        Events event = intent.getParcelableExtra("event item");
        imageView= findViewById(R.id.url_preview);
        title = findViewById(R.id.sale_title);
        category = findViewById(R.id.category);
        address = findViewById(R.id.address);
        description = findViewById(R.id.description);
        date = findViewById(R.id.date);
        join = findViewById(R.id.join);
        progressBar = findViewById(R.id.progressBar);
        populateUI(event);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(salePage.this);
                final NumberPicker numberPicker = new NumberPicker(salePage.this);
                numberPicker.setMaxValue(360);
                numberPicker.setMinValue(0);
                builder.setView(numberPicker);
                builder.setTitle("How Many would you like?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Log.d("SALE",String.valueOf(numberPicker.getValue()));
                        sendData(numberPicker.getValue());
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create();
                builder.show();
            }
        });
    }
    public void populateUI(Events event){
        title.setText(event.name);
        category.setText(event.category);//todo: style
        address.setText(event.address);
        description.setText(event.description);
        date.setText(event.date.toString());

        progressBar.setVisibility(View.VISIBLE);
        Picasso.get().load(event.imageURL).centerCrop()
                .resize(5000, 3000)
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getApplicationContext(),"Error Loading Image", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void sendData(int amount){
        Log.d("SALE DIALOG", String.valueOf(amount));
        UUID uniqueID = UUID.randomUUID();
    }
}

