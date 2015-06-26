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

import org.strandz.lgpl.note.SdzDsgnrNote;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class DisplayUtils
{
    public static final int DEFAULT_HEIGHT = 550;
    public static final int DEFAULT_WIDTH = 750;
    public static final int LEAVE = -999;

    public static void displayInDialog( JComponent comp)
    {
        JDialog dialog = new JDialog( (Frame)null, false);
        displayInDialog( comp, dialog);
    }
    
    public static JDialog displayInDialog( JComponent comp, JDialog result)
    {
        Dimension preferredSize = comp.getPreferredSize();
        result.setContentPane(comp);
        result.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        result.setLocation(screenSize.width / 2 - preferredSize.width / 2,
            screenSize.height / 2 - preferredSize.height / 2);
        result.setVisible(true);
        return result;
    }

    public static void displayInAcknowledgeDialog(JComponent comp, String title)
    {
        JFrame parent = MessageDlg.getFrame();
        if(parent == null)
        {
            //Only real problem I know of is that corner icon will not be set
            Err.alarm("In displayInAcknowledgeDialog() and no MessageDlg.getFrame() set");
        }
        JDialog frame = new JDialog(parent);
        frame.setTitle(title);
        Dimension dim = comp.getPreferredSize();
        Dimension preferredSize = setPreferredSize(comp, dim.height, dim.width);
        frame.setContentPane(comp);
        frame.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screenSize.width / 2 - preferredSize.width / 2,
            screenSize.height / 2 - preferredSize.height / 2);
        frame.setVisible(true);
    }

    public static Dimension setPreferredSize(JComponent comp, int height, int width)
    {
        Dimension result = null;
        Dimension preferredSize = comp.getPreferredSize();
        // Err.pr( "preferred size was " + preferredSize);
        if(height != LEAVE) preferredSize.height = height;
        if(width != LEAVE) preferredSize.width = width;
        comp.setPreferredSize(preferredSize);
        result = preferredSize;
        return result;
    }

    public static void display(JComponent panel)
    {
        display(panel, false);
    }

    public static void display(JFrame frame, JComponent panel)
    {
        display( panel, true, DEFAULT_HEIGHT, DEFAULT_WIDTH, frame, null, null);
    }

    public static void displayInExitable(JComponent panel)
    {
        display(panel, true);
    }

    public static void display(JComponent panel, boolean exitable)
    {
        display(panel, exitable, DEFAULT_HEIGHT, DEFAULT_WIDTH);
    }

    public static void displayRespectingPreferred(
        JComponent panel, boolean exitable, String title)
    {
        Dimension dim = panel.getPreferredSize();
        display(panel, exitable, dim.height, dim.width, title);
    }

    public static void displayRespectingPreferred(JComponent panel)
    {
        displayRespectingPreferred( panel, true);
    }

    public static void displayRespectingPreferred(JComponent panel, boolean exitable)
    {
        Dimension dim = panel.getPreferredSize();
        display(panel, exitable, dim.height, dim.width);
    }

    public static void displayRespectingPreferred(JComponent panel, boolean exitable, JFrame frame)
    {
        Dimension dim = panel.getPreferredSize();
        display(panel, exitable, dim.height, dim.width, frame, null, null);
    }

    public static void display(JComponent panel, boolean exitable, int height, int width)
    {
        display(panel, exitable, height, width, (String) null);
    }

    public static void display(JComponent panel, boolean exitable, String title)
    {
        display(panel, exitable, DEFAULT_HEIGHT, DEFAULT_WIDTH, title);
    }

    public static void display(JComponent panel, boolean exitable, int height, int width, String title)
    {
        display(panel, exitable, height, width, null, title);
    }

    public static void display(JComponent panel, boolean exitable, int height, int width, WindowListener wl)
    {
        display(panel, exitable, height, width, null, wl, null);
    }

    public static void display(JComponent panel, boolean exitable, int height, int width, WindowListener wl, String title)
    {
        display(panel, exitable, height, width, null, wl, title);
    }

    public static void display(JComponent panel, boolean exitable, int height, int width, JFrame frame)
    {
        display( panel,  exitable, height, width, frame, null, null);
    }

    public static void display(JComponent panel, boolean exitable, int height, int width, JFrame frame, WindowListener wl, String title)
    {
        if(frame == null)
        {
            frame = new JFrame();
        }
        if(title != null)
        {
            frame.setTitle(title);
        }
        if(exitable)
        {
            if(wl == null)
            {
                WindowListener l = new WindowAdapter()
                {
                    public void windowClosing(WindowEvent e)
                    {
                        System.exit(0);
                    }
                };
                frame.addWindowListener(l);
            }
            else
            {
                Err.error("Being exitable and having a window listener not compatible");
            }
        }
        if(wl != null)
        {
            frame.addWindowListener(wl);
        }
        Dimension preferredSize = setPreferredSize(panel, height, width);
        frame.setContentPane(panel);
        frame.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screenSize.width / 2 - preferredSize.width / 2,
            screenSize.height / 2 - preferredSize.height / 2);
        frame.setVisible(true);
    }

    public static void setUI()
    {
        Err.pr(SdzDsgnrNote.L_AND_F,
            "Will want to use the L and F dependent on the platform");

        //Default L&F will use for Unix
        String uiName = "Metal";
        String lAndFJavaName = "javax.swing.plaf.metal.MetalLookAndFeel";
        String osName = Utils.OS_NAME;
        if(osName.startsWith("Windows"))
        {
            uiName = "Windows";
            lAndFJavaName = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        }
        else if(osName.startsWith("Mac OS"))
        {
            //Leave at metal for now
        }
        // These embellishments stuffed the cursor up!
        // JDialog.setDefaultLookAndFeelDecorated( true);
        // JFrame.setDefaultLookAndFeelDecorated( true);
        Toolkit.getDefaultToolkit().setDynamicLayout(true);
        System.setProperty("sun.awt.noerasebackground", "true");
        try
        {
            // What is this theme stuff anyway?
            /*
             javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme(
             new javax.swing.plaf.metal.DefaultMetalTheme()
             //new DemoMetalTheme()
             );
             */
            UIManager.setLookAndFeel(lAndFJavaName);
            /*
            Err.pr( "L&F has been programmatically set to " + lAndFJavaName);
            Err.pr( "When ask for L&F get " + UIManager.getLookAndFeel());
            Print.prArray( UIManager.getInstalledLookAndFeels(), "Installed L&Fs");
            */
        }
        catch(UnsupportedLookAndFeelException e)
        {
            System.out.println(uiName
                + " Look & Feel not supported on this platform. \nProgram Terminated");
            System.exit(0);
        }
        catch(IllegalAccessException e)
        {
            System.out.println(uiName + " Look & Feel could not be accessed. \nProgram Terminated");
            System.exit(0);
        }
        catch(ClassNotFoundException e)
        {
            System.out.println(uiName + " Look & Feel could not found. \nProgram Terminated");
            System.exit(0);
        }
        catch(InstantiationException e)
        {
            System.out.println(uiName
                + " Look & Feel could not be instantiated. \nProgram Terminated");
            System.exit(0);
        }
        catch(Exception e)
        {
            System.out.println("Unexpected error. \nProgram Terminated");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void listAllFonts()
    {
        GraphicsEnvironment env =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = env.getAvailableFontFamilyNames();
        System.out.println("Available Fonts:");
        for(int i = 0; i < fontNames.length; i++)
        {
            Err.pr(" " + fontNames[i]);
        }
    }
    
    public static void main(String[] args)
    {
        listAllFonts();
    }        
}
