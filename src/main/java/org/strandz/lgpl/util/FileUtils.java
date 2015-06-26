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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Manipulation using the file IO package (java.io). Here we are reading, writing
 * and creating files.
 */
public class FileUtils
{
    public static final String DOCUMENT_SEPARATOR = "\n";
    //public static boolean savingFile = false;
    public static final String UNIX_SEPARATOR = "\r";
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static List directoryNames = new ArrayList();
    private static List fileNames = new ArrayList();

    public static List getSubFileNames(String rootDirectory, String prefix)
    {
        List result = new ArrayList();
        List fileNames = getAllSubFiles(rootDirectory);
        for(Iterator iterator = fileNames.iterator(); iterator.hasNext();)
        {
            String completeFileName = (String) iterator.next();
            String fileName = NameUtils.findFileAtEndOfPath(completeFileName);
            if(fileName.startsWith(prefix))
            {
                result.add(fileName);
            }
            else
            {
                //Err.pr( fileName + " not prefixed by " + prefix);
            }
        }
        return result;
    }

    /**
     * Once the root directory has been defined this method can be used
     * to get all the files below it
     *
     * @return Iterator of Strings of the names of the packages
     */
    public static List getAllSubFiles(String rootDirectory)
    {
        if(rootDirectory == null)
        {
            Err.error("Must have a root directory to getAllPackageNames()");
        }
        directoryNames.clear();
        fileNames.clear();
        recurseThru(rootDirectory, false);
        return fileNames;
    }

    public static Iterator getAllSubDirectories(String rootDirectory)
    {
        if(rootDirectory == null)
        {
            Err.error("Must have a root directory to getAllPackageNames()");
        }
        directoryNames.clear();
        fileNames.clear();
        recurseThru(rootDirectory, false);
        return directoryNames.iterator();
    }

