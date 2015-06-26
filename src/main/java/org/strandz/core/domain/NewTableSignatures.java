package org.strandz.core.domain;

import org.strandz.lgpl.util.*;
import org.strandz.lgpl.data.objects.TimeSpent;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.core.info.domain.TableControlDescriptor;
import org.strandz.core.info.domain.AbstractOwnTableMethods;
import org.strandz.core.info.domain.ItemDescriptor;
import org.strandz.core.info.convert.AbstractDOInterrogate;
import org.strandz.core.widgets.TableComp;

import java.util.*;
import java.util.List;
import java.lang.reflect.Method;
import java.awt.*;

/**
 * Some of these methods could, but don't yet, handle complex types.
 * We need to look at the cell editor/renderer and call a
 * ControlSignatures (actually create FieldSignatures when this
 * happens) method. Only need to do this for abilities that TableModel
 * does not provide. For instance do not need to do it for setting and
 * getting a table editable. Where methods need this work have put in
 * **COMPLEX METHOD.
 * Actually too difficult to do this. Test with a complex component,
 * for instance a date component. Those [info.*Convert]s must surely
 * also play a role.
 */
public class NewTableSignatures
{
    private static HashMap tcds = new HashMap(); // TableControlDescriptor
    private static boolean tablesValidated = false;
    private static Class startRecursiveTableControl;

    private static int times;

    /**
     * Effectively 1/3 the constructor. The Seaweed product does not itself know
     * about any Controls, other than through this and setFieldsInfo method calls.
     */
    private static void setTablesInfo()
    {
        if(tablesValidated)
        {
            Err.error("Cannot call setTablesInfo twice");
        }

        int i;
        TableControlDescriptor[] l_ccds = ControlSignatures.getImpl().getTableControlDescriptors();
        for(i = 0; i <= l_ccds.length - 1; i++)
        {
            tcds.put(l_ccds[i].controlClass, l_ccds[i]);
        }
        validateTables();
    }

    public static boolean isTableControl(Class clazz)
    {
        boolean result = true;
        try
        {
            result = internalCheckTableControlExists(clazz, false);
        }
        catch(UnknownControlException ex)
        {
            Err.error(
                clazz.getName()
                    + " is not a known table control - NOT SUPPOSED TO HAPPEN!!");
        }
        return result;
    }

    public static void checkTableControlExists(Class clazz)
        throws UnknownControlException
    {
        startRecursiveTableControl = clazz;
        internalCheckTableControlExists(clazz, true);
    }

    /**
     * If the control class does not exist in the list, but
     * a parent class does, then we must add a new entry onto
     * our HashMap that contains the new class as a key, and
     * the same value.
     */
    private static boolean internalCheckTableControlExists(Class clazz, boolean thExp)
        throws UnknownControlException
    {
        if(!tablesValidated)
        {
            setTablesInfo();
        }

        boolean componentAllowed = false;
        if(tcds.containsKey(clazz))
        {
            // Err.pr( "\t" + clazz + " exists");
            componentAllowed = true;
        }
        else
        {
            Class parent = clazz.getSuperclass();
            if(parent == Object.class)
            {
                /*
                Err.error("Table Control " +
                outerTableControl.getName() +
                " (or any superclass) not in file " + bii.getClass());
                */
                if(thExp)
                {
                    throw new UnknownControlException(
                        startRecursiveTableControl.getName(),
                        ControlSignatures.getImpl().getClass());
                }
            }
            else
            {
                // Err.pr( "\tTo recurse with " + parent);
                if(internalCheckTableControlExists(parent, thExp))
                {
                    Object value = tcds.get(parent);
                    // Err.pr( "\t" + outerTableControl + " is being added");
                    tcds.put(clazz, value);
                }
            }
        }
        return componentAllowed;
    }

