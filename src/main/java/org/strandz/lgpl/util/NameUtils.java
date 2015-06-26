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

import javax.swing.JLabel;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.awt.*;

/**
 * This static class collects all the methods that have to do with String
 * names, be they file names, package names, variable names or just names
 *
 * @author Chris Murphy
 */
public class NameUtils
{

    /*
    public static String firstWord(String in)
    {
        String result;
        int index = in.indexOf(' ');
        if(index == -1)
        {
            result = in;
        }
        else
        {
            result = in.substring(0, index);
        }
        return result;
    }
    */

    public static String getLastWord( String sentence)
    {
        String result = null;
        if(!Utils.isBlank( sentence))
        {
            for(StringTokenizer stringTokenizer = new StringTokenizer(sentence); stringTokenizer.hasMoreTokens();)
            {
                result = stringTokenizer.nextToken();
            }
        }
        return result;
    }

    public static String getFirstWord( String sentence)
    {
        return getWord( sentence, 1);
    }

    public static String getSecondWord( String sentence)
    {
        return getWord( sentence, 2);
    }
    
    public static String getWord( String sentence, int ordinal)
    {
        String result = null;
        int i=0;
        if(!Utils.isBlank( sentence))
        {
            for(StringTokenizer stringTokenizer = new StringTokenizer(sentence); 
                stringTokenizer.hasMoreTokens() && i < ordinal; i++)
            {
                result = stringTokenizer.nextToken();
            }
        }
        if(i != ordinal)
        {
            //There were not enough words to reach ordinal
            result = null; 
        }
        return result;
    }

    public static String getWords( String sentence, int ordinal, int extraWords)
    {
        String result = null;
        int i=0;
        if(!Utils.isBlank( sentence))
        {
            for(StringTokenizer stringTokenizer = new StringTokenizer(sentence);
                stringTokenizer.hasMoreTokens() && i < ordinal; i++)
            {
                result = stringTokenizer.nextToken();
            }
        }
        if(i != ordinal)
        {
            //There were not enough words to reach ordinal
            result = null;
        }
        else
        {
            for (int j = 0; j < extraWords-1; j++)
            {
                String extraWord = getWord( sentence, ordinal+1+j);
                result = result + " " + extraWord;
            }
        }
        return result;
    }

    public static String replaceLastTok(String replaceIn, String with)
    {
        String result = null;
        String lastTok = null;
        StringTokenizer st = new StringTokenizer(replaceIn);
        while(st.hasMoreTokens())
        {
            lastTok = st.nextToken();
        }
        int idx = replaceIn.indexOf(lastTok);
        String base = replaceIn.substring(0, idx);
        result = base + with;
        return result;
    }

    public static String spaceOutClassName(String str)
    {
        StringTokenizer st = new StringTokenizer(str, ".");
        while(st.hasMoreTokens())
        {
            str = st.nextToken();
        }

        char cArray[] = new char[str.length()];
        str.getChars(0, str.length(), cArray, 0);
        str = "";

        char thisChar = '\0';
        char lastChar = '\0';
        for(int i = 0; i <= cArray.length - 1; i++)
        {
            thisChar = cArray[i];
            if(Character.isLowerCase(lastChar) && Character.isUpperCase(thisChar))
            {
                str = str + ' ';
            }
            // new MessageDlg("to step thu " + cArray[i]);
            str = str + cArray[i];
            lastChar = cArray[i];
        }
        return str;
    }

    /**
     * Will convert a name such as
     * <myVariableName>
     * into something readable
     * <My Variable Name>
     * <p/>
     * uppercase first char
     * when find an uppercase first put a space
     */
    public static String variableToDisplay(String str)
    {
        String result = null;
        if(str != null)
        {
            StringBuffer out = new StringBuffer();
            char cArray[] = new char[str.length()];
            str.getChars(0, str.length(), cArray, 0);

            char thisChar = '\0';
            char lastChar = '\0';
            appendToStringBuffer(out, Character.toUpperCase(cArray[0]));
            for(int i = 1; i <= cArray.length - 1; i++)
            {
                thisChar = cArray[i];
                if(/* Character.isLowerCase( lastChar) &&*/
                    Character.isUpperCase(thisChar))
                {
                    appendToStringBuffer(out, ' ');
                }
                // new MessageDlg("to step thu " + cArray[i]);
                appendToStringBuffer(out, cArray[i]);
                lastChar = cArray[i];
            }
            result = new String(out);
        }
        // Err.pr( "variableNameToDisplayName returning " + result);
        return result;
    }

