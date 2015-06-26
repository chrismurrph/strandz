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
package org.strandz.lgpl.widgets.table;

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.ComponentUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.DuplicateChecker;
import org.strandz.lgpl.util.IdentifierI;
import org.strandz.lgpl.widgets.CellControlSignatures;
import org.strandz.lgpl.widgets.EditableObjComp;
import org.strandz.lgpl.widgets.CenteredLabel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Similar functionality to a JTable, but lightweight and is quite directly made up of
 * JComponents. You shouldn't need to subclass this as much to do things as with JTable.
 * At this stage only tested in RO mode, and when editing just the top row. 
 * The intention for editing any row is to pop up a dialog for that row.  
 */
public class ComponentTableView extends JPanel implements TableModelListener, ListSelectionListener, Scrollable, IdentifierI
{
    private CTVLayout layout;
    private TableModel model;
    private CTVModelI insertModel;
    private CurrentCell currentCell = new CurrentCell( this);
    private ColumnWidthsInformerI columnWidthsInformer;
    private CellControlSignatures cellControlSignatures;
    private CellComponentCreatorI cellComponentCreator;
    private int editableRow = 0;
    private int currentRow = Utils.UNSET_INT;
    private List advanceEditableRowControls;
    /**
     * As it is a relay then look to what it relays to for 
     * code maintenance
     */
    private ClickRelay clickRelay = new ClickRelay();
    private FocusListener alwaysFocusListener = new AlwaysFocusListener();
    private FocusListener optionalFocusListener;
    private UpDownKeyListener keyListener = new UpDownKeyListener();
    //Could be stored inside FocusListener
    private List cellChangedListeners = new ArrayList();
    private ComponentTableView outer;
    private JComponent firstComponent;
    private ControlFocuser lastControlFocuser;
    //private boolean modelDataChangeOnly = false;
    private boolean alreadyGivenFocus;
    private List<RowChangeListenerI> rowChangeListeners = new ArrayList<RowChangeListenerI>();
    private List<RowChangeListenerI> visualOnlyRowChangeListeners = new ArrayList<RowChangeListenerI>();
    private List<RowCompletedListenerI> rowCompletedListeners = new ArrayList<RowCompletedListenerI>();
    private List<DataRendererI> dataRenderers = new ArrayList<DataRendererI>();
    private List<TouchListenerI> touchListeners = new ArrayList<TouchListenerI>();
    boolean readOnly;
    private boolean editableRowMoves = false;
    private ClickListenerI localClickListener;
    private boolean firstTime = true;
    private ClickListenerI optionalClickListener = NULL_CLICK_LISTENER;
    private boolean internalRowChange = true;
    /**
     * So that are forced to rebuild the table from scratch, and more performant
     * than having DYNAMIC_LAYOUT_CHANGE set to true
     */
    private int lastNumberOfRows;
    private Map<String,Integer> headings;
    private JComponent debugControl;
    private CTVControlsHolder ctvControlsHolder;
    private boolean changeRowOnClick = true;
    private boolean useDebugControl = true;
    private boolean ignoreMouseClicks;
    private boolean focusCausesTouch = true;
    //private boolean ignoreFocusNextTime = false;
    private FocusIgnorerI focusIgnorer;
    private int lastVisualOnlyRowChangeRow = Utils.UNSET_INT;
    /**
     * The row that will be blank, and all rows subsequent to it that
     * come from the model will be drawn from the real row, which is
     * one less than the row as the table displays it. Maybe it is a
     * pity we had to incorporate the idea of a spacer (and we may have
     * more than one spacer later) into the model at all. Maybe a
     * concept of boundaries between rows would have been better.
      */
    private int spacerRow = Utils.UNSET_INT;

    private static int timesModelChanged;
    private static int modelChangedTimes;
    private static int timesSetModel;
    private static int timesConstructed;
    private static int timesObtainWrapper;
    private static int timesToCreate;
    private static int times;
    private static int times1;
    private static int timesTableChanged;
    private static int timesNonStructural;
    private static int timesRO;
    private int id;

    private static final Border WHITE_LINE_BORDER = BorderFactory.createLineBorder( Color.WHITE);
    private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder();
    private static final int ROW_HEIGHT = 19;
    private static final int SPACER_ROW_HEIGHT = 4;
    private static final boolean DEBUG = false;
    private static final boolean DYNAMIC_LAYOUT_CHANGE = false;
    private static final NullClickListener NULL_CLICK_LISTENER = new NullClickListener();
    private static final boolean FOCUS_FIRST = false;
    /**
     * Following not true - it just appeared so - see Intelligence*Provider.getFocusableTable()
     * for how solved in end.
     *
     * Switching this off means that now when click on panel after looking at the
     * R-Click menu - do not get transported right back to the cell that has focus
     */
    private static final boolean ACTIVATED_FOCUS = false;
    private static final boolean REMEMBER_LAST_FOCUSED = false;

    public ComponentTableView()
    {
        timesConstructed++;
        id = timesConstructed;
        outer = this;
//        int keyEvent = KeyEvent.VK_TAB;
//        KeyUtils.debugComponent( this, KeyStroke.getKeyStroke( keyEvent, 0));
        Err.pr( SdzNote.CTV_HOW_MANY.isVisible() || SdzNote.SET_AND_CREATE_BLOCKS.isVisible(), 
            "Constructed CTV, ID: " + id);
        if(SdzNote.CTV_HOW_MANY.isVisible() && id == 0)
        {
            Err.stack();
        }
        setLayout( new BorderLayout());
        add( debugControl = new JButton( "ComponentTableView"));
        ctvControlsHolder = new CTVControlsHolder( this);
    }

    /*
    protected void finalize()
    {
         Err.pr( SdzNote.SET_AND_CREATE_BLOCKS, "CTV <" + getName() + "> ID: " + id + " finalized");
    }
    */

    public void addRowChangeListener( RowChangeListenerI rowChangeListener)
    {
        rowChangeListeners.add( rowChangeListener);
    }

    public List<RowChangeListenerI> getRowChangeListeners()
    {
        return rowChangeListeners;
    }
    
    public boolean removeRowChangeListener( RowChangeListenerI rowChangeListener)
    {
        boolean result = rowChangeListeners.remove( rowChangeListener);
        if(!result)
        {
            if(!rowChangeListeners.isEmpty())
            {
                Print.prList( rowChangeListeners, "rowChangeListeners");
                Err.error( "rowChangeListener " + rowChangeListener + " could not be removed");
            }
        }
        return result;
    }

    public void addVisualOnlyRowChangeListener( RowChangeListenerI visualOnlyRowChangeListener)
    {
        visualOnlyRowChangeListeners.add( visualOnlyRowChangeListener);
    }

    public void removeAllVisualOnlyRowChangeListeners()
    {
        List<RowChangeListenerI> listeners = getVisualOnlyRowChangeListeners();
        for (Iterator<RowChangeListenerI> rowChangeListenerIIterator = listeners.iterator(); rowChangeListenerIIterator.hasNext();)
        {
            RowChangeListenerI rowChangeListenerI = rowChangeListenerIIterator.next();
            removeVisualOnlyRowChangeListener( rowChangeListenerI);
        }
    }

    public List<RowChangeListenerI> getVisualOnlyRowChangeListeners()
    {
        return visualOnlyRowChangeListeners;
    }

    public boolean removeVisualOnlyRowChangeListener( RowChangeListenerI visualOnlyRowChangeListener)
    {
        boolean result = visualOnlyRowChangeListeners.remove( visualOnlyRowChangeListener);
        if(!result)
        {
            if(!visualOnlyRowChangeListeners.isEmpty())
            {
                Print.prList( visualOnlyRowChangeListeners, "visualOnlyRowChangeListeners");
                Err.error( "visualOnlyRowChangeListener " + visualOnlyRowChangeListener + " could not be removed");
            }
        }
        return result;
    }

    public void addRowCompletedListener( RowCompletedListenerI rowCompletedListener)
    {
        rowCompletedListeners.add( rowCompletedListener);
    }

    public List<RowCompletedListenerI> getRowCompletedListeners()
    {
        return rowCompletedListeners;
    }

    public boolean removeRowCompletedListener( RowCompletedListenerI rowCompletedListener)
    {
        boolean result = rowCompletedListeners.remove( rowCompletedListener);
        if(!result)
        {
            if(!rowCompletedListeners.isEmpty())
            {
                Print.prList( rowCompletedListeners, "rowCompletedListeners");
                Err.error( "rowCompletedListener " + rowCompletedListener + " could not be removed");
            }
        }
        return result;
    }
    
    public void addDataRenderer( DataRendererI dataRenderer)
    {
        dataRenderers.add(dataRenderer);
    }
    
    public boolean removeDataRenderer( DataRendererI dataRenderer)
    {
        boolean result = dataRenderers.remove(dataRenderer);
        if(!result)
        {
            if(!dataRenderers.isEmpty())
            {
                Print.prList( dataRenderers, "dataRenderers");
                Err.error( "dataRenderer " + dataRenderer + " could not be removed");
            }
        }
        return result;
    }

    public void addTouchListener( TouchListenerI touchListener)
    {
        touchListeners.add( touchListener);
    }

    public List<TouchListenerI> getTouchListeners()
    {
        return touchListeners;
    }
    
    public boolean removeTouchListener( TouchListenerI touchListener)
    {
        boolean result = touchListeners.remove( touchListener);
        if(!result)
        {
            if(!touchListeners.isEmpty())
            {
                Print.prList( touchListeners, "touchListeners");
                Err.error( "touchListener " + touchListener + " could not be removed");
            }
        }
        return result;
    }
    
    private void fireRowChanged( int from, int to)
    {
        Err.pr( SdzNote.SET_ROW, "In CTV fireRowChanged from " + from + " to " + to + " have " + 
                rowChangeListeners.size());
        internalRowChange = true;
        for(Iterator iterator = rowChangeListeners.iterator(); iterator.hasNext();)
        {
            RowChangeListenerI rowChangeListener = (RowChangeListenerI) iterator.next();
            rowChangeListener.rowChanged( from, to);
        }
        internalRowChange = false;
    }

