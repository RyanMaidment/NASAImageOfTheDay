package com.example.nasaimageoftheday;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
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
        customAdapter.notifyDataSetChanged();

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

public class CustomAdapter extends ArrayAdapter<NASAObj>{


    public CustomAdapter(@NonNull Context context, List<NASAObj> nasaObjs) {
        super(context, 0, nasaObjs);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
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
        TextView id = row.findViewById(R.id.item_id);
        id.setText(Integer.toString(photos.getId()));
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photos = (NASAObj) getItem(position);
                Intent i = new Intent(PhotoView.this,DisplayImage.class);
                String date = photos.getDate();
                i.putExtra("Date", date);
                startActivity(i);
            }
        });
        row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(PhotoView.this)
                        .setTitle(R.string.dialogTitle)
                        .setMessage(
                                getResources().getString(R.string.dialogMessage))
                        .setIcon(
                                getResources().getDrawable(
                                        android.R.drawable.ic_dialog_alert))
                        .setPositiveButton(
                                getResources().getString(R.string.PostiveYesButton),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        remove(position);
                                        customAdapter.notifyDataSetChanged();
                                        Toast.makeText(getBaseContext(), "Deleted", Toast.LENGTH_LONG).show();
                                    }
                                })
                        .setNegativeButton(
                                getResources().getString(R.string.NegativeNoButton),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Toast.makeText(getBaseContext(), "Not Deleted", Toast.LENGTH_LONG).show();
                                    }
                                }).show();
                return false;
            }
        });

        return row;
    }
    public void remove(int position){
        photos = (NASAObj) getItem(position);
        int imgId = photos.getId();
        db.deleteRow(imgId);
        nasaObjs.remove(nasaObjs.get(position));;
    }
}
}