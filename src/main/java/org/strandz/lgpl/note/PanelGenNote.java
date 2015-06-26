/*
    Strandz LGPL - an API that matches the user to the data.
    Copyright (C) 2007 Chris Murphy

    Strandz LGPL is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA


    The authors can be contacted via www.strandz.org
*/
package org.strandz.lgpl.note;

public class PanelGenNote extends Note
{
    public static final Note DONT_TRY_UPDATE_ATTRIBUTES_WHEN_GEN = new PanelGenNote( "When gen new panel, get StemAttributes");

    static
    {
        String desc = null;
        //
        desc = "If names were equal, existing attributes could be updated. This "
            + "would mean existing panel could be kept, would would pose many more problems, "
            + "for example deletes would need to be procdessed";
        DONT_TRY_UPDATE_ATTRIBUTES_WHEN_GEN.setDescription(desc);
        //
    }
    
    public PanelGenNote()
    {
        super();
    }

    public PanelGenNote(String name)
    {
        super( name);
    }
    
    public PanelGenNote(String name, boolean visible)
    {
        super(name, visible);
    }
}
