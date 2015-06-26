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

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.net.URL;

public class PortableImageIcon implements Icon
{
    private transient ClassLoader cl;
    private transient URL url;
    private String location;
    private String description;
    /**
     * Allows us to only create an ImageIcon when location
     * has changed AND it is required by one of the Icon
     * methods.
     */
    private boolean freshLocation = false;
    private ImageIcon imageIcon;

    private int whenNotFound;
    private boolean deadURL;

    public static final int STACK_TRACE = 1;
    public static final int STDOUT_MSG = 2;
    public static final int STDERR_MSG = 3;
    public static final int NOTHING = 4;

    public PortableImageIcon()
    {
        this(null, null);
    }

    public PortableImageIcon(String location, String description)
    {
        this(location, description, STACK_TRACE);
    }

    public PortableImageIcon(String location, String description, int whenNotFound)
    {
        this.description = description;
        this.whenNotFound = whenNotFound;
        cl = this.getClass().getClassLoader();
        setLocation(location);
    }

    public String getLocation()
    {
        return location;
    }

    public int getWhenNotFound()
    {
        return whenNotFound;
    }

    private void chkLocation(String location)
    {
        if(location != null && location.indexOf("images") == -1)
        {
            Err.error("Every location of an image should be in an images/ directory, was trying <" + location + ">");
        }
    }

    public void setLocation(String location)
    {
        chkLocation(location);
        //Err.pr( "setLocation being called with: " + location + ", currently: " + this.location);
        if(!Utils.equals(location, this.location))
        {
            ClassLoader cl = this.getClass().getClassLoader();
            url = cl.getResource(location);
            if(url == null)
            {
                fileNotFound(location);
                deadURL = true;
            }
            else
            {
                deadURL = false; //could go on url being null, but this more explicit
            }
            this.location = location;
            freshLocation = true;
        }
    }

    private void fileNotFound(String location)
    {
        if(whenNotFound == STDOUT_MSG)
        {
            System.out.println("Could not obtain a URL for <" + location + ">");
        }
        else if(whenNotFound == STDERR_MSG)
        {
            System.err.println("Could not obtain a URL for <" + location + ">");
            //Err.stack();
        }
        else if(whenNotFound == NOTHING)
        {
            //nufin
        }
        else if(whenNotFound == STACK_TRACE)
        {
            Err.error("Could not obtain a URL for <" + location + ">");
        }
        else
        {
            Err.error("Option " + whenNotFound + " unknown by fileNotFound() method");
        }
    }

    private ImageIcon requestImageIcon()
    {
        ImageIcon result;
        if(!deadURL && freshLocation)
        {
            imageIcon = new ImageIcon(url);
            //Err.pr( "Have loaded an image from disk for <" + url + ">");
            //imageIcon.setImageObserver( this);
            freshLocation = false;
        }
        result = imageIcon;
        return result;
    }

    /*
    private static class LocalImageObserver implements ImageObserver
    {
        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height)
        {
            Err.error("Not implemented");
            return false;
        }
    }
    */

    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        if(!deadURL && location != null)
        {
            requestImageIcon().paintIcon(c, g, x, y);
        }
    }

    public int getIconWidth()
    {
        int result = 0;
        if(!deadURL && location != null)
        {
            result = requestImageIcon().getIconWidth();
        }
        return result;
    }

    public int getIconHeight()
    {
        int result = 0;
        if(!deadURL && location != null)
        {
            result = requestImageIcon().getIconHeight();
        }
        return result;
    }

    public String getDescription()
    {
        //Err.pr( "getDescription() to ret: " + description);
        return description;
    }

    public void setDescription(String description)
    {
        //Err.pr( "setDescription() to set to: " + description);
        this.description = description;
    }

    public Image getImage()
    {
        return requestImageIcon().getImage();
    }

    public static PortableImageIcon createImageIcon( String imageName, String imageDesc)
    {
        return createImageIcon(imageName, imageDesc, STACK_TRACE);
    }

    /**
     * If you have a PortableImageIcon but do not call any icon type methods on it
     * then the image will still not have been read in. Here imagine such a
     * PortableImageIcon arriving at a place where it will be displayed. Each clone
     * that is made will create its own ImageIcon - ie read in a separate image -
     * which is necessary if multiple are to be displayed at the same time.
     */
    public static PortableImageIcon cloneImageIcon( PortableImageIcon portableImageIcon)
    {
        return createImageIcon( portableImageIcon.getLocation(),
            portableImageIcon.getDescription(), portableImageIcon.getWhenNotFound());
    }

    public static PortableImageIcon createImageIcon( String imageName, String imageDesc, int whenNotFound)
    {
        PortableImageIcon result;
        if(imageName == null)
        {
            Err.error("An imageName must be supplied");
        }
        else
        {
            //Could make sure are asking for a relative path, but will bomb out later anyway
        }
        result = new PortableImageIcon(imageName, imageDesc, whenNotFound);
        /*
        if(result != null)
        {
          result.setImageObserver( new IconImageObserver( result));
        }
        */
        return result;
    }
}
