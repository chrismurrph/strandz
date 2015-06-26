/*
    Strandz LGPL - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz LGPL is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA


    The authors can be contacted via www.strandz.org
*/
package org.strandz.lgpl.widgets;

import org.strandz.lgpl.util.KeyUtils;
import org.strandz.lgpl.util.Err;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.awt.Color;

public class UnusedControls
{
    private static Border LABEL_BORDER = BorderFactory.createLineBorder(Color.white);

    abstract private static class MyLabel extends JLabel
    {
        int row;

        MyLabel( int row)
        {
            setBorder(LABEL_BORDER);
            setHorizontalAlignment(SwingConstants.CENTER);
            this.row = row;
//            //experiments
//            setOpaque( true);
//            setForeground( Color.yellow);
        }
    }

    private static class MyBrightLabel extends MyLabel
    {
        MyBrightLabel( int row)
        {
            super( row);
        }
    }

    private static class MyGrayLabel extends MyLabel
    {
        MyGrayLabel( int row)
        {
            super( row);
            setOpaque( true);
            setBackground( Color.gray);
        }

        public void setText( String txt)
        {
            super.setText( txt);
//            if(txt.equals( "0"))
//            {
//                Err.error( "How being set? - by an Integer");
//            }
//            if(txt.equals( "0.00"))
//            {
//                Err.error( "How being set? - by a BigDecimal");
//            }
//            if(txt.equals( "1.00"))
//            {
//                Err.error( "How being set?");
//            }
        }
    }

    private static class MyCalcLabel extends JLabel
    {
        int row;

        MyCalcLabel( boolean isBad)
        {
            this();
            if(isBad)
            {
                setOpaque( true);
                setBackground( Color.RED);
            }
        }

        MyCalcLabel()
        {
            setBorder(LABEL_BORDER);
            setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    private static class JComponent extends MyNormalTextField
    {
        JComponent( boolean isBad)
        {
            super( isBad);
        }
    }

    private static class MyNormalTextField extends JTextField
    {
        Action tabKeyAction;

        MyNormalTextField( boolean isBad)
        {
            this();
            if(isBad)
            {
                setOpaque( true);
                setBackground( Color.RED);
            }
        }

        MyNormalTextField()
        {
            setHorizontalAlignment(SwingConstants.CENTER);
            //configureEnterListener( this);
            KeyUtils.addEnterKey( this);
        }

        public void setText( String s)
        {
            Err.error( "Don't use");
        }

        public void setItemValue( Object obj)
        {
            super.setText( obj.toString());
        }
    }
}
