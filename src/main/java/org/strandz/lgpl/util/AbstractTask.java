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
package org.strandz.lgpl.util;

import foxtrot.Job;
import foxtrot.Worker;

import javax.swing.SwingUtilities;

abstract public class AbstractTask implements TaskI
{
    private boolean done = false;
    private boolean asynchronous = false;
    private boolean singleThread = false;
    private String title;
    private String completedPhrase;
    private int duration;
    protected ErrorMsgContainer err;

    public boolean isAsynchronous()
    {
        return asynchronous;
    }

    public void setAsynchronous(boolean asynchronous)
    {
        this.asynchronous = asynchronous;
        //Err.pr( "asynchronous set to " + asynchronous);
    }

    public boolean isSingleThread()
    {
        return singleThread;
    }

    public void setSingleThread(boolean singleThread)
    {
        this.singleThread = singleThread;
        //Err.pr( "singleThread set to " + singleThread);
    }

    abstract public Object newTask();

    public Object go( ErrorMsgContainer err)
    {
        Object result = null;
        this.err = err;
        if(asynchronous)
        {
            //Err.error("This works, but not intending to use");
            final SwingWorker worker = new SwingWorker()
            {
                public Object construct()
                {
                    done = false;
                    return newTask();
                }
            };
            worker.start();
        }
        else
        {
            if(!singleThread && SwingUtilities.isEventDispatchThread())
            {
                /**
                 * TODO Use a Task rather than a Job here. Also have a method something
                 * like pUtil.getNewPMExcept() which will throw an exception. Thus the
                 * result of Task.go() can be investigated and an error message brought
                 * to the attention of the user.
                 */
                try
                {
                    result = Worker.post(new Job()
                    {
                        public Object run()
                        {
                            done = false;
                            return newTask();
                        }
                    });
                }
                catch(ArrayIndexOutOfBoundsException ex)
                {
                    /*
                     * From moving the mouse at the wrong time:
                     *   [ConditionalEventPump] Exception occurred during event dispatching:
                     *   java.lang.ArrayIndexOutOfBoundsException: 1
                     *       at javax.swing.plaf.basic.BasicTabbedPaneUI.paintTabArea(BasicTabbedPaneUI.java:809)
                     */
                    Err.pr( "Caught and ignored ArrayIndexOutOfBoundsException that is prolly a paintTabArea swing bug");
                }
            }
            else
            {
                done = false;
                result = newTask();
            }
        }
        return result;
    }

    public boolean isDone()
    {
        return done;
    }

    public void setDone(boolean b)
    {
        done = b;
        if(b)
        {
            try
            {
                Thread.sleep( 100);
            }
            catch(InterruptedException e)
            {
                Err.error(e);
            }
        }
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getCompletedPhrase()
    {
        return completedPhrase;
    }

    public void setCompletedPhrase(String completedPhrase)
    {
        this.completedPhrase = completedPhrase;
    }

    public int getEstimatedDuration()
    {
        return duration;
    }

    public void setDuration(int duration)
    {
        this.duration = duration;
    }
}
