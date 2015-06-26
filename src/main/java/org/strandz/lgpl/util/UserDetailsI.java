package org.strandz.lgpl.util;

/**
 * User: Chris
 * Date: 7/10/2008
 * Time: 01:31:41
 */
public interface UserDetailsI
{
    String getUsername();
    String getPassword();
    String getDatabaseUsername();
    String getDatabasePassword();
    void setUsername( String username);
    void setPassword( String password);
    void setDatabaseUsername( String databaseUsername);
}
