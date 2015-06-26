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

import org.strandz.lgpl.util.AbstractTask;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ErrorMsgContainer;
import org.strandz.lgpl.util.TaskTimeBandMonitorI;
import org.strandz.lgpl.widgets.WidgetUtils;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

/**
 * An <code>Application</code> will contain many instances of this class.
 * Each of these 'commands' is made up simply of a description of itself
 * and an <code>VisibleStrandI</code>.
 * <p/>
 * We need to use the command pattern because once these objects
 * are created they are put into an <code>Application</code>. If it happens to
 * be a MenuApplication then menu items will be created - but we
 * have the flexibility to attach these actions to any event source.
 *
 * @author Chris Murphy
 */
public class VisibleStrandAction extends AbstractStrandAction
{
    private VisibleStrandI visibleStrand;
    /**
     * The Application class may observe the
     * change in the current actionable strand. For instance the Application
     * may want to finish and make invisible the last actionable strand, just
     * before this new actionable strand is displayed.
     */
    private Application application;
    private String title;
    private int estimatedDuration = DEFAULT_DURATION;
    /**
     * Where would the user ideally specify adorned? Designer doesn't deal with applichousing issues as
     * it only creates Strands. The VisibleStrand is really the place where adorned is kept
     * and you could certainly alter it there. In a way having it here is a convenience that
     * allows us to keep the VisibleStrand able to be written over by the generator and 
     * specialize in doing 'bulk standard' initialisation code.  
     * 
     * See SdzNote.NO_HOUSING_HELPERS
     */
    private boolean adorned = true;
    private boolean adornedBeenSet;
    
    private static final int DEFAULT_DURATION = 6001;

    public static final String MASTER_MENU_TITLE = "Master Menu Title";
    
    public VisibleStrandAction(String shortDesc, String masterMenuTitle)
    {
        this( shortDesc, masterMenuTitle, DEFAULT_DURATION);
    }

    public VisibleStrandAction(String shortDesc, String masterMenuTitle, int estimatedDuration)
    {
        putValue(AbstractAction.NAME, shortDesc);
        putValue(AbstractAction.LONG_DESCRIPTION, shortDesc);
        putValue(MASTER_MENU_TITLE, masterMenuTitle);
        this.title = shortDesc;
        this.estimatedDuration = estimatedDuration;
    }

    public String toString()
    {
        return getTitle();
    }

    public String getTitle()
    {
        return title;
    }
    
    /**
     * @param l - is ActionListener but easier to read as an application
     */
    public void setApplication(Application l)
    {
        application = l;
    }
    
    private void doActionWithVisualTimer()
    {
        ActionTask task = new ActionTask( estimatedDuration);
        ErrorMsgContainer err = new ErrorMsgContainer( "Calling " + getValue( AbstractAction.LONG_DESCRIPTION));
        TaskTimeBandMonitorI visualMonitor = WidgetUtils.getTimeBandMonitor( estimatedDuration);
        visualMonitor.start(task, err);
        if(err.isInError)
        {
            Err.error( err.message);
        }
        visualMonitor.stop();
    }

    public void actionPerformed(ActionEvent evt)
    {
        if(visibleStrand == null)
        {
            Err.error("There must be a VisibleStrand to fire");
        }
        doActionWithVisualTimer();
    }

    public void setVisibleStrand(VisibleStrandI visibleStrand)
    {
        this.visibleStrand = visibleStrand;
        if(adornedBeenSet)
        {
            this.visibleStrand.setAdorned( isAdorned());
        }
        else
        {
            //If it hasn't been set then we don't want to write over what 
            //user may have set adorned to on the visibleStrand itself.
        }
    }

    public VisibleStrandI getVisibleStrand()
    {
        return visibleStrand;
    }
    
    private class ActionTask extends AbstractTask
    {
        public ActionTask( int duration)
        {
            setTitle( (String)getValue(AbstractAction.LONG_DESCRIPTION));
            setCompletedPhrase( getValue(AbstractAction.SHORT_DESCRIPTION) + " Done");
            setDuration( duration);
        }

        public Object newTask()
        {
            return new ActionTask.ActualTask();
        }

        public class ActualTask
        {
            ActualTask()
            {
                /*
                * pass on the event
                */
                if(application != null)
                {
                    //ActionEvent ae = new ActionEvent(VisibleStrandAction.this, ActionEvent.ACTION_PERFORMED, null);
                    visibleStrand.setTitle( getTitle());
                    application.changeActionableStrand( visibleStrand, "Actioned (prolly from menu) from application");
                }
                else
                {
                    Err.error("There is no target to throw the event at");
                }
            }
        }
    }

    public boolean isAdorned()
    {
        return adorned;
    }

    public void setAdorned(boolean adorned)
    {
        this.adorned = adorned;
        adornedBeenSet = true;
    }
}
