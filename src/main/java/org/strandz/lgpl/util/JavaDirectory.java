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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class is useful for recursively finding all the directories that
 * contain files of a particular type. The results are given in terms of
 * JavaPackages.
 *
 * @author Chris Murphy
 */
class JavaDirectory
{
    String rootDirectory;
    private List packageNames = new ArrayList();
    private List fileNames = new ArrayList();
    private List javaPackages = new ArrayList();
    private String appendClassPath;

    public JavaDirectory()
    {
    }

    /**
     * Set the the highest directory. All directories below will be sub-directories
     *
     * @param s the root directory
     */
    public void setRootDirectory(String s)
    {
        File dir = new File(s);
        rootDirectory = dir.getPath();
    }

    /**
     * Goes thru and collects all the packageNames and fileNames
     *
     * @param path
     */
    private void recurseThru(String path)
    {
        File file = new File(path);
        if(file.isDirectory())
        {
            String[] list = file.list();
            for(int i = 0; i <= list.length - 1; i++)
            {
                String innerPath = path + FileUtils.FILE_SEPARATOR + list[i];
                File innerFile = new File(innerPath);
                if(innerFile.isDirectory())
                {
                    packageNames.add(innerPath);
                    recurseThru(innerPath);
                }
                else
                {
                    fileNames.add(innerPath);
                }
            }
        }
        else
        {
            Err.error("expected directory");
        }
    }

    /**
     * Once the root directory has been defined this method can be used
     * to get all the sub-directories below it
     *
     * @return Iterator of Strings of the names of the packages
     */
    public Iterator getAllSubDirectories()
    {
        if(rootDirectory == null)
        {
            Err.error("must set root directory b4 getAllPackageNames");
        }
        packageNames.clear();
        recurseThru(rootDirectory);
        return packageNames.iterator();
    }

    /**
     * Given a list of paths, find all the directories below it that, by their
     * extension, could be called packages. Intended to be used with .java or
     * .class.
     *
     * @param extension Part after the dot on a fileName
     * @param paths     All the directories which could contain the files we are
     *                  are after directly below them. getAllSubDirectories() would
     *                  seem to provide a list that was recursively determined.
     * @return an Iterator of JavaPackages
     */
    public Iterator getDirysContainFilesExt(String extension, Iterator paths)
    {
        for(; paths.hasNext();)
        {
            String path = (String) paths.next();
            File file = new File(path);
            String[] list = file.list();
            for(int i = 0; i <= list.length - 1; i++)
            {
                File innerFile = new File(path + FileUtils.FILE_SEPARATOR + list[i]);
                if(innerFile.isFile())
                {
                    if(NameUtils.hasOneExtension(innerFile))
                    {
                        if(NameUtils.endsIn(extension, innerFile.getName()))
                        {
                            javaPackages.add(new JavaPackage(path));
                            break;
                        }
                    }
                }
            }
        }
        return javaPackages.iterator();
    }

    /*
    public String getLatestFile( String ext)
    {
      String result = null;
      Print.prList( fileNames, "JavaDirectory.getLatestFile()");
      return result;
    }
    */
}