    private void fireVisualOnlyRowChanged( int from, int to)
    {
        for(Iterator iterator = visualOnlyRowChangeListeners.iterator(); iterator.hasNext();)
        {
            RowChangeListenerI rowChangeListener = (RowChangeListenerI) iterator.next();
            rowChangeListener.rowChanged( from, to);
        }
    }

    private void fireRowCompleted( int row)
    {
        for(Iterator iterator = rowCompletedListeners.iterator(); iterator.hasNext();)
        {
            RowCompletedListenerI rowCompletedListener = (RowCompletedListenerI) iterator.next();
            rowCompletedListener.rowDataFilledNotification( row);
        }
    }

    public boolean isFocusCausesTouch()
    {
        return focusCausesTouch;
    }

    public void setFocusCausesTouch(boolean focusCausesTouch)
    {
        this.focusCausesTouch = focusCausesTouch;
    }

    private void fireTouched( int row)
    {
        if(focusCausesTouch)
        {
            Err.pr( SdzNote.SET_ROW, "In fireTouched() and are going to fire on " + touchListeners.size());
            for(Iterator iterator = touchListeners.iterator(); iterator.hasNext();)
            {
                TouchListenerI touchListener = (TouchListenerI) iterator.next();
                touchListener.touch( row);
            }
        }
    }
    
    private void fireRenderersDataWrite( Object comp, Object text, int row, int column)
    {
        if(row != Utils.UNSET_INT)
        {
            for(Iterator iterator = dataRenderers.iterator(); iterator.hasNext();)
            {
                DataRendererI dataRenderer = (DataRendererI) iterator.next();
                dataRenderer.writeToCell( comp, text, row, column);
                Err.pr( SdzNote.SET_DISPLAY_ON_TABLE, "Used " + dataRenderer.getClass().getName() + " to write to row " + row);
            }
        }
        else
        {
            //Don't render anything on the row to be skipped
        }
    }
    
    public int getSelectedRow()
    {
        int result;
        if(editableRowMoves)
        {
            Assert.isFalse( readOnly);
            //Assert.isTrue( currentRow == Utils.UNSET_INT);
            if(currentRow != Utils.UNSET_INT)
            {
                Err.pr( "Even thou editableRowMoves, current row has somehow been set to " +
                    currentRow + ", meanwhile editableRow is " + editableRow);
            }
            result = editableRow;
        }
        else
        {
            result = currentRow;
        }
        Err.pr( SdzNote.SET_ROW, "getSelectedRow() to return " + result);
        return result;
    }

    public int getSelectedColumn()
    {
        int result = -1;
        //Err.pr( "ComponentTableView.getSelectedColumn() to be where the cursor is or -1");
        return result;
    }
    
    public int getRowCount()
    {
        int result = 0;
        if(model != null)
        {
            result = insertModel.getRowCount();
        }
        return result;
    }

    public int getNonSpacerRowCount()
    {
        int result = 0;
        if(model != null)
        {
            result = model.getRowCount();
        }
        return result;
    }

    public int getColumnCount()
    {
        int result = 0;
        if(model != null)
        {
            result = model.getColumnCount();
        }
        return result;
    }
    
    /**
     * Despite its name this method is called when the table's model has changed
     * @param e
     */
    public void tableChanged( TableModelEvent e)
    {
        if(SdzNote.REFRESH.isVisible() && getName().equals( "Tribe Row Header TableView"))
        {
            timesTableChanged++;
            Err.pr( "tableChanged() on " + this.getName() + " times " + timesTableChanged);
            if(timesTableChanged == 0)
            {
                Err.stack();
            }
        }
        String explanation;
        if(e == null)
        {
            explanation = "No row/col info given";
        }
        else if(e.getFirstRow() == TableModelEvent.HEADER_ROW)
        {
            explanation = "From header row to " + e.getLastRow();
        }
        else
        {
            String op = null;
            if(e.getType() == TableModelEvent.INSERT)
            {
                op = "INSERT";
            }
            else if(e.getType() == TableModelEvent.UPDATE)
            {
                op = "UPDATE";
            }
            else if(e.getType() == TableModelEvent.DELETE)
            {
                op = "DELETE";
            }
            String col = null;
            if(e.getColumn() == TableModelEvent.ALL_COLUMNS)
            {
                col = "ALL_COLUMNS";
            }
            else 
            {
                col = "" + e.getColumn();
            }
            explanation = op + " on " + col + " from " + e.getFirstRow() + " to " + e.getLastRow();
        }
        modelChanged( false, "tableChanged() called with " + explanation, e);
    }

    /*
    public void modelStructureChanged( String reason)
    {
        modelChanged( false, reason);
        //Err.error( "If use improve so that modelDataChangeOnly not changed");
    }

    public void modelDataChanged( String reason)
    {
        modelChanged( true, reason);
        //Err.error( "If use improve so that modelDataChangeOnly not changed");
    }
    */
    
    public void modelChanged( String reason)
    {
        modelChanged( false, reason, null);
    }

    private void modelChanged( boolean modelDataChangeOnly, String reason, TableModelEvent evt)
    {
        if(reason.contains( "blankout"))
        {
            Err.error();
        }
        if(insertModel == null)
        {
            Err.error( "No model set for ComponentTableView: " + getName());
        }
        else
        {
            insertModel.modelChanging( reason, evt);
            int numOfRows = insertModel.getRowCount();
            boolean changedStructure = false;
            if(firstTime /*|| (DYNAMIC_LAYOUT_CHANGE && !modelDataChangeOnly)*/ ||
                lastNumberOfRows != numOfRows
                //|| evt.getType() == TableModelEvent.INSERT
                )
            {
                if(SdzNote.CTV_MODEL_CHANGED.isVisible() || SdzNote.DYNAM_ATTRIBUTES.isVisible())
                {
                    modelChangedTimes++;
                    Err.pr( "Doing modelChanged STRUCTURAL change");
                    Err.pr( "\tfirstTime is " + firstTime + " in <" + getName() + ">, with ID <" + id + ">");
                    Err.pr( "\tlastNumberOfRows: " + lastNumberOfRows);
                    Err.pr( "\tnumberOfRows: " + numOfRows);
                    Err.pr( "\ttimes: " + modelChangedTimes);
                    if(modelChangedTimes == 0)
                    {
                        Err.stack();
                    }
                    if(id == 4 && numOfRows == 1)
                    {

                    }
                }
                removeAll(); //including debugControl if added
                ctvControlsHolder.clearControls();
                Err.pr( SdzNote.WANT_ALL_ON_DETAIL, "Creating brand new tablelayout (removed all) in " + this.getClass().getName());
                layout = CTVLayout.newInstance();
                layoutColumns();
                Err.pr( SdzNote.CTV_MODEL_CHANGED, "Num of DOs to print is " + numOfRows + " in " + this);
                if(numOfRows == 0)
                {
                    Err.pr( SdzNote.CTV_MODEL_CHANGED, "No DOs in " + insertModel);
                }
                else
                {
                    Err.pr( SdzNote.CTV_MODEL_CHANGED, "In " + insertModel + " have " + numOfRows);
                }
                if(numOfRows == 0)
                {
                    //Of course does happen!
                }
                for (int i = 0; i < numOfRows; i++)
                {
                    if(i == spacerRow)
                    {
                        layout.addRow( i, SPACER_ROW_HEIGHT);
                    }
                    else
                    {
                        layout.addRow( i, ROW_HEIGHT);
                    }
                }
                setLayout( layout.getLayoutManager2());
                firstTime = false;
                lastNumberOfRows = numOfRows;
                changedStructure = true;
            }
            else
            {
                if(evt.getFirstRow() == 0 && evt.getLastRow() == Integer.MAX_VALUE)
                {
                    //Err.pr( "Not doing any data refreshing: " + reason);                    
                }
                else
                {
                    Assert.isTrue( evt.getFirstRow() == evt.getLastRow(), reason + " first: " +
                            evt.getFirstRow() + ", last: " + evt.getLastRow());
                    Assert.isTrue( evt.getFirstRow() != Utils.UNSET_INT);
                    timesNonStructural++;
                    Err.pr( SdzNote.CTV_MODEL_CHANGED, "? setText() in here because " + reason + " for " + getName() + " times " + 
                            timesNonStructural + ", where row is " + evt.getFirstRow());
                    if(timesNonStructural == 0)
                    {
                        Err.stack();
                    }
                    int rowAt = evt.getFirstRow();
                    if(rowAt != -1)
                    {
                        boolean editable = false;
                        if(!readOnly)
                        {
                            editable = (rowAt == editableRow);
                        }
                        ComponentControlWrapper[] wrappers = null;
                        wrappers = obtainRowOfWrappers( rowAt, editable);
                        for(int i = 0; i < wrappers.length; i++)
                        {
                            ComponentControlWrapper wrapper = wrappers[i];
                            if(SdzNote.REFRESH.isVisible() && getName().equals( "Tribe Row Header TableView"))
                            {
                                Err.debug();
                            }
                            Object modelValue = insertModel.getValueAt( rowAt, i);
                            setText( wrapper.getComp(), modelValue, rowAt, i);                    
                        }
                    }
                }
            }
            times1++;
            Err.pr( SdzNote.CTV_ADD_CELL, "Num of rows will add to " + getName() + " is " + insertModel.getRowCount());
            Err.pr( SdzNote.CTV_ADD_CELL, "changedStructure " + changedStructure);
            Err.pr( SdzNote.CTV_ADD_CELL, "modelDataChangeOnly " + modelDataChangeOnly);
            if(times1 == 0)
            {
                Err.stack();
            }
            for(int i = 0; i < numOfRows; i++)
            {
                boolean editable = false;
                if(!readOnly)
                {
                    editable = (i == editableRow);
                    if(editable)
                    {
                        Err.pr( SdzNote.CTV_CRUD, "To do editable row, which is " + editableRow);
                    }
                    else
                    {
                        Err.pr( SdzNote.CTV_CRUD, "Doing uneditable row " + i);
                    }
                }
                Err.pr( SdzNote.CTV_ADD_CELL, "Editable is marked as " + editable + " for row " + i);
                final JComponent defaultFocusable = rowOfObjects(i, model.getColumnCount(), editable,
                    modelDataChangeOnly,
                    "Model changed because " + reason);
                if (i == editableRow)
                {
                    if(defaultFocusable != null)
                    {
                        firstComponent = defaultFocusable;
                        Err.pr(SdzNote.CTV_MODEL_CHANGED,
                            "During modelChanged() first component <" + firstComponent.getName() +
                            "> on " + this.getName());
                        currentCell.setOn( false);
                        giveFocusToFirstComponentWhenActivate();
                    }
                }
            }
            if(evt.getType() == TableModelEvent.INSERT)
            {
                if(editableRowMoves)
                {
                    RowChange rowChange = movedToRow( evt.getFirstRow(), true, false);
                    editableRow = rowChange.to;
                    doRowMove( rowChange);
                }
                else
                {
                    Err.pr( SdzNote.CTV_ONLY_EDIT_ROWCHANGE, "Ignoring INSERT because have not yet coded for a row to change other than the editable row");
                }
            }
            revalidate();
            timesModelChanged++;
            Err.pr( SdzNote.CTV_MODEL_CHANGED, "Done modelChanged() for " + getName() + " with " + 
                    //getNonEditableControls().getList().size() +
                    ctvControlsHolder.getListOfNonEditableRowWrappers().size() +
                    " non-editable controls, times " + timesModelChanged);
            if(timesModelChanged == 0)
            {
                Err.debug();
            }
            if(firstComponent != null)
            {
                Err.pr( SdzNote.CTV_MODEL_CHANGED, "Done modelChanged and first component is " + firstComponent.getName());
            }
            alreadyGivenFocus = false;
            insertModel.modelChanged();
        }
    }

