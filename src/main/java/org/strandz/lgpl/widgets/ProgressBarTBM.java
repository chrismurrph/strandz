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
package org.strandz.lgpl.widgets;

import foxtrot.Job;
import foxtrot.Worker;
import org.strandz.lgpl.note.SdzNote;
import org.strandz.lgpl.util.Assert;
import org.strandz.lgpl.util.Err;
import org.strandz.lgpl.util.ErrorMsgContainer;
import org.strandz.lgpl.util.MessageDlg;
import org.strandz.lgpl.util.TaskI;
import org.strandz.lgpl.util.TaskTimeBandMonitorI;
import org.strandz.lgpl.util.Utils;
import org.strandz.lgpl.util.StopWatch;
import org.strandz.lgpl.util.TaskUtils;
import org.strandz.lgpl.util.TimeBandI;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

public class ProgressBarTBM extends JDialog implements TaskTimeBandMonitorI
{
    JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL);
    Timer timer;
    TaskI task;
    int increment;
    int current;
    int waitingCycles;
    private StopWatch stopWatch = new StopWatch();
    private ProgressBarTBM outer;

    //Wouldn't be nice to the user to put to any more than 2
    static int MAX_WAITING_CYCLES = 1;
    //Divide this into the length of the task to find out how often an update will occur
    private static int NUM_SCREEN_REFRESHES = 20;
    private static int HEIGHT = 35;
    private static int WIDTH = 200;
    
    public ProgressBarTBM()
    {
        //works when set to modal, but the timer thread goes on forever
        super(MessageDlg.getFrame(), null, false);
        outer = this;
    }
    
    public void start(TaskI task, ErrorMsgContainer err)
    {
        stopWatch.startTiming();
        beforeTaskStarts();
        initTask(task);
        timer.start();
        setVisible(true);
        task.go( err);
    }
    
    /* Silly to make up a task, we need duration etc ...
    public void start(String text, ErrorMsgContainer err)
    {
        beforeTaskStarts();
        LogTask task = new LogTask(text, err)
        {
            public Object go( ErrorMsgContainer err)
            {
                //nufin
                return null;
            }
        };
        initTask(task);
        timer.start();
        setVisible(true);
        task.go( err);
    }
    */

    public void stop()
    {
        afterTaskSynchronous();
        //setVisible( false);
        stopWatch.stopTiming();
        String msg = TaskUtils.getPrintTimingInfo( this);
        Err.pr( msg);
    }

    private class TimerListener implements ActionListener
    {
        public void actionPerformed(ActionEvent evt)
        {
            if(task.isDone() && current <= progressBar.getMaximum() && waitingCycles <= MAX_WAITING_CYCLES - 1)
            {
                waitingCycles++;
                //Err.pr( "Will set progress bar to " + progressBar.getMaximum() );
                progressBar.setString(task.getCompletedPhrase());
                progressBar.setValue(progressBar.getMaximum());
            }
            else if(task.isDone())
            {
                timer.stop();
                setCursor(null); //turn off the wait cursor
                progressBar.setValue(progressBar.getMinimum());
                progressBar.setString("");
                setVisible(false); //dispose I suppose
                //Err.pr( "Dialog should be gone, as task has finished" );
            }
            else
            {
                current += increment;
                //Err.pr( "Will set progress bar to " + current );
                progressBar.setValue(current);
                progressBar.setString(Utils.toPercent(progressBar.getPercentComplete()));
            }
        }
    }

    private void initTask(TaskI task)
    {
        this.task = task;
        setUndecorated(true);
        //setTitle( task.getTitle());
        Assert.isPositive( task.getEstimatedDuration());
        progressBar = new JProgressBar(0, task.getEstimatedDuration());
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        increment = task.getEstimatedDuration() / NUM_SCREEN_REFRESHES;
        //Err.pr( "Increment: " + increment);
        //Err.pr( "Tot length: " + task.getLength());

        JPanel panel = new JPanel();

        panel.add(progressBar);

        JLabel label = new JLabel(task.getTitle());
        Dimension dim = new Dimension();
        dim.width = WIDTH;
        dim.height = HEIGHT / 2;
        label.setPreferredSize(dim);
        getContentPane().add(new JLabel(task.getTitle()), BorderLayout.NORTH);
        getContentPane().add(panel, BorderLayout.SOUTH);

        timer = new Timer(increment, new TimerListener());
        progressBar.setPreferredSize(dim);
        pack();

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = getSize();
        int x = (screen.width - size.width) >> 1;
        int y = (screen.height - size.height) >> 1;
        setLocation(x, y);
    }

    private void beforeTaskStarts()
    {
        JFrame parent = MessageDlg.getFrame();
        if(parent != null)
        {
            parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        current = 0;
    }

    /**
     * When doing synchronously it seems that with the thread switch the
     * timer has missed out on getting the isDone but not showing it
     * event, so we simulate that here. Next we restart which will do
     * the waiting cycles and cleanup. (Actually simplified this part -
     * calling Timer from worker prolly a no-no).
     * <p/>
     * Anything done in one of these
     * workers will be done right away. This important in applications
     * where there is still alot of work to be done on the EDT.
     */
    private void afterTaskSynchronous()
    {
        Worker.post(new Job()
        {
            public Object run()
            {
                try
                {
                    SwingUtilities.invokeAndWait(new Runnable()
                    {
                        public void run()
                        {
                            hideFromView();
                        }
                    });
                }
                catch(InterruptedException e)
                {
                    Err.error(e);
                }
                catch(InvocationTargetException e)
                {
                    Err.error(e);
                }
                /*
                try
                {
                    Thread.sleep(500);
                }
                catch(InterruptedException e)
                {
                    Err.error(e);
                }
                */
                return null;
            }
        });
    }
    
    public long timeTook()
    {
        return stopWatch.getResult();
    }

    private void hideFromView()
    {
        timer.stop();
        progressBar.setValue(progressBar.getMaximum());
        progressBar.setString(task.getCompletedPhrase());
        try
        {
            Thread.sleep(500);
        }
        catch(InterruptedException e)
        {
            Err.error(e);
        }
        JFrame parent = MessageDlg.getFrame();
        if(parent != null)
        {
            parent.setCursor(null); //turn off the wait cursor
        }
        setCursor(null);
        dispose();
        //setVisible( false);
        //for next time
         /**/
        progressBar.setValue(progressBar.getMinimum());
        progressBar.setString("");
         /**/
        Err.pr(SdzNote.GENERIC, "Have set to not visible in EDT " + SwingUtilities.isEventDispatchThread());
    }
    
    public TimeBandI getTimeBand()
    {
        return task;
    }
}
