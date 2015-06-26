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

import org.strandz.core.info.convert.AbstractObjectControlConvert;
import org.strandz.core.info.convert.BooleanInStringComponentConvert;
import org.strandz.core.info.convert.BooleanInbooleanComponentConvert;
import org.strandz.core.info.convert.ClassConvert;
import org.strandz.core.info.convert.ComponentInTextComponentConvert;
import org.strandz.core.info.convert.DateInTextComponentConvert;
import org.strandz.core.info.convert.IntegerInTextComponentConvert;
import org.strandz.core.info.convert.MoneyInTextComponentConvert;
import org.strandz.core.info.convert.ObjectInTextComponentConvert;
import org.strandz.core.info.convert.TimeSpentInTextComponentConvert;
import org.strandz.core.info.convert.AbstractDOInterrogate;
import org.strandz.core.info.convert.MoneyInterrogate;
import org.strandz.core.info.convert.BooleanInterrogate;
import org.strandz.core.info.convert.IntegerInterrogate;
import org.strandz.core.info.convert.TimeSpentInterrogate;
import org.strandz.core.info.convert.DateInterrogate;
import org.strandz.core.info.convert.StringInterrogate;
import org.strandz.core.info.convert.ClazzInTextComponentConvert;
import org.strandz.core.info.convert.BooleanInTextComponentConvert;
import org.strandz.core.info.convert.BigDecimalInTextComponentConvert;
import org.strandz.core.info.convert.HoursInTextComponentConvert;
import org.strandz.core.info.convert.KilometresInTextComponentConvert;
import org.strandz.core.info.convert.HoursInterrogate;
import org.strandz.core.info.convert.KilometresInterrogate;
import org.strandz.core.info.convert.ThousandsDollarsInTextComponentConvert;
import org.strandz.core.info.convert.ThousandsDollarsInterrogate;
import org.strandz.core.info.convert.ScorecardInManyItemsControlConvert;
import org.strandz.core.info.domain.AbstractOwnTableMethods;
import org.strandz.core.info.domain.ControlInfo;
import org.strandz.core.info.domain.ItemDescriptor;
import org.strandz.core.info.domain.FocusMonitorI;
import org.strandz.core.info.domain.TableControlDescriptor;
import org.strandz.lgpl.widgets.table.ComponentTableView;
import org.strandz.lgpl.widgets.EditableObjComp;
import org.strandz.lgpl.widgets.ObjComp;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ObjectFoundryUtils;

import java.awt.Color;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.Date;

public class SwingControlInfoImpl extends ControlInfo
{
    protected Class controlClass;
    protected Class classClass;
    protected Method setControlMethod;
    protected Method setToolTipMethod;
    protected Method getControlMethod;
    protected Method setEditableControlMethod;
    protected Method getEditableControlMethod;
    protected Method requestFocusMethod;
    private Method addActionListenerMethod;
    private Method removeActionListenerMethod;
    private Method removeAllItemsMethod;
    private Method addItemMethod;
    public Class[] args1 = new Class[1];
    private Class[] args2 = new Class[2];
    private Class[] args3 = new Class[3];
    protected Class[] argString = new Class[1];
    protected int blankControlOperator;
    protected int blankControlOperand;
    protected int blankingPolicy;
    private ItemDescriptor fcd;
    private ItemDescriptor[] fcds = new ItemDescriptor[18];
    private Class tableModelClass;
    private Method setModelMethod;
    private Method getModelMethod;
    private AbstractOwnTableMethods columnLister;
    private TableControlDescriptor[] ccds = new TableControlDescriptor[6];
    private FocusMonitor focusMonitor;
    private static final String SET_CAN_CHANGE = "setEditable";
    private static final String IS_CAN_CHANGE = "isEditable";
    protected static final String SET_CAN_CHANGE_CHK = "setEnabled";
    protected static final String IS_CAN_CHANGE_CHK = "isEnabled";
    // 31/10/03 Changed to editable for setLabel for DT.
    // then back!
    private static final String SET_CAN_CHANGE_CB = "setEnabled";
    private static final String IS_CAN_CHANGE_CB = "isEnabled";
    private static final String ADD_ACTION = "addActionListener";
    private static final String REMOVE_ACTION = "removeActionListener";
    private static Color blueColor = Color.BLUE.brighter();

    public AbstractObjectControlConvert[] getObjectControlConverts()
    {
        AbstractObjectControlConvert[] results = new AbstractObjectControlConvert[19];
        ComponentInTextComponentConvert c = new ComponentInTextComponentConvert();
        results[0] = c;
        // ContainerInTextComponentConvert con = new ContainerInTextComponentConvert();
        // results[1] = con;
        // PhysicalNodeControllerInterfaceConvert p = new PhysicalNodeControllerInterfaceConvert();
        results[1] = (AbstractObjectControlConvert) ObjectFoundryUtils.factory(
                "org.strandz.core.interf.PhysicalNodeControllerInterfaceConvert");
        // NodeConvert n = new NodeConvert();
        results[2] = (AbstractObjectControlConvert) ObjectFoundryUtils.factory(
                "org.strandz.core.interf.NodeConvert");

        ClassConvert classC = new ClassConvert();
        results[3] = classC;

        BooleanInbooleanComponentConvert bc = new BooleanInbooleanComponentConvert();
        results[4] = bc;

        BooleanInStringComponentConvert sc = new BooleanInStringComponentConvert();
        results[5] = sc;
        // NodeStatusBarInterfaceConvert nc = new NodeStatusBarInterfaceConvert();
        results[6] = (AbstractObjectControlConvert) ObjectFoundryUtils.factory(
                "org.strandz.core.applichousing.NodeStatusBarInterfaceConvert");

        DateInTextComponentConvert dc = new DateInTextComponentConvert();
        results[7] = dc;

        MoneyInTextComponentConvert mc = new MoneyInTextComponentConvert();
        results[8] = mc;

        ScorecardInManyItemsControlConvert sci = new ScorecardInManyItemsControlConvert();
        results[9] = sci;

        TimeSpentInTextComponentConvert tsc = new TimeSpentInTextComponentConvert();
        results[10] = tsc;

        IntegerInTextComponentConvert ic = new IntegerInTextComponentConvert();
        results[11] = ic;

        ClazzInTextComponentConvert cc = new ClazzInTextComponentConvert();
        results[12] = cc;
        
        BooleanInTextComponentConvert bit = new BooleanInTextComponentConvert();
        results[13] = bit;

        BigDecimalInTextComponentConvert bdt = new BigDecimalInTextComponentConvert();
        results[14] = bdt;

        HoursInTextComponentConvert ht = new HoursInTextComponentConvert();
        results[15] = ht;

        KilometresInTextComponentConvert kt = new KilometresInTextComponentConvert();
        results[16] = kt;

        ThousandsDollarsInTextComponentConvert td = new ThousandsDollarsInTextComponentConvert();
        results[17] = td;
        
        // Bit of a catch all so have last (Wanting to stay JTable independent)
        ObjectInTextComponentConvert oit = new ObjectInTextComponentConvert();
        results[18] = oit;
        return results;
    }

