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


        String[] from = new String[] { ContactDbAdapter.KEY_PRENOM, ContactDbAdapter.KEY_NOM };
        int[] to = new int[] { R.id.text1, R.id.text2 };

        SimpleCursorAdapter contact =
                new SimpleCursorAdapter(this, R.layout.contacts_row, c, from, to, 0);
        list_view_data.setAdapter(contact);
    }



}
