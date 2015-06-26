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

import org.strandz.view.util.AbstractStrandArea;

import javax.swing.JComponent;

/**
 * This is the interface that any 'application housed' RT Strand will
 * implement. The Application gets the power to perform all the following
 * methods.
 *
 * @author Chris Murphy
 */
public interface VisibleStrandI
{
    /**
     * Called the first time a form is ever displayed.
     */
    void preForm();
    /**
     * Called right before a form is about to be un-displayed. This is
     * where connections or whatever held by the form can be cleaned up.
     * TODO We prolly don't need this method as well as un-display
     */
    //void postForm();

    /**
     * Where sdzSetup() is in turn called from, where you have that kind of
     * Strand. (What we mean here is that many Actionable Strands are just normal code
     * that we wish to plug into an application housing - all such Actionable Strands
     * need is a surface to write onto).
     */
    void sdzInit();

    /**
     * 
     */
    void detachControls();

    /**
     * The first time a strand is activated it will be displayed. As long as the
     * strand is represented somehow as visible (say as a tab or a closed icon)
     * then it will still be displayed and will still keep hold of its current
     * state. In a primitive application housing where a user might not be able
     * to say 'close a tab' then undisplaying of all of them will happen at the
     * end when there is a exitNotification().
     * NOTE: Are now un-selecting before un-display when the user wants to close
     * the application.
     *
     * @param b
     */
    void display(boolean b);

    /**
     * The user can only select an already displayed tab.
     *
     * @param b
     */
    boolean select( boolean b, String reason);

    /**
     * NOTE: Have removed this b/c the only functionality it was being used for
     * was to not allow activating a strand if the current one was in error. The
     * application housing now calls the select method above to make this
     * determination.
     * <p/>
     * Called when the user has indicated to physically go from one strand to
     * another.
     * param activating - are either activating (true) or deactivating (false)
     * return whether to go ahead and activate/deactivate the strand
     */
    //boolean activateAsCurrent( boolean activating);
    void setNodeController(NodeController nodeController);

    NodeController getNodeController();

    /**
     * At moment if is adorned will have a toolbar and a statusbar. If not
     * won't have either 
     */
    boolean isAdorned();
    
    void setAdorned( boolean b);
    
    void setStrandArea(AbstractStrandArea belowMenuPanel);

    void setCurrentPane(int idx);
    
    void setTitle( String title);
    
    String getTitle();

    /*
     * From here onwards needed when started to transfer out 
     */
    SdzBagI getSdzBagI();

    void relocateBack(RelocationBackCallbacksI relocationBackCallbacks);

    void relocateOut(AbstractStrandArea externalStrandArea, RelocationOutCallbacksI relocationOutCallbacks, VisibleStrandI callingStrand);

    void setPanelNodeTitle(JComponent pane, Node workerNode, String s);

    Application getApplication();
}