    public static Class getTableModel(Object control)
    {
        Class clazz = control.getClass();
        TableControlDescriptor tcd = getTCDescriptorForClass(clazz);
        if(tcd == null)
        {
            Err.error(
                "Table " + control.getClass()
                    + " is not known about (no TableControlDescriptor entry for it or any ancestor)");
        }
        // Err.pr( "Control class returning is " + tcd.controlClass);
        // Err.pr( "Got from control " + clazz);
        // Err.pr( "Will return us " + tcd.tableModelClass);
        // Err.pr( "");
        return tcd.tableModelClass;
    }
    
    public static Object getModel(Object table)
    {
        Object result = null;
        TableControlDescriptor tcd = getTCDescriptorForClass(table.getClass());
        Method method = tcd.getModelMethod;
        if(method != null)
        {
            Object args[] = {};
            result = SelfReferenceUtils.invoke(table, method, args);
        }
        return result;
    }

    public static void setModel(Object table, Object model)
    {
        TableControlDescriptor tcd = getTCDescriptorForClass(table.getClass());
        Method method = tcd.setModelMethod;
        Object args[] = {model};
        /*
        new MessageDlg("control " + control);
        new MessageDlg("method " + method);
        for(int i=0; i<=args.length-1; i++)
        {
        new MessageDlg("arg: " + args[i]);
        }
        */
        if(method != null)
        {
            SelfReferenceUtils.invoke(table, method, args);
        }

        /*
        Directly calling can help with debugging as will get a proper
        stack trace.
        if(control instanceof JTable)
        {
        ((JTable)control).setModel( (TableModel)model);
        }
        else
        {
        Err.pr( "time to put code back");
        }
        */
        //Used to be called from here
        //setTableBuffer(Object table, int numCols)
    }

    public static List getListFromColumnName(Object tableControl, String identifier)
    {
        List result = null;
        // TableControlDescriptor tcd = getTCDescriptorForClass( tableControl.getClass());
        // checkParams( tableControl, identifier, tcd);
        AbstractOwnTableMethods ownTableMethods = getOwnTableMethods(tableControl, identifier);
        if(ownTableMethods == null)
        {
            Err.error("Could not getOwnTableMethods from <" + tableControl + "> and <"
                + identifier + ">");
        }
        result = ownTableMethods.getList(tableControl, identifier);
        return result;
    }

    public static Color getDesignTimeColor(Class clazz)
    {
        return Color.RED;
    }

    public static Object getValueFromColumnName(Object tableControl, String identifier)
    {
        if(!tablesValidated)
        {
            Err.error("NewTableSignatures must be validated");
        }
        // TableControlDescriptor tcd = getTCDescriptorForClass( tableControl.getClass());
        // checkParams( tableControl, identifier, tcd);
        // AbstractOwnTableMethods ownTableMethods = tcd.ownTableMethods;
        return getOwnTableMethods(tableControl, identifier).getValue(tableControl,
            identifier);
    }

    private static void checkParams(
        Object tableControl,
        String identifier,
        TableControlDescriptor tcd)
    {
        if(tableControl == null)
        {
            Err.error("getListFromColumnName() requires a tableControl param");
        }
        if(Utils.isBlank( identifier))
        {
            Err.error("getListFromColumnName() requires an identifier param");
        }
        if(tcd == null)
        {
            Err.error("No TableControlDescriptor for " + tableControl.getClass());
        }
    }

    public static void setListFromColumnName(Object tableControl, String identifier, Object value)
    {
        // TableControlDescriptor tcd = getTCDescriptorForClass( tableControl.getClass());
        // AbstractOwnTableMethods ownTableMethods = tcd.ownTableMethods;
        getOwnTableMethods(tableControl, identifier).setList(tableControl,
            identifier, value);
    }

    public static void setValueFromColumnName(Object tableControl, String identifier, Object value)
    {
        // TableControlDescriptor tcd = getTCDescriptorForClass( tableControl.getClass());
        // AbstractOwnTableMethods ownTableMethods = tcd.ownTableMethods;
        getOwnTableMethods(tableControl, identifier).setValue(tableControl,
            identifier, value);
    }

