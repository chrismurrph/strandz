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
package org.strandz.core.applichousing;

import org.strandz.core.domain.NodeChangeEvent;
import org.strandz.core.domain.NodeChangeListener;
import org.strandz.core.domain.constants.AccessEnum;
import org.strandz.core.domain.event.AccessEvent;
import org.strandz.core.interf.AbstStrand;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.IdentifierI;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.StringTokenizer;

/**
 * Data aware NodeBar component.  If AbstStrand property is set, the NodeBar
 * displays error and status messages related to operations on the AbstStrand.
 */
public class NodeBar extends JPanel
    implements NodeChangeListener
{
    public NodeBar()
    {
        super();
        setLayout(new BorderLayout());
        add(label, BorderLayout.CENTER);
    }

    /**
     * Gets the current alignment of this NodeBar.
     *
     * @see #setAlignment
     */
    public int getHorizontalAlignment()
    {
        return label.getHorizontalAlignment();
    }

    /**
     * Sets the alignment for this NodeBarto the specified alignment.
     *
     * @param alignment the alignment value
     * @throws IllegalArgumentException If an improper alignment was given.
     * @see #getAlignment
     */
    public void setHorizontalAlignment(int alignment)
    {
        label.setHorizontalAlignment(alignment);
    }

    /**
     * Gets the text of this NodeBar.
     *
     * @see #setText
     */
    public String getText()
    {
        return label.getText();
    }

    public boolean getPackageVisible()
    {
        return packageVisible;
    }

    public void setPackageVisible(boolean b)
    {
        packageVisible = b;
    }

    /**
     * The controller property specifies a AbstStrand
     * object to display the current Node from in the
     * NodeBar.
     */
    public Object getController()
    {
        return strand;
    }

    public void setController(AbstStrand strand)
    {
        removeListeners();
        this.strand = strand;
        if(strand == null)
        {
            isOpen = false;
        }
        updateValue();
        addListeners();
    }

    private final void removeListeners()
    {
        if(strand != null)
        {
            strand.removeNodeChangeListener(this);
        }
    }

    private final void addListeners()
    {
        if(strand != null)
        {
            try
            {
                strand.addNodeChangeListener(this);
            }
            catch(Exception ex)
            {
                Err.error("nasty exception");
            }
        }
    }

    private String removePackage(String fullPath)
    {
        if(fullPath == null)
        {
            return null;
        }

        String result = new String();
        /*
        * Seems like 1 too many tokens to me, but works!
        * ie reading nextToken 1 more time than actual
        * number of tokens. DIDN'T work when added 1 more,
        * so switched to using hasMoreTokens
        */
        StringTokenizer st = new StringTokenizer(fullPath, ".");
        // new MessageDlg( "no of tokens from " + fullPath + " is " + st.countTokens());
        // for(int i=0; i<=st.countTokens()-1; i++)
        while(st.hasMoreTokens())
        {
            result = st.nextToken();
            // new MessageDlg("token have gone to is " + result);
        }
        return result;
    }

    protected void updateValue()
    {
        if(isOpen)
        {
            if(packageVisible)
            {
                label.setText(blockLabelText); // will set to null if haven't had a nodeChangePerformed
                label.setToolTipText(blockLabelText);
            }
            else
            {
                String str = removePackage(blockLabelText);
                label.setText(str);
                label.setToolTipText(str);
            }
        }
        else
        {
            String str = "";
            label.setText(str);
            label.setToolTipText(str);
        }
    }

    /**
     * Returns the string value of the NodeBar's current value
     */

    public String toString()
    {
        return getText();
    }

    /*
    Don't need the extra width - may later incorporate width of getText
    public Dimension getPreferredSize() {
    Dimension d = super.getPreferredSize();
    if (d.width < 200)
    d.width = 200;
    return d;
    }
    */

    public void nodeChangePerformed(NodeChangeEvent e)
    {
        // Err.pr("Received NodeChangeEvent: " + e);
        // Err.pr("params: " + e.getSource());
        blockLabelText = e.getSource().toString();
        updateValue();
    }

    /**
     * Implementation of NodeChangeListener, where it happens as
     * the user changes from one Node to the other at design-time
     * (NodeCollectingState)
     */
    public void accessChange(AccessEvent event)
    {
        Err.error("accessChange not called any more");
        // open or close will be written in blank:
        blockLabelText = null; // thus will need to both OPEN and send

        // nodeChangePerformed to show anything
        AccessEnum id = event.getID();
        if(id == AccessEvent.OPEN)
        {
            // Err.pr("~~ OPEN NodeChangeListener.AccessEvent");
            isOpen = true;
            updateValue();
        }
        else if(id == AccessEvent.CLOSE)
        {
            isOpen = false;
            updateValue();
        }
        else
        {
            Err.error("AccessEvent only supports OPEN and CLOSE");
        }
    }

    public IdentifierI getNode()
    {
        return null;
    }

    protected AbstStrand strand;
    protected JLabel label = new JLabel();
    private boolean isOpen = false;
    private String blockLabelText;
    private boolean packageVisible = false;
}
