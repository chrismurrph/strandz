package org.strandz.lgpl.widgets;

import org.strandz.lgpl.tablelayout.ModernTableLayout;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.Assert;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

/**
 * User: Chris
 * Date: 18/04/2009
 * Time: 3:52:16 PM
 */
public class ManyItemsControl extends JPanel
{
    private JTextField[] textFields;

    static final int BORDER = 15;

    /**
     * Only used for debugging
     */
    public void init()
    {
        init( null, 4);
    }

    public void init( Integer eachWidth, Integer itemCount)
    {
        if(eachWidth == null)
        {
            eachWidth = 50;
        }

        ModernTableLayout modernTableLayout = new ModernTableLayout();
        int count = 0;
        for (int i = 0; i < itemCount; i++)
        {
            modernTableLayout.insertColumn( count, eachWidth);
            count++;
            modernTableLayout.insertColumn( count, 7);
            count++;
        }
        modernTableLayout.insertRow( 0, ModernTableLayout.FILL);
        setLayout( modernTableLayout);
        JTextField[] textFields = new JTextField[itemCount];
        count = 0;
        for (int i = 0; i < itemCount; i++)
        {
            JTextField item = new JTextField();
            item.setName( "tfManyItems item number " + i);
            item.setHorizontalAlignment( SwingConstants.LEADING);
            add( item, count + ", 0");
            count += 2;
            textFields[i] = item;
        }
        setName( "ManyItemsControl");
        setTextFields( textFields);
    }

    public void setTextFields(JTextField[] textFields)
    {
        this.textFields = textFields;
    }

    public JTextField[] getTextFields()
    {
        return textFields;
    }

    public JTextField getTextField(int index)
    {
        return textFields[index];
    }

    public void setTextField(int index, JTextField textField)
    {
        textFields[index] = textField;
    }

    public List<BigDecimal> getBigDecimalValues()
    {
        List<BigDecimal> result = new ArrayList<BigDecimal>();
        if(getTextFields() != null)
        {
            for (int i = 0; i < getTextFields().length; i++)
            {
                JTextField jTextField = getTextField( i);
                String value = jTextField.getText();
                if(Utils.isBlank( value))
                {
                    //result.add( null);
                    result = null;
                    break;
                }
                else
                {
                    result.add( new BigDecimal( value));
                }
            }
        }
        return result;
    }

    public void setBigDecimalValues( List<BigDecimal> value)
    {
        if(value != null)
        {
            for (int i = 0; i < value.size(); i++)
            {
                BigDecimal bigDecimal = value.get(i);
                String val = null;
                if(bigDecimal != null)
                {
                    val = "" + bigDecimal;
                }
                JTextField tf = getTextField( i);
                Assert.notNull( tf, "No textField at " + i);
                tf.setText( val);
            }
        }
        else
        {
            for (int i = 0; i < textFields.length; i++)
            {
                JTextField tf = textFields[i];
                tf.setText( null);
            }
        }
    }
}