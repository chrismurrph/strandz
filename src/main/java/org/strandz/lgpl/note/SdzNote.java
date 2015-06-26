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


public class SdzNote extends Note
{
    public static final Note INCREMENT_WHEN_ADD = new SdzNote();
    public static final Note CANT_SEE_ERROR = new SdzNote();
    public static final Note TABLE_BLANKING_POLICY = new SdzNote();
    public static final Note GO_TO_FROZEN = new SdzNote();
    public static final Note DELETE_MASTER_WHEN_COLLECTION = new SdzNote();
    public static final Note NO_CONTROLS_IN_PANE = new SdzNote();
    public static final Note INTO_BEAN_ALL_PANELS_GO_GREEN = new SdzNote();
    public static final Note RE_TRANSFER_DO_TO_CELL = new SdzNote();
    public static final Note LOOKING_UP_WRONG_TYPE = new SdzNote();
    public static final Note PETSTORE_TABLES = new SdzNote();
    public static final Note ANY_STATE_CHANGE_POSSIBLE = new SdzNote();
    public static final Note INTO_BAD_STATE_IF_DONT_POST = new SdzNote();
    public static final Note FILL_DATA_FOR_DISCONNECTED_NODES = new SdzNote();
    public static final Note INITIAL_ROW_WRONG = new SdzNote();
    public static final Note R_CLICK_RESTORE = new SdzNote();
    public static final Note MASTER_DETAIL_COPY_PASTE = new SdzNote();
    public static final Note TIMES_TOO_CLOSE = new SdzNote();
    public static final Note UP_DOWN_KEY_IN_J_TABLES = new SdzNote();
    public static final Note INTERMITTENT_UNIT_TEST_FAILURE = new SdzNote();
    public static final Note TASK_BAR_APPEARANCE = new SdzNote();
    public static final Note SAVE_CHANGES = new SdzNote();
    public static final Note TOOL_BAR_SHOULD_BE_IN_STRAND_AREA = new SdzNote();
    public static final Note WHICH_ROW_ON = new SdzNote();
    public static final Note COMPLETED_ADD_BUT_STILL_NEW = new SdzNote();
    public static final Note TWO_TABS_FOR_VALIDATION_TO_WORK = new SdzNote();
    public static final Note BAD_WOMBAT_VALIDATION = new SdzNote( HIDE);
    public static final Note SHOULD_BE_ABLE_TO_CHANGE_NODE_VISUALLY = 
            new SdzNote( "Should Be Able To Change Node Visually", HIDE);
    public static final Note AUTO_SELECT_TABLE_ROW_AFTER_INSERT = new SdzNote();
    public static final Note HAS_CHANGED_NOT_AVAILABLE_IN_POSTING_TRIGGER = new SdzNote();
    public static final Note CHANGING_NODES_STATES_I_NOT_RECOGNISED = 
            new SdzNote( "Changing Nodes and States, Insert not Recognised", HIDE);
    public static final Note SEARCH_WORKING_QUERY_NOT = new SdzNote();
    public static final Note DELETE_CONTEXT_DELETE_GENED_FILES = new SdzNote();
    public static final Note TRACKER_NEW_WAY_OBSERVING_FOCUS_CHANGES = new SdzNote();
    public static final Note IGNORED_CHILD_TABLE_STILL_GETS_UP_DOWN = new SdzNote();
    public static final Note EDIT_LOOKUP_ON_J_TABLE = new SdzNote();
    public static final Note NOT_UPDATE_ATTRIBUTE = new SdzNote();
    public static final Note ENABLEDNESS_REFACTORING = new SdzNote( "Enabledness refactoring", HIDE);
    public static final Note DYNAMIC_ALLOWED = new SdzNote();
    public static final Note TABLE_ITEM_VALIDATION = new SdzNote();
    public static final Note NODE_VALIDATION = new SdzNote();
    public static final Note GET_ITEM_LIST_ONLY_FOR_PAINTED = new SdzNote();
    public static final Note GO_AS_BEFORE_TOO_COMPLEX = new SdzNote();
    public static final Note POLLUTING_DB_WITH_NULL_STRINGS = new SdzNote( "Polluting DB with Null Strings", HIDE);
    public static final Note GENERIC = new SdzNote();
    public static final Note TIES = new SdzNote();
    //public static final Note showTimes = new SdzNote();
    public static final Note TZ = new SdzNote();
    public static final Note BG_ADD = new SdzNote( "Background Add", HIDE);
    public static final Note BACK_SENSIBLE = new SdzNote();
    public static final Note SET_DISPLAY_ON_TABLE = new SdzNote( "Set Display on Table", HIDE);
    public static final Note IMPL = new SdzNote( "implementation (impl) package", HIDE);
    public static final Note REASSIGN_CONTROL = new SdzNote( "Reassign Control", HIDE);
    public static final Note DYNAM_ALLOWED = new SdzNote();
    public static final Note CURRENT_PANE = new SdzNote( "currentPane", HIDE);
    public static final Note TIGHTEN_RECORD_VALIDATION = new SdzNote();
    public static final Note NO_MOVE_STATE = new SdzNote();
    public static final Note CREATE_FROZEN = new SdzNote();
    public static final Note DATE_CONVERT = new SdzNote();
    public static final Note USE_CS = new SdzNote();
    public static final Note SECOND_TEST = new SdzNote( "TestRoster: secondTest fails", HIDE);
    public static final Note NEW_DATA_STORE_FACTORY = new SdzNote();
    public static final Note MDATE_ENTRY_FIELD = new SdzNote( "mDateEntryField", HIDE);
    public static final Note CANT_ADD_RS = new SdzNote();
    public static final Note BLANKOUT_A_RECORD_YOURSELF = new SdzNote();
    public static final Note NV_PASTE_NOT_WORKING = new SdzNote();
    public static final Note INIT_CS = new SdzNote();
    public static final Note STILL_RED_WHEN_DELETE = new SdzNote();
    public static final Note BLANK_TWICE_WHEN_DELETE = new SdzNote();
    public static final Note WHERE_AFTER_MESSAGE_DLG = new SdzNote();
    public static final Note FIELD_VALIDATION = new SdzNote( "Before hand to Mike", HIDE);
    public static final Note COMBO_BEING_RED = new SdzNote();
    public static final Note EM_BECOMES_NULL = new SdzNote( "em Becomes Null", HIDE);
    public static final Note EMP_ERRORS = new SdzNote();
    public static final Note PROP_EDITOR_CONVERT = new SdzNote( "Property Editor Convert", HIDE);
    public static final Note TABLE_VALUE_TRACE = new SdzNote();
    public static final Note BAD_TABLE_REPOSITIONING = new SdzNote();
    public static final Note QUERY_NOT_WORKING_NON_VISUAL = new SdzNote();
    public static final Note CTV_FOCUS = new SdzNote( "Component Table View Focus", HIDE);
    public static final Note CTV_IGNORE_FOCUS = new SdzNote(
        "Component Table View Ignore Focus gained", HIDE);
    public static final Note CTV_ADD_CELL = new SdzNote( "Component Table View Add Cell", HIDE);
    public static final Note CTV_MODEL_CHANGED = new SdzNote( "Component Table View Model Changed", HIDE);
    public static final Note CTV_HOW_MANY = new SdzNote( "See how many tables", HIDE);
    public static final Note CTV_CLICK = new SdzNote( "Component Table View Mouse click", HIDE);
    public static final Note CTV_TURTLE = new SdzNote( "Turtle - up and down", HIDE);
    public static final Note CTV_SDZ_INTEGRATION = new SdzNote( "Component Table View Sdz Integration", HIDE);
    public static final Note DONT_USE_FROM_STRANDZ = new SdzNote();
    public static final Note CTV_ADVANCED_COMP = new SdzNote( 
            "Component Table View - using simple comps to store enabled settings", HIDE);
    public static final Note TX = new SdzNote();
    public static final Note PRE_AND_POST = new SdzNote();
    public static final Note FIRST_VALIDATE_NOT_PICKED_UP = new SdzNote();
    public static final Note SDZ_HAS_CONTROL = new SdzNote( "sdzHasControl", HIDE);
    public static final Note SUB_MENU = new SdzNote();
    public static final Note ADD_SAME_BLOCK_TWICE = new SdzNote();
    public static final Note DEFAULTING = new SdzNote();
    public static final Note DISPLAY_LOV = new SdzNote( 
        "New method Cell.displayLovObjects() for when need to dynamically alter what's in a Combo/LOV",
                                                        HIDE);
    public static final Note LOV_SUBSTITUTION = new SdzNote( HIDE);
    //Below two normally/often kept at SHOW
    public static final Note MONITOR_QUERIES = new SdzNote( "Monitor Queries", HIDE);
    public static final Note PERFORMANCE = new SdzNote( "Performance", HIDE);
    public static final Note ROW_AND_EXTENT_NOT_SYNCED = new SdzNote( "Row and Extent not Synced", HIDE);  
    public static final Note ROW_AND_EXTENT_NOT_SYNCED_DETAIL = new SdzNote( "Row and Extent not Synced Ddetail", HIDE);  
    public static final Note RECORD_CURRENT_VALUE = new SdzNote( "Record Current Value", HIDE);  
    public static final Note RECORD_CURRENT_VALUE_DETAILS = new SdzNote( "Record Current Value Details", HIDE);  
    public static final Note DERIVED_DATA = new SdzNote( "Derived Data", HIDE);
    public static final Note USER_DETAILS = new SdzNote( "User Details Spring Service");  
    public static final Note LOAD_FILE_PROPERTIES = new SdzNote( "Load File Properties");  
    public static final Note LOVS_CHANGE_DATA_SET = new SdzNote( "lovs old when have changed dataset", HIDE);  
    public static final Note INDENT = new SdzNote( "indent", HIDE);
    public static final Note BO_STUFF = new SdzNote( "Tx", SHOW);
    public static final Note GRAPHS = new SdzNote( "FreeChart graphs", HIDE);
    public static final Note WHY_LINES_DIFFERENT = new SdzNote();
    public static final Note TABLE_MODEL_CONFUSED = new SdzNote( "Model using for copying being confused");
    public static final Note ACCUMULATED = new SdzNote( "Accumulated");
    public static final Note ACTIONS_REQUIRED_ON_TOOL_BAR = new SdzNote( "Actions needed for Toolbar");
    public static final Note NO_HOUSING_HELPERS = new SdzNote( 
            "No housing applichousing controls wanted if not specified in DT file", HIDE);
    public static final Note BATCH = new SdzNote( "batch", HIDE);
    public static final Note TOO_MANY_TABLE_NOTIFICATIONS = new SdzNote( "Too many table notifications", HIDE);
    public static final Note CTV_STRANGE_LOADING = new SdzNote( "CTV Strange Loading", HIDE);
    public static final Note VISIBLE_STRAND_IS_INDEPENDENT = new SdzNote( 
            "Making VisibleStrand detachable from Application", HIDE);
    public static final Note GO_NODE = new SdzNote( "Go Node", HIDE);
    public static final Note SET_ROW = new SdzNote( "Set row for table", HIDE);
    public static final Note WANT_ALL_ON_DETAIL = new SdzNote( "Want all on detail", HIDE);
    public static final Note SYNC = new SdzNote( "Sync", HIDE);
    public static final Note REFRESH = new SdzNote( "Refresh", HIDE);
    public static final Note REFRESH_DETAIL = new SdzNote( "Refresh", HIDE);
    public static final Note CALC_ITEM = new SdzNote( "Calc Item", HIDE);
    public static final Note SAFE_REFRESH = new SdzNote( "Safe Refresh", HIDE);
    public static final Note NODE_GROUP = new SdzNote( "Node Group", HIDE);
    public static final Note OVER_FREEZING = new SdzNote( "Over Freezing", HIDE);
    public static final Note MANY_NON_VISUAL_ATTRIBS = new SdzNote( "Multiple Non-applichousing attributes", HIDE);
    public static final Note NOT_KEEPING_PLACE = new SdzNote( "Not keeping place", SHOW);
    public static final Note ADDING_TABS_TOO_MANY_TIMES = new SdzNote( "Adding tabs too many times (not yet investigated)", HIDE);
    public static final Note WRONG_PHYSICAL = new SdzNote( "Wrong physical controller", HIDE);
    public static final Note BI_AI = new SdzNote( "Before and after images - to see why Attributes changed", HIDE);
    public static final Note TRANSFER_OUT_POSITION = new SdzNote( "Not positioning back to where came from (is ok)", HIDE);
    public static final Note NO_TABS_THEN_BACK = new SdzNote( "No tabs then back", HIDE);
    public static final Note ROSTERABILITY = new SdzNote( "Rosterability", HIDE);
    public static final Note RED_WHEN_ENTER_QUERY = new SdzNote( "Red when enter query", HIDE);
    public static final Note STATUS_BAR = new SdzNote( "Status Bar", HIDE);
    public static final Note NULL_WORKER_ACROSS_WIRE = new WombatNote( "Null worker from Spring", HIDE);
    public static final Note TOO_MANY_FILL_INS = new WombatNote( "Too many fill ins", HIDE);
    public static final Note CREATING_DOS = new SdzNote( "Creating DOs", HIDE);
    public static final Note TABLE_REFRESH = new SdzNote( "Table refresh", HIDE);
    public static final Note SLOW_SYNC = new SdzNote( "Slow Sync", HIDE);
    public static final Note SET_TEXT_BLANK = new SdzNote( "Setting Text Blank", HIDE);
    public static final Note NOT_REPLACE_BLOCKS = new SdzNote( "Not replace blocks hack", HIDE);
    public static final Note FIRST_ITEM_FALLBACK = new SdzNote( "Using first item as fallback", HIDE);
    public static final Note SET_OBJCOMP_TABLE = new SdzNote( 
            "Set ObjComp for table needs be done just like as if was field based", HIDE);
    public static final Note BRING_BACK_TAB = new SdzNote( "Remove a tab then bring it back", HIDE);
    public static final Note COMMIT_SELECT_DISPLAY = new SdzNote( 
            "Saving and doing Sdz Action again - what is diff?", HIDE);
    public static final Note NO_CALCS = new SdzNote( "No calcs being put in when trying to run same app twice", 
                                                     HIDE);
    public static final Note TEMPORAL_PROPERTY = new SdzNote( "Temporal Properties", HIDE);
    public static final Note CAYENNE_CONSTRUCTOR = new SdzNote( "Cayenne needs a diff class constructed", HIDE);
    public static final Note CAYENNE_OVER_PERSISTING = new SdzNote( "Cayenne persisting too many DOs", HIDE);
    public static final Note SECONDARY = new SdzNote( "Secondary - how we solved over-persisting", HIDE);
    public static final Note WRITE_TO_DATAOBJ = new SdzNote( "Write out to DO", HIDE);
    public static final Note PERFORMANCE_TUNING = new SdzNote( "Performance tuning (using Cayenne prefetches)", HIDE);
    public static final Note CTV_CRUD = new SdzNote( "CRUD Operations for CTV so works in Dsgnr", HIDE);
    public static final Note INVOKE_WRONG_FIELD = new SdzNote( "Invoke wrong field", HIDE);
    public static final Note READ_USER_PROPERTY = new SdzNote( "Read user properties", HIDE);
    public static final Note DYNAM_ATTRIBUTES = new SdzNote( "Dynamic Attributes", HIDE);
    public static final Note SET_AND_CREATE_BLOCKS = new SdzNote( "Set and create blocks", HIDE);
    public static final Note DISABLE_ATTRIBUTE_CHANGES = new SdzNote(
        "Disable Focus part of Attribute Changes Monitor", HIDE);
    public static final Note TOO_MANY_LABELS = new SdzNote( "Too many labels");
    public static final Note INDEX = new SdzNote( "Index", HIDE);
    public static final Note CTV_ONLY_EDIT_ROWCHANGE = new SdzNote( 
        "A CTV that is not editable currently can't do row changes", HIDE);
    public static final Note CTV_SDZ_BUG = new SdzNote(
        "CTV not well integrated into Sdz if have to use this trigger", HIDE);
    public static final Note FIRST_MERGE = new SdzNote( "When merging just working", HIDE);
    public static final Note SCORECARD = new SdzNote( "Scorecard", HIDE);
    public static final Note TIES_ENFORCE_TYPE = new SdzNote( "Ties Enforce Type", HIDE);
    //These three are all related
    public static final Note CELL_CLAZZ = new SdzNote( "Cell Clazz (when using wrong read method)", HIDE);
    public static final Note INSTANTIATING_LOOKUP = new SdzNote( "Instantiating lookup ", HIDE);
    public static final Note BAD_READING = new SdzNote( "Bad Reading", HIDE);
    public static final Note EXPANDER_IDX = new SdzNote( "Expander Index", HIDE);
    //

