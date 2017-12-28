package swch.bcit.ca.swchdatabases;

/**
 * Note class represents the characteristics of a note.
 *
 * @author SWCH
 * @version 2017
 */

public class Todo {

    /**
     * Name of the Todo.
     */
    private final String name;

    /**
     * Priority of Todo.
     * Priority ranges from one to three.
     */
    private final int priority;

    /**
     * Due date of the Todo.
     */
    private final Date dueDate;

    /**
     * The constructor which constructs an object of type Todo.
     * @param name a string which is the name of the Todo.
     * @param priority an int which is the priority of Todo.
     * @param dueDate a Date object which is the due date of the Todo.
     * @throws IllegalDateFormatException is thrown if the dueDate parameter is not of the proper format
     *                                    and is not within the legal ranges of months, days and years.
     *                                    dueDate must be in the format of month/day/year.
     *                                    Month ranges from 1 - 12 (inclusive).
     *                                    Day ranges from 1 - 31 (inclusive).
     *                                    year ranges from 2000 - 2999 (inclusive).
     * @throws IllegalPriorityException is thrown if the priority is not within the range of 1 - 3 (inclusive).
     */
    public Todo(String name, int priority, String dueDate) throws IllegalDateFormatException, IllegalPriorityException {
        this.name = name;
        if (isValidPriority(priority)) {
            this.priority = priority;
        } else {
            throw new IllegalPriorityException("Invalid priority.");
        }
        this.dueDate = new Date(dueDate);
    }

    /**
     * Gets the name of the Todo.
     * @return String which is the name of the Todo.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the priority of the Todo.
     * @return int which is the priority of the Todo.
     */
    public int getPriority() {return this.priority; }


    /**
     * Gets the due date of the Todo.
     * @return String which is the due date of the Todo.
     */
    public String getDueDate() { return this.dueDate.getDate(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Todo todo = (Todo) o;

        if (!getName().equals(todo.getName())) return false;
        return this.getDueDate().equals(todo.getDueDate());
    }

    /**
     * Helper method used to determine if the priority is a valid priority that is within
     * the range of 1 - 3 (inclusive).
     * @param priority an int which is the priority of the Todo.
     * @return boolean where true means the priority is valid and false means otherwise.
     */
    private boolean isValidPriority(int priority) {
        return (priority <= 3) && (priority >= 1);
    }
}