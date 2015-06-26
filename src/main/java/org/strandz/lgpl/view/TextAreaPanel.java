package org.strandz.lgpl.view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * User: Chris
 * Date: 08/05/2009
 * Time: 4:08:58 PM
 */
public class TextAreaPanel extends JPanel
{
    private JTextArea textArea;
    private static int constructedTimes;
    private int id;

    public TextAreaPanel()
    {
        constructedTimes++;
        id = constructedTimes;
    }

    public void init( String parentName)
    {
        textArea = new JTextArea();
        //textArea = new JButton();
        textArea.setName( "taTextArea in " + parentName);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        setBorder( obtainRaisedLoweredBeveled());
        setLayout( new BorderLayout());
        add( textArea);
        setTextArea( textArea);
        setName( "TextAreaPanel");
    }

    private Border obtainLoweredEtched()
    {
        Border loweredetched = BorderFactory.createEtchedBorder( EtchedBorder.LOWERED);
        return loweredetched;
    }

    private Border obtainRaisedLoweredBeveled()
    {
        Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();
        Border compound = BorderFactory.createCompoundBorder(
                                          raisedbevel, loweredbevel);
        return compound;
    }

    //taCriteria.setFont(new Font("Serif", Font.ITALIC, 16));
    //taCriteria.setLineWrap(true);
    //taCriteria.setWrapStyleWord(true);
    /*
    JScrollPane spAreaScrollPane = new JScrollPane( taCriteria);
    spAreaScrollPane.setName( "spAreaScrollPane");
    spAreaScrollPane.setVerticalScrollBarPolicy(
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    */
    /*
    taCriteria.setBorder(
        BorderFactory.createEmptyBorder(5, 5, 5, 5)
    );
    */

    public JTextArea getTextArea()
    {
        return textArea;
    }

    public void setTextArea(JTextArea textArea)
    {
        this.textArea = textArea;
    }
}
