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

import org.strandz.core.domain.SdzBeanI;
import org.strandz.lgpl.util.Publisher;

import javax.swing.JComponent;

/**
 * An implementation of this interface is the packaging up of everything
 * that has been done at design time (DT). Thus you can use it to get at
 * the panes that will be displayed, as well as the strand, and all the
 * beans that it encloses. Thus when you read in a DT XML file, this is
 * the object that comes out.
 * <p/>
 * The BeanInfos for StrandControl etc will all be
 * exactly the same and will all implement this.
 * <p/>
 * Must be a Container as when run is set as a content pane. Note that
 * even although this is a container that has panes, you can't simply
 * display it. A SdzBagI needs to work in an Application Housing.
 * It is the application housing that determines whether these panes
 * are arranged across as tabs, or free floating internal frames , or
 * whatever.
 * <p/>
 * There are currently two implementations of SdzBagI, and in the future
 * this may reduce to one flexible class. Currently SdzBag is
 * mainly used where there is a strict DT/RT demarcation, and works well in
 * the Designer, while OneRowSdzBag is used for single screen,
 * single row panels, such as most of the customisers in SdzDsgnr.
 * <p/>
 * A SdzBagI is put together in one of two ways. The first way is by
 * writing the code that creates Nodes, Cells, Attributes and panels
 * and puts them all together. The second way is by reading in from an
 * XML file that was XMLEncoded by SdzDsgnr. So far the first way has been
 * used by OneRowSdzBag and both ways have been used by
 * SdzBag.
 * <p/>
 * See MLTableExample for an example of NOT using an Application Housing!
 * Whilst this works, there should be no reason to do it. This 'controversial'
 * code made use of the fact that a <code>SdzBag</code> already
 * contained the controller toolbar that was needed.
 *
 * @author Chris Murphy
 */
public interface SdzBagI
    extends SdzBeanI
{
    void setCurrentPane(int index);

    // public void add( Component comp, Object constraints);
    int getCurrentPane();

    // public JComponent getPane();
    // public void setPane( JComponent node);
    JComponent[] getPanes();

    void setPanes(JComponent[] panes);

    // will also have Pane get/setPaneByName( nodeName)
    JComponent getPane(int index);

    void setPane(int index, JComponent pane);

    PanelUpdateInfo setPanesReturnInfo(JComponent newPanes[]);

    boolean removePane(JComponent pane);

    int indexOfPane(JComponent pane);

    Strand getStrand();

    void setDefaults();

    String getDefaultName();

    String getName();

    void setName(String name);

    Node[] getNodes();

    void setNodes(Node[] nodes);

    // will also have Node get/setNodeByName( nodeName)
    Node getNode(int index);

    void setNode(int index, Node node);

    boolean removeNode(Node node);
    // public PhysicalNodeControllerInterface getPhysicalController();
    // public void setPhysicalController( PhysicalNodeControllerInterface physical);

     /**/
    void addTransactionTrigger(CloseTransactionTrigger listener);

    void removeTransactionTrigger(CloseTransactionTrigger listener);

    Publisher getTransactionTriggers();

     /**/
    // public void controlActionPerformed( InputControllerEvent event);
    /*
    public void execute( OperationEnum.EXECUTE_QUERY);
    public void insert();
    public void post();
    public void commit();
    public void setCursor( int row);
    */
    void set(SdzBagI sbI);

    boolean equals(Object o); // just to indicate you must implement this

    int hashCode(); // and this!

    int hasNonPaneNumberOfComponents();

    CopyPasteBuffer copyItemValues();

    CopyPasteBuffer copyItemValues(Node node);

    void pasteItemValues();

    //Don't use this, but save result of copyItemValues()
    //CopyPasteBuffer getCopyPasteBuffer();
    boolean isPartOfApplication();

    PanelUpdateInfo getPanelUpdateInfo();

    /**
     * Convenience property the Designer uses to store
     * the name of the file that was used to create this
     * SdzBagI in the first place.
     *
     */
    String getSource();

    void setSource(String source);    
}
