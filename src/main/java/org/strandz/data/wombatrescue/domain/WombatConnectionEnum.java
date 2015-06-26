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
package org.strandz.data.wombatrescue.domain;

import org.strandz.lgpl.store.ConnectionEnum;
import org.strandz.lgpl.util.Utils;

public class WombatConnectionEnum extends ConnectionEnum
{

    WombatConnectionEnum(String name, boolean production, String description)
    {
        super(name, production, description);
    }

    WombatConnectionEnum(String name, boolean production, String description, boolean requiresLogin)
    {
        super(name, production, description, requiresLogin);
    }

    public static WombatConnectionEnum getFromName(String name)
    {
        return (WombatConnectionEnum) Utils.getByStringFromArray(CONNECTIONS, name);
    }

    public static final WombatConnectionEnum DEBIAN_DEV_TERESA =
        new WombatConnectionEnum("DEBIAN_DEV_TERESA", false, "DEV, Teresa on Debian");
    public static final WombatConnectionEnum DEBIAN_PROD_TERESA =
        new WombatConnectionEnum("DEBIAN_PROD_TERESA", true, "PROD, Teresa on Debian");
    public static final WombatConnectionEnum LOCAL_TEST_WOMBAT =
        new WombatConnectionEnum("LOCAL_TEST_WOMBAT", true, "PROD, Teresa on Client (HTTP)");
    public static final WombatConnectionEnum LINODE_PROD_TERESA =
        new WombatConnectionEnum("LINODE_PROD_TERESA", true, "PROD, Teresa on Linode");
    public static final WombatConnectionEnum TEST_LINODE_PROD_TERESA =
        new WombatConnectionEnum("TEST_LINODE_PROD_TERESA", true, "PROD, Teresa on Linode-like");
    //
    public static final WombatConnectionEnum BACKUP =
        new WombatConnectionEnum("BACKUP", false, "Local Backup");
    public static final WombatConnectionEnum DEV =
        new WombatConnectionEnum("DEV", false, "Local DEV");
    public static final WombatConnectionEnum DEV_JPOX =
        new WombatConnectionEnum("DEV_JPOX", false, "Local DEV via JPOX");
    //public static final WombatConnectionEnum TEST_WOMBAT_JPOX =
    //    new WombatConnectionEnum("TEST_WOMBAT_JPOX", false, "Test MySql TEST via JPOX");

    //public static final WombatConnectionEnum DEMO_WOMBAT_JPOX =
    //    new WombatConnectionEnum("DEMO_WOMBAT_JPOX", false, "Demo MySql TEST via JPOX");
    /*
     * Indirect ROP connections, all are the same:
     * If they are not remote then they both point to cayenne.xml.
     * The URL gives the webapp and the webapps for DEMO and PROD
     * are usually on different machines. Even if they were not,
     * then DEMO and PROD would be different webapps. The only
     * trick required is that as part of jarring this is done:
     * <copy file="cayenne-prod.xml" tofile="cayenne.xml"/>
    public static final WombatConnectionEnum PROD_WOMBAT_CAYENNE =
        new WombatConnectionEnum("PROD_WOMBAT_CAYENNE", false, "Prod MySql PROD via CAYENNE, on server");
    */
    /**
     * Direct means when access directly. Only the test (also called DEMO) DB is accessed directly
     */
    public static final WombatConnectionEnum SERVER_CAYENNE =
        new WombatConnectionEnum("SERVER_CAYENNE", false, "CAYENNE on server");
    public static final WombatConnectionEnum DIRECT_DEMO_WOMBAT_CAYENNE =
        new WombatConnectionEnum("DIRECT_DEMO_WOMBAT_CAYENNE", false, "Demo MySql TEST via CAYENNE, on server");
    public static final WombatConnectionEnum REMOTE_DEMO_WOMBAT_CAYENNE =
        new WombatConnectionEnum("REMOTE_DEMO_WOMBAT_CAYENNE", false,
            "Demo MySql TEST (on same machine) via CAYENNE, remotely", true);
    public static final WombatConnectionEnum REMOTE_WOMBAT_CAYENNE =
        new WombatConnectionEnum("REMOTE_WOMBAT_CAYENNE", false,
            "Demo MySql TEST (on Linode) via CAYENNE, remotely", false);

    public static final WombatConnectionEnum REMOTE_PROD_WOMBAT_CAYENNE =
        new WombatConnectionEnum("REMOTE_PROD_WOMBAT_CAYENNE", true,
            "Demo MySql PROD (on Linode) via CAYENNE, remotely", true);

    public static final WombatConnectionEnum DEMO_WOMBAT_KODO =
        new WombatConnectionEnum("DEMO_WOMBAT_KODO", false, "Demo MySql TEST via Kodo");

    //Used for production on the server for remote Kodo services (Also works against DB on desktop)
    public static final WombatConnectionEnum WOMBAT_KODO =
        new WombatConnectionEnum("WOMBAT_KODO", false, "MySql via KODO");
    //Used for production on the server for spring services
    public static final WombatConnectionEnum WOMBAT_KODO_SERVICE =
        new WombatConnectionEnum("WOMBAT_KODO_SERVICE", false, "MySql via KODO");
    //Accesses the DB that local tomcat kodoservice is using (jdbc:mysql://localhost:3306/kodo)
    public static final WombatConnectionEnum REMOTE_DEMO_WOMBAT_KODO =
        new WombatConnectionEnum("REMOTE_DEMO_WOMBAT_KODO", false, "Remote Demo MySql TEST via KODO", true);
    //Production from the client
    public static final WombatConnectionEnum REMOTE_WOMBAT_KODO =
        new WombatConnectionEnum("REMOTE_WOMBAT_KODO", false, "Remote MySql PROD via KODO", true);
    public static final WombatConnectionEnum DEMO_WOMBAT_MEMORY =
        new WombatConnectionEnum("DEMO_WOMBAT_MEMORY", false, "Demo - data objects in memory");
    public static final WombatConnectionEnum PROD =
        new WombatConnectionEnum("PROD", true, "Local PROD");
    public static final WombatConnectionEnum TEST =
        new WombatConnectionEnum("TEST", false, "Local TEST");
    public static final WombatConnectionEnum XML =
        new WombatConnectionEnum("XML", false, "XML Connection that haven't coded in new way yet");

    public static final WombatConnectionEnum[] CONNECTIONS = {
        DEBIAN_DEV_TERESA, DEBIAN_PROD_TERESA, LINODE_PROD_TERESA, BACKUP,
        DEV, DEV_JPOX, DEMO_WOMBAT_KODO, WOMBAT_KODO, REMOTE_DEMO_WOMBAT_KODO,
        REMOTE_WOMBAT_KODO, DEMO_WOMBAT_MEMORY, SERVER_CAYENNE, PROD, TEST, XML};

}
