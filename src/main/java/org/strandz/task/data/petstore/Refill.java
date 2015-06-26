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
package org.strandz.task.data.petstore;

import org.strandz.data.petstore.objects.Account;
import org.strandz.data.petstore.objects.AccountStatus;
import org.strandz.data.petstore.objects.Category;
import org.strandz.data.petstore.objects.Item;
import org.strandz.data.petstore.objects.ItemStatus;
import org.strandz.data.petstore.objects.LineItem;
import org.strandz.data.petstore.objects.Order;
import org.strandz.data.petstore.objects.Product;
import org.strandz.lgpl.data.objects.MoneyAmount;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.TimeUtils;
import org.strandz.lgpl.util.Utils;
import org.strandz.store.petstore.PetstoreApplicationData;
import org.strandz.store.petstore.PetstoreData;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Refill
{
    private static Account joeZammit;
    private static Account martinLivingston;
    private static Order joeZammit1;
    private static Order joeZammit2;
    // private static Order joeZammit3;
    // private static Order joeZammit4;
    // private static Order joeZammit5;
    // private static Order joeZammit6;
    private static Product crow;
    private static Product pelican;
    private static Product shitsu;
    private static Product borderCollie;
    private static Item shitsu1;
    private static Item shitsu2;
    private static Item shitsu3;
    private static Item borderCollie1;
    private static Item pelican1;
    private static Item pelican2;
    private static Item crow1;
    private static Item crow2;
    private static List categories;
    private static List itemStatuses;
    private static PetstoreApplicationData td;
    private static boolean useCaptureFile = false;

    public static void main(String s[])
    {
        if(s.length != 0)
        {
            processParams(s);
        }
        else
        {
            String str[] = {"PetStoreData", PetstoreApplicationData.databaseName};
            processParams(str);
        }
    }

    public static PetstoreApplicationData getData()
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
        if(s[0].equals("PetStoreData"))
        {
            if(s.length == 1)
            {
                Err.error("Need to explicitly specify a database to refill");
            }
            if(s[1].equals(PetstoreApplicationData.PROD))
            {
                Err.error(
                    "Cannot refill the " + PetstoreApplicationData.PROD + " database");
            }
            if(s.length == 2)
            {
                td = PetstoreApplicationData.getNewInstance(s[1]);
            }
            else
            {
                td = PetstoreApplicationData.getNewInstance(s[1], s[2]);
            }
            // Err.error( "Use running of a capture file and scrap this!");
            if(!useCaptureFile)
            {
                td.getData().startTx();
                categories = (List) td.getData().get(PetstoreData.CATEGORY);
                itemStatuses = (List) td.getData().get(PetstoreData.ITEM_STATUS);

                 /**/
                List products = getProductData();
                Print.pr("************************************");
                Print.pr(products.size() + " products");
                Print.pr("************************************");
                td.getData().set(PetstoreData.PRODUCT, products);

                 /**/
                List items = getItemData();
                Print.pr("************************************");
                Print.pr(items.size() + " items");
                Print.pr("************************************");
                td.getData().set(PetstoreData.ITEM, items);

                 /**/
                List accounts = getAccountData();
                Print.pr("************************************");
                Print.pr(accounts.size() + " accounts");
                Print.pr("************************************");
                td.getData().set(PetstoreData.ACCOUNT, accounts);

                 /**/
                List orders = null;
                try
                {
                    orders = getOrderData();
                }
                catch(ParseException ex)
                {
                    Err.error(ex);
                }
                Print.pr("************************************");
                Print.pr(orders.size() + " orders");
                Print.pr("************************************");
                td.getData().set(PetstoreData.ORDER, orders);

                 /**/
                List lineItems = getLineItemData();
                Print.pr("************************************");
                Print.pr(lineItems.size() + " lineItems");
                Print.pr("************************************");
                td.getData().set(PetstoreData.LINE_ITEM, lineItems);
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

    private static List getLineItemData()
    {
        ArrayList lineItems = new ArrayList();
        {
            LineItem lineItem;
            lineItem = new LineItem();
            lineItem.setLineNum(1);
            lineItem.setItem(shitsu1);
            lineItem.setOrder(joeZammit1);
            lineItem.setQuantity(new Integer(2));
            lineItem.setUnitPrice(MoneyAmount.newInstance("$150.00"));
            lineItems.add(lineItem);
            lineItem = new LineItem();
            lineItem.setLineNum(2);
            lineItem.setItem(pelican1);
            lineItem.setOrder(joeZammit1);
            lineItem.setQuantity(new Integer(3));
            lineItem.setUnitPrice(MoneyAmount.newInstance("$50.00"));
            lineItems.add(lineItem);
            lineItem = new LineItem();
            lineItem.setLineNum(3);
            lineItem.setItem(crow1);
            lineItem.setOrder(joeZammit1);
            lineItem.setQuantity(new Integer(1));
            lineItem.setUnitPrice(MoneyAmount.newInstance("$5.00"));
            lineItems.add(lineItem);
            lineItem = new LineItem();
            lineItem.setLineNum(4);
            lineItem.setItem(borderCollie1);
            lineItem.setOrder(joeZammit1);
            lineItem.setQuantity(new Integer(2));
            lineItem.setUnitPrice(MoneyAmount.newInstance("$250.00"));
            lineItems.add(lineItem);
            lineItem = new LineItem();
            lineItem.setLineNum(5);
            lineItem.setItem(pelican2);
            lineItem.setOrder(joeZammit2);
            lineItem.setQuantity(new Integer(3));
            lineItem.setUnitPrice(MoneyAmount.newInstance("$52.00"));
            lineItems.add(lineItem);
            lineItem = new LineItem();
            lineItem.setLineNum(6);
            lineItem.setItem(crow2);
            lineItem.setOrder(joeZammit2);
            lineItem.setQuantity(new Integer(1));
            lineItem.setUnitPrice(MoneyAmount.newInstance("$2.00"));
            lineItems.add(lineItem);
        }
        return lineItems;
    }

    private static List getAccountData()
    {
        ArrayList accounts = new ArrayList();
        {
            Account account;
            account = new Account();
            account.setFirstName("Joe");
            account.setLastName("Zammit");
            account.setEmail("jezammit@yahoo.com.au");
            account.setCountry("Australia");
            account.setStatus(AccountStatus.OPEN);
            joeZammit = account;
            accounts.add(account);
            account = new Account();
            account.setFirstName("Martin");
            account.setLastName("Livingston");
            account.setEmail("marty_livo@yahoo.com");
            account.setCountry("Australia");
            account.setStatus(AccountStatus.OPEN);
            martinLivingston = account;
            accounts.add(account);
            account = new Account();
            account.setFirstName("Maryellen");
            account.setLastName("Mcleod");
            account.setEmail("maryellen_mcleod@hotmail.com");
            account.setCountry("Australia");
            account.setStatus(AccountStatus.OPEN);
            accounts.add(account);
            account = new Account();
            account.setFirstName("Mike");
            account.setLastName("Daughton");
            account.setEmail("mdaughton@gresham-computing.com.au");
            account.setCountry("Australia");
            account.setStatus(AccountStatus.CLOSED);
            accounts.add(account);
            account = new Account();
            account.setFirstName("Sean");
            account.setLastName("Mullin");
            account.setEmail("seanedwardmullin@yahoo.com.au");
            account.setCountry("Australia");
            account.setStatus(AccountStatus.CLOSED);
            accounts.add(account);
            account = new Account();
            account.setFirstName("Mario");
            account.setEmail("mdebattista@franciscans.org.au");
            account.setCountry("Australia");
            account.setStatus(AccountStatus.CLOSED);
            accounts.add(account);
        }
        return accounts;
    }

    private static List getItemData()
    {
        ArrayList items = new ArrayList();
        {
            Item item;
            item = new Item();
            item.setProduct(shitsu);
            item.setStatus(
                (ItemStatus) Utils.getFromList(itemStatuses, ItemStatus.SOLD));
            items.add(item);
            shitsu1 = item;
            item = new Item();
            item.setProduct(shitsu);
            item.setStatus(
                (ItemStatus) Utils.getFromList(itemStatuses, ItemStatus.SOLD));
            items.add(item);
            shitsu2 = item;
            item = new Item();
            item.setProduct(shitsu);
            item.setStatus(
                (ItemStatus) Utils.getFromList(itemStatuses, ItemStatus.SOLD));
            items.add(item);
            shitsu3 = item;
            item = new Item();
            item.setProduct(borderCollie);
            item.setStatus(
                (ItemStatus) Utils.getFromList(itemStatuses, ItemStatus.SOLD));
            items.add(item);
            borderCollie1 = item;
            item = new Item();
            item.setProduct(pelican);
            item.setStatus(
                (ItemStatus) Utils.getFromList(itemStatuses, ItemStatus.SOLD));
            items.add(item);
            pelican1 = item;
            item = new Item();
            item.setProduct(pelican);
            item.setStatus(
                (ItemStatus) Utils.getFromList(itemStatuses, ItemStatus.SOLD));
            items.add(item);
            pelican2 = item;
            item = new Item();
            item.setProduct(crow);
            item.setStatus(
                (ItemStatus) Utils.getFromList(itemStatuses, ItemStatus.SOLD));
            items.add(item);
            crow1 = item;
            item = new Item();
            item.setProduct(crow);
            item.setStatus(
                (ItemStatus) Utils.getFromList(itemStatuses, ItemStatus.SOLD));
            items.add(item);
            crow2 = item;
        }
        return items;
    }

    private static List getProductData()
    {
        ArrayList products = new ArrayList();
        {
            Product product;
            product = new Product();
            product.setName("Crow");
            product.setDescr("Crow");
            product.setCategory(
                (Category) Utils.getFromList(categories, Category.BIRD));
            products.add(product);
            crow = product;
            product = new Product();
            product.setName("Corroberee Frog");
            product.setDescr("Corroberee Frog");
            product.setCategory(
                (Category) Utils.getFromList(categories, Category.AMPHIBION));
            products.add(product);
            product = new Product();
            product.setName("Ibis");
            product.setDescr("Ibis");
            product.setCategory(
                (Category) Utils.getFromList(categories, Category.BIRD));
            products.add(product);
            product = new Product();
            product.setName("Pelican");
            product.setDescr("Pelican");
            product.setCategory(
                (Category) Utils.getFromList(categories, Category.BIRD));
            products.add(product);
            pelican = product;
            product = new Product();
            product.setName("Shitsu");
            product.setDescr("Shitsu");
            product.setCategory(
                (Category) Utils.getFromList(categories, Category.DOG));
            products.add(product);
            shitsu = product;
            product = new Product();
            product.setName("Border Collie");
            product.setDescr("Border Collie");
            product.setCategory(
                (Category) Utils.getFromList(categories, Category.DOG));
            products.add(product);
            borderCollie = product;
            product = new Product();
            product.setName("Belgian Shepard");
            product.setDescr("Belgian Shepard");
            product.setCategory(
                (Category) Utils.getFromList(categories, Category.DOG));
            products.add(product);
        }
        return products;
    }

    private static List getOrderData() throws ParseException
    {
        ArrayList orders = new ArrayList();
        {
            Order order;
            order = new Order();
            order.setAccount(joeZammit);
            order.setOrderDate(TimeUtils.DATE_FORMAT.parse("18/09/2002"));
            order.setCourier("TNT");
            order.setTotalPrice(MoneyAmount.newInstance("$200.00"));
            orders.add(order);
            joeZammit1 = order;
            order = new Order();
            order.setAccount(joeZammit);
            order.setOrderDate(TimeUtils.DATE_FORMAT.parse("19/09/2002"));
            order.setCourier("Ansett");
            order.setTotalPrice(MoneyAmount.newInstance("$210.00"));
            orders.add(order);
            joeZammit2 = order;
            /*
            order = new Order();
            order.setAccount( joeZammit);
            order.setOrderDate( pTimeUtils.dateFormatter.parse( "20/09/2002"));
            order.setCourier( "Patrick");
            order.setTotalPrice( Money.newInstance( "$220.00"));
            orders.add( order);
            joeZammit3 = order;

            order = new Order();
            order.setAccount( joeZammit);
            order.setOrderDate( pTimeUtils.dateFormatter.parse( "21/09/2002"));
            order.setCourier( "Global Express");
            order.setTotalPrice( Money.newInstance( "$230.00"));
            orders.add( order);
            joeZammit4 = order;

            order = new Order();
            order.setAccount( joeZammit);
            order.setOrderDate( pTimeUtils.dateFormatter.parse( "22/09/2002"));
            order.setCourier( "UPS");
            order.setTotalPrice( Money.newInstance( "$240.00"));
            orders.add( order);
            joeZammit5 = order;

            order = new Order();
            order.setAccount( joeZammit);
            order.setOrderDate( pTimeUtils.dateFormatter.parse( "23/09/2002"));
            order.setCourier( "Fed Express");
            order.setTotalPrice( Money.newInstance( "$250.00"));
            orders.add( order);
            joeZammit6 = order;
            */
        }
        return orders;
    }
}
