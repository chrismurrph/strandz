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
package org.strandz.applic.wombatrescue.reports;

import org.strandz.core.interf.VisibleStrandAdapter;
import org.strandz.core.interf.Application;
import org.strandz.core.interf.VisibleStrandI;
import org.strandz.data.wombatrescue.business.Emailer;
import org.strandz.data.wombatrescue.business.Informer;
import org.strandz.data.wombatrescue.business.MessagesContainer;
import org.strandz.data.wombatrescue.business.PhoneCaller;
import org.strandz.data.wombatrescue.business.ParticularRosterI;
import org.strandz.data.wombatrescue.business.RosterSessionUtils;
import org.strandz.lgpl.data.objects.DayInWeek;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.widgets.TextAreaDisplayPanel;

import javax.swing.JComponent;
import java.awt.BorderLayout;
import java.util.Date;

public class ViewEmailsWeekStrand extends VisibleStrandAdapter implements VisibleStrandI
{
    private Application application;
    private JComponent surface;
    private TextAreaDisplayPanel textAreaDisplayPanel;
    private ParticularRosterI bo;

    public ViewEmailsWeekStrand(Application a)
    {
        this.application = a;
    }

    public void sdzInit()
    {
        surface = application.getStrandArea(this);
        textAreaDisplayPanel = new TextAreaDisplayPanel();
    }

    public void setBusinessObject(ParticularRosterI bo)
    {
        this.bo = bo;
    }

    public void display(boolean b)
    {
        if(b)
        {
            if(RosterSessionUtils.getGlobalCurrentMonth() == null)
            {
                new MessageDlg("Do not have a current month");
            }
            else
            {
                application.startActivity( this.getClass().getName());
                Date today = new Date();
                Date begin = DayInWeek.getBeginningDate(today);
                // Err.pr( "BEGIN: " + begin);
                Date end = DayInWeek.getEightDaysOn(begin);
                String formattedEnd = TimeUtils.getFormattedDate(end);
                // Err.pr( "END:   " + end);
                String periodTxt = "the week ahead";
                MessagesContainer con = bo.writeAllRosteringMsgs(begin, end, periodTxt);
                String msgTitle = RosterSessionUtils.getProperty( "organisationAcroymn") + " Roster, week ending Sun, " + formattedEnd;
                Informer informer = new Emailer();
                textAreaDisplayPanel.getTextArea().setText("");
                informer.setMsgs(msgTitle, con.emails);
                textAreaDisplayPanel.getTextArea().append(informer.getFormattedMsgs());
                informer = new PhoneCaller();
                informer.setMsgs(msgTitle, con.phoneCalls);
                textAreaDisplayPanel.getTextArea().append(informer.getFormattedMsgs());
                surface.removeAll();
                surface.add(textAreaDisplayPanel, BorderLayout.CENTER);
                application.endActivity( this.getClass().getName());
            }
        }
    }
}
