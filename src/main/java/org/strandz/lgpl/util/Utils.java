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

import org.strandz.lgpl.note.JDONote;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JList;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Color;
import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * This static class is the place to put small utility methods that only
 * rely on Java classes. Note the classes with similar names pXXXUtils,
 * where such methods are grouped by function.
 *
 * @author Chris Murphy
 */
public class Utils
{
    public static final String WHITE_SPACE = "    ";
    public static final String NEWLINE = System.getProperty("line.separator");
    public final static String xml = "xml";
    public static StringListComparator stringListComparator = new StringListComparator();
    public static final Integer ZERO = new Integer(0);
    public static final Integer ONE = new Integer(1);
    public static final Integer TWO = new Integer(2);
    public static final Integer THREE = new Integer(3);
    public static final List EMPTY_ARRAYLIST = new ArrayList();
    public static final int UNSET_INT = -99;
    public static final int UNSET_INT_POSITIVE = 99;
    public static final long UNSET_LONG = -99l;
    //public static final int DEC_PL_SCALE_1 = 1;
    //public static final int DEC_PL_SCALE_2 = 2;

    public static final String OS_NAME = System.getProperty("os.name");
    public static final String RUBY_ENGINE_NAME = "jruby";

    //
    public static boolean debug = false;
    public static boolean visibleMode = false;
    private static int times;

    public static IdentifierI newIdentifier( String name)
    {
        return new StringIdentifier( name);
    }

    public static BigDecimal createDecimal( String numberStr, int numDecPl)
    {
        BigDecimal result;
        result = new BigDecimal( numberStr).setScale( numDecPl, BigDecimal.ROUND_HALF_UP);
        return result;
    }
    
    public static BigDecimal createDecimal( double divided, int numDecPl)
    {
        BigDecimal result;
        result = new BigDecimal( divided).setScale( numDecPl, BigDecimal.ROUND_HALF_UP);
        return result;
    }

    private static void divByZeroTest()
    {
        int num = 10;
        try
        {
            int ans = num / 0;
        }
//        catch( Error ex)
//        {
//            Err.pr( "Got exception");
//        }
        finally
        {
            Err.pr("In finally");
        }
    }

    public static List fromKeyFindActionNames(JComponent control)
    {
        List result = null;
        Err.error( "Not completed method");
        ActionMap actionMap = control.getActionMap();
        InputMap inputMap = control.getInputMap(JComponent.WHEN_FOCUSED);
        return result;
    }

    public static String presentNullAsBlankStr(Integer number)
    {
        String result = "";
        if(number != null)
        {
            result = number.toString();
        }
        return result;
    }

    public static String presentNullAsBlank(String value)
    {
        String result = "";
        if(value != null)
        {
            result = value;
        }
        return result;
    }

    public static void prefixBeginning(List names, String preBit)
    {
        for(int i = 0; i < names.size(); i++)
        {
            String element = (String) names.get(i);
            String appended = new StringBuffer().append(preBit).append(element).toString();
            names.set(i, appended);
        }
    }

    public static String rightPadSpace(String str, int size)
    {
        return rightPad(str, size, ' ');
    }

    public static String leftPadSpace(String str, int size)
    {
        return leftPad(str, size, ' ');
    }
    
    public static String leftPadZero(String str, int size)
    {
        return leftPad(str, size, '0');
    }

    public static String rightPad(String str, int size, char ch)
    {
        if(str.length() > size)
        {
            Err.error("Need to increase size to rightPad to, to " + str.length() + " for <"
                + str + ">, is currently " + size);
        }

        StringBuffer buf = new StringBuffer(str);
        for(int i = 0; i < size - str.length(); i++)
        {
            NameUtils.appendToStringBuffer(buf, ch);
        }
        return buf.toString();
    }

    public static String leftPad(String str, int size, char ch)
    {
        if(str.length() > size)
        {
            Err.error("Need to increase size to leftPad to, to " + str.length() + " for <"
                + str + ">");
        }

        StringBuffer buf = new StringBuffer();
        for(int i = 0; i < size - str.length(); i++)
        {
            NameUtils.appendToStringBuffer(buf, ch);
        }
        buf.append(str);
        return buf.toString();
    }

    public static int relativeRank(Object objs[], Object first, Object second)
    {
        int result = 0;
        int firstIndex = -1;
        int secondIndex = -1;
        for(int i = 0; i < objs.length; i++)
        {
            if(objs[i].equals(first))
            {
                firstIndex = i;
            }
            if(objs[i].equals(second))
            {
                secondIndex = i;
            }
            if(firstIndex != -1 && secondIndex != -1)
            {
                break;
            }
        }
        if(firstIndex > secondIndex)
        {
            result = 1;
        }
        else if(firstIndex < secondIndex)
        {
            result = -1;
        }
        return result;
    }
    
    public static Object[] toArray(List list, Object array[], int start, int end)
    {
        return toArray( list, array, start, end, -1);
    }

    /**
     * Similar to ArrayList.toArray(), but this method will return a range
     * from the passed in list
     *
     * @param list
     * @param array
     */
    public static Object[] toArray(List list, Object array[], int start, int end, int skip)
    {
        Object[] result = array;
        int j = 0;
        for(int i = start; i <= end; i++, j++)
        {
            Object o = null;
            if(skip == -1 || i != skip)
            {
                o = list.get(i);
            }
            result[j] = o;
        }
        //Print.prArray( result, "toArray() result");
        return result;
    }

    /**
     * Similar to ArrayList.toArray(), but this method will return a range
     * from the passed in list
     *
     * @param list
     */
    public static Object[] toArray(List list, int start, int end)
    {
        int sizeOfResult = start - end + 1;
        Object[] result = toArray(list, new Object[sizeOfResult], start, end);
        return result;
    }

    public static Object[] updateArrayFromList(List list, Object array[])
    {
        int i = 0;
        for(Iterator iter = list.iterator(); iter.hasNext(); i++)
        {
            Object obj = iter.next();
            array[i] = obj;
            // Err.pr( "Pos " + i + " been assigned " + obj);
        }
        return array;
    }

    public static List findDifferentElements( List list1, List list2)
    {
        List result = new ArrayList();
        for (int i = 0; i < list1.size(); i++)
        {
            Object o1 =  list1.get(i);
            if(! list2.contains( o1))
            {
                result.add( o1);
            }
        }
        for (int i = 0; i < list2.size(); i++)
        {
            Object o2 =  list2.get(i);
            if(! list1.contains( o2))
            {
                result.add( o2);
            }
        }
        return result;
    }

    public static List findInFirstListOnly( List list1, List list2)
    {
        List result = new ArrayList();
        for (int i = 0; i < list1.size(); i++)
        {
            Object o1 =  list1.get(i);
            if(! list2.contains( o1))
            {
                result.add( o1);
            }
        }
        return result;
    }


    public static List findCommonElements(List lists)
    {
        List result = new ArrayList();
        // Don't need a set, and get ordering problems when use one, as the
        // iterator returns elements in no particular order
        // HashSet set = new HashSet();
        List set = new ArrayList();
        for(Iterator iter = lists.iterator(); iter.hasNext();)
        {
            IterableList list = (IterableList) iter.next();
            for(Iterator iterator = list.iterator(); iterator.hasNext();)
            {
                Object element = iterator.next();
                set.add(element);
            }
        }
        for(Iterator iter = set.iterator(); iter.hasNext();)
        {
            Object element = iter.next();
            boolean elementInAllLists = true;
            for(Iterator iterator = lists.iterator(); iterator.hasNext();)
            {
                IterableList list = (IterableList) iterator.next();
                if(!list.contains(element))
                {
                    elementInAllLists = false;
                    break;
                }
            }
            if(elementInAllLists)
            {
                result.add(element);
            }
        }
        return result;
    }

    /**
     * More intuitive to wrap the java method around this one.
     * Hmm - this method silly - just use instanceof if have the object itself!
     *
     * @param obj
     * @param beAssignableFrom
     */
    public static boolean instanceOf(Object obj, Class beAssignableFrom)
    {
        boolean result = false;
        if(obj instanceof Class)
        {
            Err.error("First param to instanceOf s/be an object, not a Class, use isClassOfType() instead");
        }
        if(obj != null)
        {
            result = isClassOfType(obj.getClass(), beAssignableFrom);
        }
        return result;
    }

    public static boolean isClassOfType(Class clazz, Class beAssignableFrom)
    {
        if(beAssignableFrom == null)
        {
            Err.error("beAssignableFrom must not be null");
        }
        boolean result = false;
        if(clazz != null)
        {
            result = beAssignableFrom.isAssignableFrom(clazz);
            if(!result)
            {// Err.pr( "An instance of a " + obj.getClass() + ", not assignable from a " + beAssignableFrom);
            }
            else
            {// Err.pr( "AFFIRM An instance of a " + obj.getClass() + ", IS assignable from a " + beAssignableFrom);
            }
        }
        return result;
    }


