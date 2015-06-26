package org.strandz.data.gallery.objects;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.query.QueryChain;
import org.apache.cayenne.query.NamedQuery;
import org.apache.cayenne.query.SelectQuery;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.DataObjectUtils;
import org.strandz.lgpl.util.Print;

import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.List;

/**
 * User: Chris
 * Date: 27/07/2008
 * Time: 11:46:30
 */
public class Main
{
    private DataContext context;

    public Main()
    {
        context = DataContext.createDataContext();

        deleteAll();
        addSome();

        context.commitChanges();

        //queryOldArtists();

        //deletePicasso();
    }

    private void deletePicasso()
    {
        Expression qualifier = ExpressionFactory.matchExp(Artist.ARTIST_NAME_PROPERTY, "Pablo Picasso");
        SelectQuery select = new SelectQuery(Artist.class, qualifier);

        Artist picasso = (Artist) DataObjectUtils.objectForQuery(context, select);

        if (picasso != null) {
            context.deleteObject(picasso);
            context.commitChanges();
        }

    }

    private void queryOldArtists()
    {
        Calendar c = new GregorianCalendar();
        c.set(c.get(Calendar.YEAR) - 100, 0, 1, 0, 0, 0);

        Expression qualifier3 = Expression.fromString("artist.dateOfBirth < $date");
        qualifier3 = qualifier3.expWithParameters(Collections.singletonMap("date", c.getTime()));
        SelectQuery select3 = new SelectQuery(Painting.class, qualifier3);
        List paintings3 = context.performQuery(select3);
        Print.prList( paintings3, "The paintings done by old artists");
    }

    private void deleteAll()
    {
        QueryChain chain = new QueryChain();
        chain.addQuery(new NamedQuery("DeleteAll", Collections.singletonMap(
                        "table",
                        "PAINTING")));
        chain.addQuery(new NamedQuery("DeleteAll", Collections.singletonMap(
                        "table",
                        "ARTIST")));
        chain.addQuery(new NamedQuery("DeleteAll", Collections.singletonMap(
                        "table",
                        "GALLERY")));
        context.performGenericQuery(chain);
    }

    private void addSome()
    {
        Artist picasso = (Artist) context.newObject(Artist.class);
        picasso.setArtistName("Pablo Picasso");
        picasso.setDateOfBirthString("18811025");

        Gallery metropolitan = (Gallery) context.newObject(Gallery.class);
        metropolitan.setGalleryName("Metropolitan Museum of Art");

        Painting girl = (Painting) context.newObject(Painting.class);
        girl.setPaintingTitle("Girl Reading at a Table");

        Painting stein = (Painting) context.newObject(Painting.class);
        stein.setPaintingTitle("Gertrude Stein");

        picasso.addToPaintings(girl);
        picasso.addToPaintings(stein);

        //girl.setGallery(metropolitan);
        //stein.setGallery(metropolitan);
    }

    public static void main(String[] args)
    {
        new Main();
    }
}
