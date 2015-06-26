package org.strandz.view.wombatrescue;

import javax.swing.*;

/**
 * User: Chris
 * Date: 3/11/2008
 * Time: 11:31:44
 */
public class UploadCapableMonthYearPanel extends MonthYearPanel
{
    private JButton bUpload;
    private JButton bUploadAsOld;

    public void init()
    {
        super.init();
        bUpload = new JButton();
        bUpload.setName( "bUpload");
        bUpload.setText( "Upload");
        bUploadAsOld = new JButton();
        bUploadAsOld.setName( "bUploadAsOld");
        bUploadAsOld.setText( "Finalize");
        add(bUpload, "9, 2");
        add(bUploadAsOld, "11, 2");
        setName( "UploadCapableMonthYearPanel");
        setBUpload( bUpload);
        setBUploadAsOld( bUploadAsOld);
    }
    
    public JButton getBUpload()
    {
        return bUpload;
    }

    public void setBUpload(JButton bUpload)
    {
        this.bUpload = bUpload;
    }

    public JButton getBUploadAsOld()
    {
        return bUploadAsOld;
    }

    public void setBUploadAsOld(JButton bUploadAsOld)
    {
        this.bUploadAsOld = bUploadAsOld;
    }
}