    /**
     * Check that ControlInfoImpl contained reasonable values.
     */
    private static void validateTables()
    {
        for(Iterator it = tcds.values().iterator(); it.hasNext();)
        {
            TableControlDescriptor tcd = (TableControlDescriptor) it.next();
            // Err.pr( "control class have is " + tcd.controlClass);
        }
        tablesValidated = true;
    }

    // **COMPLEX METHOD

    static public int getBlankingPolicy(IdEnum id)
    {
        int result = Utils.UNSET_INT;
        if(!ControlSignatures.doInterrogatorsValidated)
        {
            ControlSignatures.setDOInterrogators();
        }

        boolean found = false;
        for(Iterator it = ControlSignatures.doInterrogators.iterator(); it.hasNext();)
        {
            AbstractDOInterrogate interrogate = (AbstractDOInterrogate) it.next();
            if(interrogate.getClassFor() == id.getClazz())
            {
                result = interrogate.getBlankingPolicy();
                found = true;
                break;
            }
        }
        if(!found)
        {
            Err.error( "No blanking policy (AbstractDOInterrogate) registered for a <" + id.getClazz().getName() + ">");
        }
        return result;
    }

    public static boolean isTextBlank(IdEnum id)
    {
        boolean result = false;
        Object text = getText(id);
        if(text == null)
        {
            result = true;
        }
        else
        {
            /* Doesn't work
            if(id.getClazz() != text.getClass())
            {
                //Some checking to prove could use either in comparison below
                Err.pr( "id.getClazz(): " + id.getClazz());
                Err.pr( "text.getClazz(): " + text.getClass());
                Err.pr( "id.getColumn(): " + id.getColumn());
                Err.error( "getColumnClass() s/have returned the value from the DO");
            }
            */
            if(!ControlSignatures.doInterrogatorsValidated)
            {
                ControlSignatures.setDOInterrogators();
            }
            boolean found = false;
            Class objClass = text.getClass();
            for(Iterator it = ControlSignatures.doInterrogators.iterator(); it.hasNext();)
            {
                AbstractDOInterrogate interrogate = (AbstractDOInterrogate) it.next();
                if(interrogate.getClassFor() == objClass)
                {
                    result = interrogate.isBlank( text);
                    found = true;
                    break;
                }
            }
            if(!found)
            {
                Err.error( "No AbstractDOInterrogate exists for a <" + id.getClazz().getName() + ">");
            }
        }
        return result;
    }

    public static boolean _isTextBlank(IdEnum id)
    {
        boolean result = false;
        Object text = getText(id);
        if(text == null)
        {
            result = true;
        }
        else
        {
            //Type of text may for instance be Money. Doing an equals() against
            //the String "" on a type of money will at best return false and at
            //worst stack trace.
            if(Utils.instanceOf( text, String.class))
            {
                if(text.equals(""))
                {
                    result = true;
                }
            }
            else
            {
                Err.error( "Unable to determine whether text is blank for a type of " + text.getClass().getName());
            }
        }
        return result;
    }

    /**
     * One day sort this out. Say next time have a DO with a
     * Boolean in it that have not initialized, or for which
     * type is boolean. Problems around automatically creating
     * which will have to do.
     */
    static public int _getBlankingPolicy(IdEnum id)
    {
        // Err.pr( "id.getClazz() is " + id.getClazz());
        int result = AbstractDOInterrogate.NEED_PUT_NULL_OBJ_WRITE;
        if(id.getClazz() == Boolean.class || id.getClazz() == Integer.class
            || id.getClazz() == TimeSpent.class || id.getClazz() == Date.class)
        {
            result = AbstractDOInterrogate.NEED_NOTHING;
        }
        if(SdzNote.TABLE_BLANKING_POLICY.isVisible())
        {
            times++;
            Err.pr( "getBlankingPolicy() reting " + result + " for " +
                id.getClazz() + " times " + times);
            if(times == 0)
            {
                Err.debug();
            }
        }
        return result;
    }

//    public static List getTableBuffer(Object table)
//    {
//        return (List) buffers.get(table);
//    }
    
