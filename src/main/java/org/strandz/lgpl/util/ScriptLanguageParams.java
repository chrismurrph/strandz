package org.strandz.lgpl.util;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;

/**
 * User: Chris
 * Date: 15/03/2009
 * Time: 1:15:17 PM
 */
public class ScriptLanguageParams
{
    private ScriptEngine scriptEngine;
    private String langName;

    private static int timesConstructed;
    private int id;

    public ScriptLanguageParams( ScriptEngine scriptEngine, String langName)
    {
        Assert.notNull( scriptEngine);
        this.scriptEngine = scriptEngine;
        timesConstructed++;
        id = timesConstructed;
        if(id > 1)
        {
            Err.error();
        }
        this.langName = langName;
    }

    public ScriptContext getScriptContext()
    {
        return scriptEngine.getContext();
    }

    public ScriptEngine getScriptEngine()
    {
        return scriptEngine;
    }

    public void lazySetScriptableDependencies( ScriptableI scriptable)
    {
        if(scriptable.getLangParams() == null)
        {
            scriptable.setLangParams( this);
        }
    }

    public String getLangName()
    {
        return langName;
    }
}
