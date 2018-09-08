package com.tharuka.easymemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "notes_db";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Note_Pojo.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Note_Pojo.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertNote(String note) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Note_Pojo.COLUMN_NOTE, note);

        // insert row
        long id = db.insert(Note_Pojo.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public void deleteNote(Note_Pojo note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Note_Pojo.TABLE_NAME, Note_Pojo.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }

    public int updateNote(Note_Pojo note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Note_Pojo.COLUMN_NOTE, note.getNote());

        // updating row
        return db.update(Note_Pojo.TABLE_NAME, values, Note_Pojo.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public Note_Pojo getNote(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Note_Pojo.TABLE_NAME,
                new String[]{Note_Pojo.COLUMN_ID, Note_Pojo.COLUMN_NOTE, Note_Pojo.COLUMN_TIMESTAMP},
                Note_Pojo.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Note_Pojo note = new Note_Pojo(
                cursor.getInt(cursor.getColumnIndex(Note_Pojo.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Note_Pojo.COLUMN_NOTE)),
                cursor.getString(cursor.getColumnIndex(Note_Pojo.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return note;
    }

    public List<Note_Pojo> getAllNotes() {
        List<Note_Pojo> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Note_Pojo.TABLE_NAME + " ORDER BY " +
                Note_Pojo.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note_Pojo note = new Note_Pojo();
                note.setId(cursor.getInt(cursor.getColumnIndex(Note_Pojo.COLUMN_ID)));
                note.setNote(cursor.getString(cursor.getColumnIndex(Note_Pojo.COLUMN_NOTE)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(Note_Pojo.COLUMN_TIMESTAMP)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + Note_Pojo.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public List<Note_Pojo> searchNotes(String search) {
        List<Note_Pojo> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Note_Pojo.TABLE_NAME + " WHERE " +
                Note_Pojo.COLUMN_NOTE + " LIKE '%"+ search +"%'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note_Pojo note = new Note_Pojo();
                note.setId(cursor.getInt(cursor.getColumnIndex(Note_Pojo.COLUMN_ID)));
                note.setNote(cursor.getString(cursor.getColumnIndex(Note_Pojo.COLUMN_NOTE)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(Note_Pojo.COLUMN_TIMESTAMP)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

}
