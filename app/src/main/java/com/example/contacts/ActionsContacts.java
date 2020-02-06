package com.example.contacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;



public class ActionsContacts  {

    private ContactDbAdapter db;

    private Context mContext;

    private String SelectedTel,
            SelectedEmail,
            SelectedAdresse,
            SelectedComplement,
            SelectedCodePostale,
            SelectedVille;


    ActionsContacts(Long id, Context context){

        db = new ContactDbAdapter(context);
        db.open();

        this.mContext = context;


        /**
         * Récupération des données en bdd car non affichées dans la ligne du listView
         */
        Cursor c = db.fetchContact(id);
        ((Activity) context).startManagingCursor(c);



        this.SelectedTel = c.getString(c.getColumnIndex("new_telephone"));
        this.SelectedEmail = c.getString(c.getColumnIndex("new_email"));

        this.SelectedAdresse = c.getString(c.getColumnIndex("new_adresse"));
        this.SelectedComplement = c.getString(c.getColumnIndex("new_complement"));
        this.SelectedCodePostale = c.getString(c.getColumnIndex("new_codepostale"));
        this.SelectedVille = c.getString(c.getColumnIndex("new_ville"));
    }


        /**
         * ACTIONS
         */

        public void call(){
            Uri number = Uri.parse("tel:" + SelectedTel);
            Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
            mContext.startActivity(callIntent);
        }

        public void message(){
            Uri msg = Uri.parse("smsto:" + SelectedTel);
            Intent messageIntent = new Intent(Intent.ACTION_SENDTO, msg);
            mContext.startActivity(messageIntent);
        }

        public void email(){
            Uri mail = Uri.parse("mailto:" + SelectedEmail);
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, mail);
            mContext.startActivity(emailIntent);
        }

        public void map(){
            Uri location = Uri.parse("geo:0,0?q=" + SelectedAdresse + ", " + SelectedComplement + ", " + SelectedCodePostale + ", " + SelectedVille);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
            mContext.startActivity(mapIntent);
        }

}
