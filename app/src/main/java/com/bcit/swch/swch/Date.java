package com.bcit.swch.swch;

/**
 * Date class represents the characteristics of a date.
 *
 * @author SWCH
 * @version 2017
 */

public class Date {

    /**
     * The regex pattern used to determine if the date is formatted
     * correctly.
     */
    private static final String PATTERN = "(1|2|3|4|5|6|7|8|9|10|11|12){1}" +
                                          "/(1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24|25|26|27|28|29|30|31){1}" +
                                          "/2\\d\\d\\d";

    /**
     * The date.
     * Dates are formatted as month/day/year.
     * Month ranges from 1 - 12 (inclusive).
     * Day ranges from 1 - 31 (inclusive).
     * Year ranges from 2000 - 2999 (inclusive).
     */
    private final String date;

    /**
     * The constructor which constructs an object of type Date.
     * @param date a string which is the date.
     * @throws IllegalDateFormatException is thrown if the date parameter is not of the proper format
     *                                    and is not within the legal ranges of months, days and years.
     *                                    date must be in the format of month/day/year.
     *                                    Month ranges from 1 - 12 (inclusive).
     *                                    Day ranges from 1 - 31 (inclusive).
     *                                    year ranges from 2000 - 2999 (inclusive).
     */
    public Date(String date) throws IllegalDateFormatException {
        if (!date.matches(PATTERN)) {
            throw new IllegalDateFormatException("Date is not formatted properly.");
        } else {
            this.date = date;
        }
    }

    /**
     * Gets the date of the Date object.
     * @return string which is the date.
     */
    public String getDate() {
        return this.date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Date date1 = (Date) o;

        return this.getDate().equals(date1.getDate());
    }
}