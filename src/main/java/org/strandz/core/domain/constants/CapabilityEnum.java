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
package org.strandz.core.domain.constants;

// import java.io.ObjectStreamException;

import java.io.Serializable;

public class CapabilityEnum implements Serializable
{
    String name = null;

    public CapabilityEnum()
    {
    }

    CapabilityEnum(String name)
    {
        this.name = name;
    }

    public String toString()
    {
        return name;
    }

    public boolean isOperation()
    {
        return false;
    }

    public static final OperationEnum UNKNOWN = new OperationEnum("UNKNOWN");
    public static final CapabilityEnum UPDATE = new CapabilityEnum("UPDATE");
    public static final CapabilityEnum EDIT_INSERTS_BEFORE_COMMIT = new CapabilityEnum(
        "EDIT_INSERTS_BEFORE_COMMIT");
    public static final CapabilityEnum BLANK_RECORD = new CapabilityEnum(
        "BLANK_RECORD");
    public static final CapabilityEnum IGNORED_CHILD = new CapabilityEnum(
        "IGNORED_CHILD");
    public static final CapabilityEnum CASCADE_DELETE = new CapabilityEnum(
        "CASCADE_DELETE");
    public static final CapabilityEnum FOCUS_NODE = new CapabilityEnum(
        "FOCUS_NODE");
    public static final OperationEnum ENTER_QUERY = new OperationEnum("Enter");
    public static final OperationEnum EXECUTE_QUERY = new OperationEnum("Query");
    public static final OperationEnum EXECUTE_SEARCH = new OperationEnum("Search");
    public static final OperationEnum PREVIOUS = new OperationEnum("Prev");
    public static final OperationEnum NEXT = new OperationEnum("Next");
    public static final OperationEnum SET_ROW = new OperationEnum("SET_ROW");
    //Never been used...
    //public static final OperationEnum INSERT_IGNORE = new OperationEnum(
    //    "INSERT_IGNORE");
    /* Commented out these two as part of important change. Replaced by
       INSERT_AT_PLACE
       INSERT_AFTER_PLACE
    public static final OperationEnum INSERT = new OperationEnum("Insert");
    public static final OperationEnum INSERT_PRIOR = new OperationEnum(
        "Insert Prior");
    */
    public static final OperationEnum INSERT_AT_PLACE = new OperationEnum("Insert at Place");
    public static final OperationEnum INSERT_AFTER_PLACE = new OperationEnum("Insert");
    public static final OperationEnum REMOVE = new OperationEnum("Delete");
    public static final OperationEnum POST = new OperationEnum("Post");
    public static final OperationEnum REFRESH = new OperationEnum("Refresh");
    public static final OperationEnum COMMIT_ONLY = new OperationEnum(
        "COMMIT_ONLY");
    public static final OperationEnum COMMIT_RELOAD = new OperationEnum("Save");
    public static final OperationEnum GOTO_NODE = new GoToEnum("GOTO_NODE");
    public static final OperationEnum GOTO_NODE_IGNORE = new GoToEnum(
        "GOTO_NODE_IGNORE");
    public static final OperationEnum SET_VALUE = new OperationEnum("SET_VALUE");
    public static final OperationEnum MANUALLY_INITIATED = new OperationEnum("MANUALLY_INITIATED");
    // serialization
    // private static int nextOrdinal = 0;
    // private final int ordinal = nextOrdinal++;
    public static final CapabilityEnum[] ALL_CAPABILITIES = {
        UNKNOWN, UPDATE, EDIT_INSERTS_BEFORE_COMMIT, BLANK_RECORD, IGNORED_CHILD,
        CASCADE_DELETE, FOCUS_NODE, ENTER_QUERY, EXECUTE_QUERY, EXECUTE_SEARCH,
        PREVIOUS, NEXT, SET_ROW, INSERT_AT_PLACE, INSERT_AFTER_PLACE, REMOVE, POST, REFRESH, COMMIT_ONLY,
        COMMIT_RELOAD, GOTO_NODE, GOTO_NODE_IGNORE, SET_VALUE, MANUALLY_INITIATED};
    public static final CapabilityEnum[] ALL_KNOWN_CAPABILITIES = {
        UPDATE, EDIT_INSERTS_BEFORE_COMMIT, BLANK_RECORD, IGNORED_CHILD,
        CASCADE_DELETE, FOCUS_NODE, ENTER_QUERY, EXECUTE_QUERY, EXECUTE_SEARCH,
        PREVIOUS, NEXT, SET_ROW, INSERT_AT_PLACE, INSERT_AFTER_PLACE, REMOVE, POST, REFRESH, COMMIT_ONLY,
        COMMIT_RELOAD, GOTO_NODE, GOTO_NODE_IGNORE, SET_VALUE, MANUALLY_INITIATED};
    public static final CapabilityEnum[] SET_EN_MASSE = {
        UPDATE, ENTER_QUERY, INSERT_AT_PLACE, INSERT_AFTER_PLACE, // not sure s/be here
        EXECUTE_SEARCH, REMOVE, POST, REFRESH, COMMIT_RELOAD, PREVIOUS, NEXT, SET_ROW,
        CASCADE_DELETE, FOCUS_NODE};
    
    public static final OperationEnum[] USUALLY_VISIBLE_OPERATIONS = {
        ENTER_QUERY, EXECUTE_QUERY, EXECUTE_SEARCH, INSERT_AFTER_PLACE, REMOVE, POST, PREVIOUS,
        NEXT, COMMIT_RELOAD};
    
    public static final OperationEnum[] ALL_KNOWN_OPERATIONS = {
        ENTER_QUERY, EXECUTE_QUERY, EXECUTE_SEARCH, PREVIOUS, NEXT, SET_ROW, INSERT_AT_PLACE, INSERT_AFTER_PLACE,
        REMOVE, POST, REFRESH, COMMIT_ONLY, COMMIT_RELOAD, GOTO_NODE,
        GOTO_NODE_IGNORE, SET_VALUE, MANUALLY_INITIATED};
    /*
    Object readResolve() throws ObjectStreamException
    {
    return VALUES[ordinal];
    }
    public int ordinalValue()
    {
    return ordinal;
    }
    */

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
