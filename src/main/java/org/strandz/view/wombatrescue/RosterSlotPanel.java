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
import org.strandz.lgpl.widgets.LargeBlueLabel;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;

public class RosterSlotPanel extends JPanel
{
    LargeBlueLabel lPerson;
    JLabel lDay;
    private JComboBox cbDay;
    JLabel lWhichShift;
    private JComboBox cbWhichShift;
    private JCheckBox chkMonthlyRestart;
    JLabel lWeekInMonth;
    private JComboBox cbWeekInMonth;
    JLabel lInterval;
    private JComboBox cbInterval;
    JLabel lFirstShift;
    private MDateEntryField mdefFirstShift;
    JLabel lSpecificDate;
    private MDateEntryField mdefSpecificDate;
    private JTextField tfSpecificDate;
    private JCheckBox chkNotAvailable;
    JLabel lOverridesOthers;
    private JComboBox cbOverridesOthers;
    private JCheckBox chkDisabled;
    JLabel lOnlyInMonth;
    private JComboBox cbOnlyInMonth;
    JLabel lNotInMonth;
    private JComboBox cbNotInMonth;
    private JLabel lSentence;
    private JLabel lFlashDay;

    static final int GAP = 15;
    static final int BORDER = 15;
    static final int SMALL_SPACING = 2;
    static final int BIG_SPACING = 13;
    static final int TEXT_HEIGHT = 23;
    static final int SMALL_WIDTH = 30;
    static final int LABEL_WIDTH = 100;
    static final int MIDDLE_WIDTH = 60;
    
    public RosterSlotPanel()
    {
    }