    private static AbstractOwnTableMethods getTableMethods( IdEnum id)
    {
        AbstractOwnTableMethods result = null;
        result = getOwnTableMethods(id.getTable(), null);
        if(result == null)
        {
            Err.error( "No AbstractOwnTableMethods subclass has been created for a table of type <" + id.getTable().getClass().getName() + ">");
        }
        return result;
    }

    /**
     * As go into the cell renderer then should bring back
     * a String rather than the object being stored, and thus the
     * same validation will work whether for a table item or for a
     * field item.
     */
    public static Object getText( IdEnum id)
    {
        Object result = null;
        Object cellControl = getTableMethods( id).getControlAt(id.getTable(), ((TableIdEnum)id).getRow(), id.getColumn());
        if(cellControl != null)
        {
            Err.pr( SdzNote.CTV_SDZ_INTEGRATION, "cellControl is of type: <" + cellControl.getClass().getName() + ">");
            /*
            result = ComponentUtils.getText( cellControl);
             */
            IdEnum fieldId = IdEnum.newField( cellControl);
            result = ControlSignatures.getText( fieldId);
            Err.pr( SdzNote.CTV_SDZ_INTEGRATION, "has value: <" + result + ">");
            Err.pr( SdzNote.CTV_SDZ_INTEGRATION, "");
        }
        Err.pr( SdzNote.TABLE_VALUE_TRACE, "To ret <" + result + "> from " + id);            
        return result;
    }
    
    public static void setText(IdEnum id, Object aValue)
    {
        Object renderer = getTableMethods( id).getControlAt(id.getTable(), ((TableIdEnum)id).getRow(), id.getColumn());
        Err.pr( SdzNote.CTV_STRANGE_LOADING, "control at row " + ((TableIdEnum)id).getRow() + ", col " + id.getColumn() + 
                " to be set with <" + aValue + ">");
        IdEnum fieldId = IdEnum.newField( renderer);
        ControlSignatures.setText( fieldId, aValue);
    }
    
    public static void setToolTipText(IdEnum id, Object aValue)
    {
        Object renderer = getTableMethods( id).getControlAt(id.getTable(), ((TableIdEnum)id).getRow(), id.getColumn());
        IdEnum fieldId = IdEnum.newField( renderer);
        ControlSignatures.setToolTipText( fieldId, aValue);
    }
    
    public static Object getItem( IdEnum id)
    {
        Object result;
        result = getTableMethods( id).getControlAt(id.getTable(), ((TableIdEnum)id).getRow(), id.getColumn());
        return result;
    }

    public static Object getBlankText(IdEnum id)
    {
        return null;
    }

    public static void setTextBlank(IdEnum id)
    {
        Object cellControl = getTableMethods( id).getControlAt(id.getTable(), ((TableIdEnum)id).getRow(), id.getColumn());
        if(cellControl != null)
        {
            IdEnum fieldId = IdEnum.newField( cellControl);
            ControlSignatures.setText( fieldId, getBlankText( fieldId));
        }
    }

    public static void repositionTo(IdEnum id)
    {
        TableIdEnum tid = (TableIdEnum) id;
        TableControlDescriptor tcd = getTCDescriptorForIdEnum(id);
        Method method = tcd.requestFocusMethod;
        SelfReferenceUtils.invoke(tid.getTable(), method);
        Err.pr(SdzNote.BAD_TABLE_REPOSITIONING, "Repositioning to " + tid.getRow() + " using " + tid);
        tcd.ownTableMethods.repositionTo(tid.getTable(), tid.getRow(),
            tid.getColumn());
    }

