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
package org.strandz.task.wombatrescue;

/**
 * "Expect to find a LOV substitute for a "
 * <p/>
 * LOV substitution is what happens when the program is checking
 * that the current value of a DO field matches to one of the ones
 * in the LOV, using equals(). The additional step of actually
 * substituting the queried value into the same spot in the LOV
 * is always done, but should only be necessary if the two values
 * were not equal from the database-equals point of view. Database
 * specific code would give a better indication of what is actually
 * going on, so this can be a TODO. (It goes without saying that all
 * DB specific code SHOULD be all in one place and be able to be plugged
 * in).
 * <p/>
 * We will solve the data problem that caused the above error
 * message. Problem seemed to be happening (no definitive way
 * of seeing exactly which lookup column is to blame) with
 * lookup of belongsToGroup on a Worker. This is the full error
 * message:
 * <p/>
 * Expect to find a LOV substitute for a null [NULL] of type org.strandz.data.wombatrescue.objects.Worker
 * <p/>
 * Looking at the toString() method for a Worker we can see that
 * [NULL] means dummy. The message is saying that two different
 * dummy workers exist, which could potentially corrupt our
 * database. (An implicit assumption here was that equals and
 * database-equals give the same results, which they should -
 * Strandz is for POJOs). An isDummy has been read out of the
 * DB, which does not exist in the LOV. From stack trace I
 * can see RosterWorkersStrand. Thus the place to look
 * to see how the LOVs are being populated is in
 * RosterWorkersTriggers. I can see that a Worker.NULL
 * that did not originally come from the DB was being used.
 * A big no-no!
 * <p/>
 * If all values come from the DB, and all values go back there,
 * then this problem will never occur. That is why methods such
 * as queryNullWorker() exist. They are to be used rather than
 * Worker.NULL.
 */
public class LOVSubstituteInvestigation
{
}
