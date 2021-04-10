package com.example.nasaimageoftheday;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EmailNASA extends AppCompatActivity {

    private EditText mEditTextTo;
    private EditText mEditTextSubject;
    private EditText mEditTextMessage;

    /**
     * onCreate will initialize EditText fields and
     * create a button with an onCLickListener that will
     * call sendEmail()
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_intent);

        mEditTextTo=findViewById(R.id.edit_text_to);
        mEditTextSubject=findViewById(R.id.edit_text_subject);
        mEditTextMessage=findViewById(R.id.edit_text_message);
        Button buttonSend=findViewById(R.id.button_send);
        buttonSend.setOnClickListener(v -> sendEmail());
        }

    /**
     * sendEmail() gets text from EditText fields and creates
     * an Intent to send the Strings to an email app to send the
     * email.
     */
    private void sendEmail() {
        String recipients = mEditTextTo.getText().toString();
        String subject = mEditTextSubject.getText().toString();
        String message = mEditTextMessage.getText().toString();
        Intent intentInfo = new Intent(Intent.ACTION_SEND);
        intentInfo.putExtra(Intent.EXTRA_EMAIL, recipients);
        intentInfo.putExtra(Intent.EXTRA_SUBJECT, subject);
        intentInfo.putExtra(Intent.EXTRA_TEXT, message);
        intentInfo.setType("message/rfc822");
        if(intentInfo.resolveActivity(getPackageManager()) != null){
        startActivity(Intent.createChooser(intentInfo, "Pick an email app"));
        }else{
            Toast toast= Toast.makeText(EmailNASA.this, "there is no application available", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}

