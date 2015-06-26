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
package org.strandz.task.data.fault;

import org.strandz.data.fault.objects.ClientContact;
import org.strandz.data.fault.objects.Fault;
import org.strandz.data.fault.objects.FaultStatus;
import org.strandz.data.fault.objects.Product;
import org.strandz.data.fault.objects.SupportPerson;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;
import org.strandz.store.fault.FaultApplicationData;
import org.strandz.store.fault.FaultData;

import java.util.ArrayList;
import java.util.List;

public class Refill
{
    private static SupportPerson supportPerson1;
    private static ClientContact clientContact1;
    private static ClientContact clientContact2;
    private static Fault fault1;
    private static Fault fault2;
    private static Fault fault3;
    private static List faultStatuses;
    private static List products;
    private static FaultApplicationData td;
    private static boolean useCaptureFile = false;

    public static void main(String s[])
    {
        if(s.length != 0)
        {
            processParams(s);
        }
        else
        {
            String str[] = {"FaultData", FaultApplicationData.databaseName};
            processParams(str);
        }
    }

    public static FaultApplicationData getData()
    {
        if(td == null)
        {
            String str[] = {};
            main(str);
        }
        return td;
    }

    /**
     * Like removing all the records of a table but
     * leaving the table there!
     */
    private static void processParams(String s[])
    {
         /**/
        if(s[0].equals("FaultData"))
        {
            if(s.length == 1)
            {
                Err.error("Need to explicitly specify a database to refill");
            }
            if(s[1].equals(FaultApplicationData.PROD))
            {
                Err.error(
                    "Cannot refill the " + FaultApplicationData.PROD + " database");
            }
            if(s.length == 2)
            {
                td = FaultApplicationData.getNewInstance(s[1]);
            }
            else
            {
                td = FaultApplicationData.getNewInstance(s[1], s[2]);
            }
            // Err.error( "Use running of a capture file and scrap this!");
            if(!useCaptureFile)
            {
                td.getData().startTx();
                faultStatuses = (List) td.getData().get(FaultData.FAULT_STATUS);
                products = (List) td.getData().get(FaultData.PRODUCT);

                 /**/
                List supportPersons = getSupportPersonData();
                Print.pr("************************************");
                Print.pr(supportPersons.size() + " supportPersons");
                Print.pr("************************************");
                td.getData().set(FaultData.SUPPORT_PERSON, supportPersons);

                 /**/
                List clientContacts = getClientContactData();
                Print.pr("************************************");
                Print.pr(clientContacts.size() + " clientContacts");
                Print.pr("************************************");
                td.getData().set(FaultData.CLIENT_CONTACT, clientContacts);

                 /**/
                List faults = getFaultData();
                Print.pr("************************************");
                Print.pr(faults.size() + " faults");
                Print.pr("************************************");
                td.getData().set(FaultData.FAULT, faults);
                 /**/
                td.getData().commitTx();
            }
            else
            {
                replay();
            }
        }
        else
        {
            Err.error("Unrecognised param " + s[0]);
        }
    }

    private static void replay()
    {
        Err.error("See Wombatrescue application for how to do this");
    }

    private static List getSupportPersonData()
    {
        ArrayList contacts = new ArrayList();
        {
            SupportPerson supp;
            supp = new SupportPerson();
            supp.setName("Graham Richardson");
            // supp.setAddress( addr1);
            contacts.add(supp);
        }
        return contacts;
    }

    private static List getClientContactData()
    {
        ArrayList clientContacts = new ArrayList();
        {
            ClientContact clientContact;
            clientContact = new ClientContact();
            clientContact.setName("Robin Cook");
            clientContacts.add(clientContact);
            clientContact1 = clientContact;
            clientContact = new ClientContact();
            clientContact.setName("Micheal Heseltine");
            clientContacts.add(clientContact);
            clientContact2 = clientContact;
        }
        return clientContacts;
    }

    private static List getFaultData()
    {
        ArrayList faults = new ArrayList();
        {
            Fault fault;
            fault = new Fault();
            fault.setFaultStatus(
                (FaultStatus) Utils.getFromList(faultStatuses, FaultStatus.FIXED));
            fault.setProduct(
                (Product) Utils.getFromList(products, Product.P_CONTROL_V2));
            fault.setName("Blank Screen");
            fault.setText(
                "Happens just after install. Caused by incorrect data setup");
            faults.add(fault);
            fault1 = fault;
            fault = new Fault();
            fault.setFaultStatus(
                (FaultStatus) Utils.getFromList(faultStatuses, FaultStatus.FIXED));
            fault.setProduct(
                (Product) Utils.getFromList(products, Product.P_CONTROL_V2));
            fault.setName("NPE on startup");
            fault.setText(
                "Happens just after install. Caused by incorrect data setup");
            faults.add(fault);
            fault2 = fault;
            fault = new Fault();
            fault.setFaultStatus(
                (FaultStatus) Utils.getFromList(faultStatuses, FaultStatus.FIXED));
            fault.setProduct(
                (Product) Utils.getFromList(products, Product.P_CONTROL_V2));
            fault.setName("Process not found");
            fault.setText("Customer has not purchased the multi-process kit");
            faults.add(fault);
            fault3 = fault;
        }
        return faults;
    }
}
