package org.strandz.lgpl.widgets;

import org.strandz.lgpl.util.Err;

import java.util.List;
import java.util.ArrayList;

/**
 * User: Chris
 * Date: 05/05/2009
 * Time: 10:35:57 PM
 */
public class TestExpanderControl extends ExpanderControl
{
    public void init()
    {
        List<String> titles = new ArrayList<String>();
        titles.add( "Wibble");
        titles.add( "Wobble");
        setName( "TestExpanderControl");
        init( titles, 1, "How do you move?", null);
    }
}
