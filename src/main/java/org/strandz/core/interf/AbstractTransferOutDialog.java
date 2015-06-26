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

import foxtrot.Task;
import foxtrot.Worker;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.DisplayUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.StopWatch;
import org.strandz.lgpl.widgets.CursorIndentationCounter;
import org.strandz.view.util.AbstractStrandArea;
import org.strandz.core.domain.constants.OperationEnum;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A Dialog Application Housing that a VisibleStrandI can be temporarily (whilst the Dialog is up)
 * transferred out into.
 */
abstract public class AbstractTransferOutDialog extends JDialog
{
    /**
     * The new, temporary place onto which we will put strandArea 
     */
    protected AbstractStrandArea externalStrandArea;
    /**
     * Actor - the VisibleStrand that gets moved to a different container
     */
    protected VisibleStrandI transferVisibleStrand;
    protected StrandI transferStrand;
    protected RuntimeAttribute clickedOnAttribute;
    private Frame owner;
    protected RelocationOutCallbacks relocationOutCallbacks = new RelocationOutCallbacks();
    protected VisibleStrandI callingVisibleStrand;
    private RelocationBackCallbacks relocationBackCallbacks = new RelocationBackCallbacks();
    private ApplicationHelper applicationHelper;
    private CursorIndentationCounter hourglassIndent = CursorIndentationCounter.getInstance( OperationEnum.class);
    private static final boolean EXPERIMENT = false;

    protected AbstractTransferOutDialog(Frame owner, String title, boolean modal)
            throws HeadlessException
    {
        super(owner, title, modal);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    protected void init( Frame owner, VisibleStrandI transferVisibleStrand, RuntimeAttribute clickedOnAttribute, 
               VisibleStrandI callingVisibleStrand, ApplicationHelper applicationHelper)
    {
        this.owner = owner;
        this.transferVisibleStrand = transferVisibleStrand;
        this.transferStrand = transferVisibleStrand.getSdzBagI().getStrand(); 
        this.clickedOnAttribute = clickedOnAttribute;
        this.applicationHelper = applicationHelper;
        this.callingVisibleStrand = callingVisibleStrand;
        addWindowListener( new WindowAdapter()
        {
            public void windowClosing(WindowEvent we)
            {
                final StopWatch closeWindowTimer = new StopWatch();
                closeWindowTimer.startTiming();
                final String msg = "Dismiss dialog, which saves and re-queries";
                hourglassIndent.indent( true, msg);
                if(EXPERIMENT)
                {
                    Err.pr( "Hourglass is supposed to be showing");
                    try
                    {
                        Thread.sleep( 2000);
                    }
                    catch(InterruptedException e)
                    {
                        Err.error(e);
                    }
                    Err.pr( "Sleep over");
                }
                else
                {
                    //Following was an attempt to make above (cursor show) actually happen for user to see. Didn't work!
                    //Also changed dispose() to setVisible( false), just in case.
                    //TODO - get help with this problem!
                    try
                    {
                       Worker.post(new Task()
                       {
                          public Object run() throws Exception
                          {
                              Object result = null;
                              if(afterUserDismissed())
                              {
                                  setVisible( false);
                                  AbstractTransferOutDialog.this.transferVisibleStrand.relocateBack( relocationBackCallbacks);
                                  hourglassIndent.indent( false, msg);
                              }
                              else
                              {
                                  //A validation problem that needs to be fixed exists
                              }
                              closeWindowTimer.stopTiming();
                              Err.pr( "closeWindowTimer: " + closeWindowTimer.getElapsedTimeStr());
                              return result;
                          }
                       });
                    }
                    catch(Exception e)
                    {
                        Err.error(e);
                    }
                }
            }
        });
        transferVisibleStrand.relocateOut( externalStrandArea, relocationOutCallbacks, callingVisibleStrand);        
    }

    public class RelocationOutCallbacks implements RelocationOutCallbacksI
    {
        //Altering what we did at DT, so s/be called from a method signature alterSdzSetup()
        public void performAlterSdzSetup()
        {
            SdzBagI sbI = transferVisibleStrand.getSdzBagI();
            relocatePanelGoingOut( sbI);
        }
        
        public void setPanelNodeTitles()
        {
            SdzBagI sbI = transferVisibleStrand.getSdzBagI();
            setPanelNodeTitlePanelGoingOut( sbI);
        }
        
        public void display()
        {
            JPanel parentPanel = (JPanel)((JFrame)owner).getContentPane();
            Dimension dim = parentPanel.getSize();
            DisplayUtils.setPreferredSize(externalStrandArea, dim.height, dim.width);            
            setContentPane( externalStrandArea);          
            Point parentLocation = parentPanel.getLocationOnScreen();
            setLocation( parentLocation);
            setVisible( false);
            if(!applicationHelper.hasAlreadyBeenDisplayed(transferVisibleStrand, ApplicationHelper.ANYWHERE))
            {
                transferVisibleStrand.preForm();
                applicationHelper.displayedExternallyForms.add(transferVisibleStrand);
            }
            transferVisibleStrand.display( true);
            pack();
        }
        
        public void performSearch()
        {
            StopWatch perfSearchTimer = new StopWatch();
            perfSearchTimer.startTiming();
            String msg = "Perform search b/c clicked on " + clickedOnAttribute;
            hourglassIndent.indent( true, msg);
            AbstractTransferOutDialog.this.performSearch( clickedOnAttribute);
            perfSearchTimer.stopTiming();
            Err.pr( "perfSearchTimer: " + perfSearchTimer.getElapsedTimeStr());
            hourglassIndent.indent( false, msg);
        }
    }
    
    class RelocationBackCallbacks implements RelocationBackCallbacksI
    {
        public void performAlterSdzSetup()
        {
            SdzBagI sbI = transferVisibleStrand.getSdzBagI();
            relocatePanelComingBack( sbI);
        }
        
        public void setPanelNodeTitles()
        {
            SdzBagI sbI = transferVisibleStrand.getSdzBagI();
            setPanelNodeTitlePanelComingBack( sbI);
        }
    }
    
    /**
     * In case you need to relocate the panel within its own panel hierachy - for example if you didn't
     * need an Alphabet panel to appear at the top. This (and relocatePanelComingBack() partner) can serve
     * to undo (and later 'undo the undo') what was done in AlterSdzSetup - in case you want to go to a
     * simpler style of display. 
     * @param sbI
     */
    abstract public void relocatePanelGoingOut( SdzBagI sbI);
    abstract public void relocatePanelComingBack( SdzBagI sbI);
    abstract public void setPanelNodeTitlePanelGoingOut( SdzBagI sbI);
    abstract public void setPanelNodeTitlePanelComingBack( SdzBagI sbI);
        
    abstract public void performSearch( RuntimeAttribute clickedOnAttribute);

    /**
     * Some subclasses may wish to override. For example can use this method to reset what is the current node
     */
    public boolean afterUserDismissed()
    {
        boolean result = true;
        boolean ok = transferVisibleStrand.select( false, "Transfer out dialog has been dismissed");
        if(!ok)
        {
            Err.error();
        }
        ok = callingVisibleStrand.select( true, "Transfer out dialog has been dismissed, so we now re-select the caller");
        if(!ok)
        {
            Err.error();
        }
        Err.pr( SdzNote.NOT_KEEPING_PLACE, "In afterUserDismissed() and current node: " + 
                transferVisibleStrand.getSdzBagI().getStrand().getCurrentNode());
        return result;
    }
        
    public Node getTargetNode( StrandI transferStrand)
    {
        return null;
    }
}
