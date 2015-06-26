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

import org.strandz.lgpl.tablelayout.ModernTableLayout;
import org.strandz.core.interf.AbstractStrandAction;
import org.strandz.core.interf.Application;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.SdzBagIHelper;
import org.strandz.core.interf.VisibleStrandAction;
import org.strandz.core.interf.VisibleStrandHelper;
import org.strandz.core.interf.VisibleStrandI;
import org.strandz.core.interf.ApplicationHelper;
import org.strandz.data.wombatrescue.business.RosterSessionUtils;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.ComponentUtils;
import org.strandz.view.util.AbstractStrandArea;
import org.strandz.view.util.StrandArea;
import org.strandz.view.util.UnadornedStrandArea;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Could make extend or own SimpleApplication, as SimpleApplication
// is a simpler version of this application.

public class MenuTabApplication extends Application implements RClickTabPopupHelper.RightClickListener
{
    private JTabbedPane tabbedPane = new JTabbedPane();
    private final TitlePanel titlePanel = new TitlePanel();
    // Different titles across the menu bar
    private List menuBars = new ArrayList();
    private List<Tab> tabs = new ArrayList<Tab>();
    private Tab selectedTab;
    private boolean causedByActionPerformed = false;
    private TabChangeListener tabListener = new TabChangeListener();
    
    private final static int MENU_BAR_HEIGHT = 20;
    private final static int TITLE_PANEL_HEIGHT = 25;    
//    private final static Dimension vpad5 = new Dimension(1, 5);
//    private final static Border loweredBorder = new SoftBevelBorder(
//        BevelBorder.LOWERED);
    private static int times = 0;
    
    private static class MenuBar
    {
        String title;
        char mnemonic;
        List actions = new ArrayList();
        List subMenus = new ArrayList();

        private void addAction(Action action)
        {
            actions.add(action);
        }

        private void removeAction(Action action)
        {
            actions.remove(action);
        }
        
        private void addSubMenuBar(MenuBar menuBar)
        {
            subMenus.add( menuBar);
        }
        
        private void removeSubMenuBar(MenuBar menuBar)
        {
            subMenus.remove( menuBar);
        }
    }

    private static class Tab
    {
        // Use getVisibleStrand() when need an VisibleStrand
        private AbstractStrandAction action;
        private AbstractStrandArea strandArea;

        private Tab(AbstractStrandAction action, AbstractStrandArea strandArea)
        {
            strandArea.init();
            this.strandArea = strandArea;
            this.action = action;
            // Err.pr( "Created tab for " + action);
        }
        
        public String toString()
        {
            String result = action.getVisibleStrand().getClass().getName();
            return result;
        }
    }
    
    private boolean removeTab( Component selected, String reason)
    {
        boolean result = true;
        Tab tab = getTabByComponent( selected);
        AbstractStrandAction strandAction = tab.action;
        boolean unSelectOk = strandAction.getVisibleStrand().select( false, reason);
        if(unSelectOk)
        {
            strandAction.getVisibleStrand().display(false);
            tabbedPane.removeChangeListener(tabListener);
            tabbedPane.remove(selected);
            Err.pr( SdzNote.NO_TABS_THEN_BACK, "Have removed " + tabbedPane.getName() + "(" + tabbedPane.getClass().getName() +  ")" + 
                    " from " + selected.getName() + "(" + selected.getClass().getName() +  ")");
            tabbedPane.addChangeListener(tabListener);
            VisibleStrandI visibleStrand = strandAction.getVisibleStrand();
            boolean ok = applicationHelper.displayedInApplicationForms.remove( visibleStrand);
            if(!ok)
            {
                Print.prList( applicationHelper.displayedInApplicationForms, "One removing was supposed to be in here");
                Err.pr( "Was not able to remove " + selected);
                result = false;
            }
            else
            {
                visibleStrand.detachControls();
            }
            if(tabbedPane.getComponentCount() == 0)
            {
                Err.pr( SdzNote.NO_TABS_THEN_BACK, "Setting selectedTab = null as have removed the last tab");
                selectedTab = null;    
            }
        }
        else
        {
            result = false;
        }
        return result;
    }
    
