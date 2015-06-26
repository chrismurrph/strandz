package org.strandz.lgpl.store;

import org.strandz.lgpl.util.NoTaskTimeBandMonitorI;

/**
 * User: Chris
 * Date: 4/10/2008
 * Time: 21:55:24
 */
abstract public class WordyInterpretedQuery extends InterpretedQuery
{
    String wordyFilter;
    String paramDeclaration;

    public WordyInterpretedQuery(Class queryOn, String id, NoTaskTimeBandMonitorI monitor, 
                                 boolean wrapInList)
    {
        super(queryOn, id, monitor, wrapInList);
    }

    public WordyInterpretedQuery(Class queryOn, String id, String wordyFilter, String paramDeclaration,
                                 NoTaskTimeBandMonitorI monitor)
    {
        super(queryOn, id, monitor);
        this.wordyFilter = wordyFilter;
        this.paramDeclaration = paramDeclaration;
    }

    public WordyInterpretedQuery(Class queryOn, String id, String wordyFilter, String paramDeclaration,
                                 NoTaskTimeBandMonitorI monitor, int estimatedDuration)
    {
        super(queryOn, id, monitor, estimatedDuration);
        this.wordyFilter = wordyFilter;
        this.paramDeclaration = paramDeclaration;
    }

    public WordyInterpretedQuery(Class queryOn, String id, String wordyFilter, String paramDeclaration,
                                 NoTaskTimeBandMonitorI monitor, boolean wrapInList,
                                 boolean compileWhenConnect, int estimatedDuration)
    {
        super(queryOn, id, monitor, wrapInList, compileWhenConnect, estimatedDuration);
        this.wordyFilter = wordyFilter;
        this.paramDeclaration = paramDeclaration;
    }

    public WordyInterpretedQuery(Class queryOn, String id, String wordyFilter, String paramDeclaration,
                                 NoTaskTimeBandMonitorI monitor, boolean wrapInList,
                                 boolean compileWhenConnect)
    {
        super(queryOn, id, monitor, wrapInList, compileWhenConnect);
        this.wordyFilter = wordyFilter;
        this.paramDeclaration = paramDeclaration;
    }
}
