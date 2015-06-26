package org.strandz.lgpl.data.objects;

import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.ScriptableI;
import org.strandz.lgpl.util.ScriptLanguageParams;
import org.strandz.lgpl.util.TypeableI;

import javax.script.ScriptException;
import javax.script.ScriptContext;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;

/**
 * User: Chris
 * Date: 15/02/2009
 * Time: 12:46:09 AM
 */
public class InformMissingPropertyComposite implements PropertyI, ScriptableI, PropertyValueChangedListener
{
    private List<PropertyI> sourceProperties;
    private List<PropertyI> destinationProperties;
    private final TypeableI propertyIdentity;
    private transient Object value;
    private Object zeroValue;
    private ScriptLanguageParams langParams;
    private Date date;
    /**
     * When set to Utils.UNSET_INT (-99) then this property is a template
     * and has no value. Used for debugging.
     */
    private int hostId;
    private String script;
    private static int constructedTimes = 0;
    private int id;

    public static InformMissingPropertyComposite newInstance(
        List<PropertyI> sourceProperties, List<PropertyI> destinationProperties,
        TypeableI propertyIdentity, int hostId, String script,
        ScriptLanguageParams langParams, Date date)
    {
        InformMissingPropertyComposite result = new InformMissingPropertyComposite(
            sourceProperties, destinationProperties, propertyIdentity,
            hostId, script, langParams, date);
        return result;
    }

    private InformMissingPropertyComposite(
        List<PropertyI> sourceProperties, List<PropertyI> destinationProperties,
        TypeableI propertyIdentity, int hostId, String script,
        ScriptLanguageParams langParams, Date date)
    {
        constructedTimes++;
        id = constructedTimes;

        this.sourceProperties = sourceProperties;
        this.destinationProperties = destinationProperties;
        this.propertyIdentity = propertyIdentity;
        this.hostId = hostId;
        this.script = script;
        this.langParams = langParams;
        this.date = date;
        listenTo( sourceProperties);
        listenTo( destinationProperties);
    }

    private void listenTo( List<PropertyI> properties)
    {
        for (Iterator<PropertyI> propertyIIterator = properties.iterator(); propertyIIterator.hasNext();)
        {
            PropertyI propertyI = propertyIIterator.next();
            propertyI.addPropertyValueChangedListener( this);
        }
    }

    public void propertyValueChanged()
    {
        value = null;
    }

    public String toString()
    {
        return propertyIdentity.getName() + ", getValue(): " + getValue() + ", ID: " + id;
    }

    public PropertyIdentityDOI getPropertyIdentity()
    {
        return (PropertyIdentityDOI)propertyIdentity;
    }

    public PropertyI copyConstruct( int hostId)
    {
        PropertyI result = new InformMissingPropertyComposite(
            sourceProperties, destinationProperties, propertyIdentity,
            hostId, script, langParams, date);
        Object value = getValue();
        if(value != null)
        {
            result.setValue( value);
        }
        else
        {
            //template property so no value?
        }
        return result;
    }

    public String getName()
    {
        return propertyIdentity.getName();
    }

    public Object getValue()
    {
        Object result = null;
        if(value == null)
        {
            try
            {
                result = compileAndRun();
            }
            catch( ScriptException ex)
            {
                Err.error( "Code does not compile: " + ex.getCause().getMessage());
            }
            value = result;
        }
        else
        {
            result = value;
        }
        return result;
    }

    /*
    public List<PropertyI> getProperties()
    {
        return properties;
    }
    */

    public void setValue(Object value)
    {
        Err.error( "Not implemented");
    }

    public void addPropertyValueChangedListener( PropertyValueChangedListener l)
    {
        Err.error( "Not implemented");
    }

    public Object zeroValue()
    {
        if(zeroValue == null)
        {
            zeroValue = newZeroValue();
        }
        return zeroValue;
    }

    public Object newZeroValue()
    {
        Object result = null;
        try
        {
            result = getType().newInstance();
        }
        catch (InstantiationException e)
        {
            Err.error( e);
        }
        catch (IllegalAccessException e)
        {
            Err.error( e);
        }
        return result;
    }

    public Class getType()
    {
        return propertyIdentity.getType();
    }

    public void setLangParams( ScriptLanguageParams langParams)
    {
        Assert.notNull( langParams);
        this.langParams = langParams;
    }

    public ScriptLanguageParams getLangParams()
    {
        return langParams;
    }

    public void setScriptText( String txt)
    {
        //Err.pr( "Not much point in calling as is constructed with ScriptText");
        this.script = txt;
    }

