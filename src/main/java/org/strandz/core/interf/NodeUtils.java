package org.strandz.core.interf;

import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * User: Chris
 * Date: 19/01/2009
 * Time: 6:20:49 PM
 */
public class NodeUtils
{
    public static final String DEFAULT_NAME = "New Node";
    //public static final int NO_ROW_SELECTED = -1;

    public static void alterWritability( Node node, boolean b)
    {
        node.setUpdate( b);
        List<RuntimeAttribute> attributes = node.getDisplayedAttributes();
        for (int i = 0; i < attributes.size(); i++)
        {
            RuntimeAttribute attribute = attributes.get(i);
            Err.pr( "In TableAttribute ID: " + attribute.getId() + " setting enabled to " + b);
            if(attribute.getId() == 12)
            {
                Err.debug();
            }
            attribute.setEnabled( b);
            Assert.isTrue( b == attribute.isEnabled());
        }
    }

    public static Map<String,Object> getB4ImageValues( Node node)
    {
        Map result = new HashMap<String,Object>();
        List<RuntimeAttribute> attributes = node.getDisplayedAttributes();
        for (int i = 0; i < attributes.size(); i++)
        {
            RuntimeAttribute attribute = attributes.get(i);
            Assert.notNull( attribute.getName());
            Assert.isFalse( result.containsKey( attribute.getName()));
            //Assert.notNull( attribute.getB4ImageValue());
            result.put( attribute.getName(), attribute.getB4ImageValue());
        }
        return result;
    }

    public static void setB4ImageValues( Node node, Map<String,Object> values)
    {
        List<RuntimeAttribute> attributes = node.getDisplayedAttributes();
        for (int i = 0; i < attributes.size(); i++)
        {
            RuntimeAttribute attribute = attributes.get(i);
            Object value = values.get( attribute.getName());
            attribute.setB4ImageValue( value);
        }
    }

    public static void setB4ImageValue( Node node, Object obj)
    {
        List<RuntimeAttribute> displayedAttributes = node.getDisplayedAttributes();
        for (int i = 0; i < displayedAttributes.size(); i++)
        {
            RuntimeAttribute runtimeAttribute = displayedAttributes.get(i);
            /*
             * This call will do not just the lookup conversion but also
             * more obviously the conversion to the attribute level.
             */
            Object fieldValue = runtimeAttribute.getFieldValue( obj);
            runtimeAttribute.setB4ImageValue( fieldValue);
            Err.pr( "Manually set b4Image on attribute ID: " + runtimeAttribute.getId());
        }
    }
}
