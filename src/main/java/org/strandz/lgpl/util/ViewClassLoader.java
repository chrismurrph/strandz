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

/**
 * This class extends SpecialClassLoader to make instrumentation style
 * debugging easier. ie. to differentiate between the data class loader
 * and the view class loader
 *
 * @author Chris Murphy
 */
public class ViewClassLoader extends SpecialClassLoader
{
    private static int constructedTimes;

    public ViewClassLoader(
        ClassLoader parent,
        String basePath,
        String packagePrefix,
        boolean debugging)
    {
        super(parent, basePath, packagePrefix, debugging);
        id = ++constructedTimes;
        pr(
            "##CONSTRUCTED ViewClassLoader with path " + basePath
                + " package prefix " + packagePrefix + " ID " + id);
        // pr( "Special files are [ID " + id + "]:");
        // pr( specialFiles.get( 0).toString());
        if(id == 0)
        {
            Err.stack();
        }
    }

    /**
     * For debugging, tells us the type, the path and the ID
     *
     * @return description of this class
     */
    public String toString()
    {
        String result = null;
        result = "ViewClassLoader with path " + basePath + " ID " + id;
        return result;
    }
}
