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

import org.strandz.core.domain.AbstNodeTable;
import org.strandz.core.domain.NodeTableMethods;
import org.strandz.core.domain.NodeTableModelI;
import org.strandz.core.interf.MasterNodeStateChangeListener;
import org.strandz.core.interf.Node;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.BeansUtils;
import org.strandz.lgpl.util.Utils;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.util.Iterator;

public class JTextAreaModelImpl extends NodeTableMethods
    implements DocumentListener
{
    // private PlainDocument documentDelegate = new PlainDocument();
    private AbstNodeTable node;
    private NodeTableModelI nodeTableModel;
    private JTextArea tableView;
    private boolean cellsEditable = true;
    private static boolean debugging = false;
    private static int times = 0;
    private MasterNodeStateChangeListener
        masterNodeStateChangeListener = new MasterNodeStateChangeListener();
    private boolean doneAssignStateChangeListener = false;

    /**
     * From NodeTableMethods
     */
    public void setNode(AbstNodeTable node, NodeTableModelI model)
    {
        this.node = node;
        nodeTableModel = node.getTableModel();
        if(nodeTableModel == null)
        {
            Err.error("No nodeTableModel from Node " + node);
        }
        // realNode.getBlock().setOwnDataManager( true);
    }

    /**
     * See comments in MasterNodeStateChangeListener
     */
    private void assignStateChangeListener()
    {
        if(!doneAssignStateChangeListener)
        {
            Node realNode = (Node) node;
            for(Iterator iter = realNode.getParents().iterator(); iter.hasNext();)
            {
                Node parentNode = (Node) iter.next();
                if(parentNode != null)
                {
                    masterNodeStateChangeListener.setNode(parentNode);
                    parentNode.getStrand().addStateChangeTrigger(
                        masterNodeStateChangeListener);
                    doneAssignStateChangeListener = true;
                }
                else
                {
                    Err.error("Can't get parent Node");
                }
            }
        }
    }

    /**
     * From NodeTableMethods
     */
    public void fireTableDataChanged( String reason)
    {
        this.tableView.getDocument().removeDocumentListener(this);
        assignStateChangeListener();

        // Err.pr( "\tWHEN fireTableDataChanged()");
        // Err.pr( "\tlength " + documentDelegate.getLength());
        // Err.pr( "\ttext " + documentDelegate.getText( 0, documentDelegate.getLength()));
        Document doc = this.tableView.getDocument();
        try
        {
            doc.remove(0, doc.getLength());
        }
        catch(BadLocationException ex)
        {
            Err.error(ex);
        }

        int offset = 0;
        // Err.pr( "fireTableDataChanged to display");
        for(int i = 0; i <= getNodeTableModel().getRowCount() - 1; i++)
        {
            Object obj = getNodeTableModel().getValueAt(i, 0);
            String str = null;
            int len = 0;
            if(obj != null)
            {
                str = obj.toString() + Utils.NEWLINE;
                len = str.length();
            }
            // Err.pr( "To display " + str);
            try
            {
                doc.insertString(offset, str, null);
            }
            catch(BadLocationException ex)
            {
                Err.error(ex);
            }
            offset += len;
        }
        doc.addDocumentListener(this);
    }

    /**
     * From NodeTableMethods
     */
    public void fireRowChangedTo(int row)
    {/*
     But we are calling this!
     if(row != 0)
     {
     Err.error( "JTextArea s/not be connected to the Controller");
     }
     */}

    /**
     * From NodeTableMethods
     */
    public void fireTableRowsDeleted(int firstRow, int lastRow)
    {/*
     But we are calling this!
     Err.error( "fireTableRowsDeleted() s/not be called as not " +
     "connected to a controller");
     */}

    /**
     * From NodeTableMethods
     */
    public void oneRowSelectionOn(Object tableView)
    {
        this.tableView = (JTextArea) tableView;
        this.tableView.getDocument().addDocumentListener(this);
        // SeaweedTextAreaFocusListener l = new SeaweedTextAreaFocusListener();
        // this.tableView.addFocusListener( l);
        this.tableView.setEnabled(true);
    }

    /**
     * From NodeTableMethods
     */
    public void acceptEdit()
    {// nufin
    }

    public void rejectEdit()
    {// nufin
    }

    /**
     * From NodeTableMethods
     */
    public void fireTableRowsInserted(int firstRow, int lastRow)
    {// does get called
    }

    /**
     * From NodeTableMethods
     * Can't focus on this control if it is unenabled. core.info seems to only
     * enable controls (by sending a setEditable( true) command) that have
     * data in them, which is a note. Bug which
     * doesn't affect this call as no reason not to always have it enabled.
     * Note that this is different to JTable problem. JTable will not accept
     * focus if no data in it.
     */
    public void setEditable(boolean b)
    {
        // best to not ever set it false:  tableView.setEnabled( b);
        // Err.pr( "Enabled set to " + b + " for JTextArea");
        times++;
        /*
        if(times == 3 && b == true)
        {
        Err.stack();
        }
        */
        cellsEditable = b;
    }

    /**
     * From NodeTableMethods
     */
    public void setDebug(boolean b)
    {
        Print.pr("setDebug()");
    }

    /**
     * From NodeTableMethods
     */
    // public void setSelection( int row)
    // {
    // Err.pr( "setSelection()");
    // }

    /**
     * From NodeTableMethods
     */
    public boolean isCellTextBlank(Object value)
    {
        if(value == null)
        {
            return true;
        }
        return false;
    }

    /**
     * From DocumentListener
     */
    public void changedUpdate(DocumentEvent e)
    {
        Err.error("PlainDocument so s/not ever be called");
        changeList(e, false);
    }

    /**
     * From DocumentListener
     */
    public void insertUpdate(DocumentEvent e)
    {
        if(debugging)
        {
            Document doc = e.getDocument();
            int changeLength = e.getLength();
            Print.pr("Document is of type " + doc.getClass());
            Print.pr("changeLength is " + changeLength);
        }
        changeList(e, false);
    }

    /**
     * From DocumentListener
     */
    public void removeUpdate(DocumentEvent e)
    {
        changeList(e, true);
    }

    public NodeTableModelI getNodeTableModel()
    {
        if(nodeTableModel == null)
        {
            if(!BeansUtils.isDesignTime())
            {
                Err.error("NodeTableModel is NULL");
            }
        }
        return nodeTableModel;
    }

    /**
     * private/package methods
     */
    AbstNodeTable getNode()
    {
        if(node == null)
        {
            Err.error("Node is NULL");
        }
        return node;
    }

    int getCurrentRow()
    {
        int result = -99;
        try
        {
            result = tableView.getLineOfOffset(tableView.getCaretPosition());
        }
        catch(BadLocationException ex)
        {
            Err.error(ex);
        }
        return result;
    }

    private void changeList(DocumentEvent e, boolean isRemoving)
    {
        NodeTableModelI mod = getNodeTableModel();
        Document doc = e.getDocument();
        int len = doc.getLength();
        String str = null;
        try
        {
            str = doc.getText(0, len);
        }
        catch(BadLocationException ex)
        {
            Err.error(ex);
        }

        /*
        * Need to go thru all the chars of str one by one so can
        * seperate out into lines, which can set on list.
        */
        char asChars[] = str.toCharArray();
        int begin = 0;
        int index = 0;
        for(int i = 0; i <= asChars.length - 1; i++)
        {
            if(asChars[i] == '\n' || (i == asChars.length - 1 && asChars[i] != '\n'))
            {
                int to = i;
                if(asChars[i] != '\n')
                {
                    to++;
                }

                String line = str.substring(begin, to);
                if(index + 1 > mod.getRowCount())
                {
                    if(index + 1 > mod.getRowCount() + 1)
                    {
                        Err.error("Assumed never more than 1 greater than");
                    }
                    mod.insertLine(line, ((Node) node).getCell().getClazz().getClassObject());
                }
                else
                {
                    // Err.pr( "Changing line " + index);
                    mod.setValueAt(line, index, 0);
                }
                begin = i + 1;
                index++;
            }
        }
        if(isRemoving)
        {
            for(int i = mod.getRowCount() - index; i > 0; i--)
            {
                int ind = index + i - 1;
                /*
                * Was nice to have AbstNodeTable, but alot easier to
                * just use Node!
                */
                mod.removeLine(ind);
                // Err.pr( "Have removed line at " + ind);
                // Err.pr( "node " + node.getName());
                /*
                int j=0;
                for(Iterator it = ext.iterator(); it.hasNext(); j++)
                {
                GUIClass obj = (GUIClass)it.next();
                Err.pr( j + ", value is " + obj.getName());
                }
                */
            }
        }
    }

    public void repositionTo(Object itemAdapter)
    {
        Err.error("[6]Not implemented yet");
    }

    /*
    public void setMoveManager( Object moveManager)
    {
    Err.error( "Not implemented yet");
    }
    */
    public String getTableName()
    {
        return tableView.getName();
    }
}
