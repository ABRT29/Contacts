/**
 *
 * PROJET ANDROID JAVA - LPDIMFI
 *
 * Aurélien BURET & Menzo KORCHIT
 *
 * Application : CONTACTS
 *
 */

package com.example.contacts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ContactDbAdapter db;

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

        /** appeller sms email localiser supprimer */
        /**
         * Menu contextuel qui seront affichés lors d'un appui long
         */
        menu.add(0, v.getId(), 0, "Appeler");
        menu.add(0, v.getId(), 0, "Envoyer un SMS");
        menu.add(0, v.getId(), 0, "Envoyer un Email");
        menu.add(0, v.getId(), 0, "Voir l'adresse du contact");
        menu.add(0, v.getId(), 0, "Supprimer le contact");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        /**
         * Récupération de l'ID en fonction de la position de la ligne dans la listView
         */
        final ListView list_view_contacts = (ListView) findViewById(R.id.list_view_contacts);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Cursor SelectedCursor = (Cursor) list_view_contacts.getItemAtPosition(info.position);

        final long SelectedId = SelectedCursor.getLong(SelectedCursor.getColumnIndex("_id"));

        /**
         * Récupération des données en bdd car non affichées dans la ligne du listView
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
         * Actions menu contextuel
         */

        ActionsContacts actions = new ActionsContacts();

        if (item.getTitle() == "Supprimer le contact"){
            alertDelete(SelectedId);
            fillData();
        }

        if (item.getTitle() == "Appeler"){
            actions.call(this,SelectedTel);
        }

        if (item.getTitle() == "Envoyer un SMS"){
            actions.message(this, SelectedTel);
        }

        if (item.getTitle() == "Envoyer un Email"){
            actions.email(this, SelectedEmail);
        }

        if (item.getTitle() == "Voir l'adresse du contact"){
            actions.map(this, SelectedAdresse, SelectedComplement, SelectedCodePostale, SelectedVille);
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
        if (id == R.id.action_ReadQRCode) {
            Intent mediaChooser =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(mediaChooser, 1);
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * POP UP ALERT DELETE
     */
    public void alertDelete(final Long SelectedId){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Suppression du contact");
        alertDialog.setMessage("Voulez-vous vraiment supprimer ce contact ?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Contact supprimés", Toast.LENGTH_SHORT).show();
                db.deleteContact(SelectedId);
                fillData();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "ANNULER", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Aucune suppression n'a était effectuée", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.show();
    }


    /**
     *
     * tabLayout
     *  Contacts
     *  Favoris
     *
     * A faire
     *
     */



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
