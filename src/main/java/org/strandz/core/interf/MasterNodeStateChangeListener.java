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

import org.strandz.core.domain.constants.StateEnum;
import org.strandz.core.domain.event.StateChangeEvent;
import org.strandz.core.domain.event.StateChangeTrigger;
import org.strandz.lgpl.util.Print;

import javax.swing.SwingUtilities;

/*
 * The preferred editable list control would have been a JTable. When
 * wrote JTextAreaImpl the JTable "focus when no rows" was just a RFE.
 * Considered too hard to work around this issue with a blank row. Of
 * course JList is read-only. When JTable comes good we will want to
 * implement another JTableImpl that is not subject to the central
 * controller, as JTextAreaImpl is not.
 *
 * If we could have easily listened to which row were on in the JTextArea
 * we might have implemented this differently. Own local controller would
 * have been directing.
 *
 * Way implemented here is to listen to every piece of text entered and
 * directly alter the data thru NodeTableModelInterface setValueAt,
 * insertLine and removeLine. The last two are new creations. This worked
 * fine in all circumstances except where user had just pressed insert. Note
 * that this is insert of the master block as the JTextAreaImpl detail
 * block is not connected to the controller. Actually adding a record from
 * the UI to the data is a "move away from record" operation. Thus after have
 * just pressed insert our block's data will be that of the previous master
 * record's detail. By going back and then forwards again our block comes to
 * contain what it should - an empty list. Here we fake the user going previous
 * then next by the main controller. TODO Why post doesn't work s/be investigated at
 * some stage. Here we are (very tackily) forcing a backgroundAdd() to occur.
 */

/**
 * This class is a special use listener used by JTextAreaModelImpl. When a user
 * causes a change into insert state some sort of an adjustment needs to be made.
 * Whatever is happening it is yet to be tested with a current version of Swing
 * or documented properly - although there is plenty of documentation in the source.
 *
 * @author Chris Murphy
 */
public class MasterNodeStateChangeListener implements StateChangeTrigger
{
    private Node node;

    public void setNode(Node node)
    {
        this.node = node;
    }

    public void stateChangePerformed(StateChangeEvent e)
    {
        if(e.getCurrentState() == StateEnum.NEW)
        {
            Print.pr("###############Got message " + e + " so will prev/next");

            Runnable doHelloWorld = new Runnable()
            {
                public void run()
                {
                    // Err.pr( "NEED MANUALLY POST");
                    node.getStrand().getOPor().post(node.getBlock(), false);
                }
            };
            SwingUtilities.invokeLater(doHelloWorld);
        }
        else
        {
            Print.pr("###############Got message " + e);
        }
    }
}
