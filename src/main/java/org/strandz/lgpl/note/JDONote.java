/*
    Strandz LGPL - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz LGPL is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA


    The authors can be contacted via www.strandz.org
*/
package org.strandz.lgpl.note;

public class JDONote extends Note
{
    public static final Note APPEARS_CONSTRUCTOR_NOT_CALLED = new JDONote( "Appears Constructor Not Called");
    public static final Note SHOULD_ONLY_PERSIST_TO_INSERT = new JDONote( "Should only persist when want to insert");
    public static final Note TOUCH_DELETED = new JDONote( "Touch deleted");
    public static final Note GENERIC = new JDONote( "Generic Strandz Note");
    public static final Note DEEP_COPY_TO_RETRIEVE = new JDONote( "Do a deepCopy to Retrieve object graph from remote");
    public static final Note ASSIGN_PM = new JDONote( "Assigning a new pm for objects brought in from Spring");
    public static final Note RELOAD_PM_KODO_BUG = new JDONote( "Need reload Kodo PM to pick up changes, Case #: 687756", SHOW);

    static
    {
        String desc = null;
        //
        GENERIC.setDescription("Catch all for any Strandz notes which are 'on their own'");
        //
        desc = "Watch out for doing a toString() of a parent object"
            + " that has already been deleted eg. RosterSlot that has"
            + " toString() that refers to a Volunteer, which was deleted"
            + " first. ";
        desc += "An example of a failure that is happening right now is when delete a roster" +
            " slot and try to generate a roster before commit. It looks as if that particular" +
            " roster slot is still in a list that gets processed. The actual error happens when" +
            " a field from the deleted roster slot is accessed. The real bug is that the roster" +
            " slot exists in a list. It is almost as if multiple lists are being kept. Are they?" +
            " This could be a JDOGenie only problem.";
        TOUCH_DELETED.setDescription(desc);
        TOUCH_DELETED.setFix(
            "No real solution. Even if had a queue and"
                + " tried to delete in the opposite order, the reverse toString() would"
                + " get us in trouble");
        //
        /* Taken from org.strandz.test.TrialRosterHyperdriveProblem, it would seem that this
        * bug has been fixed.
        //Need to tell data which extent it actually came from, otherwise
        //no saving will be taking place.
        //data.setRefinedList( POJOWombatData.VOLUNTEER, masterList);
        //RUBBISH - no need to call this anymore (for JDO anyway) as Cell.setData()
        //calls registerPersistentAll(), so we replicate that in our
        //local commit here. ALSO RUBBISH - JDOBug.shouldOnlyPersistToInsert
        //has commented it out and says it not necessary - which is true as
        //inserts of volunteers are working in production.
        */
        desc = "If a list comes from a query in the first place then only need to commit."
            + " Related question, can we get rid of data.setRefinedList(), or does some DataStore"
            + " apart from JDO require it?";
        SHOULD_ONLY_PERSIST_TO_INSERT.setDescription(desc);
        //
        desc = "Using a constructor to assign a static NULL is not portable"
            + " when moving from XMLEncoder where relied on constructor and"
            + " properties being set, to JDO where constructor is not called."
            + " May later get rid of static NULLs altogether. Task that use to"
            + " change the data after have imported from XML is called "
            + " MakeNullVolunteer. ACTUALLY constructor IS called! ";
        APPEARS_CONSTRUCTOR_NOT_CALLED.setDescription(desc);
        //
    }
    
    public JDONote()
    {
        super();
    }

    public JDONote(String name)
    {
        super( name);
    }
    
    public JDONote(String name, boolean visible)
    {
        super(name, visible);
    }
}
