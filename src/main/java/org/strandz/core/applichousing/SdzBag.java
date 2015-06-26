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

import org.strandz.core.domain.OtherSignatures;
import org.strandz.core.interf.ActualNodeControllerI;
import org.strandz.core.interf.StandardPanes;
import org.strandz.core.interf.Node;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ObjectFoundryUtils;
import org.strandz.lgpl.util.ReasonNotEquals;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.view.util.StrandArea;

public class SdzBag extends AbstractSdzBag
    implements StandardPanes
{
    private SBIStatusBarHelper barHelperSBI = new SBIStatusBarHelper();
    public static final String DEFAULT_NAME = "New_sdzBag";
    private static int times;

    public SdzBag()
    {
        super();
        setName( "SdzBag"); //for when setDefaults() not called
        MessageDlg.setDlgParent(this);
        barHelperSBI.init(getHelper().strand, this);
        /*
        times++;
        Err.pr( "SdzBag() times " + times);
        if(times == 0)
        {
        Err.stack();
        }
        */
    }
    
    public SdzBag( StrandArea strandArea)
    {
        super( strandArea);
        setName( "SdzBag"); //for when setDefaults() not called
        MessageDlg.setDlgParent(this);
        barHelperSBI.init(getHelper().strand, this);
    }

    public void setDefaults()
    {
        String s = OtherSignatures.getDefaultPhysicalControllerType().getName();
        setPhysicalController((ActualNodeControllerI) ObjectFoundryUtils.factory(s));
        s = OtherSignatures.getDefaultStatusBarType().getName();
        setStatusBar((NodeStatusBarI) ObjectFoundryUtils.factory(s));
        setName(DEFAULT_NAME);
        Err.pr( SdzNote.GENERIC, "setDefaults() being called for ID:" + getStrand().id);
    }

    public String getDefaultName()
    {
        return DEFAULT_NAME;
    }

    public void setStatusAreaPanel(StrandArea belowMenuPanel)
    {
        barHelperSBI.setStrandArea(belowMenuPanel);
        setStrandArea( belowMenuPanel);
    }

    public SBIStatusBarHelper getBarHelper()
    {
        return barHelperSBI;
    }

    public boolean equals(Object o)
    {
        boolean result = super.equals( o);         
        if(result)        
        {
            SdzBag test = (SdzBag) o;
            // if(helper.getPhysicalController().equals( test.helper.getPhysicalController()))
            if(Utils.bothNull(getHelper().getPhysicalController(), test.getHelper().getPhysicalController()) ||
                Utils.equals(getHelper().getPhysicalController().getClass(),
                    test.getHelper().getPhysicalController().getClass()))
            {
                // if(barHelperSBI.getStatusBar().equals( ))
                if(Utils.bothNull(barHelperSBI.getStatusBar(), test.barHelperSBI.getStatusBar()) ||
                    Utils.equals(barHelperSBI.getStatusBar().getClass(),
                        test.barHelperSBI.getStatusBar().getClass()))
                {
                    result = true;
                }
                else
                {
                    ReasonNotEquals.addReason("statusBar");
                }
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = super.hashCode();
        result = 37 * result
            + (getHelper().getPhysicalController() == null
            ? 0
            : getHelper().getPhysicalController().hashCode());
        result = 37 * result
            + (barHelperSBI.getStatusBar() == null
            ? 0
            : barHelperSBI.getStatusBar().hashCode());
        // Err.pr( "sdzBag " + this + "returning hashCode of " + result);
        return result;
    }
    
    public void setNodes(Node[] nodes)
    {
        super.setNodes(nodes);
        for(int i = 0; i < nodes.length; i++)
        {
            barHelperSBI.attachStatusBarToNode(nodes[i]);
        }
        /*
        if(nodes.length > 0)
        {
        if(bar != null) bar.setNode( nodes[0]);
        }
        */
    }
    
    public void setNode(int index, Node node)
    {
        super.setNode(index, node);
        barHelperSBI.attachStatusBarToNode(node);
        // Err.pr( "Strand hope have added node to is " + getStrand().hashCode());
        /*
        if(index == 0)
        {
        bar.setNode( node);
        }
        */
    }

    public void setPhysicalController(ActualNodeControllerI actual)
    {
        getHelper().setPhysicalController(actual);
    }

    public ActualNodeControllerI getPhysicalController()
    {
        return getHelper().getPhysicalController();
    }

    public void setStatusBar(NodeStatusBarI bar)
    {
        /*
        if(bar != null)
        {
        Err.pr( "$$ setStatusBar: " + bar.getClass().getName() + " on " + this.hashCode());
        }
        else
        {
        Err.pr( "$$ setStatusBar: NULL on " + this.hashCode());
        }
        */
        Node[] nodes = getNodes();
        barHelperSBI.setStatusBar(bar);
        for(int i = 0; i < nodes.length; i++)
        {
            barHelperSBI.attachStatusBarToNode(nodes[i]);
        }
    }

    public NodeStatusBarI getStatusBar()
    {
        NodeStatusBarI bar = barHelperSBI.getStatusBar();
        /*
        if(bar != null)
        {
        Err.pr( "$$ getStatusBar: " + bar.getClass().getName() + " on " + this.hashCode());
        }
        else
        {
        Err.pr( "$$ getStatusBar: NULL on " + this.hashCode());
        }
        */
        return bar;
    }

    public ActualNodeControllerI createDefaultPhysicalController()
    {
        ActualNodeControllerI result = getHelper().nodeController.createDefaultControllerForType();
        //getHelper().fitControllerArea();
        return result;
    }
    
    void setCurrentNode( Node node)
    {
        
    }

    /*
    public ToolBarPane getControlPane()
    {
    return toolBarPane;
    }
    public ToolBarPane getToolBarPane()
    {
    return toolBarPane;
    }
    */
}
