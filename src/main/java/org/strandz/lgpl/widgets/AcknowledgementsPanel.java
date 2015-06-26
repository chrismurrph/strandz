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
package org.strandz.lgpl.widgets;

import org.strandz.lgpl.tablelayout.ModernTableLayout;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashMap;

public class AcknowledgementsPanel extends JPanel
{
    private JPanel panel = new JPanel();
    private JScrollPane scrollPane;
    private Bundle bundles[];
    private HashMap specificHeights;
    private static Color bg = Color.white;
    private static final int BORDER = 25;

    public AcknowledgementsPanel(Bundle bundles[], HashMap specificHeights)
    {
        this.bundles = bundles;
        this.specificHeights = specificHeights;
    }

    public void init()
    {
        setLayout(new BorderLayout());
        panel = new ScrollablePanel();
        panel.setBackground(bg);
        scrollPane = new JScrollPane(panel);
        add(scrollPane, BorderLayout.CENTER);

         /**/
        int numRows = bundles.length + 1; //add one for border
        double size[][] = new double[2][numRows];
        size[0][0] = 0.5;
        size[0][1] = 0.5;
        double percentage = calcPercent(bundles.length - specificHeights.size());
        //Err.pr( "percentage: " + percentage);
        for(int i = 0; i <= numRows - 1; i++)
        {
            Integer specificHeight = (Integer) specificHeights.get(new Integer(i));
            if(specificHeight != null)
            {
                size[1][i] = specificHeight.intValue();
            }
            else
            {
                size[1][i] = percentage;
            }
        }
        panel.setLayout(new ModernTableLayout(size));
        /**/
        MouseListener ml = new LocalMouseListener();
        for(int i = 0; i < bundles.length; i++)
        {
            Bundle bundle = bundles[i];
            ImagePanel imagePanel = new ImagePanel(bundle.iconFile, bundle.name, bg);
            imagePanel.setName(bundle.name);
            imagePanel.init();
            imagePanel.label.setName(bundle.link);
            AttributionPanel attributionPanel = new AttributionPanel(bundle.name, bundle.text, bundle.link, bundle.license);
            String where = "0, " + new Integer(i).toString();
            panel.add(imagePanel, where);
            where = "1, " + new Integer(i).toString();
            panel.add(attributionPanel, where);
            imagePanel.label.addMouseListener(ml);
            attributionPanel.linkLabel.addMouseListener(ml);
        }
    }

    private double calcPercent(int num)
    {
        double result = Utils.floatDivide(1, num);
        return result;
    }

    public Dimension getPreferredSize()
    {
        Dimension result = super.getPreferredSize();
        result.height = 500;
        result.width = 675;
        return result;
    }

    public static class Bundle
    {
        public String name;
        public String link;
        public String iconFile;
        public String text;
        public String license;
    }

    private static class LocalMouseListener extends MouseAdapter
    {
        private void launch(JComponent comp)
        {
            try
            {
                BrowserLauncher.openURL(comp.getName());
            }
            catch(IOException e1)
            {
                Err.error(e1);
            }
        }

        public void mouseClicked(MouseEvent e)
        {
            JComponent source = (JComponent) e.getSource();
            source.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            launch(source);
            source.setCursor(null); //turn off the wait cursor
        }

        public void mouseEntered(MouseEvent e)
        {
            JComponent source = (JComponent) e.getSource();
            //Err.pr( "Entered " + e.getSource());
            source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        public void mouseExited(MouseEvent e)
        {
            JComponent source = (JComponent) e.getSource();
            //Err.pr( "Exited " + source);
            source.setCursor(null); //turn off the wait cursor
        }
    }

    static class AttributionPanel extends JLabel
    {
        JLabel titleLabel;
        JLabel textLabel;
        JLabel licenseLabel;
        JLabel linkLabel;

        private static String starta = "<html><center><a href=\"";
        private static String enda = "</a></center></html>";
        private static String start = "<html><center><P>";
        private static String end = "</P></center></html>";

        private static int TOP = 10;
        private static final int ATTRIB_ROW_HEIGHT = 30;
        private static int BOTTOM = 25;
        private static Color WEB_PAGE_COLOR = new Color(0x505028);
        private static Color LICENSE_COLOR = new Color(0xBC32FF);
        private static Font TITLE_FONT = new Font("Dialog", Font.BOLD, 14);
        private static Font PLAIN_FONT = new Font("Dialog", Font.BOLD, 12);

        AttributionPanel(String title, String txt, String linkText, String licenseText)
        {
            //setBorder( new LineBorder( Color.black));
            titleLabel = new JLabel(start + title + end, JLabel.CENTER);
            textLabel = new JLabel(start + txt + end, JLabel.CENTER);
            licenseLabel = new JLabel(start + licenseText + end, JLabel.CENTER);
            linkLabel = new JLabel(starta + linkText + "\">" + linkText +
                enda, JLabel.CENTER);
            WidgetUtils.setLabelProperties(titleLabel, WEB_PAGE_COLOR, TITLE_FONT, bg);
            WidgetUtils.setLabelProperties(textLabel, null, PLAIN_FONT, bg);
            WidgetUtils.setLabelProperties(licenseLabel, LICENSE_COLOR, PLAIN_FONT, bg);
            WidgetUtils.setLabelProperties(linkLabel, null, PLAIN_FONT, bg);

            double size[][] =
                {
                    // Columns
                    {
                        ModernTableLayout.FILL,
                    },
                    // Rows
                    {
                        TOP, ATTRIB_ROW_HEIGHT, ModernTableLayout.FILL, ATTRIB_ROW_HEIGHT, ATTRIB_ROW_HEIGHT, BOTTOM,
                    }
                };
            setLayout(new ModernTableLayout(size));
            add(titleLabel, "0, 1");
            add(textLabel, "0, 2");
            add(licenseLabel, "0, 3");
            add(linkLabel, "0, 4");
            linkLabel.setName(linkText);
        }

        private static String formLicense(String licenseText)
        {
            String result = start + licenseText + end;
            return result;
        }

        public Dimension getPreferredSize()
        {
            Dimension result = super.getPreferredSize();
            result.height = 170;
            return result;
        }

    }

    static class ScrollablePanel extends JPanel implements Scrollable
    {

        public int getScrollableBlockIncrement(Rectangle arg0, int arg1, int arg2)
        {
            return 30;
        }

        public boolean getScrollableTracksViewportWidth()
        {
            return true;
        }

        public boolean getScrollableTracksViewportHeight()
        {
            return false;
        }

        public Dimension getPreferredScrollableViewportSize()
        {
            return null;
        }

        public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2)
        {
            return 30;
        }
    }

}