    public static Class checkAllSameClass(List list)
    {
        Class result = null;
        Class previous = null;
        if(list.size() >= 1)
        {
            previous = list.get(0).getClass();
            result = previous;
        }
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Class clazz = iter.next().getClass();
            if(previous != clazz)
            {
                result = null;
                // Err.pr( "Got both " + clazz + " and " + previous);
                break;
            }
            previous = clazz;
        }
        return result;
    }

    public static final boolean EXCLUDE = false;
    public static final boolean INCLUDE = true;
    
    /**
     * Return all the classes in list which are of type. The result will include all
     * subclasses of type
     */
    public static List getSubList(List list, Class type)
    {
        return getSubList(list, type, INCLUDE);
    }
    
    /**
     * Return all the classes in list which are NOT of type if ofType is set to false.
     * The result will [include/Not include] all subclasses of type.
     */
    public static List getSubList(List list, Class type, boolean ofType)
    {
        List result = new ArrayList();
        for(Iterator en = list.iterator(); en.hasNext();)
        {
            Object obj = en.next();
            if(type.isAssignableFrom(obj.getClass()))
            {
                if(ofType)
                {
                    result.add(obj);
                }
            }
            else
            {
                if(!ofType)
                {
                    result.add(obj);
                }
            }
        }
        return result;
    }

    public static List getSubList(Object objs[], Class type)
    {
        return getSubList(objs, type, true);
    }

    public static List getSubList(Object objs[], Class type, boolean ofType)
    {
        List result = new ArrayList();
        for(int i = 0; i < objs.length; i++)
        {
            Object obj = objs[i];
            if(type.isAssignableFrom(obj.getClass()))
            {
                if(ofType)
                {
                    result.add(obj);
                }
            }
            else
            {
                if(!ofType)
                {
                    result.add(obj);
                }
            }
        }
        return result;
    }

    public static void checkParentage(List controls)
    {
        for(Iterator iter = controls.iterator(); iter.hasNext();)
        {
            Component element = (Component) iter.next();
            Component parent = element.getParent();
            if(parent == null)
            {
                Err.error("control " + element + " has no parent");
            }
            else
            {
                if(parent.getName() == null)
                {
                    Err.error("parent " + parent + " has no name");
                }
            }
        }
    }

    public static boolean validList(List list)
    {
        boolean result = true;
        if(list == null || list.isEmpty())
        {
            result = false;
        }
        return result;
    }

    /**
     * Get rid of this as s/be equivalent to equals()
     *
     * @param obj1
     * @param obj2
     */
    /*
    public static boolean differentNotNull(Object obj1, Object obj2)
    {
        boolean result = true;
        if(obj1 == null && obj2 == null)
        {
            result = false; // not different
        }
        else if(obj1 == null || obj2 == null)
        {
            result = false; // not both [not null]
        }
        else
        {
            result = !obj1.equals(obj2);
        }
        return result;
    }
    */

    /**
     * Saves the hassle of doing the checking for null yourself.
     *
     * @param obj1
     * @param obj2
     */
    public static boolean equals(Object obj1, Object obj2)
    {
        boolean result = false;
        if(obj1 == null && obj2 == null)
        {
            result = true;
        }
        else if(obj1 == null || obj2 == null)
        {
        }
        else
        {
            result = obj1.equals(obj2);
        }
        return result;
    }
    
    public static boolean equals(boolean b1, boolean b2)
    {
        return b1 == b2;
    }

    public static boolean equals(int i1, int i2)
    {
        return i1 == i2;
    }

    public static int hashCode(int result, Object obj)
    {
        result = 37 * result + (obj == null ? 0 : obj.hashCode());
        return result;
    }

    public static int hashCode(int result, int obj)
    {
        result = 37 * result + obj;
        return result;
    }

    public static int hashCode(int result, boolean b)
    {
        result = 37;
        if(b)
        {
            result++;
        }
        return result;
    }

    /**
     * For people who can't see images I presume the text that comes out
     * from here will be read out.
     *
     * @param path The name of the image file
     * @return Something without the images/ and .gif
     */
    public static String fromImageToReadable(String path)
    {
        return path;
    }

    public static boolean bothNull(Object obj1, Object obj2)
    {
        boolean result = obj1 == null && obj2 == null;
        return result;
    }

    public static boolean oneOnlyNotNull(Object objects[])
    {
        boolean result = false;
        int timesNotNull = 0;
        for(int i = 0; i < objects.length; i++)
        {
            Object object = objects[i];
            if(object != null)
            {
                timesNotNull++;
            }
        }
        if(timesNotNull == 1)
        {
            result = true;
        }
        return result;
    }
    
    public static List asArrayList(Object[] objects1, Object[] objects2)
    {
        List list = new ArrayList( objects1.length);
        return asArrayList( objects1, objects2, list);
    }
    
    public static List asArrayList(Object[] objects1)
    {
        List list = new ArrayList( objects1.length);
        return asArrayList( objects1, list);
    }
    
    public static List asArrayList(Object[] objects1, List list)
    {
        Object objects2[] = new Object[0];
        return asArrayList( objects1, objects2, list);
    }

    public static List asArrayList(Object[] objects1, Object[] objects2, List list)
    {
        List result = list;
        if(result.size() == 0)
        {
            for(int i = 0; i < objects1.length; i++)
            {
                list.add(null);
            }
            for(int i = 0; i < objects2.length; i++)
            {
                list.add(null);
            }
        }
        if(result.size() != objects1.length + objects2.length)
        {
            Err.error("Calling asArrayList() and the ArrayList args " + objects1.length + " plus " + objects2.length + " do not make up the same size as the array " + result.size());
        }
        for(int i = 0; i < objects1.length; i++)
        {
            Object object = objects1[i];
            result.set(i, object);
        }
        for(int i = 0; i < objects2.length; i++)
        {
            Object object = objects2[i];
            result.set(i, object);
        }
        return result;
    }

    /**
     * @param data a list of lists, where the initial list represents a row
     * @param col
     */
    public static List obtainColumn(List data, int col)
    {
        List result = new ArrayList();
        for(Iterator iterator = data.iterator(); iterator.hasNext();)
        {
            List list = (List) iterator.next();
            result.add(list.get(col));
        }
        return result;
    }

    public static boolean isTop(Object obj, List list, Comparator comparator)
    {
        boolean result = false;
        //Err.pr( "o: " + obj.getClass().getName());
        //Print.prList( column, "Need find top one from");
        //Utils.chkNoNulls( list, "a col of data"); - there ARE nulls, but ok
        //
        Object maxObj = Collections.max(list, comparator);
        //Collections.sort( list, comparator);
        //Object maxObj = list.get( 0);
        if(maxObj.equals(obj))
        {
            //Err.pr( "maxObj is " + maxObj + " when was using column comparator " + comparator);
            result = true;
        }
        return result;
    }

    public static int reverseInt(int toReverse)
    {
        int result = 0;
        if(toReverse != 0)
        {
            result = Math.abs(toReverse);
            result = -result;
        }
        return result;
    }

    public static List breakIntoClumpsOf(int clumpSize, List bigList)
    {
        List result = new ArrayList();
        List accumulatingList = new ArrayList();
        for(Iterator iter = bigList.iterator(); iter.hasNext();)
        {
            Object element = iter.next();
            accumulatingList.add(element);
            if(accumulatingList.size() == clumpSize)
            {
                result.add(new ArrayList(accumulatingList));
                accumulatingList.clear();
            }
        }
        result.add(new ArrayList(accumulatingList));
        return result;
    }

    public static List formList( Object object)
    {
        List result = new ArrayList();
        result.add(object);
        return result;
    }

    public static Collection formCollection(Object object)
    {
        Collection result = new ArrayList();
        result.add( object);
        return result;
    }
    
    public static List<String> formList(String s)
    {
        List<String> result = new ArrayList<String>();
        result.add(s);
        return result;
    }

    public static String[] formArray(String s)
    {
        String[] result = new String[1];
        result[0] = s;
        return result;
    }
    
    /**
     * See if the key is equal to any of the keys in the map
     */
    public static void diagnoseMapKeyProblem(Object key, Map map)
    {
        Set keys = map.keySet();
        for(Iterator iterator = keys.iterator(); iterator.hasNext();)
        {
            Object o = iterator.next();
            if(o.equals(key))
            {
                Err.pr("Two keys equal: " + o + ", " + key);
            }
        }
    }

    public static List obtainListOfType( List listOflists, Class clazz)
    {
        List result = null;
        //for(int i = listOflists.size() - 1; i >= 0; i--)
        for(int i = 0; i <= listOflists.size() - 1; i++)
        {
            List list = (List)listOflists.get( i);
            if(!list.isEmpty() && instanceOf( list.get(0), clazz))
            {
                result = list;
            }
        }
        return result;
    }

    public static void putFirstLast(List list)
    {
        list.add( list.get( 0));
        list.remove( 0);
    }

    public static void assignArray(Object[] target, Object[] source)
    {
        if(source.length >= target.length)
        {
            Err.error( "Target array is not large enough to take " + source.length + " items");
        }
        for(int i = 0; i < source.length; i++)
        {
            Object sourceItem = source[i];
            target[i] = sourceItem;
        }
    }

    /**
     * To stop IntelliJ graphics going silly - its just a marker!
     * @return
     */
    public static boolean getFalse()
    {
        return false;
    }

    public static List copyList(List list, int skipFirst)
    {
        List result = new ArrayList( list);
        int numRemoved = 0;
        for(int i = 0; i < skipFirst; i++)
        {
            Object toRemove = result.get( i-numRemoved);
            boolean ok = result.remove( toRemove);
            if(ok)
            {
                numRemoved++;
            }
            else
            {
                Print.prList( list, "From this list");
                Err.error( "Could not delete " + toRemove);
            }
        }
        return result;
    }

    public static List copyList( List list)
    {
        return copyList( list, 0);
    }

    public static boolean greaterThan( BigDecimal bigDecimal, BigDecimal bigDecimal1)
    {
        return bigDecimal.compareTo( bigDecimal1) > 0;
    }

    public static boolean greaterThan( Date date1, Date date2)
    {
        return date1.compareTo( date2) > 0;
    }

    public static boolean lessThan( BigDecimal bigDecimal, BigDecimal bigDecimal1)
    {
        return bigDecimal.compareTo( bigDecimal1) < 0;
    }

    public static boolean containsSameType(Object o, Collection collection)
    {
        boolean result = false;
        for(Iterator iterator = collection.iterator(); iterator.hasNext();)
        {
            Object o1 = iterator.next();
            if(o1.getClass() == o.getClass())
            {
                result = true;
                break;
            }
        }
        return result;
    }

    public static int ignoreCaseHashCode(String colName)
    {
        String upperCaseVersion = colName.toUpperCase();
        return upperCaseVersion.hashCode();
    }

    public static String listToStr( List<String> stringList)
    {
        StringBuffer result = new StringBuffer();
        int size = stringList.size();
        for (int i = 0; i < size; i++)
        {
            String s = stringList.get(i);
            result.append( s);
            if(i < size-1)
            {
                result.append( ",");
            }
        }
        return result.toString();
    }

    /*
    public static ImageIcon createImageIcon( String imageName)
    {
      ClassLoader cl = System.class.getClassLoader();
      //ClassLoader cl = this.getClass().getClassLoader();
      ImageIcon result = new ImageIcon( imageName);
      if(result == null || result.getImage() == null)
      {
        Err.error( "Could not find the image " + imageName);
      }
      return result;
    }
    */

    public static class StringListComparator implements Comparator
    {
        public int compare(Object obj1, Object obj2)
        {
            List list1 = (List) obj1;
            List list2 = (List) obj2;
            int listSize = list1.size();
            int result = listSize - list2.size();
            if(result == 0)
            {
                for(int i = 0; i < listSize; i++)
                {
                    String first = (String) list1.get(i);
                    String second = (String) list2.get(i);
                    int diff = first.compareTo(second);
                    if(diff != 0)
                    {
                        result = diff;
                        break;
                    }
                }
            }
            return result;
        }
    }

    public static boolean listIsAllNulls(List list)
    {
        boolean result = true;
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Object element = iter.next();
            if(element != null)
            {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * Method to get an XMLEncoded object out from it's jar file
     *
     * @param resourcename The name of a resource (basically a file name) in a jar file
     * @param classLoaded Any class that has already been classloaded
     * @param dt Before JavaBeans are created we may want to set DesignTime to true
     * @return An instance of a Java class (XMLEncoded)
     */
    public static Object loadXMLFromResource(String resourcename, Object classLoaded, boolean dt)
    {
        return loadXML(null, resourcename, classLoaded, dt);
    }

    /**
     * Loading from file is only used by unit tests, XMLMemoryGUI and
     * Strandz that are generated (currently this is the case).
     * The rest is done by the policy which is to always read in from
     * jar files.
     */
    public static Object loadXMLFromFile(String filename, boolean dt)
    {
        Object result;
        InputStream s = null;
        if(filename == null)
        {
            Err.error("No filename specified so cannot loadXMLFromFile()");
        }
        try
        {
            s = new FileInputStream(filename);
        }
        catch(FileNotFoundException ex)
        {
            Err.error(ex, filename);
        }
        result = loadXMLFromStream(s, dt, filename);
        return result;
    }

    /**
     * Used for reading in an XML file. Usually used for the DT Beans file.
     * From an ant file, which shows complexity that needed to go.
     * The last copy in this target is a bit 'backwards'. This is because StartupDev
     * running from IntelliJ uses the jar file in ${jars.from.location}. Basically
     * the dt info is put into a jar, and that is all we actually need to do for
     * deployment. The next 'non web-start' convenience action is that this jar is
     * copied into the directory from which we get all the jars when say we are
     * running from IntelliJ. The way the code works is that Utils.loadXMLFromResource() will
     * grab from the jar in preference to the file ...
     * Could do with a pool of these files, so don't read in the same one again.
     */
    private static Object loadXML(final String filename, final String portableFilename, Object classLoaded, final boolean dt)
    {
        Object result = null;
        InputStream s = null;
        String nameLoaded = null;
        if(classLoaded == null)
        {
            Err.error("In loadXML must have a classLoaded object as a param");
        }
        if(portableFilename == null && filename != null)
        {
            //Now if alter DT by using Designer or manually then must also jar it.
            //TODO Designer to automatically jar the file up (equiv to the ant task
            //but lets not create a new JVM)
            //TODO check that this method is being called by the generated code
            //Policy is that always read from jar files.
            Err.error("NOT reading in from a jar file, but from " + filename);
        }
        else if(filename == null && portableFilename != null)
        {
            s = classLoaded.getClass().getClassLoader().getResourceAsStream(portableFilename);
            if(s == null)
            {
                Err.error("Could not locate a jar file with the resource " + portableFilename + " in it");
            }
            nameLoaded = portableFilename;
        }
        else if(portableFilename == null && filename == null)
        {
            Err.error("Cannot loadXMLFromResource without any kind of file name reference");
        }
        else //have both, so prefer the filename as more convenient during development NOT NOW
        {
            /*
            //Policy change. Now have easy ant scripts, everything always comes from a jar file
            try
            {
              s = new FileInputStream( filename );
              Err.error( "Probably in DEV, and have (in preference to jar resource) read in the DT file " + filename);
            }
            catch (FileNotFoundException ex)
            {
              s = classLoaded.getClass().getClassLoader().getResourceAsStream( portableFilename );
            }
            */
            s = classLoaded.getClass().getClassLoader().getResourceAsStream(portableFilename);
            nameLoaded = portableFilename;
        }
        if(s == null)
        {
            /*
            Err.error( "Could not find the file <" + filename + "> as a local file or <" +
                portableFilename + "> as a jar resource");
            */
            Err.error("Could not find <" + portableFilename + "> as a jar resource");
        }
        result = loadXMLFromStream(s, dt, nameLoaded);
        return result;
    }

    private static Object loadXMLFromStream(InputStream s, final boolean dt, final String filename)
    {
        Object result;
        if(dt) BeansUtils.setDesignTime(true);

        XMLDecoder decoder = new XMLDecoder(s);
        decoder.setExceptionListener(new ExceptionListener()
        {
            public void exceptionThrown(Exception exception)
            {
                if (dt) BeansUtils.setDesignTime( false);
                Err.error(exception, "While trying to parse " + filename);
            }
        });
        /*
        times++;
        Err.pr( "About to read using " + filename + " times " + times );
        if (times == 0)
        {
          Err.stack();
        }
        */
        result = decoder.readObject();
        //Err.pr( "Got result " + result );
        if (dt) BeansUtils.setDesignTime( false );
        return result;
    }

    // Should really work for same number of lists as in extent
    public static void mergeIntoFrom(Object mergeInto, Object mergeFrom, Object deletes)
    {
        if(!(mergeInto instanceof List))
        {
            Err.error("Time to integrate with extent.VisibleExtent");
        }
        if(!(mergeFrom instanceof List))
        {
            Err.error("Time to integrate with extent.VisibleExtent");
        }
        if(!(deletes instanceof List))
        {
            Err.error("Time to integrate with extent.VisibleExtent");
        }

        List into = (List) mergeInto;
        List from = (List) mergeFrom;
        List del = (List) deletes;
        for(Iterator iter = from.iterator(); iter.hasNext();)
        {
            Object element = iter.next();
            int index = into.indexOf(element);
            if(index == -1)
            {
                into.add(element);
            }
            else
            {
                into.set(index, element);
            }
        }
        for(Iterator iter = del.iterator(); iter.hasNext();)
        {
            Object element = iter.next();
            int index = into.indexOf(element);
            if(index == -1)
            {/*
               * This happened when there were two Carolines:
               Err.error( "Should never be deleting a value that " +
               "did not exist in the original list: " + element);
               */
            }
            else
            {
                into.remove(index);
            }
        }
        if(into instanceof DebugList)
        {// Err.pr( "#### merged into DebugList ID: " + ((DebugList)into).id);
        }
        if(from instanceof DebugList)
        {// Err.pr( "#### merged from DebugList ID: " + ((DebugList)from).id);
        }
    }

    public static boolean containsIgnoreCase(List list, String strIn)
    {
        boolean result = false;
        if(strIn != null) // don't look for a null
        {
            if(list != null) // no list, then can't exist on it
            {
                for(Iterator iter = list.iterator(); iter.hasNext();)
                {
                    String str = (String) iter.next();
                    if(strIn.equalsIgnoreCase(str))
                    {
                        result = true;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static void prMetrics(Component comp)
    {
        if(comp instanceof JComponent)
        {
            JComponent control = (JComponent) comp;
            Dimension pref = control.getPreferredSize();
            Dimension max = control.getMaximumSize();
            Dimension min = control.getMinimumSize();
            Err.pr("preferred height: " + pref.height);
            Err.pr("max height: " + max.height);
            Err.pr("min height: " + min.height);
        }
    }

    /*
     public static void deleteToTemp( File file)
     {
     // String newName = fileName + ".deleted";
     File newFile = null;
     String lastNameInPath = null;
     try
     {
     lastNameInPath = file.getName();
     newFile = File.createTempFile( lastNameInPath, ".deleted");
     }
     catch(IOException ex)
     {
     Err.error( "Could not create a TEMP file of " + lastNameInPath);
     }
     catch(IllegalArgumentException ex)
     {
     Err.error( ex.toString() + ", fileName: <" + file.getAbsolutePath() + ">");
     }
     if(newFile != null)
     {
     //       *
     //       * Does not work with an existing file
     //       boolean ok = file.renameTo( newFile);
     //       if(ok)
     //       {
     //       Print.pr( "Have removed the file " + fileName);
     //       }
     //       else
     //       {
     //       Err.pr( "Not able to rename " + fileName);
     //       }
     //       *
     Utils.copyFile( file, newFile);
     file.delete();
     }
     else
     {
     Err.error( "Could not move " + file.getPath() + " to the TEMP directory");
     }
     }
     */

//    public static void chkNotNull(Object object)
//    {
//        if(object == null)
//        {
//            Err.error("%%found null element");
//        }
//    }
//
//    public static void chkNotNull(Object object, String id)
//    {
//        if(object == null)
//        {
//            Err.error("%%" + id + ", found null element");
//        }
//    }

    public static void chkNoNulls(Object objs[], String id)
    {
        if(objs == null)
        {
            Err.error("Found a null array");
        }
        for(int i = 0; i < objs.length; i++)
        {
            Object object = objs[i];
            if(object == null)
            {
                Err.error("%%" + id + ", found null element in " + objs);
            }
            else
            {// Err.pr( element);
            }
        }
    }


    /**
     * Used for equals() (and others) methods just to check we
     * haven't done anything really stupid.
     *
     * @param o
     * @param clazz
     */
    public static void chkType(Object o, Class clazz)
    {
        if(o != null)
        {
            // if(!(o.getClass().equals( clazz)))
            if(!(clazz.isAssignableFrom(o.getClass())))
            // if(!(o.getClass().isAssignableFrom( clazz)))
            {
                if(o != null)
                {
                    Err.error(clazz.getName() + " cannot be compared with "
                        + o.getClass().getName() + " of value <" + o + ">");
                }
                else
                {
                    Err.error( "Can't check that type is a " + clazz.getName() + " when given a null");
                }
            }
        }
    }

    /**
     * Everything in list must be of type clazz. The first element that is found not
     * to be causes a stack trace.
     *
     * @param list
     * @param clazz
     * @param id
     */
    public static void chkTypes(Collection list, Class clazz, String id)
    {
        chkTypesEitherWay(list, clazz, id, true);
    }
    
    public static void chkTypes(Collection list, Class clazz)
    {
        chkTypesEitherWay(list, clazz, "", true);
    }

    public static void chkTypesNot(Collection list, Class clazz, String id)
    {
        chkTypesEitherWay(list, clazz, id, false);
    }
    
    public static void chkNotEmpty(Collection list)
    {
        if(list.isEmpty())
        {
            Err.error( "Found an empty list");
        }
    }

    public static void chkOneOnly( Collection list, String id)
    {
        if(list.size() != 1)
        {
            Print.prCollection( list, id);
            Err.error( "One element only required, instead got " + list.size());
        }
    }

    public static void chkTypesEitherWay(Collection list, Class clazz, String id, boolean mustAllBe)
    {
        if(list == null)
        {
            Err.error("Found a null list");
        }
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Object element = iter.next();
            if(element == null)
            {
                //Err.error("%%" + id + ", found null element in " + list);
            }
            else
            {
                if(mustAllBe)
                {
                    if(!instanceOf(element, clazz))
                    {
                        Print.prCollection(list, "Bad collection");
                        String str = "F";
                        if(!Utils.isBlank( id))
                        {
                            str = id + ", f";
                        }
                        Err.error("%%" + str + "ound that <" + element.getClass() + "> is not of expected type <" + clazz + ">");
                    }
                }
                else //We are looking for a bad apple
                {
                    if(instanceOf(element, clazz))
                    {
                        Print.prCollection(list, "Bad collection");
                        Err.error("%%" + id + ", do not want to see a <" + clazz + ">, were expecting a <" + clazz + ">");
                    }

                }
            }
        }
    }

    public static void chkTypesOneOnly(Collection list, Class clazz, String id)
    {
        if(list == null)
        {
            Err.error("Found a null list");
        }
        int count = 0;
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Object element = iter.next();
            if(element == null)
            {
                //Err.error("%%" + id + ", found null element in " + list);
            }
            else
            {
                if(instanceOf(element, clazz))
                {
                    count++;
                }
            }
        }
        if(count == 0)
        {
            Print.prCollection(list, "Bad collection");
            Err.error("%%" + id + ", could not see any of <" + clazz + ">");
        }
        else if(count > 1)
        {
            Print.prCollection(list, "Bad collection");
            Err.error("%%" + id + ", could see more than one of <" + clazz + ">");
        }
        else
        {
            Err.pr( "As expected, can see one of <" + clazz + ">");
        }
    }
    
    public static boolean chkNoNulls(Collection list, String id)
    {
        return chkNoNulls(list, id, true);
    }

    public static boolean chkNoNulls(Collection list, String id, boolean fail)
    {
        boolean result = true;
        if(list == null)
        {
            result = false;
            if(fail) Err.error("Found a null list");
        }
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Object element = iter.next();
            if(element == null)
            {
                result = false;
                if(fail)
                {
                    //Causes recursion, so don't do
                    //Print.prCollection( list, "At least one element is null");
                    Err.error("%%" + id + ", found null element in " + list);
                }
                break;
            }
            else
            {// Err.pr( element);
            }
        }
        return result;
    }

    public static boolean chkNotAllNulls(Collection list, String id, boolean fail)
    {
        boolean result = true;
        if(list == null)
        {
            result = false;
            if(fail) Err.error("Found a null list");
        }
        int nullCount = 0;
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Object element = iter.next();
            if(element == null)
            {
                nullCount++;
            }
            else
            {// Err.pr( element);
            }
        }
        if(nullCount == list.size())
        {
            result = false;
            if(fail) Err.error("%%" + id + ", found all null elements in " + list);
        }
        return result;
    }

    public static void chkNoInstanceDups(List list)
    {
        if(list == null)
        {
            Err.error("Found a null list");
        }
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Object element = iter.next();
            if(containsByInstance(list, element) >= 2)
            {
                Print.prList(list, "Utils.chkNoInstanceDups()");
                Err.error("Same element exists twice in a list: (element) " + element);
            }
        }
    }

    /**
     * Comapres two components based on their names
     *
     * @param comp1
     * @param comp2
     */
    public static int compareTo(JComponent comp1, JComponent comp2)
    {
        int result = 0;
        String name1 = comp1.getName();
        String name2 = comp2.getName();
        result = compareTo(name1, name2);
        return result;
    }
    
    public static int compareTo(int i1, int i2)
    {
        int result = Utils.UNSET_INT;
        if(i1 > i2)
        {
            result = 1;
        }
        else if(i1 < i2)
        {
            result = -1;
        }
        else
        {
            result = 0;
        }
        return result;
    }

    /**
     * Compares two Comparables, taking into account nulls
     *
     * @param obj1
     * @param obj2
     */
    public static int compareTo( Comparable obj1, Comparable obj2)
    {
        int result = 0;
        Class clazz = null;
        if(obj1 == null && obj2 == null)
        {// nufin
            //Err.stack();
        }
        else if(obj1 == null)
        {
            result = -1;
            clazz = obj2.getClass();
        }
        else if(obj2 == null)
        {
            result = 1;
            clazz = obj1.getClass();
        }
        else
        {
            result = obj1.compareTo(obj2);
            clazz = obj2.getClass();
        }
        String className = null;
        if(clazz != null)
        {
            className = NameUtils.tailOfPackage( clazz.toString());
        }
        //Err.pr( "Cfing (class:" + className + ") <"
        //        + obj1 + "> with <" + obj2 + "> gives <" + result + ">");
        return result;
    }

    /**
     * Sort by the opposite to the natural ordering. 
     */
    public static int compareToReverse(Comparable obj1, Comparable obj2)
    {
        int result = 0;
        if(obj1 == null && obj2 == null)
        {// nufin
        }
        else if(obj1 == null)
        {
            result = 1;
        }
        else if(obj2 == null)
        {
            result = -1;
        }
        else
        {
            result = obj2.compareTo(obj1);
        }
        return result;
    }

    public static int compareTo(boolean b1, boolean b2)
    {
        int result = -1;
        if(b1 == b2)
        {
            result = 0;
        }
        return result;
    }

    public static int compareToReverse(boolean b1, boolean b2)
    {
        int result = 1;
        if(b1 == b2)
        {
            result = 0;
        }
        return result;
    }

    public static int roundToNearest(int value, int pixels)
    {
        int result;
        BigInteger bigValue = new BigInteger(Integer.toString(value));
        BigInteger bigpixels = new BigInteger(Integer.toString(pixels));
        BigInteger bigResult[] = bigValue.divideAndRemainder(bigpixels);
        BigInteger half = bigpixels.divide(new BigInteger(Integer.toString(2)));
        if(bigResult[1].compareTo(half) <= 0)
        {
            result = bigResult[0].intValue();
        }
        else
        {
            result = bigResult[0].intValue() + 1;
        }
        return result * pixels;
    }

    public static String findFirstLineContaining(String match, String comments)
    {
        String result = null;
        String lines[] = comments.split(NEWLINE);
        if(lines.length == 1)
        {
            // As writing to a text area in java on a PC gives \n (rather than \n\r)
            lines = comments.split("\n");
        }
        for(int i = 0; i <= lines.length - 1; i++)
        {
            if(lines[i].indexOf(match) != -1)
            {
                result = lines[i];
                break;
            }
        }
        return result;
    }

    public static String arrowQuote( String in)
    {
        StringBuffer result = new StringBuffer();
        String sep = NEWLINE;
        String lines[] = in.split(NEWLINE);
        if(lines.length == 1)
        {
            // As writing to a text area in java on a PC gives \n (rather than \n\r)
            lines = in.split("\n");
            sep = "\n";
        }
        for(int i = 0; i <= lines.length - 1; i++)
        {
            result.append( "> ");
            result.append( lines[i]);
            result.append( sep);
            //Err.pr( lines[i]);
        }
        return result.toString();
    }

    public static boolean containsWhitespace(String str)
    {
        boolean result = false;
        if(str != null)
        {
            char cArray[] = new char[str.length()];
            str.getChars(0, str.length(), cArray, 0);
            for(int i = 0; i <= cArray.length - 1; i++)
            {
                if(Character.isWhitespace(cArray[i]))
                {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public static int findFirstPositiveNumberInTxt(String str)
    {
        int result = -99;
        if(str != null)
        {
            StringBuffer out = new StringBuffer();
            char cArray[] = new char[str.length()];
            str.getChars(0, str.length(), cArray, 0);

            char thisChar = '\0';
            boolean foundDigit = false;
            for(int i = 0; i <= cArray.length - 1; i++)
            {
                thisChar = cArray[i];
                if(Character.isDigit(thisChar))
                {
                    NameUtils.appendToStringBuffer(out, thisChar);
                    foundDigit = true;
                }
                else if(foundDigit && Character.isWhitespace(thisChar))
                {
                    break;
                }
            }
            try
            {
                result = Integer.parseInt(new String(out));
            }
            catch(NumberFormatException nfe)
            {
                Err.error("Should always parse correctly");
            }
        }
        return result;
    }
    
    public static Object getFromList(List list, Object object)
    {
        return getFromList( list, object, false);
    }
    
    public static Object getFromList(List list, Object object, boolean chkNumTimes)
    {
        return getFromList( list, object, chkNumTimes, null);
    }
    
    /**
     * For LOV type data that has been stored in the database we
     * need to be able to get the object that has been stored in
     * the database, rather than the static in the enumId. We can
     * use the static in the Enum to get the stored object.
     * <p/>
     * Convenience method when you need to find a particular instance from a list.
     * This is useful for example when the list of objects comes from the DB, and
     * it is the DB instance that needs to be used.
     *
     * @param object Usually a static, say Flexibility.NO_OVERNIGHTS
     * @param list   The list that actually came from the DB
     * @return The DB instance
     */
    public static Object getFromList(List list, Object object, boolean chkNumTimes, String equalsPropNames[])
    {
        return getFromList( list, object, chkNumTimes, equalsPropNames, true);        
    }
    
    /**
     * For LOV type data that has been stored in the database we
     * need to be able to get the object that has been stored in
     * the database, rather than the static in the enumId. We can
     * use the static in the Enum to get the stored object.
     * <p/>
     * Convenience method when you need to find a particular instance from a list.
     * This is useful for example when the list of objects comes from the DB, and
     * it is the DB instance that needs to be used.
     *
     * @param object Usually a static, say Flexibility.NO_OVERNIGHTS
     * @param list   The list that actually came from the DB
     * @return The DB instance
     */
    public static Object getFromList(List list, Object object, boolean chkNumTimes, String equalsPropNames[], boolean errOnNull)
    {
        Object result = null;
        if(list == null)
        {
            Err.error( "list == null");
        }
        int timesFound = 0;
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Object element = iter.next();
            boolean equalsAnswer;
            if(equalsPropNames == null)
            {
                equalsAnswer = element.equals(object);
            }
            else
            {
                //ReasonNotEquals.turnOn(true);                
                equalsAnswer = SelfReferenceUtils.equalsByProperties( equalsPropNames, element, object);    
                //ReasonNotEquals.turnOn(false);                
            }
            if(equalsAnswer)
            {
                result = element;
                timesFound++;
                if(!chkNumTimes)
                {
                    break;
                }
                else
                {
                    if(timesFound > 1)
                    {
                        debugNotFound(list, object, equalsPropNames);
                        Err.error( "Did not expect to find an <" + object + "> in list more than once");
                    }
                }
            }
        }
        if(errOnNull && result == null)
        {
            Assert.isTrue( timesFound == 0);
            debugNotFound(list, object, equalsPropNames);
            Err.error("Expected to find a <" + object + "> of type " + object.getClass().getName() + " in list " /*+ list*/);
        }
        return result;
    }
    
    private static void debugNotFound(List list, Object object, String equalsPropNames[])
    {
        for(int i = 0; i < equalsPropNames.length; i++)
        {
            String propName = equalsPropNames[i];
            Err.pr( propName + ", " + SelfReferenceUtils.invokeReadProperty( object, propName));                
        }
        Err.pr( "");
        Err.pr( "To choose from:");
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            Object element = iter.next();
            for(int i = 0; i < equalsPropNames.length; i++)
            {
                String propName = equalsPropNames[i];
                Err.pr( propName + ", " + SelfReferenceUtils.invokeReadProperty( element, propName));
            }
            Err.pr( "");
        }
    }

    public static Object getByStringFromArray(Object objs[], String s)
    {
        Object result = null;
        boolean foundNull = false;
        for(int i = 0; i < objs.length; i++)
        {
            Object element = objs[i];
            String toString = element.toString();
            if(toString == null)
            {
                //This is normal if use VALUES as NULL case
                //Err.pr( "Class of type <" + element.getClass().getName() + "> has toString() that is returning null");
                if(s == null)
                {
                    foundNull = true;    
                }
            }
            else if(toString.equals(s))
            {
                result = element;
                break;
            }
        }
        if(result == null && !foundNull)
        {
            for(int i = 0; i < objs.length; i++)
            {
                Object element = objs[i];
                Err.pr(element.toString());
            }
            Err.error("Expected to find a <" + s + "> in array (see above for toString() contents) " + objs);
        }
        return result;
    }

    /**
     * I keep forgetting the syntax for the mod
     * integer arithmetic operator, as defined in The Java Language
     * Specification, so here doing it using BigInteger.
     * Gives the number left over after dividing.
     */
    public static int mod( int value, int divideBy)
    {
        int result;
        BigInteger bigValue = new BigInteger(Integer.toString( value));
        BigInteger bigDivideBy = new BigInteger(Integer.toString( divideBy));
        BigInteger bigResult = bigValue.mod(bigDivideBy);
        result = bigResult.intValue();
        return result;
    }
    
    public static float divide( int value, int divideBy)
    {
        float result;
        Float floatValue = new Float(value);
        Float floatDivideBy = new Float(divideBy);
        result = floatValue.floatValue() / floatDivideBy.floatValue();
        //Err.pr( "From " + value + " divided by " + divideBy + " will ret " + result);
        return result;
    }

    public static float floatDivide( long value, long divideBy)
    {
        float result;
        Float floatValue = new Float(value);
        Float floatDivideBy = new Float(divideBy);
        result = floatValue.floatValue() / floatDivideBy.floatValue();
        //Err.pr( "From " + value + " divided by " + divideBy + " will ret " + result);
        return result;
    }

    public static BigDecimal decimalDivide( int value, int divideBy)
    {
        BigDecimal result;
        BigDecimal bdValue = new BigDecimal(value);
        result = decimalDivide( bdValue, divideBy);
        return result;
    }

    public static BigDecimal decimalDivide( BigDecimal bdValue, int divideBy)
    {
        BigDecimal result = null;
        try
        {
            result = bdValue.divide( new BigDecimal( divideBy), 10, RoundingMode.HALF_UP);
        }
        catch(ArithmeticException ex)
        {
            Err.error( ex, "Cannot divide " + bdValue + " by " + divideBy);
        }
        //Err.pr( "From " + value + " divided by " + divideBy + " will ret " + result);
        return result;
    }

    /**
     * Given two BigDecimals, return the one that is closest to the reference. 
     */
    public static BigDecimal findClosest( BigDecimal first, BigDecimal second, BigDecimal reference)
    {
        BigDecimal result;
        BigDecimal firstDifference = first.subtract( reference).abs(); 
        BigDecimal secondDifference = second.subtract( reference).abs(); 
        BigDecimal least = findLeast( firstDifference, secondDifference);
        if(least == firstDifference)
        {
            result = first;
        }
        else
        {
            result = second;
        }
        return result;
    }
    
    public static BigDecimal absoluteDifference( BigDecimal first, BigDecimal second)
    {
        BigDecimal result;
        BigDecimal diff = first.subtract( second);
        result = diff.abs();
        return result;
    }
    
    public static BigDecimal findLeast( BigDecimal first, BigDecimal second)
    {
        BigDecimal result;
        int cf = first.compareTo( second);
        if(cf < 0)
        {
            result = first;
        }
        else
        {
            result = second;
        }
        return result;
    }    

    public static BigDecimal multiply( BigDecimal toBeMultiplied, int multiplyBy)
    {
        BigDecimal result = toBeMultiplied.multiply( new BigDecimal( multiplyBy));
        return result;
    }    

    public static BigDecimal multiply( float toBeMultiplied, int multiplyBy)
    {
        float answer = toBeMultiplied * multiplyBy;
        BigDecimal result = new BigDecimal( answer);
        return result;
    }

    public static boolean isEven(int value)
    {
        boolean result = false;
        if(value == 0 || (mod(value, 2) == 0))
        {
            result = true;
        }
        return result;
    }

    /*
    */

    public static Rectangle translateRectFromPointDiff(Point fromPoint, Rectangle fromRect, Point toPoint)
    {
        // Err.pr( "fromPoint: " + fromPoint);
        // Err.pr( "toPoint: " + toPoint);
        Rectangle result = new Rectangle();
        result.height = fromRect.height;
        result.width = fromRect.width;

        int xdiff = fromPoint.x - toPoint.x;
        int ydiff = fromPoint.y - toPoint.y;
        // Err.pr( "xdiff is " + xdiff);
        // Err.pr( "ydiff is " + ydiff);
        result.x = fromPoint.x - xdiff;
        result.y = fromPoint.y - ydiff;
        return result;
    }

    public static List emunToList(Enumeration enumeration)
    {
        List result = new ArrayList();
        for(; enumeration.hasMoreElements();)
        {
            Object element = enumeration.nextElement();
            result.add(element);
        }
        return result;
    }

    /**
     * Calling hashCode on an array does not go into the items in the array,
     * as would be done for a List.
     */
    public static int arrayHashCode(Object array[])
    {
        int result = 0;
        if(array != null)
        {
            for(int i = 0; i < array.length; i++)
            {
                Object obj = array[i];
                result += obj.hashCode();
            }
        }
        // Err.pr( "arrayHashCode " + array + "returning hashCode of " + result);
        return result;
    }

    public static boolean equalsArrays(Object[] arr1, Object[] arr2)
    {
        ReasonNotEquals.addClassVisiting("Utils.equalsArrays()");

        boolean result = true;
        if(arr1 != arr2)
        {
            if(arr1 == null || arr2 == null)
            {
                ReasonNotEquals.addReason("got two null arrays");
                result = false;
            }
            else if(arr1.getClass() == arr2.getClass())
            {
                if(arr1.length == arr2.length)
                {
                    int size = arr1.length;
                    for(int i = 0; i < size; i++)
                    {
                        Object e1 = arr1[i];
                        Object e2 = arr2[i];
                        if(e1 == null ? e2 == null : e1.equals(e2))
                        {// keep on being true
                        }
                        else
                        {
                            ReasonNotEquals.addReason("objects not equal: " + e1 + ", " + e2);
                            result = false;
                            break;
                        }
                    }
                }
                else
                {
                    ReasonNotEquals.addReason("lengths of arrays not equal");
                    result = false;
                }
            }
            else
            {
                ReasonNotEquals.addReason("arrays are of different classes: " + arr1.getClass().getName()
                    + ", " + arr2.getClass().getName());
                result = false;
            }
        }
        return result;
    }

    public static boolean isSimple(Class clazz)
    {
        boolean result = false;
        if(clazz == Boolean.class)
        {
            result = true;
        }
        else if(clazz == String.class)
        {
            result = true;
        }
        else if(clazz == Integer.class)
        {
            result = true;
        }
        else if(clazz == Float.class)
        {
            result = true;
        }
        else if(clazz == Double.class)
        {
            result = true;
        }
        else if(clazz == Date.class)
        {
            result = true;
        }
        else if(clazz == Long.class)
        {
            result = true;
        }
        return result;
    }
    
    public static StringBuffer getStringBufferFromList(List list)
    {
        return getStringBufferFromList(list, new char[]{'\r'});
    }

    public static StringBuffer getStringBufferFromList(List list, char ch[])
    {
        StringBuffer result = new StringBuffer();
        for(Iterator iter = list.iterator(); iter.hasNext();)
        {
            String line = (String) iter.next();
            result.append(line);
            for(int i = 0; i < ch.length; i++)
            {
                char c = ch[i];
                NameUtils.appendToStringBuffer(result, c);
            }
        }
        return result;
    }

    public static List getListFromString(String s)
    {
        List result = new ArrayList();
        StringBuffer line = new StringBuffer();
        StringCharacterIterator it = new StringCharacterIterator(s);
        for(char c = it.current(); c != StringCharacterIterator.DONE; c = it.next())
        {
            if(c == '\r')
            {
                if(it.next() == '\n')
                {
                    result.add(line.toString());
                    line.setLength(0);
                    // Err.pr( "Created line: " + line);
                }
                else
                {
                    it.previous();
                    result.add(line.toString());
                    line.setLength(0);
                }
            }
            else
            {
                NameUtils.appendToStringBuffer(line, c);
            }
        }
        result.add(line.toString());
        return result;
    }

    /*
     * dis.readLine() depreciated, and not used anyway
     public static StringBuffer readInFile( File file)
     {
     StringBuffer result = new StringBuffer();
     DataInputStream dis = null;
     String record = null;
     int recCount = 0;

     try
     {
     FileInputStream fis = new FileInputStream( file);
     BufferedInputStream bis = new BufferedInputStream( fis);
     dis = new DataInputStream( bis);

     while ( (record=dis.readLine()) != null )
     {
     recCount++;
     //System.out.println(recCount + ": " + record);
     //result.append( '\r'); //see if looks ok from notepad
     result.append( record + "\r");
     }
     }
     catch(IOException e)
     {
     //catch io errors from FileInputStream or readLine()
     Err.error( e);
     }
     finally
     {
     // if the file opened okay, make sure we close it
     if (dis != null)
     {
     try
     {
     dis.close();
     }
     catch (IOException ioe)
     {
     Err.error( ioe);
     }
     }
     }
     return result;
     }
     */

    public static String removeUnusedImports(StringBuffer fileText,
                                             String[] unusedImports)
    {
        String result = null;
        String importsPart = null;
        String afterImportsPart = null;
        List possiblyUnusedImports = new ArrayList();
        for(int i = 0; i < unusedImports.length; i++)
        {
            String classNameWithSemi = NameUtils.findClassAtEndOfPackage(unusedImports[i]);
            String className = NameUtils.removeEnding(classNameWithSemi, ";");
            possiblyUnusedImports.add(className);
        }

        String asString = fileText.toString();
        int index = asString.lastIndexOf("import");
        index = asString.indexOf('\r', index);
        importsPart = asString.substring(0, index);
        afterImportsPart = asString.substring(index);
        /*
        Print.pr( "---------------------");
        Print.pr( afterImportsPart);
        Print.pr( "---------------------");
        */
        List unusedClasses = new ArrayList();
        int i = 0;
        for(Iterator iter = possiblyUnusedImports.iterator(); iter.hasNext(); i++)
        {
            String className = (String) iter.next();
            if(afterImportsPart.indexOf(className) == -1)
            {
                //Err.pr( "Did not find " + className + " being used - " + unusedImports[i]);
                unusedClasses.add(className);
            }
        }

        String alteredImportsPart = removeLinesContaining(importsPart,
            unusedClasses);
        result = alteredImportsPart + afterImportsPart;
        return result;
    }

    /**
     * Divide importsPart into lines. Go thru each line with all unusedClasses
     * and note which lines are to be removed. Remove all of these lines. Make
     * a String out of this shorter list of lines, and return it.
     *
     * @param importsPart   The text to search through
     * @param unusedClasses The strings whose lines are not to make it to result
     * @return result
     */
    private static String removeLinesContaining
        (String importsPart, List unusedClasses)
    {
        StringBuffer result = null;
        List asLines = Utils.getListFromString(importsPart);
        //Print.prList(asLines, "import lines");
        List toRmLines = new ArrayList();
        int i = 0;
        for(Iterator iter = asLines.iterator(); iter.hasNext(); i++)
        {
            String line = (String) iter.next();
            for(Iterator iterator = unusedClasses.iterator(); iterator.hasNext();)
            {
                String className = (String) iterator.next();
                if(line.indexOf(className) != -1) //An unused class is found
                {
                    //Err.pr("Unused class " + className + " so will remove " + i);
                    toRmLines.add(new Integer(i));
                    break;
                }
            }
        }

        List shorterList = new ArrayList();
        i = 0;
        for(Iterator iter = asLines.iterator(); iter.hasNext(); i++)
        {
            String line = (String) iter.next();
            if(!toRmLines.contains(new Integer(i)))
            {
                shorterList.add(line);
            }
        }
        result = getStringBufferFromList(shorterList);
        return result.toString();
    }

    public static String elementsOfSet(Set set)
    {
        String result = "";
        if(set != null)
        {
            for(Iterator iter = set.iterator(); iter.hasNext();)
            {
                if(!result.equals(""))
                {
                    result += " | ";
                }

                Object element = iter.next();
                String type = "";
                if(element != null)
                {
                    type = element.getClass().getName();
                }
                result += "\tvalue: " + element + ", class: " + type;
            }
        }
        return result;
    }

    public static void pickFirstIfNone(JList list)
    {
        if(list.isSelectionEmpty())
        {
            // Err.pr( "pickFirstIfNone isSelectionEmpty true so will SELECT first");
            list.setSelectedIndex(0);
        }
        else
        {// Err.pr( "pickFirstIfNone isSelectionEmpty false");
        }
    }

    public static boolean isBlank(String s)
    {
        boolean result = false;
        if(s == null || s.equals(""))
        {
            result = true;
        }
        return result;
    }

    public static boolean isBlank(Object o)
    {
        boolean result = false;
        if(o == null)
        {
            result = true;
        }
        else if((o instanceof String) && o.equals( ""))
        {
            result = true;
        }
        else
        {
            //Err.pr( "Not blank because type is " + o.getClass().getName());
            if(o.toString().equals( ""))
            {
                //Err.pr( "Proved to be blank using toString()");
                result = true;
            }
        }
        return result;
    }
    
    public static boolean isBlank(Integer i)
    {
        boolean result = false;
        if(i == null || i.toString().equals(""))
        {
            result = true;
        }
        return result;
    }

    public static boolean isBlank(Boolean b)
    {
        boolean result = false;
        if(b == null)
        {
            result = true;
        }
        return result;
    }
    
    public static boolean isBlank(List list)
    {
        boolean result = false;
        if(list == null)
        {
            result = true;
        }
        else if(list.isEmpty())
        {
            result = true;
        }
        return result;
    }

    /**
     * Can use this if have overridden equals() yet want to see if it contains
     * the exact same memory instance.
     *
     * @param list
     * @param obj
     * @return the number of times that obj is found in list
     */
    public static int containsByInstance(List list, Object obj)
    {
        int result = 0;
        if(list != null)
        {
            for(Iterator iter = list.iterator(); iter.hasNext();)
            {
                Object element = iter.next();
                if(element == obj)
                {
                    result++;
                }
            }
        }
        return result;
    }

    public static int containsByEquals(Collection collection, Object obj)
    {
        int result = 0;
        for(Iterator iter = collection.iterator(); iter.hasNext();)
        {
            Object element = iter.next();
            if(element.equals(obj))
            {
                result++;
            }
        }
        return result;
    }

    public static int containsByToStringEquals(List list, Object obj)
    {
        int result = 0;
        for(Iterator iterator = list.iterator(); iterator.hasNext();)
        {
            Object o = iterator.next();
            if(o.toString().equals(obj.toString()))
            {
                result++;
            }
        }
        return result;
    }

    public static int containsByInstance(Object[] comps, Object obj)
    {
        int result = 0;
        for(int i = 0; i < comps.length; i++)
        {
            Object element = comps[i];
            if(element == obj)
            {
                result++;
            }
        }
        return result;
    }

    /**
     * Nearly the same as contains for a list, but works for an array.
     */
    public static int containsByEquals(Object[] comps, Object obj)
    {
        int result = 0;
        for(int i = 0; i < comps.length; i++)
        {
            Object element = comps[i];
            if(element.equals(obj))
            {
                result++;
            }
        }
        return result;
    }

    /**
     * The same list that has come in as a parameter is returned.
     */
    public static List reverseOrder(List list)
    {
        Collections.sort(list, Collections.reverseOrder());
        return list;
    }

    public static List reverse(List list)
    {
        //Print.prList( list, "b4 reverse");
        List result = new ArrayList();
        for (int i = list.size()-1; i >= 0; i--)
        {
            Object o = list.get(i);
            result.add( o);
        }
        //Print.prList( result, "after reversal");
        return result;
    }

    public static boolean isOdd( int value)
    {
        return ((value & 1) == 1);
    }
    
    public static String toPercent(double percentComplete)
    {
        int byOneHundred = (int) (percentComplete * 100);
        return String.valueOf(byOneHundred + "%");
    }

    public static Double average(List collected)
    {
        Double result = null;
        double allAdded = 0;
        //Print.prList( collected, "To average from", false);
        int numNulls = 0;
        for(Iterator iterator = collected.iterator(); iterator.hasNext();)
        {
            Object obj = iterator.next();
            //Err.pr( tsdi.getValue().getClass().getName());
            Number value = (Number) obj;
            if(value != null)
            {
                allAdded += value.doubleValue();
            }
            else
            {
                numNulls++;
            }
        }
        result = new Double(allAdded / (collected.size() - numNulls));
        return result;
    }

    public static double getProportion( long value, long max) 
    {
        return getProportion( value, 0, max);
    }
    
    public static double getProportion( long value, long min, long max) 
    {
        double result;
        long span = max - min;
        result = floatDivide( value - min, span);
        //result = (value - min) / span;
        return result;
    }
    
    /**
     * Used when we want to access (read) every point in an object graph - for instance
     * to bring all the objects across from the server.
     */
    public static void copyTrick(Collection c)
    {
        if(!c.isEmpty())
        {
            if(c.size() > 1)
            {
                Err.pr( JDONote.DEEP_COPY_TO_RETRIEVE, "Will do deepCopy for " + c.size() + " objects");
            }
            for(Iterator iterator = c.iterator(); iterator.hasNext();)
            {
                Object obj = iterator.next();
                Err.pr( JDONote.DEEP_COPY_TO_RETRIEVE, 
                        "From deepCopy have got <" + ObjectFoundryUtils.deepCopy( obj) + ">");
            }
        }
    }
    
    public static Integer createInteger( Object txt)
    {
        Integer result;
        try
        {
            result = new Integer( (String)txt);
        }
        catch(NumberFormatException ex)
        {
            result = new Integer( 0);
        }
        return result;
    }

    /**
     * Attribution:
     * http://www.theeggeadventure.com/wikimedia/index.php/Java_Unique_List
     * "The downside of this method is that the order of the items in the unique list will not be the same as
     * those in the original. If maintaining order is important, simply use a LinkedHashSet instead of a HashSet,
     * or if you want the items sorted, use a TreeSet instead of the HashSet. "
     * @param repeatsList
     * @return
     */
    public static List uniqueList( List repeatsList)
    {
        List result;
        Set set = new HashSet( repeatsList);
        result = new ArrayList(set);
        return result;
    }
        
    public static void main(String[] args)
    {
        // String name = "C:\\cml\\dt\\petstore\\PetStore.xml";
        // createNewFile( new File( name), false);
        //
        // int result = roundToNearest( 28, 5);
        //
        // String result = findClassAtEndOfPackage( "java.util.Collection");
        //
        // File file = new File( "C:\\dev\\...\\applic\\wombatrescue\\DohTriggers.java");
        // StringBuffer fileText = readInFile( file);
        // String str = removeUnusedImports( fileText, Triggers.UNUSED_IMPORTS);
        // Err.pr( str);
        //divByZeroTest();
        
//        StringBuffer email = new StringBuffer();
//        email.append("Hi Chris" + "," + Utils.separator);
//        email.append(Utils.separator);
//        email.append(
//            "Teresa House has you as unrostered for July - are there any spaces you can fill?"
//                + Utils.separator);
//        email.append(
//            "As an option we can take you off as a candidate for rostering - just reply to this email"
//                + Utils.separator);
//        email.append(
//            "with your request - we can always put you on again in the future ..."
//                + Utils.separator + Utils.separator);
//        Err.pr( arrowQuote( email.toString()));
        
//        Err.pr( "1: " + isOdd( 1));
//        Err.pr( "2: " + isOdd( 2));
//        Err.pr( "3: " + isOdd( 3));
//        Err.pr( "4: " + isOdd( 4));
        
//        BigDecimal femaleRatio = decimalDivide( 5, 11+5); 
//        BigDecimal maleRatio = decimalDivide( 11, 11+5); 
//        Err.pr( "female ratio: " + femaleRatio);
//        Err.pr( "male ratio: " + maleRatio);
//        BigDecimal refRatio = new BigDecimal( "0.48");
//        BigDecimal closest = findClosest( femaleRatio, maleRatio, refRatio);
//        Err.pr( "Closest to " + refRatio + " is " + closest);
        
        //outputSwingDefs();
        /*
        List listOfLists = new ArrayList();
        listOfLists.add( new ArrayList());
        listOfLists.add( new ArrayList());
        listOfLists.add( new ArrayList());
        ((List)listOfLists.get( 0)).add( "A");
        ((List)listOfLists.get( 0)).add( "B");
        ((List)listOfLists.get( 0)).add( "C");
        ((List)listOfLists.get( 0)).add( "D");
        ((List)listOfLists.get( 1)).add( "A");
        ((List)listOfLists.get( 1)).add( "B");
        ((List)listOfLists.get( 1)).add( "C");
        ((List)listOfLists.get( 1)).add( "D");
        ((List)listOfLists.get( 2)).add( "A");
        ((List)listOfLists.get( 2)).add( "B");
        ((List)listOfLists.get( 2)).add( "C");
        ((List)listOfLists.get( 2)).add( "D");
        stripe( listOfLists);
        */
        BigDecimal bigNum = new BigDecimal( "12.3");
        BigDecimal smallNum = new BigDecimal( "12.1");
        Err.pr( bigNum + " gt " + smallNum + ": " + greaterThan( bigNum, smallNum));
        Err.pr( bigNum + " lt " + smallNum + ": " + lessThan( bigNum, smallNum));
    }
    
    /**
     * Rotate a matrix. For instance we are passed a list of lists where each contains many people at a point in time.
     * We want to return a series of lists where each one is for one person, but over a time period. 
     */
    public static List stripe( List listOfLists)
    {
        List result = new ArrayList();
        int length = Utils.UNSET_INT;
        for(int i = 0; i < listOfLists.size(); i++)
        {
            int size = ((List)listOfLists.get(i)).size();
            if(size != length && length != Utils.UNSET_INT)
            {
                Print.prMatrix( listOfLists);
                Err.error( "Expect size to be like all previous ones: " + length + ", but got " + size + " for row " + i);
            }
            length = size;
        }
        for(int i = 0; true; i++)
        {
            List colAsRow = colAsRow( listOfLists, i);
            if(colAsRow.isEmpty())
            {
                break;
            }
            result.add( colAsRow);
            Print.prList( colAsRow, "Col as Row");
        }
        return result;
    }
    
    private static List colAsRow( List listofLists, int every)
    {
        List result = new ArrayList();
        for(int i = 0; i < listofLists.size(); i++)
        {
            List list = (List)listofLists.get(i);
            if(every < list.size())
            {
                result.add( list.get( every));
            }
        }
        return result;
    }
    
    public static String toHexString( Color col)
    {
        return Integer.toHexString((col.getRGB() & 0xffffff) | 0x1000000).substring(1);
    }

    public static boolean isNumber( String s)
    {
        boolean result = true;
        int i = 0;
        if(Utils.isBlank( s))
        {
            result = false;
        }
        else
        {
            if(s.charAt(0) == '-' && s.length() > 1)
            {
                i++; //Even if it starts off with a '-', it can still be a number!
            }
            for(; i < s.length(); i++)
            {
                char ch = s.charAt(i);
                if(!Character.isDigit( ch))
                {
                    if(!(ch == '.'))
                    {
                        result = false;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static boolean isPercentage( String test)
    {
        boolean result = obeysFormat( test, "99%") || obeysFormat( test, "9%") || test.equals("100%");
        return result;
    }

    public static String convertPercentageToFraction( String percentage)
    {
        String result;
        //Assert.isTrue( isPercentage( percentage));
        String[] sides = percentage.split( "%");
        Assert.isTrue( sides.length == 1);
        BigDecimal decimal = decimalDivide( new Integer( sides[0]), new Integer( 100));
        result = decimal.toString().substring( 0, 4);
        //Err.pr( "From %age <" + percentage + "> will be returning <" + result + ">");
        return result;
    }

    /**
     * Finds out if the test string param obeys the simple format.
     * Going over the length of the format is still considered a match.
     * @param test
     * the String that will be tested to see if it obeys the format
     * @param format
     * made up of these letters:
     * A uppercase char
     * a lowercase char
     * - minus sign
     * 9 digit
     * % percent sign
     * @return
     * true if test obeys format
     */
    public static boolean obeysFormat( String test, String format)
    {
        boolean result = true;
        Assert.notNull( test);
        Assert.notNull( format);
        Assert.isTrue( format.length() > 0);
        Assert.isTrue( test.length() > 0);
        char[] formatChars = format.toCharArray();
        char[] testChars = test.toCharArray();
        if(formatChars.length > testChars.length)
        {
            /*
             * If you have not got as many chars as you need to
             * examine then test does not meet the format
             */
            result = false;
        }
        else
        {
            for (int i = 0; i < formatChars.length; i++)
            {
                char formatChar = formatChars[i];
                //if(testChars.length > i)
                {
                    char testChar = testChars[i];
                    if(formatChar == 'A')
                    {
                        if(!Character.isUpperCase( testChar))
                        {
                            result = false; break;
                        }
                    }
                    else if(formatChar == 'a')
                    {
                        if(!Character.isLowerCase( testChar))
                        {
                            result = false; break;
                        }
                    }
                    else if(formatChar == '-')
                    {
                        if(testChar != '-')
                        {
                            result = false; break;
                        }
                    }
                    else if(formatChar == '9')
                    {
                        if(!Character.isDigit( testChar))
                        {
                            result = false; break;
                        }
                    }
                    else if(formatChar == '%')
                    {
                        if(testChar != '%')
                        {
                            result = false; break;
                        }
                    }
                    else
                    {
                        Err.error( "Do not recognise formatChar: <" + formatChar + ">");
                    }
                }
            }
        }
        return result;
    }

    /* Useful but no point compiling - put in examples package sometime...
    public static void outputSwingDefs()
    {
        String lineSep = System.getProperty("line.separator");
        javax.swing.UIDefaults uid =
                javax.swing.UIManager.getDefaults();
        java.util.Enumeration uidKeys = uid.keys();
        java.io.BufferedWriter buff = null;
        java.io.FileWriter fr = null;

        try
        {
            fr = new java.io.FileWriter("swing_defaults.txt", true);
            buff = new java.io.BufferedWriter(fr);

            while(uidKeys.hasMoreElements())
            {
                Object aKey = uidKeys.nextElement();
                Object aValue = uid.get(aKey);
                String str = "KEY: " + aKey + ", VALUE: " + aValue + lineSep;
                buff.write(str, 0, str.length());
            }
        }
        catch(java.io.IOException ioe)
        {
            //deal with exception
        }
        finally
        {
            try
            {
                buff.flush();
                buff.close();
                fr.close();
            }
            catch(Exception e)
            {
                //deal with exception
            }
        }
    }
    */
    
    /*
     public static void setYear( int i, Date date)
     {
     date.setYear( i);
     }
     public static void setMonth( int i, Date date)
     {
     date.setMonth( i);
     }
     */

    /*
     public static boolean isRectangleContainingPoint(
     Rectangle inner, Point point)
     {
     boolean result = false;
     if(inner.contains( point))
     {
     result = true;
     }
     return result;
     }
     */
}
