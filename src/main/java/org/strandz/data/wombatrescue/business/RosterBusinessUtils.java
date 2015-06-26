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
package org.strandz.data.wombatrescue.business;

import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.store.EntityManagedDataStore;
import org.strandz.lgpl.persist.SdzEntityManagerI;
import org.strandz.lgpl.note.WombatNote;
import org.strandz.lgpl.data.objects.DayInWeek;
import org.strandz.lgpl.data.objects.MonthInYearI;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ComponentUtils;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.NameUtils;
import org.strandz.lgpl.util.Assert;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.data.wombatrescue.util.RosterUtils;
import org.strandz.data.wombatrescue.calculated.ParticularShift;
import org.strandz.data.wombatrescue.calculated.RosterTransferObj;
import org.strandz.data.wombatrescue.calculated.Night;
import org.strandz.data.wombatrescue.calculated.ClientObjProvider;

import javax.swing.JOptionPane;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class RosterBusinessUtils
{
    /**
     * @param vols If empty then return all the particularShifts
     * @param particularShifts All the particularShifts for a month from which the selection will be made
     * @param ds, DataStore in, used to check that each vol is entity managed (rather than has come from Spring)
     * @return All the shifts that belong to the list of vols passed in
     */
    public static List getParticularShifts(List vols, List particularShifts, DataStore ds, boolean memory, boolean all)
    {
        List result = new ArrayList();
        //Err.pr( "Will look thru " + particularShifts.size() + " shifts");
        if(!particularShifts.isEmpty())
        {
            //Err.pr( "First " + particularShifts.get( 0));
            //Err.pr( "Last " + particularShifts.get( particularShifts.size()-1));
            //Err.pr( "First volunteer " + ((ParticularShift)particularShifts.get( 0)).getWorker());
            //Err.pr( "Last volunteer " + ((ParticularShift)particularShifts.get( particularShifts.size()-1)).getWorker());
        }
        SdzEntityManagerI em = null;
        if(ds instanceof EntityManagedDataStore)
        {
            em = ((EntityManagedDataStore)ds).getEM();
        }
        for(Iterator iterator = particularShifts.iterator(); iterator.hasNext();)
        {
            ParticularShift particShift = (ParticularShift) iterator.next();
            if(particShift.getWorker() != null)
            {
                if(
                        particShift.getWorker().isStrange()
                        //equivalent
                        //particShift.getNight().getDayInWeek().equals( DayInWeek.SUNDAY)
                        )
                {
                    if(ParticularShift.STRANGE_WORKER_NOT_ALLOWED)
                    {
                        Err.error( "A Sunday ParticularShift should not have a Worker, but has " + 
                                particShift.getWorker() + " who isDummy() " + 
                                particShift.getWorker().isDummy() + " and isStrange() " + 
                                particShift.getWorker().isStrange());
                    }
                }
                else
                {
                    // getWorkers() going right back to DataStore is cause of requiring
                    // equals()
                    // if(vol == particShift.getWorker())
                    WorkerI vol = particShift.getWorker();
                    if(all || RosterUtils.contains(vol, vols, em, memory))
                    {
                        boolean ok = result.add(particShift);
                        if(!ok)
                        {
                            //Err.error( "Not added " + particShift + " for " + vols.get( 0));
                        }
                        else
                        {
                            if(vols.size() == 1)
                            {
                                //Err.pr( "Have added " + particShift);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
    
    public static void checkSpreadOfDays( List<ParticularShift> shifts, MonthInYearI month)
    {
        if(WombatNote.BAD_ALLOCATION_BM_EMAILS.isVisible())
        {
            boolean hasMonday = false;
            boolean hasTuesday = false;
            for(ParticularShift shift : shifts)
            {
                if(shift.getNight().getDayInWeek().equals(DayInWeek.MONDAY))
                {
                    hasMonday = true;
                }
                else if(shift.getNight().getDayInWeek().equals(DayInWeek.TUESDAY))
                {
                    hasTuesday = true;
                }
                if(hasMonday && hasTuesday)
                {
                    break;
                }
            }
            if(!(hasMonday && hasTuesday))
            {
                Err.pr( "Has at least one Monday " + hasMonday);
                Err.pr( "Has at least one Tuesday " + hasTuesday);
                Err.error( "Expect to have at least a Monday and a Tuesday operating as shifts at least once each in " + month);
            }
            else
            {
                Err.pr( "Roster seems balanced");
            }
        }
    }
    
    public static void uploadRoster( boolean old, String rosterText, RostererSessionI rostererSession,
                                     ParticularRosterI particularRoster)
    {
        ComponentUtils.copyToClipboard( rosterText);
        if(rostererSession.isUploadRosterServiceImplemented())
        {
            String fileName;
            if(old)
            {
                fileName = particularRoster.uploadRosterAsOld();
            }
            else
            {
                fileName = particularRoster.uploadRoster();
            }
            if(fileName == null)
            {
                new MessageDlg( "Failure uploading roster onto server");
            }
            else
            {
                String msgs[] = new String[3];
                msgs[0] = "Roster has been copied to the Clipboard.";
                msgs[1] = "Also it has been uploaded onto the server";
                String url = "<" + RosterUtils.translateToURL( fileName) + ">";
                msgs[2] = "Set your browser to " + url + ".";
                new MessageDlg( NameUtils.colourInVariables( msgs));
            }
        }
        else
        {
            String msgs[] = new String[4];
            msgs[0] = "Roster has been copied to the Clipboard.";
            msgs[1] = "It has not been uploaded onto the server as this";
            msgs[2] = "is not possible with this 'non-server capable'";
            msgs[3] = "version of the application.";
            new MessageDlg( msgs, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    static void prepareForClientUse( RosterTransferObj rosterTransferObj, 
                                     EntityManagedDataStore dataStore)
    {
        Assert.notNull( dataStore);
        ClientObjProvider clientObjProvider = rosterTransferObj.getRosterMonth().getClientObjProvider();
        rosterTransferObj.getRosterMonth().setupForClientUse( clientObjProvider);
        List<Night> nights = rosterTransferObj.getRosterMonth().getNights();
        for(Iterator<Night> nightIterator = nights.iterator(); nightIterator.hasNext();)
        {
            Night night = nightIterator.next();
            night.setClientObjProvider( clientObjProvider);
        }
        clientObjProvider.setDataStore( dataStore);
        //clientObjProvider.setRostererSession( rostererSession);
        for(ParticularShift particularShift : rosterTransferObj.getRosterMonth().particularShifts)
        {
            //delay anything too intensive till later - so see particularShift for how this used
            particularShift.setClientObjProvider(clientObjProvider);
        }
    }
}
