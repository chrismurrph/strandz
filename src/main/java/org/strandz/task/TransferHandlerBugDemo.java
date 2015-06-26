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
package org.strandz.task;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ObjectFoundryUtils;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.DisplayUtils;
import org.strandz.view.wombatrescue.WorkerPanel;

import javax.swing.JComponent;
import javax.swing.text.DefaultEditorKit;
import java.awt.event.ActionEvent;

/**
 * This demo illustrates that I was wrong to think that XMLEncoder was the reason
 * for ComponentUtils.fixCopyPasteBug(). It must be something else ...
 */
public class TransferHandlerBugDemo
{
    private WorkerPanel workerPanel = new WorkerPanel();
    DefaultEditorKit.CopyAction copyAction = new MyCopyAction();

    public static void main(String s[])
    {
        main();
    }

    public static void main()
    {
        TransferHandlerBugDemo transferHandlerBugDemo = new TransferHandlerBugDemo();
        boolean ok = transferHandlerBugDemo.doesCopyWork();
        //Err.pr( "Copy worked: " + ok);
        Err.pr( "Test for yourself whether copy and paste work on this panel (it s/be displayed)");
    }

    public TransferHandlerBugDemo()
    {
        workerPanel.init();
        Err.pr("Newly created has transfer handler: " + hasTransferHandler(workerPanel.getTfGroupName()));
        WorkerPanel afterEncodeDecode = (WorkerPanel) ObjectFoundryUtils.copyConstruct(workerPanel);
        Assert.notNull( afterEncodeDecode);
        afterEncodeDecode.init();
        Err.pr("After encode/decode has transfer handler: " + hasTransferHandler(afterEncodeDecode.getTfGroupName()));
        workerPanel = afterEncodeDecode;
    }
    
    boolean doesCopyWork()
    {
        boolean result = true;
        DisplayUtils.display( workerPanel);
        return result;
    }

    private static boolean hasTransferHandler(JComponent control)
    {
        boolean result = true;
        if(control.getTransferHandler() == null)
        {
            result = false;
        }
        return result;
    }
    
    private class MyCopyAction extends DefaultEditorKit.CopyAction
    {
        public void actionPerformed(ActionEvent e)
        {
            super.actionPerformed( e);
        }
    }
}