    public static String displayToVariable(String str)
    {
        String result = null;
        if(str != null)
        {
            StringBuffer out = new StringBuffer();
            char cArray[] = new char[str.length()];
            str.getChars(0, str.length(), cArray, 0);

            char thisChar = '\0';
            char lastChar = '\0';
            appendToStringBuffer(out, Character.toLowerCase(cArray[0]));
            for(int i = 1; i <= cArray.length - 1; i++)
            {
                thisChar = cArray[i];
                if(!Character.isSpaceChar(thisChar))
                {
                    appendToStringBuffer(out, cArray[i]);
                }
                /*
                else
                {
                cArray[i+1] = cArray[i+1]
                }
                */
                lastChar = cArray[i];
            }
            result = new String(out);
        }
        // Err.pr( "variableNameToDisplayName returning " + result);
        return result;
    }

    /**
     * Example:
     * From data.needs.objects
     * Get  data.needs
     * , as have stripped off the last package component
     */
    public static String baseOfPackage(String wholePackage)
    {
        String result = "";
        String packs[] = new String[10];
        StringTokenizer st = new StringTokenizer(wholePackage, ".");
        int indents = 0;
        for(int i = 0; st.hasMoreTokens(); i++)
        {
            packs[i] = st.nextToken();
            indents++;
        }
        for(int i = 0; i <= indents - 2; i++)
        {
            result += packs[i];
            if(i < indents - 2)
            {
                result += ".";
            }
        }
        return result;
    }

    /**
     * Example:
     * From applic.needs
     * Get  needs
     * , as have stripped off everything b4 last package component
     */
    public static String tailOfPackage(String wholePackage)
    {
        String result = "";
        String packs[] = new String[10];
        StringTokenizer st = new StringTokenizer(wholePackage, ".");
        int indents = 0;
        for(int i = 0; st.hasMoreTokens(); i++)
        {
            packs[i] = st.nextToken();
            indents++;
        }
        result = packs[indents - 1];
        return result;
    }

    public static String findFileAtEndOfPath(String filename)
    {
        String result = null;
        int lastSlashIdx = filename.lastIndexOf(File.separatorChar);
        if(lastSlashIdx > 0 && lastSlashIdx < filename.length() - 1)
        {
            result = filename.substring(lastSlashIdx + 1);
        }
        return result;
    }

    /**
     * Get the extension of a String.
     */
    public static String getExtension(String s)
    {
        // Err.pr( "IN: " + s);
        String ext = null;
        int i = s.lastIndexOf('.');
        if(i > 0 && i < s.length() - 1)
        {
            ext = s.substring(i + 1).toLowerCase();
        }
        // Err.pr( "OUT: " + ext);
        return ext;
    }

    /**
     * Method endsLikeDirectory.
     *
     * @param s
     * @return String
     */
    public static String endsLikeDirectory(String s)
    {
        String result;
        if(s == null || s.endsWith(File.separator))
        {
            result = s;
        }
        else
        {
            result = s + File.separator;
        }
        return result;
    }

    public static String dotsToSlashes(String s)
    {
        String result;
        result = s.replace('.', File.separatorChar);
        return result;
    }

    public static String slashesToDots(String s)
    {
        String result;
        result = s.replace(File.separatorChar, '.');
        return result;
    }

    public static String endOfDots(String s)
    {
        String result;
        int lastDot = s.lastIndexOf('.');
        result = s.substring(lastDot + 1, s.length());
        return result;
    }

    public static String getFullFilePath(String diry, String pack)
    {
        String result = null;
        String secondBit = dotsToSlashes(pack);
        result = diry + File.separatorChar + secondBit;
        return result;
    }

    public static String getFullFileName(String diry, String pack, String name)
    {
        String result = null;
        String baseName = getFullFilePath(diry, pack);
        result = baseName + File.separatorChar + name;
        return result;
    }

    public static String getFullFileName(String diry, String name)
    {
        String result = null;
        if(diry != null)
        {
            result = diry + File.separatorChar + name;
        }
        else
        {
            result = name;
        }
        return result;
    }

    public static String getJavaName(String pack, String name)
    {
        String result = null;
        if(pack != null)
        {
            result = pack + "." + name;
        }
        else
        {
            result = name;
        }
        return result;
    }

    public static String getFullFileNameExt(String diry, String pack, String name, String ext)
    {
        String result = null;
        String baseName = getFullFileName(diry, pack, name);
        result = baseName + "." + ext;
        return result;
    }

    public static String findClassAtEndOfPackage(String packAndClass)
    {
        String result = null;
        int whereDot = packAndClass.lastIndexOf('.');
        result = packAndClass.substring(whereDot + 1);
        return result;
    }

    public static String removeEnding(String in, String ending)
    {
        String result = in;
        int index = in.lastIndexOf(ending);
        if(index != -1)
        {
            result = in.substring(0, index);
        }
        else
        {
            Err.error("Expect name <" + in + "> to have ending: <" + ending + ">");
        }
        return result;
    }

