
copy C:\sdz-etc\lib\*.* C:\sdz-etc-old\lib 
copy C:\sdz-etc\lib\MDateSelector-mine._jar C:\sdz-etc-old\lib\MDateSelector-mine.jar 

copy C:\sdz-etc\property-files\global.properties C:\sdz-etc-old\property-files 

copy C:\sdz-etc\spring\build.properties C:\sdz-etc-old\spring 

copy C:\sdz-etc\spring\reload.xml C:\sdz-etc-old\spring

copy C:\sdz-etc\spring\teresa\war\WEB-INF\applicationContext-common-business.xml C:\sdz-etc-old\spring\teresa\war\WEB-INF

copy C:\sdz-etc\property-files\wombatrescue\clientContext.xml C:\sdz-etc-old\property-files\wombatrescue

copy C:\sdz-etc\property-files\wombatrescue\clientforlocal.properties C:\sdz-etc-old\property-files\wombatrescue

copy C:\sdz-etc\property-files\wombatrescue\wombat-prod.properties C:\sdz-etc-old\property-files\wombatrescue

copy C:\sdz-etc\bin-jdo\enhance.bat C:\sdz-etc-old\bin-jdo

rem global.properies also needs to be changed so src.root and out.root are dev-old not dev
copy C:\data\external-files\old.global.properties C:\sdz-etc-old\property-files\global.properties
