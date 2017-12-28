package swch.bcit.ca.swchdatabases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * NoteDatabase class is the database for Notes.
 *
 * @author SWCH
 * @version 2017
 */

public class NoteDatabase extends Database {

    /**
     * Name of the database table.
     */
    private static final String TABLE_NAME = "note_table";

    /**
     * The ID column of the table for notes.
     */
    private static final String COL0 = "ID";

    /**
     * The name column of the table for notes.
     */
    private static final String COL1 = "note_name";

    /**
     * The priority column of the table for notes.
     */
    private static final String COL2 = "priority";

    /**
     * The contents column of the table for notes.
     */
    private static final String COL3 = "contents";

    /**
     * The array list is used to manage the IDs of the data entries.
     */
    private static ArrayList<Integer> indexArrayList = new ArrayList<>();

    /**
     * The constructor which constructs an object of type NoteDatabase.
     * @param context the application context.
     */
    public NoteDatabase(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME
                + " (" + COL0 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                       + COL1 + " TEXT, "
                       + COL2 + " INT, "
                       + COL3 + " TEXT)";
        sqLiteDatabase.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    @Override
    public void addDataEntry(Object data)
            throws DataExistenceException, IllegalPriorityException {
        if (!(data instanceof Note)) {
            throw new DataExistenceException("Data is not a Note.");
        }

        Note note = (Note) data;

        if (isDataInDatabase(note)) {
            throw new DataExistenceException("Data is already in database.");
        }

        updateIndexes();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, note.getName());
        contentValues.put(COL2, note.getPriority());
        contentValues.put(COL3, note.getContents());
        this.getWritableDatabase().insert(TABLE_NAME, null, contentValues);
    }

    @Override
    public void deleteDataEntry(int id)
            throws DataExistenceException {
        try {
            updateIndexes();
            int index = indexArrayList.get(id);
            this.getWritableDatabase().delete(TABLE_NAME, COL0 + " = ?", new String[] {Integer.toString(index)});
            this.getWritableDatabase().execSQL("VACUUM");
            indexArrayList.remove(id);

            if (indexArrayList.size() == 0) {
                this.getWritableDatabase().execSQL("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = " + "'" + TABLE_NAME + "'");
            }
        } catch (IndexOutOfBoundsException e) {
            throw new DataExistenceException("Data is not in database.");
        }
    }

    /**
     * Gets the Note from the database.
     * @param id an int which is the id of the Note to retrieve from the data base.
     * @return Note the note containing all the data of the note.
     * @throws DataExistenceException is thrown if the Note is not in the database.
     * @throws IllegalPriorityException is thrown if the priority is not with the range of 1 - 3 (inclusive).
     */
    public Note getNote(int id)
            throws DataExistenceException, IllegalPriorityException {
        try {
            updateIndexes();
            int index = indexArrayList.get(id);
            return new Note(getNoteName(index),
                            getNotePriority(index),
                            getNoteContents(index));
        } catch (IndexOutOfBoundsException e) {
            throw new DataExistenceException("Data is not in database.");
        }
    }

    /**
     * Updates the Note to a new Note.
     * @param newNoteName a string which is the new Note name to update to.
     * @param newNotePriority an int which is the new priority to update to.
     * @param newNoteContents a string which is the new contents to update to.
     * @param id an int which is the id of the Note to update.
     * @throws DataExistenceException is thrown if the Note is not in the database.
     * @throws IllegalPriorityException is thrown if the priority is not within the range of 1 - 3 (inclusive).
     */
    public void updateNote(String newNoteName, int newNotePriority, String newNoteContents, int id)
            throws DataExistenceException, IllegalPriorityException {
        updateNoteName(newNoteName, id);
        updateNotePriority(newNotePriority, id);
        updateNoteContents(newNoteContents, id);
    }

