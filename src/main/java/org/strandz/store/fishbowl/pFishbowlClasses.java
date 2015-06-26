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
package org.strandz.store.fishbowl;

import org.strandz.data.fishbowl.objects.Client;
import org.strandz.data.fishbowl.objects.ClientIndustry;
import org.strandz.data.fishbowl.objects.ClientType;
import org.strandz.data.fishbowl.objects.Entity;
import org.strandz.data.fishbowl.objects.Job;
import org.strandz.data.fishbowl.objects.JobRequirement;
import org.strandz.data.fishbowl.objects.JobType;
import org.strandz.data.fishbowl.objects.PostalAddress;
import org.strandz.data.fishbowl.objects.Promotion;
import org.strandz.data.fishbowl.objects.PromotionType;
import org.strandz.data.fishbowl.objects.SkillCategory;
import org.strandz.data.fishbowl.objects.StreetAddress;

public class pFishbowlClasses
{
    public static final Class STREET_ADDRESS = StreetAddress.class;
    public static final Class POSTAL_ADDRESS = PostalAddress.class;
    public static final Class CLIENT = Client.class;
    public static final Class ENTITY = Entity.class;
    public static final Class JOB = Job.class;
    public static final Class JOB_REQUIREMENT = JobRequirement.class;
    public static final Class SKILL_CATEGORY = SkillCategory.class;
    public static final Class CLIENT_INDUSTRY = ClientIndustry.class;
    public static final Class CLIENT_TYPE = ClientType.class;
    public static final Class PROMOTION_TYPE = PromotionType.class;
    public static final Class PROMOTION = Promotion.class;
    public static final Class JOB_TYPE = JobType.class;
    public static final Class[] CLASSES = {
        STREET_ADDRESS, POSTAL_ADDRESS, CLIENT, ENTITY, JOB, JOB_REQUIREMENT,
        SKILL_CATEGORY, CLIENT_INDUSTRY, CLIENT_TYPE, PROMOTION_TYPE, PROMOTION,
        JOB_TYPE};
}
