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
package org.strandz.data.supersix.business;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import java.util.List;
import java.util.ArrayList;

/**
 * Gives maximum variability over the use of a resource. What is given to each user 
 * is remembered, so at any time we can know if fairness has been reached. Fairness
 * is reached as frequently as possible.
 */
public class ProportionalResourceAllocator
{
    /**
     * Allocatees - the objects that will need to share the resources
     * between themselves. eg./ a ground available at a certain time
     * is a resource that will need to be allocated between Male and
     * Female divisions.
     */
    List<ResourceUser> resourceUsers;
    /**
     * List of lists, each one to be iterated over, and allocated proportionally
     * eg. 11 dates, 6 KO times, 2 pitches, gives us 11*6*2 = 132 games altogether,
     * which we must allocate between the two divisions. In our case there will be
     * three lists and the order will be as given.
     */
    Object resourcesLists[];
    /**
     *
     */
    Proportions lastAllocation;
    /**
     *
     */
    Proportions fairProportions;

    static class ResourceUser
    {
        Object user;
        int weight;

        public ResourceUser(Object user, int weight)
        {
            this.user = user;
            this.weight = weight;
        }
    }

    ProportionalResourceAllocator( List<ResourceUser> resourceUsers, Object resourcesLists[])
    {
        this.resourceUsers = resourceUsers;
        this.resourcesLists = resourcesLists;
        if(resourceUsers.size() != 2)
        {
            Err.error( "resourceUsers.size() != 2 - make Proportions any number, not just two");
        }
        if(resourcesLists.length != 3)
        {
            Err.error( "resourcesLists.length != 3 - how do we loop an arbitary number of times?");
        }
        fairProportions = new Proportions( resourceUsers.get(0).weight, resourceUsers.get(1).weight);
        lastAllocation = new Proportions( 0, 0);
    }

    /**
     * We need to keep the allocation looking as much like the
     * ratio as possible 
     */
    private Proportions getNextAllocation( Proportions lastAllocation)
    {
        Proportions result = null;
        Proportions proportionsIfAddToFirst = new Proportions(lastAllocation.first+1, lastAllocation.second);
        Proportions proportionsIfAddToSecond = new Proportions(lastAllocation.first, lastAllocation.second+1);
        int cfFirstToIdeal = proportionsIfAddToFirst.compareTo( fairProportions);
        int cfSecondToIdeal = proportionsIfAddToSecond.compareTo( fairProportions);
        if(cfFirstToIdeal < cfSecondToIdeal)
        {
            result = proportionsIfAddToFirst;
        }
        else
        {
            result = proportionsIfAddToSecond;
        }
        return result;
    }

    /**
     * 
     * @param user eg. Division
     * @return eg. [date, KO time, pitch]
     */
    public Object[] nextResource( Object user)
    {
        Object[] result = null;
        return result;
    }
    
    public static void trialAllocations()
    {
        ResourceUser mensDiv = new ResourceUser( "Men", 11);
        ResourceUser womensDiv = new ResourceUser( "Women", 5);
        List<ResourceUser> users = new ArrayList<ResourceUser>();
        users.add( mensDiv);
        users.add( womensDiv);
        ProportionalResourceAllocator allocator = new ProportionalResourceAllocator( users, null);
        Err.pr( "Fair as decimal: " + Utils.decimalDivide( allocator.fairProportions.first, allocator.fairProportions.second));
        Proportions lastAllocation = allocator.lastAllocation;
        lastAllocation = allocator.getNextAllocation( lastAllocation);
        Err.pr( "Got to " + lastAllocation);
        for(int i = 0; i < 31; i++)
        {
            lastAllocation = allocator.getNextAllocation( lastAllocation);
            Err.pr( "Got to " + lastAllocation);
            Err.pr( "As decimal: " + Utils.decimalDivide( lastAllocation.first, lastAllocation.second));
        }
    }
    
    public static void main( String args[])
    {
        
    }
}
