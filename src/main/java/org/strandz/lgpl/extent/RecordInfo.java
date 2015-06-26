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
package org.strandz.lgpl.extent;

/**
 * A replication of what the OODB will already know.
 * Used currently so that user will have capability of deleting/updating
 * a record that has been inserted, even if user has not been allowed
 * to delete.
 */
class RecordInfo
{
    static final int QUERIED_UPDATE = 1;
    static final int QUERIED_VIEW = 2;
    static final int INSERTED = 3;
    private int status;

    RecordInfo(int status)
    {
        this.status = status;
    }
}
