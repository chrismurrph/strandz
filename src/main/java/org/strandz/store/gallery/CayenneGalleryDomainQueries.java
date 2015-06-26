/*
    Strandz - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    Strandz is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Strandz; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    In addition, as a special exception, Chris Murphy gives
    permission to link the code of this program with any Java software for
    which complete source code is available at no cost (even if distribution
    or modification of that source code is restricted), and distribute the
    results of that linking. You must obey the GNU General Public
    License in all respects for all of the code used other than this Java code.
    If you modify this file, you may extend this exception to your version of
    the file, but you are not obligated to do so. If you do not wish to do so,
    delete this exception statement from your version.

    The authors can be contacted via www.strandz.org
*/
package org.strandz.store.gallery;

import org.strandz.data.gallery.domain.GalleryDomainQueryEnum;
import org.strandz.lgpl.store.CayenneInterpretedQuery;
import org.strandz.lgpl.store.DomainQueries;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.Utils;

import java.util.Collection;
import java.util.List;

/**
 *
 */
public class CayenneGalleryDomainQueries extends DomainQueries
{

    /**
     * Comment out queries that are not needed by application. They can
     * be done by calling a seperate method as get errors.
     */
    public CayenneGalleryDomainQueries()
    {
        initQuery(GalleryDomainQueryEnum.PAINTINGS,
            new CayenneInterpretedQuery(
                GalleryData.PAINTING,
                GalleryDomainQueryEnum.PAINTINGS.getDescription(),
                null,
                null,
                null,
                loggingMonitor, 1000)
            {
            });
        initQuery(GalleryDomainQueryEnum.GALLERIES,
            new CayenneInterpretedQuery(
                GalleryData.GALLERY,
                GalleryDomainQueryEnum.GALLERIES.getDescription(),
                null,
                null,
                null,
                loggingMonitor, 1000)
            {
            });
        initQuery(GalleryDomainQueryEnum.ARTISTS,
            new CayenneInterpretedQuery(
                GalleryData.ARTIST,
                GalleryDomainQueryEnum.ARTISTS.getDescription(),
                null,
                null,
                null,
                loggingMonitor, 1000)
            {
            });
        initQuery(GalleryDomainQueryEnum.ARTIST_BY_NAME,
            new CayenneInterpretedQuery(
                GalleryData.ARTIST,
                GalleryDomainQueryEnum.ARTIST_BY_NAME.getDescription(),
                "artistName = $artistNameParam", //filter
                null, //ordering
                new String[]{"artistNameParam"}, //param declaration
                loggingMonitor, 1000)
            {
                public void formSingleResult(Collection c)
                {
                    if(!c.isEmpty())
                    {
                        List l = (List) c;
                        setSingleResult(l.get(0));
                    }
                    else
                    {
                        Err.pr( "No matching artist");
                    }
                }
                public void chkResult(Collection c)
                {
                    Utils.chkOneOnly(c, getId());
                }
            });
    }
}