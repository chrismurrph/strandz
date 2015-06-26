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
package org.strandz.core.info.impl.swing;

import javax.swing.JTextArea;

public class TextAreaFocusListener
// implements FocusListener
{
    private JTextArea taView;
    private JTextAreaModelImpl outer;

    // XMLEncoder
    public TextAreaFocusListener()
    {
        super();
    }

    void setTableView(JTextArea taView)
    {
        this.taView = taView;
    }

    void setOuter(JTextAreaModelImpl outer)
    {
        this.outer = outer;
    }
    /*
    public void focusGained( FocusEvent evt)
    {
    //Err.pr( "caret position from focusGained: " +
    //  taView.getCaretPosition());
    Err.pr( "focusGained");
    //outer.valueChanged( outer.getCurrentRow());
    }

    public void focusLost( FocusEvent evt)
    {
    //Err.pr( "Inside focusLost");
    }
    */

    /*
    public class SomeClass
    {
    //XMLEncoder
    public SomeClass()
    {
    super();
    }

    public void mousePressed(MouseEvent event)
    {
    //Err.pr( "Inside mousePressed");
    Point p = event.getPoint();
    int row = taView.locationToIndex( p);
    outer.valueChanged( row);
    *
    * Found must put super.mousePressed() last so that
    * when setRow (which causes post()) on a different
    * row the first item in the combobox is not put into
    * the model. See TableComboBug.java to experiment
    * with this.
    *
    *
    * allowUserEvent as called here comes after the row
    * validation has been done, and checks out the results
    * of this validation. Not doing super.mousePressed() is
    * the only way to stop the row change taking place.
    *
    if(outer.getNodeTableModel().allowUserEvent( row))
    {
    //Err.pr( "Doing super.mousePressed now");
    super.mousePressed( event);
    }
    }
    } //end inner class SomeClass
    */
} // end inner class SeaweedTableFocusListener
