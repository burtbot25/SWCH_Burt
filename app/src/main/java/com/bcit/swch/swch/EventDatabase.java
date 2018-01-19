package com.bcit.swch.swch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * EventDatabase class is the database for Events.
 *
 * @author SWCH
 * @version 2017
 */

public class EventDatabase extends Database {

    /**
     * Name of the database table.
     */
    private static final String TABLE_NAME = "event_table";

    /**
     * The ID column of the table for events.
     */
    private static final String COL0 = "ID";

    /**
     * The name column of the table for events.
     */
    private static final String COL1 = "event_name";

    /**
     * The priority column of the table for events.
     */
    private static final String COL2 = "priority";

    /**
     * The event date column of the table for events.
     */
    private static final String COL3 = "event_date";

    /**
     * The array list is used to manage the IDs of the data entries.
     */
    private static ArrayList<Integer> indexArrayList = new ArrayList<>();

    /**
     * The constructor which constructs an object of type EventDatabase.
     * @param context the application context.
     */
    public EventDatabase(Context context) {
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
        if (!(data instanceof Event)) {
            throw new DataExistenceException("Data is not an Event.");
        }

        Event event = (Event) data;

        try {
            if (isDataInDatabase(event)) {
                throw new DataExistenceException("Data is already in database.");
            }
        } catch (IllegalDateFormatException e) {
            throw new DataExistenceException("Date is not formatted correctly.");
        }

        updateIndexes();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, event.getName());
        contentValues.put(COL2, event.getPriority());
        contentValues.put(COL3, event.getEventDate());
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
     * Gets the Event from the database.
     * @param id an int which is the id of the Event to retrieve from the database.
     * @return Event the event containing all the data of the Event.
     * @throws DataExistenceException is thrown if the Event is not in the database.
     * @throws IllegalDateFormatException is thrown if the date is not of the proper format
     *                                    and is not within the legal ranges of months, days and years.
     *                                    dueDate must be in the format of month/day/year.
     *                                    Month ranges from 1 - 12 (inclusive).
     *                                    Day ranges from 1 - 31 (inclusive).
     *                                    year ranges from 2000 - 2999 (inclusive).
     * @throws IllegalPriorityException is thrown if the priority is not within the range of 1 - 3 (inclusive).
     */
    public Event getEvent(int id)
            throws DataExistenceException, IllegalDateFormatException, IllegalPriorityException {
        try {
            updateIndexes();
            int index = indexArrayList.get(id);
            return new Event(getEventName(index),
                             getEventPriority(index),
                             getEventDate(index));
        } catch (IndexOutOfBoundsException e) {
            throw new DataExistenceException("Data is not in database.");
        }
    }

    /**
     * Updates the Event to a new Event.
     * @param newEventName a string which is the new Event name to update to.
     * @param newEventPriority an int which is the new priority to update to.
     * @param newEventDate a string which is the new event date to update to.
     * @param id an int which is the id of the Event to update.
     * @throws DataExistenceException is thrown if the Event is not in the database.
     * @throws IllegalPriorityException is thrown if the priority is not within the range of 1 - 3 (inclusive).
     * @throws IllegalDateFormatException is thrown if the date is not of the proper format
     *                                    and is not within the legal ranges of months, days and years.
     *                                    dueDate must be in the format of month/day/year.
     *                                    Month ranges from 1 - 12 (inclusive).
     *                                    Day ranges from 1 - 31 (inclusive).
     *                                    year ranges from 2000 - 2999 (inclusive).
     */
    public void updateEvent(String newEventName, int newEventPriority, String newEventDate, int id)
            throws DataExistenceException, IllegalPriorityException, IllegalDateFormatException {
        updateEventName(newEventName, id);
        updateEventPriority(newEventPriority, id);
        updateEventDate(newEventDate, id);
    }

