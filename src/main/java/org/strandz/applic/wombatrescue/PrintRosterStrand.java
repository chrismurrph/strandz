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

import org.strandz.core.interf.Application;
import org.strandz.core.interf.VisibleStrandAdapter;
import org.strandz.core.interf.VisibleStrandI;
import org.strandz.data.wombatrescue.business.ParticularRosterI;
import org.strandz.data.wombatrescue.business.RosterBusinessUtils;
import org.strandz.data.wombatrescue.business.RostererSessionI;
import org.strandz.data.wombatrescue.util.RosteringConstants;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.widgets.TextAreaDisplayPanel;
import org.strandz.view.wombatrescue.UploadCapableMonthYearPanel;
import org.strandz.view.wombatrescue.MonthYearPanel;

import javax.swing.JComponent;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * If thinking of trying to make this run on its own,
 * then first of all make it managed, so that panels
 * go into xml and code to create the JFrame is generated.
 */
public class PrintRosterStrand extends VisibleStrandAdapter implements VisibleStrandI, ActionListener
{
    private Application application;
    private JComponent surface;
    private UploadCapableMonthYearPanel monthYearPanel;
    private TextAreaDisplayPanel textAreaDisplayPanel;
    private ParticularRosterI particularRoster;
    private RostererSessionI rostererSession;
    // private boolean doneAlready;
    // static final String VIEW_TITLE = "Return to Printed Roster";
    // static final String PRINT_TITLE = "Print Roster";
    // static final String VIEW_TITLE_SHORT = "Vols";
    // static final String PRINT_TITLE_SHORT = "Non Vols";

    public PrintRosterStrand(Application a)
    {
        this.application = a;
    }

    public void sdzInit()
    {
        surface = application.getStrandArea(this);
        textAreaDisplayPanel = new TextAreaDisplayPanel();
        //No need for rosterer to manually select now copying to clipboard via button:
        //Above true, but it is convenient to highlight so can send emails with bits of the roster in them
        //textAreaDisplayPanel.getTextArea().setHighlighter( null);
        monthYearPanel = new UploadCapableMonthYearPanel();
        monthYearPanel.init();
    }

    public void setBusinessObjects(ParticularRosterI particularRoster, RostererSessionI rostererSession)
    {
        Assert.notNull( particularRoster.getDataStore(), particularRoster.getClass().getName() + " requires a DataStore");
        Assert.notNull( rostererSession.getDataStore());
        this.particularRoster = particularRoster;
        this.rostererSession = rostererSession;
    }

    public void display(boolean b)
    {
        if(b)
        {
            application.startActivity( this.getClass().getName());
            Assert.notNull( particularRoster, "No particularRoster - which is needed to invoke the rostering service");
            Assert.notNull( particularRoster.getWhenMonth(), "No particularRoster.getWhenMonth()");
            Assert.notNull( monthYearPanel, "No monthYearPanel");
            monthYearPanel.tfMonth.setText(particularRoster.getWhenMonth().getName());
            monthYearPanel.tfYear.setText(Integer.toString(particularRoster.getWhenYear()));
            StringBuffer buf = particularRoster.display(RosteringConstants.ROSTER);
            //buf will be null when server is down, and when here a message
            //will have already gone to the user
            String txt;
            if(buf != null)
            {
                txt = buf.toString();
                monthYearPanel.getBUpload().removeActionListener( this);
                monthYearPanel.getBUpload().addActionListener( this);
                monthYearPanel.getBUploadAsOld().removeActionListener( this);
                monthYearPanel.getBUploadAsOld().addActionListener( this);
            }
            else
            {
                txt = "Roster unable to be displayed";
            }
            textAreaDisplayPanel.getTextArea().setText("");
            textAreaDisplayPanel.getTextArea().append( txt);
            textAreaDisplayPanel.scrollToTop();
            surface.add(textAreaDisplayPanel, BorderLayout.CENTER);
            surface.add(monthYearPanel, BorderLayout.NORTH);
            application.endActivity( this.getClass().getName());
        }
    }
    
    public String getRosterText()
    {
        return textAreaDisplayPanel.getTextArea().getText();
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == monthYearPanel.getBUpload())
        {
            RosterBusinessUtils.uploadRoster( false, getRosterText(), rostererSession, particularRoster);
        }
        else if(e.getSource() == monthYearPanel.getBUploadAsOld())
        {
            RosterBusinessUtils.uploadRoster( true, getRosterText(), rostererSession, particularRoster);
        }
        else
        {
            Err.error();
        }
    }
}
