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
package org.strandz.core.interf;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class can collect and provide a report on how swapping a new
 * panel into a SdzBagI went. You swap in a new panel every time you
 * substitute one of the panels for a different one. The report will indicate
 * how the substitution process fared. For example if an equivalently named
 * control was not found in the new panel, then what was once a FieldAttribute
 * might now become a StemAttribute.
 *
 * @author Chris Murphy
 */
public class PanelUpdateInfo
{
    public String errorMsg;
    // Set at once
    public List subsequentErrorMsgs;
    // Are added one by one
    public List informationalMsgs = new ArrayList();

    public boolean isInError()
    {
        boolean result = (errorMsg != null);
        /*
        Err.pr( "%%% PanelUpdateInfo.isInError() to return " + result);
        if(result)
        {
        Err.debug();
        }
        */
        return result;
    }

    public boolean hasInfo()
    {
        return (informationalMsgs != null && !informationalMsgs.isEmpty());
    }

    public void addInfo(String info)
    {
        informationalMsgs.add(info);
    }

    public void addInfo(List info)
    {
        informationalMsgs.addAll(info);
    }

    public void setError(String txt)
    {
        if(isInError())
        {
            Err.error("Should not be setting in error when already in error");
        }
        errorMsg = txt;
    }

    public void setRestOfError(List list)
    {
        if(subsequentErrorMsgs != null)
        {
            Err.error(
                "Should not be setting additional error messages when already in error");
        }
        subsequentErrorMsgs = list;
    }

    /**
     * Only need to append informational stuff. The error stuff will always
     * be used straight away ie. when get an error, the program does not
     * continue to load messages into this class.
     * Actually the error part will need to be appended as well, as the error
     * part is sometimes handed over to another PanelUpdateInfo.
     *
     * @param info
     */
    public void append(PanelUpdateInfo info)
    {
        informationalMsgs.addAll(info.informationalMsgs);
        if(isInError())
        {
            Err.error("Should not be appending when are already in error");
        }
        setError(info.errorMsg);
        setRestOfError(info.subsequentErrorMsgs);
    }

    public String toString()
    {
        StringBuffer result = new StringBuffer();
        if(isInError())
        {
            // informationalMsgs may also exist that may want to display
            result.append(errorMsg);
            if(subsequentErrorMsgs != null)
            {
                result.append(Utils.getStringBufferFromList(subsequentErrorMsgs));
            }
        }
        else
        {
            result.append(Utils.getStringBufferFromList(informationalMsgs));
        }
        return result.toString();
    }
}
