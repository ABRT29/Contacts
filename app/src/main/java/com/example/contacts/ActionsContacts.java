package com.example.contacts;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.AdapterView;
import android.widget.ListView;

public class ActionsContacts {


    public void call(final Context context, final String SelectedTel){
        Uri number = Uri.parse("tel:" + SelectedTel);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        context.startActivity(callIntent);
    }

    public void message(final Context context, final String SelectedTel){
        Uri msg = Uri.parse("smsto:" + SelectedTel);
        Intent messageIntent = new Intent(Intent.ACTION_SENDTO, msg);
        context.startActivity(messageIntent);
    }

    public void email(final Context context, final String SelectedEmail){
        Uri mail = Uri.parse("mailto:" + SelectedEmail);
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, mail);
        context.startActivity(emailIntent);
    }

    public void map(final Context context, final String SelectedAdresse, final String SelectedComplement, final String SelectedCodePostale, final String SelectedVille){
        Uri location = Uri.parse("geo:0,0?q=" + SelectedAdresse + ", " + SelectedComplement + ", " + SelectedCodePostale + ", " + SelectedVille);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        context.startActivity(mapIntent);
    }




}
