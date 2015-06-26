package org.strandz.lgpl.widgets.table;

import javax.swing.event.TableModelEvent;

/**
 * User: Chris
 * Date: 22/01/2009
 * Time: 6:28:48 PM
 */
interface CTVModelI
{
    int getRowCount();
    void modelChanged();
    void modelChanging( String reason, TableModelEvent evt);
    Object getValueAt( int row, int col);
    boolean isPendingInsert();
    int adjustRowFromTableForModel( int row);
    int adjustRowFromModelForTable( int row);
}