    /**
     * Goes thru and collects all the packageNames and fileNames
     *
     * @param path
     */
    private static void recurseThru(String path, boolean error)
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
                    directoryNames.add(innerPath);
                    recurseThru(innerPath, error);
                }
                else
                {
                    fileNames.add(innerPath);
                }
            }
        }
        else
        {
            if(error) Err.error("Expected directory, arg passed in " + path);
        }
    }

    public static void copyFile(File from, File to)
    {
        if(from.length() == 0)
        {
            //Err.pr("No point in copying an empty file: " + from);
            Err.alarm( "No point in copying an empty file: " + from);
            return;
        }

        BufferedOutputStream os = null;
        BufferedInputStream is = null;
        if(!to.exists())
        {
            boolean ok = true;
            try
            {
                ok = to.createNewFile();
            }
            catch(IOException ex)
            {
                Err.error(ex);
            }
            if(!ok)
            {
                Err.error("Strange that <" + to.getAbsolutePath()
                    + "> already exists when only here if have checked that doesn't!");
            }
        }
        try
        {
            os = new BufferedOutputStream(new FileOutputStream(to));
        }
        catch(FileNotFoundException ex)
        {
            Err.error(ex);
        }
        try
        {
            is = new BufferedInputStream(new FileInputStream(from));
        }
        catch(FileNotFoundException ex)
        {
            Err.error(ex);
        }

        int oneChar, count = 0;
        try
        {
            while((oneChar = is.read()) != -1)
            {
                os.write(oneChar);
                count++;
            }
            is.close();
            os.close();
        }
        catch(IOException ex)
        {
            Err.error(ex);
        }
        // Print.pr( "Have copied a file of size " + from.length() + " to a file of size " + to.length());
    }
    
    
    public static PrintWriter createPrintWriter(File file)
    {
        PrintWriter result = null;
        try
        {
            result = new PrintWriter( new FileWriter(file));
        }
        catch(IOException ex)
        {
            Err.error( ex);
        }
        return result;
    }

    /**
     * The version in File will not also create all the directories
     * leading up to.
     * 
     * @param file The new file to be created
     * @param diry Whether the file to be created is a directory 
     */
    public static void createNewFile(File file, boolean diry)
    {
        boolean fileCreated = false;
        while(!fileCreated)
        {
            try
            {
                boolean ok;
                if(diry)
                {
                    ok = file.mkdirs();
                }
                else
                {
                    ok = file.createNewFile();
                    fileCreated = true;
                }
                if(!ok)
                {
                    // Err.error( "File <" + file + "> already exists");
                    break;
                }
                else
                {
                    break;
                }
            }
            catch(IOException ex) // when file does not exist
            {
                // Err.pr( "Trying parent: " + file.getParentFile());
                createNewFile(file.getParentFile(), true);
            }
        }
    }

    private static String extendDir(String dir, String subDiry)
    {
        String result = dir + File.separator + subDiry;
        return result;
    }

    public static File createTempFile(String id, String tempSubDiry, String postFix)
    {
        File result = null;
        try
        {
            String dir = System.getProperty("java.io.tmpdir");
            String subdir = extendDir(dir, tempSubDiry);
            File sub = new File(subdir);
            if(!sub.exists())
            {
                sub.mkdir();
            }
            result = File.createTempFile(id, postFix, sub);
            // Err.pr( "$$ temp file: " + tmpFile.getAbsolutePath());
        }
        catch(IOException ex)
        {
            Err.error("Unable to create temporary file from " + id);
        }
        return result;
    }

    public static void safeDelete(File file, String tempSubDiry, String postFix)
    {
        File tmpFile = createTempFile(file.getName() + "_SafeDelete", tempSubDiry,
            postFix);
        if(file.length() != 0)
        {
            copyFile(file, tmpFile);
        }
        file.delete();
    }

    public static File[] stringToFile(String fileNames[])
    {
        File result[] = null;
        if(fileNames != null)
        {
            result = new File[fileNames.length];
            for(int i = 0; i < fileNames.length; i++)
            {
                String fileName = fileNames[i];
                result[i] = new File(fileName);
            }
        }
        return result;
    }

    public static List filesInDirectory(String fullPath, String extension)
    {
        JavaPackage jp = new JavaPackage(fullPath, extension);
        return jp.getFileNames();
    }

    public static String[] fileToString(File files[])
    {
        String result[] = new String[files.length];
        for(int i = 0; i < files.length; i++)
        {
            File file = files[i];
            result[i] = file.getAbsolutePath();
        }
        return result;
    }

    public static PrintStream createPrintStream(String fileName)
    {
        PrintStream result = null;
        if(!Utils.isBlank( fileName))
        {
            try
            {
                result = new PrintStream(new FileOutputStream(fileName));
            }
            catch(FileNotFoundException e)
            {
                Err.error(e);
            }
        }
        return result;
    }
    
    public static PrintStream createPrintStream( File file)
    {
        PrintStream result = null;
        Assert.notNull( file, "Cannot obtain a PrintStream from a null file");
        try
        {
            result = new PrintStream(new FileOutputStream( file));
        }
        catch(FileNotFoundException e)
        {
            Err.error(e);
        }
        return result;
    }

    public static StringBuffer readFile(File file, boolean ignoreNotFound)
    {
        StringBuffer result = null;
        InputStream is = null;
        StringWriter stringWriter = new StringWriter();
        try
        {
            is = new FileInputStream(file);
        }
        catch(FileNotFoundException ex)
        {
            if(!ignoreNotFound)
            {
                Err.error("Could not open <" + file.getAbsolutePath() + "> for reading");
            }
        }
        if(is != null)
        {
            int c = 0;
            while(c != -1)
            {
                try
                {
                    c = is.read();
                }
                catch(IOException ex)
                {
                    Err.error("Could not read from <" + file.getAbsolutePath());
                }
                stringWriter.write(c);
            }
            result = stringWriter.getBuffer();
        }
        return result;
    }
        
    public static void writeFile( String contents, String fileName)
    {
        BufferedWriter outputStream = null;
        try
        {
            outputStream = new BufferedWriter( new FileWriter( fileName));
            outputStream.write( contents);
        }
        catch(IOException e)
        {
            Err.error(e);
        }
        finally
        {
            if(outputStream != null)
            {
                try
                {
                    outputStream.close();
                }
                catch(IOException e)
                {
                    Err.error(e);
                }
            }
        }
    }

    public static File mostRecentFile(String dirName, String ext)
    {
        File dir = new File(dirName);
        if(!dir.isDirectory())
        {
            Err.error(dirName + " is not a directory");
        }

        File files[] = dir.listFiles(new FileExtensionFilter(ext, "Java source files"));
        long mostRecentStamp = 0L;
        int mostRecentIndex = 0;
        for(int i = 0; i < files.length; i++)
        {
            if(files[i].lastModified() > mostRecentStamp)
            {
                mostRecentStamp = files[i].lastModified();
                mostRecentIndex = i;
            }
        }
        return files[mostRecentIndex];
    }

    public static boolean directoryExists(String dirName)
    {
        boolean result = false;
        File dir = new File( dirName);
        if(dir.exists())
        {
            if(dir.isDirectory())
            {
                result = true;
            }
        }
        return result;
    }

    public static class FileExtensionFilter extends javax.swing.filechooser.FileFilter implements FileFilter
    {
        String ext;
        String desc;

        public FileExtensionFilter(String ext, String desc)
        {
            this.ext = ext;
            this.desc = desc;
        }

        public boolean accept(File pathname)
        {
            boolean result = false;
            String name = pathname.getName();
            if(name.endsWith("." + ext))
            {
                result = true;
            }
            return result;
        }

        public String getDescription()
        {
            return desc;
        }
    }
}
