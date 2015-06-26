package org.strandz.lgpl.util;

import javax.script.ScriptException;

/**
 * User: Chris
 * Date: 26/03/2009
 * Time: 12:20:25 PM
 */
public interface ScriptableI
{
    ScriptLanguageParams getLangParams();
    void setLangParams( ScriptLanguageParams langParams);
    void setScriptText( String txt);
    Object compileAndRun() throws ScriptException;
}
