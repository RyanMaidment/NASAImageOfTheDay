package com.example.nasaimageoftheday;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    private Button mButtonTask;
    private Snackbar mSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AtomicReference<TextView> textView = new AtomicReference<>(findViewById(R.id.textView_date));
        textView.get().setText(PrefConfig.loadDateInPref(this));
        findViewById(R.id.button_today)
                .setOnClickListener(v -> startActivity(new Intent(MainActivity2.this, ImageOfTheDay.class)));
// Get the application context
        mContext = getApplicationContext();
        mActivity = MainActivity2.this;

        // Get the widget reference from XML layout
        mRelativeLayout = findViewById(R.id.rl);
 //       mButtonTask = (Button) findViewById(R.id.btn_task);

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
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
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
    public void showSnackbar(View view, String message, int duration)
    {
        // Create snackbar
        final Snackbar snackbar = Snackbar.make(view, message, duration);

        // Set an action on it, and a handler
        snackbar.setAction("DISMISS", v -> snackbar.dismiss());

        snackbar.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        AtomicReference<TextView> textView = new AtomicReference<>(findViewById(R.id.textView_date));
        textView.get().setText(currentDateString);
        PrefConfig.saveDateInPref(getApplicationContext(), currentDateString);
    }



// TODO LIST
// TODO 1.    The project must have a ListView somewhere to present items. Selecting an item from the ListView must show detailed information about the item selected.
// TODO 2.    The project must have at least 1 progress bar and at least 1 button^.
// TODO 3.    The project must have at least 1 edit text with appropriate text input method and at least 1 Toast and 1 Snackbar.
// TODO 4.    The software must have at least 4 or more activities. Your activity must be accessible by selecting a graphical icon from a --Toolbar!, and --NavigationDrawer!.
//               The top navigation layout should have the Activity’s title, and a version number.
// TODO 5.    The project must use a fragment! somewhere in its graphical interface.
// TODO 6.    Each activity must have a help menu item that displays an AlertDialog with instructions for how to use the interface.
// TODO 7.    There must be at least 1 other language supported by your Activity. Please use Canadian French as the secondary language if you do not you know a language other than English.
// TODO 8.    The items listed in the ListView must be stored by the application so that they appear the next time the application is launched. The user must be able to add and delete items, which would then also be stored in a database.
// TODO 9.    When retrieving data from an --http server!, the activity must use an --AsyncTask!.
// TODO 10.   The project must use --SharedPreferences! to save something about the application for use the next time the application is launched.
// TODO 11.   All activities must be integrated into a single working application, on a single emulator, and must be uploaded to GitHub.
// TODO 12.   The interfaces must look professional, with GUI elements properly laid out and aligned.
// TODO 13.   The functions and variables you write must be properly documented using JavaDoc comments.
}