    public static boolean isEnabled(IdEnum id)
    {
        boolean result = false;
        AbstractOwnTableMethods tableMethods = getTableMethods( id); 
        boolean editable = tableMethods.isEditableControlAt(id.getTable(), /*((TableIdEnum)id).getRow(),*/ id.getColumn());
        if(editable)
        {
            Object cellControl = tableMethods.getControlAt(id.getTable(), /*((TableIdEnum)id).getRow(),*/ id.getColumn());
            if(cellControl != null)
            {
                result = ControlSignatures.isEnabled( cellControl);
            }
        }
        return result;
    }
    
    public static void setEnabled(IdEnum id, boolean b)
    {
        AbstractOwnTableMethods tableMethods = getTableMethods( id); 
        boolean editable = tableMethods.isEditableControlAt(id.getTable(), /*((TableIdEnum)id).getRow(),*/ id.getColumn());
        if(editable)
        {
            Object cellControl = tableMethods.getControlAt(id.getTable(), /*((TableIdEnum)id).getRow(),*/ id.getColumn());
            //Err.pr( "cellControl: " + cellControl);
            if(cellControl != null)
            {
                ControlSignatures.setEnabled( cellControl, b);
            }
            else
            {
                Err.error( "Table <" + id.getTable() + "> does not create its own controls on demand, col used was " + 
                        id.getColumn());
                //
                //Use setControlAt() to create the control. In CTV we must make that this control is picked up
                //tableMethods.setControlAt( id.getTable(), ((TableIdEnum)id).getRow(), id.getColumn(), cellControl);
            }
        }
    }
    
    /*
    Object model = getModel( id.getTable());
    if(model != null)
    {
        Err.error( "A model " + model + " exists so perhaps we could actually create the control at row " + ((TableIdEnum)id).getRow() + ", col " + id.getColumn());
    }
    else
    {
        Err.pr( "Cannot setEnabled() because no control yet exists at row " + ((TableIdEnum)id).getRow() + ", col " + id.getColumn());
    }
    */

    public static void setLOV(IdEnum id, List items)
    {
        Err.error("Not written setLOV() for NewTableSignatures");
    }

    private static void chkBuffer(RowBuffer buffer, IdEnum id)
    {
        if(buffer == null)
        {
            Object obj = id.getTable();
            if(obj instanceof Component)
            {
                Err.error(
                    "Could not find a buffer for table named <"
                        + ((Component) obj).getName() + ">");
            }
            else
            {
                Err.error(
                    "Could not find a buffer for table named <"
                        + ((TableComp) obj).getName() + ">");
            }
        }
    }

//    public static Object convertToDisplaySpecialControlType(
//        IdEnum id, Class typeRequired, Object object)
//    {
//        Err.error("Same as for field, but different params");
//        return null;
//    }

    /**
     * Original doesn't get used here, but maybe it will for another
     */
    public static Color getBGColor(IdEnum id)
    {
        // TableControlDescriptor tcd = getTCDescriptorForIdEnum( id);
        // AbstractOwnTableMethods ownTableMethods = tcd.ownTableMethods;
        TableIdEnum tid = (TableIdEnum) id;
        Object renderer = getOwnTableMethods(tid).getControlAt(tid.getTable(),
                                                               tid.getRow(), tid.getColumn());
        FieldIdEnum newId = IdEnum.newField(renderer);
        return ControlSignatures.getBGColor(newId);
    }

