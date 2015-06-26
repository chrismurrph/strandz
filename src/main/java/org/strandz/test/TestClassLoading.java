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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.SimpleClassLoader;
import org.strandz.lgpl.util.BeansUtils;
import org.strandz.lgpl.util.ObjectFoundryUtils;

import javax.swing.JPanel;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Just proving that cannot go onto XMLEncode a class that has
 * been dynamically loaded. XMLEncoder just doesn't know about
 * the class path.
 * <p/>
 * To get a .class open a cmd and:
 * C:\dynamic>javac C:/dynamic/AddressGUI.java
 */
public class TestClassLoading extends TestCase
{
    private String toLoad = "dynamic.AddressGUI";
    private String baseDir = "C:\\dev\\";
    private static String filename = "test_classload_output.xml";
    private BufferedOutputStream out = null;

    public static void main(String s[])
    {
        TestClassLoading obj = new TestClassLoading();
        obj.setUp();
        obj.testLoadClass();
    }

    protected void setUp()
    {
        BeansUtils.setDesignTime(true);
        try
        {
            out = new BufferedOutputStream(new FileOutputStream(filename));
        }
        catch(FileNotFoundException ex)
        {
            Err.error(ex);
        }
    }

    public static Test suite()
    {
        return new TestSuite(TestClassLoading.class);
    }

    public void testLoadClass()
    {
        ClassLoader cl = new SimpleClassLoader(baseDir);
        Class clazz = null;
        try
        {
            clazz = cl.loadClass(toLoad);
        }
        catch(ClassNotFoundException e)
        {
            Err.error(e);
        }

        Object obj = (JPanel) ObjectFoundryUtils.factory(clazz);
        // Print.pr( "Have loaded a " + obj.getClass().getName());
        tryEncode(obj);
    }

    private void tryEncode(Object obj)
    {
        XMLEncoder encoder = new XMLEncoder(out);
        encoder.writeObject(obj);
        encoder.close();
        Print.pr(filename + " has been encoded");
    }

    private void tryEncodeComplexly(Object obj, ClassLoader cl)
    {
        Class encoderClass = null;
        try
        {
            encoderClass = Class.forName("XMLEncoder", true, cl);
        }
        catch(ClassNotFoundException e1)
        {
            Err.error(e1);
        }

        XMLEncoder encoder = (XMLEncoder) ObjectFoundryUtils.factory(encoderClass);
        encoder.writeObject(obj);
        encoder.close();
        Print.pr(filename + " has been encoded");
    }
}
