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
package org.strandz.lgpl.note;

public class SuperSixNote extends Note
{
    public static final Note GENERIC = new SuperSixNote( "Generic Supersix Note");
    public static final Note MULTIPLE_CONNECTING = new SuperSixNote( "Multiple Connecting");
    public static final Note CURRENT_SEASON = new SuperSixNote( "Current CompetitionSeason");
    public static final Note TEAMS_NOT_SHOWING = new SuperSixNote( "Teams not showing", SHOW);
    
    static
    {
        //
        TEAMS_NOT_SHOWING.setDescription("Under Games Tab none of the teams are showing");
        TEAMS_NOT_SHOWING.setFixed( true);
        //
        MULTIPLE_CONNECTING.setDescription("Connects for Unpaid Teams, then again for Teams and Players");
        MULTIPLE_CONNECTING.setFixed( true);
        //
        GENERIC.setDescription("Catch all for any SS notes which are 'on their own'");
    }

    public SuperSixNote()
    {
    }

    public SuperSixNote(boolean visible)
    {
        super(visible);
    }

    public SuperSixNote(String name, boolean visible)
    {
        super(name, visible);
    }

    public SuperSixNote(String name)
    {
        super(name);
    }
}
