package org.strandz.service.wombatrescue;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.strandz.data.util.AbstractUserDetailsService;
import org.strandz.data.wombatrescue.domain.WombatDomainQueryEnum;
import org.strandz.lgpl.store.DomainQueryEnum;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.UserDetailsI;
import org.strandz.lgpl.util.UserDetailsTransferObj;
import org.strandz.lgpl.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: Chris
 * Date: 18/05/2015
 * Time: 12:48 PM
 */
public class AuthenticationUserDetailsGetter extends AbstractUserDetailsService implements UserDetailsService {

    public AuthenticationUserDetailsGetter()
    {
        setDataStore( CayenneServiceFactoryUtils.getDataStore());
    }

    //Really why does this silly method exist at all?
    @Override
    public DomainQueryEnum getAllUsersEnum()
    {
        return WombatDomainQueryEnum.ALL_USER;
    }

    public static void main(String[] args)
    {
        ServerUserDetailsService service = new ServerUserDetailsService();
        Err.pr( "Created " + service);
        Err.pr( "About to try and call display service:");
        UserDetailsTransferObj received = service.getUserDetails( "Mike");
        Err.pr( "DEBUGGING User Details was ok: " + !Utils.isBlank( received.getUserDetails().toString()));
        Err.pr( "---------------START User Details---------------");
        Err.pr( received.getUserDetails().toString());
        Err.pr( "----------------END User Details----------------");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user;
        Err.pr( "");
        Err.pr( "HEY! In AuthenticationUserDetailsGetter for user: " + username);
        UserDetailsTransferObj unDetails = getUserDetails( username);
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        UserDetailsI userDetails = unDetails.getUserDetails();
        //List<String> authorityNames = userDetails.getAuthorities();
        List<String> authorityNames = Utils.EMPTY_ARRAYLIST;
        authorityNames.add( "ROLE_USER");
        Err.pr( "authorityNames for user <" + userDetails.getUsername() + "> are: <" + authorityNames + ">");
        for (int i = 0; i < authorityNames.size(); i++)
        {
            final String authorityName = authorityNames.get(i);
            authorities.add( new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return authorityName;
                }
            });
        }
        Err.pr( "To create Spring user object: " + userDetails.getUsername() + ", " + userDetails.getPassword() + ", " + authorities);
        user = new AuthenticationUserDetails( new User( userDetails.getUsername(), userDetails.getPassword(), authorities));
        return user;
    }
}
