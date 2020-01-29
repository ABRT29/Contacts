package com.example.contacts;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

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


        list_view_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                /**
                 * permet de faire passer l'ID de cette activité (MainActivity) vers l'activité ShowContactActivity
                 */
                Intent i = new Intent(MainActivity.this, ShowContactActivity.class);
                i.putExtra("Id", id);
                startActivity(i);

                /**
                 * Code à copier/coller dans l'activité ShowContactActivity afin de réceptionner l'ID
                 */
                //Intent intent = getIntent();
                //int id = intent.getExtras().getInt("Id");
                //Cursor c = db.fetchContact(id);
                //startManagingCursor(c);

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
