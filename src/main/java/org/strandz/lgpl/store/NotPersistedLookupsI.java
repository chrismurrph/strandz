package org.strandz.lgpl.store;

import org.strandz.lgpl.store.DomainLookupEnum;

import java.util.List;

/**
 * User: Chris
 * Date: 29/08/2008
 * Time: 08:20:43
 */
public interface NotPersistedLookupsI
{
    List get( DomainLookupEnum enumId);
    void initValues();
}