    public AbstractDOInterrogate[] getDOInterrogators()
    {
        AbstractDOInterrogate[] results = new AbstractDOInterrogate[9];
        results[0] = new MoneyInterrogate();
        results[1] = new BooleanInterrogate();
        results[2] = new IntegerInterrogate();
        results[3] = new TimeSpentInterrogate();
        results[4] = new DateInterrogate();
        results[5] = new StringInterrogate();
        results[6] = new HoursInterrogate();
        results[7] = new KilometresInterrogate();
        results[8] = new ThousandsDollarsInterrogate();
        return results;
    }

    /**
     * There may definitely be some interesting controls within
     * these controls.
     */
    public Class[] getLookThruControls()
    {
        Class[] classes = new Class[2];
        classes[0] = javax.swing.JScrollPane.class;
        classes[1] = javax.swing.JViewport.class;
        // classes[2] = javax.swing.CellRendererPane.class;
        return classes;
    }

    /**
     * There may definitely be some interesting controls within
     * these controls. Also the user will want these controls to
     * be prominently displayed.
     */
    public Class[] getLookThruButStructuralControls()
    {
        Class[] classes = new Class[1];
        classes[0] = javax.swing.JPanel.class;
        return classes;
    }

    /**
     * End controls don't care about, so won't be looking into. If you get a
     * control displaying in View that you don't want to see, then place it
     * in here.
     * <p/>
     * Had to use Strings:
     * strandz.info\SwingControlInfoImpl.java:53: javax.swing.JScrollPane.ScrollBar has protected access in javax.swing.JScrollPane
     * classes[1] = javax.swing.JScrollPane$ScrollBar.class;
     */
    public String[] getTerminatingControls()
    {
        String[] classes = new String[12];
        classes[0] = "javax.swing.JLabel"; 
        // sometimes will need to see JLabels - by convention use org.strandz.lgpl.widgets.ROJLabel -
        //for any others use getNotTerminatingControls()
        classes[1] = "javax.swing.JScrollPane$ScrollBar";
        classes[2] = "javax.swing.Box$Filler";
        classes[3] = "javax.swing.plaf.metal.MetalScrollButton";
        classes[4] = "javax.swing.plaf.metal.MetalComboBoxButton";
        classes[5] = "javax.swing.JButton";
        classes[6] = "javax.swing.CellRendererPane";
        classes[7] = "javax.swing.plaf.metal.MetalComboBoxEditor$1";
        classes[8] = "com.sun.java.swing.plaf.windows.XPStyle$GlyphButton";
        classes[9] = "mseries.ui.MDateEntryField$1";
        classes[10] = "mseries.ui.MDateField";
        classes[11] = "com.sun.java.swing.plaf.windows.WindowsComboBoxUI$XPComboBoxButton";
        //May not need, as have entry as a visible terminating control (and putting 'My' in was my addition)
        //classes[12] = "com.michaelbaranov.microba.calendar.ui.basic.BasicDatePickerUI$MyJFormattedTextField";
        //This one here because we are now happy to map
        //to the OneOnlyGroup which will contain the JRadioButtons
        //NOW commented as made FlexibilityRadioButtons and perhaps its
        //cohorts (when/if create them) into VisibleTerminatingControls
        //classes[8] = "javax.swing.JRadioButton";
        return classes;
    }

    /**
     * When we want to override a Terminating Control, as by default all subclasses of
     * Terminating Controls are also terminating. 
     */
    public String[] getNotTerminatingControls()
    {
        String[] classes = new String[1];
        classes[0] = "org.strandz.lgpl.widgets.ROJLabel"; 
        return classes;
    }

    public String[] getSystemWidgetsPackages()
    {
        String[] packageNames = new String[1];
        packageNames[0] = "javax.swing";
        return packageNames;
    }

    /**
     * Complex endpoints. Note that if a control is not
     * listed here, that the only problem will be performance.
     * Everything worked perfectly even before JTextField was
     * added.
     */
    public Class[] getVisibleTerminatingControls()
    {
        Class[] classes = new Class[7];
        classes[0] = javax.swing.JTable.class;
        classes[1] = javax.swing.JComboBox.class;
        classes[2] = javax.swing.JTextField.class;
        classes[3] = org.strandz.widgets.data.wombatrescue.FlexibilityRadioButtons.class;
        classes[4] = mseries.ui.MDateEntryField.class;
        classes[5] = javax.swing.JList.class;
        classes[6] = org.strandz.lgpl.widgets.ROJLabel.class;
        //classes[7] = com.michaelbaranov.microba.calendar.DatePicker.class;
        return classes;
    }

