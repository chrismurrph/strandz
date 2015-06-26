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
package org.strandz.store.petstore;

import org.strandz.data.petstore.objects.Account;
import org.strandz.data.petstore.objects.AccountStatus;
import org.strandz.data.petstore.objects.Category;
import org.strandz.data.petstore.objects.Item;
import org.strandz.data.petstore.objects.ItemStatus;
import org.strandz.data.petstore.objects.LineItem;
import org.strandz.data.petstore.objects.Order;
import org.strandz.data.petstore.objects.Product;
import org.strandz.data.petstore.objects.Supplier;
import org.strandz.data.petstore.objects.SupplierStatus;

public class PetstoreData
{
    public static final Class ACCOUNT = Account.class;
    public static final Class ACCOUNT_STATUS = AccountStatus.class;
    public static final Class CATEGORY = Category.class;
    public static final Class ITEM = Item.class;
    public static final Class ITEM_STATUS = ItemStatus.class;
    public static final Class LINE_ITEM = LineItem.class;
    public static final Class ORDER = Order.class;
    public static final Class PRODUCT = Product.class;
    public static final Class SUPPLIER = Supplier.class;
    public static final Class SUPPLIER_STATUS = SupplierStatus.class;
    public static final Class[] CLASSES = {
        ACCOUNT, ACCOUNT_STATUS, CATEGORY, ITEM, ITEM_STATUS, LINE_ITEM, ORDER,
        PRODUCT, SUPPLIER, SUPPLIER_STATUS
    };
}
