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
package org.strandz.core.interf;

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.IndentationCounter;
import org.strandz.lgpl.widgets.ApplicationPanel;
import org.strandz.lgpl.widgets.CursorIndentationCounter;
import org.strandz.view.util.AbstractStrandArea;
import org.strandz.core.domain.constants.OperationEnum;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class (or concrete versions of it) are Application Housings.
 * <p/>
 * Anything to do the 'Overall L&F' of the application is
 * specified in one of these subclasses.
 * <p/>
 * This is the abstract form of the high-level UI class that dictates
 * essentially how an application looks and feels outside of the area
 * upon which <code>AbstractVisibleStrand</code>s play. It contains a list of the
 * <code>AbstractVisibleStrand</code>s that the user can select, and manages their
 * visibility as they are selected.
 * <p/>
 * As well as being concerned with the UI this class references the
 * database that is used by the application.
 * <p/>
 * Whether the application looks like a set of tabs or a set of MDI windows
 * is determined by the subclass. Also which physical controls are used
 * to set a particular AbstractVisibleStrand in motion - menus/buttons are
 * the obvious examples. Anything to do the 'Overall L&F' - thus a JTree
 * driven Application could be written. Note that as well as writting an
 * Application subclass you would also need to write an equivalent
 * VisibleStrandHelper.
 *
 * @author Chris Murphy
 */
abstract public class Application
{
    protected String title;
    protected List<AbstractStrandAction> actionItems = new ArrayList<AbstractStrandAction>();
    // Don't have a last for pre/postForm(), have a cache instead
    VisibleStrandI last;
    protected ApplicationHelper applicationHelper; 
    protected ApplicationPanel panel = new ApplicationPanel(); // outermost panel
    private DataStore data;
    private IndentationCounter indentationCounter = CursorIndentationCounter.getInstance( OperationEnum.class);
    
    private static int times = 0;

    public Application()
    {
    }

    /**
     * Have a menu so can spark off any of many NodeTransactions
     */
    public Application(DataStore data)
    {
        // Err.error( "Whilst doing beanbox do not want to create an Application");
        this.data = data;
        //this.frame = frame;
        // is nicer to do this than to not, 'cause if user uses an
        // Application will not have to call himself

        // Now done at AbstractVisibleStrand granularity since need a different
        // type of parent componet for an MDIVisibleStrand:
        // MessageDlg.setFrame( frame);
    }

    public void setTitle(String s)
    {
        title = s;
    }

    public JPanel getEnclosingPanel()
    {
        return panel;
    }

    public abstract JComponent getEnclosure(VisibleStrandI actionableStrand);

    public abstract JComponent getStrandArea(VisibleStrandI actionableStrand);

    public abstract VisibleStrandHelper createStrandHelper( SdzBagI sbI, VisibleStrandI as, AbstractStrandArea.EnclosurePanel surface);
    
    public abstract VisibleStrandI getCurrentVisibleStrand();

    /**
     * @param cmd Comes from RClickTabPopupHelper
     */
    public abstract void removeDisplayedStrands( String cmd);
    
    public abstract boolean isDisplayed( VisibleStrandI visibleStrand);

    public abstract int getPosition( VisibleStrandI visibleStrand);
    
    public abstract void removeVisibleStrand( VisibleStrandI visibleStrand, String reason);
    
    public DataStore getDataStore()
    {
        return data;
    }
        
    /**
     * Would think that Application could be in control of its own surface
     * for writing on. Is not, however because the VisibleStrand...see subclass
     */
    public void addItem(AbstractStrandAction action)
    {
        // VisibleStrand visibleStrand = action.getVisibleStrand();
        /*
        * Was always setting to the wrong thing
        if(!action.isMultiplePanels())
        {
        times++;
        Err.pr( "Setting panel (Application) to " + panel.getName() +
        " for " + action + " times " + times);
        if(times == 0)
        {
        Err.stack();
        }
        visibleStrand.setPanelNodeTitle( panel, null, null);
        }
        */
        /**
         * Ought to set off background thread to do sdzInit. NO
         * Instead collect all the visibleStrands together
         * (ie actionItems already gives) and do fabInits as last
         * part of display()
         */
        // visibleStrand.sdzInit();
        // DONE AS SAID ABOVE
        // non-Swing stuff, when the concrete displayed
        // transaction is set to visible, will add the
        // UI to the LayeredPane, and then add the layeredpane to the panel.
        // For the menu style we must create a tabbed pane and then add it to
        // the enclosure. As the layered pane is important to the manipulator
        // style, the enclosed panel is to menu style.
        // keep as actions, 'cause have yet to add them to the menu
        actionItems.add(action);
        action.setApplication(this);
        // visibleStrand.setPartOfApplication( true);
        //this.vsa = action;
    }
    
    public List getItemsWithTitleContaining( String title)
    {
        List result = new ArrayList();
        List copyActionItems = new ArrayList( actionItems);
        for(Iterator iterator = copyActionItems.iterator(); iterator.hasNext();)
        {
            AbstractStrandAction action = (AbstractStrandAction) iterator.next();
            if(action.getTitle().contains( title))
            {
                result.add( action);
            }
        }
        return result;
    }
    
    public void removeAllItems( List items)
    {
        for(Iterator iterator = items.iterator(); iterator.hasNext();)
        {
            AbstractStrandAction action = (AbstractStrandAction) iterator.next();
            actionItems.remove( action);
        }
    }
    
