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
package org.strandz.core.interf;

import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.event.StateChangeTrigger;
import org.strandz.lgpl.util.Publisher;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.widgets.IconEnum;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.List;

/*
 * TODO
 * Currently will NOT error if try to give more than one node,
 * but should! (Implementing ControllerInterface will always give
 * the appearance of allowing > 1 node)
 */

/**
 * This class is the simplest type of SdzBagI. It is generally used for
 * a one-panel, non-table fill-in form that does not need to have a
 * <code>ActualNodeControllerI</code> (read Toolbar) or a
 * <code>NodeStatusBarI</code>
 *
 * @author Chris Murphy
 */
public class OneRowSdzBag extends JPanel implements SdzBagI
{
    private SdzBagIHelper helper = new SdzBagIHelper();
    public static final String DEFAULT_NAME = "Default OneRowSdzBag";
    private static int times = 0;
    
    public IconEnum getIconEnum()
    {
        return helper.getIconEnum();
    }

    public OneRowSdzBag()
    {
        helper.init(this, null);
        setLayout(new BorderLayout());
        /*
        times++;
        Err.pr( "OneRowSdzBag() times " + times);
        if(times == 3)
        {
        Err.stack();
        }
        */
    }

    public void setDefaults()
    {
        setName(DEFAULT_NAME);
    }

    public int hasNonPaneNumberOfComponents()
    {
        return 1;
    }

    public boolean validateBean()
    {
        return helper.validateBean();
    }

    public boolean validateBean(boolean childrenToo)
    {
        return helper.validateBean(childrenToo);
    }

    public boolean equals(Object o)
    {
        // When add to JTabbedPane, is compared with other types
        // (SdzBag was)
        // pUtils.chkType( o, this.getClass());

        boolean result = true;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof OneRowSdzBag))
        {
            result = false;
        }
        else
        {
            result = false;

            OneRowSdzBag test = (OneRowSdzBag) o;
            if(helper.name.equals(test.helper.name))
            {
                if(Utils.equalsArrays(helper.getNodes(), test.helper.getNodes()))
                {
                    // if(pUtils.equalsArrays( helper.getPanes(), test.helper.getPanes(), JComponent[].class))
                    // {
                    result = true;
                    // }
                }
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = 37 * result + (helper.name == null ? 0 : helper.name.hashCode());
        result = 37 * result + (Utils.arrayHashCode(helper.getNodes()));
        // result = 37*result + helper.getPanes().hashCode();
        return result;
    }

    public void set(SdzBagI sbI)
    {
        setPanes(sbI.getPanes());
        setName(sbI.getName());
        setSource(sbI.getSource());
        setNodes(sbI.getNodes());
    }

    public String getName()
    {
        return helper.name;
    }

    public String getDefaultName()
    {
        return DEFAULT_NAME;
    }

    public void setName(String name)
    {
        helper.setName(name);
    }

    public void setCurrentPane(int index)
    {
        helper.setCurrentPane(index);
    }

    public int getCurrentPane()
    {
        return helper.getCurrentPane();
    }

    public Strand getStrand()
    {
        return helper.strand;
    }

    public Node[] getNodes()
    {
        return helper.getNodes();
    }

    public void setNodes(Node[] nodes)
    {
        helper.setNodes(nodes);
    }

    public Node getNode(int index)
    {
        return helper.getNode(index);
    }

    public void setNode(int index, Node node)
    {
        helper.setNode(index, node);
    }

    public boolean removeNode(Node node)
    {
        return helper.removeNode(node);
    }

    public JComponent[] getPanes()
    {
        return helper.getPanes();
    }

    public void setPanes(JComponent[] panes)
    {
        helper.setPanes(panes);
    }

    public JComponent getPane(int index)
    {
        return helper.getPane(index);
    }

    public void setPane(int index, JComponent pane)
    {
        helper.setPane(index, pane);
    }

    public PanelUpdateInfo setPanesReturnInfo(JComponent newPanes[])
    {
        PanelUpdateInfo result = helper.setPanesReturnInfo(newPanes, this);
        return result;
    }

    public boolean removePane(JComponent pane)
    {
        return helper.removePane(pane);
    }

    public int indexOfPane(JComponent pane)
    {
        return helper.indexOfPane(pane);
    }

    public void addTransactionTrigger(CloseTransactionTrigger listener)
    {
        helper.addTransactionTrigger(listener);
    }

    public void removeTransactionTrigger(CloseTransactionTrigger listener)
    {
        helper.removeTransactionTrigger(listener);
    }

    public Publisher getTransactionTriggers()
    {
        return helper.getTransactionTriggers();
    }

    public void addStateChangeListener(StateChangeTrigger listener)
    {
        helper.addStateChangeListener(listener);
    }

    public void removeStateChangeListener(StateChangeTrigger listener)
    {
        helper.removeStateChangeListener(listener);
    }

    public boolean isPartOfApplication()
    {
        return false;
    }

    public void execute(OperationEnum op)
    {
        helper.strand.execute(op);
    }

    public void setCursor(int row)
    {
        helper.strand.SET_ROW(row);
    }

    public CopyPasteBuffer copyItemValues()
    {
        helper.copyItemValues();
        return helper.getCopyPasteBuffer();
    }

    public CopyPasteBuffer copyItemValues(Node node)
    {
        helper.copyItemValues(node);
        return helper.getCopyPasteBuffer();
    }

    public void pasteItemValues()
    {
        helper.pasteItemValues();
    }

    /*
     * Doing the copy in the first place gives you the buffer
    public CopyPasteBuffer getCopyPasteBuffer()
    {
      return helper.getCopyPasteBuffer();
    }
    */

    public String toString()
    {
        String result = helper.toString();
        return result;
    }

    public boolean isAlreadyBeenCustomized()
    {
        return getStrand().isAlreadyBeenCustomized();
    }

    public void setAlreadyBeenCustomized(boolean alreadyBeenCustomized)
    {
        getStrand().setAlreadyBeenCustomized(alreadyBeenCustomized);
    }

    public List retrieveValidateBeanMsg()
    {
        return getStrand().retrieveValidateBeanMsg();
    }

    public String toShow()
    {
        return helper.toShow();
    }

    public PanelUpdateInfo getPanelUpdateInfo()
    {
        return helper.getPanelUpdateInfo();
    }

    public String getSource()
    {
        return helper.getSource();
    }

    public void setSource(String source)
    {
        helper.setSource(source);
    }
}
