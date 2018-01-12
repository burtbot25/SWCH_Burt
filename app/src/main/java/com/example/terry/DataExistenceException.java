package swch.bcit.ca.swchdatabases;

/**
 * DataExistenceException class.
 *
 * @author SWCH
 * @version 2017
 */

public class DataExistenceException extends Exception {

    /**
     * The constructor which constructs an object of type DataExistenceException.
     * @param message a string represents the message to output when the exception is thrown.
     */
    public DataExistenceException(String message) {
        super(message);
    }
}