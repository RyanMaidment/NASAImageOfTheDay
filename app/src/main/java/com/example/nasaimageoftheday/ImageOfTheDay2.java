package com.example.nasaimageoftheday;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;


import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicReference;


public class ImageOfTheDay2 extends AppCompatActivity {

    private static final String TAG ="" ;
    private static String nasaApi = "https://api.nasa.gov/planetary/apod?api_key=VATcMfMCvQtVHKgzXnC8pmkDHooE7qpd89Beqw0m";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageoftheday);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);
        Intent i = getIntent();
        String appendURL = i.getStringExtra("Date");
        String uri = "https://api.nasa.gov/planetary/apod?api_key=VATcMfMCvQtVHKgzXnC8pmkDHooE7qpd89Beqw0m&date=" + appendURL;
        MyHTTPRequest req = new MyHTTPRequest();
        req.execute(uri); //Type 1
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyHTTPRequest extends AsyncTask<String, Integer, String> {
        protected String date;
        protected String explanation;
        protected String title;
        protected String hdurl;
        TextView viewTitle = (TextView) findViewById(R.id.viewTitle);
        TextView viewDate = (TextView) findViewById(R.id.viewDate);
        TextView viewDesc = (TextView) findViewById(R.id.viewDesc);
        //Type3                Type1
        public String doInBackground(String... args) {
            try {

                //create a URL object of what server to contact:
                URL url = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();


                //JSON reading:
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


                // convert string to JSON:
                JSONObject nasaJson = new JSONObject(result);
                //get the double associated with "value"
                this.date = nasaJson.getString("date");
                this.explanation = nasaJson.getString("explanation");
                this.title = nasaJson.getString("title");
                this.hdurl = nasaJson.getString("hdurl");


            } catch (Exception e) {
                Log.e(TAG, "doInBackground: ", e);
            }
            return date + explanation + title + hdurl;
        }

        //Type3
        public void onPostExecute(String fromDoInBackground) {
            Log.i("HTTP", fromDoInBackground);
            viewTitle.setText(title);
            viewDate.setText(date);
            viewDesc.setText(explanation);
            new DownloadImageTask((ImageView) findViewById(R.id.imageView2))
                    .execute(hdurl);
        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