    public boolean isDisplayed( VisibleStrandI visibleStrand)
    {
        boolean result = false;
        Component comps[] = tabbedPane.getComponents();
        JComponent strandArea = getStrandArea( visibleStrand);
        for(int i = 0; i < comps.length; i++)
        {
            Component comp = comps[i];
            if(comp == strandArea)
            {
                result = true;
                break;
            }
        }        
        return result;
    }
    
    public int getPosition( VisibleStrandI visibleStrand)
    {
        int result = -1;
        if(applicationHelper.hasAlreadyBeenDisplayed( visibleStrand, ApplicationHelper.APPLICATION))
        {
            JComponent component = getStrandArea( visibleStrand);
            result = ComponentUtils.getIndexOf( tabbedPane, component);
        }
        return result;
    }

    /**
     * Note that what is removed might not be currently displayed (it just has been displayed in the past)
     * Removal means physical removal from the tabbed pane plus other things that need to be done for the
     * integrity of Strandz.
     */
    public void removeVisibleStrand( VisibleStrandI visibleStrand, String reason)
    {
        if(applicationHelper.hasAlreadyBeenDisplayed( visibleStrand, ApplicationHelper.APPLICATION))
        {
            JComponent component = getStrandArea( visibleStrand);
            removeTab( component ,reason);
        }
    }

    public void removeDisplayedStrands(String cmd)
    {
        if(cmd.equals(RClickTabPopupHelper.CLOSE))
        {
            removeTab( tabbedPane.getSelectedComponent(), "Right Click Close");
        }
        else if(cmd.equals(RClickTabPopupHelper.CLOSE_ALL))
        {
            Component comps[] = tabbedPane.getComponents();
            for(int i = 0; i < comps.length; i++)
            {
                Component comp = comps[i];
                if(!removeTab( comp, "Right Click Close All"))
                {
                    break;
                }
            }
        }
        else if(cmd.equals(RClickTabPopupHelper.CLOSE_ALL_BUT_SELECTED))
        {
            Component comps[] = tabbedPane.getComponents();
            Component selected = tabbedPane.getSelectedComponent();
            for(int i = 0; i < comps.length; i++)
            {
                Component comp = comps[i];
                if(comp != selected)
                {
                    if(!removeTab( comp, "Right Click Close All but Selected"))
                    {
                        break;
                    }
                }
            }
        }
        else
        {
            Err.error("Unknown cmd: " + cmd);
        }
    }

    private class TabChangeListener implements ChangeListener
    {
        public void stateChanged(ChangeEvent ev)
        {
            AbstractStrandArea selected = (AbstractStrandArea) tabbedPane.getSelectedComponent();
            Err.pr( SdzNote.NO_TABS_THEN_BACK, "TabChangeListener has picked up that have gone to " + selected);
            if(selected == null)
            {// selected = selectedTab.strandArea;
            }
            else
            {
                Tab tab = getTabByComponent(selected);
                String title = (String) tab.action.getValue(
                    AbstractAction.NAME);
                // Err.pr( "Have selected " + title);
                boolean lastStrandOk = true;
                if(!causedByActionPerformed && selectedTab != null)
                {
                    lastStrandOk = selectedTab.action.getVisibleStrand().select(false, "User tabbed away from");
                }
                if(lastStrandOk)
                {
                    setCurrentItemTitle(title);
                    tab.action.getVisibleStrand().select(true, "User tabbed to");
                    selectedTab = tab;
                    times++;
                    Err.pr( SdzNote.LOVS_CHANGE_DATA_SET, "Selected tab changed to " + selectedTab + " times " + times);
                    if(times == 0)
                    {
                        Err.stack();
                    }
                }
                else //Strandz hasn't gone to the next tab, but the tabbedpane has!
                {
                    //Unfortunately with tabs we are in an 'after the event' situation. Thus
                    //we will need to force the tab back to the previous one, and not be
                    //listening in here as we do so.
                    tabbedPane.removeChangeListener(this);
                    tabbedPane.setSelectedIndex(tabbedPane.indexOfTab((String) selectedTab.action.getValue(
                        AbstractAction.NAME)));
                    tabbedPane.addChangeListener(this);
                }
            }
        }
    }

