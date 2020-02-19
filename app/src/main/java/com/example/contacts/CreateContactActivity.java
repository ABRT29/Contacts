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

import java.util.StringTokenizer;


public class CreateContactActivity extends AppCompatActivity {

    ContactDbAdapter db = new ContactDbAdapter(this);
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        db.open();

        /**
         * Récupération du token si contact importé depuis scan QR Code
         *
         * IMPOSSIBLE DE CREER UN CONTACT NORMAL CAR LE BUNDLE CASSE TOUT CAR OBLIGATOIRE....
         */
        Bundle bundle = getIntent().getExtras();
        this.token = bundle.getString("token");
        if (this.token != null) {fillData();}

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

            if (nom.length() == 0 || telephone.length() == 0) {
                if (nom.length() == 0) {
                    nom.setError("L'ajout d'un nom est obligatoire.");
                }
                if (telephone.length() == 0) {
                    telephone.setError("L'ajout d'un numéro est obligatoire.");
                }
            } else {
                db.createContact(new_nom, new_prenom, new_adresse, new_complement, new_codepostale, new_ville, new_telephone, new_email);


                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }






    public void fillData(){

        StringTokenizer tokens = new StringTokenizer(this.token, ";");
        String prenom = tokens.nextToken();
        String nom = tokens.nextToken();
        String telephone = tokens.nextToken();
        String email = tokens.nextToken();



        EditText new_prenom = findViewById(R.id.new_prenom);
        new_prenom.setText(prenom);

        EditText new_nom = findViewById(R.id.new_nom);
        new_nom.setText(nom);

        EditText new_telephone = findViewById(R.id.new_telephone);
        new_telephone.setText(telephone);


        /**
         * si champs vide BUG.....
         */
        EditText new_email = findViewById(R.id.new_email);
        new_email.setText(email);
    }

}
