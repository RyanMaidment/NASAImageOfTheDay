package com.example.nasaimageoftheday;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;


import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class ImageOfTheDay extends AppCompatActivity {
    protected String date;
    protected String explanation;
    protected String title;
    protected String url;
    protected Bitmap nasaImage;
    private static final String TAG ="" ;
    private static String nasaApi = "https://api.nasa.gov/planetary/apod?api_key=VATcMfMCvQtVHKgzXnC8pmkDHooE7qpd89Beqw0m";

    /**
     * onCreate will send the API URL to MyHTTPRequest to get the info from the API
     * It creates an onClickListener to call getImage() to download the
     * image and create a toast when image is successfully downloaded.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageoftheday);
        MyHTTPRequest req = new MyHTTPRequest();
        req.execute(nasaApi);
        Button button1 = findViewById(R.id.button);

        //onClickListener to create a toast when the image has been downloaded.
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage(nasaImage, title, date, explanation);
                Toast.makeText(getBaseContext(), "Image Downloaded", Toast.LENGTH_LONG).show();

            }
        });
    }

    /**
     * onCreateOptionsMenu adds items to the action bar if it is present.
     * @param menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     * @param item
     * @return
     */
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

    /**
     * getImage converts Bitmap image to byte array,
     * and stores values from doInBackground into a
     * database.
     *
     * @param result
     * @param title
     * @param date
     * @param explanation
     */
    public void getImage(Bitmap result, String title, String date, String explanation) {
       //Initializing DatabaseHelper object.
        DatabaseHelper databaseHelper = new DatabaseHelper(ImageOfTheDay.this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        //Converts Bitmap image to byte array.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        result.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        //Storing values in database.
        ContentValues contentValues = new ContentValues();
        contentValues.put("image_name", title);
        contentValues.put("image_data", bytes);
        contentValues.put("image_date", date);
        contentValues.put("image_explanation", explanation);
        db.insert("table_image", null,contentValues);
    }


    private class MyHTTPRequest extends AsyncTask<String, Integer, String> {

        TextView viewTitle = (TextView) findViewById(R.id.viewTitle);
        TextView viewDate = (TextView) findViewById(R.id.viewDate);
        TextView viewDesc = (TextView) findViewById(R.id.viewDesc);
        //Type3                Type1

        /**
         * doInBackground reads the JSON from the API and creates an
         * JSON object from the information.
         * It returns the data from the API.
         * @param args
         * @return
         */
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
                date = nasaJson.getString("date");
                publishProgress(25);
                explanation = nasaJson.getString("explanation");
                publishProgress(50);
                title = nasaJson.getString("title");
                publishProgress(75);
                ImageOfTheDay.this.url = nasaJson.getString("url");
                publishProgress(100);

            } catch (Exception e) {
                Log.e(TAG, "doInBackground: ", e);
            }
            return date + explanation + title + url;
        }

        /**
         * onProgressUpdate updates the progress bar when called.
         * @param progress
         */
        protected void onProgressUpdate(Integer... progress) {
            ProgressBar progressBar = findViewById(R.id.progressBar2);
            progressBar.setProgress(progress[0]);
        }

        /**
         * onPostExecute takes the values from API and
         * puts them into a TextView.
         * It also calls DownloadImageTask to download the image from API
         * @param fromDoInBackground
         */
        public void onPostExecute(String fromDoInBackground) {
            Log.i("HTTP", fromDoInBackground);
            viewTitle.setText(title);
            viewDate.setText(date);
            viewDesc.setText(explanation);
            viewDesc.setMovementMethod(new ScrollingMovementMethod());

            //Calls DownloadImageTask to download image from API
            new DownloadImageTask((ImageView) findViewById(R.id.imageView2))
                    .execute(url);
        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        /**
         * doInBackground gets appended URL and creates a Bitmap image
         * from API image URL.
         * @param urls
         * @return
         */
        protected Bitmap doInBackground(String... urls) {
            //Get url
            String urldisplay = urls[0];
            Bitmap bmImg = null;
            //Get image from URL
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmImg = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            //Return image
            return bmImg;

        }

        /**
         * onPostExecute sets image from api to an ImageView
         * @param result
         */
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            nasaImage = result;

        }

    }
}
