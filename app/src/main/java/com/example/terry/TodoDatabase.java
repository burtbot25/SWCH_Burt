package swch.bcit.ca.swchdatabases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

/**
 * TodoDatabase class is the database for Todos.
 *
 * @author SWCH
 * @version 2017
 */

public class TodoDatabase extends Database {

    /**
     * Name of the database table.
     */
    private static final String TABLE_NAME = "todo_table";

    /**
     * The ID column of the table for todos.
     */
    private static final String COL0 = "ID";

    /**
     * The name column of the table for todos.
     */
    private static final String COL1 = "todo_name";

    /**
     * The priority column of the table for todos.
     */
    private static final String COL2 = "priority";

    /**
     * The due date column of the table for todos.
     */
    private static final String COL3 = "due_date";

    /**
     * The array list is used to manage the IDs of the data entries.
     */
    private static ArrayList<Integer> indexArrayList = new ArrayList<>();

    /**
     * The constructor which constructs an object of type NoteDatabase.
     * @param context the application context.
     */
    public TodoDatabase(Context context) {
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
        if (!(data instanceof Todo)) {
            throw new DataExistenceException("Data is not a Todo.");
        }

        Todo todo = (Todo) data;

        try {
            if (isDataInDatabase(todo)) {
                throw new DataExistenceException("Data is already in database.");
            }
        } catch (IllegalDateFormatException e) {
            throw new DataExistenceException("Date is not formatted correctly.");
        }

        updateIndexes();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, todo.getName());
        contentValues.put(COL2, todo.getPriority());
        contentValues.put(COL3, todo.getDueDate());
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
     * Gets the Todo from the database.
     * @param id an int which is the id of the Todo to retrieve from the data base.
     * @return Todo the todo containing all the data of the todo.
     * @throws DataExistenceException is thrown if the Todo is not in the database.
     * @throws IllegalDateFormatException is thrown if the date is not of the proper format
     *                                    and is not within the legal ranges of months, days and years.
     *                                    dueDate must be in the format of month/day/year.
     *                                    Month ranges from 1 - 12 (inclusive).
     *                                    Day ranges from 1 - 31 (inclusive).
     *                                    year ranges from 2000 - 2999 (inclusive).
     * @throws IllegalPriorityException is thrown if the priority is not within the range of 1 - 3 (inclusive).
     */
    public Todo getTodo(int id)
            throws DataExistenceException, IllegalDateFormatException, IllegalPriorityException {
        try {
            updateIndexes();
            int index = indexArrayList.get(id);
            return new Todo(getTodoName(index),
                            getTodoPriority(index),
                            getTodoDueDate(index));
        } catch (IndexOutOfBoundsException e) {
            throw new DataExistenceException("Data is not in database.");
        }
    }

    /**
     * Updates the Todo to a new Todo.
     * @param newTodoName a string which is the new Todo name to update to.
     * @param newTodoPriority an int which is the new priority to update to.
     * @param newTodoDueDate a string which is the new due date to update to.
     * @param id an int which is the id of the Todo to update.
     * @throws DataExistenceException is thrown if the Todo is not in the database.
     * @throws IllegalPriorityException is thrown if the priority is not within the range of 1 - 3 (inclusive).
     * @throws IllegalDateFormatException is thrown if the date is not of the proper format
     *                                    and is not within the legal ranges of months, days and years.
     *                                    dueDate must be in the format of month/day/year.
     *                                    Month ranges from 1 - 12 (inclusive).
     *                                    Day ranges from 1 - 31 (inclusive).
     *                                    year ranges from 2000 - 2999 (inclusive).
     */
    public void updateTodo(String newTodoName, int newTodoPriority, String newTodoDueDate, int id)
            throws DataExistenceException, IllegalPriorityException, IllegalDateFormatException {
        updateTodoName(newTodoName, id);
        updateTodoPriority(newTodoPriority, id);
        updateTodoDueDate(newTodoDueDate, id);
    }

