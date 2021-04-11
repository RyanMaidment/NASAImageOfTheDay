package com.example.nasaimageoftheday;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity2 extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private AppBarConfiguration mAppBarConfiguration;
    private Context mContext;
    private Activity mActivity;
    private RelativeLayout mRelativeLayout;
    private Snackbar mSnackbar;

    /**
     * This onCreate() is used to create:
     * FAB that shows a Snackbar and takes user to EmailNASA.java
     * Toolbar where there is a NavDrawer
     * Button to pick the date using a DatePicker
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab2 = findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity2.this)
                        .setTitle(R.string.help_title)
                        .setMessage(R.string.help_main)

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(R.string.help_button, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        AtomicReference<TextView> textView = new AtomicReference<>(findViewById(R.id.textView_date));
        textView.get().setText(PrefConfig.loadDateInPref(this));
        findViewById(R.id.button_today)
                .setOnClickListener(v -> startActivity(new Intent(MainActivity2.this, ImageOfTheDay.class)));
        //Get the application context
        mContext = getApplicationContext();
        mActivity = MainActivity2.this;

        // Get the widget reference from XML layout
        mRelativeLayout = findViewById(R.id.rl);
        //mButtonTask = (Button) findViewById(R.id.btn_task);

        // Initialize an empty text Snackbar
        mSnackbar = Snackbar.make(mRelativeLayout,"",Snackbar.LENGTH_INDEFINITE);
        // Get the Snackbar view
        View view = mSnackbar.getView();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view1 -> {
            // Change the Snackbar text
            mSnackbar.setText("Emailing NASA Eh!");

            // Set an action for Snackbar
            // Set a click listener for Snackbar action button
            mSnackbar.setAction("DO IT", v -> {
                Intent intent = new Intent(MainActivity2.this, EmailNASA.class);
                    startActivity(intent);
                });
            // Display the Snackbar
            mSnackbar.show();
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        AtomicReference<Button> button = new AtomicReference<>(findViewById(R.id.button4));
        button.get().setOnClickListener(v -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
            });



    }

    /**
     * @param view
     * @param message
     * @param duration
     */
    public void showSnackbar(View view, String message, int duration)
    {
        // Create snackbar
        final Snackbar snackbar = Snackbar.make(view, message, duration);

        // Set an action on it, and a handler
        snackbar.setAction("DISMISS", v -> snackbar.dismiss());

        snackbar.show();
    }

    /**
     * onCreateOptionsMenu adds items to the action bar if it is present.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }

    public void onClick(MenuItem item) {
        Intent i = new Intent(this, PhotoView.class);
        startActivity(i);
    }
    /**
     * onSupportNavigateUp is called whenever
     * the user chooses to navigate within the application's
     * activity hierarchy from the action bar.
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * onDateSet gets date picked by user,
     * Stores date picked by user in a TextView,
     * Formats the date from API,
     * Sends date pucked by user to ImageOfTheDay
     * @param view
     * @param year
     * @param month
     * @param dayOfMonth
     */
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //Gets date picked by user
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        //Stores date picked by user in a TextView
        AtomicReference<TextView> textView = new AtomicReference<>(findViewById(R.id.textView_date));
        textView.get().setText(currentDateString);
        PrefConfig.saveDateInPref(getApplicationContext(), currentDateString);

        //Logic to format the date given from API
        int monthOfYear1 = month + 1;
        String formattedMonth = "" + monthOfYear1;
        String formattedDayOfMonth = "" + dayOfMonth;

        if(monthOfYear1 < 10){

            formattedMonth = "0" + monthOfYear1;
        }
        if(dayOfMonth < 10){

            formattedDayOfMonth = "0" + dayOfMonth;
        }

        //Sends date pucked by user to ImageOfTheDay
        Intent intent = new Intent(MainActivity2.this, ImageOfTheDay2.class);
        intent.putExtra("Date", year+"-"+formattedMonth+"-"+formattedDayOfMonth);
        startActivity(intent);
    }
}