    public Object compileAndRun() throws ScriptException
    {
        Object result;
        Assert.notNull( langParams, "setLangParams() has not been called on a " + this.getClass().getName());
        langParams.getScriptContext().setAttribute(
            "sourceProperties", sourceProperties, ScriptContext.ENGINE_SCOPE);
        langParams.getScriptContext().setAttribute(
            "destinationProperties", destinationProperties, ScriptContext.ENGINE_SCOPE);
        langParams.getScriptContext().setAttribute(
            "name", getName(), ScriptContext.ENGINE_SCOPE);
        langParams.getScriptContext().setAttribute(
            "date", date, ScriptContext.ENGINE_SCOPE);
        langParams.getScriptContext().setAttribute(
            "result", new MoneyAmount(), ScriptContext.ENGINE_SCOPE);
        result = langParams.getScriptEngine().eval(
            script, langParams.getScriptContext());
        //Err.pr( "script result: " + result);
        return result;
    }

    static class DummyIdentity implements TypeableI
    {
        private String name = "Dummy identity";
        private static Class type = MoneyAmount.class;

        public String getName()
        {
            return name;
        }

        public String toShow()
        {
            return name;
        }

        public Class getType()
        {
            return type;
        }
    }

    static class DummyPropertyIdentity implements PropertyIdentityDOI
    {
        private String name = "Dummy property identity";
        private static Class type = MoneyAmount.class;

        public String getName()
        {
            return name;
        }

        public String toShow()
        {
            return name;
        }

        public String getDisplayName()
        {
            return name;
        }

        public Class getType()
        {
            return type;
        }

        public int getId()
        {
            return Utils.UNSET_INT;
        }

        public void setToBeAveraged( boolean b)
        {
            Err.error("Not implemented");
        }

        public boolean isToBeAveraged()
        {
            Err.error("Not implemented");
            return false;
        }
    }

    private static StringBuffer nonsenseScript()
    {
        StringBuffer result = new StringBuffer();
        result.append( "puts $sourceProperties\nputs $destinationProperties\n");
        result.append( "return $result");
        return result;
    }

    public static StringBuffer sensibleScript()
    {
        StringBuffer result = new StringBuffer();
        /**/
        result.append( "");
        result.append( "if $destinationProperties.length == 0 or $destinationProperties[0].value.is_zero\n");
        result.append( "then\n");
        result.append( "$sourceProperties.each do |property|\n");
        //result.append( "    puts \"Going to add #{property.value} to #{$result} on #{$date}\"\n");
        result.append( "  $result = $result.add( property.value)\n");
        result.append( "end else\n");
        result.append( "  $result = $destinationProperties[0].value\n");
        result.append( "end\n");
        /**/
        /*
        result.append( "$result.setDollars( 10)\n");
        result.append( "puts $destinationProperties[0].value\n");
        result.append( "puts $destinationProperties[0].value.class\n");
        result.append( "$result = $result.add( $result)\n");
        */
        result.append( "return $result\n");
        result.append( "");
        return result;
    }

    public static InformMissingPropertyComposite exampleNewInstance1()
    {
        return exampleNewInstance( "0.00");
    }
    public static InformMissingPropertyComposite exampleNewInstance2()
    {
        return exampleNewInstance( "1.00");
    }

    private static InformMissingPropertyComposite exampleNewInstance( String destAmount)
    {
        InformMissingPropertyComposite result;
        PropertyIdentityDOI propertyIdentity = new DummyPropertyIdentity();
        PropertyI property1 = Property.newInstance( propertyIdentity);
        property1.setValue( new MoneyAmount( 1));
        PropertyI property2 = Property.newInstance( propertyIdentity);
        property2.setValue( new MoneyAmount( 2));
        PropertyI property3 = Property.newInstance( propertyIdentity);
        property3.setValue( new MoneyAmount( 3));
        PropertyI property4 = Property.newInstance( propertyIdentity);
        property4.setValue( MoneyAmount.newInstance( destAmount));
        List<PropertyI> sourceProperties = new ArrayList<PropertyI>();
        sourceProperties.add( property1);
        sourceProperties.add( property2);
        sourceProperties.add( property3);
        List<PropertyI> destProperties = Utils.formList( property4);
        DummyIdentity dummyIdentity = new DummyIdentity();
        result =
            InformMissingPropertyComposite.newInstance(
                sourceProperties, destProperties, dummyIdentity, Utils.UNSET_INT,
                null, null, new Date());
        return result;
    }

    public static void exampleCompile( String script) throws ScriptException
    {
        ScriptEngineManager m = new ScriptEngineManager();
        ScriptEngine rubyEngine = m.getEngineByName( Utils.RUBY_ENGINE_NAME);
        //
        InformMissingPropertyComposite impc = exampleNewInstance1();
        impc.setLangParams( new ScriptLanguageParams( rubyEngine, "Ruby"));
        impc.setScriptText( script);
        impc.compileAndRun();
    }

    public static void main(String[] args)
    {
        try
        {
            exampleCompile( sensibleScript().toString());
        }
        catch (ScriptException e)
        {
            Err.error();
        }
    }

}