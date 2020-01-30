package com.example.contacts;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    private ContactDbAdapter db;
// COUcou c'est menzo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityCreateContact();
            }
        });

        final ListView list_view_contacts = (ListView) findViewById(R.id.list_view_contacts);

        db = new ContactDbAdapter(this);
        db.open();

        fillData();

        registerForContextMenu(list_view_contacts);


        list_view_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /**
                 * permet de faire passer l'ID de cette activité (MainActivity) vers l'activité ShowContactActivity
                 */
                Intent i = new Intent(MainActivity.this, ShowContactActivity.class);
                i.putExtra("Id", id);
                startActivity(i);
            }
        });
    }

    /**
     * Redirection vers l'activité : CreateContact
     */
    public void openActivityCreateContact(){
        Intent intent = new Intent(this, CreateContactActivity.class);
        startActivity(intent);
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        /** appeller sms email localise supprimer */
        menu.add(0, v.getId(), 0, "Appeler");
        menu.add(0, v.getId(), 0, "Envoyer un SMS");
        menu.add(0, v.getId(), 0, "Envoyer un Email");
        menu.add(0, v.getId(), 0, "Voir l'adresse du contact");
        menu.add(0, v.getId(), 0, "Supprimer le contact");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final ListView list_view_contacts = (ListView) findViewById(R.id.list_view_contacts);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Cursor SelectedCursor = (Cursor) list_view_contacts.getItemAtPosition(info.position);

        final long SelectedId = SelectedCursor.getLong(SelectedCursor.getColumnIndex("_id"));

        /**
         * Récupération des données non affichées dans la ligne
         */
        Cursor c = db.fetchContact(SelectedId);
        startManagingCursor(c);

        final String SelectedTel = c.getString(c.getColumnIndex("new_telephone"));
        final String SelectedEmail = c.getString(c.getColumnIndex("new_email"));

        final String SelectedAdresse = c.getString(c.getColumnIndex("new_adresse"));
        final String SelectedComplement = c.getString(c.getColumnIndex("new_complement"));
        final String SelectedCodePostale = c.getString(c.getColumnIndex("new_codepostale"));
        final String SelectedVille = c.getString(c.getColumnIndex("new_ville"));


        /**
         * Actions
         */
        if (item.getTitle() == "Supprimer le contact"){
            db.deleteContact(SelectedId);
            fillData();
        }

        if (item.getTitle() == "Appeler"){
            Uri number = Uri.parse("tel:" + SelectedTel);
            Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
            startActivity(callIntent);
        }

        if (item.getTitle() == "Envoyer un SMS"){
            Uri msg = Uri.parse("smsto:" + SelectedTel);
            Intent messageIntent = new Intent(Intent.ACTION_SENDTO, msg);
            startActivity(messageIntent);
        }

        if (item.getTitle() == "Envoyer un Email"){
            Uri mail = Uri.parse("mailto:" + SelectedEmail);
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, mail);
            startActivity(emailIntent);
        }

        if (item.getTitle() == "Voir l'adresse du contact"){
            Uri location = Uri.parse("geo:0,0?q=" + SelectedAdresse + ", " + SelectedComplement + ", " + SelectedCodePostale + ", " + SelectedVille);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
            startActivity(mapIntent);
        }


        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fillData() {
        Cursor c = db.fetchAllContacts();
        startManagingCursor(c);

        final ListView list_view_contacts = (ListView) findViewById(R.id.list_view_contacts);


        String[] from = new String[] { ContactDbAdapter.KEY_PRENOM, ContactDbAdapter.KEY_NOM };
        int[] to = new int[] { R.id.text1, R.id.text2 };

        SimpleCursorAdapter contacts =
                new SimpleCursorAdapter(this, R.layout.contacts_row, c, from, to, 0);
        list_view_contacts.setAdapter(contacts);
    }


}
