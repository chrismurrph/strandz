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
package org.strandz.lgpl.widgets;

import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.BeansUtils;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Print;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Assert;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Replacement for ButtonGroup that can be used in the same way. Unlike
 * ButtonGroup this class can be used as a component, for which you can
 * simply get and set its text. This makes life easier when you are
 * mapping to a DO's field. Also this class incorporates being able
 * to visually not select anything. Is a wrapper around a ButtonGroup
 * and can be XMLEncoded.
 */
public class OneOnlyGroup implements ActionListener
{
    //TODO It would be less confusing to only have this ButtonGroup exist at display time,
    //which is when newButton is called
    private ButtonGroup radioGroup; //half portable - we re-create its contents at display time
    private String lastSelectedText;
    private boolean enabled = true; //not sure which way round should be
    private static boolean debug = false;
    private JRadioButton rbNone = new JRadioButton(NONE);

    public static final String NONE = "";

    public void add(AbstractButton b)
    {
        if(getRadioGroup() == null)
        {
            setRadioGroup(new ButtonGroup());
            newButton(rbNone); //as recommended in ButtonGroup JavaDoc
         }
        newButton(b);
        visualSelection(lastSelectedText, radioGroup);
    }

    public void newButton(AbstractButton b)
    {
        Assert.notNull( b, "No button supplied - perhaps the getter/setter combination does not exist");
        getRadioGroup().add(b);
        //Err.pr( "DT is " + Beans.isDesignTime());
        if(!BeansUtils.isDesignTime())
        {
            b.addActionListener( this);
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        AbstractButton source = (AbstractButton) e.getSource();
        if(debug)
        {
            Err.alarm("--------------------Clicked on " + source.getText());
        }
        lastSelectedText = source.getText();
    }

    /**
     * Won't be the same as lastSelectedText if the user has selected a
     * different radio button - but InternalActionListener changed this
     *
     * @return
     */
    public String getSelectedText()
    {
        String result = lastSelectedText;
        /*
         * Won't work
        if(radioGroup != null && radioGroup.getSelection() != null) //DT
        {
          result = radioGroup.getSelection().getActionCommand();
        }
        */
        return result;
    }

    public void setSelectedText(String selectedText)
    {
        if(selectedText == null && SdzNote.GENERIC.isVisible())
        {
            Err.debug();
        }
        visualSelection(selectedText, radioGroup);
        /*
         * Won't work
        if(radioGroup != null && radioGroup.getSelection() != null) //DT
        {
          if(radioGroup.getSelection().getActionCommand() == null)
          {
            radioGroup.getSelection().setActionCommand( selectedText);
          }
        }
        */
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean b)
    {
        if(b != enabled)
        {
            if(radioGroup != null)
            {
                for(Enumeration enumId = radioGroup.getElements(); enumId.hasMoreElements();)
                {
                    AbstractButton button = (AbstractButton) enumId.nextElement();
                    button.setEnabled(b);
                }
            }
            enabled = b;
        }
    }

    private List getPossibleSelections()
    {
        List result = new ArrayList();
        for(Enumeration enumId = radioGroup.getElements(); enumId.hasMoreElements();)
        {
            String text = ((AbstractButton) enumId.nextElement()).getText();
            if(text != NONE)
            {
                result.add(text);
            }
        }
        return result;
    }

    private AbstractButton getButtonFromName(String name)
    {
        AbstractButton result = null;
        if(name != NONE)
        {
            for(Enumeration enumId = radioGroup.getElements(); enumId.hasMoreElements();)
            {
                AbstractButton button = (AbstractButton) enumId.nextElement();
                if(button.getText().equals(name))
                {
                    result = button;
                    break;
                }
            }
        }
        if(result == null)
        {
            result = rbNone;
        }
        return result;
    }

    private void visualSelection(
        String selectedText, ButtonGroup radioGroup)
    {
        if(radioGroup != null)
        {
            if(radioGroup.getButtonCount() == 0)
            {
                /**
                 * TODO
                 * This workaround is only needed because OneOnlyGroup has not been written well
                 * enough as to be portable (XMLEncode/Decode)
                 */
                Err.error("Buttons lost in XML transfer, see how FlexibilityRadioButtons as an example");
            }
            boolean noSelection = true;
            if(selectedText != null)
            {
                if(getPossibleSelections().contains(selectedText))
                {
                    if(!Utils.equals(this.lastSelectedText, selectedText))
                    {
                        getButtonFromName(selectedText).setSelected(true);
                        this.lastSelectedText = selectedText;
                    }
                    noSelection = false;
                }
                if(noSelection)
                {
                    Print.prList(getPossibleSelections(), "OneOnlyGroup possibilities");
                    //Need to find a good reason if want to comment this out ie. keep it!
                    Err.error("Nothing being selected in OneOnlyGroup as <" + selectedText + "> is not a possibility");
                }
            }
            if(noSelection)
            {
                getButtonFromName(NONE).setSelected(true);
                this.lastSelectedText = null;
            }
        }
    }

    public ButtonGroup getRadioGroup()
    {
        return radioGroup;
    }

    public void setRadioGroup(ButtonGroup radioGroup)
    {
        this.radioGroup = radioGroup;
    }

    public JRadioButton getRbNone()
    {
        return rbNone;
    }

    public void setRbNone(JRadioButton rbNone)
    {
        this.rbNone = rbNone;
    }
}
