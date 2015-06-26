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
package org.strandz.core.info.impl.swing;

import org.strandz.core.domain.ItemAdapter;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Err;

import javax.swing.JComboBox;
import java.awt.Component;
import java.awt.event.MouseListener;
import java.lang.reflect.Method;

public class JComboBoxMoreComplexMethods extends ComponentMoreComplexMethods
{
    JComboBox defaultCombo = new JComboBox();

    public static void main(String[] args)
    {
        JComboBoxMoreComplexMethods obj = new JComboBoxMoreComplexMethods();
        Err.pr(obj.defaultCombo.getRenderer());
        Err.pr(obj.defaultCombo.getEditor());
    }

    /* Let super do it
    public Color getBGColor(Object fieldComponent)
    {
      Color result = null;
      JComboBox combo = (JComboBox)fieldComponent;
      result = ((Component)combo.getEditor().getEditorComponent()).getBackground();
      return result;
    }
    */

    /*
    * Does not work!
    */
    public void setBGColor(Object fieldComponent, Object color)
    {
        /*
        super.setBGColor( fieldComponent, color);
        JComboBox combo = (JComboBox)fieldComponent;
        ((Component)combo.getEditor().getEditorComponent()).setBackground(
            (Color)color);
        */
        Err.pr(SdzNote.COMBO_BEING_RED, "Does not work, so not trying");
    }

    public void blankComponent(Object fieldComponent)
    {
        // Is difficult to get this component back to normal
        JComboBox combo = (JComboBox) fieldComponent;
        boolean canEdit = combo.isEditable();
        if(!canEdit)
        {
            combo.setEditable(true);
        }
        combo.removeAllItems();
        combo.setSelectedIndex(-1);
        if(!canEdit)
        {
            combo.setEditable(false);
        }
        /*
        combo.setEditor( defaultCombo.getEditor());
        combo.setRenderer( defaultCombo.getRenderer());
        Err.pr( "rmed all items from " + combo.getName());
        */
    }

    public void setComponent(Object fieldComponent, Object obj)
    {
        // Is difficult to get this component back to normal
        JComboBox combo = (JComboBox) fieldComponent;
        boolean canEdit = combo.isEditable();
        if(!canEdit)
        {
            combo.setEditable(true);
        }
        combo.setSelectedItem(obj);
        if(!canEdit)
        {
            combo.setEditable(false);
        }
    }

    public void uninstallListeners(Object fieldComponent, Method removeActionListenerMethod)
    {
        Component comp = (Component) fieldComponent;
        JComboBox combo = (JComboBox) comp;
        removeActionListeners(combo.getEditor().getEditorComponent(),
            removeActionListenerMethod);
        removeClickListeners(comp);
    }

    public void installClickListener(Object fieldComponent, Object itemAdapter)
    {
        Component comp = (Component) fieldComponent;
        ClickListener adapter = createClickListener( comp, itemAdapter);
        JComboBox combo = (JComboBox) comp;
        combo.getEditor().getEditorComponent().addMouseListener(adapter);
        comp.addMouseListener(adapter);
    }

    void removeClickListeners(Component comp)
    {
        super.removeClickListeners(comp);
        JComboBox combo = (JComboBox) comp;
        MouseListener fls[] = combo.getEditor().getEditorComponent().getMouseListeners();
        for(int i = 0; i < fls.length; i++)
        {
            if(fls[i] instanceof ClickListener)
            {
                comp.removeMouseListener(fls[i]);
            }
        }
    }

    public void installRClickRestore(Object fieldComponent, Object itemAdapter)
    {
        JComboBox combo = (JComboBox) fieldComponent;
        Component comboComp = combo.getEditor().getEditorComponent();
        addRClickListener(comboComp, (ItemAdapter) itemAdapter);
    }

    public void uninstallRClickRestore(Object fieldComponent)
    {
        JComboBox combo = (JComboBox) fieldComponent;
        removePopupListeners(combo.getEditor().getEditorComponent());
    }
}
