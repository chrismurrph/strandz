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
import org.strandz.core.interf.VisibleStrandI;
import org.strandz.core.interf.Application;
import org.strandz.data.wombatrescue.business.ParticularRosterI;
import org.strandz.data.wombatrescue.util.RosteringConstants;
import org.strandz.data.wombatrescue.business.RosterSessionUtils;
import org.strandz.data.wombatrescue.business.Reports;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.widgets.TextAreaDisplayPanel;
import org.strandz.view.wombatrescue.MonthYearPanel;

import javax.swing.JComponent;
import java.awt.BorderLayout;
import java.util.List;

public class ViewUnrosteredVolsStrand extends VisibleStrandAdapter implements VisibleStrandI
{
    private Application application;
    private JComponent surface;
    private MonthYearPanel monthYearPanel;
    private TextAreaDisplayPanel textAreaDisplayPanel;
    private Reports reports;

    public ViewUnrosteredVolsStrand(Application a)
    {
        this.application = a;
    }

    public void sdzInit()
    {
        surface = application.getStrandArea(this);
        textAreaDisplayPanel = new TextAreaDisplayPanel();
        monthYearPanel = new MonthYearPanel();
        monthYearPanel.init();
    }

    public void setReports(Reports reports)
    {
        this.reports = reports;
    }

    public void display(boolean b)
    {
        if(b)
        {
            ParticularRosterI bo = RosterSessionUtils.getGlobalCurrentParticularRoster();
            if(bo.getCurrentMonth() == null)
            {
                new MessageDlg("Do not have a current month");
            }
            else
            {
                application.startActivity( this.getClass().getName());
                List vols = reports.reportUnrosteredWorkers( false);
                monthYearPanel.tfMonth.setText(bo.getWhenMonth().getName());
                monthYearPanel.tfYear.setText(Integer.toString(bo.getWhenYear()));
                //StringBuffer buf = bo.display(RosteringConstants.UNROSTERED_VOLS);
                textAreaDisplayPanel.getTextArea().setText("");
                textAreaDisplayPanel.getTextArea().append( vols.toString());
                surface.removeAll();
                surface.add(textAreaDisplayPanel, BorderLayout.CENTER);
                surface.add(monthYearPanel, BorderLayout.NORTH);
                application.endActivity( this.getClass().getName());
            }
        }
    }
}