    public ItemDescriptor[] getItemDescriptors()
    {
        //
        //
        controlClass = javax.swing.JTextField.class;
        classClass = java.lang.String.class;
        // public void setText( String t)
        args1[0] = classClass;
        argString[0] = java.lang.String.class;
        try
        {
            setControlMethod = controlClass.getMethod("setText", args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method setText from JTextField");
        }
        try
        {
            setToolTipMethod = controlClass.getMethod("setToolTipText", argString);
        }
        catch(Exception ex)
        {
            Err.error("Missing method setToolTipText from JTextField");
        }
        // public String getText()
        args1[0] = null;
        try
        {
            getControlMethod = controlClass.getMethod("getText", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method getText from JTextField");
        }
        args1[0] = Boolean.TYPE;
        try
        {
            setEditableControlMethod = controlClass.getMethod(SET_CAN_CHANGE, args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method " + SET_CAN_CHANGE + " from JTextField");
        }
        // public String isEditable()
        args1[0] = null;
        try
        {
            getEditableControlMethod = controlClass.getMethod(IS_CAN_CHANGE, (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method " + IS_CAN_CHANGE + " from JTextField");
        }
        // public void requestFocus()
        args1[0] = null;
        try
        {
            requestFocusMethod = controlClass.getMethod("requestFocusInWindow", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method requestFocus from JTextField");
        }
        args1[0] = ActionListener.class;
        try
        {
            addActionListenerMethod = controlClass.getMethod(ADD_ACTION, args1);
            removeActionListenerMethod = controlClass.getMethod(REMOVE_ACTION, args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // the rest
        blankControlOperator = ItemDescriptor.EQUALS_METHOD;
        blankControlOperand = ItemDescriptor.NULL_STRING;
        blankingPolicy = AbstractDOInterrogate.NEED_PUT_NULL_OBJ_WRITE;
        fcd = new ItemDescriptor(controlClass, "tf", classClass,
            setControlMethod, setToolTipMethod, getControlMethod, setEditableControlMethod,
            getEditableControlMethod, requestFocusMethod, addActionListenerMethod,
            removeActionListenerMethod, null, null, blankControlOperator,
            blankControlOperand, blankingPolicy, null, false,
            new BasicMoreComplexMethods(), blueColor, "name");
        fcds[0] = fcd;
        //
        //
        controlClass = org.strandz.lgpl.widgets.ComponentLabelField.class;
        classClass = javax.swing.JComponent.class;
        // public void setComponent( JComponent c)
        args1[0] = classClass;
        try
        {
            setControlMethod = controlClass.getMethod("setComponent", args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method setComponent from ComponentLabelField");
        }
        try
        {
            setToolTipMethod = controlClass.getMethod("setToolTipText", argString);
        }
        catch(Exception ex)
        {
            Err.error("Missing method setToolTipText from ComponentLabelField");
        }
        args1[0] = null;
        try
        {
            getControlMethod = controlClass.getMethod("getComponent", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method getComponent from ComponentLabelField");
        }
        setEditableControlMethod = null;
        getEditableControlMethod = null;
        // public void requestFocus()
        args1[0] = null;
        try
        {
            requestFocusMethod = controlClass.getMethod("requestFocusInWindow", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method requestFocus from ComponentLabelField");
        }
        // the rest
        blankControlOperator = ItemDescriptor.EQUALS_OPERATOR;
        blankControlOperand = ItemDescriptor.NULL_KEYWORD;
        blankingPolicy = AbstractDOInterrogate.NEED_NOTHING;
        fcd = new ItemDescriptor(controlClass, "l", classClass,
            setControlMethod, setToolTipMethod, getControlMethod, setEditableControlMethod,
            getEditableControlMethod, requestFocusMethod, null, null, null, null,
            blankControlOperator, blankControlOperand, blankingPolicy, null, false,
            new BasicMoreComplexMethods(), blueColor, null);
        fcds[1] = fcd;
        //
        //
        controlClass = org.strandz.lgpl.widgets.ComponentJLabel.class;
        classClass = javax.swing.JComponent.class;
        // public void setComponent( JComponent c)
        args1[0] = classClass;
        try
        {
            setControlMethod = controlClass.getMethod("setComponent", args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method setComponent from ComponentJLabel");
        }
        try
        {
            setToolTipMethod = controlClass.getMethod("setToolTipText", argString);
        }
        catch(Exception ex)
        {
            Err.error("Missing method setToolTipText from ComponentJLabel");
        }
        args1[0] = null;
        try
        {
            getControlMethod = controlClass.getMethod("getComponent", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method getComponent from ComponentJLabel");
        }
        setEditableControlMethod = null;
        getEditableControlMethod = null;
        // public void requestFocus()
        args1[0] = null;
        try
        {
            requestFocusMethod = controlClass.getMethod("requestFocusInWindow", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method requestFocus from ComponentJLabel");
        }
        // the rest
        blankControlOperator = ItemDescriptor.EQUALS_OPERATOR;
        blankControlOperand = ItemDescriptor.NULL_KEYWORD;
        blankingPolicy = AbstractDOInterrogate.NEED_NOTHING;
        fcd = new ItemDescriptor(controlClass, "l", classClass,
            setControlMethod, setToolTipMethod, getControlMethod, setEditableControlMethod,
            getEditableControlMethod, requestFocusMethod, null, null, null, null,
            blankControlOperator, blankControlOperand, blankingPolicy, null, false,
            new BasicMoreComplexMethods(), blueColor, null);
        fcds[2] = fcd;
        //
        //
        controlClass = org.strandz.lgpl.widgets.ContainerJLabel.class;
        classClass = java.awt.Container.class;
        // public void setContainer( Container c)
        args1[0] = classClass;
        try
        {
            setControlMethod = controlClass.getMethod("setContainer", args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method setContainer from ContainerJLabel");
        }
        try
        {
            setToolTipMethod = controlClass.getMethod("setToolTipText", argString);
        }
        catch(Exception ex)
        {
            Err.error("Missing method setToolTipText from ContainerJLabel");
        }
        args1[0] = null;
        try
        {
            getControlMethod = controlClass.getMethod("getContainer", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method getContainer from ContainerJLabel");
        }
        setEditableControlMethod = null;
        getEditableControlMethod = null;
        // public void requestFocus()
        args1[0] = null;
        try
        {
            requestFocusMethod = controlClass.getMethod("requestFocusInWindow", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method requestFocus from ContainerJLabel");
        }
        // the rest
        blankControlOperator = ItemDescriptor.EQUALS_OPERATOR;
        blankControlOperand = ItemDescriptor.NULL_KEYWORD;
        blankingPolicy = AbstractDOInterrogate.NEED_NOTHING;
        fcd = new ItemDescriptor(controlClass, "l", classClass,
            setControlMethod, setToolTipMethod, getControlMethod, setEditableControlMethod,
            getEditableControlMethod, requestFocusMethod, null, null, null, null,
            blankControlOperator, blankControlOperand, blankingPolicy, null, false,
            new BasicMoreComplexMethods(), blueColor, null);
        fcds[3] = fcd;
        //
        controlClass = org.strandz.lgpl.widgets.ActiveImageLabel.class;
        classClass = Boolean.TYPE;
        args1[0] = classClass;
        try
        {
            setControlMethod = controlClass.getMethod("setActive", args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        try
        {
            setToolTipMethod = controlClass.getMethod("setToolTipText", argString);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        args1[0] = null;
        try
        {
            getControlMethod = controlClass.getMethod("isActive", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        blankControlOperator = ItemDescriptor.NOT_USED;
        blankControlOperand = ItemDescriptor.NOT_USED;
        blankingPolicy = AbstractDOInterrogate.NEED_NOTHING;
        fcd = new ItemDescriptor(controlClass, null, classClass,
            setControlMethod, setToolTipMethod, getControlMethod, null, null, null, null, null, null,
            null, blankControlOperator, blankControlOperand, blankingPolicy, null,
            false, new MostlyEmptyMoreComplexMethods(), blueColor, null);
        fcds[4] = fcd;
        //        
        //
        controlClass = javax.swing.JLabel.class;
        classClass = java.lang.String.class;
        // public void setText( String t)
        args1[0] = classClass;
        try
        {
            setControlMethod = controlClass.getMethod("setText", args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method setText from JLabel");
        }
        try
        {
            setToolTipMethod = controlClass.getMethod("setToolTipText", argString);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // public String getText()
        args1[0] = null;
        try
        {
            getControlMethod = controlClass.getMethod("getText", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method getText from JLabel");
        }
        setEditableControlMethod = null;
        getEditableControlMethod = null;
        // public void requestFocus()
        args1[0] = null;
        try
        {
            requestFocusMethod = controlClass.getMethod("requestFocusInWindow", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method requestFocus from JLabel");
        }
        // the rest
        // blankControlOperator = FieldControlDescriptor.EQUALS_METHOD;
        // blankControlOperand = FieldControlDescriptor.NULL_STRING;
        blankControlOperator = ItemDescriptor.EQUALS_OPERATOR;
        blankControlOperand = ItemDescriptor.NULL_KEYWORD;
        blankingPolicy = AbstractDOInterrogate.NEED_PUT_NULL_OBJ_WRITE;
        fcd = new ItemDescriptor(controlClass, "l", classClass,
            setControlMethod, setToolTipMethod, getControlMethod, setEditableControlMethod,
            getEditableControlMethod, requestFocusMethod, null, null, null, null,
            blankControlOperator, blankControlOperand, blankingPolicy, null, false,
            new BasicMoreComplexMethods(), Color.YELLOW, "text");
        fcds[5] = fcd;
        /************/

        controlClass = javax.swing.table.DefaultTableCellRenderer.class;
        classClass = java.lang.String.class;
        // public void setText( String t)
        args1[0] = classClass;
        try
        {
            setControlMethod = controlClass.getMethod("setText", args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        try
        {
            setToolTipMethod = controlClass.getMethod("setToolTipText", argString);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // public String getText()
        args1[0] = null;
        try
        {
            getControlMethod = controlClass.getMethod("getText", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        setEditableControlMethod = null;
        getEditableControlMethod = null;
        // public void requestFocus()
        args1[0] = null;
        try
        {
            requestFocusMethod = controlClass.getMethod("requestFocusInWindow", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // the rest
        blankControlOperator = ItemDescriptor.EQUALS_METHOD;
        blankControlOperand = ItemDescriptor.NULL_STRING;
        blankingPolicy = AbstractDOInterrogate.NEED_PUT_NULL_OBJ_WRITE;
        fcd = new ItemDescriptor(controlClass, "l", classClass,
            setControlMethod, setToolTipMethod, getControlMethod, setEditableControlMethod,
            getEditableControlMethod, requestFocusMethod, null, null, null, null,
            blankControlOperator, blankControlOperand, blankingPolicy,
            org.strandz.core.info.impl.swing.ReplacementCellRenderer.class, false,
            new BasicMoreComplexMethods(), blueColor, null);
        fcds[6] = fcd;
        /************/

        controlClass = javax.swing.JTextArea.class;
        classClass = java.lang.String.class;
        // public void setText( String t)
        args1[0] = classClass;
        try
        {
            setControlMethod = controlClass.getMethod("setText", args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        try
        {
            setToolTipMethod = controlClass.getMethod("setToolTipText", argString);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // public String getText()
        args1[0] = null;
        try
        {
            getControlMethod = controlClass.getMethod("getText", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // public void setEditable( boolean)
        args1[0] = Boolean.TYPE;
        try
        {
            setEditableControlMethod = controlClass.getMethod(SET_CAN_CHANGE, args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // public String isEnabled()
        args1[0] = null;
        try
        {
            getEditableControlMethod = controlClass.getMethod(IS_CAN_CHANGE, (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method isEnabled from JTextArea");
        }
        // public void requestFocus()
        args1[0] = null;
        try
        {
            requestFocusMethod = controlClass.getMethod("requestFocusInWindow", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method requestFocus from JTextArea");
        }
        blankControlOperator = ItemDescriptor.EQUALS_METHOD;
        blankControlOperand = ItemDescriptor.NULL_STRING;
        blankingPolicy = AbstractDOInterrogate.NEED_PUT_NULL_OBJ_WRITE;
        fcd = new ItemDescriptor(controlClass, "ta", classClass,
            setControlMethod, setToolTipMethod, getControlMethod, setEditableControlMethod,
            getEditableControlMethod, requestFocusMethod, null, null, null, null,
            blankControlOperator, blankControlOperand, blankingPolicy, null, false,
            new BasicMoreComplexMethods(), blueColor, "name");
        fcds[7] = fcd;
        //
        //
        controlClass = javax.swing.JComboBox.class;
        classClass = java.lang.Object.class;
        // public void setSelectedItem(Object anObject)
        args1[0] = classClass;
        try
        {
            setControlMethod = controlClass.getMethod("setSelectedItem", args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        try
        {
            setToolTipMethod = controlClass.getMethod("setToolTipText", argString);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // public Object getSelectedItem()
        args1[0] = null;
        try
        {
            getControlMethod = controlClass.getMethod("getSelectedItem", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // public void setEnabled(boolean b)
        args1[0] = Boolean.TYPE;
        try
        {
            setEditableControlMethod = controlClass.getMethod(SET_CAN_CHANGE_CB,
                args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // public String isEnabled()
        args1[0] = null;
        try
        {
            getEditableControlMethod = controlClass.getMethod(IS_CAN_CHANGE_CB, (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // public void requestFocus()
        args1[0] = null;
        try
        {
            requestFocusMethod = controlClass.getMethod("requestFocusInWindow", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method requestFocus from JComboBox");
        }
        args1[0] = ActionListener.class;
        try
        {
            addActionListenerMethod = controlClass.getMethod(ADD_ACTION, args1);
            removeActionListenerMethod = controlClass.getMethod(REMOVE_ACTION, args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // public void removeAllItems()
        args1[0] = null;
        try
        {
            removeAllItemsMethod = controlClass.getMethod("removeAllItems", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        args1[0] = Object.class;
        try
        {
            addItemMethod = controlClass.getMethod("addItem", args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        blankControlOperator = ItemDescriptor.EQUALS_OPERATOR;
        blankControlOperand = ItemDescriptor.NULL_KEYWORD;
        blankingPolicy = AbstractDOInterrogate.NEED_NOTHING;
        fcd = new ItemDescriptor(controlClass, "cb", classClass,
            setControlMethod, setToolTipMethod, getControlMethod, setEditableControlMethod,
            getEditableControlMethod, requestFocusMethod, addActionListenerMethod,
            removeActionListenerMethod, removeAllItemsMethod, addItemMethod,
            blankControlOperator, blankControlOperand, blankingPolicy, null, false,
            new JComboBoxMoreComplexMethods(), blueColor, "name");
        fcds[8] = fcd;
        //
        //
        controlClass = org.strandz.widgets.data.wombatrescue.FlexibilityRadioButtons.class;
        classClass = java.lang.String.class;
        args1[0] = classClass;
        try
        {
            setControlMethod = controlClass.getMethod("setSelectedText", args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method setSelectedText from FlexibilityRadioButtons");
        }
        try
        {
            setToolTipMethod = controlClass.getMethod("setToolTipText", argString);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        args1[0] = null;
        try
        {
            getControlMethod = controlClass.getMethod("getSelectedText", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method getSelectedText from FlexibilityRadioButtons");
        }
        //
        args1[0] = Boolean.TYPE;
        try
        {
            setEditableControlMethod = controlClass.getMethod(SET_CAN_CHANGE_CHK, args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method " + SET_CAN_CHANGE_CHK + " from FlexibilityRadioButtons");
        }
        args1[0] = null;
        try
        {
            getEditableControlMethod = controlClass.getMethod(IS_CAN_CHANGE_CHK, (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method " + IS_CAN_CHANGE_CHK + " from FlexibilityRadioButtons");
        }
        args1[0] = null;
        try
        {
            requestFocusMethod = controlClass.getMethod("requestFocusInWindow", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method requestFocusInWindow from FlexibilityRadioButtons");
        }
        // the rest
        blankControlOperator = ItemDescriptor.EQUALS_OPERATOR;
        blankControlOperand = ItemDescriptor.NULL_KEYWORD;
        blankingPolicy = AbstractDOInterrogate.NEED_PUT_NULL_OBJ_WRITE;
        fcd = new ItemDescriptor(controlClass, "frb", classClass,
            setControlMethod, setToolTipMethod, getControlMethod, setEditableControlMethod,
            getEditableControlMethod, requestFocusMethod, null, null, null, null,
            blankControlOperator, blankControlOperand, blankingPolicy, null, false,
            new BasicMoreComplexMethods(), Color.YELLOW, "text");
        fcds[9] = fcd;
        //
        //
        controlClass = javax.swing.JCheckBox.class;
        classClass = Boolean.TYPE;
        // public void setSelected(boolean b)
        args1[0] = classClass;
        try
        {
            setControlMethod = controlClass.getMethod("setSelected", args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        try
        {
            setToolTipMethod = controlClass.getMethod("setToolTipText", argString);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // public Object isSelected()
        args1[0] = null;
        try
        {
            getControlMethod = controlClass.getMethod("isSelected", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // public void setEnabled(boolean b)
        args1[0] = Boolean.TYPE;
        try
        {
            setEditableControlMethod = controlClass.getMethod(SET_CAN_CHANGE_CHK,
                args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex + " for " + controlClass);
        }
        // public boolean isEnabled()
        args1[0] = null;
        try
        {
            getEditableControlMethod = controlClass.getMethod(IS_CAN_CHANGE_CHK, (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // public void requestFocus()
        args1[0] = null;
        try
        {
            requestFocusMethod = controlClass.getMethod("requestFocusInWindow", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        args1[0] = ActionListener.class;
        try
        {
            addActionListenerMethod = controlClass.getMethod(ADD_ACTION, args1);
            removeActionListenerMethod = controlClass.getMethod(REMOVE_ACTION, args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        blankControlOperator = ItemDescriptor.NOT_USED;
        blankControlOperand = ItemDescriptor.NOT_USED;
        blankingPolicy = AbstractDOInterrogate.NEED_NOTHING;
        fcd = new ItemDescriptor(controlClass, "chk", classClass,
            setControlMethod, setToolTipMethod, getControlMethod, setEditableControlMethod,
            getEditableControlMethod, requestFocusMethod, addActionListenerMethod,
            removeActionListenerMethod, null, null, blankControlOperator,
            blankControlOperand, blankingPolicy, null, false,
            new BasicMoreComplexMethods(), blueColor, "name");
        fcds[10] = fcd;
        //
        //
        controlClass = javax.swing.JButton.class;
        classClass = Boolean.TYPE;
        args1[0] = classClass;
        try
        {
            setControlMethod = controlClass.getMethod("setEnabled", args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        try
        {
            setToolTipMethod = controlClass.getMethod("setToolTipText", argString);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        args1[0] = null;
        try
        {
            getControlMethod = controlClass.getMethod("isEnabled", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        args1[0] = Boolean.TYPE;
        try
        {
            setEditableControlMethod = controlClass.getMethod(SET_CAN_CHANGE_CHK,
                args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex + " for " + controlClass);
        }
        args1[0] = null;
        try
        {
            getEditableControlMethod = controlClass.getMethod(IS_CAN_CHANGE_CHK, (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        blankControlOperator = ItemDescriptor.NOT_USED;
        blankControlOperand = ItemDescriptor.NOT_USED;
        blankingPolicy = AbstractDOInterrogate.NEED_NOTHING;
        fcd = new ItemDescriptor(controlClass, "b", classClass,
            setControlMethod, setToolTipMethod, getControlMethod, null, null, null, null, null, null,
            null, blankControlOperator, blankControlOperand, blankingPolicy, null,
            false, new BasicMoreComplexMethods(), blueColor, "name");
        fcds[11] = fcd;
        //
        //
        controlClass = ObjComp.class;
        classClass = Object.class;
        args1[0] = classClass;
        try
        {
            setControlMethod = controlClass.getMethod("setText", args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        args1[0] = null;
        try
        {
            getControlMethod = controlClass.getMethod("getText", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        blankControlOperator = ItemDescriptor.EQUALS_METHOD;
        blankControlOperand = ItemDescriptor.NULL_KEYWORD;
        blankingPolicy = AbstractDOInterrogate.NEED_NOTHING;
        fcd = new ItemDescriptor(controlClass, null, classClass,
            setControlMethod, null, getControlMethod, null, null, null, null, null, null,
            null, blankControlOperator, blankControlOperand, blankingPolicy, null,
            false, new MostlyEmptyMoreComplexMethods(), blueColor, null);
        fcds[12] = fcd;
        //
        //
        controlClass = EditableObjComp.class;
        classClass = Object.class;
        args1[0] = classClass;
        try
        {
            setControlMethod = controlClass.getMethod("setText", args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        args1[0] = null;
        try
        {
            getControlMethod = controlClass.getMethod("getText", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        args1[0] = Boolean.TYPE;
        try
        {
            setEditableControlMethod = controlClass.getMethod(SET_CAN_CHANGE,
                args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex + " for " + controlClass);
        }
        args1[0] = null;
        try
        {
            getEditableControlMethod = controlClass.getMethod(IS_CAN_CHANGE, (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        blankControlOperator = ItemDescriptor.EQUALS_METHOD;
        blankControlOperand = ItemDescriptor.NULL_KEYWORD;
        blankingPolicy = AbstractDOInterrogate.NEED_NOTHING;
        fcd = new ItemDescriptor(controlClass, null, classClass,
            setControlMethod, null, getControlMethod, setEditableControlMethod, getEditableControlMethod, 
            null, null, null, null,
            null, blankControlOperator, blankControlOperand, blankingPolicy, null,
            false, new MostlyEmptyMoreComplexMethods(), blueColor, null);
        fcds[13] = fcd;
        //
        //
        controlClass = org.strandz.core.widgets.StrComp.class;
        classClass = String.class;
        args1[0] = classClass;
        try
        {
            setControlMethod = controlClass.getMethod("setText", args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        args1[0] = null;
        try
        {
            getControlMethod = controlClass.getMethod("getText", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        blankControlOperator = ItemDescriptor.EQUALS_METHOD;
        blankControlOperand = ItemDescriptor.NULL_KEYWORD;
        blankingPolicy = AbstractDOInterrogate.NEED_NOTHING;
        fcd = new ItemDescriptor(controlClass, null, classClass,
            setControlMethod, null, getControlMethod, null, null, null, null, null, null,
            null, blankControlOperator, blankControlOperand, blankingPolicy, null,
            false, new MostlyEmptyMoreComplexMethods(), blueColor, null);
        fcds[14] = fcd;
        //
        //
        controlClass = org.strandz.core.widgets.DateComp.class;
        classClass = Date.class;
        args1[0] = classClass;
        try
        {
            setControlMethod = controlClass.getMethod("setText", args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        args1[0] = null;
        try
        {
            getControlMethod = controlClass.getMethod("getText", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        blankControlOperator = ItemDescriptor.EQUALS_METHOD;
        blankControlOperand = ItemDescriptor.NULL_KEYWORD;
        blankingPolicy = AbstractDOInterrogate.NEED_NOTHING;
        fcd = new ItemDescriptor(controlClass, null, classClass,
            setControlMethod, null, getControlMethod, null, null, null, null, null, null,
            null, blankControlOperator, blankControlOperand, blankingPolicy, null,
            false, new MostlyEmptyMoreComplexMethods(), blueColor, null);
        fcds[15] = fcd;
        //
        //
        controlClass = org.strandz.core.widgets.PrimitiveBooleanComp.class;
        classClass = Boolean.TYPE;
        args1[0] = classClass;
        try
        {
            setControlMethod = controlClass.getMethod("setText", args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        args1[0] = null;
        try
        {
            getControlMethod = controlClass.getMethod("getText", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        blankControlOperator = ItemDescriptor.NOT_USED;
        blankControlOperand = ItemDescriptor.NOT_USED;
        blankingPolicy = AbstractDOInterrogate.NEED_NOTHING;
        fcd = new ItemDescriptor(controlClass, null, classClass,
            setControlMethod, null, getControlMethod, null, null, null, null, null, null,
            null, blankControlOperator, blankControlOperand, blankingPolicy, null,
            false, new MostlyEmptyMoreComplexMethods(), blueColor, null);
        fcds[16] = fcd;
        //
        //
        Err.pr(SdzNote.MDATE_ENTRY_FIELD, "TEXT HERE TO READ To use value or text property?");
        //Tried using Date (get/setValue()), but would have had to convert the different
        //vendors Dates, so then tried String (get/setText()). This even worse as setText() is a
        //NOP!! (The set text blank baulk highlighted this - its in the MDateEntryField
        //JavaDoc). Thus going back to Date.
        //Actually DOAdapter was too tight. Vendor date types extend from java.util.Date
        //so really no need to try and use a converter. If do need to re-tighten later then a Date to
        //Date converter will work fine.
        controlClass = mseries.ui.MDateEntryField.class;
        classClass = Date.class;
        args1[0] = classClass;
        /*
        try
        {
            setControlMethod = controlClass.getMethod("setValue", args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        */
        //See MoreComplexMethods subclass
        setControlMethod = null;
        try
        {
            setToolTipMethod = controlClass.getMethod("setToolTipText", argString);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        //Instead of "getValue()" using null to indicate that need to
        //use a special method in the MoreComplexMethods subclass
        getControlMethod = null;
        // public void setEnabled(boolean b)
        args1[0] = Boolean.TYPE;
        try
        {
            setEditableControlMethod = controlClass.getMethod(SET_CAN_CHANGE_CB,
                args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // public String isEnabled()
        args1[0] = null;
        try
        {
            getEditableControlMethod = controlClass.getMethod(IS_CAN_CHANGE_CB, (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // public void requestFocus()
        /*
        * The component we need to focus on is actually the display component
        * inside the MDateEntryField, which is of type MDateField. When this
        * was set up naively the user would need to tab twice to focus away.
        * Can't get to this method statically (controlClass.getMethod) so see
        * MDateEntryFieldMoreComplexMethods().
        */
        requestFocusMethod = null;
         /**/
//    blankControlOperator = FieldControlDescriptor.EQUALS_METHOD;
//    blankControlOperand = FieldControlDescriptor.NULL_STRING;
//    blankingPolicy = ControlInfo.NEED_PUT_NULL_OBJ_WRITE;
        blankControlOperator = ItemDescriptor.EQUALS_OPERATOR;
        blankControlOperand = ItemDescriptor.NULL_KEYWORD;
        blankingPolicy = AbstractDOInterrogate.NEED_NOTHING;
        fcd = new ItemDescriptor(controlClass, "mdef", classClass,
            setControlMethod, setToolTipMethod, getControlMethod, setEditableControlMethod,
            getEditableControlMethod, requestFocusMethod, null,
            null, null, null,
            blankControlOperator, blankControlOperand, blankingPolicy, null, false,
            new MDateEntryFieldMoreComplexMethods(), blueColor, "name");
        fcds[17] = fcd;
        //
        return fcds;
    }

    public TableControlDescriptor[] getTableControlDescriptors()
    {
        controlClass = javax.swing.JTable.class;
        /**
         * The following class will implement the model interface as required
         * by the vendor (eg. javax.swing.table.TableModel). It will
         * also implement the NodeTableMethods interface. The methods will be
         * called internally to set the data in TableModelImpl.
         */
        tableModelClass = org.strandz.core.info.impl.swing.JTableModelAttachedImpl.class;
        // public void setModel(TableModel newModel)
        args1[0] = javax.swing.table.TableModel.class; // an interface
        try
        {
            setModelMethod = controlClass.getMethod("setModel", args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // public TableModel getModel()
        args1[0] = null;
        try
        {
            getModelMethod = controlClass.getMethod("getModel", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // public void requestFocus()
        args1[0] = null;
        try
        {
            requestFocusMethod = controlClass.getMethod("requestFocusInWindow", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }

        TableControlDescriptor ccd = new
            TableControlDescriptor(controlClass, tableModelClass, setModelMethod,
            getModelMethod, requestFocusMethod, new JTableMethods(),
            false);
        ccds[0] = ccd;

        /******/
        controlClass = org.strandz.core.widgets.TableComp.class;
        tableModelClass = org.strandz.core.info.impl.swing.TableCompModelImpl.class;
        args1[0] = org.strandz.core.widgets.CompTableModelI.class; // an interface
        try
        {
            setModelMethod = controlClass.getMethod("setModel", args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        args1[0] = null;
        try
        {
            getModelMethod = controlClass.getMethod("getModel", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        ccd = new
            TableControlDescriptor(controlClass, tableModelClass, setModelMethod,
            getModelMethod, null, new TableCompColumnLister(),
            false);
        ccds[1] = ccd;
        /******/

        controlClass = org.strandz.lgpl.widgets.MyUnattachedJTable.class;
        /**
         * The following class will implement the model interface as required
         * by the vendor (eg. javax.swing.table.TableModel). It will
         * also implement the NodeTableMethods interface. The methods will be
         * called internally to set the data in TableModelImpl.
         */
        tableModelClass = org.strandz.core.info.impl.swing.JTableModelImpl.class;
        // public void setModel(TableModel newModel)
        args1[0] = javax.swing.table.TableModel.class; // an interface
        try
        {
            setModelMethod = controlClass.getMethod("setModel", args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // public TableModel getModel()
        args1[0] = null;
        try
        {
            getModelMethod = controlClass.getMethod("getModel", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // public void requestFocus()
        args1[0] = null;
        try
        {
            requestFocusMethod = controlClass.getMethod("requestFocusInWindow", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        ccd = new
            TableControlDescriptor(controlClass, tableModelClass, setModelMethod,
            getModelMethod, requestFocusMethod, new JTableMethods(),
            false);
        ccds[2] = ccd;

        controlClass = ComponentTableView.class;
        /**
         * The following class will implement the model interface as required
         * by the vendor (eg. javax.swing.table.TableModel). It will
         * also implement the NodeTableMethods interface. The methods will be
         * called internally to set the data in TableModelImpl.
         */
        tableModelClass = org.strandz.core.info.impl.swing.ComponentTableViewModelImpl.class;
        // public void setModel(TableModel newModel)
        args1[0] = javax.swing.table.TableModel.class; // an interface
        try
        {
            setModelMethod = controlClass.getMethod("setModel", args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // public TableModel getModel()
        args1[0] = null;
        try
        {
            getModelMethod = controlClass.getMethod("getModel", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // public void requestFocus()
        args1[0] = null;
        try
        {
            requestFocusMethod = controlClass.getMethod("requestFocusInWindow", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        ccd = new
            TableControlDescriptor(controlClass, tableModelClass, setModelMethod,
            getModelMethod, requestFocusMethod, new ComponentTableViewMethods(),
            false);
        ccds[3] = ccd;

        controlClass = javax.swing.JList.class;
        /**
         * The following class will implement the model interface as required
         * by the vendor (eg. javax.swing.table.TableModel). It will
         * also implement the NodeTableMethods interface. The methods will be
         * called internally to set the data in TableModelImpl.
         */
        tableModelClass = org.strandz.core.info.impl.swing.JListModelImpl.class;
        // public void setModel( ListModel model)
        args1[0] = javax.swing.ListModel.class; // an interface
        try
        {
            setModelMethod = controlClass.getMethod("setModel", args1);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // public ListModel getModel()
        args1[0] = null;
        try
        {
            getModelMethod = controlClass.getMethod("getModel", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        // public void requestFocus()
        args1[0] = null;
        try
        {
            requestFocusMethod = controlClass.getMethod("requestFocusInWindow", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        ccd = new TableControlDescriptor(controlClass, tableModelClass,
            setModelMethod, getModelMethod, requestFocusMethod,
            new JListColumnLister(), false);
        ccds[4] = ccd;

         /**/
        //
        /*
        * Normally JTextArea would not be thought of as a table.
        * MyJTextArea extends only in name. Thus in the special
        * case where user wants to treat it as a table use
        * ui.MyJTextArea
        */
        // controlClass = javax.swing.JTextArea.class;
        controlClass = org.strandz.lgpl.widgets.MyJTextArea.class;
        /**
         * The following class will implement the model interface as required
         * by the vendor (eg. javax.swing.text.Document). It will
         * also implement the NodeTableMethods interface.
         */
        tableModelClass = org.strandz.core.info.impl.swing.JTextAreaModelImpl.class;
        setModelMethod = null;
        getModelMethod = null;
        /*
        args1[0] = javax.swing.text.Document.class; //an interface
        try{
        setModelMethod = controlClass.getMethod("setDocument", args1);
        } catch (Exception ex) {
        Err.error("Missing method: " + ex);
        }
        args1[0] = null;
        try{
        getModelMethod = controlClass.getMethod("getDocument", null);
        } catch (Exception ex) {
        Err.error("Missing method: " + ex);
        }
        */

        args1[0] = null;
        try
        {
            requestFocusMethod = controlClass.getMethod("requestFocusInWindow", (Class[])null);
        }
        catch(Exception ex)
        {
            Err.error("Missing method: " + ex);
        }
        ccd = new TableControlDescriptor(controlClass, tableModelClass,
            setModelMethod, getModelMethod, requestFocusMethod,
            new JTextAreaColumnLister(), false);
        ccds[5] = ccd;
         /**/

        return ccds;
    }

    public boolean needToMonitorFocus()
    {
        return true;
    }

    public final void doStartupCode()
    {
        if(needToMonitorFocus() && focusMonitor == null)
        {
            //want to move to
            focusMonitor = new FocusMonitor(null);
            KeyboardFocusManager focusManager =
                KeyboardFocusManager.getCurrentKeyboardFocusManager();
            focusManager.addPropertyChangeListener(focusMonitor);
            //old way used ComponentMoreComplexMethods.MyFocusListener
        }
    }

    public FocusMonitorI getFocusMonitor()
    {
        FocusMonitorI result;
        if(focusMonitor == null)
        {
            //Err.error("Need to create the FocusMonitor here");
            doStartupCode();
        }
        result = focusMonitor;
        return result;
    }
}
