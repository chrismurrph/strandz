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
package org.strandz.task.data.fishbowl;

import org.strandz.core.applichousing.SdzBag;
import org.strandz.data.fishbowl.objects.Client;
import org.strandz.data.fishbowl.objects.ClientIndustry;
import org.strandz.data.fishbowl.objects.ClientType;
import org.strandz.data.fishbowl.objects.Contact;
import org.strandz.data.fishbowl.objects.Job;
import org.strandz.data.fishbowl.objects.JobRequirement;
import org.strandz.data.fishbowl.objects.JobType;
import org.strandz.data.fishbowl.objects.PostalAddress;
import org.strandz.data.fishbowl.objects.Promotion;
import org.strandz.data.fishbowl.objects.PromotionType;
import org.strandz.data.fishbowl.objects.SkillCategory;
import org.strandz.data.fishbowl.objects.StreetAddress;
import org.strandz.data.startsimple.Context;
import org.strandz.data.startsimple.ContextData;
import org.strandz.data.startsimple.ControllerUIClass;
import org.strandz.data.startsimple.CustomizerData;
import org.strandz.data.startsimple.GuiClass;
import org.strandz.lgpl.data.objects.Timesegment;
import org.strandz.lgpl.data.objects.Timespace;
import org.strandz.lgpl.store.DataStore;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.store.fishbowl.FishbowlDataStoreFactory;
import org.strandz.store.fishbowl.pFishbowlClasses;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Refill
{
    private static StreetAddress chrissPlace;
    private static PostalAddress chiswick;
    private static Client chrisClient = new Client();
    private static Client cornishClient = new Client();
    private static JobType whiteCollar = new JobType();
    private static JobType blueCollar = new JobType();
    private static Job specJob = new Job();
    private static ClientIndustry retailClientIndustry = new ClientIndustry();
    private static ClientType priceClientType = new ClientType();
    private static Promotion firstPromo = new Promotion();
    private static Promotion secondPromo = new Promotion();
    private static PromotionType christmasPromoType = new PromotionType();
    private static PromotionType beerPromoType = new PromotionType();

    //private static PersistenceManager pm;
    public static void main(String s[])
    {
        if(s.length != 0)
        {
            processParams(s);
        }
        else
        {
            String str[] = {"FishBowlData"};
            processParams(str);
            // Err.pr( "Specify an AbstData subclass as param to purge it");
        }
    }

    /**
     * Like removing all the records of a table but
     * leaving the table there!
     */
    private static void processParams(String s[])
    {
        for(int i = 0; i <= s.length - 1; i++)
        {
            if(s[i].equals("FishBowlData"))
            {
                DataStore fbd = new FishbowlDataStoreFactory( true).getDataStore();
                //DataStore fbd = FishbowlApplicationData.getInstance().getData();
                // FishBowlData fbd = new FishBowlData();
                fbd.rollbackTx();
                fbd.startTx();
                //pm = fbd.getPM();

                List streetAddresses = getFishBowlAddressData();
                List postalAddresses = getFishBowlPostalAddressData();
                List jobTypes = getFishBowlJobTypeData();
                List skillCategorys = getFishBowlSkillCategoryData();
                List clientIndustrys = getFishBowlClientIndustryData();
                List clientTypes = getFishBowlClientTypeData();
                List promotionTypes = getFishBowlPromotionTypeData();
                List clients = getFishBowlClientData();
                List promotions = getFishBowlPromotionData();
                List jobs = getFishBowlJobData();
                List jobRequirements = getJobRequirementData();
                Print.pr("************************************");
                Print.pr(clients.size() + " clients");
                Print.pr(jobs.size() + " jobs");
                Print.pr(promotionTypes.size() + " promotionTypes");
                Print.pr("************************************");
                fbd.set(pFishbowlClasses.STREET_ADDRESS, streetAddresses);
                fbd.set(pFishbowlClasses.POSTAL_ADDRESS, postalAddresses);
                fbd.set(pFishbowlClasses.JOB_TYPE, jobTypes);
                // fbd.set( JDOFishBowlData.SKILL_CATEGORY,
                // skillCategorys);
                fbd.set(pFishbowlClasses.CLIENT_INDUSTRY, clientIndustrys);
                fbd.set(pFishbowlClasses.CLIENT_TYPE, clientTypes);
                fbd.set(pFishbowlClasses.PROMOTION_TYPE, promotionTypes);
                fbd.set(pFishbowlClasses.CLIENT, clients);
                fbd.set(pFishbowlClasses.PROMOTION, promotions);
                fbd.set(pFishbowlClasses.JOB, jobs); // must be after client
                fbd.set(pFishbowlClasses.JOB_REQUIREMENT, jobRequirements); // must be after job I guess
                fbd.commitTx();
            }
            else if(s[i].equals("ContextData"))
            {
                ContextData fbd = new ContextData();
                fbd.startTx();
                fbd.set(ContextData.CONTEXT, getContextData());
                fbd.commitTx();
                Print.pr("Have refilled " + fbd.getFileName());
            }
            else if(s[i].equals("CustomizerData"))
            {
                CustomizerData fbd = new CustomizerData();
                fbd.startTx();
                fbd.set(CustomizerData.STRAND_CONTROL, getCustomizerData());
                fbd.commitTx();
                Print.pr("Have refilled " + fbd.getFileName());
            }
            else
            {
                Err.error("Unrecognised param " + s[i]);
            }
        }
    }

    private static List getCustomizerData()
    {
        ArrayList beans = new ArrayList();
        SdzBag sc;
        sc = new SdzBag();
        // sc.setBeansFile( "some file name");
        beans.add(sc);
        return beans;
    }

    private static List getContextData()
    {
        ArrayList contexts = new ArrayList();
        {
            Context c;
            c = new Context();
            c.setBeansFile("some file name");

            //
            ArrayList guiClasses = new ArrayList();
            GuiClass g;
            {
                g = new GuiClass();
                g.setName("set 1, line 1, GUI class");
            }
            guiClasses.add(g);
            {
                g = new GuiClass();
                g.setName("set 1, line 2, GUI class");
            }
            guiClasses.add(g);
            c.setGuiClasses(guiClasses);

            //


            ArrayList classes = new ArrayList();
            ControllerUIClass co;
            {
                co = new ControllerUIClass();
                co.setName("set 1, line 1, Controller UI");
            }
            classes.add(co);
            {
                co = new ControllerUIClass();
                co.setName("set 1, line 2, Controller UI");
            }
            classes.add(co);
            c.setControllerUIClasses(classes);
            //
            contexts.add(c);
            c = new Context();
            c.setBeansFile("some other file name");
            //
            guiClasses = new ArrayList();
            {
                g = new GuiClass();
                g.setName("set 2, line 1, GUI class");
            }
            guiClasses.add(g);
            {
                g = new GuiClass();
                g.setName("set 2, line 2, GUI class");
            }
            guiClasses.add(g);
            c.setGuiClasses(guiClasses);
            classes = new ArrayList();
            {
                co = new ControllerUIClass();
                co.setName("set 2, line 1, Controller UI");
            }
            classes.add(co);
            {
                co = new ControllerUIClass();
                co.setName("set 2, line 2, Controller UI");
            }
            classes.add(co);
            c.setControllerUIClasses(classes);
            //
            contexts.add(c);
        }
        return contexts;
    }

    private static List getFishBowlAddressData()
    {
        ArrayList data = new ArrayList();
        StreetAddress a = new StreetAddress();
        a.setStreet("1 Philip Avenue");
        a.setSuburb("Leabrook");
        a.setState("SA");
        a.setPostcode("5068");
        data.add(a);
        a = new StreetAddress();
        a.setStreet("348 Angas Street");
        a.setSuburb("Adelaide");
        a.setState("SA");
        a.setPostcode("5000");
        data.add(a);
        a = new StreetAddress();
        a.setStreet("4/8 Roberts Avenue");
        a.setSuburb("Randwick");
        a.setState("NSW");
        a.setPostcode("2031");
        data.add(a);
        a = new StreetAddress();
        a.setStreet("1/2 Victoria Road");
        a.setSuburb("Glebe");
        a.setState("NSW");
        a.setPostcode("2037");
        chrissPlace = a;
        data.add(a);
        return data;
    }

    private static List getFishBowlPostalAddressData()
    {
        ArrayList data = new ArrayList();
        PostalAddress a = new PostalAddress();
        a.setStreet("8/2 Bortfield Drive");
        a.setSuburb("Chiswick");
        a.setState("NSW");
        a.setPostcode("2046");
        chiswick = a;
        data.add(a);
        return data;
    }

    private static List getJobRequirementData()
    {
        ArrayList reqs = new ArrayList();
        JobRequirement jobReq = new JobRequirement();
        jobReq.setConfirmed(Boolean.valueOf( true));

        Timespace canWork;
        {
            canWork = new Timespace();

            Calendar start = Calendar.getInstance();
            Calendar finish = Calendar.getInstance();
            start.set(1999, 5 - 1, 14, 21, 13, 0);
            finish.set(1999, 5 - 1, 14, 23, 17, 0);

            //
            Timesegment ts = new Timesegment
                (start.getTime(), finish.getTime());
            // canWork.add( Timespace.DAY, Calendar.THURSDAY);
            // canWork.add( Timespace.DAY, Calendar.FRIDAY);
            canWork.add(Timespace.DATE_CLOSED_TIMESEGMENT, ts);
        }
        jobReq.setTimespace(canWork);
        jobReq.setJob(specJob);
        jobReq.setConfirmed(Boolean.valueOf( true));
        reqs.add(jobReq);
        return reqs;
    }

    private static List getFishBowlJobData()
    {
        ArrayList jobs = new ArrayList();
        {
            specJob.setClient(chrisClient);
            specJob.setDescription("Writing a spec");
            specJob.setContact("Kate");

            StreetAddress a;
            {
                /*
                * Will this address appear when query all the addresses?
                */
                a = new StreetAddress();
                a.setStreet("12a Illawarah Drive");
                a.setSuburb("Glebe");
                a.setState("NSW");
                a.setPostcode("2000");
            }
            specJob.setSite(a);

            JobRequirement jobReq = new JobRequirement();
            jobReq.setConfirmed(Boolean.valueOf( true));

            Timespace canWork;
            {
                canWork = new Timespace();

                Calendar start = Calendar.getInstance();
                Calendar finish = Calendar.getInstance();
                start.set(1999, 5 - 1, 14, 21, 13, 0);
                finish.set(1999, 5 - 1, 14, 23, 17, 0);

                //
                Timesegment ts = new Timesegment
                    (start.getTime(), finish.getTime());
                // canWork.add( Timespace.DAY, Calendar.THURSDAY);
                // canWork.add( Timespace.DAY, Calendar.FRIDAY);
                canWork.add(Timespace.DATE_CLOSED_TIMESEGMENT, ts);
            }
            jobReq.setTimespace(canWork);
            jobReq.setJob(specJob);
            jobReq.setConfirmed(Boolean.valueOf( true));
            jobs.add(specJob);

            Job j = new Job();
            j.setClient(chrisClient);
            j.setDescription("Integration testing");
            j.setContact("Kate");
            j.setSite(a); // same address
            jobs.add(j);

             /**/
            Job cleanManlyUnits = new Job();
            cleanManlyUnits.setDescription("Clean Manly units");

            Job doAccounts = new Job();
            doAccounts.setDescription("Do accounts");

            Job repairAirconditioning = new Job();
            repairAirconditioning.setDescription("Repair airconditioning");

            Job giveManagementAdvice = new Job();
            giveManagementAdvice.setDescription("Give management advice");
            Print.pr(chrisClient);
            Print.pr(whiteCollar);
            cleanManlyUnits.setClient(chrisClient);
            cleanManlyUnits.setJobType(blueCollar);
            jobs.add(cleanManlyUnits);
            doAccounts.setClient(chrisClient);
            doAccounts.setJobType(whiteCollar);
            jobs.add(doAccounts);
            repairAirconditioning.setClient(cornishClient);
            repairAirconditioning.setJobType(blueCollar);
            jobs.add(repairAirconditioning);
            giveManagementAdvice.setClient(cornishClient);
            giveManagementAdvice.setJobType(whiteCollar);
            jobs.add(giveManagementAdvice);
        }
        return jobs;
    }

    private static List getFishBowlClientData()
    {
        ArrayList data = new ArrayList();
        ArrayList contacts = new ArrayList();
        {
            Contact con = new Contact();
            con.setPerson("Gov Phillip");
            con.setTitle("CEO");
            contacts.add(con);
        }
        chrisClient.setContacts(contacts);
        chrisClient.setCompanyName("StingRay Bay");
        if(chrissPlace == null)
        {
            Err.error("Not want null");
        }
        chrisClient.setAddress(chrissPlace); // s/be no bother if null
        chrisClient.setPhone("02 9326 3664");
        chrisClient.setFax("02 9326 3664");
        chrisClient.setEmail("gov@seaweedsoftware.com.au");
        chrisClient.setDescription("Gov of the First Fleet");
        chrisClient.setClientIndustry(retailClientIndustry);
        chrisClient.setClientType(priceClientType);
        data.add(chrisClient);
        chrisClient.setEndClient(cornishClient);
        contacts = new ArrayList();
        {
            Contact con = new Contact();
            con.setPerson("Andrew Curnow");
            con.setTitle("NT Systems Administrator");
            contacts.add(con);
        }
        cornishClient.setContacts(contacts);
        cornishClient.setCompanyName("Cornish Pasties Pty Ltd");
        cornishClient.setPostalAddress(chiswick);
        cornishClient.setPhone("02 3333 3333");
        cornishClient.setFax("02 3333 3333");
        cornishClient.setEmail("andrewc@walker.com.au");
        cornishClient.setDescription("A small fishing company");
        data.add(cornishClient);
        return data;
    }

    private static List getFishBowlSkillCategoryData()
    {
        ArrayList data = new ArrayList();
        SkillCategory s = new SkillCategory();
        s.setDescription("Unskilled");
        data.add(s);
        s = new SkillCategory();
        s.setDescription("General Hospitality");
        data.add(s);
        s = new SkillCategory();
        s.setDescription("General Hospitality with dishwashing experience");
        data.add(s);
        s = new SkillCategory();
        s.setDescription("Chef ***");
        data.add(s);
        s = new SkillCategory();
        s.setDescription("Standard Waiter");
        data.add(s);
        s = new SkillCategory();
        s.setDescription("Silver Spoon Waiter");
        data.add(s);
        return data;
    }

    private static List getFishBowlClientIndustryData()
    {
        ArrayList data = new ArrayList();
        // ClientIndustry s = new ClientIndustry();
        retailClientIndustry.setDescription("Retail");
        data.add(retailClientIndustry);

        ClientIndustry s = new ClientIndustry();
        s.setDescription("Manufacturing");
        data.add(s);
        s = new ClientIndustry();
        s.setDescription("Information Technology");
        data.add(s);
        return data;
    }

    private static List getFishBowlClientTypeData()
    {
        ArrayList data = new ArrayList();
        // ClientType s = new ClientType();
        priceClientType.setDescription("Need to be very price competitive with");
        data.add(priceClientType);

        ClientType s = new ClientType();
        s.setDescription("Need to give speedy response to");
        data.add(s);
        s = new ClientType();
        s.setDescription("Needs us to be very technical up front");
        data.add(s);
        return data;
    }

    private static List getFishBowlPromotionData()
    {
        ArrayList data = new ArrayList();
        firstPromo = new Promotion();
        firstPromo.setPromotionType(christmasPromoType);
        firstPromo.setClient(chrisClient);
        data.add(firstPromo);
        secondPromo = new Promotion();
        secondPromo.setPromotionType(beerPromoType);
        secondPromo.setClient(chrisClient);
        data.add(secondPromo);
        return data;
    }

    private static List getFishBowlPromotionTypeData()
    {
        ArrayList data = new ArrayList();
        PromotionType s = new PromotionType();
        s.setDescription("Christmas cards 1999");
        christmasPromoType = s;
        data.add(s);
        s = new PromotionType();
        s.setDescription("Beer mats July 1999");
        beerPromoType = s;
        data.add(s);
        s = new PromotionType();
        s.setDescription("Cold calling 20th June 1999");
        data.add(s);
        return data;
    }

    private static List getFishBowlJobTypeData()
    {
        ArrayList data = new ArrayList();
        whiteCollar.setDescription("White collar");
        data.add(whiteCollar);
        blueCollar.setDescription("Blue collar");
        data.add(blueCollar);
        return data;
    }
}
