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

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionListenerAction extends AbstractStrandAction
{
    private ActionListener actionListener;
    private String title;

    public static final String MASTER_MENU_TITLE = "Master Menu Title";

    public ActionListenerAction(String shortDesc, String menuBarTitle)
    {
        if(Utils.isBlank( menuBarTitle))
        {
            Err.error( "Master Menu Title must not be blank");
        }
        putValue(AbstractAction.NAME, shortDesc);
        putValue(AbstractAction.LONG_DESCRIPTION, shortDesc);
        putValue(ActionListenerAction.MASTER_MENU_TITLE, menuBarTitle);
        Err.pr( SdzNote.SUB_MENU, "Have put MASTER_MENU_TITLE attribute of <" + menuBarTitle +  "> on action <" + this + "> of type " + this.getClass().getName());
        this.title = shortDesc;
    }

    public String toString()
    {
        return getTitle();
    }

    public String getTitle()
    {
        return title;
    }
    
    public void actionPerformed(ActionEvent evt)
    {
        if(actionListener == null)
        {
            Err.error("There must be an actionListener to fire");
        }
        /*
        * pass on the event
        */
        actionListener.actionPerformed( evt);
    }

    public void setActionListener(ActionListener action)
    {
        this.actionListener = action;
    }

    public ActionListener getActionListener()
    {
        return actionListener;
    }

    public void setApplication(Application application)
    {
        //Err.error("setApplication()" + " not implemented");
    }

    public org.strandz.core.interf.VisibleStrandI getVisibleStrand()
    {
        return null;
    }
}
