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
package org.strandz.data.fishbowl.objects;

import org.strandz.lgpl.data.objects.Timespace;

import java.io.Serializable;

/**
 * Job Requirement - AdaptersList are: skillCategory required,
 * timespace, associate, confirmed. Part of updating a Job would
 * be to add and remove job requirements. When a job requirement is first
 * added the only attributes that will be filled in will be
 * skillCategory required and timespace. After the skillCategory-matching
 * process the associate attribute may have been filled in. I
 * put in confirmed because I thought that you could mark in confirmed
 * when you have phoned up the staff member and he has
 * commited himself to the job.
 */
public class JobRequirement implements Serializable
{
    private SkillCategory skillCategory;
    private Timespace timespace;
    private Associate associate;
    private Job job;
    private Boolean confirmed;

    public JobRequirement()
    {
    }

    public SkillCategory getSkillCategory()
    {
        return skillCategory;
    }

    public void setSkillCategory(SkillCategory skillCategory)
    {
        this.skillCategory = skillCategory;
    }

    public Timespace getTimespace()
    {
        return timespace;
    }

    public void setTimespace(Timespace timespace)
    {
        this.timespace = timespace;
    }

    public Associate getAssociate()
    {
        return associate;
    }

    public void setAssociate(Associate associate)
    {
        this.associate = associate;
    }

    public Job getJob()
    {
        return job;
    }

    public void setJob(Job job)
    {
        this.job = job;
    }

    public Boolean getConfirmed()
    {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed)
    {
        this.confirmed = confirmed;
    }
}
