package swch.bcit.ca.swchdatabases;

/**
 * Note class represents characteristics of a note.
 *
 * @author SWCH
 * @version 2017
 */

public class Note {

    /**
     * The name of the note.
     */
    private final String name;

    /**
     * The priority of the note.
     */
    private final int priority;

    /**
     * The contents of the note.
     */
    private final String contents;

    /**
     * The constructor constructs an object of type Note.
     * @param name a string which is the name of the note.
     * @param priority an int which is the priority of the note.
     * @param contents a string which is the contents of the note.
     * @throws IllegalPriorityException is thrown if the priority is not with the range of 1 - 3 (inclusive).
     */
    public Note(String name, int priority, String contents) throws IllegalPriorityException {
        if (!isValidPriority(priority)) {
            throw new IllegalPriorityException("Invalid priority.");
        }
        this.name = name;
        this.priority = priority;
        this.contents = contents;
    }

    /**
     * Gets the name of the Note.
     * @return string which is the name of the Note.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the priority of the Note.
     * @return int which is the priority of the Note.
     */
    public int getPriority() {
        return this.priority;
    }

    /**
     * Gets the contents of the Note.
     * @return string which is the contents of the Note.
     */
    public String getContents() {
        return this.contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        if (!getName().equals(note.getName())) return false;
        return getContents().equals(note.getContents());
    }

    /**
     * Helper method used to determine if the priority is a valid priority that is within
     * the range of 1 - 3 (inclusive).
     * @param priority an int which is the priority of the Note.
     * @return boolean where true means the priority is valid and false means otherwise.
     */
    private boolean isValidPriority(int priority) {
        return (priority <= 3) && (priority >= 1);
    }
}