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
package org.strandz.lgpl.view;

import org.strandz.lgpl.tablelayout.ModernTableLayout;
import org.strandz.lgpl.util.Err;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.util.List;

public class SecurePanel extends JPanel
{
    public JTextField tfUsername;
    public JComboBox cbUsername;
    protected JPasswordField tfPassword;
    protected JLabel lUsername;
    protected JLabel lPassword;
    protected JButton bOk;
    protected JButton bCancel;
    protected TranslatorI translator;
    
    //static final int MID_GAP = 7;
    protected static final int GAP = 15;
    protected static final int BORDER = 20;
    protected static final int SMALL_SPACING = 2;
    protected static final int TEXT_HEIGHT = 23;
    protected static final int TEXT_WIDTH = 75;
    
    public static final String OK = "OK";
    public static final String CANCEL = "Cancel";

    /**
     * Required to stop the view package depending on other packages
     */
    public interface TranslatorI
    {
        String translateForUserCombo( String selectedItem);    
    }

    public SecurePanel(TranslatorI translator)
    {
        this.translator = translator;
    }

    public JComboBox getRoleComponent()
    {
        return null;
    }

    public void init( boolean useComboForUser)
    {
        if(useComboForUser)
        {
            cbUsername = new JComboBox();
            //On select put the user's number into here so works as did before, even thou textfield never seen
            tfUsername = new JTextField();
        }
        else
        {
            tfUsername = new JTextField();
        }
        tfPassword = new JPasswordField();
        lUsername = new JLabel();
        lPassword = new JLabel();
        doInitLayout();        
        add(lUsername, "1, 1");
        if(useComboForUser)
        {
            add(cbUsername, "3, 1");
        }
        else
        {
            add(tfUsername, "3, 1");
        }
        add(lPassword, "1, 3");
        add(tfPassword, "3, 3");
        
        TwoButtonPanel twoButton = new TwoButtonPanel( new String[]{ OK, CANCEL} );
        twoButton.init();
        bOk = twoButton.getBOne();
        bCancel = twoButton.getBTwo();
        add(twoButton, "0, 5, 5, 7");        
        
        lUsername.setText("Username");
        lPassword.setText("Password");
        bOk.setText( OK);
        bCancel.setText( CANCEL);
    }
    
    protected void doInitLayout()
    {
        double size[][] = // Columns
            {
                {
                    BORDER, TEXT_WIDTH, GAP, 0.74, ModernTableLayout.FILL,
                    BORDER},
                // Rows
                {
                    ModernTableLayout.FILL,
                        TEXT_HEIGHT, SMALL_SPACING, 
                        TEXT_HEIGHT, SMALL_SPACING,
                        TEXT_HEIGHT, SMALL_SPACING, 
                        TEXT_HEIGHT, SMALL_SPACING, 
                    ModernTableLayout.FILL,
                }};
        setLayout(new ModernTableLayout(size));
    }

    public Dimension getPreferredSize() 
    {
        Dimension d = super.getPreferredSize();
        d.width = 403;
        d.height = 129;
        return d;
    }

    private void initItems(JComboBox cbUsername, List<String> userItems)
    {
        cbUsername.removeAllItems();
        if (userItems != null)
        {
            for (int i = 0; i < userItems.size(); i++)
            {
                String item = userItems.get(i);
                cbUsername.addItem( item);
            }
        }
    }

    public void setUserSelectionsLOV( List<String> userItems)
    {
        initItems( cbUsername, userItems);
    }
    
    protected String translateForUserCombo( String selectedItemFromList)
    {
        String result = null;
        Err.error( "To be implemented by subclass (only called where useComboForUser)");
        return result;
    }
}
