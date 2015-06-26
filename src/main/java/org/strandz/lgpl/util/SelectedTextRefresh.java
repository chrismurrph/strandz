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
package org.strandz.lgpl.util;

import javax.swing.JTextField;
import javax.swing.JComponent;
import java.awt.KeyboardFocusManager;

/**
 * Named 'Refresh' because can be used in conjunction with Node.REFRESH() to get over
 * the fact that strandz does not remember what the selected text was.
 */
public class SelectedTextRefresh
{
    private int startSelectedText;
    private int endSelectedText;
    private JTextField textField;
    
    public void beforeRefresh()
    {
        textField = null;
        startSelectedText = Utils.UNSET_INT;
        endSelectedText = Utils.UNSET_INT;
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        JComponent component = (JComponent)manager.getPermanentFocusOwner();
        if(component instanceof JTextField)
        {
            textField = (JTextField)component;
            startSelectedText = textField.getSelectionStart(); 
            endSelectedText = textField.getSelectionEnd(); 
        }
    }
    
    public void afterRefresh()
    {
        if(textField != null)
        {
            textField.setSelectionStart( startSelectedText);
            textField.setSelectionEnd( endSelectedText);
            //No need
            //textField.revalidate();
            //textField.repaint();
            //Err.pr( "Have re-set selection on JTextField <" + textField.getName() + 
            //        "> to (" + startSelectedText + "," + endSelectedText + ")");
        }
    }
}
