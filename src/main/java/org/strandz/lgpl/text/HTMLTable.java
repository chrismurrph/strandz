/*
    Strandz - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    Strandz is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Strandz; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    In addition, as a special exception, Chris Murphy gives
    permission to link the code of this program with any Java software for
    which complete source code is available at no cost (even if distribution
    or modification of that source code is restricted), and distribute the
    results of that linking. You must obey the GNU General Public
    License in all respects for all of the code used other than this Java code.
    If you modify this file, you may extend this exception to your version of
    the file, but you are not obligated to do so. If you do not wish to do so,
    delete this exception statement from your version.

    The authors can be contacted via www.strandz.org
*/
package org.strandz.lgpl.text;

import org.apache.commons.lang.math.NumberUtils;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class HTMLTable
{
    private ResultSet rs;
    private List<String> colHeadings;
    private Integer colPercentages[];
    private WordTokenizerAbstractFactory tokenizerCreator;
    
    static final String TAB = "  ";
    
    public static void main(String[] args)
    {
        List<String> colHeadings = new ArrayList<String>();
        colHeadings.add( "head 1");
        colHeadings.add( "head 2");
        colHeadings.add( "head 3");
        Integer colPercentages[] = new Integer[3];
        colPercentages[0] = new Integer( 33);
        colPercentages[1] = new Integer( 33);
        colPercentages[2] = new Integer( 33);
        HTMLTable table = new HTMLTable( colHeadings, colPercentages, new TextTokenizerConcreteFactory());
        table.addRow( "one two three");
        table.addRow( "four five six");
        table.addRow( "seven eight nine");
        String out = table.toHTMLTable().toString();
        Err.pr( out);
    }
    
    public HTMLTable( List<String> colHeadings, Integer colPercentages[], WordTokenizerAbstractFactory tokenizerCreator)
    {
        Assert.notEmpty( colHeadings);
        boolean cond = colPercentages.length == colHeadings.size();
        if(!cond)
        {
            Err.pr( "Got " + colPercentages.length + " column percentages");
            Err.pr( "Got " + colHeadings.size() + " column headings");
            Print.prArray( colPercentages, "Col Percentages");
            Print.prList( colHeadings, "Col Headings");
        }
        Assert.isTrue( cond);
        rs = new ResultSet( colHeadings);
        this.colHeadings = colHeadings;
        this.colPercentages = colPercentages;
        this.tokenizerCreator = tokenizerCreator;
    }

    /**
     * Start collecting data for a new table that has the same column headings
     */
    public void newTable()
    {
        rs = new ResultSet( colHeadings);
    }
    
    public void addRow( String s)
    {
        rs.addRow( s);
    }
    
    public class ResultSet
    {
        private List<String> columnLabels = new ArrayList<String>();
        private List<String> rows = new ArrayList<String>();
        
        public ResultSet( List<String> columnLabels)
        {
            this.columnLabels = columnLabels;
        }

        public ResultSet()
        {
        }

        public void addColumnHeading( String s)
        {
            columnLabels.add( s);
        }
        
        public void addRow( String s)
        {
            rows.add( s);
        }
        
        int getColumnCount()
        {
            return columnLabels.size();
        }

        int getRowCount()
        {
            return rows.size();
        }
                
        String getColumnLabel( int col)
        {
            return columnLabels.get( col);
        }

        Row getRow( int ele)
        {
            return new Row( rows.get( ele), getColumnCount());
        }
    }
    
    class Row
    {
        private String line;
        private String cells[];
        
        public Row( String s, int numCols)
        {
            this.line = s;
            cells = new String[numCols];
            //TextTokenizer reader = new TextTokenizer( new StringBuffer( s));
            WordTokenizer reader = tokenizerCreator.newInstance( new StringBuffer( s));
            for(int i = 0; i < cells.length; i++)
            {
                cells[i] = reader.readToken();
            }
        }
        
        public String toString()
        {
            return line;
        }
        
        public String getCellValue( int col)
        {
            return cells[col];
        }
    }
    
    private String startDetailTag( String value)
    {
        String result;
        String trimmed = value.trim();
        if(NumberUtils.isNumber( trimmed))
        {
            result = "<TD align=right>";
        }
        else
        {
            result = "<TD>";
        }
        return result;
    }
    
    //<TH width=44%>
    private String startHeadingTag( int col)
    {
        String result;
        Integer percentage = colPercentages[col];
        result = "<TH width=" + percentage.toString() + "%>";
        return result;
    }
    
    public StringBuffer toHTMLTable()
    {
        StringBuffer result = new StringBuffer();
        result.append("<P ALIGN='center'><TABLE BORDER=0>" + Utils.NEWLINE);
        int columnCount = rs.getColumnCount();
        // table header
        result.append("<TR>" + Utils.NEWLINE);
        for(int i = 0; i < columnCount; i++)
        {
            result.append(TAB + startHeadingTag( i) + rs.getColumnLabel( i) + "</TH>" + Utils.NEWLINE);
        }
        result.append("</TR>" + Utils.NEWLINE);
        // the data
        for(int i = 0; i < rs.getRowCount(); i++)
        {
            Row row = rs.getRow( i);
            result.append("<TR>" + Utils.NEWLINE);
            for(int j = 0; j < columnCount; j++)
            {
                String value = row.getCellValue( j);
                result.append(TAB + startDetailTag( value) + value + "</TD>" + Utils.NEWLINE);
            }
            result.append("</TR>" + Utils.NEWLINE);
        }
        result.append("</TABLE></P>" + Utils.NEWLINE);
        return result;
    }
}