    public MenuTabApplication(DataStore dataStore)
    {
        super(dataStore);
        applicationHelper = new ApplicationHelper( true);        
        new RClickTabPopupHelper(tabbedPane, this);
        Border empty = BorderFactory.createEmptyBorder();
        tabbedPane.setBorder(empty);

        tabbedPane.addChangeListener(tabListener);
        titlePanel.setName("titlePanel");

        // Create a TableLayout for the panel
        double size[][] = // Columns
            {{
                ModernTableLayout.FILL,
            }, // Rows
                {MENU_BAR_HEIGHT, TITLE_PANEL_HEIGHT, ModernTableLayout.FILL
                }};
        // Changed from BorderLayout
        panel.setLayout(new ModernTableLayout(size));
    }

    public void setTitle(String s)
    {
        Err.error(
            "Do not call setTitle( String s), but addTitle( String s, char mnemonic)");
    }

    public void addTitle(String s, char mnemonic)
    {
        MenuBar mb = new MenuBar();
        mb.title = s;
        mb.mnemonic = mnemonic;
        menuBars.add(mb);
    }
    
    public void addSubTitle(String title, String subTitle, char mnemonic)
    {
        MenuBar mb = null;
        for(Iterator iterator = menuBars.iterator(); iterator.hasNext();)
        {
            MenuBar menuBar = (MenuBar) iterator.next();
            if(menuBar.title.equals( title))
            {
                mb = menuBar;
                break;
            }
        }
        if(mb == null)
        {
            Err.error( "Must have a title before can add a sub-title");
        }
        else
        {
            MenuBar subMB = new MenuBar();
            subMB.title = subTitle;
            subMB.mnemonic = mnemonic;
            mb.addSubMenuBar( subMB);
        }
    }

    /**
     * This is when change to a different Strand, not when change to
     * a different tab. (A Strand often being made up of multiple tabs).
     * (There are two sets of tabs, a set for a strand and a set for an
     * application)
     * The user has selected a different VisibleStrand on the menu.
     * The associated application tab may or may not yet exist.
     */
    public void changeActionableStrand(VisibleStrandI visibleStrand, String reason)
    {
        boolean veto = false;
        if(selectedTab != null)
        {
            veto = !selectedTab.action.getVisibleStrand().select(false, reason);
            Err.pr( SdzNote.NO_TABS_THEN_BACK, "selectedTab = null where used to be " + selectedTab);
            selectedTab = null;            
        }
        //boolean veto = vetoChangeActionableStrand( e);
        if(!veto)
        {
            //VisibleStrandAction source = (VisibleStrandAction) e.getSource();
            //VisibleStrandI visibleStrand = source.getVisibleStrand();
            //This is the application tab:
            Tab tab = getTabByVisibleStrand(visibleStrand);
            /*
            * As each has own StrandArea, do not need to
            * remove and add, as did when they all shared
            * the same StrandArea
            */
            //String visibleStrandTitle = (String) source.getValue(Action.NAME);
            if(visibleStrand.getNodeController() != null)
            {
                // Will only remove contentPanel
                // tab.strandArea.removeAll();
                // tab.strandArea.add( tab.savedContent, BorderLayout.CENTER);
                // tab.strandArea.getEnclosure().removeAll();
                if(StrandArea.USE_TOOL_BAR_AREA)
                {
                    tab.strandArea.getToolBarArea().refitArea(
                        (JComponent) visibleStrand.getNodeController().getPhysicalController());
                }
                else
                {
                    tab.strandArea.getToolBarPane().refitControllerArea(
                        (JComponent) visibleStrand.getNodeController().getPhysicalController());
                }
            }
            else
            {
                // Will only remove contentPanel
                // tab.strandArea.removeAll();
                if(StrandArea.USE_TOOL_BAR_AREA)
                {
                    tab.strandArea.getToolBarArea().refitArea(null);
                }
                else
                {
                    tab.strandArea.getToolBarPane().refitControllerArea(null);
                }
            }
            // This calls display on the strand
            super.changeActionableStrand(visibleStrand, reason);
            // do here to make sure that select occurs after display:
            // tabbedPane.remove( tab.strandArea);
            causedByActionPerformed = true;
            if(tabbedPane.indexOfComponent(tab.strandArea) == -1)
            {
                // Err.pr( "Title for TAB: " + visibleStrandTitle);
                tabbedPane.addTab(visibleStrand.getTitle(), tab.strandArea);
                tabbedPane.setSelectedComponent(tab.strandArea);
            }
            else
            {
                //
                tabbedPane.setSelectedComponent(tab.strandArea);
            }
            causedByActionPerformed = false;
        }
    }