    abstract public void removeAllItemsWithTitleContaining( String title);

    /**
     * For situation where use the same VisibleStrand, but
     * call its display using a different VisibleStrandAction
     * param. In this case we do not want to ignore (not invoke
     * display) a switch between the two.
     */
    private boolean namesNotEqual(
        VisibleStrandAction current, VisibleStrandAction last)
    {
        boolean result = false;
        if(last != null && current != null)
        {
            String lastName = (String) last.getValue(Action.NAME);
            String currentName = (String) current.getValue(Action.NAME);
            if(!lastName.equals(currentName))
            {
                result = true;
            }
        }
        return result;
    }

    /* No longer needed
    public boolean vetoChangeActionableStrand( ActionEvent e)
    {
      boolean result = false; //don't veto
      boolean keepGoing = true;
      if(last != null)
      {
        keepGoing = last.activateAsCurrent( false);
      }
      if(!keepGoing)
      {
        result = true; //meaning veto
      }
      Err.pr( SdzNote.tightenRecordValidation, "vetoChangeActionableStrand reting veto " + result);
      return result;
    }
    */
    
    /**
     * This is when user has chosen to change to a different Strand.
     */
    public void changeActionableStrand(VisibleStrandI visibleStrand, String reason)
    {
        // Err.pr("Action Performed - source is " + e.getSource());
        //VisibleStrandAction source = (VisibleStrandAction) e.getSource();
        //VisibleStrandI visibleStrand = source.getVisibleStrand();
        Err.pr(SdzNote.TIGHTEN_RECORD_VALIDATION, "Changing from " + last + " to " + visibleStrand);
        boolean keepGoing = true;
        //The functionality of 'you can't start me up (activate me) unless a certain condition
        //applies' has not yet been required in practise. The other way round, where you are
        //deactivating and you want to veto, is handled fine by select( false).
        //keepGoing = visibleStrand.activateAsCurrent( true);
        if(keepGoing)
        {
            if(last != null)
            {// Do this from the cache at exitNotification time
                // last.postForm();
                // 09/11/03 Removed so that hopefully will UN display only at
                // exitNotification time - checked and this what happens
                // Err.pr( "To UN-display last!! - " + last);
                // last.display( false, (String)source.getValue( AbstractAction.NAME));
            }
            if(!applicationHelper.hasAlreadyBeenDisplayed(visibleStrand, ApplicationHelper.APPLICATION))
            {
                visibleStrand.preForm();
                applicationHelper.displayedInApplicationForms.add(visibleStrand);
            }
            visibleStrand.display(true);
            // new MessageDlg("Message required for TAB!, Have set invisible for " + last);
            // even with this call don't get tab to re-appear!
            myInvalidate();
            last = visibleStrand;
            //lastSource = source;
            //The effect of this still happening, but will be from a menu
            //or something similar.
            //fireActionPerformed( e);
        }
        else
        {
            //in this situation with a tabbed for instance the surface would still
            //come up but the strand would not have put itself on the surface.
        }
    }
    
    public boolean exitVetoNotification()
    {
        boolean result = false; //don't veto
        /*
        if(last != null)
        {
        last.postForm();
        //not necessary, just neat!
        last.display( false);
        last = null;
        }
        */
        for(Iterator iter = applicationHelper.displayedInApplicationForms.iterator(); iter.hasNext();)
        {
            VisibleStrandI as = (VisibleStrandI) iter.next();
            result = !as.select(false, "Exiting the application notification");
            if(!result)
            {
                as.display(false);
            }
            else
            {
                break;
            }
        }
        if(!result) applicationHelper.displayedInApplicationForms = null;
        return result;
    }
    
    public void display()
    {
        display( actionItems);
    }

    public void display( List actionItems)
    {
        show();
        // Err.pr( " --- Could do all fabInits here");
        for(Iterator iter = actionItems.iterator(); iter.hasNext();)
        {
            AbstractStrandAction action = (AbstractStrandAction) iter.next();
            VisibleStrandI visibleStrand = action.getVisibleStrand();
//            times++;
//            Err.pr("  ++ to do sdzInit() for " + action + " times " + times);
//            if(times == 0)
//            {
//                Err.stack();
//            }
            if(visibleStrand != null)
            {
                visibleStrand.sdzInit();
            }
        }
    }

    /**
     * Will have been constructed with a JPanel that will
     * place itself onto.
     */
    abstract public void show();

    /**
     * I don't understand why this sort of thing doesn't work with swing,
     * so here is the hard core solution
     */
    public void myInvalidate()
    {
        getEnclosingPanel().revalidate(); // for JInternalFrame to appear
        getEnclosingPanel().repaint();
    }

    public void setDataStore(DataStore data)
    {
        this.data = data;
    }
    
    public void startActivity( Object id)
    {
        indentationCounter.indent( true, id);
    }
    
    public void endActivity( Object id)
    {
        indentationCounter.indent( false, id);
    }
    
    public List<VisibleStrandI> getVisibleStrands()
    {
        List<VisibleStrandI> result = new ArrayList<VisibleStrandI>();
        for(Iterator<AbstractStrandAction> iterator = actionItems.iterator(); iterator.hasNext();)
        {
            AbstractStrandAction abstractStrandAction = iterator.next();
            result.add( abstractStrandAction.getVisibleStrand());
        }     
        return result;
    }

    public ApplicationHelper getApplicationHelper()
    {
        return applicationHelper;
    }
}