    /**
     * For list selection changes - ie. on this component will be for receiving which row
     * is now selected - will call setRowSelectionInterval
     * @param e
     */
    public void valueChanged( ListSelectionEvent e)
    {
        Err.stack( "ComponentTableView.valueChanged() called with " + e);
    }

    public int getColumnIdxOfHeading( String identifier)
    {
        int result;
        if(headings == null)
        {
            //Err.error( "setHeadings() not been called on ComponentTableView named " + getName() + ", so can't find index for heading <" + identifier + ">");
            formHeadingsFromTableModel();            
        }
        result = headings.get( identifier);
        return result;
    }
    
    private void formHeadingsFromTableModel()
    {
        Map<String,Integer> modelHeadings = new HashMap<String,Integer>();
        int colCount = model.getColumnCount();
        for(int i = 0; i < colCount; i++)
        {
            String colName = model.getColumnName( i);
            modelHeadings.put( colName, i);
        }
        setHeadings( modelHeadings);
    }
    
    public void setHeadings( Map<String,Integer> headings)
    {
        this.headings = headings;    
    }

    private class AlwaysClickListener implements ClickListenerI
    {
        public void outerDoubleClicked(int row, int column, MouseEvent e)
        {
            if(!ignoreMouseClicks)
            {
                fireTouched( row);                            
                optionalClickListener.outerDoubleClicked( row, column, e);
            }
        }

        public void outerSingleClicked(int row, int column, MouseEvent e)
        {
            if(!ignoreMouseClicks)
            {
                //Err.pr( "outerSingleClicked at row " + row + " in table " + ComponentTableView.this.getName());
                fireTouched( row);                            
                RowChange rowChange = new RowChange();
                rowChange.to = row;
                rowChange.from = currentRow;
                fireRowChanged( rowChange);
                optionalClickListener.outerSingleClicked( row, column, e);
            }
        }
    
        public void innerDoubleClicked(int row, int column, MouseEvent e)
        {
            if(!ignoreMouseClicks)
            {
                fireTouched( row);                            
                optionalClickListener.innerDoubleClicked(row, column, e);
            }
        }

        public void innerSingleClicked(int row, int column, MouseEvent e)
        {
            if(!ignoreMouseClicks)
            {
                if(editableRowMoves)
                {
                    moveToANewRow( row, column, e);
                }
                else
                {
                    Err.pr( SdzNote.CTV_ONLY_EDIT_ROWCHANGE, getName() + " (1) ignoring rowChange to " + row + " because have not yet coded for a row to change other than the editable row");
                    visualOnlyRowChange( row);
                }
            }
        }
    }

    public void visualOnlyRowChange( int row)
    {
        fireVisualOnlyRowChanged( lastVisualOnlyRowChangeRow, row);
        if(lastVisualOnlyRowChangeRow != row)
        {
            if(lastVisualOnlyRowChangeRow != Utils.UNSET_INT)
            {
                /*
                 * Undo the background colouring did to lastVisualOnlyRowChangeRow
                 */
                //Err.pr( getName() + ", to paint out: " + lastVisualOnlyRowChangeRow);
                backgroundRow( lastVisualOnlyRowChangeRow, null);
            }
            /*
             * Background colour the row
             */
            //Err.pr( getName() + ", to paint in: " + row);
            backgroundRow( row, Color.GREEN);
            lastVisualOnlyRowChangeRow = row;
        }
        else
        {
            /*
             * Have pressed existing coloured, so we toggle (ie unpaint)
             */
            //Err.pr( "To paint in: " + row);
            backgroundRow( lastVisualOnlyRowChangeRow, null);
            lastVisualOnlyRowChangeRow = Utils.UNSET_INT;
        }
    }

    /**
     * If colour param is null then we are un-backgrounding
     */
    private void backgroundRow( int row, Color colour)
    {
        List<JComponent> controls = getRowOfControls( row);
        for (Iterator<JComponent> jComponentIterator = controls.iterator(); jComponentIterator.hasNext();)
        {
            JComponent control = jComponentIterator.next();
            ComponentControlWrapper wrapper = ctvControlsHolder.obtainWrapperFromControl( control);
            if(colour != null)
            {
                wrapper.savePreviousBackground( control.getBackground());
                control.setBackground( colour);
            }
            else
            {
                Color previousBackground = wrapper.getPreviousBackground();
                control.setBackground( previousBackground);
                //Err.pr( "Painted out " + wrapper + " with prev colour " + previousBackground);
            }
            control.repaint();
        }
    }

    /**
     * TODO
     * Much of this variable checking has become superfluous
     */
    private void moveToANewRow( int row, int column, MouseEvent e)
    {
        fireTouched( row);
        RowChange rowChange;
        boolean needMoveEditableRow = editableRowMoves && row != editableRow;
        if(!readOnly && needMoveEditableRow)
        {
            //lastFocusedControl is used for picking up the column to focus
            //on when change to a different row. Without this code the
            //cursor would end up directly above/below the last focused -
            //which looks silly when you have just used the mouse to say
            //which column you want to be on.
            if(column != Utils.UNSET_INT)
            {
                currentCell.setLastEditableFocusedControl( ctvControlsHolder.obtainWrapper(
                    editableRow, column, true, true, editableRow).getComp(), "Single mouse click", row, column);
                Err.pr( SdzNote.CTV_FOCUS, "mouse clicked 'hack', lastFocusedControl now <" +
                    currentCell.getLastEditableFocusedControl().getName() + "> using col " + column + ", row " + editableRow);
            }
            rowChange = movedToRow( row, false, true);
        }
        else
        {
            rowChange = new RowChange();
            rowChange.to = row;
            rowChange.from = currentRow;
        }
        if(editableRowMoves)
        {
            fireRowChanged( rowChange);
        }
        else
        {
            Err.pr( SdzNote.CTV_ONLY_EDIT_ROWCHANGE, "2 Ignoring rowChange to " + row + " because have not yet coded for a row to change other than the editable row");
            visualOnlyRowChange( row);
        }
        if(e != null)
        {
            optionalClickListener.innerSingleClicked( row, column, e);
        }
        else
        {
            Assert.isTrue( column == Utils.UNSET_INT);
            //Comes from a key listener
        }
        if(editableRowMoves)
        {
            editableRow = rowChange.to;
        }
        /* Scrapped because doRowMove() is already called back by fireRowChanged
        if(!readOnly && needMoveEditableRow)
        {
             *
             * Listeners that are called, for example from fireRowChanged, are entitled to
             * assume that editableRow will not be changed until right at the end. For example
             * fireRowChanged listeners may want to get the controls of the currently being edited
             * row
             *
            editableRow = rowChange.to;
            doRowMove( rowChange);
        }
        */
    }

    private class UpDownKeyListener extends KeyAdapter
    {
        public void keyPressed(KeyEvent ke)
        {
            Err.pr( SdzNote.CTV_TURTLE, "Pressed " + ke + " from " + ke.getSource());
            if(ke.getKeyCode() == KeyEvent.VK_DOWN || ke.getKeyCode() == KeyEvent.VK_UP)
            {
                int newRow = editableRow;
                if (ke.getKeyCode() == KeyEvent.VK_DOWN)
                {
                    newRow++;
                }
                else if (ke.getKeyCode() == KeyEvent.VK_UP)
                {
                    newRow--;
                }
                /*
                 * Currently using key presses changes the editable row, hence 2nd param
                 * can be true
                 */
                if(!(newRow >= insertModel.getRowCount() || newRow < 0))
                {
                    moveToANewRow( newRow, Utils.UNSET_INT, null);
                    //RowChange rowChange = movedToRow( newRow, true, true);
                    //editableRow = rowChange.to;
                    //doRowMove( rowChange);
                }
            }
            else
            {
                Err.pr( SdzNote.CTV_TURTLE, "Only need to listen to VK_UP and VK_DOWN, ignoring <" + ke + ">");
            }
        }
    }

    private static class NullClickListener implements ClickListenerI
    {
        public void outerDoubleClicked(int row, int column, MouseEvent e)
        {
            //nufin
        }

        public void outerSingleClicked(int row, int column, MouseEvent e)
        {
            //nufin
        }
    
        public void innerDoubleClicked(int row, int column, MouseEvent e)
        {
            //nufin
        }

        public void innerSingleClicked(int row, int column, MouseEvent e)
        {
            //nufin
        }
    }
    
    private class DefaultCellControlSignatures implements CellControlSignatures
    {
        public Object getText(Object comp)
        {
            return ComponentUtils.getText( comp);
        }

        public void setEditable(Object comp, boolean b)
        {
            ComponentUtils.setEditable( comp, b);
        }

        public boolean isEditable(Object comp)
        {
            return ComponentUtils.isEditable( comp);
        }

        public void setText(Object comp, Object txt)
        {
            ComponentUtils.setText( comp, txt);
        }