    public void init()
    {
        lPerson = new LargeBlueLabel();
        lDay = new JLabel();
        lWhichShift = new JLabel();
        lWeekInMonth = new JLabel();
        lInterval = new JLabel();
        lFirstShift = new JLabel();
        lOverridesOthers = new JLabel();
        lOnlyInMonth = new JLabel();
        lNotInMonth = new JLabel();
        cbDay = new JComboBox();
        // cbDay.setEditable( true);
        cbWhichShift = new JComboBox();
        // cbWhichShift.setEditable( true);
        chkMonthlyRestart = new JCheckBox();
        cbWeekInMonth = new JComboBox();
        // cbWeekInMonth.setEditable( true);
        cbInterval = new JComboBox();
        // cbInterval.setEditable( true);
        mdefFirstShift = new MDateEntryField();
        mdefFirstShift.setNullOnEmpty(true);
        cbOnlyInMonth = new JComboBox();
        cbNotInMonth = new JComboBox();
        lSpecificDate = new JLabel();
        tfSpecificDate = new JTextField();
        mdefSpecificDate = new MDateEntryField();
        mdefSpecificDate.setNullOnEmpty( true); 
        cbOverridesOthers = new JComboBox();
        chkDisabled = new JCheckBox();
        chkNotAvailable = new JCheckBox();
        lSentence = new JLabel();
        lFlashDay = new JLabel();

        // Create a TableLayout for the panel
        double size[][] = // Columns
            {
                {BORDER, LABEL_WIDTH, GAP, 0.40, MIDDLE_WIDTH, LABEL_WIDTH, GAP, ModernTableLayout.FILL, BORDER},
                // Rows
                {
                    ModernTableLayout.FILL, TEXT_HEIGHT, BIG_SPACING, TEXT_HEIGHT, BIG_SPACING,
                    TEXT_HEIGHT, BIG_SPACING, TEXT_HEIGHT, BIG_SPACING, TEXT_HEIGHT,
                    BIG_SPACING, TEXT_HEIGHT, BIG_SPACING, TEXT_HEIGHT, BIG_SPACING,
                    TEXT_HEIGHT, BIG_SPACING, TEXT_HEIGHT, BIG_SPACING, ModernTableLayout.FILL}};
        setLayout(new ModernTableLayout(size));
        lPerson.setName( "lPerson");
        lDay.setHorizontalAlignment(SwingConstants.TRAILING);
        lDay.setText("Day");
        cbDay.setName("cbDay");
        lWhichShift.setHorizontalAlignment(SwingConstants.TRAILING);
        lWhichShift.setText("Which Shift");
        cbWhichShift.setName("cbWhichShift");
        chkMonthlyRestart.setText("Monthly Restart");
        chkMonthlyRestart.setName("chkMonthlyRestart");
        lWeekInMonth.setHorizontalAlignment(SwingConstants.TRAILING);
        lWeekInMonth.setText("Week In Month");
        cbWeekInMonth.setName("cbWeekInMonth");
        lInterval.setHorizontalAlignment(SwingConstants.TRAILING);
        lInterval.setText("Interval");
        cbInterval.setName("cbInterval");
        lFirstShift.setHorizontalAlignment(SwingConstants.TRAILING);
        lFirstShift.setText("First Shift");
        mdefFirstShift.setName("mdefStartDate");
        lSpecificDate.setHorizontalAlignment(SwingConstants.TRAILING);
        lSpecificDate.setText("Known Date");
        mdefSpecificDate.setName("mdefSpecificDate");
        tfSpecificDate.setName("tfSpecificDate");
        lOverridesOthers.setHorizontalAlignment(SwingConstants.TRAILING);
        lOverridesOthers.setText("Overrides Others");
        cbOverridesOthers.setName("cbOverridesOthers");
        chkDisabled.setText("Disabled");
        chkDisabled.setName("chkDisabled");
        chkNotAvailable.setText("NOT Available (on Known Date)");
        chkNotAvailable.setName("chkNotAvailable");
        // chkNotAvailable.setHorizontalAlignment( SwingConstants.TRAILING);

        lOnlyInMonth.setHorizontalAlignment(SwingConstants.TRAILING);
        lOnlyInMonth.setText("Only In Month");
        cbOnlyInMonth.setName("cbOnlyInMonth");
        lNotInMonth.setHorizontalAlignment(SwingConstants.TRAILING);
        lNotInMonth.setText("Not In Month");
        cbNotInMonth.setName("cbNotInMonth");
        lSentence.setName("lSentence");
        lSentence.setForeground(new Color(0x8000FF));
        lFlashDay.setName("lFlashDay");
        //lFlashDay.setHorizontalAlignment( SwingConstants.RIGHT);
        lFlashDay.setForeground(new Color(0x8000FF));
        /* Was just to get a applichousing idea:
        lFlashDay.setText( "          " + "HappyDay");
        */

        add(lPerson, "1,0 , 5,0");
        add(lWhichShift, "1, 5");
        add(cbWhichShift, "3, 5");
        add(lDay, "1, 7");
        add(cbDay, "3, 7");

        add(chkMonthlyRestart, "7, 3");
        add(lWeekInMonth, "5, 5");
        add(cbWeekInMonth, "7, 5");
        add(lInterval, "5, 7");
        add(cbInterval, "7, 7");
        add(lFirstShift, "5, 9");
        add(mdefFirstShift, "7, 9");
        add(lSpecificDate, "1, 9");
        add(mdefSpecificDate, "3, 9");
        add(chkNotAvailable, "3, 11, 4, 11");
        add(lOverridesOthers, "5, 11");
        add(cbOverridesOthers, "7, 11");
        add(chkDisabled, "7, 13");
        add(lOnlyInMonth, "1, 15");
        add(cbOnlyInMonth, "3, 15");
        add(lNotInMonth, "1, 17");
        add(cbNotInMonth, "3, 17");
        add(lSentence, "7,15,7,17");
        add(lFlashDay, "4,9,5,9");

        setName("RosterSlotPanel");
        setCbWhichShift(cbWhichShift);
        setCbDay(cbDay);
        setChkMonthlyRestart(chkMonthlyRestart);
        setCbWeekInMonth(cbWeekInMonth);
        setCbInterval(cbInterval);
        setMdefFirstShift(mdefFirstShift);
        setTfSpecificDate(tfSpecificDate);
        setMdefSpecificDate(mdefSpecificDate);
        setCbOverridesOthers(cbOverridesOthers);
        setChkDisabled(chkDisabled);
        setChkNotAvailable(chkNotAvailable);
        setCbNotInMonth(cbNotInMonth);
        setCbOnlyInMonth(cbOnlyInMonth);
        setLSentence(lSentence);
        setLFlashDay(lFlashDay);
    }
    
