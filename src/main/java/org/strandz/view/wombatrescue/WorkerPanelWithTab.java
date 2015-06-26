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
package org.strandz.view.wombatrescue;

import org.strandz.lgpl.tablelayout.ModernTableLayout;
import mseries.ui.MDateEntryField;
import org.strandz.lgpl.widgets.MonitorPaintTextField;
import org.strandz.widgets.data.wombatrescue.FlexibilityRadioButtons;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class WorkerPanelWithTab extends JPanel implements MouseWheelListener
{
    // AlphabetPanel alphabetPanel;
    JLabel lChristianName;
    public JTextField tfChristianName;
    JLabel lSurname;
    public JTextField tfSurname;
    JLabel lGroupName;
    public MonitorPaintTextField tfGroupName;
    JLabel lHomePhone;
    JTextField tfHomePhone;
    JLabel lWorkPhone;
    JTextField tfWorkPhone;
    JLabel lMobilePhone;
    JTextField tfMobilePhone;
    JLabel lContactName;
    JTextField tfContactName;
    JLabel lEmail1;
    JTextField tfEmail1;
    JLabel lEmail2;
    JTextField tfEmail2;
    JLabel lShiftPreference;
    JComboBox cbShiftPreference;
    JLabel lSeniority;
    public JComboBox cbSeniority;
    JLabel lSex;
    JComboBox cbSex;
    JLabel lBirthday;
    MDateEntryField mdefBirthday;
    public FlexibilityRadioButtons frbFlexibilityRadioButtons;
    JLabel lBelongsTo;
    JComboBox cbBelongsTo;
    JCheckBox chkIsManager;
    JCheckBox chkUnknown;
    //public JLabel lComments;
    JTextArea taComments;
    QuickViewRosterSlotsPanel quickViewRosterSlotsPanel;
    AddressPanel addressTab;
    HolidaysPanel holidaysTab;
    JTabbedPane tabbedPane;
    JLabel lFlashDay;
    /*
    private Volunteer belongsToGroup;
    private String groupName;
    private boolean groupContactPerson = false;
    */

    static final int GAP = 15;
    static final int BORDER = 15;
    static final int SMALL_SPACING = 2;
    static final int BIG_SPACING = 13;
    static final int TEXT_HEIGHT = 23;
    // for debugging
    private static String[] names = {
        "tfChristianName", "tfSurname", "tfGroupName", "tfContactName", "tfStreet",
        "tfSuburb", "tfPostcode", "tfHomePhone", "tfWorkPhone", "tfMobilePhone",
        "cbBelongsTo", "tfEmail1", "tfEmail2", "cbShiftPreference", "cbSeniority",
        "cbSex", "chkIsManager", "chkUnknown", "rbNoEvenings", "rbNoOvernights",
        "rbFlexible", "taComments", "spComments",
    };

    public WorkerPanelWithTab()
    {
        //Register for mouse-wheel events on the text area.
        addMouseWheelListener(this);
    }

    public void init()
    {
        // alphabetPanel = new AlphabetPanel();
        // alphabetPanel.init();
        lChristianName = new JLabel();
        tfChristianName = new JTextField();
        lSurname = new JLabel();
        tfSurname = new JTextField();
        lGroupName = new JLabel();
        tfGroupName = new MonitorPaintTextField();
        lHomePhone = new JLabel();
        tfHomePhone = new JTextField();
        lWorkPhone = new JLabel();
        tfWorkPhone = new JTextField();
        lMobilePhone = new JLabel();
        tfMobilePhone = new JTextField();
        lContactName = new JLabel();
        tfContactName = new JTextField();
        chkIsManager = new JCheckBox();
        lBelongsTo = new JLabel();
        cbBelongsTo = new JComboBox();
        lEmail1 = new JLabel();
        tfEmail1 = new JTextField();
        lEmail2 = new JLabel();
        tfEmail2 = new JTextField();
        lShiftPreference = new JLabel();
        cbShiftPreference = new JComboBox();
        lSeniority = new JLabel();
        cbSeniority = new JComboBox();
        lSex = new JLabel();
        cbSex = new JComboBox();
        lBirthday = new JLabel();
        mdefBirthday = new MDateEntryField();
        mdefBirthday.setNullOnEmpty(true);
        chkUnknown = new JCheckBox();
        //lComments = new JLabel();
        taComments = new JTextArea();
        quickViewRosterSlotsPanel = new QuickViewRosterSlotsPanel();
        quickViewRosterSlotsPanel.init();
        addressTab = new AddressPanel();
        addressTab.init();
        holidaysTab = new HolidaysPanel();
        holidaysTab.init();
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab( "Roster Slots", quickViewRosterSlotsPanel);
        tabbedPane.addTab( "Comments", taComments);
        tabbedPane.addTab( "Holidays", holidaysTab);
        tabbedPane.addTab( "Address", addressTab);
        frbFlexibilityRadioButtons = new FlexibilityRadioButtons();
        frbFlexibilityRadioButtons.init();
        lFlashDay = new JLabel();
        //Fixed sized Strings in MySql so we have to enforce it here
        //This didn't get into XMLEncoder so instead do columns and rows
        //If doing it this way really was important we could do something
        //at runtime in the Triggers file. See
        // RosterWorkersTriggers.alterSdzSetup()
        //
        //taComments.setDocument( new FixedDocument( 250));
        /*
         * Definitely got in, but did not have desired effect, so went with alterSdzSetup
        taComments.setColumns( 50);
        taComments.setRows( 5);
        */

        // Create a TableLayout for the panel
        double size[][] = // Columns
            {
                {BORDER, 0.17, GAP, 0.32, GAP, 0.17, GAP, ModernTableLayout.FILL, ModernTableLayout.FILL, BORDER},
                // Rows
                {
                    ModernTableLayout.FILL, TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT, BIG_SPACING,
                    TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT, BIG_SPACING, TEXT_HEIGHT,
                    SMALL_SPACING, TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT, SMALL_SPACING,
                    TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT,
                    SMALL_SPACING, TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT, SMALL_SPACING,
                    TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT,
                    SMALL_SPACING, TEXT_HEIGHT, SMALL_SPACING, TEXT_HEIGHT, SMALL_SPACING,
                    TEXT_HEIGHT, ModernTableLayout.FILL}};
        setLayout(new ModernTableLayout(size));
        lChristianName.setHorizontalAlignment(SwingConstants.TRAILING);
        lChristianName.setText("First Name");
        tfChristianName.setName("tfChristianName");
        lSurname.setHorizontalAlignment(SwingConstants.TRAILING);
        lSurname.setText("Surname");
        tfSurname.setName("tfSurname");
        lGroupName.setHorizontalAlignment(SwingConstants.TRAILING);
        lGroupName.setText("Group Name");
        tfGroupName.setName("tfGroupName");
        lContactName.setHorizontalAlignment(SwingConstants.TRAILING);
        lContactName.setText("Group Contact Name");
        tfContactName.setName("tfContactName");
        lHomePhone.setHorizontalAlignment(SwingConstants.TRAILING);
        lHomePhone.setText("Home Phone");
        tfHomePhone.setName("tfHomePhone");
        lWorkPhone.setHorizontalAlignment(SwingConstants.TRAILING);
        lWorkPhone.setText("Work Phone");
        tfWorkPhone.setName("tfWorkPhone");
        lMobilePhone.setHorizontalAlignment(SwingConstants.TRAILING);
        lMobilePhone.setText("Mobile Phone");
        tfMobilePhone.setName("tfMobilePhone");
        lBelongsTo.setHorizontalAlignment(SwingConstants.TRAILING);
        lBelongsTo.setText("Belongs To ");
        cbBelongsTo.setName("cbBelongsTo");
        lEmail1.setHorizontalAlignment(SwingConstants.TRAILING);
        lEmail1.setText("Email 1");
        tfEmail1.setName("tfEmail1");
        lEmail2.setHorizontalAlignment(SwingConstants.TRAILING);
        lEmail2.setText("Email 2");
        tfEmail2.setName("tfEmail2");
        lShiftPreference.setHorizontalAlignment(SwingConstants.TRAILING);
        lShiftPreference.setText("Shift Pref ");
        cbShiftPreference.setName("cbShiftPreference");
        lSeniority.setHorizontalAlignment(SwingConstants.TRAILING);
        lSeniority.setText("Seniority ");
        cbSeniority.setName("cbSeniority");
        frbFlexibilityRadioButtons.setName("frbFlexibilityRadioButtons");
        lSex.setHorizontalAlignment(SwingConstants.TRAILING);
        lSex.setText("Sex ");
        cbSex.setName("cbSex");
        lBirthday.setHorizontalAlignment(SwingConstants.TRAILING);
        lBirthday.setText("Birthday");
        mdefBirthday.setName("mdefBirthday");
        chkIsManager.setText("Is Group Roster Manager");
        chkIsManager.setName("chkIsManager");
        chkUnknown.setText("Un-Rosterable");
        chkUnknown.setName("chkUnknown");
        //lComments.setHorizontalAlignment(SwingConstants.LEADING);
        //lComments.setText("Comments");
        taComments.setName("taComments");
        tabbedPane.setName( "tpTabbedPane");
        tabbedPane.setBorder( BorderFactory.createEmptyBorder( 0,0,0,0));
        lFlashDay.setName("lFlashDay");
        //lFlashDay.setHorizontalAlignment( SwingConstants.RIGHT);
        lFlashDay.setForeground(new Color(0x8000FF));

        add(lGroupName, "1, 1");
        add(tfGroupName, "3, 1");
        add(lContactName, "1, 3");
        add(tfContactName, "3, 3");
        add(lChristianName, "1, 5");
        add(tfChristianName, "3, 5");
        add(lSurname, "1, 7");
        add(tfSurname, "3, 7");
        add(lHomePhone, "1, 9");
        add(tfHomePhone, "3, 9");
        add(lWorkPhone, "1, 11");
        add(tfWorkPhone, "3, 11");
        add(lMobilePhone, "1, 13");
        add(tfMobilePhone, "3, 13");
        
        add(lSeniority, "1, 15");
        add(cbSeniority, "3, 15");
        add(lSex, "1, 17");
        add(cbSex, "3, 17");
        add(lBirthday, "1, 19");
        add(mdefBirthday, "3, 19");
        
        add(lBelongsTo, "5, 5");
        add(cbBelongsTo, "7, 5, 8, 5");
        add(chkIsManager, "5,7,7,7");
        add(lEmail1, "5, 9");
        add(tfEmail1, "7, 9, 8, 9");
        add(lEmail2, "5, 11");
        add(tfEmail2, "7, 11, 8, 11");
        add(lShiftPreference, "5, 13");
        add(cbShiftPreference, "7, 13, 8, 13");
        /*
        add( rbNoEvenings, "7, 15");
        add( rbNoOvernights, "7, 17");
        add( rbFlexible, "7, 19");
        */
        add(frbFlexibilityRadioButtons, "7,15,7,19");
                
        add(chkUnknown, "7, 35");
        //add(lComments, "1, 23");
        add(lFlashDay, "8, 35");

        //Size restriction to 250 is done at RT
        JScrollPane sp = new JScrollPane( tabbedPane);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setName("spTabbedPane");
        sp.setBorder( BorderFactory.createEmptyBorder( 0,0,0,0));
        add(sp, "1, 21, 5, 33");
        //add( taComments, "1, 24, 4, 29"); //instead of scrollpane, lets use TestArea directly

        // Will adding this last mean it gets painted last??
        // add ( alphabetPanel,    "0, 0, 7, 0");

        setName("WorkerPanel");
        setMdefBirthday(mdefBirthday);
        setTfGroupName(tfGroupName);
        setLFlashDay(lFlashDay);
        setHolidaysTab( holidaysTab);
        setQuickViewRosterSlotsPanel( quickViewRosterSlotsPanel);
    }

    /*
     * In Dsgnr adding a component (FlexibilityRadioButtons) works whereas having a property
     * as was doing here only worked for XMLEncoding. FlexibilityRadioButtons works for both
    public OneOnlyGroup getFlexibleGroup()
    {
      return flexibleGroup;
    }

    public void setFlexibleGroup( OneOnlyGroup flexibleGroup)
    {
      this.flexibleGroup = flexibleGroup;
    }
    */

    public void fixRepaintProblem()
    {
        // pAway1Start.addComponentListener( alphabetPanel);
        tfGroupName.setToInvalidateComponent(tfGroupName);
        // tfGroupName.setDebugGraphicsOptions( DebugGraphics.NONE_OPTION);
        // alphabetPanel.setDebugGraphicsOptions( DebugGraphics.FLASH_OPTION);
    }

    public MDateEntryField getMdefBirthday()
    {
        return mdefBirthday;
    }

    public void setMdefBirthday(MDateEntryField mdefBirthday)
    {
        this.mdefBirthday = mdefBirthday;
    }

    /*
    public AlphabetPanel getAlphabetPanel() {
    return alphabetPanel;
    }
    public void setAlphabetPanel(AlphabetPanel alphabetPanel) {
    this.alphabetPanel = alphabetPanel;
    }
    */

    public MonitorPaintTextField getTfGroupName()
    {
        return tfGroupName;
    }

    public void setTfGroupName(MonitorPaintTextField tfGroupName)
    {
        this.tfGroupName = tfGroupName;
    }

    public FlexibilityRadioButtons getFrbFlexibilityRadioButtons()
    {
        return frbFlexibilityRadioButtons;
    }

    public void setFrbFlexibilityRadioButtons(FlexibilityRadioButtons frbFlexibilityRadioButtons)
    {
        this.frbFlexibilityRadioButtons = frbFlexibilityRadioButtons;
    }

    public JLabel getLFlashDay()
    {
        return lFlashDay;
    }

    public void setLFlashDay(JLabel lFlashDay)
    {
        this.lFlashDay = lFlashDay;
    }

    public HolidaysPanel getHolidaysTab()
    {
//        if(holidaysTab == null)
//        {
//            holidaysTab = new HolidaysPanel();
//            holidaysTab.init();
//        }
        return holidaysTab;
    }

    public void setHolidaysTab(HolidaysPanel holidaysTab)
    {
        this.holidaysTab = holidaysTab;
    }
    
    /*
    //ease transition
    public MDateEntryField getMdefAway1End()
    {
        return getHolidaysTab().getMdefAway1End();
    }

    public void setMdefAway1End(MDateEntryField mdefAway1End)
    {
        getHolidaysTab().setMdefAway1End( mdefAway1End);
    }

    public MDateEntryField getMdefAway1Start()
    {
        return getHolidaysTab().getMdefAway1Start();
    }

    public void setMdefAway1Start(MDateEntryField mdefAway1Start)
    {
        this.getHolidaysTab().setMdefAway1Start( mdefAway1Start);
    }

    public MDateEntryField getMdefAway2End()
    {
        return getHolidaysTab().getMdefAway2End();
    }

    public void setMdefAway2End(MDateEntryField mdefAway2End)
    {
        this.getHolidaysTab().setMdefAway2End( mdefAway2End);
    }

    public MDateEntryField getMdefAway2Start()
    {
        return getHolidaysTab().getMdefAway2Start();
    }

    public void setMdefAway2Start(MDateEntryField mdefAway2Start)
    {
        this.getHolidaysTab().setMdefAway2Start( mdefAway2Start);
    }
    //end ease transition
    */

    public boolean isFlexibleAdded()
    {
        boolean result = false;
        Component comps[] = getComponents();
        for(int i = 0; i < comps.length; i++)
        {
            Component comp = comps[i];
            String name = comp.getName();
            if(name != null && name.equals("chkFlexible"))
            {
                result = true;
                break;
            }
        }
        return result;
    }

    public QuickViewRosterSlotsPanel getQuickViewRosterSlotsPanel()
    {
        return quickViewRosterSlotsPanel;
    }

    public void setQuickViewRosterSlotsPanel(QuickViewRosterSlotsPanel quickViewRosterSlotsPanel)
    {
        this.quickViewRosterSlotsPanel = quickViewRosterSlotsPanel;
    }

    /*
    protected void addImpl(Component comp,
    Object constraints,
    int index)
    {
    String name = comp.getName();
    Class clazz = comp.getClass();
    boolean ok = false;
    if(clazz == VolunteerPanel.class)
    {
    Err.error( "Cannot add a container to itself: " + this);
    }
    else if(clazz == JLabel.class)
    {
    ok = true;
    }
    else if(name == null)
    {
    Err.error( "To volunteerPanel we are trying to add a: " + clazz.getName());
    }
    else if(pUtils.containsByEquals( names, comp.getName()) > 0)
    {
    ok = true;
    }
    if(ok)
    {
    super.addImpl( comp, constraints, index);
    }
    }
    */

    public void mouseWheelMoved(MouseWheelEvent e)
    {
        //Err.pr("Mouse Wheel Moved - programmer hasn't implemented scrolling up and down - request in forum!");
    }
} // end class