    public static String removeBeginning(String in, String beginning)
    {
        String result = in;
        int index = in.indexOf(beginning);
        if(index != -1)
        {
            result = in.substring(beginning.length(), in.length());
        }
        else
        {
            Err.error("Expect name <" + in + "> to have beginning: <" + beginning + ">");
        }
        return result;
    }

    public static void trimEnding(List names, String ending)
    {
        for(int i = 0; i < names.size(); i++)
        {
            String element = (String) names.get(i);
            int index = element.indexOf(ending);
            if(index != -1)
            {
                String trimmed = element.substring(0, index);
                names.set(i, trimmed);
            }
            else
            {
                Err.error("Expect all names to have ending " + ending);
            }
        }
    }

    public static boolean endsIn(String extension, String name)
    {
        //String name = file.getName();
        if(name.endsWith("." + extension))
        {
            return true;
        }
        return false;
    }

    public static boolean hasOneExtension(File file)
    {
        String name = file.getName();
        boolean result = true;
        int index = name.indexOf('.', 0);
        if(index != -1)
        {
            int secondIndex = name.indexOf('.', index + 1);
            if(secondIndex != -1)
            {
                result = false;
            }
        }
        else
        {
            result = false;
        }
        return result;
    }

    public static String giveNewExtension(String fileName, String newExtension)
    {
        String newFileName;
        int index = fileName.lastIndexOf('.');
        if(index == -1)
        {
            throw new Error(fileName + " should have an extension");
        }
        else
        {
            if(newExtension != null)
            {
                newFileName = fileName.substring(0, index) + "." + newExtension;
            }
            else
            {
                newFileName = fileName.substring(0, index);
            }
        }
        return newFileName;
    }

    public static String upperCaseFirstChar( String sentence)
    {
        StringBuffer result = new StringBuffer();
        if(!Utils.isBlank( sentence))
        {
            int i=0;
            for(StringTokenizer stringTokenizer = new StringTokenizer(sentence);
                stringTokenizer.hasMoreTokens(); i++)
            {
                String str = stringTokenizer.nextToken();
                char character = str.charAt(0);
                char upperCharacter = Character.toUpperCase(character);
                String firstChar = Character.toString(upperCharacter);
                String newStr = firstChar.concat(str.substring(1));
                if(i > 0)
                {
                    result.append( " ");
                }
                result.append( newStr);
            }
        }
        return result.toString();
    }

    public static String lowerCaseFirstChar(String str)
    {
        char character = str.charAt(0);
        char lowerCharacter = Character.toLowerCase(character);
        String firstChar = Character.toString(lowerCharacter);
        return firstChar.concat(str.substring(1));
    }

    /**
     * Whereever we find a '\', replace it with '\\'.
     */
    public static String stringifyFileName(String filename)
    {
        StringBuffer result = new StringBuffer();
        char special = '\\';
        if(filename != null)
        {
            char cArray[] = new char[filename.length()];
            filename.getChars(0, filename.length(), cArray, 0);

            char thisChar = '\0';
            for(int i = 0; i <= cArray.length - 1; i++)
            {
                thisChar = cArray[i];
                if(thisChar == special)
                {
                    appendToStringBuffer(result, thisChar);
                    appendToStringBuffer(result, thisChar);
                }
                else
                {
                    appendToStringBuffer(result, thisChar);
                }
            }
        }
        return result.toString();
    }
    
    public static String colourInVariables( String txt)
    {
        return colourInVariables( txt, "#BC32FF");
    }

    public static String colourInVariables( String txt, Color colour)
    {
        String colourStr = "#" + Integer.toHexString((colour.getRGB() & 0xffffff) | 0x1000000).substring(1);
        return colourInVariables( txt, colourStr);
    }

    public static String colourInVariables( String txt, String hexColour)
    {
        StringBuffer result = new StringBuffer();
        Assert.isTrue( hexColour.indexOf( "#") == 0, "Specify hex colour with a '#' at the front");
        final char startCh = '<';
        final char endCh = '>';
        if(txt != null)
        {
            char cArray[] = new char[txt.length()];
            txt.getChars(0, txt.length(), cArray, 0);

            for(int i = 0; i <= cArray.length - 1; i++)
            {
                char thisChar = cArray[i];
                if(thisChar == startCh)
                {
                    result.append( "<span color=");
                    result.append( hexColour);
                    result.append( ">");
                }
                else if(thisChar == endCh)
                {
                    result.append("</span>");
                }
                else
                {
                    appendToStringBuffer(result, thisChar);
                }
            }
        }
        return result.toString();
    }
    
    public static String colourInVariables(String msg[])
    {
        List colouredIn = new ArrayList();
        for(int i = 0; i < msg.length; i++)
        {
            String s = msg[i];
            colouredIn.add(colourInVariables(s));
        }
        return toHTML(colouredIn);
    }

