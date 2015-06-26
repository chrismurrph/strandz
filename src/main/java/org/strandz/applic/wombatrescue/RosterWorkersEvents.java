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
package org.strandz.applic.wombatrescue;

import foxtrot.AsyncTask;
import foxtrot.AsyncWorker;
import mseries.ui.MChangeEvent;
import mseries.ui.MChangeListener;
import mseries.ui.MDateEntryField;
import org.strandz.core.domain.constants.StateEnum;
import org.strandz.core.interf.RuntimeAttribute;
import org.strandz.core.applichousing.VisibleStrand;
import org.strandz.data.wombatrescue.objects.RosterSlot;
import org.strandz.data.wombatrescue.objects.RosterSlotI;
import org.strandz.lgpl.data.objects.DayInWeek;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.note.WombatNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.NameUtils;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.widgets.table.ComponentTableView;
import org.strandz.view.util.DTUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.ParseException;

public class RosterWorkersEvents
    implements RosterWorkersOps
{
    RosterWorkers_NEW_FORMATDT dt;
    RosterWorkersOps outer; //Can't use RosterWorkersOps.this for an interface, so still need  
    boolean neuteredToggle = false;
    ComboActionListener intervalComboActionListener;

    RosterWorkersEvents( RosterWorkers_NEW_FORMATDT dt, VisibleStrand visibleStrand)
    {
        outer = this;
        this.dt = dt;
        DTUtils.chkNotNull( dt.ui0.getMdefBirthday()); 
        dt.ui0.getMdefBirthday().addMChangeListener(
            new FlashDayActionListener(dt.workerBirthdayAttribute));
        dt.ui0.getHolidaysTab().getMdefAway1End().addMChangeListener(
            new FlashDayActionListener(dt.workerAway1EndAttribute));
        dt.ui0.getHolidaysTab().getMdefAway1Start().addMChangeListener(
            new FlashDayActionListener(dt.workerAway1StartAttribute));
        dt.ui0.getHolidaysTab().getMdefAway2End().addMChangeListener(
            new FlashDayActionListener(dt.workerAway2EndAttribute));
        dt.ui0.getHolidaysTab().getMdefAway2Start().addMChangeListener(
            new FlashDayActionListener(dt.workerAway2StartAttribute));
        dt.ui1.getChkMonthlyRestart().addActionListener(
            new MonthlyRestartToggleActionListener());
        dt.ui1.getChkMonthlyRestart().addPropertyChangeListener(
            new CBChangedListener(this));
        dt.ui1.getChkDisabled().addActionListener(
            new DisabledToggleActionListener());
        dt.ui1.getChkNotAvailable().addActionListener(
            new NotAvailableToggleActionListener());        
        dt.ui1.getCbWhichShift().addActionListener( new ComboActionListener( this));
        dt.ui1.getCbDay().addActionListener( new ComboActionListener( this));
        dt.ui1.getCbOnlyInMonth().addActionListener( new ComboActionListener( this));
        dt.ui1.getCbNotInMonth().addActionListener( new ComboActionListener( this));
        dt.ui1.getCbWeekInMonth().addActionListener( new ComboActionListener( this));
        intervalComboActionListener = new ComboActionListener( this);
        dt.ui1.getCbInterval().addActionListener( intervalComboActionListener);
        dt.ui1.getCbOverridesOthers().addActionListener( new ComboActionListener( this));
        dt.ui1.getMdefSpecificDate().addMChangeListener(
            new SentenceFlashDayActionListener(dt.rosterSlotspecificDateAttribute, this));
        dt.ui1.getMdefSpecificDate().getDisplay().addFocusListener( 
                new SentenceFlashDayFocusListener( dt.ui1.getMdefSpecificDate()));
        dt.ui1.getMdefFirstShift().addMChangeListener(
            new SentenceFlashDayActionListener(dt.rosterSlotstartDateAttribute, this));
        dt.ui1.getMdefFirstShift().getDisplay().addFocusListener( 
                new SentenceFlashDayFocusListener( dt.ui1.getMdefFirstShift()));
        Assert.notNull( dt.ui0.getQuickViewRosterSlotsPanel(), 
            "Expect a QuickViewRosterSlotsPanel to have made it out from the XML file " +
                    "(possibly a non-visual XML file is not up to date)");
        ComponentTableView tableView = dt.ui0.getQuickViewRosterSlotsPanel().getTableView();
        tableView.setClickListener( 
                new QuickRosterSlotRightClickPopup( tableView, visibleStrand));
    }
    
    class SentenceFlashDayFocusListener extends FlashDayFocusListener 
    {
        public SentenceFlashDayFocusListener(MDateEntryField dateItem)
        {
            super(dateItem);
        }
        
        void dateChanged()
        {
            super.dateChanged();
            calculateRSSentence( dt.strand.getCurrentNode().getState());
        }
    }
    
    class FlashDayFocusListener implements FocusListener
    {
        MDateEntryField dateItem;
        String startText;

        public FlashDayFocusListener( MDateEntryField dateItem)
        {
            this.dateItem = dateItem;
        }
        
        void dateChanged()
        {
            try
            {
                Date date = dateItem.getValue();
                doWhenDateChanged( date);            
            }
            catch(ParseException e1)
            {
                //nufin
            }
        }

        public void focusGained(FocusEvent e)
        {
            startText = dateItem.getText();
            //Err.pr( "Gained focus so saved text <" + startText + ">");
        }

        public void focusLost(FocusEvent e)
        {
            boolean textChanged = !startText.equals( dateItem.getText()); 
            //Err.pr( "Lost focus and text changed: " + textChanged);
            if(textChanged)
            {
                dateChanged();
            }
        }
    }
    
    private void dayChanged( DayInWeek day)
    {
        //empty
    }
    
    private void doWhenDateChanged( Date date)
    {
        if(date != null)
        {
            GregorianCalendar cal = new GregorianCalendar(TimeUtils.SYDNEY_TIME_ZONE);
            cal.setTime( date);
            int dayOfWeek = cal.get(GregorianCalendar.DAY_OF_WEEK);
            DayInWeek day = DayInWeek.fromOrdinal(dayOfWeek);
            dayChanged( day);
            flashDay( day);
        }
        else
        {
            dayChanged( null);
        }
    }

    class FlashDayActionListener implements MChangeListener
    {
        private RuntimeAttribute attr;

        FlashDayActionListener(RuntimeAttribute attr)
        {
            this.attr = attr;
        }
        
        public void valueChanged(MChangeEvent event)
        {
            if(event.getType() == MChangeEvent.CHANGE)
            {
                Date date = (Date) event.getValue();
                doWhenDateChanged( date);
            }
        }
    }
    
    class SpecificActionListener extends FlashDayActionListener
    {
        SpecificActionListener(RuntimeAttribute attr)
        {
            super(attr);
        }

        void dayChanged( DayInWeek day)
        {
            if(day != null)
            {
                dt.ui1.getCbDay().setSelectedItem( day.getName());
            }
            else
            {
                dt.ui1.getCbDay().setSelectedItem( DayInWeek.NULL);
            }
        }
    }

    private void flashDay(DayInWeek day)
    {
        //Doing both at once is ok because the user doesn't see the other screen
        dt.ui0.getLFlashDay().setText("          " + day);
        dt.ui1.getLFlashDay().setText("          " + day);
        AsyncWorker.post(new AsyncTask()
        {
            public Object run() throws Exception
            {
                Thread.sleep(3000);
                return "";
            }

            public void finish()
            {
                try
                {
                    String text = (String) getResultOrThrow();
                    dt.ui0.getLFlashDay().setText("          " + text);
                    dt.ui1.getLFlashDay().setText(text);
                }
                catch(Exception x)
                {
                    // Do exception handling: here is rethrown
                    // what is eventually thrown inside run()
                }
            }

            public void success(Object o)
            {
            }

            public void failure(Throwable throwable)
            {
            }
        });
    }

    void disableToggle(boolean b)
    {
        neuteredToggle = b;
    }

    public void toggleMonthlyRestart(boolean selected)
    {
        // boolean selected = ui2.getChkMonthlyRestart().isSelected();
        // Err.pr( "$$$$$$$$$$$$ toggleMonthlyRestart " + selected);
        if(!neuteredToggle)
        {
            dt.ui1.getCbInterval().removeActionListener( intervalComboActionListener);
            if(selected)
            {
                Err.pr(SdzNote.NV_PASTE_NOT_WORKING, "weekInMonth to be enabled TRUE");
                dt.weekInMonthnameAttribute.setEnabled(true);
                dt.numDaysIntervalnameAttribute.setEnabled(false);
                dt.ui1.getMdefFirstShift().setEnabled(false);
                dt.rosterSlotstartDateAttribute.setEnabled(false);
            }
            else
            {
                Err.pr(SdzNote.NV_PASTE_NOT_WORKING, "weekInMonth to be enabled FALSE");
                dt.weekInMonthnameAttribute.setEnabled(false);
                dt.numDaysIntervalnameAttribute.setEnabled(true);
                dt.ui1.getMdefFirstShift().setEnabled(true);
                dt.rosterSlotstartDateAttribute.setEnabled(true);
            }
            dt.ui1.getCbInterval().addActionListener( intervalComboActionListener);
        }
    }
    
    /**
     * A calculated field by definition won't be looking at
     * any Attributes - it will put a value directly on
     * the panel.
     */
    public void calculateRSSentence(StateEnum stateId)
    {
        if(dt.ui1 != null) //possible that a dt would not have any panels
        {
            DTUtils.chkNotNull( dt.ui1.getLSentence());             
            RosterSlotI rosterSlot = null;
            //if(stateId.isNew())
            if(stateId != StateEnum.FROZEN)
            {
                /*
                * No roster slot actually exists, yet one is manufactured from
                * all the items - and that includes items connected to many DOs. 
                * So you end up with a 'deep' roster slot with all its object 
                * references.
                */
                rosterSlot = (RosterSlotI) dt.rosterSlotsCell.getItemValue();
            }
            /*
            else
            {
                VisibleExtent ve = dt.rosterSlotsCell.getDataRecords();
                int row = dt.rosterSlotsListDetailNode.getRow();
                if(row != -1) //when stateId == StateEnum.FROZEN/UNKNOWN row will be -1
                {
                    rosterSlot = (RosterSlot) ve.get(row);
                }
            }
            */
            if(rosterSlot != null)
            {
                String sentence = rosterSlot.getToSentence();
                //"#FF0000"
                Err.pr(SdzNote.CALC_ITEM, "SENTENCE (from creational method) : " + sentence);
                String redHex = "#FF0000";
                String colouredIn = NameUtils.colourInVariables( sentence, redHex);
                dt.ui1.getLSentence().setText( NameUtils.toHTML( colouredIn));
            }
            else
            {
                dt.ui1.getLSentence().setText("");
            }
        }
    }

    /*
    * Doesn't work when changing programmatically, for example
    * when paste, thus need other triggers for these other events. 
    * (Did try using a propertyChangeListener instead)
    */
    class MonthlyRestartToggleActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            outer.toggleMonthlyRestart(((Boolean) dt.rosterSlotmonthlyRestartAttribute.getItemValue()).booleanValue());
            outer.calculateRSSentence(dt.strand.getCurrentNode().getState());
        }
    }

    class DisabledToggleActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            outer.calculateRSSentence(dt.strand.getCurrentNode().getState());
        }
    }

    class NotAvailableToggleActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            outer.calculateRSSentence(dt.strand.getCurrentNode().getState());
        }
    }
    
    /*
    private boolean isAnEveryMonthRestartRow()
    {
      boolean result = dt.ui1.getChkMonthlyRestart().isSelected();
      boolean byAttrib = ((Boolean)dt.monthlyRestartAttribute.getItemValue()).booleanValue();
      if(result != byAttrib)
      {
        Err.error();
      }
      return result;
    }
    */

    /**
     * Only property change that seems to be seen is 'enabled',
     * making this class not fit for purpose. As was needing to
     * do as part of paste operation, can do by manually calling
     * toggleMonthlyRestart().
     */
    private class CBChangedListener
        implements PropertyChangeListener
    {
        RosterWorkersOps ops;

        CBChangedListener(RosterWorkersOps ops)
        {
            super();
            this.ops = ops;
        }

        public void propertyChange(PropertyChangeEvent e)
        {
            String propertyName = e.getPropertyName();
            // Err.pr( "PROP CHANGE: " + propertyName);
            if(propertyName.equals("selected"))
            {
                Err.error( "Thought not being called");
                Boolean enabledState = (Boolean) e.getNewValue();
                ops.toggleMonthlyRestart(enabledState.booleanValue());
                ops.calculateRSSentence(dt.strand.getCurrentNode().getState());
            }
        }
    }
        
    class ComboActionListener implements ActionListener
    {
        RosterWorkersOps ops;

        ComboActionListener( RosterWorkersOps ops)
        {
            this.ops = ops;
        }

        public void actionPerformed(ActionEvent e)
        {
            if(!dt.strand.sdzHasControl())
            {
                Err.pr( WombatNote.REFRESH_SENTENCE, "ComboActionListener for " + e);
                ops.calculateRSSentence( dt.strand.getCurrentNode().getState());
            }
        }
    }

    class SentenceFlashDayActionListener extends FlashDayActionListener
    {
        RosterWorkersOps ops;

        SentenceFlashDayActionListener( RuntimeAttribute attr, RosterWorkersOps ops)
        {
            super(attr);
            this.ops = ops;
        }
        
        public void valueChanged(MChangeEvent event)
        {
            super.valueChanged( event);
            if(event.getType() == MChangeEvent.CHANGE)
            {
                ops.calculateRSSentence( dt.strand.getCurrentNode().getState());
            }
        }
    }
}
