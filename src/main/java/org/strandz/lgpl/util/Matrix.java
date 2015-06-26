package org.strandz.lgpl.util;

import java.util.List;
import java.util.ArrayList;

/**
 * Matrix of String values, as might read from a spreadsheet.
 * Read by column going along the top then row going down
 * (0,0) top left corner
 * (0,1) first row, but second column
 *
 * User: Chris
 * Date: 01/12/2008
 * Time: 10:23:22 AM
 */
public class Matrix
{
    List<List<String>> rows = new ArrayList<List<String>>();

    public static class Position
    {
        public int col;
        public int row;
        public Object origValue;

        public Position( int col, int row)
        {
            this.col = col;
            this.row = row;
        }

        public Position( int col, int row, Object origValue)
        {
            this.col = col;
            this.row = row;
            this.origValue = origValue;
        }

        public String toString()
        {
            return col + ", " + row + ", <" + origValue + ">";
        }
    }

    public static Matrix.Position newPostionInstance( int col, int row)
    {
        Matrix.Position result = new Position( col, row);
        return result;
    }

    public void populate( List<String[]> entries)
    {
        for (int i = 0; i < entries.size(); i++)
        {
            String line[] = entries.get(i);
            if(line.length == 1 && line[0].equals( ""))
            {
                //Means a completely empty line
                //Err.pr( "Empty line at " + i);
            }
            else
            {
                List<String> list = new ArrayList<String>();
                for (int j = 0; j < line.length; j++)
                {
                    String cellValue = line[j];
                    list.add( cellValue);
                }
                rows.add( list);
            }
        }
    }

    public void addRow( List<String> row)
    {
        rows.add( row);
    }

    public List<String> getRow( int row)
    {
        List<String> result = null;
        if(row < rows.size())
        {
            result = rows.get( row);
        }
        return result;
    }

    public List<Position> getNotNullColumn( int col, int startRow)
    {
        List<Position> result = new ArrayList<Position>();
        List<String> rowOfValues;
        for(int row = startRow; (rowOfValues = getRow( row)) != null; row++)
        {
            //Err.pr( "rowOfValues: <" + rowOfValues + ">");
            String value = rowOfValues.get( col);
            if(!Utils.isBlank( value))
            {
                Position position = new Position( col, row, value);
                result.add( position);
            }
        }
        return result;
    }

    public List<Position> getColumn( int col, int startRow)
    {
        return getColumn( col, startRow, Integer.MAX_VALUE);
    }

    public List<Position> getColumn( int col, int startRow, int rowCount)
    {
        List<Position> result = new ArrayList<Position>();
        List<String> rowOfValues;
        int timesEmpty = 0;
        for(int row = startRow; row < startRow+rowCount; row++)
        {
            rowOfValues = getRow( row);
            if(rowOfValues == null)
            {
                break;
            }
            else
            {
                if(!rowOfValues.isEmpty())
                {
                    String value = rowOfValues.get( col);
                    Position position = new Position( col, row, value);
                    result.add( position);
                }
                else
                {
                    timesEmpty++;
                    if(timesEmpty > 20)
                    {
                        break;
                    }
                }
            }
        }
        return result;
    }

    public List<Position> getNotNullRow( int row, int startCol)
    {
        List<Position> result = new ArrayList<Position>();
        List<String> rowOfValues = getRow( row);
        for(int col = startCol; col < rowOfValues.size(); col++)
        {
            String value = rowOfValues.get(col);
            if(!Utils.isBlank( value))
            {
                Position position = new Position( col, row, value);
                result.add( position);
            }
        }
        return result;
    }

    public List<Position> getObeysCountRow( int row, int startCol, int count)
    {
        List<Position> result = new ArrayList<Position>();
        List<String> rowOfValues = getRow( row);
        for(int col = startCol; col < rowOfValues.size(); col++)
        {
            if(col-startCol == count)
            {
                break;
            }
            else
            {
                String value = rowOfValues.get(col);
                Position position = new Position( col, row, value);
                result.add( position);
            }
        }
        return result;
    }

    public List<Position> getObeysFormatRow( int row, int startCol, String format)
    {
        List<Position> result = new ArrayList<Position>();
        List<String> rowOfValues = getRow( row);
        for(int col = startCol; col < rowOfValues.size(); col++)
        {
            String value = rowOfValues.get(col);
            if(!value.isEmpty())
            {
                if(Utils.obeysFormat( value, format))
                {
                    Position position = new Position( col, row, value);
                    result.add( position);
                }
                else
                {
                    break;
                }
            }
        }
        return result;
    }

    public List<Position> getRowContaining( int row, int startCol, List<String> names)
    {
        List<Position> result = new ArrayList<Position>();
        List<String> rowOfValues = getRow( row);
        for(int col = startCol; col < rowOfValues.size(); col++)
        {
            String value = rowOfValues.get(col);
            if(!Utils.isBlank( value))
            {
                if(names.contains( value))
                {
                    Position position = new Position( col, row, value);
                    result.add( position);
                }
                else
                {
                    Err.pr( "Skipping <" + value + "> because it is not in " + names);
                }
            }
        }
        return result;
    }

    /**
     * List will stop as soon as the numbers stop contiguously ascending.
     */
    public List<Position> getAscendingNumbersRow( int row, int startCol)
    {
        List<Position> result = new ArrayList<Position>();
        List<String> rowOfValues = getRow( row);
        int lastNumber = Utils.UNSET_INT;
        for(int col = startCol; col < rowOfValues.size(); col++)
        {
            String strValue = rowOfValues.get(col);
            if(!Utils.isBlank( strValue))
            {
                int intValue = Integer.parseInt( strValue);
                if(intValue > lastNumber)
                {
                    Position position = new Position( col, row, intValue);
                    result.add( position);
                    lastNumber = intValue;
                }
                else
                {
                    break;
                }
            }
            else
            {
                break;
            }
        }
        return result;
    }

    public String getValue( Position position)
    {
        String result = null;
        List<String> rowOfStrings = rows.get( position.row);
        if(rowOfStrings.isEmpty())
        {
            //Err.error( "Gone out of range of the matrix at row: " + position.row);
        }
        else
        {
            if(rowOfStrings.size() > position.col)
            {
                result = rowOfStrings.get( position.col);
                position.origValue = result;
            }
        }
        return result;
    }

    public void print()
    {
        for (int i = 0; i < rows.size(); i++)
        {
            List<String> row = rows.get(i);
            StringBuffer buf = new StringBuffer();
            for (int j = 0; j < row.size(); j++)
            {
                String s = row.get( j);
                buf.append( s);
                buf.append( ',');
            }
            Err.pr( buf);
        }
    }
}
