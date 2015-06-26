package org.strandz.data.wombatrescue.objects.cayenne.client;

import org.strandz.data.wombatrescue.objects.cayenne.client.auto._UserDetails;
import org.strandz.lgpl.util.UserDetailsI;
import org.strandz.lgpl.util.Err;

public class UserDetails extends _UserDetails implements UserDetailsI
{
    public String getDatabaseUsername()
    {
        return getDatabaseusername();
    }
    public String getDatabasePassword()
    {
        return getDatabasepassword();
    }

    public void setDatabaseUsername(String databaseUsername)
    {
        setDatabaseusername( databaseUsername);
    }
}



