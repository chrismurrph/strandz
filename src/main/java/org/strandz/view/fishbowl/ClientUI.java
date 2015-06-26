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
package org.strandz.view.fishbowl;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class ClientUI extends JPanel
{
    public ClientGUISizedForBoxLayout clientGUI;
    public AddressGUISized addressGUIStreet;
    public AddressGUISized addressGUIPostal;
    public ContactUI contactUI;
    private JPanel pLeft;
    private JPanel pRight;
    private static int times = 0;

    public ClientUI()
    {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        /*
        times++;
        Err.pr( "ClientUI constructor has finished, times " + times);
        if(times == 2)
        {
        //Err.error();
        }
        */
    }

    public ClientGUISizedForBoxLayout getClientGUI()
    {
        return clientGUI;
    }

    public void setClientGUI(ClientGUISizedForBoxLayout clientGUI)
    {
        this.clientGUI = clientGUI;
    }

    public ContactUI getContactUI()
    {
        return contactUI;
    }

    public void setContactUI(ContactUI contactUI)
    {
        this.contactUI = contactUI;
    }

    public void otherinit()
    {
        init();
    }

    public void init()
    {
        clientGUI = new ClientGUISizedForBoxLayout();
        clientGUI.init();
        // contactUI = new ContactUI( new Dimension(clientGUI.getPreferredSize().width,
        // clientGUI.getPreferredSize().height));
        contactUI = new ContactUI();
        contactUI.init();
        contactUI.setPreferredSize(clientGUI.getPreferredSize());
        pLeft = new JPanel();
        pLeft.setLayout(new BoxLayout(pLeft, BoxLayout.Y_AXIS));
        addressGUIStreet = new AddressGUISized();
        addressGUIStreet.init();
        addressGUIStreet.setTitle("Street Address");
        addressGUIStreet.setName("Street Address, LHS");
        pLeft.add(clientGUI);
        pLeft.add(addressGUIStreet);
        pRight = new JPanel();
        pRight.setLayout(new BoxLayout(pRight, BoxLayout.Y_AXIS));
        pRight.add(contactUI);
        /* temp, to avoid confusion while debugging */
        addressGUIPostal = new AddressGUISized();
        addressGUIPostal.init();
        addressGUIPostal.setTitle("Postal Address");
        addressGUIPostal.setName("Postal Address, RHS");
        pRight.add(addressGUIPostal);
         /**/
        add(pLeft);
        add(pRight);
        setName("clientUI");
        // Err.pr( "ClientUI.init has finished");
    }
}
