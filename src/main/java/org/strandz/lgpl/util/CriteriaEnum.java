package org.strandz.lgpl.util;

import org.strandz.lgpl.util.NullVerifiable;
import org.strandz.lgpl.util.Utils;

import javax.script.ScriptContext;
import javax.script.ScriptException;

/**
 * User: Chris
 * Date: 30/01/2009
 * Time: 2:08:14 AM
 */
abstract public class CriteriaEnum implements Comparable, NullVerifiable, IdentifierI
{
    private String name;
    private boolean dummy;
    String baseName;

    private static int timesConstructed;
    private int id;

    /*
    public CriteriaEnum()
    {
        timesConstructed++;
        id = timesConstructed;
    }
    */

    private CriteriaEnum(String name, int id, boolean dummy)
    {
        this.name = name;
        this.id = id;
        this.dummy = dummy;
    }

    public String getName()
    {
        return name;
    }

    public int getId()
    {
        return id;
    }

    public String toString()
    {
        return name;
    }

    public boolean isDummy()
    {
        return dummy;
    }

    public int compareTo(Object obj)
    {
        return Utils.relativeRank(OPEN_VALUES, this, obj);
    }

    public boolean equals(Object o)
    {
        Utils.chkType(o, CriteriaEnum.class);

        boolean result = false;
        if(o == this)
        {
            result = true;
        }
        else if(!(o instanceof CriteriaEnum))
        {// nufin
        }
        else
        {
            CriteriaEnum test = (CriteriaEnum) o;
            if((name == null ? test.getName() == null : name.equals(test.getName())))
            {
                result = true;
            }
        }
        return result;
    }

    public int hashCode()
    {
        int result = 17;
        result = 37 * result + (name == null ? 0 : name.hashCode());
        return result;
    }

    public static CriteriaEnum fromOrdinal( int ordinal)
    {
        return OPEN_VALUES[ordinal];
    }

    public String getBaseName()
    {
        return baseName;
    }

    public void setBaseName(String baseName)
    {
        this.baseName = baseName;
    }

    abstract public boolean meetsCriteria( String name);

    /**
     * So in DB 0 is unknown and the others are as here
     */
    public static final CriteriaEnum NULL = new CriteriaEnum( "N/A", 0, true)
    {
        public boolean meetsCriteria(String name)
        {
            Err.error("Not implemented");
            return false;
        }
    };
    public static final CriteriaEnum CONTAINS = new CriteriaEnum( "Contains", 1, false)
    {
        public boolean meetsCriteria(String name)
        {
            return name.contains( baseName);
        }
    };
    public static final CriteriaEnum STARTS_WITH = new CriteriaEnum( "Starts with", 2, false)
    {
        public boolean meetsCriteria(String name)
        {
            return name.startsWith( baseName);
        }
    };
    public static final CriteriaEnum CODE = new CodeCriteriaEnum();

    public static final CriteriaEnum[] OPEN_VALUES = {
        NULL, CONTAINS, STARTS_WITH, CODE};

    /**
     * Despite appearances there is only ever one of these. So the setters
     * are always called before meetsCriteria is called.
     */
    public static class CodeCriteriaEnum extends CriteriaEnum implements ScriptableI
    {
        private ScriptLanguageParams langParams;

        public CodeCriteriaEnum()
        {
            super("Code", 3, false);
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
            setBaseName( txt);
        }

        public Object compileAndRun() throws ScriptException
        {
            return compileAndRun( "");
        }

        public Boolean compileAndRun( String nameVarValue) throws ScriptException
        {
            Boolean resultObj;
            Assert.notNull( langParams, "setLangParams() has not been called on " + this);
            Assert.notNull( getBaseName(), "setScript() has not been called on " + this);
            langParams.getScriptContext().setAttribute( "name", nameVarValue, ScriptContext.ENGINE_SCOPE);
            Object obj = langParams.getScriptEngine().eval(
                getBaseName(), langParams.getScriptContext());
            if(obj instanceof Boolean)
            {
                resultObj = (Boolean)obj;
            }
            else if(obj == null)
            {
                if(!Utils.isBlank( getBaseName()))
                {
                    Err.pr( "----start script----");
                    Err.pr( getBaseName());
                    Err.pr( "-----end script-----");
                    String txt = "Code needs to return a Boolean, instead returns null";
                    throw new ScriptException( txt);
                }
                else
                {
                    /*
                     * When compiling for validation purposes
                     */
                    resultObj = null;
                }
            }
            else
            {
                String txt = "Code needs to return a Boolean, instead got " + obj.getClass().getName();
                throw new ScriptException( txt);
            }
            return resultObj;
        }

        public boolean meetsCriteria( String name)
        {
            Boolean resultObj = null;
            Assert.notNull( getBaseName());
            try
            {
                resultObj = compileAndRun( name);
            }
            catch( ScriptException ex)
            {
                Err.error( "Code does not compile, and should have been checked earlier: " +
                    ex.getCause().getMessage());
            }
            if(resultObj != null)
            {
                //langParams.nullify();
                setBaseName( null);
            }
            else
            {
                Err.error();
            }
            return resultObj;
        }
    }
}