    public static String colourInVariables(List msg)
    {
        List colouredIn = new ArrayList();
        for(Iterator iterator = msg.iterator(); iterator.hasNext();)
        {
            String s = (String) iterator.next();
            colouredIn.add(colourInVariables(s));
        }
        return toHTML(colouredIn);
    }

    public static String toHTML(String str)
    {
        List list = new ArrayList();
        list.add(str);
        return toHTML(list);
    }

    public static String toHTML(List msg)
    {
        String result;
        if(msg.size() > 1)
        {
            String wholeMsg = "";
            for(int i = 0; i < msg.size(); i++)
            {
                wholeMsg += msg.get(i) + "<br>";
            }
            result = "<html><p>" + wholeMsg + "</p></html>";
        }
        else
        {
            result = "<html><p>" + msg.get(0) + "</p></html>";
        }
        return result;
    }

    public static String lineHTML( String in)
    {
        StringBuffer result = new StringBuffer();
        result.append( "<html>");
        result.append( in);
        result.append( "</html>");
        return result.toString();
    }

    public static String multilineHTML( String in)
    {
        StringBuffer result = new StringBuffer();
        String words[] = in.split( "/");
        //Print.prArray( words, "split words");
        result.append( "<html>");
        for (int i = 0; i < words.length; i++)
        {
            String word = words[i];
            result.append( word);
            if(i < words.length-1)
            {
                result.append( "<br>/<br>");
            }
        }
        result.append( "</html>");
        return result.toString();
    }

    /**
     * With 1.5 an IOException can be thrown.
     * When change comments, also comment around line 466
     * of RepaintManager
     */
    static void appendToStringBuffer(StringBuffer buf, char c)
    {
        // try
        {
            buf.append(c);
        }
        // catch(IOException ex)
        // {
        // Err.error( ex);
        // }
    }


    public static String getLongestLine( String text, List<String> partsToRemove, String separator)
    {
        String result = null;
        String toInvestigate = removeParts( text, partsToRemove);
        String longest = "";
        String words[] = toInvestigate.split( separator);
        for (int i = 0; i < words.length; i++)
        {
            String word = words[i];
            if(word.length() > longest.length())
            {
                longest = word;
            }
        }
        if(!longest.equals( ""))
        {
            result = longest;
        }
        return result;
    }

    public static String removeParts( String text, List<String> parts)
    {
        String result;
        if(text != null)
        {
            for (int i = 0; i < parts.size(); i++)
            {
                String part = parts.get(i);
                while(text.contains( part))
                {
                    text = remove( text, part);
                }
            }
        }
        result = text;
        return result;
    }

    /**
     * If result == in then nothing happened so don't do it again
     *
     * @param in
     * @param toRemove
     * @return
     */
    public static String remove(String in, String toRemove)
    {
        String result = in;
        int index = in.indexOf(toRemove);
        if(index != -1)
        {
            String before = in.substring(0, index);
            String after = in.substring(index+toRemove.length(), in.length());
            result = before.concat( after);
        }
        return result;
    }

    public static void main(String[] args)
    {
//        String in = "At end number 78";
//        int replace = 89;
//        String out = replaceLastTok(in, "89");
//        Err.pr("Changed " + in + " to " + out);

//        String in = "#2005-01-01#";
//        String beginGone = removeBeginning( in, "#");
//        Err.pr( "beginGone: <" + beginGone + ">");
//        String endGone = removeEnding( in, "#");
//        Err.pr( "endGone: <" + endGone + ">");
//        String bothGone = removeEnding( beginGone, "#");
//        Err.pr( "bothGone: <" + bothGone + ">");

//        String in = "stats_2005_07_01";
//        String beginGone = removeBeginning(in, "stats_");
//        Err.pr("beginGone: <" + beginGone + ">");

        /*
        String redHex = "#FF0000";
        String example = "What <Legend> is that?";
        String colouredIn = colourInVariables( example, redHex);
        Err.pr( "result: " + colouredIn);
        JLabel label = new JLabel();
        String asHTML = toHTML( colouredIn);
        label.setText( asHTML);
        Err.pr( "result as HTML: " + asHTML);
        DisplayUtils.displayInDialog( label);
        */

        /*
        String source = "<html>Restaurant <br>/<br> All Consumables</html>";
        List<String> parts = new ArrayList<String>();
        parts.add( "<html>");
        parts.add( "</html>");
        String result = getLongestLine( source, parts, "<br>");
        Err.pr( result);
        */
        String source = "Restaurant / All Consumables";
        String target = "<html>Restaurant <br>/<br> All Consumables</html>";
        Err.pr( "Got:  " + multilineHTML( source));
        Err.pr( "Want: " + target );
    }

}
