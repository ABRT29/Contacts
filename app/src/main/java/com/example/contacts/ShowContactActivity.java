package com.example.contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class ShowContactActivity extends AppCompatActivity {

    private ContactDbAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact);

        final Long id = getIntent().getExtras().getLong("Id");

        final ActionsContacts actions = new ActionsContacts(id, this);

        FloatingActionButton floatTelephone = findViewById(R.id.floatTelephone);
        floatTelephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actions.call();
            }
        });

        FloatingActionButton floatMessage = findViewById(R.id.floatMessage);
        floatMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actions.message();
            }
        });

        FloatingActionButton floatEmail = findViewById(R.id.floatEmail);
        floatEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actions.email();
            }
        });

        FloatingActionButton floatMap = findViewById(R.id.floatMap);
        floatMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actions.map();
            }
        });


        fillData(id);


        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap("content", BarcodeFormat.QR_CODE, 400, 400);
            ImageView imageViewQrCode = (ImageView) findViewById(R.id.qrCode);
            imageViewQrCode.setImageBitmap(bitmap);
        } catch(Exception e) {
        }



    }


    public void fillData(Long id) {

        db = new ContactDbAdapter(this);
        db.open();

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
