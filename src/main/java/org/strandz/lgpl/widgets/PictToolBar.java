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
package org.strandz.lgpl.widgets;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.note.SdzNote;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import java.awt.ContainerOrderFocusTraversalPolicy;
import java.awt.Insets;
import java.awt.event.FocusEvent;

/**
 * Compared to Manipulator, this Toolbar is more conservative, and
 * more in line with what users would use in a Swing environment.
 */
public class PictToolBar extends JToolBar
{
    //private String[] NO_ICON_NAMES = { "images/Enter.gif", "images/Query.gif", "images/Search.gif"};
    private static int times = 0;
    
    private static int constructedTimes;
    private int id;

    public PictToolBar(String name)
    {
        setFloatable(false); // if true can bring on the left margin problem
        getAccessibleContext().setAccessibleName(name);
        setName(name);
        putClientProperty("JToolBar.isRollover", Boolean.FALSE); //another name for rollover text is 'tool tip'
        // This done so that the focus cycles around the toolbar buttons, and
        // does not drift onto 'attributed controls'. When drifting onto
        // 'attributed controls' goNode() is called, which will cause confusion.
        setFocusTraversalPolicy(new ContainerOrderFocusTraversalPolicy());
        // Needed this for it to work at all. This takes focus up to the toolbar
        // itself rather than cycling them round. Oh well! Could probably fix by
        // actually writing one. See downloads/focusTraverable/
        setFocusCycleRoot(true);
        constructedTimes++;
        id = constructedTimes;
        Err.pr( SdzNote.WRONG_PHYSICAL, "Created a " + name + " ID: " + id);
        if(id == 0)
        {
            Err.stack();
        }
    }

    /* Better to make the user use an Action
    public JButton addTool(String name)
    {
        JButton b = new JButton();
        return addButton(b, name, null, null);
    }
    */
    
    public void addTool(JButton b)
    {
        addButton(b, null, null, null);
        Err.pr(SdzNote.ACTIONS_REQUIRED_ON_TOOL_BAR, "Comment out this method");
    }

    public JButton addTool(Action action)
    {
        String name = (String) action.getValue(Action.NAME);
        String shortDescription = (String) action.getValue(Action.SHORT_DESCRIPTION);
        String longDescription = (String) action.getValue(Action.LONG_DESCRIPTION);
        String toolTipText = null; 
        if(Utils.isBlank( longDescription))
        {
            if(Utils.isBlank( shortDescription))
            {
                //if stays null then name will be used     
                Err.pr(SdzNote.ACTIONS_REQUIRED_ON_TOOL_BAR, "Name always being used!");
            }
            else
            {
                toolTipText = shortDescription;
            }
        }
        else
        {
            toolTipText = longDescription;
        }
        Icon icon = (Icon) action.getValue(Action.SMALL_ICON);
        JButton b = new JButton();
        b.addActionListener(action);
        // Done by PictToolBarController (subclass), so will work dynamically
        // as well. (Need here as well b/c adding property change listener comes
        // after call to addTool).
        b.setEnabled(action.isEnabled());
        return addButton(b, name, toolTipText, icon);
    }

    private JButton addButton(JButton button, String name, String toolTipText, Icon icon)
    {
        if(button == null)
        {
            Err.error("Expect to have a button param");
        }
        if(name == null)
        {
            name = button.getText();
            if(name == null)
            {
                Err.error("Expect to have a name param");
            }
        }
        if(toolTipText == null)
        {
            toolTipText = name;
        }
        if(icon == null)
        {
            String fileName = "images/" + name + ".gif";
            int whenNotFind = PortableImageIcon.STDERR_MSG;
            if(name.equals("Enter") || name.equals("Query") || name.equals("Search"))
            {
                whenNotFind = PortableImageIcon.NOTHING;
            }
            icon = PortableImageIcon.createImageIcon(fileName, name, whenNotFind);
            /*
            if(new File( fileName).exists())
            {
              icon = pUtils.createImageIcon( this, fileName, null, name);
            }
            else
            {
              if(name.equals( "Enter") || name.equals( "Query") || name.equals( "Search") || name.equals( "Debug"))
              {
                //haven't got a picture for these yet
              }
              else
              {
                Err.error( "Could not find " + fileName + " in location " + System.getProperty( "user.dir"));
              }
            }
            */
        }
        button.setText(name);
        button.getAccessibleContext().setAccessibleName(name);
        button.setToolTipText(toolTipText);
        /*
         * Drawing the image is happening, but the image seems to be spoilt.
         * Images were spolit!!
        if(name.equals( "Delete"))
        {
          button.setDebugGraphicsOptions( DebugGraphics.LOG_OPTION);
        }
        */
        if(icon != null)
        {
            button.setIcon(icon);
            //tmp try not do
            button.setMargin(new Insets(7, 7, 7, 7));
        }
        else
        {
            // Only way could get a non-iconed ability to be same height as rest
            button.setMargin(new Insets(7, 7, 7, 7));
            // Err.pr( "For " + name + " setting margin");
        }
        /*
        * No effect
        Dimension dim = button.getPreferredSize();
        dim.height = 30;
        button.setPreferredSize( dim);
        */
        add(button);
        validate();
        repaint(); // important for case where many Nodes are
        // visible on same screen
        return button;
    }

    public void removeTool(JButton b)
    {
        remove(b);
        repaint(); // don't need to validate when remove
    }

    public static class DebugButton extends JButton
    {
        private int toolBarId = Utils.UNSET_INT;
        
        public DebugButton( int toolBarId)
        {
            super();
            this.toolBarId = toolBarId;
        }
        
        public DebugButton(String name)
        {
            super(name);
        }

        public DebugButton(String name, int toolBarId)
        {
            super(name);
            this.toolBarId = toolBarId;
        }

        public DebugButton(ImageIcon ii, int toolBarId)
        {
            super(ii);
            this.toolBarId = toolBarId;
        }

        protected void processFocusEvent(FocusEvent e)
        {
            // Err.pr( "PROCESSING: " + e);
            super.processFocusEvent(e);
        }

        public void setEnabled(boolean b)
        {
            if(getText().equals("Next"))
            {
                times++;
                Err.pr( SdzNote.WRONG_PHYSICAL, "[Next] DebugButton.setEnabled() - " + b + " times " + times + " in tool ID: " + toolBarId);
                if(times == 0)
                {
                    Err.debug();
                }
            }
            super.setEnabled(b);
        }
    }
    
    public int getId()
    {
        return id;
    }
}