        public boolean hasEditableMethod(Object comp)
        {
            return ComponentUtils.hasEditableMethod( comp);
        }
    }
    
    public Dimension getMaximumSize()
    {
        Dimension result = super.getMaximumSize();
        result.height = 78;
        int listSize = insertModel.getRowCount();
        int rowHeight = ROW_HEIGHT; //Must be the default size of a control we create!
        result.height += (listSize*rowHeight);
        if(listSize > 0)
        {
            result.height += 1;
        }
        result.height = adjustHeightForSpacerRow( result.height);
        return result;
    }

    public Dimension getPreferredSize()
    {
        Dimension result = super.getPreferredSize();
        result.height = 0;
        int listSize = 0;
        if(model != null)
        {
            listSize = insertModel.getRowCount();
        }
        int rowHeight = ROW_HEIGHT; //Must be the default size of a control we create
        //Err.pr( "listSize using for height is " + listSize);
        result.height += (listSize*rowHeight);
        if(listSize > 0)
        {
            result.height += 1;
        }
        else
        {
            //The header colour not going to the bottom possibly an insets thing
            //result.height -= 1;
        }
        //If is under the preferred we will have a horizontal scrollbar - doesn't help
        //result.width = 900;
        result.height = adjustHeightForSpacerRow( result.height);
        return result;
    }

    private int adjustHeightForSpacerRow( int height)
    {
        int result = height;
        if(spacerRow != Utils.UNSET_INT)
        {
            result -= ROW_HEIGHT;
            result += SPACER_ROW_HEIGHT;
        }
        return result;
    }
    
    /*
    public Dimension getMinimumSize()
    {
        Dimension result = super.getMinimumSize();
        //If is under the preferred we will have a horizontal scrollbar
        result.width = 900;
        return result;
    }
    */

    public void setModel(TableModel model)
    {
        if(this.model != null && model != null)
        {
            //Model will be set each time execute a query (consumeNodesIntoRT)
            //Err.error( getName() + " already has a model");
        }
        timesSetModel++;
        if(timesSetModel == 1)
        {
            Err.pr( SdzNote.CTV_SDZ_INTEGRATION, "ComponentTableView.setModel() called with " + model);
            //Err.stack();
        }
        this.model = model;
        model.addTableModelListener( this);
        insertModel =
            new SpacerAndBufferAwareModel(
            new BufferAwareModel( model), spacerRow
        )
        ;
    }

    public TableModel getModel()
    {
        return model;
    }

    public void setColumnWidthsInformer(ColumnWidthsInformerI columnWidthsInformer)
    {
        if(this.columnWidthsInformer != null && columnWidthsInformer != null)
        {
            Err.error( getName() + " already has a columnWidthsInformer");
        }
        this.columnWidthsInformer = columnWidthsInformer;
    }
    
    public ColumnWidthsInformerI getColumnWidthsInformer()
    {
        return columnWidthsInformer;
    }

    public void addCellChangedListener( CurrentCellListenerI l)
    {
        if(cellChangedListeners.contains( l))
        {
            Err.error( "Already added, can't add again");
        }
        else if(Utils.containsSameType( l, cellChangedListeners))
        {
            Print.prList( cellChangedListeners, "Current cellChangedListeners");
            Err.error( "We already have a " + l.getClass());
        }
        cellChangedListeners.add( l);
        //Err.pr( "cellChangedListeners in <" + getName() + ", ID: " + getId() + "> size now " + cellChangedListeners.size());
    }

    public void removeCellChangedListener( CurrentCellListenerI l)
    {
        cellChangedListeners.remove( l);
    }
    
    public void debugEditableControls()
    {
        List controls = ctvControlsHolder.getListOfEditableRowControls();
        if(controls == null || controls.isEmpty())
        {
            Err.pr( "No editableControls");
        }
        else
        {
            JComponent leading = (JComponent)controls.get(1);
            Utils.chkTypesOneOnly( Utils.asArrayList( leading.getMouseListeners()), ClickRelay.class, "ClickRelay in mouseListeners");
            Utils.chkTypesOneOnly( Utils.asArrayList( leading.getKeyListeners()), UpDownKeyListener.class, "UpDownKeyListener in keyListeners");
            Utils.chkTypesOneOnly( Utils.asArrayList( leading.getFocusListeners()), AlwaysFocusListener.class, "FocusRelay in focusListeners");
        }
    }
    
    private static class DebugMouseListener extends MouseAdapter
    {
        DebugMouseListener()
        {
        }

        public void mousePressed(MouseEvent e)
        {
            if(e.getClickCount() == 2)
            {
                JComponent control = (JComponent)e.getSource();
                Err.pr( "You have clicked on a " + control.getClass().getName());
//                if(control instanceof MyMonthlyLabel)
//                {
//                    MyMonthlyLabel label = (MyMonthlyLabel)control;
//                    Err.pr( "Control text is " + label.getText());
//                }
            }
        }
    }

    private static class ControlFocuser extends WindowAdapter
    {
        private ComponentTableView outer;

        private ControlFocuser(ComponentTableView outer)
        {
            this.outer = outer;
        }

        public void windowActivated(WindowEvent e)
        {
            outer.giveFocusToSelectedComponent( false);
        }

        public String toString()
        {
            String result = "ControlFocuser for CTV with ID: " + outer.getId() + ", name: <" + outer.getName() + ">";
            return result;
        }
    }

    /**
     * Notes on this trick:
     * http://java.sun.com/docs/books/tutorial/uiswing/misc/focus.html#api
     *
     * This code happens every time a model change comes thru to the table.
     * It causes the first control to get the focus when the window is activated - whenever
     * the user clicks on the window.
     * TODO - same thing needs to be done as a result of user tabbing
     */
    private void giveFocusToFirstComponentWhenActivate()
    {
        if(FOCUS_FIRST)
        {
            JFrame frame = MessageDlg.getFrame( true);
            if(frame != null)
            {
                if(lastControlFocuser != null)
                {
                    frame.removeWindowListener( lastControlFocuser);
                }
                WindowListener listeners[] = frame.getWindowListeners();
                //Print.prArray( listeners, "On others as have removed self");
                for (int i = 0; i < listeners.length; i++)
                {
                    WindowListener windowListener = listeners[i];
                    if(windowListener instanceof ControlFocuser)
                    {
                        ControlFocuser controlFocuser = (ControlFocuser)windowListener;
                        if(controlFocuser.outer.getName().equals( getName()))
                        {
                            frame.removeWindowListener( controlFocuser);
                        }
                    }
                }
                ControlFocuser controlFocuser = new ControlFocuser( this);
                frame.addWindowListener( controlFocuser);
                //Err.pr( "Put a ControlFocuser on <" + this.getName() + ">");
                lastControlFocuser = controlFocuser;
            }
            else
            {
                Err.pr( "No frame so no focus on first component");
            }
        }
    }