    static
    {
        String desc;
        String fix;
        
        desc = "Made coming from top do query which makes sense to user. Selecting and un-selecting" +
                " does not work as would expect, but as no bugs we'll leave that till later...";
        COMMIT_SELECT_DISPLAY.setDescription( desc);
        
        desc = "Main culprit is manually done REFRESH - so see REFRESH";
        TOO_MANY_TABLE_NOTIFICATIONS.setDescription( desc);

        desc = "Is call to this necessary? One issue is when doing refresh of the Quick RS and have" +
                " just done an insert of a new RS. The quick detail node will want to process one more" +
                " row than it has controls for. There is no concept of a node being visually realised so" +
                " we may need to ignore the exception that a control does not exist. Seemed to only be" +
                " need for inserting.";
        SET_TEXT_BLANK.setDescription( desc);        
        
        desc = "Getting together some hacks from around the place for table refreshing. Prefer to" +
                " stay with these hacks for the moment rather then risk the performance blowout" +
                " of a generic solution. Really two problems. With setData() a modelDataChanged() to" +
                " the table is required. With changing node a refresh is required." +
                " Fix in conjunction with TOO_MANY_FILL_INS. Don't need modelDataChanged() after setData()" +
                " any more.";
        TABLE_REFRESH.setDescription( desc);        

        desc = "Just from asking for the roster a very large number of workers were created - seems unnecessary";
        TOO_MANY_FILL_INS.setDescription( desc);        
        
        desc = "Looks like setting text blank for ENTER QUERY gives us this blank row as well, " +
                "which is RED by default. Leaving this till later as it is cosmetic.";
        RED_WHEN_ENTER_QUERY.setDescription( desc);
        
        desc = "Currently confusing when change tabs and change the rosterability of workers";
        ROSTERABILITY.setDescription( desc);
        ROSTERABILITY.setFixed( true);
        fix = "When tab we commit and query on both, making sure do the altered one first";
        ROSTERABILITY.setFix( fix);
        
        desc = "After dialog want current user to be the one whose roster slots were queried into the dialog";
        NOT_KEEPING_PLACE.setDescription( desc);
        NOT_KEEPING_PLACE.setFixed( true);
        fix = "Use afterUserDismissed() to make the master node the current one so a (re) query will not be forced";
        NOT_KEEPING_PLACE.setFix( fix);
        
        NODE_GROUP.setCauseProblem( false); //false so doesn't stack
        
        desc = "A detail table ought to be showing all rows but is only showing first";
        WANT_ALL_ON_DETAIL.setDescription( desc);
        String howFixed = "Solved by removing all controls when num of rows changes";
        WANT_ALL_ON_DETAIL.setFix( howFixed);

        desc = "Everything works fine if label the ordinals from 0 upwards. Replicate problem by going from 1" +
                " upwards in Designer. Need to do some rationalisation of column and ordinal. Needs to be a" +
                " translation of ordinals (DT) into columns (RT) - for example never have both going in as" +
                " parameters of a method. This way will be both missing number and start from whereever tolerant" +
                " - pretty essential before others use the Designer";
        CTV_STRANGE_LOADING.setDescription( desc);
        //
        desc = "Dicovered that the action for a Search Button was not an AbstractAction itself," +
                " so couldn't even put a LONG_DESCRIPTION on it";
        ACTIONS_REQUIRED_ON_TOOL_BAR.setDescription( desc);
        //
        BO_STUFF.setName("Business Object stuff");
        BO_STUFF.setDescription("Business Object stuff. Related to wanting to refactor BO" +
            " a bit. Also one dataStore really s/be shared by all.");
        //
        LOVS_CHANGE_DATA_SET.setName( "lovs old when have changed dataset (to Wed night comp)");
        desc = "Reason for problem is that the old value for division is still being returned " +
                "(from division = (Division)dt.divisionLookupCell.getItemValue();) even thou the " +
                "division had been changed. Call is from setLOVs() which is done as part of doing " +
                "another query (same as getting the tab up again). The simple rule that will hopefully " +
                "solve this problem is that when we shutdown a strand (a tab is a strand) - detach its " +
                "controls - that if code subsequently asks for data null is returned. Thus closing a tab means that " +
                "the data in it will no longer be accessible, however the controls will be kept for the " +
                "purpose of re-constituting the sdz object graph (consumeNodesIntoRT)";
        LOVS_CHANGE_DATA_SET.setDescription( desc);
        //
        desc = "Want to see what service has returned. As service uses caching - sometimes need to see if the new " +
                "value has been picked up. Note that you have to visit where this is called from to actually get" +
                " these security details to show!";
        USER_DETAILS.setDescription( desc);
        //
        ROW_AND_EXTENT_NOT_SYNCED.setName( "row and Extent not Synced");
        ROW_AND_EXTENT_NOT_SYNCED.setDescription( "The current row (of a node) should always be indexable into " +
                "the extent/data held by the row");
        //
        LOV_SUBSTITUTION.setName( "LOV substitution");
        desc = "Kodo complains with <java.lang.UnsupportedOperationException: Result lists are read-only.>. Do " +
                "other JDOs complain as well - ie. are their returned lists RO? Prolly. Normal LOVs from LOV" +
                " tables are automatically wrapped in ArrayLists - we were doing complex stuff with teams and" +
                " divisions and not wrapping b4 calling setLOV";
        LOV_SUBSTITUTION.setDescription( desc);
        //
        DEFAULTING.setName("Defaulting");
        //
        ADD_SAME_BLOCK_TWICE.setName("Somehow same block is being added twice to a composite when EXEQRY");
        desc = "Initial query when block comes up works fine - only difference in code goes thru s/be " +
                "destroying the old structures. Replicate by running SuperSix with CompetitionSeason -> Meets -> Games. " +
                "Done fix but it's a complete hack";
        ADD_SAME_BLOCK_TWICE.setDescription( desc);
        //
        SUB_MENU.setName("Application Housing (MenuTabApplication) submenus - currently only one level but will do more");
        //
        FIRST_VALIDATE_NOT_PICKED_UP.setName("first change not picked up - for combo box - also investigate why doing right at end");
        //
        PRE_AND_POST.setName("Pre and Post triggers only seem to happen the first time - expected them to happen as moved from tab to tab");
        //
        TX.setName("Transaction");
        //
        desc = "Transfer of values not happening for all comps. Did this b/c want the creation of" +
                " the real components (by user) to be at time when model exists. The simple comps are" +
                " really just enabled holders. A 'bug' that exists here is that when no data comes in" +
                " to a row these holders stay there and getControlAt() will return them.";
        CTV_ADVANCED_COMP.setDescription(desc);
        //
        DONT_USE_FROM_STRANDZ.setName("Dont use from Strandz - need to use classes from convert package");
        //
        CTV_TURTLE.setName("Component Table View turtling around");
        desc = "As navigate around using keys, editable rows are created as move to them";
        CTV_TURTLE.setDescription(desc);
        //
        QUERY_NOT_WORKING_NON_VISUAL.setName("Query not working for NonVisual controls");
        desc = "Lots of debugging here on setting enabled. Reason for problem was due" +
            " to the fact that NV controls were returning enabled as F always, as no method" +
            " has been assigned to them in ControlSignatures. Now returning T always. Ideal" +
            " would be for NV controls to actually have their own methods.";
        QUERY_NOT_WORKING_NON_VISUAL.setDescription(desc);
        //
        BAD_TABLE_REPOSITIONING.setName("Bad Table Repositioning");
        desc = "When arrow key down to the last row, is trying to goAsBefore() above row index";
        BAD_TABLE_REPOSITIONING.setDescription(desc);
        //
        TABLE_VALUE_TRACE.setName("Table Value Trace");
        desc = "Trace as a table control's column value is extracted";
        TABLE_VALUE_TRACE.setDescription(desc);
        //
        desc = "To instrument the from and to of a conversion";
        PROP_EDITOR_CONVERT.setDescription(desc);
        //
        EMP_ERRORS.setName("Entity Manager Provider caused errors");
        //
        desc = "There was no em being specified for aggregation relationships. em being null is quite" +
                " common - either no trigger or the trigger is returning null. Where a strand" +
                " has no dataStore or the dataStore is not entity managed (eg XMLDatastore) then" +
                " either of these will be the case. There will be danger if you upgrade your strand to" +
                " having an entity managed dataStore and the trigger is no overspecified enough to draw" +
                " your attention to what needs to be done";
        EM_BECOMES_NULL.setDescription(desc);
        //
        COMBO_BEING_RED.setName("JComboBox is RED when don't want it to be");
        desc = "Is sometimes blue even when have set to RED. Also does not" +
            " go back to normal after set back to not being in error. For now" +
            " lets not bother setting to bg RED at all.";
        COMBO_BEING_RED.setDescription(desc);
        COMBO_BEING_RED.setFixed(false);
        //
        String desc1 = "Red color sometimes lingers - for instance when delete (fixed)";
        String desc2 = "When known date wrong takes 2 tabs (perhaps b/c focus after dialog [Ok] " +
            "does not go directly back, but to somewhere unknown - see SdzNote.whereAfterMessageDlg)";
        desc = desc1 + "\n" + desc2;
        FIELD_VALIDATION.setDescription(desc);
        FIELD_VALIDATION.setFixed(true);
        //
        WHERE_AFTER_MESSAGE_DLG.setName("Where Focus After MessageDlg?");
        WHERE_AFTER_MESSAGE_DLG.setFixed(true);
        //
        STILL_RED_WHEN_DELETE.setName("Still Red When Delete");
        desc = "Field validation has failed and the user decides to delete - the field" +
            " that was in error still shows as RED, but shouldn't. Noticed as part of this" +
            " that blanking out happens twice when delete - once would be enough";
        /*
          "JComboBox background"

          http://forum.java.sun.com/thread.jspa?threadID=143735&messageID=399266
          http://forum.java.sun.com/thread.jspa?threadID=448892&messageID=2035688
          http://www.javadatepicker.com/docs/tutorial/jdatepicker.html
        */
        STILL_RED_WHEN_DELETE.setDescription(desc);
        STILL_RED_WHEN_DELETE.setFixed(true);
        //
        BLANK_TWICE_WHEN_DELETE.setName("blanking out happens twice when delete, prolly");
        BLANK_TWICE_WHEN_DELETE.setDescription(desc); // from stillRedWhenDelete
        //
        NV_PASTE_NOT_WORKING.setName("Non Visual (Item Attributes) Paste not Working");
        desc = "Paste problem was the symptom. Cause was that monthlyRestartAttribute was" +
            " not being used. Stopped working again. This time just needed to make the test" +
            " (NVTR.testCopyPaste()) do a POST";
        NV_PASTE_NOT_WORKING.setDescription(desc);
        NV_PASTE_NOT_WORKING.setFixed(true);
        //
        BLANKOUT_A_RECORD_YOURSELF.setName("blankoutARecordYourself");
        desc = "Go to a Roster Slot and stuff it up so you get all sorts of error" +
            " messages. When finally blank it out you still get the last error message." +
            " Really the message you should be getting is that it is blank. Possibly do" +
            " SdzNote.noMoveState at the same time as do this.";
        BLANKOUT_A_RECORD_YOURSELF.setDescription(desc);
        //
        CANT_ADD_RS.setName("cantAddRS");
        desc = "The Memory version of WR was not allowing Noreen Neve to be given a Roster Slot.";
        CANT_ADD_RS.setDescription(desc);
        CANT_ADD_RS.setFixed(true);
        CANT_ADD_RS.setFix("Made MemoryEntityManager make changes to the real lists");
        //
        desc = "Issues with installing a new component. Made DOAdapters converting less tight. Thus" +
            " now components will always also accept subclasses of the type they are supposed to work" +
            " with. When this (inevitably) works out to NOT be the way to go, create a Date to Date" +
            " (say for JDO Dates) convert to give this looseness on a case by case basis";
        MDATE_ENTRY_FIELD.setDescription(desc);
        //
        NEW_DATA_STORE_FACTORY.setName("newDataStoreFactory");
        desc = "Fix everything up after this major refactoring";
        NEW_DATA_STORE_FACTORY.setDescription(desc);
        //
        desc = "In TestRoster the tests run ok on their own but not one after the other";
        SECOND_TEST.setDescription(desc);
        SECOND_TEST.setFixed(true);
        SECOND_TEST.setFix("ValidationContext is a static so needs to be reset");
        //
        USE_CS.setName("useCS");
        desc = "ControlSignatures needs to be used consistently by all Attributes, esp" +
            " NonVisualFieldAttribute that now needs to use it when setText when it is" +
            " acting as a replacement for an actual item. This way pushing onto the 'screen'" +
            " will do the actual conversion. Even though it is a virtual screen to be" +
            " consistent (and for unit tests to work consistently) it needs to have a String" +
            " in it if that's what the real item had. First noticed this problem when running" +
            " applichousing and non-applichousing versions of TestRoster.";
        USE_CS.setDescription(desc);
        USE_CS.setFixed(true);
        USE_CS.setOngoingConcerns("Still not done same to NonVisualTableAttribute. When do set TestTable" +
            " to run on the server.");
        //
        DATE_CONVERT.setName("dateConvert");
        desc = "dateConvert";
        DATE_CONVERT.setDescription(desc);
        //
        CREATE_FROZEN.setName("createFrozen");
        desc = "createFrozen";
        CREATE_FROZEN.setDescription(desc);
        //
        NO_MOVE_STATE.setName("noMoveState");
        desc = "The NO_MOVE states work but need to be thought about. They only work for " +
            "blank records and stop the user doing things until the blank record has been " +
            "filled in. Four issues. 1./ You shouldn't need a state if the problem can be " +
            "recognised in the first place - that recognition code should be relied upon " +
            "for all situations. 2./ These states are being under-used for applichousing effects, " +
            "for example the toolbar should disable almost everything but doesn't. As come " +
            "out of the state again can obviously put the memento back again. 3./ Why only used " +
            "for blank records? Surely record and block (?and field) level validation would also benefit from " +
            "toolbar and other things not being enabled when in a certain state. 4./ Everything " +
            "needs to be pretty reliable before state-driven unenabling is implemented as for " +
            "programmatic operations unenabling counts for nothing - thus when implement this " +
            "should also have a very easy way of turning it off - so we can always test what " +
            "happens when we alter things with the programmatic interface.";
        NO_MOVE_STATE.setDescription(desc);
        //
        TIGHTEN_RECORD_VALIDATION.setName("tightenRecordValidation");
        desc = "tightenRecordValidation";
        TIGHTEN_RECORD_VALIDATION.setDescription(desc);
        //
        desc = "currentPane - think this concept only important for the Designer";
        CURRENT_PANE.setDescription(desc);
        //
        DYNAM_ALLOWED.setName("dynamAllowed");
        desc = "Changing what is allowed at runtime";
        DYNAM_ALLOWED.setDescription(desc);
        //
        desc = "When press Update on the Designer loads of comparisons are done";
        REASSIGN_CONTROL.setDescription(desc);
        //
        BACK_SENSIBLE.setName("Back to sensible state");
        BACK_SENSIBLE.setDescription("Anything to do with going back to a sensible state");
        //
        TZ.setName("Time Zone");
        TZ.setDescription("Anything to do with timezones");
        //
//        showTimes.setName("Show Times");
//        showTimes.setDescription("For measuring how long lengthy operations take");
        //
        TIES.setName("Ties Note");
        TIES.setDescription("Messages all for ties");
        //
        GENERIC.setName("Generic Strandz Note");
        GENERIC.setDescription("Catch all for any Strandz notes which are 'on their own'");
        //
        desc = "Looks like this is happening even with Comp ie. NonVisual attributes." +
            " We shouldn't stop Swing components doing this if they want. Ways" +
            " around would be to use a Convert. As part of FieldControlDescriptor need" +
            " to have setting that null Strings are always converted to nulls before" +
            " going into DB, which IMHO is the preferred way, and less hassle than" +
            " using Converts. TestORMapping.testUpdateToNull() and another are failing" +
            " because getControlSignatures() is returning null String. This not surprising," +
            " but it is surprising that happening even when boolean useItem = false, where" +
            " would have thought Comp would have been used. No matter how the control is" +
            " configured, setItemValue( null) should end up with null in the DB. See" +
            " comments at 'Merde' as well";
        POLLUTING_DB_WITH_NULL_STRINGS.setDescription(desc);
        POLLUTING_DB_WITH_NULL_STRINGS.setCauseProblem(false);
        //
        GO_AS_BEFORE_TOO_COMPLEX.setName("goAsBefore() too complex");
        desc = "Automatically focusing often brings forth validation. We are only half-baked" +
            " here - but POST was causing goAsBefore, which was succeeding, which meant that" +
            " POST then returned that it had succeeded when in fact it had failed. Why was POST" +
            " doing any movement at all? Maybe that's the real bug. Have now stopped doing goAsBefore" +
            " in cases where no movement would have been expected, such as POST. goAsBefore was" +
            " probably just focusing where had been before. The problem was not with this, but" +
            " with a new (and thus 'not in error') ValidationContext being created because had" +
            " successfully done this non-move. TestContextController.testValidationKicksIn was " +
            " where the POST was happening from. Will now need OperationEnum to have a new method" +
            " isMovement()";
        GO_AS_BEFORE_TOO_COMPLEX.setDescription(desc);
        //
        GET_ITEM_LIST_ONLY_FOR_PAINTED.setName("getItemList() will only work for painted cells");
        desc = "See TestTable.testCopyPasteMasterDetail() - run with loads of rows, more" +
            " than can display, and see if when get the list it won't be all of them";
        GET_ITEM_LIST_ONLY_FOR_PAINTED.setDescription(desc);
        //
        NODE_VALIDATION.setName("Node Validation");
        desc = "When post or commit or go to another node, ALL validation" +
            " should be done. Check that this is the case for item and record" +
            " validation. It should also be the case for node validation. TestTable.main" +
            " is a good place to experiment. There is some Node Validation that checks for" +
            " \"Billy\" which should be made into a loop if not already.";
        NODE_VALIDATION.setDescription(desc);
        //
        TABLE_ITEM_VALIDATION.setName("Table Item Validation");
        desc = "This shows how table validation not working at all. Should"
            + " happen when change the value of the field, and should show b4"
            + " and after images differently, and obviously have not altered"
            + " any data in memory (won't have). This is where we need to start"
            + " using FormattedTextFields, and prolly get away completely from"
            + " using FocusListening validation (which includes )";
        TABLE_ITEM_VALIDATION.setDescription(desc);
        //
        DYNAMIC_ALLOWED.setName("Dynamic Allowed");
        desc = "The dynamicAllowed style code came about because many of the"
            + " preferences that the user puts in will need to be overridden by Strandz,"
            + " for example NEXT s/not be available when at the end of a list. There"
            + " is a difference between the *configuration allowed* value (which encompasses"
            + " both design and run-time, and might be refered to as a hint or preference)"
            + " and the *current allowed* value, which is determined by some code within"
            + " Strandz. There can even be another, the *control allowed* value, which"
            + " will examine the control in question. If the state (always a boolean by"
            + " the way) of the 'current allowed' is always kept with the control, then"
            + " we can dispense with 'control allowed' - just make sure we obey that state"
            + " rule! Thus the properties might be called *allowedPreference* and *allowed*."
            + " A good example for state being stored with the control is editability. Prolly"
            + " best to complete the editabilityRefactoring bug before this one.";
        DYNAMIC_ALLOWED.setDescription(desc);
        //
        ENABLEDNESS_REFACTORING.setName("Editability Refactoring");
        desc = "Seems a bit strange that on JTableModelImpl we are going all"
            + " the way round to the node, and then to the attribute, just to"
            + " get to the actual table control's control, when all along"
            + " JTableModelImpl is the actual model. Err - there is an error in"
            + " this thinking, as RowBuffer is where the *current allowed* is"
            + " kept. Thus the code already implemented is fine. With a TableModel "
            + " there is actually no concept of a "
            + " *control allowed*, which is of course different to a field. Anyway"
            + " the recommended method signatures in the above bug will still hold."
            + " All need to do here is get the method signatures right and test that"
            + " fields work, and verify that the state behind the *allowed* method call"
            + " is held with the control.";
        ENABLEDNESS_REFACTORING.setDescription(desc);
        //
        NOT_UPDATE_ATTRIBUTE.setName("Can't specify that can't update a table attribute");
        desc = "Can still double click on the table Cell. Not sure whether"
            + " would stop editing field attributes either. Work arounds in"
            + " both cases.";
        NOT_UPDATE_ATTRIBUTE.setDescription(desc);
        //
        EDIT_LOOKUP_ON_J_TABLE.setName("User editing a lookup column on a JTable");
        desc = "Set up a complex structure with lookups and map it to a JTable,"
            + " as did with a Fault application. When change one of the looked up"
            + " values they are all changed. This expected, but not what the user"
            + " would expect. Is the mapping done differently for fields? Need to"
            + " experiment with what need to do,and put in a ComboBox and see how"
            + " to make this work";
        EDIT_LOOKUP_ON_J_TABLE.setDescription(desc);
        //
        IGNORED_CHILD_TABLE_STILL_GETS_UP_DOWN.setName("An ignoredChild JTable responds to up/down keys in context of its parent");
        desc = "This proves that the keys are not going thru the same route. It may not"
            + " be impossible to improve this, in which case the keys should be disabled"
            + " for an ignoredChild.";
        IGNORED_CHILD_TABLE_STILL_GETS_UP_DOWN.setDescription(desc);
        //
        TRACKER_NEW_WAY_OBSERVING_FOCUS_CHANGES.setName("Tracker new way of Observing FocusChanges");
        desc = "You can now register a PropertyChangeListener that will receive "
            + " PropertyChangeEvents when a change to the focus state occurs. Should"
            + " track the focus this way, although I'm not sure how much help it will be.";
        TRACKER_NEW_WAY_OBSERVING_FOCUS_CHANGES.setDescription(desc);
        //
        DELETE_CONTEXT_DELETE_GENED_FILES.setName("Are not currently deleting GenedFiles when have deleted Context");
        //
        SEARCH_WORKING_QUERY_NOT.setName("Search Working but Query not working");
        desc = "Found out by constructing a simple application on roster slots"
            + " and search for a partic known date. Works perfectly with Search"
            + " but Query returns no rows.";
        SEARCH_WORKING_QUERY_NOT.setDescription(desc);
        //
        desc = "Debugging 'changing nodes and states' will be often required.";
        CHANGING_NODES_STATES_I_NOT_RECOGNISED.setDescription(desc);
        CHANGING_NODES_STATES_I_NOT_RECOGNISED.setFixed(true);
        CHANGING_NODES_STATES_I_NOT_RECOGNISED.setFix("The type of entry points that Insert"
            + " was covered by was being ignored by MoveTracker. When we stopped these type"
            + " of entries being ignored the row change to 0 was picked up, so validation"
            + " was able to nicely focus back on the first table row where it was trying to"
            + " get to. As part of this fix stopped the row++ thing which never understood"
            + " anyway. Back to fixed false as was hasty with that row++ thing. Problem now" +
            " getting is at TestTable.testInsertTableSelectedRowOk() where reposition back" +
            " to 0 even thou have done an insert and s/be at 1. Hmm - set to fixed again as" +
            " all TestTable tests pass");
        //
        AUTO_SELECT_TABLE_ROW_AFTER_INSERT.setName("Auto Select Table Row After Insert");
        desc = "TestTable.testInsertTableSelectedRowOk was failing, so even thou have" +
            " told the model to insert a row, still need to fire that the selected" +
            " row has changed. This bug was about wondering whether had to do that. Do" +
            " have to! Actually more needed to be done to fix the test " +
            " TestTable.testInsertTableSelectedRowOk";
        CHANGING_NODES_STATES_I_NOT_RECOGNISED.setDescription(desc);
        CHANGING_NODES_STATES_I_NOT_RECOGNISED.setFixed(true);
        //
        desc = "Definitely comments in pad, and prolly in code somewhere too"
            + " about this. If setNode() is called on a SdzBagI then it should"
            + " not just change the node, but (for example) change to the tab in the"
            + " case of a SdzBag. Current problem with this is that need to explicity" 
            + " go to a master node first, otherwise will end up where need to be except" 
            + " in FROZEN state. Just because current tabbed application housing doesn't" 
            + " support user doing this does not mean it is not valid. See testDetailToDetail()";
        SHOULD_BE_ABLE_TO_CHANGE_NODE_VISUALLY.setDescription(desc);
        SHOULD_BE_ABLE_TO_CHANGE_NODE_VISUALLY.setFixed(false);
        //
        BAD_WOMBAT_VALIDATION.setName("Bad Wombat Validation");
        desc = "Go to Nick Airly and try to create another roster slot for him. Even"
            + " if valid will get the popup error, and will get it twice. Also you seem to"
            + " be allowed to tab out, which you weren't b4. Even when validation taken away"
            + " the problem that get is that the values that you set are taken away as soon"
            + " as you previous to another roster slot. Thus possibly this is the real cause"
            + " of the problem and the validation thing is just a furfy.";
        BAD_WOMBAT_VALIDATION.setDescription(desc);
        BAD_WOMBAT_VALIDATION.setFixed(true);
        BAD_WOMBAT_VALIDATION.setCauseProblem(true);
        BAD_WOMBAT_VALIDATION.setFix("fixed as part of tighten...");
        //
        TOOL_BAR_SHOULD_BE_IN_STRAND_AREA.setName("ToolBar Should Be In StrandArea");
        TOOL_BAR_SHOULD_BE_IN_STRAND_AREA.setFixed(true);
        //
        TWO_TABS_FOR_VALIDATION_TO_WORK.setName("Two tabs required before validation works");
        desc = "Can replicate on wombatrescue as well. To see on MLD (good because it has"
            + " the JTable as well) put view package as ui rather than view, and ctl-tab away. Will"
            + " need to go twice before turns RED. Also twice to put to correct value. Going forward"
            + " (tab only) doesn't work at all (perhaps due to table). Once have fixed so first tab"
            + " registers, see if this fixes going forward to table as well. Another test to do is to"
            + " set the package so that it is wrong, then [validate], then enter into the package tf."
            + " What happens is that validation occurs as enter the tf. This is wrong as validation"
            + " should only occur as leave it";
        TWO_TABS_FOR_VALIDATION_TO_WORK.setDescription(desc);
        //
        COMPLETED_ADD_BUT_STILL_NEW.setName("completeAdd() done yet still in New state");
        desc = "The cautious trigger writer will not want to use Node.getRow() to index"
            + " into the actual data when in INSERT state, as insert state should mean that"
            + " no actual insert has yet taken place. There must be a reason for the change in"
            + " state being delayed (? block.postCompleteAdd() has yet to be done), but it should"
            + " be overcome to reduce the confusion to the user. Thinking - surely if have done the"
            + " insert into the actual list, then the transaction is over? Answer was yes, and now"
            + " this bug has been fixed by changing state back to sensible before doing "
            + " firePostingEvent(). These decisions should be made a little more formally! Also"
            + " still have CMLBug.whichRowOn to investigate yet, which occurs often, probably quite"
            + " validly in most cases. Also MLDBug.indexingIntoRealDataWhileInsert";
        COMPLETED_ADD_BUT_STILL_NEW.setDescription(desc);
        //
        WHICH_ROW_ON.setName("Which Row are On");
        desc = "Is row really being set correctly? What about whether in INSERT/EDIT modes?"
            + " If set it incorrectly here then may end up going back after failure say correctly"
            + " if was in insert mode, and incorrectly if was in edit mode, or whatever. Need some"
            + " unit tests around all this stuff. Should not be getting a current row whilst in NEW "
            + " state if are going to be indexing into the list b4 the post has been done.";
        WHICH_ROW_ON.setDescription(desc);
        //
        SAVE_CHANGES.setName("Save changes");
        desc = "Need a ChangeListener for every single property that will go"
            + " into XML file. ie. every property of Cell, Attribute etc. Only need one"
            + " listener and one event. Will be kept in each SdzBagI. Maybe a factory"
            + " can be used at DT, and this factory does the job of assigning the listener."
            + " The listener will record changes as they are made. With this in place exiting"
            + " from MLD will be able to bring up a Dialog asking if you want to save changes"
            + " For the context side of things call isDirty. All actual XML changes are done"
            + " using mini, local sdzBags, so they can fireChangeEvent if isDirty"
            + " before they are whisked out of memory. As part of this do a little doco map"
            + " which for each property of XML CI, says which customiser works it. Better way"
            + " of doing this would be to have copy constructors - when initially read in the"
            + " xml file take a copy, and compare before save the xml. Currently doing via XML"
            + " memory copy way. See ContextController.createCICopy() for where to change so"
            + " instead uses set(), equals(), hashCode(). Yet to do Dialog to ask if user wants"
            + " to save.";
        SAVE_CHANGES.setDescription(desc);
        //
        TASK_BAR_APPEARANCE.setName("TaskBar appearance");
        desc = "Too much height in MLD, and text not vertically centered";
        TASK_BAR_APPEARANCE.setDescription(desc);
        TASK_BAR_APPEARANCE.setFixed(true);
        //
        INTERMITTENT_UNIT_TEST_FAILURE.setName("Intermittent UnitTest Failure");
        desc = "Probably due to code being in test itself rather than setup. "
            + " Can replicate with new machine. Will need to make other"
            + " test classes. The problem is that with unit testing many threads are all"
            + " using the same strand, and thus can overwrite each other when they for"
            + " instance 'blank records allowed for this node'. Even in a web situation"
            + " each thread (ie user) will have its own strand. Or have I misunderstood"
            + " how junit works? Related to reTransferDOtoCell as well";
        INTERMITTENT_UNIT_TEST_FAILURE.setDescription(desc);
        INTERMITTENT_UNIT_TEST_FAILURE.setFix("Required the use of a ReferenceLookupAttribute"
            + " so that from a promotion could get through to a PromotionType. Might need a"
            + " better error message to this effect for when the API is being used directly");
        INTERMITTENT_UNIT_TEST_FAILURE.setFixed(true);
        //
        UP_DOWN_KEY_IN_J_TABLES.setName("upDownKeyInJTables");
        desc = "Up/down keys, use of, will cause stack trace in JTables. Try using TestTable"
            + " with a table as the master";
        UP_DOWN_KEY_IN_J_TABLES.setDescription(desc);
        //
        TIMES_TOO_CLOSE.setName("Times too close");
        desc = "If change pUtils.visibleMode to true then this will kick in a timesTooClose"
            + " error when running Wombat, which presumably indicates a problem. Note that the unit"
            + " tests seem to run just as well if visible or not, just not much point in having them"
            + " visible!";
        TIMES_TOO_CLOSE.setDescription(desc);
        //
        MASTER_DETAIL_COPY_PASTE.setName("Master/Detail copy and paste");
        desc = "Should be able to work with any combination of field/table, but currently"
            + " does not.  Problem occurs when child is not set "
            + " to a table (childNonTable = true). Most of the tests in"
            + " TestTable will fail. testCopyPasteMasterDetail has a different message to the"
            + " others. It's problem looks like it is on the wrong node.";
        MASTER_DETAIL_COPY_PASTE.setDescription(desc);
        //
        R_CLICK_RESTORE.setName("Right click restore");
        desc = "Think about getting rid of this functionality or doing it for table"
            + " as well. Will JFormattedTextField work work for all types of controls? This is"
            + " a real field thing, and s/not be in cml's domain. Problem occurs when masterNonTable = false"
            + " causes this problem. 6 of the tests in"
            + " TestTable will fail. testCopyPasteMasterDetail has a different message to the"
            + " others. See masterDetailCopyPaste for this one as its a seperate bug.";
        R_CLICK_RESTORE.setDescription(desc);
        //
        INITIAL_ROW_WRONG.setName("Initial MoveTracker row badly set");
        desc = "We are making a hard coded assumption about what the initial row"
            + " stored in previousAdapterReleased will be. Using 0 will work in the majority"
            + " of cases where a query will be done and at least 1 row will be returned."
            + " 12/09/04 Stopped incrementing for insert as that was making it too high. Allowed"
            + " entry into frozen blocks at this time so INSERT when have no rows will actually"
            + " make it to this point. Well TestTable.testCopyPasteMasterDetailNulls() was" +
            " failing so put back to -1 and started incrementing again. Would be easy enough" +
            " to give a reason to getEnteredAdapter() so that we can set it differently depending" +
            " on action";
        INITIAL_ROW_WRONG.setDescription(desc);
        //
        FILL_DATA_FOR_DISCONNECTED_NODES.setName("Fill Data For Disconnected Nodes");
        desc = "Will need to make this optional for cases like the unit test example. Do"
            + " not want to get rid of the error message as by default it is usually telling"
            + " the user he has forgotten. Could we get really complicated and not have an"
            + " option on the node for not doing the error but work it out with some sort of rule."
            + " Sounds stupid. If not dealing with promotion then why have it set up in the first"
            + " place?";
        FILL_DATA_FOR_DISCONNECTED_NODES.setDescription(desc);
        //
        INTO_BAD_STATE_IF_DONT_POST.setName("Into Bad State If Dont POST");
        desc = "See page/s in pad";
        INTO_BAD_STATE_IF_DONT_POST.setDescription(desc);
        INTO_BAD_STATE_IF_DONT_POST.setCauseProblem(true); // can make it cause the problem as have fixed it
        //
        ANY_STATE_CHANGE_POSSIBLE.setName("Petstore Tables");
        desc = "If consumeNodesIntoRT is causing the same state to be created again,"
            + " then checking to see that not putting back in same state does not make sense."
            + " Perhaps there should be a special state change reason, then we should get"
            + " rid of all the state change exceptions have been forced to put in, and start"
            + " the testing process over again.";
        ANY_STATE_CHANGE_POSSIBLE.setDescription(desc);
        ANY_STATE_CHANGE_POSSIBLE.setFix("Would be to give up erroring");
        //
        PETSTORE_TABLES.setName("Petstore Tables");
        desc = "Setup the Petstore demo with two tables and immediately get"
            + " a painting stack trace. Perhaps problem goes to fact that two expected DT"
            + " panels did not compile and had to be deleted. First problem was simplistic"
            + " code generation - fixed by going into the actual panel. Then to try and stop NPEs"
            + " made sure that the three enclosed panels were properly initialised and accessed - but "
            + " then still got NPEs";
        PETSTORE_TABLES.setDescription(desc);
        PETSTORE_TABLES.setFix("Simplistic code generation - go into the actual panel");
        //
        LOOKING_UP_WRONG_TYPE.setName("Looking up wrong type");
        desc = "In Petstore demo changed screen so Account has status. This cell validation"
            + " message appeared, but did not seem to do any harm";
        LOOKING_UP_WRONG_TYPE.setDescription(desc);
        //
        RE_TRANSFER_DO_TO_CELL.setName("Transfer DO to Cell again");
        desc = "In Petstore demo try to transfer Account again, then try to run."
            + " Also get this intermitently when doing the junit tests, specifically it"
            + " comes from TestClientsChildFocus.testChildFocus()";
        RE_TRANSFER_DO_TO_CELL.setDescription(desc);
        //
        NO_CONTROLS_IN_PANE.setName("No controls in pane - not a bug at all!");
        //
        INCREMENT_WHEN_ADD.setName("Increment When Add");
        desc = "At same time as add in background, should "
            + "increment the block's index. First discovered when coding "
            + "a RecordChangeListener in MLD";
        INCREMENT_WHEN_ADD.setDescription(desc);
        //
        CANT_SEE_ERROR.setName("Can't see error");
        desc = "ErrorThrower s/realise that are not on a control "
            + "and error should go to stderr";
        CANT_SEE_ERROR.setDescription(desc);
        //
        TABLE_BLANKING_POLICY.setName("Table blanking policy");
        desc = "Blanking (whether null allowed) for a table could be "
            + "based on the cell editor that is used. Then it could use exactly "
            + "the same code that fields use";
        TABLE_BLANKING_POLICY.setDescription(desc);
        String techConcerns = "If all we need is the type of the editor, then should "
            + "be no concerns about finding out editor when not currently editing. " +
            "The way have solved the problem for now is to know the type of the object that " +
            "the table is giving back to Strandz - thus getBlankingPolicy() has a new " +
            "param, the clazz. The member clazz of the TableItem is always set to a " +
            "String.class I think. Thus TODO is to investigate whether this member is needed " +
            "at all";
        TABLE_BLANKING_POLICY.setOngoingConcerns(techConcerns);
        //
        GO_TO_FROZEN.setName("Going to a frozen block");
        desc = "Should user even be allowed to go to a FROZEN block";

        String designConcerns = "Users will want to fill things in in their order, "
            + "and may not understand any restrictions";
        GO_TO_FROZEN.setDescription(desc);
        //
        DELETE_MASTER_WHEN_COLLECTION.setName("Delete Master when Collection");
        desc = "When master has a collection to detail then probably no refering attributes "
            + "so this will be ok, but test anyway!";
        DELETE_MASTER_WHEN_COLLECTION.setDescription(desc);
        //
    }

    public SdzNote(boolean visible)
    {
        super(visible);
    }

    public SdzNote()
    {
    }

    public SdzNote(String name)
    {
        super(name);
    }

    public SdzNote(String name, boolean visible)
    {
        super(name, visible);
    }
}
