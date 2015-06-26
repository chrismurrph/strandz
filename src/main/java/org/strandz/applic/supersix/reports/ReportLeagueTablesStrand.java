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

import org.strandz.core.interf.Application;
import org.strandz.core.interf.VisibleStrandAdapter;
import org.strandz.core.interf.VisibleStrandI;
import org.strandz.data.supersix.business.Reports;
import org.strandz.data.supersix.objects.Division;
import org.strandz.lgpl.util.ComponentUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.FileUtils;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.ValidationException;
import org.strandz.lgpl.widgets.TextAreaDisplayPanel;
import org.strandz.lgpl.text.NameAndNumberTokenizerConcreteFactory;
import org.strandz.lgpl.text.Appender;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ReportLeagueTablesStrand extends VisibleStrandAdapter implements VisibleStrandI
{
    private Application application;
    private JComponent surface;
    private TextAreaDisplayPanel textAreaDisplayPanel;
    private Reports reports;
    
    private static final boolean HTML_TOO = true;
    
    public ReportLeagueTablesStrand(Application a)
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
    
    private static String formatHeading( Division division)
    {
        String TAB = Reports.TAB;
        StringBuffer result = new StringBuffer();
        result.append(FileUtils.DOCUMENT_SEPARATOR);
        result.append( Utils.leftPadSpace(division.getName(), Reports.TeamPointsInfo.NAME_WIDTH) + TAB);
        result.append( Utils.leftPadSpace("P", Reports.TeamPointsInfo.SMALLEST_WIDTH) + TAB);
        result.append( Utils.leftPadSpace("W", Reports.TeamPointsInfo.SMALLEST_WIDTH) + TAB);
        result.append( Utils.leftPadSpace("D", Reports.TeamPointsInfo.SMALLEST_WIDTH) + TAB);
        result.append( Utils.leftPadSpace("L", Reports.TeamPointsInfo.SMALLEST_WIDTH) + TAB);
        result.append( Utils.leftPadSpace("F", Reports.TeamPointsInfo.SMALLEST_WIDTH) + TAB);
        result.append( Utils.leftPadSpace("A", Reports.TeamPointsInfo.SMALLEST_WIDTH) + TAB);
        result.append( Utils.leftPadSpace("GD", Reports.TeamPointsInfo.SMALLEST_WIDTH) + TAB);
        result.append( Utils.leftPadSpace("Pts", Reports.TeamPointsInfo.SMALLEST_WIDTH) + TAB);
        return result.toString();
    }

    private static List<String> outlineHeading( Division division)
    {
        List<String> result = new ArrayList<String>();
        result.add( division.getName());
        result.add( "P");
        result.add( "W");
        result.add( "D");
        result.add( "L");
        result.add( "F");
        result.add( "A");
        result.add( "GD");
        result.add( "Pts");
        return result;
    }
    
    public void display(boolean b)
    {
        if(b)
        {
            List infos = null;
            boolean ok = true;
            try
            {
                infos = reports.reportLeagueTables();
            }
            catch(ValidationException ex)
            {

                new MessageDlg( ex.getMsg(), JOptionPane.ERROR_MESSAGE);
                Err.pr( "DIALOG WARNING: " + ex.getMsg());
                ok = false;
            }
            if(ok)
            {
                NameAndNumberTokenizerConcreteFactory concreteFactory = null;
                Appender appender = null;
                String htmlTables[] = null;
                concreteFactory = new NameAndNumberTokenizerConcreteFactory();
                JTextArea textArea = textAreaDisplayPanel.getTextArea();
                appender = new Appender( textArea, concreteFactory, HTML_TOO);
                htmlTables = new String[infos.size()];
                for(int i = 0; i < infos.size(); i++)
                {
                    Reports.LeagueTableInfo leagueTableInfo = (Reports.LeagueTableInfo) infos.get(i);
                    concreteFactory.setRecognisedNames( leagueTableInfo.getTeamNames());
                    concreteFactory.setNumberSpacing( 3);
                    String headingText = formatHeading( leagueTableInfo.division);
                    List<String> headings = outlineHeading( leagueTableInfo.division);
                    appender.setHeadingText( headingText, headings, new Integer[]
                            {new Integer(44), new Integer(7), new Integer(7), new Integer(7), new Integer(7), 
                                    new Integer(7), new Integer(7), new Integer(7), new Integer(7)});
                    appender.endOfRow();
                    for(int j = 0; j < leagueTableInfo.pointsInfos.size(); j++)
                    {
                        Reports.TeamPointsInfo teamPointsInfo = leagueTableInfo.pointsInfos.get(j);
                        appender.addRow( teamPointsInfo.toString());
                    }
                    appender.endOfRow();
                    if(HTML_TOO)
                    {
                        String html = appender.endTable();
                        htmlTables[i] = html;
                    }
                }
                if(HTML_TOO)
                {
                    ComponentUtils.copyToClipboard( htmlTables);
                    new MessageDlg( "Report has been copied to the Clipboard as HTML");
                }
                textAreaDisplayPanel.scrollToTop();
                surface.add(textAreaDisplayPanel, BorderLayout.CENTER);
            }
        }
    }

    private List<String> formLeagueTableHeadings( String headingText) 
    {
        List<String> result = new ArrayList<String>();
        org.strandz.lgpl.text.NameAndNumberTokenizer textReader = new org.strandz.lgpl.text.NameAndNumberTokenizer( new StringBuffer( headingText));
        textReader.setRecognisedNames( Utils.formList( Division.FIRST_DIVISION.getName()));
        String word;
        while(!(word = textReader.readToken()).equals( ""))
        {
            result.add( word);
        }
        return result;
    }
}
