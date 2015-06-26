package org.strandz.lgpl.widgets.table;

/**
 * User: Chris
 * Date: 11/03/2009
 * Time: 3:41:35 AM
 */
public interface FocusIgnorerI
{
    void pushIgnoreFocus();
    void popIgnoreFocus();
    boolean isIgnoreFocus();
}