    /**
     * Updates the name of the Note to a new Note name.
     * @param newNoteName a string which is the new Note name to update to.
     * @param id an int which is the id of the Note to update.
     * @throws DataExistenceException is thrown if the Note is not in the database.
     */
    public void updateNoteName(String newNoteName, int id)
            throws DataExistenceException {
        try {
            updateIndexes();
            int index = indexArrayList.get(id);
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL1, newNoteName);
            this.getWritableDatabase().update(TABLE_NAME, contentValues, COL0 + " = ?", new String[]{Integer.toString(index)});
        } catch (IndexOutOfBoundsException e) {
            throw new DataExistenceException("Data is not in database.");
        }
    }

    /**
     * Updates the priority of the Note to a new priority.
     * @param newNotePriority an int which is the new priority to update to.
     * @param id an int which is the id of the Note to update.
     * @throws DataExistenceException is thrown if the Note is not within the database.
     * @throws IllegalPriorityException is thrown if the priority is not with the range of 1 - 3 (inclusive).
     */
    public void updateNotePriority(int newNotePriority, int id)
            throws DataExistenceException, IllegalPriorityException {
        if (!isValidPriority(newNotePriority)) {
            throw new IllegalPriorityException("Invalid priority.");
        }
        try {
            updateIndexes();
            int index = indexArrayList.get(id);
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL2, newNotePriority);
            this.getWritableDatabase().update(TABLE_NAME, contentValues, COL0 + " = ?", new String[]{Integer.toString(index)});
        } catch (IndexOutOfBoundsException e) {
            throw new DataExistenceException("Data is not in database.");
        }
    }

    /**
     * Updates the contents of the Note to new content.
     * @param newNoteContents a string which is the new contents to update to.
     * @param id an int which is the id of the Note to update.
     * @throws DataExistenceException is thrown if the Note is not within the database.
     */
    public void updateNoteContents(String newNoteContents, int id)
            throws DataExistenceException {
        try {
            updateIndexes();
            int index = indexArrayList.get(id);
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL3, newNoteContents);
            this.getWritableDatabase().update(TABLE_NAME, contentValues, COL0 + " = ?", new String[]{Integer.toString(index)});
        } catch (IndexOutOfBoundsException e) {
            throw new DataExistenceException("Data is not in database.");
        }
    }

    /**
     * The helper method gets the name of the Note given the id of the Note.
     * @param id an int which is the id of the Note to get the name of.
     * @return a string which is the name of the Note retrieved.
     */
    private String getNoteName(int id) {
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME + " WHERE " + COL0 + " = " + id;
        Cursor cursor = this.getWritableDatabase().rawQuery(query, null);
        String data = "";

        if (cursor.moveToFirst()) {
            do {
                data += cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return data;
    }

    /**
     * The helper method gets the priority of the Note given the id of the Note.
     * @param id an int which is the id of the Note to get the priority of.
     * @return an int which is the priority of the Note retrieved.
     */
    private int getNotePriority(int id) {
        String query = "SELECT " + COL2 + " FROM " + TABLE_NAME + " WHERE " + COL0 + " = " + id;
        Cursor cursor = this.getWritableDatabase().rawQuery(query, null);
        int data = 0;

        if (cursor.moveToFirst()) {
            do {
                data = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return data;
    }

    /**
     * The helper method gets the contents of the Note given the id of the Note.
     * @param id an int which is the id of the Note to get the contents of.
     * @return a string which is the contents of the Note retrieved.
     */
    private String getNoteContents(int id) {
        String query = "SELECT " + COL3 + " FROM " + TABLE_NAME + " WHERE " + COL0 + " = " + id;
        Cursor cursor = this.getWritableDatabase().rawQuery(query, null);
        String data = "";

        if (cursor.moveToFirst()) {
            do {
                data += cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return data;
    }

    /**
     * The helper method determines if the Note is already in the database.
     * @param note a Note object to determine if is is within the database.
     * @return a boolean which is true if the Note is in the database and false otherwise.
     * @throws IllegalPriorityException is thrown if the priority is not with the range of 1 - 3 (inclusive).
     */
    private boolean isDataInDatabase(Note note) throws IllegalPriorityException {
        Cursor cursor = this.getAllData(TABLE_NAME);
        boolean isInDatabase = false;

        if (cursor.moveToFirst()) {
            do {
                Note n = new Note(cursor.getString(1), cursor.getInt(2), cursor.getString(3));
                if (note.equals(n)) {
                    isInDatabase = true;
                    break;
                }
            } while (cursor.moveToNext());
        }
        return isInDatabase;
    }

    /**
     * The helper method updates the indexes of the Database.
     */
    private void updateIndexes() {
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = this.getWritableDatabase().rawQuery(query, null);
        indexArrayList.clear();

        if (cursor.moveToFirst()) {
            do {
                indexArrayList.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
    }

    /**
     * The helper method determines if a priority is valid.
     * @param priority an int which is a Note priority.
     * @return a boolean which is true if the priority is valid and false otherwise.
     */
    private boolean isValidPriority(int priority) {
        return (priority <= 3) && (priority >= 1);
    }
}