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
package org.strandz.applic.petstore;

import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.interf.VisibleStrandAction;
import org.strandz.core.record.GoToOperation;
import org.strandz.core.record.PlayerI;
import org.strandz.core.record.SimpleOperation;
import org.strandz.core.applichousing.SimpleApplication;
import org.strandz.core.applichousing.SimplestApplicationStrandRunner;
import org.strandz.lgpl.util.Err;
import org.strandz.store.petstore.PetstoreApplicationData;

import javax.jdo.PersistenceManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayParticularAgain
{
    private static PetstoreApplicationData psd;
    private static PersistenceManager pm;
    private static MaintainOrdersStrand mo = new MaintainOrdersStrand();
    public static String AGAIN = "play/lookup_disappears.xml";
    public static final String REFILL = "play/refill.xml";

    public static void main(String s[])
    {
        if(s.length != 0)
        {
            processParams(s);
        }
        else
        {
            String str[] = {"PetstoreData", PetstoreApplicationData.databaseName};
            processParams(str);
        }
    }

    public static PetstoreApplicationData getData()
    {
        if(psd == null)
        {
            String str[] = {};
            main(str);
        }
        return psd;
    }

    /**
     * Like removing all the records of a table but
     * leaving the table there!
     */
    private static void processParams(String s[])
    {
         /**/
        if(s[0].equals("PetstoreData"))
        {
            if(s.length == 1)
            {
                Err.error("Need to explicitly specify a database to replay against");
            }
            if(s[1].equals(PetstoreApplicationData.PROD))
            {
                Err.error(
                    "Cannot replay against the " + PetstoreApplicationData.PROD
                        + " database");
            }
            if(s.length == 2)
            {
                psd = PetstoreApplicationData.getNewInstance(s[1]);
            }
            else
            {
                psd = PetstoreApplicationData.getNewInstance(s[1], s[2]);
            }
            replay();
        }
        else
        {
            Err.error("Unrecognised param " + s[0]);
        }
    }

    private static void installActions()
    {/*
     * Use this type of code if want actions that only exist when replay
     ChangeNodeAction cnAction = new ChangeNodeAction();
     cnAction.putValue( Action.NAME, "Change Node");
     cnAction.putValue( Action.SHORT_DESCRIPTION, "Change Node");
     strand.getVolunteerAbilities().add( cnAction);
     strand.getRosterSlotAbilities().add( cnAction);
     //
     DebugDataAction ddAction = new DebugDataAction();
     ddAction.putValue( Action.NAME, "Debug Data");
     ddAction.putValue( Action.SHORT_DESCRIPTION, "Debug Data");
     strand.getVolunteerAbilities().add( ddAction);
     strand.getRosterSlotAbilities().add( ddAction);
     */}

    private static void replay()
    {
        replay(new File(AGAIN), false);
    }

    public static void replay(File file, boolean commit)
    {
        Err.setBatch(false);

        // mo = new MaintainOrdersStrand();
        SimpleApplication simple = new SimpleApplication();
        mo.setApplication(simple);
        simple.setDataStore(mo.getDataStore());

        VisibleStrandAction vsa = new VisibleStrandAction("MaintainOrders",
            "MaintainOrders");
        vsa.setVisibleStrand(mo);
        simple.addItem(vsa);

        SimplestApplicationStrandRunner strandRunner = new SimplestApplicationStrandRunner(simple);
        mo.sdzInit();
        strandRunner.execute(vsa);
        ((PlayerI) mo.getSbI().getStrand()).replayRecorded(file.getPath(), null,
            getStartupCode());
        if(commit)
        {
            mo.getSbI().getStrand().COMMIT_ONLY();
        }
    }

    public static List getStartupCode()
    {
        List result = new ArrayList();
        GoToOperation goTo = new GoToOperation();
        goTo.setNodeName("Account Node");
        goTo.setOperation(OperationEnum.GOTO_NODE);
        result.add(goTo);

        SimpleOperation simple = new SimpleOperation();
        simple.setOperation(OperationEnum.EXECUTE_QUERY);
        result.add(simple);
        return result;
    }
}
