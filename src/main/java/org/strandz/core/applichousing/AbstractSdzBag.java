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

import org.strandz.core.domain.WidgetClassifier;
import org.strandz.core.domain.constants.OperationEnum;
import org.strandz.core.domain.event.StateChangeTrigger;
import org.strandz.core.interf.CloseTransactionTrigger;
import org.strandz.core.interf.CopyPasteBuffer;
import org.strandz.core.interf.Node;
import org.strandz.core.interf.PanelUpdateInfo;
import org.strandz.core.interf.SdzBagI;
import org.strandz.core.interf.SdzBagIHelper;
import org.strandz.core.interf.StandardPanes;
import org.strandz.core.interf.Strand;
import org.strandz.lgpl.util.ComponentUtils;
import org.strandz.lgpl.util.Publisher;
import org.strandz.lgpl.util.ReasonNotEquals;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.widgets.IconEnum;
import org.strandz.view.util.AbstractStrandArea;
import org.strandz.view.util.StrandArea;
import org.strandz.view.util.UnadornedStrandArea;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.List;

abstract public class AbstractSdzBag extends JPanel implements StandardPanes, SdzBagI
{
    private AbstractStrandArea strandArea;
    private boolean partOfApplication = true;
    private WidgetClassifier widgetClassifier = new WidgetClassifier();
    private SdzBagIHelper helper;
    
    public AbstractSdzBag()
    {
        helper = new SdzBagIHelper();
        setPartOfApplication(false);
        strandArea = new UnadornedStrandArea();
        strandArea.init();        
        helper.init(null, this);
        init();
    }
    
    public AbstractSdzBag( StrandArea strandArea)
    {
        helper = new SdzBagIHelper();
        setPartOfApplication(false);
        setStrandArea( strandArea);
        helper.init(null, this);
        init();
    }
    
    public int hasNonPaneNumberOfComponents()
    {
        return 1;
    }
    
    public String toString()
    {
        String result = helper.toString();
        // result += "NodeStatusBar: " + bar.hashCode() + " on " + this.hashCode() + "\n";
        // result += "has node: " + bar.getNode();
        return result;
    }
    
    public IconEnum getIconEnum()
    {
        return helper.getIconEnum();
    }
    
    private void init()
    {
        removeAll();
        /*
        Where did SimpleVisibleStrand do this? setVisible, which we can't call.
        (As is intended to be called when running remotely from an Application)
        */
        setLayout(new BorderLayout());
        // add( toolBarPane, BorderLayout.NORTH);
        // helper.fitControlArea();
        add(strandArea, BorderLayout.CENTER);
    }
    
    /**
     * Always called after has just been created. Thus copy/paste rather than
     * blank out and copy/paste is sufficient.
     */
    public void set(SdzBagI sbI)
    {
        init();
        setPanes(sbI.getPanes());
        setName(sbI.getName());
        setSource(sbI.getSource());
        setNodes(sbI.getNodes());
        /*
        for(Iterator it = sbI.getTransactionTriggers().iterator(); it.hasNext();)
        {
          CloseTransactionTrigger l = (CloseTransactionTrigger)it.next();
          addTransactionTrigger( l);
        }
        */
        sbI.getTransactionTriggers().publish
            (
                new Publisher.Distributor()
                {
                    public void deliverTo(Object subscriber)
                    {
                        CloseTransactionTrigger ctt = (CloseTransactionTrigger) subscriber;
                        addTransactionTrigger(ctt);
                    }
                }
            );
        // Again ordinal might come in handy for more complex ControllerInterfaces
        // Err.pr( "BAR getting node set to: " + nodes.get( 0));
        // bar.setNode( (Node)sbI.getNodes()[0]);
    }
    
    public void setPartOfApplication(boolean b)
    {
        if(b != partOfApplication)
        {
            /*
            if(b)
            {
                strandArea = null;
            }
            else
            {
                strandArea = new UnadornedStrandArea();
                strandArea.init();
            }
            */
            partOfApplication = b;
        }
    }
    
    public SdzBagIHelper getHelper()
    {
        return helper;
    }

    public boolean validateBean()
    {
        return helper.validateBean();
    }

    public boolean validateBean(boolean childrenToo)
    {
        return helper.validateBean(childrenToo);
    }

    public String getName()
    {
        return helper.name;
    }

    public void setName(String name)
    {
        helper.setName(name);
    }
    
    public boolean isPartOfApplication()
    {
        return partOfApplication;
    }
    
    public AbstractStrandArea getStrandArea()
    {
        return strandArea;
    }

    public void setStrandArea(AbstractStrandArea strandArea)
    {
        this.strandArea = strandArea;
    }
    
    public void setCurrentPane(int index)
    {
        helper.setCurrentPane(index);
    }

    public int getCurrentPane()
    {
        return helper.getCurrentPane();
    }
    
    /**
     * These two only used at design time by customizer. Convenience
     * methods so that the business object does the conversions.
     */
    public void setCurrentPaneStr(String s)
    {
        /*
        Err.pr( "*");
        Err.pr( "* setCurrentPaneStr() with <" + s + ">");
        Err.pr( "*");
        Err.pr( "*");
        */
        helper.setCurrentPaneStr(s);
    }

    public String getCurrentPaneStr()
    {
        return helper.getCurrentPaneStr();
    }

    public Strand getStrand()
    {
        return helper.strand;
    }
    
    public void setNodes(Node[] nodes)
    {
        getHelper().setNodes(nodes);
    }

    public void setNode(int index, Node node)
    {
        getHelper().setNode(index, node);
    }

    public Node[] getNodes()
    {
        return helper.getNodes();
    }
    
    public Node getNode(int index)
    {
        return helper.getNode(index);
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

    public boolean removeNode(Node node)
    {
        boolean ok = helper.removeNode(node);
        return ok;
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

    public void execute(OperationEnum op)
    {
        helper.strand.execute(op);
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
    
    public String getSource()
    {
        return helper.getSource();
    }

    public void setSource(String source)
    {
        helper.setSource(source);
    }    
    
    public String toShow()
    {
        return helper.toShow();
    }

    public PanelUpdateInfo getPanelUpdateInfo()
    {
        return helper.getPanelUpdateInfo();
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
    
    public boolean equals(Object o)
    {
        ReasonNotEquals.addClassVisiting( getClass().getName());

        // When add to JTabbedPane, is compared with other types
        // pUtils.chkType( o, this.getClass());

        boolean result = true;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof UnadornedSdzBag))
        {
            result = false;
        }
        else
        {
            result = false;

            AbstractSdzBag test = (AbstractSdzBag) o;
            if(helper.name.equals(test.helper.name))
            {
                if(Utils.equalsArrays(helper.getNodes(), test.helper.getNodes()))
                {
                    if(ComponentUtils.equalsByProperties(helper.getPanes(),
                        test.helper.getPanes(), widgetClassifier))
                    {
                        result = true;
                    }
                    else
                    {
                        ReasonNotEquals.addReason("panes");
                    }
                }
                else
                {
                    ReasonNotEquals.addReason("nodes");
                }
            }
            else
            {
                ReasonNotEquals.addReason("name");
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = 37 * result + (helper.name == null ? 0 : helper.name.hashCode());
        result = 37 * result + (Utils.arrayHashCode(helper.getNodes()));
        result = 37 * result + (ComponentUtils.hashCodeComponentArrays(helper.getPanes()));
        return result;
    }
}

