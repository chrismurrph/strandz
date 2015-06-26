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
package org.strandz.data.supersix.domain;

import org.strandz.lgpl.store.ConnectionEnum;
import org.strandz.lgpl.util.Utils;

public class SuperSixConnectionEnum extends ConnectionEnum
{
    SuperSixConnectionEnum(String name, boolean production, String description)
    {
        super(name, production, description);
    }

    public static SuperSixConnectionEnum getFromName(String name)
    {
        return (SuperSixConnectionEnum) Utils.getByStringFromArray(SuperSixConnectionEnum.CONNECTIONS, name);
    }

    public static final SuperSixConnectionEnum SUPERSIX_DEMO_MEMORY =
        new SuperSixConnectionEnum("DEMO_MEMORY", false, "Demo - data objects in memory");
    public static final SuperSixConnectionEnum SUPERSIX_KODO =
        new SuperSixConnectionEnum("SUPERSIX_KODO", false, "SuperSix - KODO/Postgres");
    public static final SuperSixConnectionEnum SUPERSIX_KODO_SERVICE =
        new SuperSixConnectionEnum("SUPERSIX_KODO_SERVICE", false, "SuperSix - KODO/Postgres");
    public static final SuperSixConnectionEnum REMOTE_DEMO_SUPERSIX_KODO =
        new SuperSixConnectionEnum("REMOTE_DEMO_SUPERSIX_KODO", false, "Remote Demo MySql TEST via KODO");
    public static final SuperSixConnectionEnum REMOTE_SUPERSIX_KODO =
        new SuperSixConnectionEnum("REMOTE_SUPERSIX_KODO", false, "Remote MySql PROD via KODO");
    //Not implemented
    public static final SuperSixConnectionEnum DEMO_SUPERSIX_JPOX =
        new SuperSixConnectionEnum("DEMO_SUPERSIX_JPOX", false, "Demo - JPOX/MySql");

    public static final SuperSixConnectionEnum[] CONNECTIONS = {
            SUPERSIX_DEMO_MEMORY, SUPERSIX_KODO};
}
