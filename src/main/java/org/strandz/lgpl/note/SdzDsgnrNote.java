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


public class SdzDsgnrNote extends Note
{
    public static final Note GENERATING_OVER_TABLE = new SdzDsgnrNote( "Generating over table");
    public static final Note ERROR_REMOVING_ATTRIBUTE = new SdzDsgnrNote( "Error removing Attribute");
    public static final Note GONE_CONTROL = new SdzDsgnrNote( "Equivalently named control no longer exists in new panel");
    public static final Note CONTROLS_VIEW_NOT_GETTING_PANEL_NAME_CHANGE = new SdzDsgnrNote( "Controls View not getting Panel Name Change");
    public static final Note NOT_VALIDATING_ON_CONTEXT = new SdzDsgnrNote( "Not validating Prefixes or Panel Names On Context");
    public static final Note L_AND_F = new SdzDsgnrNote( "Look and Feel");
    public static final Note GENERIC = new SdzDsgnrNote( "generic");
    public static final Note ADD_S_HAVE_HAPPENED = new SdzDsgnrNote( "S/not get indexing problems after have added");
    public static final Note TO_STEM = new SdzDsgnrNote( "Why toStemAttribute", HIDE);
    public static final Note TO_TABLE = new SdzDsgnrNote( "Why toTableAttribute", HIDE);

    static
    {
        String desc = null;
        //
        desc = "Nav to last context and press insert";
        ADD_S_HAVE_HAPPENED.setDescription("addSHaveHappened");
        //
        desc = "While doing this validation, also a good idea to validate that"
            + "every panel (including childPanels) has a name";
        NOT_VALIDATING_ON_CONTEXT.setDescription(desc);
        //
        desc = "Pressing [Update Bean] before go to design tab fixes this problem."
            + " Thus we need to be able to do this programmatically, which is"
            + " by calling dynamicallyLoadSelectedPanel(), which (despite name) loads"
            + " all the panels, incorporates them into the BeanCI and completely re-displays"
            + " the controls (ControlsController.refresh()) and beans panels. dynamicallyLoadSelectedPanel()"
            + " is overkill as it should be called when an external .class file has changed since"
            + " MLD has started AND/OR the user needs to get these panels into the BeanCI. Also it "
            + " seems to load the panels twice (seperate bug). All we are wanting to do here is"
            + " for the controls tree to reflect the guiClasses. So all we want to do is call"
            + " ControlsController.refresh(), which is already happening as part of fireRecordChangeEvent. "
            + " The problem lies in the"
            + " fact that refresh() is reading the guiClass DATA, not the value of the attribute,"
            + " as if a POST has already been done. (Aside - if a POST is not being done, then how is recordChange"
            + " being done? A: POST being done). Problem lies in fact that fireRecordChangeEvent being"
            + " called before applyDifferences(), so swapped these calls. Unit tests worked, however"
            + " from running PetstoreTest, can see that when go to Design tab after having entered a"
            + " new Context, that the beans tree still shows the last Context's details. RUBBISH - that"
            + " happens no matter which way round the calls, so sep. bug, beansTreeContextUnaware";
        CONTROLS_VIEW_NOT_GETTING_PANEL_NAME_CHANGE.setDescription(desc);
        CONTROLS_VIEW_NOT_GETTING_PANEL_NAME_CHANGE.setFixed(true);
        CONTROLS_VIEW_NOT_GETTING_PANEL_NAME_CHANGE.setFix("The swap did the trick, so now in any change"
            + " trigger you can actually look at the data. If you wanted to stop a change before it"
            + " happened then you would use a validation trigger. Makes sense for changes to have already"
            + " happened in a change trigger.");
        //
        //
        desc = "We need to interact with the user here. User should be told that mapping"
            + " info will be lost either because an item has been removed from a panel which is"
            + " fine, or renamed. If renamed the user will have to do the mapping again. If let"
            + " this go through, as did with birthday, then in Designer it will still be in green,"
            + " as is still mapped to something. We need to get rid of this something so that it"
            + " does not come out in green, but RED: unmapped. Maybe mark them in purple.";
        GONE_CONTROL.setDescription(desc);
        GONE_CONTROL.setFix("Have decided that need to put a cross thru green and table (actually FieldAttributes"
            + " automatically go back to being StemAttributes, which is perfect really - tables"
            + " are ok with a cross thru them, as this is an indicator to the user that he must"
            + " get rid of the reference to the table THIS LAST RUBBISH - changing inside Designer"
            + " s/always convert between green and RED. Only outside Designer will give crosses,"
            + " so can get crossed fields if import code changes have made) "
            + " icons, and to do the deciding each time icon is rendered. Fix used at place of"
            + " message was only happening when changed a panel (still unclear on what changing"
            + " a panel means!). For fix now using see PanelCompatibleIconService. A set param"
            + " now being used, and same logging going on for whether have just read in the xml"
            + " file, or have just pressed [Update Bean]. There was some concern as to what would"
            + " happen in a many panels situation. The loop, when calling setPanel/s, would be"
            + " for all attributes and one panel. This is actually not a problem as adding in"
            + " first loop, then not adding in second, gives us the net result of an attribute"
            + " having a control. The logging for the user would need to be read in this light."
            + " The most important thing is that the attribute's icon would not have a cross"
            + " through it. FINALLY - no crossed icons s/now appear even when meddle externally"
            + " and then [Into Bean]");
        GONE_CONTROL.setFixed(false);
        //
        desc = "This happened all the time when did remove all below in MLD, "
            + "but not sure why. Had it been removed already? Stay away from removing "
            + "until get this fixed. Assoc with this is when have removed all below (when"
            + " ignoring error), and created a whole new controller, get an error saying"
            + " that the controller has no nodes! Were there somehow two controllers?";
        ERROR_REMOVING_ATTRIBUTE.setDescription(desc);
        ERROR_REMOVING_ATTRIBUTE.setFixed(true);
        ERROR_REMOVING_ATTRIBUTE.setFix("Did do some work on this, so must have fixed it. Only"
            + " problems still expect to get will be with expanding and collapsing nodes and"
            + " which node is selected after delete operations");
        //
        desc = "Will not be able a layout a table any version soon, "
            + "so this is not exactly a bug";
        GENERATING_OVER_TABLE.setDescription(desc);
        //
        desc = "Could not find WindowsLookAndFeel when looked. Use it. Later maybe buy one - JGoodies or what JDOGenie workbench uses.";
        L_AND_F.setDescription(desc);
        L_AND_F.setFixed(true);
    }
    
    public SdzDsgnrNote()
    {
        super();
    }

    public SdzDsgnrNote(String name)
    {
        super( name);
    }
    
    public SdzDsgnrNote(String name, boolean visible)
    {
        super(name, visible);
    }
}
