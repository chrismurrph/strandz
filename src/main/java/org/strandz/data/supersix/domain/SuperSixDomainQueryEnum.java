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

import org.strandz.lgpl.store.DomainQueryEnum;

public class SuperSixDomainQueryEnum extends DomainQueryEnum
{
    SuperSixDomainQueryEnum(String name, String description)
    {
        super(name, description);
    }

    public static final SuperSixDomainQueryEnum ALL_CURRENT_TEAM =
        new SuperSixDomainQueryEnum("ALL_TEAM", "All Teams");
    public static final SuperSixDomainQueryEnum ALL_CURRENT_TEAM_INCL_NULL =
        new SuperSixDomainQueryEnum("ALL_TEAM_INCL_NULL", "All Teams (including NULL)");
    public static final SuperSixDomainQueryEnum ALL_CURRENT_TEAM_BY_DIVISION =
        new SuperSixDomainQueryEnum("ALL_TEAM_BY_DIVISION", "All Teams in a particular Division");
    public static final SuperSixDomainQueryEnum SINGLE_GLOBAL =
        new SuperSixDomainQueryEnum("SINGLE_GLOBAL", "Single Global");
    public static final SuperSixDomainQueryEnum SEASON_BY_SEASONYEAR_COMP =
        new SuperSixDomainQueryEnum("SEASON_BY_SEASONYEAR_COMP", "CompetitionSeason by SeasonYear and Competition (usually called using globals)");
    public static final SuperSixDomainQueryEnum ALL_USER =
        new SuperSixDomainQueryEnum("ALL_USER", "All Users");
    public static final SuperSixDomainQueryEnum LOOKUPS =
        new SuperSixDomainQueryEnum("LOOKUPS", "SuperSix Lookups");
    
    //public static final SuperSixDomainQueryEnum DEFAULT_QUERY = ALL_TEAM;
}
