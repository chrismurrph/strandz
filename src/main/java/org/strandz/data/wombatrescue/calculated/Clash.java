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
package org.strandz.data.wombatrescue.calculated;

import org.strandz.data.wombatrescue.objects.WeekInMonth;
import org.strandz.data.wombatrescue.objects.WorkerI;
import org.strandz.lgpl.util.Utils;

import java.io.Serializable;

public class Clash implements Serializable
{
    WorkerI tryWorker;
    WorkerI establishedWorker;
    Night night;
    boolean bump = false; // if true means the opposite, established bumped
    String reason;
    private String notOpenSundayText; 
    private String closedText;

    public Clash( String reason)
    {
        this.reason = reason;
        // Err.stack( reason);
    }

    /**
     * Both formats a sentence and injects params for later use
     * @param notOpenSundayText
     * @param closedText
     * @return
     */
    String formattedClash( String notOpenSundayText, String closedText)
    {
        String result = formSentence( notOpenSundayText, closedText, true);
        this.notOpenSundayText = notOpenSundayText;
        this.closedText = closedText;
        return result;
    }

    /**
     * @param extras True to get a full sentence - a style which is no longer used
     * @return
     */
    private String formSentence( String notOpenSundayText, String closedText, boolean extras)
    {
        //String result = "Already have volunteer called " + establishedWorker
        //    + ", was trying " + tryWorker;
        String result = tryWorker + " will not go where " + establishedWorker + " is";
        if(extras)
        {
            result += ": " + Utils.NEWLINE + night.formattedNight( notOpenSundayText, closedText);
        }
        if(bump)
        {
            result = establishedWorker + " has been bumped by " + tryWorker;
            if(extras)
            {
                result += ": " + Utils.NEWLINE + night.formattedNight( notOpenSundayText, closedText);
            }
        }
        if(extras)
        {
            result += " because " + reason;
        }
        return result;
    }
        
    public WeekInMonth getWeekInMonth()
    {
        return night.getWeekInMonth();
    }

    /**
     * Should only be called after formattedClash has injected notOpenSundayText and closedText   
     */
    public String getToSentence()
    {
        return formSentence( notOpenSundayText, closedText, false);
    }
    
    public String getToReason()
    {
        return reason;
    }
    
    public ParticularShift getFirstEvening()
    {
        return night.getFirstEvening();
    }

    public ParticularShift getSecondEvening()
    {
        return night.getSecondEvening();
    }

    public ParticularShift getThirdEvening()
    {
        return night.getThirdEvening();
    }

    public ParticularShift getFirstOvernight()
    {
        return night.getFirstOvernight();
    }

    public ParticularShift getSecondOvernight()
    {
        return night.getSecondOvernight();
    }
    
    public String getFormattedDate()
    {
        return night.getFormattedDate();
    }
    
    public ParticularShift getParticularShift( int idx)
    {
        ParticularShift result = night.getParticularShift( idx);
        return result;
    }
}