    // called within display on the Strand
    public JComponent getEnclosure( VisibleStrandI actionableStrand)
    {
        AbstractStrandArea strandArea = getTabByVisibleStrand(actionableStrand).strandArea;
        Assert.notNull( strandArea);
        return strandArea.getEnclosure();
    }

    // called within display on the Strand
    public JComponent getStrandArea( VisibleStrandI visibleStrand)
    {
        return getTabByVisibleStrand( visibleStrand).strandArea;
    }

    /**
     * This is the title of the currently selected menu item
     */
    public void setCurrentItemTitle(String s)
    {
        titlePanel.setTitle(s);
    }

    /**
     * Will have been constructed with a JPanel that will
     * place itself onto.
     */
    public void show()
    {
        panel.removeAll();
        JMenuBar menuBar = new JMenuBar();
        String liveValue = RosterSessionUtils.getProperty( "live", true); 
        Color color;
        if(liveValue != null && liveValue.equals( "true"))
        {
            color = Color.RED;
        }
        else
        {
            color = Color.GREEN;
        }
        menuBar.setBackground( color);
        menuBar.setName("menuBar");
        for(Iterator iter = menuBars.iterator(); iter.hasNext();)
        {
            MenuBar mb = (MenuBar) iter.next();
            JMenu newMenu = new JMenu(mb.title);
            newMenu.setBackground( color);
            JMenu mainMenu = menuBar.add( newMenu);
            // mainMenu.setKeyAccelerator('i');
            mainMenu.setMnemonic(mb.mnemonic);

            Action newAction;
            for(Iterator it1 = mb.actions.iterator(); it1.hasNext();)
            {
                newAction = (Action) it1.next();
                JMenuItem newItem = mainMenu.add(newAction);
                newItem.setHorizontalTextPosition(JButton.RIGHT);
                // newItem.setKeyAccelerator('N');
            }
            for(Iterator it1 = mb.subMenus.iterator(); it1.hasNext();)
            {
                MenuBar subMB = (MenuBar) it1.next();
                if(subMB.actions.isEmpty())
                {
                    Err.pr( "Expect a submenu to have at least one action");
                }
                JMenu subMenu = (JMenu)mainMenu.add(new JMenu( subMB.title));
                for(Iterator it2 = subMB.actions.iterator(); it2.hasNext();)
                {
                    newAction = (AbstractStrandAction) it2.next();
                    JMenuItem newItem = subMenu.add(newAction);
                    newItem.setHorizontalTextPosition(JButton.RIGHT);
                }
            }
        }
        panel.add(menuBar, "0, 0");
        panel.add(titlePanel, "0, 1");
        panel.add(tabbedPane, "0, 2");
        panel.revalidate();
    }
    
