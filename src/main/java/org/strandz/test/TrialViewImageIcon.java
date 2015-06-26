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
package org.strandz.test;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.DisplayUtils;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.net.URL;

public class TrialViewImageIcon
{
    public static void main(String s[])
    {
        new TrialViewImageIcon();
    }

    /**
     * Can only see the image when constructed from the url, not from a String.
     * Thus we can see why our XMLEncoded is not displaying the images. The
     * conclusion is that we need an ImageIcon that actually has a URL in it -
     * there is no way this will happen from text alone.
     * <p/>
     * When pass in String it is interpretted as a fileName. When experiment with
     * fileText, then it displays no problem. Thus there is a 'text solution'
     * (ie. works with XMLEncoder) that will point directly at a file. Trouble
     * is we need to have only "images/Calendar.gif" in XML, and it should look
     * into whatever jar are available for web start to work. Looking into the
     * available jars is the job of a classloader.
     * <p/>
     * Thus perhaps could use our own simple class that only has whatever it needs
     * to access as text, "images/Calendar.gif", and has a transient property, so
     * can getIcon(). So we've invented a org.strandz.widgets.PortableImageIcon.
     * <p/>
     * The problem we have now is how do we attach the org.strandz.widgets.PortableImageIcon to the JLabel
     * or JButton? Easy - Icon is an interface, so org.strandz.widgets.PortableImageIcon will just implement
     * it.
     * <p/>
     * Here we test out the need for a classloader by seeing if
     * the following work with just "images/Calendar.gif":
     * portableTxt with straight text - result is not good
     * portableTxt with url intervention - result is not good
     * portableTxt with classloader intervention - result is good
     * <p/>
     * When using classloader (see "URL using classloader: ") urlTxt is what the
     * URL looks like. The classloader achieves resolving to urlTxt from
     * portableTxt. We need an Icon that when constructed will have its own
     * classloader. Thus whereever XMLEncoder is it will be able to find images.
     * <p/>
     * Thus move to org.strandz.test.TrialSdzImageIcon, but while we are here, does the resolving
     * work as well for an image on the filesystem as for an image in a jar?
     * Changed setup and answer is NO - which makes sense as you wouldn't think
     * that the JVM was a way to access the file system.
     */
    TrialViewImageIcon()
    {
        String urlTxt = "jar:file:/c:/core/lib/images.jar!/images/Calendar.gif";
        String fileTxt = "/c:/core/images/Calendar.gif";
        String portableTxt = "images/Calendar.gif";
        String constructTxt = portableTxt;
         /**/
        ClassLoader cl = this.getClass().getClassLoader();
        URL location = cl.getResource(portableTxt);
        Err.pr("URL using classloader: " + location);
        /*
        URL location = null;
        try
        {
          location = new URL( urlTxt);
        }
        catch (MalformedURLException e)
        {
          Err.error( e );
        }
        */
        ImageIcon imageIcon = new ImageIcon(location);
        JLabel label = new JLabel();
        label.setIcon(imageIcon);
        DisplayUtils.displayInDialog(label);
    }
}
