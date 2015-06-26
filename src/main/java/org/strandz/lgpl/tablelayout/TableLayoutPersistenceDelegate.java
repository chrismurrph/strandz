/*
 * ====================================================================
 *
 * The Clearthought Software License, Version 1.0
 *
 * Copyright (c) 2002 Daniel Barbalace.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. The original software may not be altered.  However, the classes
 *    provided may be subclasses as long as the subclasses are not
 *    packaged in the info.clearthought package or any subpackage of
 *    info.clearthought.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR, AFFILATED BUSINESSES,
 * OR ANYONE ELSE BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 */

/*
 * ====================================================================
 * This is an extension of the info.clearthought.layout package.
 * This extension only works under J2SE 1.4 or later.
 * ====================================================================
 */

package org.strandz.lgpl.tablelayout;


import info.clearthought.layout.TableLayoutConstraints;

import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Statement;
import java.util.LinkedList;
import java.util.ListIterator;

public class TableLayoutPersistenceDelegate extends DefaultPersistenceDelegate
{
    protected void initialize
        (Class type, Object oldInstance, Object newInstance, Encoder out)
    {
        super.initialize(type, oldInstance, newInstance, out);

        int C = 0;
        int R = 1;

        try
        {
            LinkedList list = ((org.strandz.lgpl.tablelayout.ModernTableLayout) oldInstance).getList();

            if (list != null)
            {
                ListIterator iterator = list.listIterator();

                while (iterator.hasNext())
                {
                    ModernTableLayout.Entry entry = (ModernTableLayout.Entry) iterator.next();
                    TableLayoutConstraints constraint = new TableLayoutConstraints
                        (entry.cr1[C], entry.cr1[R], entry.cr2[C], entry.cr2[R],
                            entry.alignment[C], entry.alignment[R]);

                    Statement statement = new Statement
                        (oldInstance, "addLayoutComponent",
                            new Object[]{entry.component, constraint});

                    out.writeStatement(statement);
                }
            }
        }
        catch (Exception e)
        {
            out.getExceptionListener().exceptionThrown(e);
        }
    }

}
