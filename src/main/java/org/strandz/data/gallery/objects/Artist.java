package org.strandz.data.gallery.objects;

import org.strandz.data.gallery.objects.auto._Artist;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Artist extends _Artist
{
    static final String DEFAULT_DATE_FORMAT = "yyyyMMdd";

    /**
     * Sets date of birth using a string in format yyyyMMdd.
     */
    public void setDateOfBirthString(String yearMonthDay)
    {
        if (yearMonthDay == null)
        {
            setDateOfBirth(null);
        }
        else
        {

            Date date;
            try
            {
                date = new SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(yearMonthDay);
            }
            catch (ParseException e)
            {
                throw new IllegalArgumentException("A date argument must be in format '"
                    + DEFAULT_DATE_FORMAT
                    + "': "
                    + yearMonthDay);
            }

            setDateOfBirth(date);
        }
    }

    public String toString()
    {
        return getArtistName();
    }
}



