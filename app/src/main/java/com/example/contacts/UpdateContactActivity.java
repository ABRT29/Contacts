package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class UpdateContactActivity extends AppCompatActivity {

    ContactDbAdapter db = new ContactDbAdapter(this);
    private Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

        this.id = getIntent().getExtras().getLong("id");


        fillData(id);
    }



    public void fillData(Long id) {

        db = new ContactDbAdapter(this);
        db.open();

        String prenom = db.prenomContact(id);
        EditText d_prenom = findViewById(R.id.update_prenom);
        d_prenom.setText(prenom);

        String nom = db.nomContact(id);
        EditText d_nom = findViewById(R.id.update_nom);
        d_nom.setText(nom);

        String adresse = db.adresseContact(id);
        EditText d_adresse = findViewById(R.id.update_adresse);
        d_adresse.setText(adresse);

        String complement = db.complementContact(id);
        EditText d_complement = findViewById(R.id.update_complement);
        d_complement.setText(complement);

        String codepostale = db.cpContact(id);
        EditText d_codepostale = findViewById(R.id.update_codepostale);
        d_codepostale.setText(codepostale);

        String ville = db.villeContact(id);
        EditText d_ville = findViewById(R.id.update_ville);
        d_ville.setText(ville);

        String telephone = db.telephoneContact(id);
        EditText d_telephone = findViewById(R.id.update_telephone);
        d_telephone.setText(telephone);

        String email = db.emailContact(id);
        EditText d_email = findViewById(R.id.update_email);
        d_email.setText(email);

    }


    public void updateContact(View view) {

        final EditText nom = (EditText) findViewById(R.id.update_nom);
        final EditText prenom = (EditText) findViewById(R.id.update_prenom);

        final EditText adresse = (EditText) findViewById(R.id.update_adresse);
        final EditText complement = (EditText) findViewById(R.id.update_complement);
        final EditText codepostale = (EditText) findViewById(R.id.update_codepostale);
        final EditText ville = (EditText) findViewById(R.id.update_ville);

        final EditText telephone = (EditText) findViewById(R.id.update_telephone);
        final EditText email = (EditText) findViewById(R.id.update_email);

        String new_nom = nom.getText().toString();
        String new_prenom = prenom.getText().toString();

        String new_adresse = adresse.getText().toString();
        String new_complement = complement.getText().toString();
        String new_codepostale = codepostale.getText().toString();
        String new_ville = ville.getText().toString();

        String new_telephone = telephone.getText().toString();
        String new_email = email.getText().toString();


        if (nom.length() == 0 || telephone.length() == 0 ){
            if(nom.length() == 0)
            {
                nom.setError("L'ajout d'un nom est obligatoire.");
            }
            if(telephone.length() == 0) {
                telephone.setError("L'ajout d'un numéro est obligatoire.");
            }
        }
        else {
            db.updateContact(this.id, new_nom, new_prenom, new_adresse, new_complement, new_codepostale, new_ville, new_telephone, new_email);

            finish();

            Intent i = new Intent(UpdateContactActivity.this, ShowContactActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("Id", id);
            i.putExtras(bundle);
            startActivity(i);
        }
    }



}


