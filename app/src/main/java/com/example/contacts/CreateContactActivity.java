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

    private ContactDbAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        db = new ContactDbAdapter(this);
        db.open();
    }

    private void createContact() {
        final EditText nom = (EditText) findViewById(R.id.new_nom);
        final EditText prenom = (EditText) findViewById(R.id.new_prenom);


        String new_nom = nom.getText().toString();
        String new_prenom = prenom.getText().toString();
        db.createContact(new_nom, new_prenom);

    }


    public void addItems(View view){
        final EditText new_nom = (EditText) findViewById(R.id.new_nom);
        final EditText new_prenom = (EditText) findViewById(R.id.new_prenom);

        final ListView list_view_contacts = (ListView) findViewById(R.id.list_view_contacts);

        createContact();

        new_nom.setText("");
        new_prenom.setText("");

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        //BUG FILL DATA ???

    }


}
