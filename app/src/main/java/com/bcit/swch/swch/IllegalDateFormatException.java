package com.bcit.swch.swch;

/**
 * IllegalDateFormatException class.
 *
 * @author SWCH
 * @version 2017
 */

public class IllegalDateFormatException extends Exception {

    /**
     * The constructor which constructs an object of type IllegalDateFormatException.
     * @param message a string represents the message to output when the exception is thrown.
     */
    public IllegalDateFormatException(String message) {
        super(message);
    }
}