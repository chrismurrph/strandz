/*
    Strandz LGPL - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz LGPL is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA


    The authors can be contacted via www.strandz.org
*/
package org.strandz.lgpl.util;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.util.List;

/**
 * This class provides a way of showing a message and getting a response. It
 * is a wrapper around JOptionPane.showMessageDialog. If
 * you require anything more complex then create your own dialog. An example
 * is CustomDialog.
 * Also serves to store the application's frame in one central place, and
 * to control user event blocking.
 *
 * @author Chris Murphy
 */
public class MessageDlg
{
    private static JFrame frame;
    private static JComponent parent;
    private static boolean internalFrames = false;
    private static FixedGlassPane userBlocker;

    public static final int KILL = 1;
    //Only set this to true for debugging - it is quite legitimate
    //to not have set up your main frame/panel, and yet a MessageDlg
    //needs to be thrown up.
    private static final boolean ERROR_ON_FRAME_CREATE = false;
    /*
     static public void pr( String str)
     {
     Err.pr( str);
     System.out.flush();
     }
     */

    static public void setDlgParent(JComponent f)
    {
        parent = f;
        //Err.pr( "DlgParent set to <" + f.getClass().getName() + "> named <" + f.getName() + ">");
        //It makes for better debugging if you...
        Assert.notNull( f.getName(), "Must name the <" + 
                f.getClass().getName() + "> panel before calling setDlgParent()");
//        if(f.getName().equals( "TableAttributeCustomizer"))
//        {
//            Err.stack();
//        }
    }

    static public JComponent getDlgParent()
    {
        return parent;
    }

    static public void setInternalFrames(boolean b)
    {
        internalFrames = b;
    }

    /*
    public MessageDlg(String msg, int kill)
    {
        if(kill != KILL)
        {
            Err.error("Can only call with KILL");
        }
        if(parent == null)
        {
            if(Err.isBatch())
            {
                Err.error(msg);
            }
            else
            {
                if(ERROR_ON_FRAME_CREATE)
                {
                    Err.error("ERROR_ON_FRAME_CREATE");
                }
                JFrame frame = new JFrame();
                JPanel panel = new JPanel();
                frame.add(panel);
                parent = panel;
            }
        }
        else
        {
            int ret = showConfirmDialog(null, msg, "Stack Dumper",
                JOptionPane.YES_NO_OPTION);
            if(ret == JOptionPane.YES_OPTION)
            {
                Err.error(msg);
            }
        }
    }
    */

    public MessageDlg(String msg)
    {
        this(msg, null, JOptionPane.INFORMATION_MESSAGE);
    }

    public MessageDlg(String msg, String title, int msgType)
    {
        String msgs[] = new String[1];
        msgs[0] = msg;
        display(msgs, title, msgType);
    }

    public MessageDlg(String msgs[], String title, int msgType)
    {
        display(msgs, title, msgType);
    }
    
    private static void chkParent()
    {
        if(parent == null)
        {
            Err.error( "parent == null (try calling setDlgParent() to fix this)");
        }
        if(!parent.isVisible())
        {
            Err.pr( "parent not visible");
        }
    }

    private void display(String msgs[], String title, int msgType)
    {
        boolean autoAcknowledgement = false;
        if(Err.isBatch())
        {
            for(int i = 0; i <= msgs.length - 1; i++)
            {
                Print.pr("AUTOMATIC BATCH TEXT ACKNOWLEDGEMENT <<" + msgs[i] + ">>");
                // To find out which trigger coming from:
                // throw new Error();
            }
            autoAcknowledgement = true;
        }
        else
        {
            chkParent();
            /* Was doing too much here ...
            if(parent == null)
            {
                if(ERROR_ON_FRAME_CREATE)
                {
                    Err.error("ERROR_ON_FRAME_CREATE");
                }
                JFrame frame = new JFrame();
                JPanel panel = new JPanel();
                frame.getContentPane().add(panel);
                parent = panel;
            }
            */
        }
        if(!autoAcknowledgement)
        {
            showMessageDialog(parent, msgs, title, msgType);
        }
    }

    public MessageDlg(String msgs[], int msgType)
    {
        display(msgs, null, msgType);
    }
    
    public MessageDlg(Object list, int msgType)
    {
        this( (List)list, msgType);
    }

    public MessageDlg(List list, int msgType)
    {
        if(!list.isEmpty())
        {
            Object obj = list.get(0);
            if(!(obj instanceof String))
            {
                Err.error("Can't MessageDlg() a " + obj.getClass().getName());
            }
        }
        display((String[]) list.toArray(new String[list.size()]), null, msgType);
    }

