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

        final EditText adresse = (EditText) findViewById(R.id.new_adresse);
        final EditText complement = (EditText) findViewById(R.id.new_complement);
        final EditText codepostale = (EditText) findViewById(R.id.new_codepostale);
        final EditText ville = (EditText) findViewById(R.id.new_ville);

        final EditText telephone = (EditText) findViewById(R.id.new_telephone);
        final EditText email = (EditText) findViewById(R.id.new_email);

        String new_nom = nom.getText().toString();
        String new_prenom = prenom.getText().toString();

        String new_adresse = adresse.getText().toString();
        String new_complement = complement.getText().toString();
        String new_codepostale = codepostale.getText().toString();
        String new_ville = ville.getText().toString();

        String new_telephone = telephone.getText().toString();
        String new_email = email.getText().toString();

        db.createContact(new_nom, new_prenom, new_adresse, new_complement, new_codepostale, new_ville, new_telephone, new_email);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }




}
