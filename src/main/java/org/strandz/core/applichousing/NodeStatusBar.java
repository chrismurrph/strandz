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
package org.strandz.core.applichousing;

import org.strandz.core.domain.constants.CapabilityEnum;
import org.strandz.core.domain.constants.StateEnum;
import org.strandz.core.domain.event.OperationEvent;
import org.strandz.core.domain.event.StateChangeEvent;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.Strand;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.MsgSubstituteUtils;
import org.strandz.lgpl.util.Utils;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

/**
 * Data aware NodeStatusBar component.  If Node property is set, the NodeStatusBar
 * displays error and status messages related to operations on the Node.
 */
public class NodeStatusBar extends JPanel implements NodeStatusBarI // NavigationTrigger,
// StateChangeListener,
{
    public Node node = null;
    protected JLabel label;
    private TextHolder holder = new TextHolder();
    private boolean showMode = true;
    private boolean showRelativeRecord = true;

    //private static int times = 0;
    private static int timesSetModeText = 0;
    private static int timesSetNode = 0;
    private static int timesUpdateRowCount = 0;
    private static int timesStateChangePerformed = 0;
    private static int timesNavigated = 0;
    private static int timesDebugLabel = 0;
    private static int constructedTimes;
    public int id;

    class TextHolder
    {
        private String modeText;

        String getModeText()
        {
            return modeText;
        }

        void setModeText(String s)
        {
            timesSetModeText++;
            Err.pr(SdzNote.NO_MOVE_STATE, "&&&(YET) setting modeText from <" + modeText + "> to <" + s + "> " + timesSetModeText);
            if(timesSetModeText == 0)
            {
                Err.stack();
            }
            modeText = s;
        }
    }

    public NodeStatusBar()
    {
        super();
        setName("org.strandz.core.applichousing.NodeStatusBar");
        if(SdzNote.NO_HOUSING_HELPERS.isVisible())
        {
            //Err.stack();
        }
        constructedTimes++;
        id = constructedTimes;
        /**/
        Err.pr( SdzNote.OVER_FREEZING, "#-#-# CONSTRUCTING NEW NodeStatusBar ID: " + id);
        if(id == 0)
        {
            Err.stack();
        }
        /**/
        setLayout(new BorderLayout());
        label = new JLabel();
        add(label, BorderLayout.WEST);
        label.setForeground(Color.green.darker().darker());
        Err.pr(SdzNote.TASK_BAR_APPEARANCE, "Border to debug");
        // setBorder( BorderFactory.createEtchedBorder());
    }

    public boolean equals(Object o)
    {
        // pUtils.chkType( o, this.getClass());

        boolean result = true;
         /**/
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof NodeStatusBar))
        {
            // Nulls come through here
            // Err.stack( "o actually an instanceof " + o.getClass().getName());
            result = false;
        }
        else
        {
            result = false;

            NodeStatusBar test = (NodeStatusBar) o;
            if(Utils.equals(holder.getModeText(), test.holder.getModeText()))
            {
                result = true;
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = 37 * result
            + (holder.getModeText() == null ? 0 : holder.getModeText().hashCode());
        return result;
    }

    /**
     * The node property specifies a Node
     * object to display data from in the NodeStatusBar.
     */
    public Node getNode()
    {
        return node;
    }

    public void setShowMode(boolean b)
    {
        showMode = b;
    }

    public boolean getShowMode()
    {
        return showMode;
    }

    public void setShowRelativeRecord(boolean b)
    {
        showRelativeRecord = b;
    }

    public boolean getShowRelativeRecord()
    {
        return showRelativeRecord;
    }

    public void setNode(Node node)
    {
        if(node == null)
        {
            performOpen(node);
        }
        else if(!node.isAllowed(CapabilityEnum.IGNORED_CHILD))
        {
            timesSetNode++;
            Err.pr( SdzNote.STATUS_BAR, "Setting node to " + node + " times " + timesSetNode);
            if(timesSetNode == 0)
            {
                Err.stack();
            }
            performOpen(node);
        }
        else
        {/*
       * If node is to be ignored, then we want NSB to keep
       * the same node as it already has, so we do nothing
       */// Err.pr("BARBAR NOT Setting node to " + node);
        }
    }

    private final void removeListeners()
    {
        if(node != null)
        {
            node.removeNavigationTrigger(this);
            node.getStrand().removeStateChangeTrigger(this);
        }
    }

    private final void addListeners()
    {
        if(node != null)
        {
            try
            {
                node.addNavigationTrigger(this);
                node.getStrand().addStateChangeTrigger(this);
            }
            catch(Exception ex)
            {
                Err.error("nasty exception");
            }
        }
    }

    String debugLabel(String txt)
    {
        if(SdzNote.STATUS_BAR.isVisible())
        {
            Err.pr("$-$ DEBUG LABEL: " + node.toString() + " on ID: " + id + " with parent " + getParent());
            timesDebugLabel++;
            Err.pr(
                    "^^^   label from <" + label.getText() + "> to <" + txt + "> times " + timesDebugLabel);
            if(timesDebugLabel == 0)
            {
                Err.stack();
            }
        }
        return txt;
    }

    protected void updateValue()
    {
        String mode = holder.getModeText();
        if(mode.equals(""))
        {
            label.setText(debugLabel(""));
        }
        else
        {
            int maxCursorPosition = node.getRowCount();
            if(maxCursorPosition > 0)
            {
                Integer rowCount = new Integer(maxCursorPosition);
                timesUpdateRowCount++;
                Err.pr( SdzNote.STATUS_BAR, "rowCount got was " + rowCount + ", times " + timesUpdateRowCount);
                if(timesUpdateRowCount == 0)
                {
                    Err.stackOff();
                }
                Integer row = new Integer(node.getRow() + 1);
                String recordMode = mode + ": ";
                if(!showMode)
                {
                    recordMode = "";
                    mode = "";
                }
                Integer args[] = {row, rowCount};
                String txt;
                if(showRelativeRecord && !mode.equals("ENTER QUERY"))
                {
                    txt = debugLabel(recordMode + MsgSubstituteUtils.formMsg("Record $ of $", args));
                }
                else
                {
                    txt = debugLabel(mode);
                }
                label.setText(txt);
            }
            else
            {
                String displayMode = new String(mode);
                if(!showMode || displayMode.equals("FROZEN"))
                {
                    displayMode = "";
                }
                label.setText(debugLabel(displayMode));
            }
        }
    }

    public Dimension getPreferredSize()
    {
        Dimension d = super.getPreferredSize();
        if(d.width < 200)
        {
            d.width = 200;
        }
        return d;
    }

    /**
     * Implementation of StateChangeListener.
     */
    public void stateChangePerformed(StateChangeEvent e)
    {
        timesStateChangePerformed++;
        Err.pr(SdzNote.NO_MOVE_STATE.isVisible() || SdzNote.STATUS_BAR.isVisible(), 
               "~~ StateChangeEvent from " + e.getSource() +
            " times: " + timesStateChangePerformed + ">> Ours: " + node + " " + e.getCurrentState());
        if(timesStateChangePerformed == 0)
        {
            Err.stack();
        }
        /*
        * Get status change events for all nodes, so only act on those that
        * apply to this NodeStatusBar. (Status is not a per-node concept)
        */
        if(e.getSource() == node)
        {
            holder.setModeText(e.getCurrentState().toString());
            updateValue();
        }
    }

    private void performOpen(Node node)
    {
        removeListeners(); // done on current node
        this.node = node;
        Err.pr( SdzNote.STATUS_BAR, "((( doing NodeStatusBar open on " + node);
        if(node.toString().equals( "applic.fishbowl.data.objects.Job"))
        {
            Err.stackOff();
        }
        if(node == null)
        {// Necessary to put NodeStatusBar back to its constructed state
        }
        else
        {
            // Get the modeText to represent state of new mode
            if(node.getStrand().getMode() != Strand.NODECOLLECTING)
            {
                StateEnum state = node.getState();
                holder.setModeText(state.toString());
            }
            else
            {
                holder.setModeText("");
            }
        }
        if(node != null)
        {
            updateValue();
            addListeners();
        }
    }

    /**
     * Implementation of NavigationListener.
     */
    public void navigated(OperationEvent event)
    {
        timesNavigated++;
        Err.pr( SdzNote.STATUS_BAR, "^^^   In navigated times " + timesNavigated);
        updateValue();
    }

    public String toString()
    {
        String result;
        result = node + ", <" + label.getText() + "> " + ", modeText: <"
            + holder.getModeText() + ">";
        // + ", show: " + showMode;
        return result;
    }
}
