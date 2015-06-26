 
mysql

use test

select worker_id, dummy from WORKER;

select jdoid, dummy from worker where dummy = 1; //production DB is Kodo
delete from worker where jdoid = 50871;

desc ROSTERSLOT;

--select ROSTERSLOT_ID, WORKER_WORKER_ID_OID
--from ROSTERSLOT;

desc WORKER;

select WORKER_ID, BELONGS_TO_GROUP_WORKER_ID_OID, DUMMY
from WORKER;
 
+-----------+--------------------------------+-------+
| WORKER_ID | BELONGS_TO_GROUP_WORKER_ID_OID | DUMMY |
+-----------+--------------------------------+-------+
|         1 |                              2 |     1 |
|         2 |                           NULL |     1 |
|         3 |                              1 |     0 |
|         4 |                              1 |     0 |
|         5 |                              1 |     0 |


--delete from WORKER
--where BELONGS_TO_GROUP_WORKER_ID_OID = 2;

--or better do next two 

select WORKER_ID
from WORKER
where BELONGS_TO_GROUP_WORKER_ID_OID is not null and
DUMMY = 1;

delete from WORKER
where WORKER_ID = 1; --1 is result of above select


update WORKER 
set BELONGS_TO_GROUP_WORKER_ID_OID = 2 --WORKER_ID we DID NOT delete
where BELONGS_TO_GROUP_WORKER_ID_OID = 1; --WORKER_ID we deleted

update worker 
set surname = 'Andersn(R)'
where jdoid = 52120 and christianname = 'Julian';

