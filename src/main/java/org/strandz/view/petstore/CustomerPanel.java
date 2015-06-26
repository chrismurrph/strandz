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
package org.strandz.view.petstore;

import org.strandz.lgpl.tablelayout.ModernTableLayout;

import javax.swing.JPanel;
import java.awt.Dimension;

public class CustomerPanel extends JPanel
{
    private AccountPanel accountPanel;
    private OrderPanel orderPanel;
    private LineItemPanel lineItemPanel;

    public void init()
    {
        double size[][] = {{ModernTableLayout.FILL}, // Columns
            {0.33, 0.33, ModernTableLayout.FILL}}; // Rows
        setLayout(new ModernTableLayout(size));
        accountPanel = new AccountPanel();
        accountPanel.init();
        orderPanel = new OrderPanel();
        orderPanel.init();
        lineItemPanel = new LineItemPanel();
        lineItemPanel.init();
        add(accountPanel, "0, 0");
        add(orderPanel, "0, 1");
        add(lineItemPanel, "0, 2");
        setName("CustomerPanel");

        Dimension preferredSize = getPreferredSize();
        preferredSize.height = 550;
        preferredSize.width = 750;
        setPreferredSize(preferredSize);
        setAccountPanel(accountPanel);
        setOrderPanel(orderPanel);
        setLineItemPanel(lineItemPanel);
    }

    public AccountPanel getAccountPanel()
    {
        return accountPanel;
    }

    public void setAccountPanel(AccountPanel accountPanel)
    {
        this.accountPanel = accountPanel;
    }

    public LineItemPanel getLineItemPanel()
    {
        return lineItemPanel;
    }

    public void setLineItemPanel(LineItemPanel lineItemPanel)
    {
        this.lineItemPanel = lineItemPanel;
    }

    public OrderPanel getOrderPanel()
    {
        return orderPanel;
    }

    public void setOrderPanel(OrderPanel orderPanel)
    {
        this.orderPanel = orderPanel;
    }
}
