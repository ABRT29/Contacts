package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toolbar;


public class CreateContactActivity extends AppCompatActivity {

    ContactDbAdapter db = new ContactDbAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        db.open();
    }

    public void createContact(View view) {
        final EditText nom = (EditText) findViewById(R.id.new_nom);
        final EditText prenom = (EditText) findViewById(R.id.new_prenom);

        String new_nom = nom.getText().toString();
        String new_prenom = prenom.getText().toString();

        db.createContact(new_nom, new_prenom);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }




}