    /**
     * Updates the name of the Event to a new Event name.
     * @param newEventName a string which is the new Event name to update to.
     * @param id an int which is the id of the Event to update.
     * @throws DataExistenceException is thrown if the Event is not in the database.
     */
    public void updateEventName(String newEventName, int id)
            throws DataExistenceException {
        try {
            updateIndexes();
            int index = indexArrayList.get(id);
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL1, newEventName);
            this.getWritableDatabase().update(TABLE_NAME, contentValues, COL0 + " = ?", new String[]{Integer.toString(index)});
        } catch (IndexOutOfBoundsException e) {
            throw new DataExistenceException("Data is not in database.");
        }
    }

    /**
     * Updates the priority of the Event to a new priority.
     * @param newEventPriority an int which is the new priority to update to.
     * @param id an int which is the id of the Event to update.
     * @throws DataExistenceException is thrown if the Event is not within the database.
     * @throws IllegalPriorityException is thrown if the priority is not within the range of 1 - 3 (inclusive).
     */
    public void updateEventPriority(int newEventPriority, int id)
            throws DataExistenceException, IllegalPriorityException {
        if (!isValidPriority(newEventPriority)) {
            throw new IllegalPriorityException("Invalid priority.");
        }
        try {
            updateIndexes();
            int index = indexArrayList.get(id);
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL2, newEventPriority);
            this.getWritableDatabase().update(TABLE_NAME, contentValues, COL0 + " = ?", new String[]{Integer.toString(index)});
        } catch (IndexOutOfBoundsException e) {
            throw new DataExistenceException("Data is not in database.");
        }
    }

    /**
     * Updates the date of the event to a new event date.
     * @param newEventDate a string which is the new date to update to.
     * @param id an int which is the id of the Event to update.
     * @throws DataExistenceException is thrown if the Event is not within the database.
     * @throws IllegalDateFormatException is thrown if the date is not of the proper format
     *                                    and is not within the legal ranges of months, days and years.
     *                                    dueDate must be in the format of month/day/year.
     *                                    Month ranges from 1 - 12 (inclusive).
     *                                    Day ranges from 1 - 31 (inclusive).
     *                                    year ranges from 2000 - 2999 (inclusive).
     */
    public void updateEventDate(String newEventDate, int id)
            throws DataExistenceException, IllegalDateFormatException {
        Date date = new Date(newEventDate);
        try {
            updateIndexes();
            int index = indexArrayList.get(id);
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL3, newEventDate);
            this.getWritableDatabase().update(TABLE_NAME, contentValues, COL0 + " = ?", new String[]{Integer.toString(index)});
        } catch (IndexOutOfBoundsException e) {
            throw new DataExistenceException("Data is not in database.");
        }
    }

    /**
     * The helper method gets the name of the Event given the id of the Event.
     * @param id an int which is the id of the Event to get the name of.
     * @return a string which is the name of the Event retrieved.
     */
    private String getEventName(int id) {
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
     * The helper method gets the priority of the Event given the id of the Event.
     * @param id an int which is the id of the Event to get the priority of.
     * @return an int which is the priority of the Event retrieved.
     */
    private int getEventPriority(int id) {
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
     * The helper method gets the date of the Event given the id of the Event.
     * @param id an int which is the id of the Event to get the date of.
     * @return a string which is the date of the Event retrieved.
     */
    private String getEventDate(int id) {
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
     * The helper method determines if the Event is already in the database.
     * @param event an Event object to determine if is is within the database.
     * @return a boolean which is true if the Event is in the database and false otherwise.
     * @throws IllegalDateFormatException is thrown if the date is not of the proper format
     *                                    and is not within the legal ranges of months, days and years.
     *                                    dueDate must be in the format of month/day/year.
     *                                    Month ranges from 1 - 12 (inclusive).
     *                                    Day ranges from 1 - 31 (inclusive).
     *                                    year ranges from 2000 - 2999 (inclusive).
     * @throws IllegalPriorityException is thrown if the priority is not within the range of 1 - 3 (inclusive).
     */
    private boolean isDataInDatabase(Event event) throws IllegalDateFormatException, IllegalPriorityException {
        Cursor cursor = this.getAllData(TABLE_NAME);
        boolean isInDatabase = false;

        if (cursor.moveToFirst()) {
            do {
                Event e = new Event(cursor.getString(1), cursor.getInt(2), cursor.getString(3));
                if (event.equals(e)) {
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
     * @param priority an int which is an Event priority.
     * @return a boolean which is true if the priority is valid and false otherwise.
     */
    private boolean isValidPriority(int priority) {
        return (priority <= 3) && (priority >= 1);
    }
}
