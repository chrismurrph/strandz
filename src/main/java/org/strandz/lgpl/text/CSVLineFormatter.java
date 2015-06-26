package org.strandz.lgpl.text;

import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;

import java.util.List;
import java.util.ArrayList;

/**
 * Given an array of Strings this class will produce a CSV (comma separated values) line.
 * In the output tokens will be separated by spaces (where they are to be merged) or commas
 *
 * User: Chris
 * Date: 16/01/2009
 * Time: 2:20:19 PM
 */
public class CSVLineFormatter
{
    private String values[];
    private List<Integer[]> listOfLists;

    public void setValues(String[] values)
    {
        this.values = values;
    }

    public void setColumns( List<Integer[]> listOfLists)
    {
        this.listOfLists = listOfLists;
    }

    public String getLine()
    {
        StringBuffer result = new StringBuffer();
        Assert.notNull( values);
        Assert.notNull( listOfLists);
        for (int i = 0; i < listOfLists.size(); i++)
        {
            Integer[] integers = listOfLists.get(i); //indicates value columns that need to have spaces put between them
            StringBuffer token = new StringBuffer();
            for (int j = 0; j < integers.length; j++)
            {
                Integer integer = integers[j];
                if(j > 0)
                {
                    token.append( ' ');
                }
                String value = values[integer];
                if(value == null)
                {
                    value = "";
                }
                token.append( value);
            }
            String trimmedToken = token.toString().trim();
            result.append( trimmedToken);
            if(i < listOfLists.size()-1)
            {
                result.append( ',');
            }
        }
        return result.toString();
    }

    public static void main(String[] args)
    {
        String values[] = new String[]{ "Chris", "Murphy", "1 Philip Ave.", "Leabrook", "5034",
            "02 9809 4356", "0409 158 667", "", "murrph@gmail.com"};
        CSVLineFormatter csvLineFormatter = new CSVLineFormatter();
        csvLineFormatter.setValues( values);
        List<Integer[]> columns = new ArrayList<Integer[]>();
        columns.add( new Integer[]{0,1});
        columns.add( new Integer[]{2,3,4});
        columns.add( new Integer[]{5});
        columns.add( new Integer[]{6});
        columns.add( new Integer[]{7});
        columns.add( new Integer[]{8});
        csvLineFormatter.setColumns( columns);
        Err.pr( csvLineFormatter.getLine());
    }
}