    public void giveFocusToSelectedComponent( boolean force)
    {
        if(ACTIVATED_FOCUS)
        {
            if((force || !alreadyGivenFocus) && firstComponent != null)
            {
                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        if(currentCell.isOn())
                        {
                            currentCell.getLastEditableFocusedControl().requestFocusInWindow();
                            Err.pr( SdzNote.CTV_FOCUS, "lastFocusedControl now focused: <" +
                                ComponentUtils.getName( currentCell.getLastEditableFocusedControl()) + ">");
                        }
                        else
                        {
                            firstComponent.requestFocusInWindow();
                        }
                    }
                });
                alreadyGivenFocus = true;
            }
            else
            {
                if(firstComponent == null)
                {
                    Err.pr( SdzNote.CTV_FOCUS, "Wanted to focus on first component but didn't have one");
                }
                if(alreadyGivenFocus)
                {
                    Err.pr( SdzNote.CTV_FOCUS, "Wanted to focus on first component but alreadyGivenFocus in " + this.getName());
                }
            }
        }
    }

    /**
     * Fetching everything from the model and creating all the rows at
     * that time is not the way to go, but will do for now.
     *
     * @return All the data from the model as a list of lists
     */
    /*
    private List formCurrentModelList()
    {
        List result = new ArrayList();
        List columnValue = new ArrayList();
        for(int i = 0; i < model.getRowCount(); i++)
        {
            for(int j = 0; j < model.getColumnCount(); j++)
            {
                Object o =  model.getValueAt( i, j);
                columnValue.add( o);
            }
            result.add( columnValue);
        }
        return result;
    }
    */

    private class DefaultColumnWidthsInformer implements ColumnWidthsInformerI
    {
        public double getColumnWidthAt(int column, int numOfColumns)
        {
            double result = Utils.floatDivide( 1, numOfColumns);
            return result;
        }
    }

    /**
     * Rather than overriding this method, call setColumnWidthsInformer() as part of setting up the table.
     */
    protected void createDefaultColumnWidthsInformer()
    {
        Err.pr( SdzNote.CTV_SDZ_INTEGRATION, "columnWidthsInformer == null so DefaultTableColumnWidthsInformer used");
        columnWidthsInformer = new DefaultColumnWidthsInformer();
    }
    
    /**
     * Rather than overriding this method, call setCellControlSignatures() as part of setting up the table.
     */
    protected void createDefaultCellControlSignatures()
    {
        cellControlSignatures = new DefaultCellControlSignatures();
    }
    
    private ColumnWidthsInformerI getUsedColumnWidthsInformer()
    {
        if(columnWidthsInformer == null)
        {
            createDefaultColumnWidthsInformer();
        }
        return columnWidthsInformer;
    }
    
    private CellControlSignatures getUsedCellControlSignatures()
    {
        if(cellControlSignatures == null)
        {
            createDefaultCellControlSignatures();
        }
        return cellControlSignatures;
    }

    private void layoutColumns()
    {
        int columnCount = model.getColumnCount();
        for (int i = 0; i < columnCount; i++)
        {
            double colWidth = getUsedColumnWidthsInformer().getColumnWidthAt( i, columnCount);
            layout.addColumn( i, colWidth);
        }
        setBorder(EMPTY_BORDER);
    }
    
    /**
     * Save what is on the screen to the model
     */
    public void commitEditableRow()
    {
        ctvControlsHolder.commitEditableRow( model, getUsedCellControlSignatures());
    }

    /**
     * More the function of a controller - should be done from outside the view -
     * only thing is that we will need to know the right trigger point - what
     * would be the listener that a JTable would use?
     *
     * After user has altered in a text field we want the calculated field
     * to be recalculated. We do it for all. We commit the row to the model
     * and then display the row from the model.
     */
    private void recalculateAll()
    {

    }

    /**
     * After user has altered in a text field we want the calculated field
     * to be recalculated. We do it for all. We commit the row to the model
     * and then display the row from the model.
     */
    /*
    private void recalculateAll()
    {
        commitEditableRow();
        DO do = model.getEditingDO();
        if(do != null)
        {
            int i=0;
            for (Iterator iterator = editableRowControls.iterator(); iterator.hasNext(); i++)
            {
                ComponentControlWrapper controlWrapper = (ComponentControlWrapper)iterator.next();
                JComponent editingComp = controlWrapper.getComp();
                if(model.isCalculated( i))
                {
                    ((JLabel)editingComp).setText(model.getColumnValue(i, do).toString());
                }
            }
        }
    }
    */

    public String toString()
    {
        return "ID: " + getId() + ", " + super.toString();
    }

    public void addClickListener( ClickListenerI clickListener)
    {
        if(clickListener == null)
        {
            Err.error( "clickListener == null");
        }
        clickRelay.addClickListener( clickListener);
    }

    public void removeAllClickListeners()
    {
        clickRelay.removeClickRelayAsListenerFrom( ctvControlsHolder.getListOfEditableRowWrappers());
        clickRelay.removeClickRelayAsListenerFrom( ctvControlsHolder.getListOfNonEditableRowWrappers());
        clickRelay.removeAllClickListeners();
    }

    private ComponentControlWrapper[] obtainRowOfWrappers( int row, boolean editable)
    {
        ComponentControlWrapper[] result;
//        if(editable)
//        {
//            Err.error( "There is only one row for editables so no need to use this method");
//        }
        int numOfCols = model.getColumnCount();
        result = new ComponentControlWrapper[numOfCols];
        for(int i = 0; i < numOfCols; i++)
        {
            result[i] = ctvControlsHolder.obtainWrapper( row, i, editable, true, editableRow);
        }
        return result;
    }
    
    public Object getControlAt( int column)
    {
        return getControlAt( editableRow, column);
    }
    
    public int getRow( Object control)
    {
        int result;
        Err.pr( SdzNote.MANY_NON_VISUAL_ATTRIBS, "Trying to obtain wrapper from source <" + ((JComponent)control).getName() + ">");
        ComponentControlWrapper wrapper = ctvControlsHolder.obtainWrapper( control, true);
        result = wrapper.getRow();
        return result;
    }

    public ColRow getColRow( Object control)
    {
        ColRow result;
        ComponentControlWrapper wrapper = ctvControlsHolder.obtainWrapper( control, true);
        result = wrapper.getColRow();
        return result;
    }

    public List getColumnOfControls( int column)
    {
        List result = new ArrayList<JComponent>();
        int rowCount = getRowCount();
        for(int i = 0; i < rowCount; i++)
        {
            result.add( getControlAt( i, column));
        }
        return result;
    }

    public List getRowOfControls( int row)
    {
        List result = new ArrayList<JComponent>();
        int colCount = getColumnCount();
        for(int i = 0; i < colCount; i++)
        {
            result.add( getControlAt( row, i));
        }
        return result;
    }

    public Object getControlAt( int row, int column)
    {
        Object result = null;
        boolean editable = false;
        if(editableRow == row)
        {
            editable = true;
        }
        //Err.pr( "Getting control at " + row + " and is editable: " + editable);
        timesObtainWrapper++;
        Err.pr( SdzNote.ENABLEDNESS_REFACTORING, "To obtainWrapper at row " + row + ", col " + column + " times " + timesObtainWrapper);
        if(timesObtainWrapper == 0)
        {
            Err.stack();
        }
        ComponentControlWrapper wrapper = ctvControlsHolder.obtainWrapper( row, column, editable, false, editableRow);
        if(wrapper != null)
        {
            result = wrapper.getComp();
        }
        else
        {
            if(!editable)
            {
                /* Don't have time to look into all this dummy stuff
                if(editableRow + 1 == row)
                {
                     **
                     * Happens when INSERT_AFTER
                     *
                    editableRow++;
                    DummyInAdvanceControlWrapper dummyInAdvanceControlWrapper = obtainDummyInAdvanceControlWrapper( row, column, true);
                    result = dummyInAdvanceControlWrapper.getComp();
                }
                else
                */
                {
                    //To invoke the stack trace - so not assigning result
                    Err.pr( "Intending to cause stack trace, editableRow: " + editableRow + ", row: " + row);
                    ctvControlsHolder.obtainWrapper( row, column, editable, true, editableRow);
                }
            }
            else
            {
                //Err.pr( "Have not found an editable at row " + row + " so resorting to obtainDummyInAdvanceControlWrapper()...");
                DummyInAdvanceControlWrapper dummyInAdvanceControlWrapper = obtainDummyInAdvanceControlWrapper( row, column, editable);
                result = dummyInAdvanceControlWrapper.getComp();
            }
        }
        if(result instanceof EditableObjComp)
        {
            EditableObjComp objComp = (EditableObjComp)result;
            Err.pr( SdzNote.CTV_ADVANCED_COMP, "EditableObjComp ID: " + objComp.id);
        }
        return result;
    }
    
    private DummyInAdvanceControlWrapper obtainDummyInAdvanceControlWrapper(int row, int col, boolean editable)
    {
        DummyInAdvanceControlWrapper result = null;
        if(advanceEditableRowControls == null)
        {
            advanceEditableRowControls = new ArrayList();
        }
        if(!editable)
        {
            Err.error( "Cannot obtain an advance component at editable row " + row + ", col " + col);
        }
        for(Iterator iterator = advanceEditableRowControls.iterator(); iterator.hasNext();)
        {
            DummyInAdvanceControlWrapper dummyInAdvanceControlWrapper = (DummyInAdvanceControlWrapper) iterator.next();
            if(col == dummyInAdvanceControlWrapper.getCol())
            {
                result = dummyInAdvanceControlWrapper;
                break;
            }
        }
        if(result == null)
        {
            result = createDummyInAdvance( row, col, editable, false);
            advanceEditableRowControls.add( result);
        }
        return result;
    }
        
    private static class RowChange
    {
        boolean doRowMove;
        int from;
        int to;
        boolean fireRowChange;
        boolean transferProperties;
    }

    /**
     * 
     * @param row
     * @param fireRowChange When true call will not be due to a non-editable row change
     * @return
     */
    private RowChange movedToRow( int row, boolean fireRowChange, boolean transferProperties)
    {
        RowChange result = null;
        Assert.isTrue( editableRowMoves, "Should not be calling movedToRow if editableRow does not Move");
        if(editableRowMoves)
        {
            result = new RowChange();
            if(row >= insertModel.getRowCount())
            {
                Err.error( SdzNote.CTV_TURTLE, "No row as large as " + row);
            }
            else if(row < 0)
            {
                Err.error( SdzNote.CTV_TURTLE, "No row as small as " + row);
            }
            else if(row == editableRow)
            {
                Err.pr( SdzNote.CTV_TURTLE, "Staying at " + row);
            }
            else
            {
                result.doRowMove = true;
                result.from = editableRow;
                result.to = row;
                result.fireRowChange = fireRowChange;
                result.transferProperties = transferProperties;
            }
        }
        if(readOnly)
        {
            Assert.notNull( currentCell.getLastEditableFocusedControl(), "lastEditableFocusedControl s/be null for a RO table");
        }
        return result;
    }

    private void doRowMove( RowChange rowChange)
    {
        if(rowChange.doRowMove)
        {
            if(insertModel.isPendingInsert())
            {
                Err.pr( "pendingInsert still around");
                insertModel.modelChanged();
            }
            Err.pr( SdzNote.CTV_TURTLE, "Moving to row " + rowChange.to + " from " + rowChange.from);
            int numOfCols = model.getColumnCount();
            //editable row that going to (previously non-editable)
            ComponentControlWrapper[] newEditables = createRowOfComponentsAsMovedRow( numOfCols, rowChange.to, true,
                                                                            "Moved to " + rowChange.to + " so creating editable " +
                                                                                    "comps for it");
            Object[] oldEditableRow = ctvControlsHolder.getListOfEditableRowWrappers().toArray();
            if(rowChange.transferProperties)
            {
                transferProperties( oldEditableRow, newEditables);
            }
            ComponentControlWrapper[] oldLabels = obtainRowOfWrappers( rowChange.to, false);
            clearEditableRowControls();
            for(int i = 0; i < numOfCols; i++)
            {
                ComponentControlWrapper newEditable = newEditables[i];
                addToEditableRowControls( newEditable);
            }
            firstComponent = newEditables[0].getComp();
            //non-editable row that coming from (previously editable)
            Err.pr( "Editable row moving from " + rowChange.from);
            Err.pr( "Current row " + currentRow);
            ComponentControlWrapper[] newLabels = createRowOfComponentsAsMovedRow(
                numOfCols, rowChange.from, false,
                "Moving away from previously editable row " +
                    "need to fill with non-editable comps");
            for(int i = 0; i < numOfCols; i++)
            {
                ComponentControlWrapper oldLabel = oldLabels[i];
                removeFromNonEditableRowControls( oldLabel);
                addToNonEditableRowControls( newLabels[i]);
            }
            //now that state has been fixed lets do visually
            for(int i = 0; i < numOfCols; i++)
            {
                remove( ((ComponentControlWrapper)oldEditableRow[i]).getComp());
                remove( oldLabels[i].getComp());
                String quoted = CTVUtils.formBodyQuotedStr(newLabels[i].getCol(), newLabels[i].getRow());
                add(newLabels[i].getComp(), quoted);
                quoted = CTVUtils.formBodyQuotedStr(newEditables[i].getCol(), newEditables[i].getRow());
                add(newEditables[i].getComp(), quoted);
            }
            revalidate();
            //finally the cursor/focus needs to have moved too
            //ComponentControlWrapper wrapper = ctvControlsHolder.obtainWrapperFromControl(
            //        lastEditableFocusedControl, Arrays.asList( oldEditableRow));
            //JComponent lastEditableFocusedControl = currentCell.getLastEditableFocusedControl();
            ComponentControlWrapper wrapper = new ColRowListSet( (List)Arrays.asList( oldEditableRow)).
                    findWrapperByPosition( currentCell.getColRow().col);
            if(wrapper == null)
            {
                Print.prArray( oldEditableRow, "Was looking in");
                Err.error( "Could not obtain a controlWrapper using control <" +
                    currentCell.getControlName() + ">, text <" +
                    currentCell.getControlText() + ">, class: <" +
                    currentCell.getControlClassName() +
                        ">, trying to move to " + rowChange.to + " from " + rowChange.from);
            }
            int lastFocusedControlCol = wrapper.getCol();
            currentCell.setLastEditableFocusedControl(
                newEditables[lastFocusedControlCol].getComp(), "User moved to another row");
            Err.pr( SdzNote.CTV_TURTLE, "lastFocusedControl is now <" + currentCell.getControlName() + ">");
            currentCell.getLastEditableFocusedControl().requestFocusInWindow();
            //
            if(rowChange.fireRowChange)
            {
                fireRowChanged( rowChange);
            }
        }
    }

    /**
     * Called for row changes whether editable or not 
     * @param rowChange
     */
    private void fireRowChanged( RowChange rowChange)
    {
        if(currentRow != rowChange.to)
        {
            currentRow = rowChange.to;
            if(changeRowOnClick)
            {
                fireRowChanged( rowChange.from, rowChange.to);
            }
        }
    }

    private void transferProperties(Object[] oldEditableRow, ComponentControlWrapper[] newEditables)
    {
        if(oldEditableRow.length != newEditables.length)
        {
            Err.error("ComponentTableView.transferProperties(): oldEditableRow.length != newEditables.length");
        }
        for(int i = 0; i < oldEditableRow.length; i++)
        {
            ComponentControlWrapper oldWrapper = (ComponentControlWrapper)oldEditableRow[i];
            if(!getUsedReadOnly( oldWrapper.getComp()))
            {
                ComponentControlWrapper newWrapper = newEditables[i];
                getUsedCellControlSignatures().setEditable( newWrapper.getComp(), getUsedCellControlSignatures().isEditable( oldWrapper.getComp()));
            }
            else
            {
                //Transfering editable properties that are always 'no' to a non RO-control makes no sense.
                //However for other properties it might...
            }
        }
    }

    private ComponentControlWrapper[] createRowOfComponentsAsMovedRow( int numOfCols, int row, boolean editableRow, String reason)
    {
        ComponentControlWrapper[] result = new ComponentControlWrapper[numOfCols];
        for (int col = 0; col < numOfCols; col++)
        {
            result[col] = createComponent( row, col, editableRow, true, reason);
        }
        return result;
    }
    
    private DummyInAdvanceControlWrapper createDummyInAdvance(int row, int col, boolean editable, boolean fillData)
    {
        DummyInAdvanceControlWrapper result;
        EditableObjComp comp = new EditableObjComp();
        comp.setName( "editableObjComp at col " + col + ", row " + row);
        comp.setEditable( editable);
        if(fillData)
        {
            if(insertModel == null)
            {
                Err.error( "ComponentTableView has no model to get data from");
            }
            setText( comp, insertModel.getValueAt(row, col), row, col);
        }
        result = new DummyInAdvanceControlWrapper( row, col, comp);
        return result;
    }

    private ComponentControlWrapper createEditableRowComponentFromDummyInAdvance(
            DummyInAdvanceControlWrapper dummyInAdvanceWrapper, boolean fillData, String reason)
    {
        ComponentControlWrapper result;
        //Here was mixing up editability with whether was on an editable cell 
        //boolean sourceEditable = getUsedCellControlSignatures().isEditable( dummyInAdvanceWrapper.getComp());
        JComponent comp = getUsedCellComponentCreator().createCell( dummyInAdvanceWrapper.getRow(), 
                                                                dummyInAdvanceWrapper.getCol(), 
                                                                true, reason, dummyInAdvanceWrapper.getRow() == spacerRow);
        if(fillData)
        {
            if(model == null)
            {
                Err.error( "ComponentTableView has no model to get data from");
            }
            Object modelValue = insertModel.getValueAt(dummyInAdvanceWrapper.getRow(), dummyInAdvanceWrapper.getCol());
            Err.pr( SdzNote.CTV_ADVANCED_COMP, "Directly from model when from dummy in advance: <" + modelValue + ">"); 
            setText( comp, modelValue, dummyInAdvanceWrapper.getRow(), dummyInAdvanceWrapper.getCol());
            Err.pr( SdzNote.CTV_ADVANCED_COMP, "Got from comp from model: <" + getUsedCellControlSignatures().getText( comp) + 
                    "> for row " + dummyInAdvanceWrapper.getRow() + ", col " + dummyInAdvanceWrapper.getCol());
        }
        else
        {
            Err.pr( SdzNote.CTV_ADVANCED_COMP, "Source value loosing: " + getUsedCellControlSignatures().getText( dummyInAdvanceWrapper.getComp()));
        }
        if(!getUsedReadOnly( comp))
        {
            getUsedCellControlSignatures().setEditable( comp, getUsedCellControlSignatures().isEditable( dummyInAdvanceWrapper.getComp()));
        }
        installFocusListenersOn( comp);
        listenToKeyOnComp( comp);
        result = new ComponentControlWrapper( dummyInAdvanceWrapper.getRow(), dummyInAdvanceWrapper.getCol(), comp);
        clickRelay.addClickRelayAsListenerTo( result);
        return result;
    }

    private void listenToKeyOnComp( JComponent comp)
    {
        comp.addKeyListener( keyListener);
    }

    private ComponentControlWrapper createComponent(
            int row, int col, boolean editableRow, boolean fillData, String reason)
    {
        ComponentControlWrapper result;
        JComponent comp = getUsedCellComponentCreator().createCell(
            row, col, editableRow, reason, row == spacerRow);
        if(fillData)
        {
            if(model == null)
            {
                Err.error( "ComponentTableView <" + getName() + "> has no model to get data from");
            }
            /*
            if(
                    getName().equals( "Tribe Row Header TableView") 
                    //|| getName().equals( "Person Row Header TableView")
                    )
            {
                Err.debug();
            }
            */
            Object modelValue = insertModel.getValueAt(row, col);
            Err.pr( SdzNote.CTV_ADVANCED_COMP, 
                    "Directly from model when just createComponent: <" + modelValue + "> in <" + getName() + ">"); 
            if(modelValue instanceof String && modelValue.equals( "Head Office"))
            {
                times++;
                if(times == 0)
                {
                    Err.stack();
                }
            }
            setText( comp, modelValue, row, col);
        }
        installFocusListenersOn( comp);
        if(editableRow)
        {
            listenToKeyOnComp( comp);
        }
        result = new ComponentControlWrapper( row, col, comp);
        clickRelay.addClickRelayAsListenerTo( result);
        return result;
    }
    
    private DuplicateChecker duplicateChecker = new DuplicateChecker();

    private void installFocusListenersOn( JComponent comp)
    {
        duplicateChecker.add( comp);
        comp.addFocusListener( alwaysFocusListener);
        if(optionalFocusListener != null)
        {
            comp.addFocusListener( optionalFocusListener);
        }
        //Err.pr( "Focus listeners on " + comp);
    }
    
    private void setText( Object comp, Object txt, int row, int col)
    {
        if (
            SdzNote.DYNAM_ATTRIBUTES.isVisible() &&
            getName().equals("Person Row Header TableView") && col == 0
            //|| getName().equals( "Person TableView")
            )
        {
            Err.pr("setText to <" + txt + "> at col " + col + ", row " + row + " on comp " +
                ((CenteredLabel) comp).getName() + /*comp.getClass().getName() +*/ " for table <" + getName() + ">");
            if (txt == null)
            {
                //Err.debug();
            }
        }
        fireRenderersDataWrite( comp, txt, /*row*/ spacerRowAdjustment( row), col);
        getUsedCellControlSignatures().setText( comp, txt);
    }

    private int spacerRowAdjustment( int row)
    {
        int result = row;
        if(spacerRow != Utils.UNSET_INT)
        {
            result = insertModel.adjustRowFromTableForModel( row);
        }
        return result;
    }

    public int spacerRowChange( int row)
    {
        int result = row;
        if(spacerRow != Utils.UNSET_INT && insertModel != null)
        {
            result = insertModel.adjustRowFromModelForTable( row);
        }
        return result;
    }

    /**
     * Class that listens to the clicks of all controls. Also holds the relay
     * targets (a list of ClickListenerI) that are informed about the click.
     */
    private class ClickRelay extends MouseAdapter
    {
        private List clickListeners = new ArrayList();
        private boolean firstTime = true;

        ClickRelay()
        {
        }

        public void mousePressed(MouseEvent e)
        {
            if(firstTime)
            {
                firstTime = false;
                addClickListener( getAlwaysUsedClickListener());
            }
            if(e.getClickCount() == 1 || e.getClickCount() == 2)
            {
                Err.pr( SdzNote.CTV_CLICK, "ClickRelay EVT: " + e);
                Err.pr( SdzNote.CTV_CLICK, "ClickRelay ID: " + id);
                JComponent comp = (JComponent)e.getSource();
                ComponentControlWrapper wrapper = ctvControlsHolder.obtainWrapperFromControl( comp);
                Err.pr( SdzNote.CTV_CLICK, "ClickRelay source: " + wrapper);
                int clickCount = e.getClickCount();
                int button = e.getButton();
                for(Iterator iterator = clickListeners.iterator(); iterator.hasNext();)
                {
                    ClickListenerI clickListener = (ClickListenerI) iterator.next();
                    if(button == MouseEvent.BUTTON1)
                    {
                        if(clickCount == 2)
                        {
                            clickListener.innerDoubleClicked( wrapper.getRow(), wrapper.getCol(), e);
                        }
                        else if(clickCount == 1)
                        {
                            clickListener.innerSingleClicked( wrapper.getRow(), wrapper.getCol(), e);
                        }
                    }
                    else if(button == MouseEvent.BUTTON3)
                    {
                        if(clickCount == 2)
                        {
                            clickListener.outerDoubleClicked( wrapper.getRow(), wrapper.getCol(), e);
                        }
                        else if(clickCount == 1)
                        {
                            clickListener.outerSingleClicked( wrapper.getRow(), wrapper.getCol(), e);
                        }
                    }
                    else if(button == MouseEvent.BUTTON2)
                    {
                        //Who uses the middle button?
                    }
                    else
                    {
                        Err.error( "Unknown button " + button);
                    }
                }
            }
        }

        private void addClickListener( ClickListenerI clickListener)
        {
            this.clickListeners.add( clickListener);
        }

        private void addClickListeners( List clickListeners)
        {
            this.clickListeners.addAll( clickListeners);
        }

        private void removeClickListener( ClickListenerI clickListener)
        {
            boolean ok = clickListeners.remove( clickListener);
            if(!ok)
            {
                Err.error( "Did not remove clickListener: " + clickListener);
            }
        }

        private void removeClickListeners( List clickListeners)
        {
            for(Iterator iterator = clickListeners.iterator(); iterator.hasNext();)
            {
                ClickListenerI clickListener = (ClickListenerI) iterator.next();
                removeClickListener( clickListener);
            }
        }

        private void removeAllClickListeners()
        {
            clickListeners.clear();
        }

        /**
         * this ClickRelay becomes the listener for all the controls that are
         * passed in here
         *
         * When going up or down to another row this will need to be called
         * @param controlWrappers
         */
        private void addClickRelayAsListenerTo( List controlWrappers)
        {
            for (Iterator iterator = controlWrappers.iterator(); iterator.hasNext();)
            {
                ComponentControlWrapper controlWrapper = (ComponentControlWrapper) iterator.next();
                addClickRelayAsListenerTo( controlWrapper);
            }
        }

        /**
         * One ClickRelay listens to all the controls 
         */
        void addClickRelayAsListenerTo( ComponentControlWrapper controlWrapper)
        {
            JComponent comp = controlWrapper.getComp();
            ClickRelay existingClickRelayOfComp = getClickRelay( comp);
            if(existingClickRelayOfComp != null)
            {
                Err.error( "Control: <" + comp + "> already has a ClickRelay, so no point in adding another");
            }
            comp.addMouseListener( this);
        }

        private void removeClickRelayAsListenerFrom( List<ComponentControlWrapper> controlWrappers)
        {
            for (Iterator iterator = controlWrappers.iterator(); iterator.hasNext();)
            {
                ComponentControlWrapper controlWrapper = (ComponentControlWrapper) iterator.next();
                removeClickRelayAsListenerFrom( controlWrapper);
            }
        }

        private void removeClickRelayAsListenerFrom( ComponentControlWrapper controlWrapper)
        {
            JComponent comp = controlWrapper.getComp();
            ClickRelay clickRelay = getClickRelay( comp);
            if(clickRelay != null)
            {
                comp.removeMouseListener( clickRelay);
            }
            else
            {
                Err.error( "There was no ClickRelay on < " + comp + ">");
            }
        }

        private ClickRelay getClickRelay( JComponent comp)
        {
            ClickRelay result = null;
            MouseListener[] listeners = comp.getMouseListeners();
            int timesFound = 0;
            for(int i = 0; i < listeners.length; i++)
            {
                MouseListener mouseListener = listeners[i];
                if(mouseListener instanceof ClickRelay)
                {
                    result = (ClickRelay)mouseListener;
                    timesFound++;
                }
            }
            if(timesFound > 1)
            {
                Err.error( "Control found with more than one (" + timesFound + ") ClickRelay: <" + comp + ">");
            }
            return result;
        }
    }    
        
    private JComponent rowOfObjects( int row, int numOfCols, boolean editableRow, 
                                     boolean modelDataChangeOnly, String reason)
    {
        JComponent result = null;
        Err.pr( SdzNote.CTV_ADD_CELL, "numOfCols " + numOfCols);
        for (int col = 0; col < numOfCols; col++)
        {
            timesToCreate++;
            if(timesToCreate == 0)
            {
                Err.debug();
            }
            ComponentControlWrapper obtainedWrapper = ctvControlsHolder.obtainWrapper(
                    row, col, editableRow, false, this.editableRow);
            Err.pr( SdzNote.CTV_ADD_CELL, "Obtained wrapper is " + obtainedWrapper + " for " + row + ", " + col + ", " + editableRow + " times " + timesToCreate);
            JComponent comp;
            if(!modelDataChangeOnly)
            {
                if(obtainedWrapper == null)
                {
                    ComponentControlWrapper controlWrapper = null;
                    if(editableRow)
                    {
                        //If the advance row exists then we use these components
                        DummyInAdvanceControlWrapper editableControlWrapper = obtainDummyInAdvanceControlWrapper(row, col, editableRow);
                        controlWrapper = createEditableRowComponentFromDummyInAdvance(editableControlWrapper, true, reason);
                    }
                    if(controlWrapper == null)
                    {
                        controlWrapper = createComponent(row, col, editableRow, true ,reason);
                    }
                    comp = controlWrapper.getComp();
                    if(editableRow)
                    {
                        addToEditableRowControls( controlWrapper);
                    }
                    else
                    {
                        addToNonEditableRowControls( controlWrapper);
                    }
                }
                else //lazy load as this being called even when data is changed
                {
                    comp = obtainedWrapper.getComp();
                }
            }
            else
            {
                if(obtainedWrapper == null)
                {
                    Err.error( "Cannot go for a modelDataChangeOnly when <" +
                            getName() + ", ID: " + id + "> has not even had its structure set up");
                }
                comp = obtainedWrapper.getComp();
                //refresh( comp);
            }
            if(col == 0)
            {
                result = comp;
            }
            if(!modelDataChangeOnly)
            {
                if(obtainedWrapper == null || DYNAMIC_LAYOUT_CHANGE)
                {
                    String quoted = CTVUtils.formBodyQuotedStr(col, row);
                    Err.pr( SdzNote.CTV_ADD_CELL, "Adding a row cell at " + quoted);
                    add(comp, quoted);
                }
            }
        }
        if(editableRow)
        {
            currentCell.newSetOfControls();
        }
        return result;
    }
    
    void addToEditableRowControls( ComponentControlWrapper wrapper)
    {
        ctvControlsHolder.addToEditableRowControls( wrapper);
        Err.pr( SdzNote.CTV_CRUD, "Added " + wrapper.getComp() + " to editableRowControls");
    }

    void clearEditableRowControls()
    {
        ctvControlsHolder.clearEditableRowControls();
        Err.pr( SdzNote.CTV_CRUD, "Cleared editableRowControls");
    }

    void addToNonEditableRowControls( ComponentControlWrapper wrapper)
    {
        ctvControlsHolder.addToNonEditableRowControls( wrapper);
        Err.pr( SdzNote.CTV_CRUD, "Added " + wrapper.getComp() + " to nonEditableControls");
    }

    void removeFromNonEditableRowControls( ComponentControlWrapper wrapper)
    {
        ctvControlsHolder.removeFromNonEditableRowControls( wrapper);
        Err.pr( SdzNote.CTV_CRUD, "Removed " + wrapper.getComp() + " from nonEditableControls");
    }

    void clearNonEditableRowControls()
    {
        ctvControlsHolder.clearNonEditableRowControls();
        Err.pr( "Cleared editableRowControls");
    }

    /**
     * No controls are added via this call
     */
    public List getListOfEditableRowControls()
    {
        return ctvControlsHolder.getListOfEditableRowControls();
    }

    /**
     * No controls are added via this call
     */
    public List getListOfNonEditableRowControls()
    {
        return ctvControlsHolder.getListOfNonEditableRowControls();
    }

    /*
    public ColRowListSet getNonEditableControls()
    {
        return ctvControlsHolder.getNonEditableControls();
    }
    public ColRowListSet getEditableControls()
    {
        return ctvControlsHolder.getEditableRowControls();
    }
    */

    public void setRowSelectionInterval( int index0,
                                         int index1)
    {
        if(!readOnly)
        {
            if(index0 != index1)
            {
                Err.error( "Only one row can be selected at a time, so both index params must have the same value");
            }
            if(index0 < 0)
            {
                Err.error( "Index param cannot be negative, is " + index0);
            }
            if(editableRowMoves)
            {
                RowChange rowChange = movedToRow( index0, true, true);
                editableRow = rowChange.to;
                doRowMove( rowChange);
            }
            else
            {
                Err.pr( SdzNote.CTV_ONLY_EDIT_ROWCHANGE, "3 Ignoring rowChange to " + index0 + " because have not yet coded for a row to change other than the editable row");
                visualOnlyRowChange( index0);
            }
        }
        else
        {
            Err.pr( SdzNote.INDEX, "CTV is RO, so has no concept of a selected row, so setting the row interval to " + index0 + " ignored");
        }
    }

    public boolean getReadOnly()
    {
        return readOnly;
    }

    public void setReadOnly( boolean readOnly)
    {
        this.readOnly = readOnly;
        /*
        timesRO++;
        Err.pr( "RO now " + this.readOnly + " on " + getName() + " times " + timesRO);
        if(timesRO == 0)
        {
            Err.stack();
        }
        */
    }
    
    private boolean getUsedReadOnly( Object comp)
    {
        //readOnly just serves to give a performance advantage
        boolean result = readOnly || !getUsedCellControlSignatures().hasEditableMethod( comp);
//        if(comp instanceof NumberTextField)
//        {
//            Err.pr( "Used as RO: " + result + ", " + comp);
//        }
        return result;
    }
    
    private class DefaultCellComponentCreator implements CellComponentCreatorI
    {
        public JComponent createCell(int row, int col, boolean editableRow, String reason, boolean spacerRow)
        {
            /* Assert is wrong, as cellComponentCreator can be of type DefaultCellComponentCreator 
            Assert.isNull( cellComponentCreator, "Should not be using DefaultCellComponentCreator in <" + 
                    getName() + "> as already have a " + cellComponentCreator.getClass().getName()); 
            */
            JComponent result;
            if(editableRow)
            {
                result = new JTextField();
            }
            else
            {
                result = new JLabel();
            }
            Err.pr( SdzNote.CTV_ADD_CELL, "In " + this.getClass().getName() + " have created a " + 
                    result.getClass().getName() + " because " + reason);
            result.setName( "ComponentTableView textfield at col " + col + ", row " + row);
            if(Utils.isBlank( result.getName()))
            {
                Err.error( "Somehow a name did not get assigned to a " + result.getClass().getName());
            }
            return result;
        }
    }
    
    public void setName( String txt)
    {
        super.setName( txt);
        if(debugControl instanceof JButton) //useDebugControl set to F
        {
            ((JButton)debugControl).setText( txt + " ID: " + id);
        }
        Err.pr( SdzNote.CTV_HOW_MANY.isVisible() || SdzNote.DYNAM_ATTRIBUTES.isVisible(),
            "CTV, have setName() of ID: " + id + " to " + getName());
    }

    public void setCellComponentCreator(CellComponentCreatorI cellComponentCreator)
    {
        /*
        if(this.cellComponentCreator != null && cellComponentCreator != null)
        {
            Err.error( getName() + " already has a cellComponentCreator");
        }
        */
        this.cellComponentCreator = cellComponentCreator;
    }
    
    public CellComponentCreatorI getCellComponentCreator()
    {
        return cellComponentCreator;
    }

    public CellControlSignatures getCellControlSignatures()
    {
        return cellControlSignatures;
    }
    
    public void setCellControlSignatures(CellControlSignatures cellControlSignatures)
    {
        /*
        if(this.cellControlSignatures != null && cellControlSignatures != null)
        {
            Err.error( getName() + " already has a cellControlSignatures");
        }
        */
        this.cellControlSignatures = cellControlSignatures;
    }
    
    private CellComponentCreatorI getUsedCellComponentCreator()
    {
        if(cellComponentCreator == null)
        {
            createDefaultCellComponentCreator();
        }
        return cellComponentCreator;
    }
    
    /**
     * Rather than overriding this method, call setCellComponentCreator() as part of setting up the table.
     */
    protected void createDefaultCellComponentCreator()
    {
        Assert.isNull( cellComponentCreator); 
        cellComponentCreator = new DefaultCellComponentCreator();
        Err.pr( SdzNote.CTV_SDZ_INTEGRATION, "cellComponentCreator == null so DefaultCellComponentCreator used");
    }

    /*
    public void ignoreFocusNextTime()
    {
        //Assert.isFalse( ignoreFocusNextTime);
        ignoreFocusNextTime = true;
        Err.pr( SdzNote.CTV_IGNORE_FOCUS, "Will ignoreFocusNextTime in " + this);
    }
    */

    public void setFocusIgnorer( FocusIgnorerI focusIgnorer)
    {
        this.focusIgnorer = focusIgnorer;
    }
    
    private class AlwaysFocusListener implements FocusListener
    {
        public void focusGained(FocusEvent e)
        {
            /*
            if(e.toString().contains( "cause=UNKNOWN") ||
               e.toString().contains( "FOCUS_GAINED,permanent,opposite=null,cause=ACTIVATION"))
            {
                 *
                 * When we bring up a dialog we don't want the scrollbar to go right
                 * back to the beginning - we want to keep what we scrolled across to
                 * in view.
                 *
                Err.pr( "skipped FocusEvent: " + e);
            }
            else
            */
            if(focusIgnorer == null || !focusIgnorer.isIgnoreFocus())
            {
                Err.pr( SdzNote.CTV_IGNORE_FOCUS, "Normal focusGained in " + ComponentTableView.this);
                Err.pr( SdzNote.CTV_FOCUS, "Doing FocusEvent: " + e);
                fireTouched( Utils.UNSET_INT);
                JComponent component = (JComponent)e.getSource();
                if(component.getName() == null)
                {
                    Err.error( "component.getName() == null");
                }
                SdzNote.CTV_FOCUS.incTimes();
                Err.pr( SdzNote.CTV_FOCUS, "focusGained(), now focused on <" + component.getName() +
                    "> num of listeners is " + cellChangedListeners.size());
                if(SdzNote.CTV_FOCUS.getTimes() == 2)
                {
                    Err.stack();
                }
                for (Iterator iterator = cellChangedListeners.iterator(); iterator.hasNext();)
                {
                    CurrentCellListenerI currentCellListener = (CurrentCellListenerI) iterator.next();
                    Rectangle rect = SwingUtilities.getLocalBounds( component);
                    Rectangle destRect = SwingUtilities.convertRectangle( component, rect, outer);
                    currentCellListener.cellChanged( destRect, component, e);
                }
                ComponentControlWrapper wrapper = ctvControlsHolder.obtainWrapperFromControl( component);
                if(wrapper != null)
                {
                    if(REMEMBER_LAST_FOCUSED)
                    {
                        currentCell.setLastEditableFocusedControl( component, "Focus gained",
                            wrapper.getRow(), wrapper.getCol());
                        Err.pr( SdzNote.CTV_FOCUS, "focusGained(), lastFocusedControl now <" +
                            currentCell.getLastEditableFocusedControl().getName() + ">");
                    }
                }
            }
            else
            {
                Err.pr( SdzNote.CTV_IGNORE_FOCUS, "Ignoring focusGained one time");
                focusIgnorer.popIgnoreFocus();
            }
        }

        public void focusLost(FocusEvent e)
        {
            JComponent textField = (JComponent)e.getSource();
            Err.pr( SdzNote.CTV_FOCUS, "Lost focus where value is " + getUsedCellControlSignatures().getText( textField));
            recalculateAll();
        }
    }
    
    public void setFocusListener(FocusListener focusListener)
    {
        /*
        if(this.localFocusListener != null && focusListener != null)
        {
            Err.error( getName() + " already has a focusListener: " + this.localFocusListener.getClass().getName());
        }
        */
        this.optionalFocusListener = focusListener;
    }
    
    public FocusListener getFocusListener()
    {
        return optionalFocusListener;
    }
    
    /**
     * Never need to set to null unless are trying to un-set from a
     * previous call
     * @param clickListener
     */
    public void setClickListener( ClickListenerI clickListener)
    {
        if(clickListener == null)
        {
            //Err.pr( "Expect no click listener for " + this.getName());
            Assert.notNull( optionalClickListener);
            Assert.isFalse( this.optionalClickListener == NULL_CLICK_LISTENER, 
                            "No point in calling setClickListener() with null - only use like this when want to " +
                                    "un-set from a previous call");
            this.optionalClickListener = NULL_CLICK_LISTENER;
        }
        else
        {
            this.optionalClickListener = clickListener;
        }
    }
    
    public ClickListenerI getClickListener()
    {
        return optionalClickListener;
    }
    
    private ClickListenerI getAlwaysUsedClickListener()
    {
        if(localClickListener == null)
        {
            createAlwaysClickListener();
        }
        return localClickListener;
    }
    
    protected void createAlwaysClickListener()
    {
        Assert.isNull(localClickListener); 
        localClickListener = new AlwaysClickListener();
        //Err.pr( "AlwaysClickListener been created for " + this.getName());        
    }
        
    public int getScrollableBlockIncrement( Rectangle visibleRect,
                                            int orientation,
                                            int direction)
    {
        return 10;
    }

    public boolean getScrollableTracksViewportWidth()
    {
        return false;
    }

    public boolean getScrollableTracksViewportHeight()
    {
        return false;
    }

    public Dimension getPreferredScrollableViewportSize()
    {
        return null;
    }

    public int getScrollableUnitIncrement( Rectangle visibleRect,
                                           int orientation,
                                           int direction)
    {
        return 10;
    }
    
    public boolean isEditableControlAt( int column)
    {
        if(readOnly)
        {
            return false;
        }
        else
        {
            return isEditableControlAt( currentRow, column);
        }
    }
    
    public boolean isEditableControlAt( int row, int column)
    {
        boolean editable = isEditable( editableRow);
        return editable;
    }
    
    private boolean isEditable( int row)
    {
        boolean result = false;
        if(!readOnly)
        {
            result = (row == editableRow);
        }
        return result;
    }

    public boolean isChangeRowOnClick()
    {
        return changeRowOnClick;
    }

    public void setChangeRowOnClick(boolean changeRowOnClick)
    {
        this.changeRowOnClick = changeRowOnClick;
    }
    
    public JComponent getDebugControl()
    {
        return debugControl;
    }

    public void setDebugControl(JComponent debugControl)
    {
        this.debugControl = debugControl;
    }

    public boolean isUseDebugControl()
    {
        return useDebugControl;
    }

    public void setUseDebugControl(boolean useDebugControl)
    {
        if(!useDebugControl)
        {
            remove( debugControl);
            debugControl = null;
        }
        this.useDebugControl = useDebugControl;
    }

    public boolean isIgnoreMouseClicks()
    {
        return ignoreMouseClicks;
    }

    /**
     * If set to true when CTV is part of a Sdz application then clicks
     * will not go to the framework. However you should still expect to
     * be able to attach your own click listeners. The usual use of a true
     * call is to stop clicking automatically changing node. 
     *  
     * @param ignoreMouseClicks
     */
    public void setIgnoreMouseClicks(boolean ignoreMouseClicks)
    {
        this.ignoreMouseClicks = ignoreMouseClicks;
    }

    public boolean isEditableRowMoves()
    {
        return editableRowMoves;
    }

    public void setEditableRowMoves(boolean editableRowMoves)
    {
        this.editableRowMoves = editableRowMoves;
    }

    public int getId()
    {
        return id;
    }

    public void setSpacerRow(int spacerRow)
    {
        this.spacerRow = spacerRow;
    }

/*
public boolean onEmptyCell( int row, int col)
{
    boolean result = false;
    Object control = getControlAt( row, col);
    if(control instanceof EditableObjComp)
    {
        EditableObjComp editableObjComp = (EditableObjComp)control;
        if(!Utils.isBlank( editableObjComp.getText()))
        {
            Err.error( "Expected to have a real control when data (" + editableObjComp.getText() + ") put into row " + row + ", col " + col);
        }
        result = true;
        Err.pr( SdzNote.CTV_ADVANCED_COMP,
                "Instead of using this method we could return null from getControlAt()");
    }
    else if(control == null)
    {
        //Not tested that even get here
        result = true;
    }
    return result;
}
*/
}
