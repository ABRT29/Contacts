package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ShowContactActivity extends AppCompatActivity {

    private ContactDbAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact);

        db = new ContactDbAdapter(this);
        db.open();

        fillData();
    }


    public void fillData() {


        Intent intent = getIntent();
        Long id = intent.getExtras().getLong("Id");


        String prenom = db.prenomContact(id);
        TextView d_prenom = findViewById(R.id.d_prenom);
        d_prenom.setText(prenom);

        String nom = db.nomContact(id);
        TextView d_nom = findViewById(R.id.d_nom);
        d_nom.setText(nom);

        String adresse = db.adresseContact(id);
        TextView d_adresse = findViewById(R.id.d_adresse);
        d_adresse.setText(adresse);

        String complement = db.complementContact(id);
        TextView d_complement = findViewById(R.id.d_complement);
        d_complement.setText(complement);

        String codepostale = db.cpContact(id);
        TextView d_codepostale = findViewById(R.id.d_codepostale);
        d_codepostale.setText(codepostale);

        String ville = db.villeContact(id);
        TextView d_ville = findViewById(R.id.d_ville);
        d_ville.setText(ville);

        String telephone = db.telephoneContact(id);
        TextView d_telephone = findViewById(R.id.d_telephone);
        d_telephone.setText(telephone);

        String email = db.emailContact(id);
        TextView d_email = findViewById(R.id.d_email);
        d_email.setText(email);





    }



}
