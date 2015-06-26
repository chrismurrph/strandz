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

import org.strandz.data.gallery.objects.Artist;
import org.strandz.data.gallery.objects.Gallery;
import org.strandz.data.gallery.objects.Painting;

public class GalleryData
{
    public static final Class ARTIST = Artist.class;
    public static final Class GALLERY = Gallery.class;
    public static final Class PAINTING = Painting.class;

    /**
     * Order of these important if have RI constraints on. To see if they are on:
     * SHOW CREATE TABLE Worker;
     * In MySql.
     * Theoretically flush s/be smart enough to delete objects in the right order to
     * navigate around FK constraints, even for when Worker refers to itself. Our
     * slightly tacky solution is to not have integrity constraints for Worker->Worker
     */
    public static final Class[] CLASSES = {
        ARTIST, GALLERY, PAINTING,
    };

    public static final Class[] NON_LOOKUP_CLASSES = {
        ARTIST, GALLERY, PAINTING,
    };

    public static final Class[] LOOKUP_CLASSES = {
    };

    public static Object[] getLookupValues( Class clazz)
    {
        Object[] result = null;
        return result;
    }

    public static boolean isLookupDomainObject( Class clazz)
    {
        boolean result = true;
        if(clazz == ARTIST || clazz == GALLERY || clazz == PAINTING)
        {
            result = false;
        }
        return result;
    }
}