    /**
     * Updates the name of the Todo to a new Todo name.
     * @param newTodoName a string which is the new Todo name to update to.
     * @param id an int which is the id of the Todo to update.
     * @throws DataExistenceException is thrown if the Todo is not in the database.
     */
    public void updateTodoName(String newTodoName, int id)
            throws DataExistenceException {
        try {
            updateIndexes();
            int index = indexArrayList.get(id);
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL1, newTodoName);
            this.getWritableDatabase().update(TABLE_NAME, contentValues, COL0 + " = ?", new String[]{Integer.toString(index)});
        } catch (IndexOutOfBoundsException e) {
            throw new DataExistenceException("Data is not in database.");
        }
    }

    /**
     * Updates the priority of the Todo to a new priority.
     * @param newTodoPriority an int which is the new priority to update to.
     * @param id an int which is the id of the Todo to update.
     * @throws DataExistenceException is thrown if the Todo is not within the database.
     * @throws IllegalPriorityException is thrown if the priority is not within the range of 1 - 3 (inclusive).
     */
    public void updateTodoPriority(int newTodoPriority, int id)
            throws DataExistenceException, IllegalPriorityException {
        if (!isValidPriority(newTodoPriority)) {
            throw new IllegalPriorityException("Invalid priority.");
        }
        try {
            updateIndexes();
            int index = indexArrayList.get(id);
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL2, newTodoPriority);
            this.getWritableDatabase().update(TABLE_NAME, contentValues, COL0 + " = ?", new String[]{Integer.toString(index)});
        } catch (IndexOutOfBoundsException e) {
            throw new DataExistenceException("Data is not in database.");
        }
    }

    /**
     * Updates the due date of the Todo to a new due date.
     * @param newTodoDueDate a string which is the new due date to update to.
     * @param id an int which is the id of the Todo to update.
     * @throws DataExistenceException is thrown if the Todo is not within the database.
     * @throws IllegalDateFormatException is thrown if the date is not of the proper format
     *                                    and is not within the legal ranges of months, days and years.
     *                                    dueDate must be in the format of month/day/year.
     *                                    Month ranges from 1 - 12 (inclusive).
     *                                    Day ranges from 1 - 31 (inclusive).
     *                                    year ranges from 2000 - 2999 (inclusive).
     */
    public void updateTodoDueDate(String newTodoDueDate, int id)
            throws DataExistenceException, IllegalDateFormatException {
        Date date = new Date(newTodoDueDate);
        try {
            updateIndexes();
            int index = indexArrayList.get(id);
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL3, newTodoDueDate);
            this.getWritableDatabase().update(TABLE_NAME, contentValues, COL0 + " = ?", new String[]{Integer.toString(index)});
        } catch (IndexOutOfBoundsException e) {
            throw new DataExistenceException("Data is not in database.");
        }
    }

    /**
     * The helper method gets the name of the Todo given the id of the Todo.
     * @param id an int which is the id of the Todo to get the name of.
     * @return a string which is the name of the Todo retrieved.
     */
    private String getTodoName(int id) {
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
     * The helper method gets the priority of the Todo given the id of the Todo.
     * @param id an int which is the id of the Todo to get the priority of.
     * @return an int which is the priority of the Todo retrieved.
     */
    private int getTodoPriority(int id) {
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
     * The helper method gets the due date of the Todo given the id of the Todo.
     * @param id an int which is the id of the Todo to get the due date of.
     * @return a string which is the due date of the Todo retrieved.
     */
    private String getTodoDueDate(int id) {
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
     * The helper method determines if the Todo is already in the database.
     * @param todo a Todo object to determine if is is within the database.
     * @return a boolean which is true if the Todo is in the database and false otherwise.
     * @throws IllegalDateFormatException is thrown if the date is not of the proper format
     *                                    and is not within the legal ranges of months, days and years.
     *                                    dueDate must be in the format of month/day/year.
     *                                    Month ranges from 1 - 12 (inclusive).
     *                                    Day ranges from 1 - 31 (inclusive).
     *                                    year ranges from 2000 - 2999 (inclusive).
     * @throws IllegalPriorityException is thrown if the priority is not within the range of 1 - 3 (inclusive).
     */
    private boolean isDataInDatabase(Todo todo) throws IllegalDateFormatException, IllegalPriorityException {
        Cursor cursor = this.getAllData(TABLE_NAME);
        boolean isInDatabase = false;

        if (cursor.moveToFirst()) {
            do {
                Todo t = new Todo(cursor.getString(1), cursor.getInt(2), cursor.getString(3));
                if (todo.equals(t)) {
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
     * @param priority an int which is a Todo priority.
     * @return a boolean which is true if the priority is valid and false otherwise.
     */
    private boolean isValidPriority(int priority) {
        return (priority <= 3) && (priority >= 1);
    }
}