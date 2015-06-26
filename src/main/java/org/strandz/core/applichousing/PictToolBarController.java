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
package org.strandz.core.applichousing;

import org.strandz.core.domain.constants.CapabilityEnum;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.event.ControlActionListener;
import org.strandz.core.domain.event.DynamicAllowedEvent;
import org.strandz.core.domain.event.InputControllerEvent;
import org.strandz.core.domain.exception.ApplicationError;
import org.strandz.core.interf.AbstStrand;
import org.strandz.core.interf.ActualNodeControllerI;
import org.strandz.core.interf.StrandI;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.widgets.PictToolBar;

import javax.swing.Action;
import javax.swing.JButton;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * When want to make so that delete is visually unenabled,
 * rather than visually hidden, then get/setAllowed should
 * reflect real reason eg./ setAllowedInsertedRec() and
 * getAllowedInsertedRec(). (Have ints instead to show
 * mutual exclusivity). NON_INSERTED_REC, INSERTED_REC. Thus
 * any NodeController can decide its own response.
 */
public class PictToolBarController extends PictToolBar
    implements ActualNodeControllerI
{
    private static final OperationEnum[] BUTTONS = {
        OperationEnum.ENTER_QUERY, OperationEnum.EXECUTE_QUERY,
        OperationEnum.EXECUTE_SEARCH, OperationEnum.INSERT_AFTER_PLACE, OperationEnum.REMOVE,
        OperationEnum.POST, OperationEnum.PREVIOUS, OperationEnum.NEXT,
        OperationEnum.COMMIT_RELOAD,
    };
    private Map buttons = new HashMap(BUTTONS.length);
    private MyButton bEnter;
    private MyButton bQuery;
    private MyButton bSearch;
    private MyButton bInsert;
    private MyButton bDelete;
    private MyButton bPost;
    private MyButton bPrevious;
    private MyButton bNext;
    // Don't intend to use from a physical controller
    // private MyButton bCommitOnly;
    private MyButton bCommitReload;
    private ArrayList abilities;
    private AllActionListener allActionListener;
    private ArrayList controlActionListenerList = new ArrayList();
    private int howDisplayNotAllowed = VisualUtils.GRAYED;
    private List permanentlyDisabledButtons = new ArrayList();
    private static int times;

    public PictToolBarController()
    {
        super("org.strandz.core.applichousing.PictToolBarController");
        if(SdzNote.NO_HOUSING_HELPERS.isVisible())
        {
            //Err.stack();
        }
        for(int i = 0; i <= BUTTONS.length - 1; i++)
        {
            buttons.put(BUTTONS[i], new MyButton(BUTTONS[i], getId()));
        }
        bEnter = (MyButton) buttons.get(OperationEnum.ENTER_QUERY);
        bQuery = (MyButton) buttons.get(OperationEnum.EXECUTE_QUERY);
        bSearch = (MyButton) buttons.get(OperationEnum.EXECUTE_SEARCH);
        bInsert = (MyButton) buttons.get(OperationEnum.INSERT_AFTER_PLACE);
        bDelete = (MyButton) buttons.get(OperationEnum.REMOVE);
        bPost = (MyButton) buttons.get(OperationEnum.POST);
        bPrevious = (MyButton) buttons.get(OperationEnum.PREVIOUS);
        bNext = (MyButton) buttons.get(OperationEnum.NEXT);
        bCommitReload = (MyButton) buttons.get(OperationEnum.COMMIT_RELOAD);
        bEnter.setMnemonic(KeyEvent.VK_E);
        bQuery.setMnemonic(KeyEvent.VK_Q);
        bSearch.setMnemonic(KeyEvent.VK_S);
        bInsert.setMnemonic(KeyEvent.VK_I);
        bDelete.setMnemonic(KeyEvent.VK_D);
        bPost.setMnemonic(KeyEvent.VK_O);
        bPrevious.setMnemonic(KeyEvent.VK_P);
        bNext.setMnemonic(KeyEvent.VK_N);
        bCommitReload.setMnemonic(KeyEvent.VK_C);
        allActionListener = new AllActionListener();
        addTool(bEnter);
        bEnter.addActionListener(allActionListener);
        addTool(bQuery);
        bQuery.addActionListener(allActionListener);
        addTool(bSearch);
        bSearch.addActionListener(allActionListener);
        addTool(bInsert);
        bInsert.addActionListener(allActionListener);
        addTool(bDelete);
        bDelete.addActionListener(allActionListener);
        addTool(bPost);
        bPost.addActionListener(allActionListener);
        addTool(bPrevious);
        bPrevious.addActionListener(allActionListener);
        addTool(bNext);
        bNext.addActionListener(allActionListener);
        addSeparator();
        addTool(bCommitReload);
        bCommitReload.addActionListener(allActionListener);
        setName("Standard PictToolBarController");
    }

    public PictToolBarController(String name)
    {
        super(name);
    }

    //debug
    public void setVisible(boolean b)
    {
        Err.pr("PictToolBarController VISIBLE: " + b);
    }

    public void removeAllControlActionListeners()
    {
        controlActionListenerList.clear();
    }

    public void addControlActionListener(ControlActionListener l)
    {
        controlActionListenerList.add(l);
    }

    public void removeControlActionListener(ActualNodeControllerI al)
    {
        controlActionListenerList.remove(al);
    }

    /**
     * Perhaps this default action s/be done by caller
     */
    public void strandChanged(StrandI strand)
    {// strand.removeAllNodeChangeListeners();
    }

    public void setAbilities(List abilities)
    {
        times++;
        Err.pr(SdzNote.GENERIC, "=========setAbilities called with " + abilities + " times " + times);
        if(times == 0)
        {
            Err.stack();
        }
        if(abilities != null && !abilities.isEmpty())
        {
            if(this.abilities != null)
            {
                for(Iterator en = this.abilities.iterator(); en.hasNext();)
                {
                    Component c = (Component) en.next();
                    remove(c);
                }
                this.abilities.clear();
            }
            else
            {
                this.abilities = new ArrayList();
            }
            for(Iterator en = abilities.iterator(); en.hasNext();)
            {
                // Err.pr( "To add an Action Ability");
                Object obj = en.next();
                Action a = null;
                JButton tool = null;
                if(obj instanceof Action)
                {
                    a = (Action) obj;
                    tool = addTool(a);
                }
                else if(obj instanceof JButton)
                {
                    Err.pr( SdzNote.ACTIONS_REQUIRED_ON_TOOL_BAR, "Abilities must be set as AbstractActions - so error here");
                }
                tool.setName((String) a.getValue(Action.NAME));
                this.abilities.add(tool);
                a.addPropertyChangeListener(new ActionChangedListener(tool));
                // Err.pr( "Now have " + this.abilities.size());
            }
        }
        else
        {
            // Err.pr( "Need remove all abilities");
            if(this.abilities != null)
            {
                for(Iterator en = this.abilities.iterator(); en.hasNext();)
                {
                    JButton b = (JButton) en.next();
                    // Err.pr( "To remove " + c);
                    removeTool(b);
                }
                this.abilities = null;
            }
        }
    }

    private static class ActionChangedListener implements PropertyChangeListener
    {
        JButton b;

        ActionChangedListener(JButton b)
        {
            super();
            this.b = b;
        }

        /**
         * Copied from Manipulator.java, which copied from JMenu!
         */
        public void propertyChange(PropertyChangeEvent e)
        {
            String propertyName = e.getPropertyName();
            if(propertyName.equals(Action.NAME))
            {
                String text = (String) e.getNewValue();
                b.setText(text);
            }
            else if(propertyName.equals("enabled"))
            {
                Boolean enabledState = (Boolean) e.getNewValue();
                b.setEnabled(enabledState.booleanValue());
            }
        }
    }

    public void setHowDisplayNotAllowed(int how)
    {
        /**
         * Need to un-do the old way
         */
        for(int i = 0; i < BUTTONS.length; i++)
        {
            MyButton button = (MyButton) buttons.get(BUTTONS[i]);
            setEnabled(button, !isEnabled(button));
        }
        howDisplayNotAllowed = how;
        /**
         * Need to do so that all buttons will become displayed according
         * to the new way.
         */
        for(int i = 0; i < BUTTONS.length; i++)
        {
            MyButton button = (MyButton) buttons.get(BUTTONS[i]);
            setEnabled(button, isEnabled(button));
        }
    }

    public int getHowDisplayNotAllowed()
    {
        return howDisplayNotAllowed;
    }

    private void setEnabled(MyButton button, boolean enabled)
    {
        /*
        if(button == bNext)
        {
        times++;
        if(enabled == false)
        {
        Err.pr( "bNext enabled OFF " + times + " for " + id);
        }
        else
        {
        Err.pr( "bNext enabled ON " + times + " for " + id);
        }
        if(times == 0)
        {
        Err.stack();
        }
        }
        */
        if(!permanentlyDisabledButtons.contains( button))
        {
            if(button != null)
            {
                if(enabled == true && button.isDeleted())
                {
                    // Err.pr( "Why not allowing REMOVE button to be enabled in PctToolBarController");
                    return;
                }
                if(howDisplayNotAllowed == VisualUtils.INVISIBLE)
                {
                    // Err.pr( "button " + button + " being set to visible " + enabled);
                    button.setVisible(enabled);
                }
                else if(howDisplayNotAllowed == VisualUtils.GRAYED)
                {
                    /*
                    times++;
                    Err.pr( "%%% button " + button + " being set to enabled " + enabled +
                    " times " + times);
                    if(times == 0)
                    {
                    Err.stack();
                    }
                    */
                    button.setEnabled(enabled);
                }
                else
                {
                    Err.error("howDisplayNotAllowed property not correctly set");
                }
            }
        }
    }

    private boolean isEnabled(MyButton button)
    {
        boolean result = false;
        if(howDisplayNotAllowed == VisualUtils.INVISIBLE)
        {
            result = button.isVisible();
        }
        else if(howDisplayNotAllowed == VisualUtils.GRAYED)
        {
            result = button.isEnabled();
        }
        else
        {
            Err.error("howDisplayNotAllowed property not correctly set");
        }
        return result;
    }

     /**/

    public void deleteTool(OperationEnum id)
    {
        MyButton b = (MyButton) buttons.get(id);
        if(b != null)
        {
            b.setVisible(false);
            b.setDeleted(true);
        }
    }

    /**
     * Once disabled can't be enabled gain
     * @param id
     */
    public void disableTool(OperationEnum id)
    {
        MyButton b = (MyButton) buttons.get(id);
        if(b != null)
        {
            setEnabled(b, false);
            if(!permanentlyDisabledButtons.contains( b))
            {
                permanentlyDisabledButtons.add( b);
            }
        }
    }
    
    private boolean operationSupported(CapabilityEnum id)
    {
        boolean result = true;
        if(id == OperationEnum.SET_ROW || id == OperationEnum.GOTO_NODE
            || id == OperationEnum.GOTO_NODE_IGNORE || id == OperationEnum.SET_VALUE)
        {
            result = false;
        }
        return result;
    }

    public void allowedPerformed(DynamicAllowedEvent event)
    {
        CapabilityEnum id = event.getID();
        if(operationSupported(id))
        {
            if(event.getDynamicAllowed() == true)
            {
                setEnabled((MyButton) buttons.get(id), true);
            }
            else
            {
                setEnabled((MyButton) buttons.get(id), false);
            }
        }
    }

    class AllActionListener implements ActionListener,
        Serializable
    {
        public void actionPerformed(ActionEvent evt)
        {
            // Err.pr("menu event happened: " + evt.getActionCommand());
            InputControllerEvent ce = null;
            for(int i = 0; i < OperationEnum.USUALLY_VISIBLE_OPERATIONS.length; i++)
            {
                OperationEnum enumId = OperationEnum.USUALLY_VISIBLE_OPERATIONS[i];
                if(enumId == null)
                {
                    Err.error("No OperationEnum at " + i);
                }

                String displayName = enumId.getName();
                if(displayName == null)
                {
                    Err.error(
                        "No displayName for "
                            + OperationEnum.USUALLY_VISIBLE_OPERATIONS[i]);
                }
                // Err.pr( "To cf " + displayName + " with " + evt.getActionCommand());
                if(displayName.equals(evt.getActionCommand()))
                {
                    ce = new InputControllerEvent(this,
                        OperationEnum.USUALLY_VISIBLE_OPERATIONS[i]);
                }
            }
            if(ce == null)
            {
                Err.error("Non-menu event happened: " + evt.getActionCommand());
            }
            try
            {
                if(controlActionListenerList.size() == 0)
                {
                    Err.error("PictToolBarController can't fire to anyone");
                }
                for(Iterator en = controlActionListenerList.iterator(); en.hasNext();)
                {
                    ControlActionListener l = (ControlActionListener) en.next();
                    l.execute(ce);
                }
            }
            catch(ApplicationError ae)
            {
                Print.pr(
                    "\tcaught ApplicationError for key command "
                        + evt.getActionCommand());
                Err.error(
                    "caught ApplicationError for key command " + evt.getActionCommand());
                // raised to completely abort what was doing
            }
        }
    } // end AllActionListener class


    private static class MyButton extends PictToolBar.DebugButton
    {
        /**
         * If a button is deleted then it will never physically be shown.
         * This is different to enabled (which translates to enabled/visible
         * for MyButton), which is controlled from a capability of a Node.
         * Thus can have 'programmatic only' capabilities.
         */
        private boolean deleted = false;

        MyButton( OperationEnum enumId, int toolBarId)
        {
            super(enumId.getName(), toolBarId);
        }

        MyButton( int toolBarId)
        {
            super( toolBarId);
        }

        public boolean isDeleted()
        {
            return deleted;
        }

        public void setDeleted(boolean b)
        {
            deleted = b;
        }

        public String toString()
        {
            return getText();
        }
    }

    /*
    * As determined by using a different RepaintManager in the JVM,
    * these calls causing problems.
    */
    /*
    public boolean equals( Object o)
    {
    Utils.chkType( o, this.getClass());

    boolean result = true;
    if(o == this)
    {
    result = true;
    }
    else if(!(o instanceof PictToolBarController))
    {
    //Nulls come through here
    //Err.stack( "o actually an instanceof " + o.getClass().getName());
    result = false;
    }
    else
    {
    result = false;

    PictToolBarController test = (PictToolBarController)o;
    if(Utils.equals(abilities, test.abilities))
    {
    if(ComponentUtils.mapComponentsEqual(buttons, test.buttons)) //All components just go to the default hashcode
    {
    result = true;
    }
    else
    {
    //debug reason diff
    Err.pr( "---------buttons REASON");
    Print.prMap( buttons);
    Print.prMap( test.buttons);
    Err.pr( "---------END REASON");
    }
    }
    else
    {
    //debug reason diff
    Err.pr( "---------abilities REASON");
    Print.prList( abilities, "");
    Print.prList( test.abilities, "");
    Err.pr( "---------END REASON");
    }
    }
    return result;
    }

    public int hashCode()
    {
    int result = 17;
    result = 37 * result + (abilities == null ? 0 : abilities.hashCode());
    result = 37 * result + ComponentUtils.mapComponentsHashCode( buttons);
    return result;
    }
    */
}
