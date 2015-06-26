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

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PhoneCaller extends Informer
{
    private boolean debugOn = false;
    private List outGoing;
    // For testing
    private static String toPhoneNumber1 = "0403 162 669";
    private static String toPhoneNumber2 = "(03) 9486 5557";

    public static void main(String[] args) throws Exception
    {
        PhoneCaller main = new PhoneCaller();
        List list = new ArrayList();
        String txt1 = "Dear Sir, " + Utils.NEWLINE + "\tFirst call";
        String txt2 = "Dear Sir, " + Utils.NEWLINE + "\tSecond call";
        list.add(new Msg(toPhoneNumber1, txt1));
        list.add(new Msg(toPhoneNumber2, txt2));
        main.setMsgs("Test", list);
        main.sendMsgs();
    }

    public PhoneCaller()
    {
    }

    public void setMsgs(String subject, List emails)
    {
        if(!emails.isEmpty())
        {
            if(!(emails.get(0) instanceof Msg))
            {
                Err.error(
                    "setMsgs() must be called with a list of Msgs, got a "
                        + emails.get(0).getClass().getName());
            }
        }
        this.subject = subject;
        this.outGoing = emails;
    }

    public String getFormattedMsgs()
    {
        chk();

        StringBuffer buf = new StringBuffer();
        for(Iterator iter = outGoing.iterator(); iter.hasNext();)
        {
            Msg msg = (Msg) iter.next();
            buf.append(Utils.NEWLINE);
            buf.append("---------------START CALL");
            buf.append(Utils.NEWLINE);
            buf.append(msg.toString(subject));
            buf.append(Utils.NEWLINE);
            buf.append("---------------END CALL");
            buf.append(Utils.NEWLINE);
        }
        return buf.toString();
    }

    public void sendMsgs()
    {
        chk();
        for(Iterator iter = outGoing.iterator(); iter.hasNext();)
        {
            Msg msg = (Msg) iter.next();
            Print.pr(msg.to);
            Print.pr(msg.txt);
        }
        unchk();
    }
}
