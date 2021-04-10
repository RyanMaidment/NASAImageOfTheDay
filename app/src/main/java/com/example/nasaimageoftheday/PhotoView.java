package com.example.nasaimageoftheday;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class PhotoView extends AppCompatActivity {
    private ArrayList<NASAObj> nasaObjs = new ArrayList<>();
    NASAObj photos;
    CustomAdapter customAdapter;
    private ListView listView;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        db = new DatabaseHelper(this);
        listView = (ListView) findViewById(R.id.lvPhoto);
        customAdapter = new CustomAdapter(this,nasaObjs);
        listView.setAdapter(customAdapter);

        viewData();

    }

    private void viewData() {
        Cursor cursor = db.viewPhoto();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                byte[] byteArray = cursor.getBlob(2);
                Bitmap photo = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                String date = cursor.getString(3);
                String explanation = cursor.getString(4);

                NASAObj nasaObj = new NASAObj(id, date, explanation, title, photo);
                nasaObjs.add(nasaObj);

            }
            while (cursor.moveToNext());
        }
    }

public class CustomAdapter extends ArrayAdapter<NASAObj> {


    public CustomAdapter(@NonNull Context context, List<NASAObj> nasaObjs) {
        super(context, 0, nasaObjs);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if(row == null) {
            LayoutInflater layoutInflate = getLayoutInflater();
            row = layoutInflate.inflate(R.layout.single_item, parent, false);
        }
        photos = (NASAObj) getItem(position);
        ImageView photo = row.findViewById(R.id.item_image);
        photo.setImageBitmap(photos.getPhoto());
        TextView title = row.findViewById(R.id.item_title);
        title.setText(photos.getTitle());
        TextView date = row.findViewById(R.id.item_date);
        date.setText(photos.getDate());

        return row;
    }

}
}