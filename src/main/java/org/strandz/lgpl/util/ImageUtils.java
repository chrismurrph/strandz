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
import java.awt.Color;
import java.awt.Image;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;

public class ImageUtils
{
    /**
     * Creates a square image of a particular colour. If someone knows how to do the
     * same with a circle...
     * 
     * Attribution:
     * http://www.javaworld.com/javaworld/javatips/jw-javatip16.html 
     * 
     * @param width Width of the required image
     * @param height Height of the required image
     * @param colour Colour of the required image
     * @param someComponent  
     * @return The image
     */
    public static Image createMemoryImage(int width, int height, Color colour, JComponent someComponent)
    {
        Image result;
        int[] pixels = new int[width * height];
        int arraySize = 3;
        Color colorArray[] = new Color[arraySize];
        for(int i = 0; i < arraySize; i++)
        {
            colorArray[i] = colour;
        }
        byte reds[] = new byte[arraySize];
        byte greens[] = new byte[arraySize];
        byte blues[] = new byte[arraySize];
        for(int i = 0; i < arraySize; i++)
        {
            reds[i] = (byte) colorArray[i].getRed();
            greens[i] = (byte) colorArray[i].getGreen();
            blues[i] = (byte) colorArray[i].getBlue();
        }
        for(int index = 0, y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                pixels[index++] = (byte) (x % arraySize);
            }
        }
        result = someComponent.createImage(new MemoryImageSource(width, height,
                                                   new IndexColorModel(8, arraySize, reds, greens, blues),
                                                   pixels, 0, width));
        return result;
    }
}
