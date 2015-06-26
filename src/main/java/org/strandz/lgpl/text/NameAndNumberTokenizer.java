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
package org.strandz.lgpl.text;

import org.apache.commons.lang.math.NumberUtils;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;

import java.util.List;
import java.util.Iterator;

/**
 * Redefines a word to be either a known name or a number
 */
public class NameAndNumberTokenizer implements WordTokenizer
{
    /**
     * Tokens for this WordTokenizer are made up of several from textTokenizer 
     */
    private TextTokenizer textTokenizer;
    private StringBuffer input;
    private int numberSpacing;
    /**
     * Known names
     */
    private List<String> recognisedNames;
    private int beginPos;
    private int endPos;
    
    public static void main(String[] args)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append( "123");
        buffer.append( "  ");
        buffer.append( "Diamond Dog");
        buffer.append( "  ");
        buffer.append( "456");
        buffer.append( "  ");
        buffer.append( "789");
        NameAndNumberTokenizer reader = new NameAndNumberTokenizer( buffer, 6);
        reader.setRecognisedNames( Utils.formList( "Diamond Dog"));
        String token;
        while(!(token = reader.readToken()).equals( ""))
        {
            Err.pr( "<" + token + ">");
        }
    }
    
    /**
     * Use this constructor where do not expect to have any numbers
     * @param input buffer will read words from
     */
    public NameAndNumberTokenizer(StringBuffer input)
    {
        textTokenizer = new TextTokenizer( input);        
        this.input = input;
        this.numberSpacing = Utils.UNSET_INT;
    }

    /**
     * @param input buffer will read words from
     */
    public NameAndNumberTokenizer(StringBuffer input, int numberSpacing)
    {
        textTokenizer = new TextTokenizer( input);        
        this.input = input;
        this.numberSpacing = numberSpacing;
    }

    /**
     * 
     * @param recognisedNames Known names - that may have whitespaces in them
     */
    public void setRecognisedNames(List<String> recognisedNames)
    {
        this.recognisedNames = recognisedNames;
    }
    
    private boolean isRecognised( String token)
    {
        boolean result = false;
        for(Iterator<String> iterator = recognisedNames.iterator(); iterator.hasNext();)
        {
            String name = iterator.next();
            if(token.indexOf( name) != -1)
            {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 
     * @return
     */
    public String readToken()
    {
        String result = "";
        boolean wordFound = false;
        beginPos = endPos;
        boolean nameRecognising = false;
        while(!wordFound)
        {
            String word = textTokenizer.readToken();
            //Err.pr( "word got: <" + word + ">");
            if(word.equals( ""))
            {
                break;
            }
            if(!nameRecognising && NumberUtils.isNumber( word))
            {
                endPos = textTokenizer.position();                            
                wordFound = true;
            }
            else
            {
                endPos = textTokenizer.position();                            
                String toExamine = input.substring( beginPos, endPos);
                if(isRecognised( toExamine))
                {
                    wordFound = true;
                    nameRecognising = false;
                }
                else if(textTokenizer.isFinished())
                {
                    Print.prList( recognisedNames, "recognisedNames");
                    Err.error( "<" + toExamine + "> has not been recognised");
                }
                else
                {
                    nameRecognising = true;
                }
            }
        }
        if(wordFound)
        {
            String tatty = input.substring( beginPos, endPos);
            String notSpaced = tatty.trim();
            if(NumberUtils.isNumber( notSpaced))
            {
                result = Utils.leftPadSpace( notSpaced, numberSpacing);
            }
            else
            {
                result = notSpaced;
            }
        }
        return result;
    }
}
