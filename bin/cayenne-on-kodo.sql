--
-- Good for a one off change from Kodo to Cayenne if users are not going back again
-- Note must first have created the table auto_pk_support and written values to it, which the Cayenne modeler can do
-- Afterwards check that only one NULL worker exists
-- Also MySql dumps don't seem to be including any workers - get round this by altering the file on the PC.
--
update auto_pk_support
set next_id = 
(
  select sequence_value
  from JDO_SEQUENCE
);

-- The largest count here will show what should be the jdoid of the NULL worker:
--
select belongstogroup_jdoid,count(*) from worker group by belongstogroup_jdoid;
select groupname, unknown0 from worker where jdoid = 48427;
select groupname, unknown0 from worker where jdoid = 48437;

-- If these don't have any values then candidate to get rid of them:
--
select jdoid, christianname, contactname, groupcontactperson, groupname, dummy from worker where belongstogroup_jdoid IS NULL;
-- However first check to see if they are important:
select count(*) from worker where belongstogroup_jdoid = 51931;
-- Get rid:
delete from worker where jdoid = 51931;

--NULL worker query should agree:
select jdoid from worker where dummy = 1; --answer is 48532 on laptop and prod

-- Make sure that all of these exist in worker:
select worker_jdoid from buddymanager;

--test on laptop:
select count(*) from worker where jdoid = 48436;
select count(*) from worker where jdoid = 51306;
select count(*) from worker where jdoid = 48532;
select count(*) from worker where jdoid = 48377;
select count(*) from worker where jdoid = 51306;
select count(*) from worker where jdoid = 48569;
select count(*) from worker where jdoid = 48436;
select count(*) from worker where jdoid = 48569;
select count(*) from worker where jdoid = 51801;
select count(*) from worker where jdoid = 48377;
select count(*) from worker where jdoid = 48438;
select count(*) from worker where jdoid = 48438;
--prod:
select count(*) from worker where jdoid = 48436;
select count(*) from worker where jdoid = 51306;
select count(*) from worker where jdoid = 48438;
select count(*) from worker where jdoid = 48377;
select count(*) from worker where jdoid = 51306;
select count(*) from worker where jdoid = 48569;
select count(*) from worker where jdoid = 48436;
select count(*) from worker where jdoid = 48569;
select count(*) from worker where jdoid = 48438;
select count(*) from worker where jdoid = 48377;
select count(*) from worker where jdoid = 48438;
select count(*) from worker where jdoid = 48438;

--If find one that doesn't exist then substitute a null in there (here 51801 does not exist)
select jdoid from buddymanager where worker_jdoid = 51801; 
--Here result of last query was 48649
update buddymanager set worker_jdoid = 48532 where jdoid = 48649; 

-- Have any workers been entered with null lookups? (expect dummy only)
select christianname, groupname, dummy from worker where seniority_pkid is null;
-- Fix:
delete from worker where seniority_pkid is null and dummy <> 1;

-- Make sure no RosterSlots with null lookups
select jdoid from rosterslot where whichshift_pkid is null;

-- Don't expect to see any NULL unknown0:
select unknown0, count(*) from worker group by unknown0;

-- Dates not right at beginning/end can still occur, for example because of daylight savings 
select jdoid, away1end, away1start, away2end, away2start from worker
where away1end is not null or away1start is not null or away2end is not null or away2start is not null;

select jdoid, away1end, away1start, away2end, away2start from worker
where christianname = "Ivan";
update worker set away1start = "blah" where jdoid = ;