    private void showMessageDialog(JComponent parent, Object message, String title, int msgType)
    {
        chkParent();
//        Component ultimateParent = SwingUtilities.getWindowAncestor( parent);
//        Err.pr( "parent is " + parent.getName() + " of type " + parent.getClass().getName()
//            + " and ultimate parent is " + ultimateParent.getName() +
//            " of type " + ultimateParent.getClass().getName());
        if(title == null)
        {
            if(internalFrames)
            {
                JOptionPane.showInternalMessageDialog(parent, message);
            }
            else
            {
                JOptionPane.showMessageDialog(parent, message);
            }
        }
        else
        {
            if(internalFrames)
            {
                JOptionPane.showInternalMessageDialog(parent, message, title, msgType);
            }
            else
            {
                JOptionPane.showMessageDialog(parent, message, title, msgType);
            }
        }
    }

    public static class FormatList
    {
        private List list;

        public FormatList(List list)
        {
            this.list = list;
        }

        public String toString()
        {
            StringBuffer result = new StringBuffer();
            for(int i = 0; i < list.size(); i++)
            {
                Object o = list.get(i);
                result.append(o);
                result.append('\n');
            }
            return result.toString();
        }
    }
    
    public static int showConfirmCancelDialog(String messages[],
                                              String title)
    {
        chkParent();        
        return showConfirmDialog(parent, messages, title,
            JOptionPane.YES_NO_CANCEL_OPTION);
    }

    public static int showConfirmCancelDialog(Object message,
                                              String title)
    {
        chkParent();        
        return showConfirmDialog(parent, message, title,
            JOptionPane.YES_NO_CANCEL_OPTION);
    }

    public static int showConfirmDialog(Object message,
                                        String title)
    {
        chkParent();        
        return showConfirmDialog(parent, message, title, JOptionPane.YES_NO_OPTION);
    }

    private static int showConfirmDialog(Component parentComponent,
                                         Object message,
                                         String title,
                                         int optionType)
    {
        int result;
        if(message instanceof List && !((List)message).isEmpty() && ((List)message).get(0) instanceof String)
        {
            List listMsg = (List)message;
            message = listMsg.toArray();
        }
        if(internalFrames)
        {
            result = JOptionPane.showInternalConfirmDialog(parentComponent, message,
                title, optionType);
        }
        else
        {
            result = JOptionPane.showConfirmDialog(parentComponent, message, title,
                optionType);
        }
        return result;
    }

    public static JFrame getFrame()
    {
        return getFrame(false);
    }

    public static JFrame getFrame(boolean nullOk)
    {
        if(!nullOk && frame == null)
        {
            Err.error("Have not set a frame yet so too early to call getFrame()");
        }
        return frame;
    }

    public static void blockUser()
    {
        userBlocker.start();
        /*
        try
        {
            Thread.sleep( 400);
        }
        catch (InterruptedException e)
        {
        }
        */
    }

    public static void allowUser()
    {
        userBlocker.finish();
    }

    public static void setFrame(JFrame frame)
    {
        MessageDlg.frame = frame;
        //Err.pr( "Set frame to " + frame);
        if(userBlocker == null)
        {
            userBlocker = new FixedGlassPane( frame);
        }
        else
        {
            Err.pr( "MessageDlg.setFrame() only needs to be called once per application");
        }
    }
}

/*
   public boolean getResponse() {
   **
   setVisible( true);
   setModal( true);
   return true;
   **
   return true;
   }
   */

/*
   static public Frame getFrame()
   {
   Frame result = null;
   if(parent == null)
   {
   Err.error( "Cannot call getFrame() until " +
   "setDlgParent() has been called");
   }
   else
   {
   Err.pr( "Starting off with a " + parent.getClass().getName() +
   " called <" + parent.getName() + ">");
   }
   Container higher = parent;
   while(true)
   {
   higher = higher.getParent();
   if(higher instanceof Frame)
   {
   result = (Frame)higher;
   break;
   }
   else if(higher == null)
   {
   Err.error( "Got to top and didn't find a Frame!");
   }
   else
   {
   Err.pr( "Reading a " + higher.getClass().getName());
   }
   }
   return result;
   }
   */

/*
 class BOKListener implements ActionListener{
 Frame parent;
 MessageDlg dlg;

 public BOKListener(MessageDlg dlg, Frame parent){
 this.dlg = dlg;
 this.parent = parent;
 }

 public void actionPerformed(ActionEvent e){
 *
 dlg.setVisible( false);
 dlg.setModal( false);
 dlg.dispose();
 parent.toFront();
 parent.requestFocus(); //doesn't get it but may do one day! - try grabFocus()!!
 *
 }
 }
 */