    /**
     * Here we cater for two situations. First where every cell has its own
     * renderer. Second (JTable brought this on) where the renderer is shared
     * and we have to have written our own. Two ways of doing this. First is that
     * user may have created own renderer that interacts with Strandz. In this case
     * the FieldControlDescriptor isMLRenderer == true. Other way is to specify a
     * renderer to change to using fcd.replacementCellRenderer. This second way
     * can be shipped to change from DefaultCellRenderer, so making it work with
     * a straight JTable.
     * The object to be returned is the original state that will want to set back.
     * Often implemented as background Color.
     */
    public static void setBGColor(IdEnum id, Object color)
    {
        TableIdEnum tid = (TableIdEnum) id;
        TableControlDescriptor tcd = getTCDescriptorForIdEnum(id);
        // AbstractOwnTableMethods ownTableMethods = tcd.ownTableMethods;
        AbstractOwnTableMethods ownTableMethods = getOwnTableMethods(tid);
        Object renderer = ownTableMethods.getControlAt(tid.getTable(), tid.getRow(),
                                                       tid.getColumn());
        ItemDescriptor fcd = ControlSignatures.getFCDescriptorForClass(
            renderer.getClass());
        // pUtils.debug( ControlSignatures.fcds);
        if(fcd.replacementCellRenderer != null && !fcd.isSdzRenderer)
        {
            Object newRenderer = null;
            Class newRendererType = fcd.replacementCellRenderer;
            newRenderer = ObjectFoundryUtils.factory(newRendererType);

            FieldIdEnum newId = IdEnum.newField(newRenderer);
            //FieldControlDescriptor newFCD = ControlSignatures.getFCDescriptorForClass(
            //    newRendererType);
            FieldIdEnum oldId = IdEnum.newField(renderer);
            ControlSignatures.setBGColor(newId, ControlSignatures.getBGColor(oldId));
            ControlSignatures.setText(newId, ControlSignatures.getText(oldId));
            ControlSignatures.setEnabled(newId, ControlSignatures.isEnabled(oldId));
            ownTableMethods.setControlAt(tid.getTable(), tid.getRow(), tid.getColumn(),
                                         newRenderer);
            // Normally used at configuration time. We use it here so that once we've
            // dynamically installed the renderer, it looks just as if we had done it
            // at configuration time.
            fcd.isSdzRenderer = true;
        }
        else if(fcd.isSdzRenderer == true)
        {
        }
        else if(tcd.rendererPerCell == true)
        {
            /*
            * Here there is no special renderer that can take a look at
            * the attribute to decide whether it is in error, so this call
            * should work out to be exactly the same as if the call was for
            * a field.
            */
            FieldIdEnum newId = IdEnum.newField(renderer);
            ControlSignatures.setBGColor(newId, color);
        }
        else
        {
            Err.error("Must use a TableCellRenderer that interacts with Strandz");
        }
    }

    private static TableControlDescriptor getTCDescriptorForIdEnum(IdEnum id)
    {
        return getTCDescriptorForClass(id.getTable().getClass());
    }

    private static TableControlDescriptor getTCDescriptorForClass(Class clazz)
    {
        if(!tablesValidated)
        {
            setTablesInfo();
        }

        TableControlDescriptor tcd = (TableControlDescriptor) tcds.get(clazz);
        if(tcd == null)
        {
            if(isTableControl(clazz))
            {
                // Now try again, above s/have added to hash table.
                tcd = (TableControlDescriptor) tcds.get(clazz);
            }
            else
            {
                Err.error(
                    "Need a new TableControlDescriptor for the control "
                        + clazz.getName());
            }
        }
        return tcd;
    }

    private static AbstractOwnTableMethods getOwnTableMethods(TableIdEnum tid)
    {
        TableControlDescriptor tcd = getTCDescriptorForIdEnum(tid);
        AbstractOwnTableMethods ownTableMethods = tcd.ownTableMethods;
        return ownTableMethods;
    }

    private static AbstractOwnTableMethods getOwnTableMethods(
        Object tableControl, String identifier)
    {
        TableControlDescriptor tcd = getTCDescriptorForClass(
            tableControl.getClass());
        AbstractOwnTableMethods ownTableMethods = tcd.ownTableMethods;
        if(identifier != null)
        {
            NewTableSignatures.checkParams(tableControl, identifier, tcd);
        }
        return ownTableMethods;
    }
}
