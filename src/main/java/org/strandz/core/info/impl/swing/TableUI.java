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

import org.strandz.core.domain.AbstractTableItemAdapter;
import org.strandz.core.domain.constants.EntrySiteEnum;
import org.strandz.core.prod.Session;
import org.strandz.lgpl.util.Err;

import javax.swing.JTable;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicTableUI;
import java.awt.Point;
import java.awt.event.MouseEvent;

public class TableUI extends BasicTableUI
{
    private JTable tableView;
    private AbstTableModelImpl tableModelImpl;
    private static int times;

    // XMLEncoder
    public TableUI()
    {
        super();
    }

    void setTableView(JTable tableView)
    {
        this.tableView = tableView;
        // tableView.addKeyListener( new LocalKeyListener());
        // attachKeys();
    }

    /**
     * NOT USING
     * Use these for where want to replace the behaviour. This less
     * complex than relying on alreadyDoneVisually.
     * PROBLEM - JTable then doesn't work so well anymore. For example
     * the current column comes out as -1.
     * <p/>
     * For applichousing things be careful when use
     * an InputMap/ActionMap pair, as will replace the current
     * behaviour.
     */
    private void attachKeys()
    {
        Err.error("Not using");

//        KeyStroke aKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
//        tableView.getInputMap().put(aKeyStroke, "Up");
//        tableView.getActionMap().put("Up", new KeyUpHandler());
//        aKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
//        tableView.getInputMap().put(aKeyStroke, "Down");
//        tableView.getActionMap().put("Down", new KeyDownHandler());
    }

    /* Start Not using */
//    class KeyUpHandler extends AbstractAction
//    {
//        public void actionPerformed(ActionEvent ae)
//        {
//            tableModelImpl.keyReleased(KeyEvent.VK_UP);
//        }
//    }
//
//
//    class KeyDownHandler extends AbstractAction
//    {
//        public void actionPerformed(ActionEvent ae)
//        {
//            tableModelImpl.keyReleased(KeyEvent.VK_DOWN);
//        }
//    }
    /* End Not using */

    void setTableModelImpl(AbstTableModelImpl outer)
    {
        this.tableModelImpl = outer;
    }

    // XMLEncoder

    // protected KeyListener createKeyListener()
    // {
    // return new SeaweedKeyHandler();
    // }
    protected MouseInputListener createMouseInputListener()
    {
        return new MyMouseHandler();
    }

    public class MyMouseHandler extends BasicTableUI.MouseInputHandler
    {
        // XMLEncoder
        public MyMouseHandler()
        {
            super();
        }

        public void mousePressed(MouseEvent event)
        {
            tableModelImpl.acceptEdit();

            Point p = event.getPoint();
            int col = tableView.columnAtPoint(p);
            /*
            if(col == -1)
            {
            col = 0; //Happns when say first tab into a table.
            }
            */
            /*
            * Not sure for mouse either
            *
            if(tableModelImpl.getAdapter( col).isInError())
            {
            return;
            }
            */
            /*
            times++;
            Err.pr( "==================Inside mousePressed times " + times);
            if(times == 3)
            {
            Err.debug();
            }
            */
            int row = tableView.rowAtPoint(p);
            // Err.pr( "From point " + p + " have got row " + row);
            AbstractTableItemAdapter itemAdapter = tableModelImpl.formAdapterReleased(
                    row, col, tableModelImpl.getNode().getCalculationPlace());
            if(tableModelImpl.isSpotInError(row, col))
            {
                Session.getMoveManager().enterWithoutValidation(itemAdapter,
                    EntrySiteEnum.TABLE_MOUSE_RELEASED);
                super.mousePressed(event);
                Session.getMoveManager().exitEnter();
                return;
            }
            // Err.pr( "==================Inside mousePressed to enter (affirm)");
            Session.getMoveManager().enter(itemAdapter,
                EntrySiteEnum.TABLE_MOUSE_RELEASED);
            if(Session.getMoveManager().readyNextStep())
            {
                // Need earlier
                // tableModelImpl.acceptEdit();
                //
                // Here we set row regardless of which we are on - did s/thing different
                // with keys which could repeat here for performance if wanted (why not use
                // allowUserEvent from below?).
                tableModelImpl.mousePressedOn(row);
                /*
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
                */
                 /**/
                if(tableModelImpl.getNodeTableModel().getRecordValidationOutcome())
                {
                    //Err.pr( "Doing super.mousePressed now");
                    super.mousePressed(event);
                }
                else
                {// Err.pr( "super.mousePressed NOT allowed");
                }
            }
            Session.getMoveManager().exitEnter();
        }

        /**
         * Disabling mouse dragging. At one stage I used to think that
         * fast mouse clicks were causing current row to be dragged - I
         * was actually inadvertently dragging.
         */
        public void mouseDragged(MouseEvent event)
        {
        }
    } // end inner class MouseHandler
} // end inner class SeaweedTableUI
