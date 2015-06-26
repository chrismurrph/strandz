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
import org.strandz.lgpl.util.Err;
import org.strandz.widgets.data.wombatrescue.FlexibilityRadioButtons;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

public class WorkerPanel extends JPanel implements MouseWheelListener
{
    // AlphabetPanel alphabetPanel;
    JLabel lChristianName;
    public JTextField tfChristianName;
    JLabel lSurname;
    public JTextField tfSurname;
    JLabel lGroupName;
    public MonitorPaintTextField tfGroupName;
    JLabel lStreet;
    JTextField tfStreet;
    JLabel lSuburb;
    JTextField tfSuburb;
    JLabel lPostcode;
    JTextField tfPostcode;
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
    JLabel lAway1Start;
    MDateEntryField mdefAway1Start;
    JLabel lAway1End;
    MDateEntryField mdefAway1End;
    JLabel lAway2Start;
    MDateEntryField mdefAway2Start;
    JLabel lAway2End;
    MDateEntryField mdefAway2End;
    public FlexibilityRadioButtons frbFlexibilityRadioButtons;
    JLabel lBelongsTo;
    JComboBox cbBelongsTo;
    JCheckBox chkIsManager;
    JCheckBox chkUnknown;
    public JLabel lComments;
    JTextArea taComments;
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

    public WorkerPanel()
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
        lStreet = new JLabel();
        tfStreet = new JTextField();
        lSuburb = new JLabel();
        tfSuburb = new JTextField();
        lPostcode = new JLabel();
        tfPostcode = new JTextField();
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
        lAway1Start = new JLabel();
        mdefAway1Start = new MDateEntryField();
        mdefAway1Start.setNullOnEmpty(true);
        lAway1End = new JLabel();
        mdefAway1End = new MDateEntryField();
        mdefAway1End.setNullOnEmpty(true);
        lAway2Start = new JLabel();
        mdefAway2Start = new MDateEntryField();
        mdefAway2Start.setNullOnEmpty(true);
        lAway2End = new JLabel();
        mdefAway2End = new MDateEntryField();
        mdefAway2End.setNullOnEmpty(true);
        lComments = new JLabel();
        taComments = new JTextArea();
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
                    ModernTableLayout.FILL, TEXT_HEIGHT, BIG_SPACING, TEXT_HEIGHT, SMALL_SPACING,
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
        lContactName.setText("Contact Name");
        tfContactName.setName("tfContactName");
        lStreet.setHorizontalAlignment(SwingConstants.TRAILING);
        lStreet.setText("Street");
        tfStreet.setName("tfStreet");
        lSuburb.setHorizontalAlignment(SwingConstants.TRAILING);
        lSuburb.setText("Suburb");
        tfSuburb.setName("tfSuburb");
        lPostcode.setHorizontalAlignment(SwingConstants.TRAILING);
        lPostcode.setText("Postcode");
        tfPostcode.setName("tfPostcode");
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
        lAway1Start.setHorizontalAlignment(SwingConstants.TRAILING);
        lAway1Start.setText("Away 1 Start");
        mdefAway1Start.setName("mdefAway1Start");
        lAway1End.setHorizontalAlignment(SwingConstants.TRAILING);
        lAway1End.setText("Away 1 End");
        mdefAway1End.setName("mdefAway1End");
        lAway2Start.setHorizontalAlignment(SwingConstants.TRAILING);
        lAway2Start.setText("Away 2 Start");
        mdefAway2Start.setName("mdefAway2Start");
        lAway2End.setHorizontalAlignment(SwingConstants.TRAILING);
        lAway2End.setText("Away 2 End");
        mdefAway2End.setName("mdefAway2End");
        lComments.setHorizontalAlignment(SwingConstants.LEADING);
        lComments.setText("Comments");
        taComments.setName("taComments");
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
        add(lStreet, "1, 9");
        add(tfStreet, "3, 9");
        add(lSuburb, "1, 11");
        add(tfSuburb, "3, 11");
        add(lPostcode, "1, 13");
        add(tfPostcode, "3, 13");
        add(lHomePhone, "1, 15");
        add(tfHomePhone, "3, 15");
        add(lWorkPhone, "1, 17");
        add(tfWorkPhone, "3, 17");
        add(lMobilePhone, "1, 19");
        add(tfMobilePhone, "3, 19");
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
        add(lSeniority, "5, 21");
        add(cbSeniority, "7, 21, 8, 21");
        add(lSex, "5, 23");
        add(cbSex, "7, 23, 8, 23");
        add(lBirthday, "5, 25");
        add(mdefBirthday, "7, 25, 8, 25");
        add(lAway1Start, "5, 27");
        add(mdefAway1Start, "7, 27, 8, 27");
        add(lAway1End, "5, 29");
        add(mdefAway1End, "7, 29, 8, 29");
        add(lAway2Start, "5, 31");
        add(mdefAway2Start, "7, 31, 8, 31");
        add(lAway2End, "5, 33");
        add(mdefAway2End, "7, 33, 8, 33");
        add(chkUnknown, "7, 35");
        add(lComments, "1, 23");
        add(lFlashDay, "8, 35");

        //Size restriction to 250 is done at RT
        JScrollPane sp = new JScrollPane(taComments);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setName("spComments");
        add(sp, "1, 24, 4, 33");
        //add( taComments, "1, 24, 4, 29"); //instead of scrollpane, lets use TestArea directly

        // Will adding this last mean it gets painted last??
        // add ( alphabetPanel,    "0, 0, 7, 0");

        setName("WorkerPanel");
        setMdefBirthday(mdefBirthday);
        setMdefAway1End(mdefAway1End);
        setMdefAway1Start(mdefAway1Start);
        setMdefAway2End(mdefAway2End);
        setMdefAway2Start(mdefAway2Start);
        setTfGroupName(tfGroupName);
        setLFlashDay(lFlashDay);
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

    public MDateEntryField getMdefAway1End()
    {
        return mdefAway1End;
    }

    public void setMdefAway1End(MDateEntryField mdefAway1End)
    {
        this.mdefAway1End = mdefAway1End;
    }

    public MDateEntryField getMdefAway1Start()
    {
        return mdefAway1Start;
    }

    public void setMdefAway1Start(MDateEntryField mdefAway1Start)
    {
        this.mdefAway1Start = mdefAway1Start;
    }

    public MDateEntryField getMdefAway2End()
    {
        return mdefAway2End;
    }

    public void setMdefAway2End(MDateEntryField mdefAway2End)
    {
        this.mdefAway2End = mdefAway2End;
    }

    public MDateEntryField getMdefAway2Start()
    {
        return mdefAway2Start;
    }

    public void setMdefAway2Start(MDateEntryField mdefAway2Start)
    {
        this.mdefAway2Start = mdefAway2Start;
    }

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
        Err.pr("Mouse Wheel Moved - programmer hasn't implemented scrolling up and down - request in forum!");
    }
} // end class

