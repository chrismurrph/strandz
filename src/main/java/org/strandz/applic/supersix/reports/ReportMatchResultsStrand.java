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
package org.strandz.applic.supersix.reports;

import org.strandz.core.interf.VisibleStrandAdapter;
import org.strandz.core.interf.Application;
import org.strandz.core.interf.VisibleStrandI;
import org.strandz.data.supersix.business.Reports;
import org.strandz.data.supersix.business.SuperSixManagerUtils;
import org.strandz.data.supersix.objects.Division;
import org.strandz.data.supersix.objects.CompetitionSeason;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.FileUtils;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.widgets.TextAreaDisplayPanel;

import javax.swing.JComponent;
import java.awt.BorderLayout;
import java.util.Iterator;
import java.util.List;

public class ReportMatchResultsStrand extends VisibleStrandAdapter implements VisibleStrandI
{
    private Application application;
    private JComponent surface;
    private TextAreaDisplayPanel textAreaDisplayPanel;
    private Reports reports;

    public ReportMatchResultsStrand(Application a)
    {
        this.application = a;
    }

    public void sdzInit()
    {
        surface = application.getStrandArea(this);
        textAreaDisplayPanel = new TextAreaDisplayPanel();
    }

    public void setReports(Reports reports)
    {
        this.reports = reports;
    }

    public void display(boolean b)
    {
        if(b)
        {
            List infos = reports.reportMatchResults();
            textAreaDisplayPanel.getTextArea().setText("");
            CompetitionSeason currentCompetitionSeason = SuperSixManagerUtils.getCurrentCompetitionSeason();
            List divisions = currentCompetitionSeason.getDivisions();
            for(int i = 0; i < infos.size(); i++)
            {
                Reports.MeetInfo meetInfo = (Reports.MeetInfo) infos.get(i);
                if(divisions.size() != meetInfo.matchInfos.size())
                {
                    Print.prList( divisions, "divisions");
                    Print.prMap( meetInfo.matchInfos);
                    Err.error( "divisions and matchInfos should be the same size");
                }
                textAreaDisplayPanel.getTextArea().append( meetInfo.toString());
                textAreaDisplayPanel.getTextArea().append( FileUtils.DOCUMENT_SEPARATOR);
                for(int j = 0; j < meetInfo.matchInfos.size(); j++)
                {
                    Division division = (Division)divisions.get( j);
                    textAreaDisplayPanel.getTextArea().append( division.getName());
                    textAreaDisplayPanel.getTextArea().append( FileUtils.DOCUMENT_SEPARATOR);
                    List list = meetInfo.matchInfos.get( division);
                    if(list == null)
                    {
                        Err.pr( "No matches exist for " + division + " for " + currentCompetitionSeason);
                    }
                    else
                    {
                        for(Iterator iterator = list.iterator(); iterator.hasNext();)
                        {
                            Reports.MatchInfo matchInfo = (Reports.MatchInfo) iterator.next();
                            Assert.notNull( matchInfo, "null matchInfo");
                            String gameLine = matchInfo.toString();
                            textAreaDisplayPanel.getTextArea().append( gameLine);
                        }
                        textAreaDisplayPanel.getTextArea().append( FileUtils.DOCUMENT_SEPARATOR);
                    }
                    textAreaDisplayPanel.getTextArea().append( FileUtils.DOCUMENT_SEPARATOR);
                }
                /*
                textAreaDisplayPanel.getTextArea().append( meetInfo.toString());
                textAreaDisplayPanel.getTextArea().append( FileUtils.DOCUMENT_SEPARATOR);
                textAreaDisplayPanel.getTextArea().append( Division.DIVISION_MALE.getName());
                textAreaDisplayPanel.getTextArea().append( FileUtils.DOCUMENT_SEPARATOR);
                for(int j = 0; j < meetInfo.menMatchInfos.size(); j++)
                {
                    Reports.MatchInfo matchInfo = (Reports.MatchInfo) meetInfo.menMatchInfos.get(j);
                    textAreaDisplayPanel.getTextArea().append( matchInfo.toString());
                }
                textAreaDisplayPanel.getTextArea().append( FileUtils.DOCUMENT_SEPARATOR);
                textAreaDisplayPanel.getTextArea().append( FileUtils.DOCUMENT_SEPARATOR);
                textAreaDisplayPanel.getTextArea().append( Division.DIVISION_FEMALE.getName());
                textAreaDisplayPanel.getTextArea().append( FileUtils.DOCUMENT_SEPARATOR);
                for(int j = 0; j < meetInfo.womenMatchInfos.size(); j++)
                {
                    Reports.MatchInfo matchInfo = (Reports.MatchInfo) meetInfo.womenMatchInfos.get(j);
                    textAreaDisplayPanel.getTextArea().append( matchInfo.toString());
                }
                */
            }
            textAreaDisplayPanel.scrollToTop();
            surface.add(textAreaDisplayPanel, BorderLayout.CENTER);
        }
    }
}