    public VisibleStrandHelper createStrandHelper(SdzBagI sbI, VisibleStrandI as, AbstractStrandArea.EnclosurePanel surface)
    {
        SBIStatusBarHelper barHelper = null;
        SdzBagIHelper helper = null;
        if(sbI instanceof SdzBag)
        {
            barHelper = ((SdzBag) sbI).getBarHelper();
            helper = ((SdzBag) sbI).getHelper();
        }
        else if(sbI instanceof UnadornedSdzBag)
        {
            helper = ((UnadornedSdzBag) sbI).getHelper();
        }
        else
        {
            Err.error( "Not support " + sbI.getClass());
        }
        VisibleStrandHelper result = new TabVisibleStrandHelper(
            barHelper, sbI.getStrand(), this,
            surface
            //getSdzSurface(as)
        );
        result.setNodeController( helper.nodeController);
        return result;
    }
    
    public VisibleStrandI getCurrentVisibleStrand()
    {
        return selectedTab.action.getVisibleStrand(); 
    }

    /**
     * For JPanel the default layout is a FlowLayout. If you plonk one
     * thing into this layout then it will appear in the center.
     */
    class TitlePanel extends JPanel
    {
        private final Font boldFont = new Font("Dialog", Font.BOLD, 14);
        JLabel label = new JLabel();

        TitlePanel()
        {
            label.setFont(boldFont);
            /*
            * W/out this we get a LHS margin appearing.
            * (Don't know why).
            */
            setMinimumSize(new Dimension(super.getMinimumSize().width, 25));
            /*
            * Makes sense that a BoxLayout going down the
            * page will take notice of the height of a
            * JPanel that is added to it.
            */
            setPreferredSize(new Dimension(super.getPreferredSize().width, 25));
        }

        void setTitle(String title)
        {
            label.setText(title);
            removeAll();
            add(label);
        }
    }

    private void addRemoveFromMenu(AbstractStrandAction action, boolean remove)
    {
        String menuBarTitle = (String) action.getValue(VisibleStrandAction.MASTER_MENU_TITLE);
        if(Utils.isBlank( menuBarTitle))
        {
            Err.error( "No MASTER_MENU_TITLE attribute on action <" + action + "> of type " + action.getClass().getName());
        }
        boolean found = false;
        List<String> titlesSearchedThru = new ArrayList<String>(); //Debugging code
        for(Iterator iter = menuBars.iterator(); iter.hasNext();)
        {
            MenuBar menuBar = (MenuBar) iter.next();
            if(menuBar.title.equals(menuBarTitle))
            {
                found = true;
                titlesSearchedThru.add( menuBar.title);
                if(!remove)
                {
                    menuBar.addAction(action);
                }
                else
                {
                    menuBar.removeAction(action);    
                }
                break;
            }
            else
            {
                titlesSearchedThru.add( menuBar.title);
            }
            for(Iterator iterator = menuBar.subMenus.iterator(); iterator.hasNext();)
            {
                MenuBar bar = (MenuBar) iterator.next();
                Err.pr( SdzNote.SUB_MENU, "Looking at submenu titled <" + bar.title + "> to see if matches with <" + menuBarTitle + ">");
                if(bar.title.equals(menuBarTitle))
                {
                    found = true;
                    titlesSearchedThru.add( bar.title);
                    if(!remove)
                    {
                        bar.addAction(action);
                    }
                    else
                    {
                        bar.removeAction(action);
                    }
                    break;
                }
                else
                {
                    titlesSearchedThru.add( bar.title);
                }
            }
        }
        if(!found)
        {
            if(titlesSearchedThru.isEmpty())
            {
                Err.pr( "No menus have been specified, you need to call addTitle() at least once");
            }
            Print.prList(titlesSearchedThru, "titlesSearchedThru had to choose from");
            Err.error("VisibleStrandAction does not know about menu title: <" + menuBarTitle + ">, which is meant to be title to use for action <" + action + ">");
        }
    }
    
