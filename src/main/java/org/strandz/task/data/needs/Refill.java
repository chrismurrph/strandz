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
package org.strandz.task.data.needs;

import org.strandz.data.needs.objects.Address;
import org.strandz.data.needs.objects.Contact;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.store.needs.NeedsApplicationData;
import org.strandz.store.needs.NeedsData;

import java.util.ArrayList;
import java.util.List;

public class Refill
{
    private static Contact con1;
    private static Contact con2;
    private static Contact con3;
    private static Address addr1;
    private static Address addr2;
    private static Address addr3;
    private static List sendingMediums;
    private static NeedsApplicationData td;
    private static boolean useCaptureFile = false;

    public static void main(String s[])
    {
        if(s.length != 0)
        {
            processParams(s);
        }
        else
        {
            String str[] = {"NeedsData", NeedsApplicationData.databaseName};
            processParams(str);
        }
    }

    public static NeedsApplicationData getData()
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
        if(s[0].equals("NeedsData"))
        {
            if(s.length == 1)
            {
                Err.error("Need to explicitly specify a database to refill");
            }
            if(s[1].equals(NeedsApplicationData.PROD))
            {
                Err.error(
                    "Cannot refill the " + NeedsApplicationData.PROD + " database");
            }
            if(s.length == 2)
            {
                td = NeedsApplicationData.getInstance(s[1]);
            }
            else
            {
                td = NeedsApplicationData.getInstance(s[1], s[2]);
            }
            // Err.error( "Use running of a capture file and scrap this!");
            if(!useCaptureFile)
            {
                td.getData().startTx();
                sendingMediums = (List) td.getData().get(NeedsData.SENDING_MEDIUM);

                 /**/
                List contacts = getContactData();
                Print.pr("************************************");
                Print.pr(contacts.size() + " contacts");
                Print.pr("************************************");
                td.getData().set(NeedsData.CONTACT, contacts);

                 /**/
                List addresses = getAddressData();
                Print.pr("************************************");
                Print.pr(addresses.size() + " addresses");
                Print.pr("************************************");
                td.getData().set(NeedsData.ADDRESS, addresses);
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

    private static List getContactData()
    {
        ArrayList contacts = new ArrayList();
        {
            Contact contact;
            contact = new Contact();
            contact.setFirstName("Donald");
            contact.setSecondName("Duck");
            contact.setAddress(addr1);
            contacts.add(contact);
            contact = new Contact();
            contact.setFirstName("Mickey");
            contact.setSecondName("Mouse");
            contact.setAddress(addr2);
            contacts.add(contact);
            contact = new Contact();
            contact.setFirstName("Minnie");
            contact.setSecondName("Mouse");
            contact.setAddress(addr3);
            contacts.add(contact);
        }
        return contacts;
    }

    private static List getAddressData()
    {
        ArrayList addresss = new ArrayList();
        {
            Address address;
            address = new Address();
            address.setStreet("135 Shaftesbury Rd");
            address.setSuburb("Eastwood");
            address.setPostcode("2122");
            addresss.add(address);
            addr1 = address;
            address = new Address();
            address.setStreet("45 Cowles Rd");
            address.setSuburb("MOSMAN");
            address.setPostcode("2088");
            addresss.add(address);
            addr2 = address;
            address = new Address();
            address.setStreet("10 Charles Street");
            address.setSuburb("Balmain");
            address.setPostcode("2041");
            addresss.add(address);
            addr3 = address;
        }
        return addresss;
    }
}
