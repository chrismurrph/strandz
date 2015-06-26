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
import org.strandz.lgpl.util.Utils;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class Emailer extends Informer
{
    private static final String SMTP_MAIL = "smtp";
    private boolean debugOn = false;
    /*
    private String smtpHost;
    private String user;
    private String password;
    private String fromEmail = "from-person@provider.net";
    */
    private Session session;
    private List outGoing;
    private Address recipients[];
    // For testing
    private static final String TO_EMAIL1 = "from-person@provider.com";
    private static final String TO_EMAIL2 = "silly";
    private static final String TO_EMAIL3 = "from-person@provider.net";
    //
    private static final boolean fromGmail = true;

    public static void main(String[] args) throws Exception
    {
        //ensures properties will be read in
        RosterSessionUtils.setGlobalRostererSession( RostererSessionFactory.newRostererSession( RostererSessionFactory.APP_PROPERTIES_ONLY)); 
        EmailerTrial main = new EmailerTrial();
        List list = new ArrayList();
        String txt1 = "Dear Sir, " + Utils.NEWLINE + "\tFirst email";
        String txt2 = "Dear Sir, " + Utils.NEWLINE + "\tSecond email";
        String txt3 = "Dear Sir, " + Utils.NEWLINE + "\tThird email";
        list.add(new Msg(TO_EMAIL1, TO_EMAIL1, txt1));
        list.add(new Msg(TO_EMAIL1, TO_EMAIL2, txt2));
        list.add(new Msg(TO_EMAIL1, TO_EMAIL3, txt3));
        main.setMsgs("Test", list);
        main.sendMsgs();
    }

    /*
    private void populateMTADetails()
    {
      String propFileName = WombatConstants.PROPERTY_FILE_NAME;
      Properties props = pUtils.getPortableProperties( propFileName, this);
      smtpHost = pUtils.getProperty( "smtpHost", props);
      user = pUtils.getProperty( "smtpUser", props);
      password = pUtils.getProperty( "smtpPassword", props);
    }
    */

    public Emailer()
    {
        Properties props = new Properties();
        if(fromGmail)
        {
            props.put("mail.smtp.starttls.enable", "true");
        }
        props.put("mail.smtp.host", RosterSessionUtils.getProperty( "smtpHost"));
        session = Session.getDefaultInstance(props, null);
    }

    public void setMsgs(String subject, List msgs)
    {
        if(!msgs.isEmpty())
        {
            if(!(msgs.get(0) instanceof Msg))
            {
                Err.error("setEmails() must be called with a list of Emails");
            }
        }
        this.subject = subject;
        this.outGoing = msgs;
    }

    public String getFormattedMsgs()
    {
        chk();

        StringBuffer buf = new StringBuffer();
        for(Iterator iter = outGoing.iterator(); iter.hasNext();)
        {
            Msg email = (Msg) iter.next();
            buf.append(Utils.NEWLINE);
            buf.append(email.toString(subject));
        }
        return buf.toString();
    }

    public void sendMsgs()
    {
        chk();

        Exception error;
        for(Iterator iter = outGoing.iterator(); iter.hasNext();)
        {
            error = null;

            Msg email = (Msg) iter.next();
            Message msg = constructMessage(email.txt, email.to, email.from);
            Transport transport = null;
            try
            {
                transport = session.getTransport(SMTP_MAIL);
            }
            catch(NoSuchProviderException ex)
            {
                error = ex;
            }
            if(error == null)
            {
                if(transport != null)
                {
                    try
                    {
                        transport.connect(
                                RosterSessionUtils.getProperty( "smtpHost"), 
                                RosterSessionUtils.getProperty( "smtpUser"), 
                                RosterSessionUtils.getProperty( "smtpPassword"));
                    }
                    catch(MessagingException ex)
                    {
                        error = ex;
                    }
                    if(error == null)
                    {
                        try
                        {
                            transport.sendMessage(msg, recipients);
                        }
                        catch(MessagingException ex)
                        {
                            error = ex;
                        }
                    }
                }
                else
                {
                    Err.error();
                }
            }
            if(error != null)
            {
                Err.pr("PROBLEM sending email, details follow:");
                prRecipients(recipients);
                Err.pr(error.toString());
                Err.pr("");
            }
            else
            {
                Err.pr( "Have successfully sent an email to " + email.to);
            }
        }
        unchk();
    }

    private void prRecipients(Address recipients[])
    {
        for(int i = 0; i < recipients.length; i++)
        {
            InternetAddress recipient = (InternetAddress) recipients[i];
            Err.pr(recipient.getAddress());
        }
    }

    private Message constructMessage(String txt, String toEmail, String fromEmail)
    {
        MimeMessage result = new MimeMessage(session);
        if(Utils.isBlank(txt))
        {
            Err.error( "Emailer cannot construct a message with blank txt");
        }
        if(Utils.isBlank(toEmail))
        {
            Err.error( "Emailer cannot construct a message with blank toEmail");
        }
        if(Utils.isBlank(fromEmail))
        {
            Err.error( "Emailer cannot construct a message with blank fromEmail");
        }
        Address from = null;
        Address replyTo[] = new InternetAddress[1];
        recipients = new InternetAddress[1];
        try
        {
            from = new InternetAddress(fromEmail);
            replyTo[0] = from;
        }
        catch(AddressException ex)
        {
            Err.error(ex);
        }

        int i = 0;
        try
        {
            recipients[i] = new InternetAddress(toEmail);
        }
        catch(AddressException ex)
        {
            Err.error(ex);
        }
        try
        {
            result.setFrom(from);
            result.setReplyTo(replyTo);
            result.addRecipients(Message.RecipientType.TO, recipients);
            result.setSentDate(new Date());
            result.setSubject(subject);
            result.setText(txt);
        }
        catch(MessagingException ex)
        {
            Err.error(ex);
        }
        return result;
    }
}
