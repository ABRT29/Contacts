package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

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



        System.out.println(id);

        Cursor c = db.fetchContact(id);
        startManagingCursor(c);

        final ListView list_view_data = (ListView) findViewById(R.id.list_view_data);


        String[] from = new String[] { ContactDbAdapter.KEY_PRENOM, ContactDbAdapter.KEY_NOM, ContactDbAdapter.KEY_ADRESSE, ContactDbAdapter.KEY_COMPLEMENT, ContactDbAdapter.KEY_CODEPOSTALE, ContactDbAdapter.KEY_VILLE, ContactDbAdapter.KEY_TELEPHONE, ContactDbAdapter.KEY_EMAIL };
        int[] to = new int[] { R.id.data_prenom, R.id.data_nom, R.id.data_adresse, R.id.data_complement, R.id.data_codepostale, R.id.data_ville, R.id.data_telephone, R.id.data_email };

        SimpleCursorAdapter contact =
                new SimpleCursorAdapter(this, R.layout.data_row, c, from, to, 0);
        list_view_data.setAdapter(contact);
    }



}