    private void addRemoveFromMenu(Action action, boolean remove)
    {
        String menuBarTitle = (String) action.getValue(VisibleStrandAction.MASTER_MENU_TITLE);
        if(Utils.isBlank( menuBarTitle))
        {
            Err.error( "No MASTER_MENU_TITLE attribute on action <" + action + "> of type " + action.getClass().getName());
        }
        boolean found = false;
        for(Iterator iter = menuBars.iterator(); iter.hasNext();)
        {
            MenuBar menuBar = (MenuBar) iter.next();
            if(menuBar.title.equals(menuBarTitle))
            {
                found = true;
                if(!remove)
                {
                    menuBar.addAction(action);
                }
                else
                {
                    menuBar.removeAction(action);    
                }
                break;
            }
            for(Iterator iterator = menuBar.subMenus.iterator(); iterator.hasNext();)
            {
                MenuBar bar = (MenuBar) iterator.next();
                Err.pr( SdzNote.SUB_MENU, "Looking at submenu titled <" + bar.title + "> to see if matches with <" + menuBarTitle + ">");
                if(bar.title.equals(menuBarTitle))
                {
                    found = true;
                    if(!remove)
                    {
                        bar.addAction(action);
                    }
                    else
                    {
                        bar.removeAction(action);
                    }
                    break;
                }
            }
        }
        if(!found)
        {
            Err.error("Action does not know about title: <" + menuBarTitle + ">, which is meant to be title to use for action <" + action + ">");
        }
    }

    private Tab getTabByVisibleStrand(VisibleStrandI visibleStrand)
    {
        Tab result = null;
        for(Iterator iter = tabs.iterator(); iter.hasNext();)
        {
            Tab tab = (Tab) iter.next();
            if(tab.action.getVisibleStrand() == visibleStrand)
            {
                result = tab;
                break;
            }
        }
        if(result == null)
        {
            Err.error(
                "Could not find a tab for visibleStrand: <" + visibleStrand.getClass().getName() + 
                        "> - you need to add items (addItem()) before calling sdzInit()");
        }
        Assert.notNull( result.strandArea);
        return result;
    }

    /**
     * Not looking at the tabbedpane, so not looking at physical/displayed 
     */
    private Tab getTabByComponent(Component strandArea)
    {
        Tab result = null;
        for(Iterator iter = tabs.iterator(); iter.hasNext();)
        {
            Tab tab = (Tab) iter.next();
            if(tab.strandArea == strandArea)
            {
                result = tab;
                break;
            }
        }
        if(result == null)
        {
            Err.error("Could not find a tab for strandArea: " + strandArea);
        }
        return result;
    }
    
    public void addAction(Action action)
    {
        addRemoveFromMenu(action, false);
    }
        
    public void addItem(AbstractStrandAction action)
    {
        super.addItem(action);
        addRemoveFromMenu(action, false);

        VisibleStrandI visibleStrand = action.getVisibleStrand();
        if(visibleStrand != null)
        {
            AbstractStrandArea strandArea = createStrandArea( visibleStrand);
            Tab tab = new Tab(action, strandArea);
            visibleStrand.setStrandArea(tab.strandArea);
            tabs.add(tab);
            // times++;
            // Err.pr( "\t\tIn MenuApplication.addItem() times " + times);
        }
    }
    
    private AbstractStrandArea createStrandArea( VisibleStrandI visibleStrand)
    {
        AbstractStrandArea result;
        if(visibleStrand.isAdorned())
        {
            result = new StrandArea(); 
        }
        else
        {
            result = new UnadornedStrandArea(); 
        }
        return result;
    }

    /**
     * We prolly ought to remove the physical tab as well - although lets see
     * the problems that may occur in actual use 
     */
    public void removeAllItemsWithTitleContaining( String title)
    {
        List items = getItemsWithTitleContaining( title);
        for(Iterator iterator = items.iterator(); iterator.hasNext();)
        {
            AbstractStrandAction action = (AbstractStrandAction)iterator.next();
            addRemoveFromMenu(action, true);
        }
        super.removeAllItems( items);
    }
}