    public LargeBlueLabel getLPerson()
    {
        return lPerson;
    }

    public void setLPerson(LargeBlueLabel lPerson)
    {
        this.lPerson = lPerson;
    }

    public MDateEntryField getMdefFirstShift()
    {
        return mdefFirstShift;
    }

    public void setMdefFirstShift(MDateEntryField mdefFirstShift)
    {
        this.mdefFirstShift = mdefFirstShift;
    }
    
    public MDateEntryField getMdefSpecificDate()
    {
        return mdefSpecificDate;
    }

    public void setMdefSpecificDate(MDateEntryField mdefSpecificDate)
    {
        this.mdefSpecificDate = mdefSpecificDate;
    }
    
    public JCheckBox getChkMonthlyRestart()
    {
        return chkMonthlyRestart;
    }

    public void setChkMonthlyRestart(JCheckBox chkMonthlyRestart)
    {
        this.chkMonthlyRestart = chkMonthlyRestart;
    }

    public JComboBox getCbWeekInMonth()
    {
        return cbWeekInMonth;
    }

    public void setCbWeekInMonth(JComboBox cbWeekInMonth)
    {
        this.cbWeekInMonth = cbWeekInMonth;
    }

    public JComboBox getCbInterval()
    {
        return cbInterval;
    }

    public void setCbInterval(JComboBox cbInterval)
    {
        this.cbInterval = cbInterval;
    }

    public JComboBox getCbDay()
    {
        return cbDay;
    }

     /**/
    public JComboBox getCbWhichShift()
    {
        return cbWhichShift;
    }

     /**/

    public void setCbDay(JComboBox cbDay)
    {
        this.cbDay = cbDay;
    }

     /**/
    public void setCbWhichShift(JComboBox cbWhichShift)
    {
        this.cbWhichShift = cbWhichShift;
    }

     /**/

    public JComboBox getCbOverridesOthers()
    {
        return cbOverridesOthers;
    }

    public void setCbOverridesOthers(JComboBox cbOverridesOthers)
    {
        this.cbOverridesOthers = cbOverridesOthers;
    }

    public JComboBox getCbNotInMonth()
    {
        return cbNotInMonth;
    }

    public JComboBox getCbOnlyInMonth()
    {
        return cbOnlyInMonth;
    }

    public JCheckBox getChkDisabled()
    {
        return chkDisabled;
    }

    public JCheckBox getChkNotAvailable()
    {
        return chkNotAvailable;
    }

    public void setCbNotInMonth(JComboBox cbNotInMonth)
    {
        this.cbNotInMonth = cbNotInMonth;
    }

    public void setCbOnlyInMonth(JComboBox cbOnlyInMonth)
    {
        this.cbOnlyInMonth = cbOnlyInMonth;
    }

    public void setChkDisabled(JCheckBox chkDisabled)
    {
        this.chkDisabled = chkDisabled;
    }

    public void setChkNotAvailable(JCheckBox chkNotAvailable)
    {
        this.chkNotAvailable = chkNotAvailable;
    }

    public JLabel getLSentence()
    {
        return lSentence;
    }

    public void setLSentence(JLabel lSentence)
    {
        this.lSentence = lSentence;
    }

    public JLabel getLFlashDay()
    {
        return lFlashDay;
    }

    public void setLFlashDay(JLabel lFlashDay)
    {
        this.lFlashDay = lFlashDay;
    }

    public JTextField getTfSpecificDate()
    {
        return tfSpecificDate;
    }

    public void setTfSpecificDate(JTextField tfSpecificDate)
    {
        this.tfSpecificDate = tfSpecificDate;
    }
} // end class
