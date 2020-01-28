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

    public static final String KEY_ADRESSE = "adresse";
    public static final String KEY_COMPLEMENT = "complement";
    public static final String KEY_CODEPOSTALE = "codepostale";
    public static final String KEY_VILLE = "ville";

    public static final String KEY_TELEPHONE = "telephone";
    public static final String KEY_EMAIL = "email";




    private static final String TAG = "ContactDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table contact (_id integer primary key autoincrement, "
                    + "new_nom text not null, new_prenom text not null);";

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
    public long createContact(String new_nom, String new_prenom) {
        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_NOM, new_nom);
        initialValues.put(KEY_PRENOM, new_prenom);

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
                KEY_PRENOM}, null, null, null, null, KEY_PRENOM);
    }

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
                                KEY_NOM, KEY_PRENOM}, KEY_ROWID + "=" + rowId, null,
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
     * @param nom value to set note title to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateContact(long rowId, String nom, String prenom) {
        ContentValues args = new ContentValues();
        args.put(KEY_NOM, nom);
        args.put(KEY_PRENOM, prenom);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

}
