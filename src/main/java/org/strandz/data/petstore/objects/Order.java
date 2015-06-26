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
package org.strandz.data.petstore.objects;

import org.strandz.lgpl.data.objects.MoneyAmount;

import java.util.Date;

public class Order
{
    private Account account;
    private Date orderDate;
    private String shipCountry;
    private String billCountry;
    private String courier;
    private MoneyAmount totalPrice;
    private String billToFirstName;
    private String billToLastName;
    private String creditCard;
    private String exprDate;
    private String cardType;

    public String toString()
    {
        String result = "";
        if(orderDate != null)
        {
            result = orderDate.toString();
        }
        if(account != null)
        {
            if(orderDate != null)
            {
                result += " ";
            }
            result += account;
        }
        if(result.equals(""))
        {
            result = "BLANK ORDER";
        }
        return result;
    }

    public Account getAccount()
    {
        return account;
    }

    public String getBillCountry()
    {
        return billCountry;
    }

    public String getBillToFirstName()
    {
        return billToFirstName;
    }

    public String getBillToLastName()
    {
        return billToLastName;
    }

    public String getCardType()
    {
        return cardType;
    }

    public String getCourier()
    {
        return courier;
    }

    public String getCreditCard()
    {
        return creditCard;
    }

    public String getExprDate()
    {
        return exprDate;
    }

    public Date getOrderDate()
    {
        return orderDate;
    }

    public String getShipCountry()
    {
        return shipCountry;
    }

    public MoneyAmount getTotalPrice()
    {
        return totalPrice;
    }

    public void setAccount(Account account)
    {
        this.account = account;
    }

    public void setBillCountry(String billCountry)
    {
        this.billCountry = billCountry;
    }

    public void setBillToFirstName(String billToFirstName)
    {
        this.billToFirstName = billToFirstName;
    }

    public void setBillToLastName(String billToLastName)
    {
        this.billToLastName = billToLastName;
    }

    public void setCardType(String cardType)
    {
        this.cardType = cardType;
    }

    public void setCourier(String courier)
    {
        this.courier = courier;
    }

    public void setCreditCard(String creditCard)
    {
        this.creditCard = creditCard;
    }

    public void setExprDate(String exprDate)
    {
        this.exprDate = exprDate;
    }

    public void setOrderDate(Date orderDate)
    {
        this.orderDate = orderDate;
    }

    public void setShipCountry(String shipCountry)
    {
        this.shipCountry = shipCountry;
    }

    public void setTotalPrice(MoneyAmount totalPrice)
    {
        this.totalPrice = totalPrice;
    }
}
