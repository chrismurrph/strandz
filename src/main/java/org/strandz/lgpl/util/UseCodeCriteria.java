package org.strandz.lgpl.util;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Only exists for its main unit test
 *
 * User: Chris
 * Date: 22/02/2009
 * Time: 12:03:17 AM
 */
public class UseCodeCriteria extends AbstractCriteria
{
    public UseCodeCriteria( String rubyScriptCode)
    {
        super( rubyScriptCode);
        setCriteriaEnum( CriteriaEnum.CODE);
    }

    public static void main( String[] args)
    {
        boolean result;
        ScriptEngineManager m = new ScriptEngineManager();
        ScriptEngine rubyEngine = m.getEngineByName( Utils.RUBY_ENGINE_NAME);
        //ScriptContext context = rubyEngine.getContext();
        String nameToExamine = "Some kind of Bucket we have";
        String codeToUse = "($name.include? \"Bucket\") && ($name.include? \"kind\")";

        UseCodeCriteria useCodeCriteria = new UseCodeCriteria( codeToUse);
        CriteriaEnum.CodeCriteriaEnum codeCriteriaEnum = (CriteriaEnum.CodeCriteriaEnum)useCodeCriteria.getCriteriaEnum();
        codeCriteriaEnum.setLangParams( new ScriptLanguageParams( rubyEngine, "Ruby"));
        result = useCodeCriteria.meetsCriteria( nameToExamine);
        Err.pr( "Meets criteria: " + result);
        Assert.isTrue( result);
        /*
        context.setAttribute( "name", "Some kind of Bucket we have", ScriptContext.ENGINE_SCOPE);

        //String code = "puts $name == \"Bucket\" || $name == \"Spade\"";
        //String code = "puts String.instance_methods";
        String code = "($name.include? \"Bucket\") && ($name.include? \"kind\")";

        try{
            result = rubyEngine.eval( code, context);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        */
    }
}