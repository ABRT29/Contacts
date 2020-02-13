package com.example.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 *
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class ContactDbAdapter {

    /**
     * Champs de la table :
     *
     * ID -> obligatoire
     * Nom -> obligatoire
     * Prénom -> obligatoire
     * Adresse -> facultatif
     * Complément d'adresse -> facultatif
     * Code postale -> facultatif
     * Ville -> facultatif
     * Numéro de téléphone -> obligatoire
     * Adresse E-mail -> -> facultatif
     *
     */
    public static final String KEY_ROWID = "_id";

    public static final String KEY_NOM = "new_nom";
    public static final String KEY_PRENOM = "new_prenom";

    public static final String KEY_ADRESSE = "new_adresse";
    public static final String KEY_COMPLEMENT = "new_complement";
    public static final String KEY_CODEPOSTALE = "new_codepostale";
    public static final String KEY_VILLE = "new_ville";

    public static final String KEY_TELEPHONE = "new_telephone";
    public static final String KEY_EMAIL = "new_email";
    public static final String KEY_FAVORIS = "new_favoris" ;




    private static final String TAG = "ContactDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table contact (_id integer primary key autoincrement, "
                    + "new_nom text not null, new_prenom text not null, new_adresse text not null, new_complement text not null, new_codepostale text not null, new_ville text not null, new_telephone text not null, new_email text not null, new_favoris INTEGER DEFAULT 0 );";


    private static final String DATABASE_NAME = "contacts";
    private static final String DATABASE_TABLE = "contact";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contact");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public ContactDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public ContactDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     *
     * @return rowId or -1 if failed
     */
    public long createContact(String new_nom, String new_prenom, String new_adresse, String new_complement, String new_codepostale, String new_ville, String new_telephone, String new_email) {

        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_NOM, new_nom);
        initialValues.put(KEY_PRENOM, new_prenom);

        initialValues.put(KEY_ADRESSE, new_adresse);
        initialValues.put(KEY_COMPLEMENT, new_complement);
        initialValues.put(KEY_CODEPOSTALE, new_codepostale);
        initialValues.put(KEY_VILLE, new_ville);

        initialValues.put(KEY_TELEPHONE, new_telephone);
        initialValues.put(KEY_EMAIL, new_email);


        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }



    /**
     * Delete the note with the given rowId
     *
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteContact(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public long deleteAllContact() {

        return mDb.delete(DATABASE_TABLE, null, null);
    }


    /**
     * Return a Cursor over the list of all notes in the database
     *
     * @return Cursor over all notes
     */
    public Cursor fetchAllContacts() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NOM,
                KEY_PRENOM }, null, null, null, null, KEY_PRENOM + " COLLATE NOCASE ASC, " + KEY_NOM + " COLLATE NOCASE ASC ");
    }

    //////////////////////////////////////////////

    public Cursor fetchContactsFavoris() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NOM,
                KEY_PRENOM },  KEY_ROWID + "=" + 1, null, null, null, KEY_PRENOM + " COLLATE NOCASE ASC, " + KEY_NOM + " COLLATE NOCASE ASC ");
    }
    //////////////////////////////////////////////
    /**
     * Return a Cursor positioned at the note that matches the given rowId
     *
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchContact(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_NOM, KEY_PRENOM, KEY_ADRESSE, KEY_COMPLEMENT, KEY_CODEPOSTALE, KEY_VILLE, KEY_TELEPHONE, KEY_EMAIL, KEY_FAVORIS}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     *
     * @param rowId id of note to update
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateContact(long rowId, String new_nom, String new_prenom, String new_adresse, String new_complement, String new_codepostale, String new_ville, String new_telephone, String new_email) {
        ContentValues args = new ContentValues();
        args.put(KEY_NOM, new_nom);
        args.put(KEY_PRENOM, new_prenom);
        args.put(KEY_NOM, new_nom);
        args.put(KEY_PRENOM, new_prenom);
        args.put(KEY_ADRESSE, new_adresse);
        args.put(KEY_COMPLEMENT, new_complement);
        args.put(KEY_CODEPOSTALE, new_codepostale);
        args.put(KEY_VILLE, new_ville);
        args.put(KEY_TELEPHONE, new_telephone);
        args.put(KEY_EMAIL, new_email);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /////////////////////////////////////////////////////////////
    public boolean updateFavoris(long rowId,Integer new_favoris) {
        ContentValues args = new ContentValues();
        args.put(KEY_FAVORIS, new_favoris);


        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }


    public Integer getFavoris (long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] { KEY_FAVORIS}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        int int_fav = mCursor.getInt(mCursor.getColumnIndex("new_favoris"));

        return int_fav;

    }

    ////////////////////////////////////////////////////////////

    public String prenomContact(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] { KEY_PRENOM}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        String str = mCursor.getString(mCursor.getColumnIndex("new_prenom"));

        return str;

    }

    public String nomContact(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] { KEY_NOM}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        String str = mCursor.getString(mCursor.getColumnIndex("new_nom"));

        return str;

    }

    public String adresseContact(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] { KEY_ADRESSE}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        String str = mCursor.getString(mCursor.getColumnIndex("new_adresse"));

        return str;

    }

    public String complementContact(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] { KEY_COMPLEMENT}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        String str = mCursor.getString(mCursor.getColumnIndex("new_complement"));

        return str;

    }
    public String cpContact(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] { KEY_CODEPOSTALE}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        String str = mCursor.getString(mCursor.getColumnIndex("new_codepostale"));

        return str;

    }

    public String villeContact(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] { KEY_VILLE}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        String str = mCursor.getString(mCursor.getColumnIndex("new_ville"));

        return str;

    }

    public String telephoneContact(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] { KEY_TELEPHONE}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        String str = mCursor.getString(mCursor.getColumnIndex("new_telephone"));

        return str;

    }

    public String emailContact(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] { KEY_EMAIL}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        String str = mCursor.getString(mCursor.getColumnIndex("new_email"));

        return str;

    }

}