package swch.bcit.ca.swchdatabases;

/**
 * Event class represents the characteristics of an event.
 *
 * @author SWCH
 * @version 2017
 */

public class Event {

    /**
     * The name of the event.
     */
    private final String name;

    /**
     * The priority of the event.
     */
    private final int priority;

    /**
     * The date of the event.
     */
    private final Date eventDate;

    /**
     * The constructor constructs an object of type Event.
     * @param name a string which is the name of the event.
     * @param priority an int which is the priority of the event.
     * @param eventDate a string which is the date of the event.
     * @throws IllegalDateFormatException is thrown if the eventDate parameter is not of the proper format
     *                                    and is not within the legal ranges of months, days and years.
     *                                    dueDate must be in the format of month/day/year.
     *                                    Month ranges from 1 - 12 (inclusive).
     *                                    Day ranges from 1 - 31 (inclusive).
     *                                    year ranges from 2000 - 2999 (inclusive).
     * @throws IllegalPriorityException is thrown if the priority is not within the range of 1 - 3 (inclusive).
     */
    public Event(String name, int priority, String eventDate) throws IllegalDateFormatException, IllegalPriorityException{
        if (!isValidPriority(priority)) {
            throw new IllegalPriorityException("Invalid priority.");
        }
        this.name = name;
        this.priority = priority;
        this.eventDate = new Date(eventDate);
    }

    /**
     * Gets the name of the Event.
     * @return String which is the name of the Event.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the priority of the Event.
     * @return int which is the priority of the Event.
     */
    public int getPriority() {
        return this.priority;
    }

    /**
     * Gets the date of the Event.
     * @return String which is the date of the Event.
     */
    public String getEventDate() {
        return this.eventDate.getDate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (!getName().equals(event.getName())) return false;
        return getEventDate().equals(event.getEventDate());
    }

    /**
     * Helper method used to determine if the priority is a valid priority that is within
     * the range of 1 - 3 (inclusive).
     * @param priority an int which is the priority of the Event.
     * @return boolean where true means the priority is valid and false means otherwise.
     */
    private boolean isValidPriority(int priority) {
        return (priority <= 3) && (priority >= 1);
    }
}