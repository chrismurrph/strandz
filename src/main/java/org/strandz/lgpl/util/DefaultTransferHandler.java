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

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.BadLocationException;
import javax.swing.TransferHandler;
import javax.swing.JComponent;
import javax.swing.plaf.UIResource;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.im.InputContext;
import java.io.IOException;

/**
 * Copied from Java as part of a workaround
 * 
 * A Simple TransferHandler that exports the data as a String, and
 * imports the data from the String clipboard.  This is only used
 * if the UI hasn't supplied one, which would only happen if someone
 * hasn't subclassed Basic.
 */
class DefaultTransferHandler extends TransferHandler implements
        UIResource
{
    public void exportToClipboard(JComponent comp, Clipboard clipboard,
                                  int action) throws IllegalStateException {
        if (comp instanceof JTextComponent) {
            JTextComponent text = (JTextComponent)comp;
            int p0 = text.getSelectionStart();
            int p1 = text.getSelectionEnd();
            if (p0 != p1) {
                try {
                    Document doc = text.getDocument();
                    String srcData = doc.getText(p0, p1 - p0);
                    StringSelection contents =new StringSelection(srcData);

                    // this may throw an IllegalStateException,
                    // but it will be caught and handled in the
                    // action that invoked this method
                    clipboard.setContents(contents, null);

                    if (action == TransferHandler.MOVE) {
                        doc.remove(p0, p1 - p0);
                    }
                } catch (BadLocationException ble) {}
            }
        }
    }
    public boolean importData(JComponent comp, Transferable t) {
        if (comp instanceof JTextComponent) {
            DataFlavor flavor = getFlavor(t.getTransferDataFlavors());

            if (flavor != null) {
    InputContext ic = comp.getInputContext();
    if (ic != null) {
  ic.endComposition();
    }
                try {
                    String data = (String)t.getTransferData(flavor);

                    ((JTextComponent)comp).replaceSelection(data);
                    return true;
                } catch (UnsupportedFlavorException ufe) {
                } catch (IOException ioe) {
                }
            }
        }
        return false;
    }
    public boolean canImport(JComponent comp,
                             DataFlavor[] transferFlavors) 
    {
        if(comp instanceof JTextComponent)
        {
            JTextComponent c = (JTextComponent)comp;
            if (!(c.isEditable() && c.isEnabled())) {
                return false;
            }
        }
        else
        {
            //Not sure how to replicate (from some idle dragging) but kept on getting ClassCastExceptions
            //which this should fix
            Err.pr( "Stopped ClassCastException from idle dragging to a " + comp.getClass().getName());
            return false;
        }
        return (getFlavor(transferFlavors) != null);
    }
    public int getSourceActions(JComponent c) {
        return NONE;
    }
    private DataFlavor getFlavor(DataFlavor[] flavors) {
        if (flavors != null) {
            for (int counter = 0; counter < flavors.length; counter++) {
                if (flavors[counter].equals(DataFlavor.stringFlavor)) {
                    return flavors[counter];
                }
            }
        }
        return null;
    }
}
