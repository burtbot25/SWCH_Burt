package swch.bcit.ca.swchdatabases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database class is an abstract class that represents the characteristics of
 * a database.
 *
 * @author SWCH
 * @version 2017
 */

public abstract class Database extends SQLiteOpenHelper {

    /**
     * The constructor which constructs an object of type Database.
     * @param context the application context.
     */
    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Adds the data to the database.
     * @param data is the data to be added to the database.
     * @throws DataExistenceException is thrown if the data object is already in the database.
     * @throws IllegalPriorityException is thrown if the priority is not within the range of 1 - 3 (inclusive).
     */
    public abstract void addDataEntry(Object data) throws DataExistenceException, IllegalPriorityException;

    /**
     * Deletes the data from the database given the ID of the data object.
     * @param id is the ID of the data object to be deleted.
     * @throws DataExistenceException is thrown if the data object to be deleted is not in the database.
     */
    public abstract void deleteDataEntry(int id) throws DataExistenceException;

    /**
     * Deletes everything in the database and resets the primary key (the ID).
     * @param table a string which is the name of the database's table used to make the database.
     */
    public void deleteAllData(String table) {
        this.getWritableDatabase().execSQL("DELETE FROM " + table);
        this.getWritableDatabase().execSQL("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = " + "'" + table + "'");
        this.getWritableDatabase().execSQL("VACUUM");
    }

    /**
     * Gets all the data in the database.
     * @param table a string which is the name of the database's table used to make the database.
     * @return Cursor which contains the results from the SQL query.
     */
    public Cursor getAllData(String table) {
        return this.getWritableDatabase().rawQuery("SELECT * FROM " + table, null);
    }
}