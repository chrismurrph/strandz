#!/bin/sh

cd $SDZ/bin

rm $SDZ/docs/TEST-*.txt
cp $SDZ/docs/unittest.results.old $SDZ/docs/unittest.results.old2
cp $SDZ/docs/unittest.results $SDZ/docs/unittest.results.old
ant -f jUnit.xml > $SDZ/docs/unittest.results

#
# Increasing these numbers GOOD
# (If removing a test reduce these numbers together dummy!)
#
E_ALL_CLEAR=16 
E_LENGTH=19

#org.strandz.core.extent.TestArrayExtent #1
#org.strandz.core.extent.TestListExtent
#org.strandz.core.extent.TestSetExtent
#org.strandz.core.extent.TestTie
#org.strandz.test.TestTreeMethods
#org.strandz.test.fishbowl.TestClientsChildFocus
#org.strandz.test.fishbowl.TestComplexNavigation
#org.strandz.test.fishbowl.TestORMapping
#REMOVED to Designer org.strandz.test.fishbowl.TestTableORMap
#org.strandz.test.fishbowl.TestXMLEncoding
#org.strandz.test.wombatrescue.TestORM
#org.strandz.test.wombatrescue.TestORMOperations #12
#...
#org.strandz.test.wombatrescue.NonVisualTestRoster #13
#org.strandz.test.wombatrescue.TestFileUpload #14
#org.strandz.test.wombatrescue.TestRosterServiceResults

#
# Increasing these numbers BAD
#
E_ONE_ERROR=0 #
E_TWO_ERROR=0 # 
E_ONE_FAIL=1 # NonVisualTestRoster 
E_TWO_FAIL=1 # TestORMapping
E_ONE_EACH=0 #
# These two ought to be corrected but are to do with the old style of table - means effectively no table testing ...
E_FIVE_ERROR=1 # TestTableORMap
E_THREE_ERROR=0 # TestClientsChildFocus

ALL_CLEAR=`grep "Failures: 0, Errors: 0," $SDZ/docs/unittest.results | wc -l`
ONE_ERROR=`grep "Failures: 0, Errors: 1," $SDZ/docs/unittest.results | wc -l`
TWO_ERROR=`grep "Failures: 0, Errors: 2," $SDZ/docs/unittest.results | wc -l`
ONE_FAIL=`grep "Failures: 1, Errors: 0," $SDZ/docs/unittest.results | wc -l`
TWO_FAIL=`grep "Failures: 2, Errors: 0," $SDZ/docs/unittest.results | wc -l`
ONE_EACH=`grep "Failures: 1, Errors: 1," $SDZ/docs/unittest.results | wc -l`
FIVE_ERROR=`grep "Failures: 0, Errors: 5," $SDZ/docs/unittest.results | wc -l`
THREE_ERROR=`grep "Failures: 0, Errors: 3," $SDZ/docs/unittest.results | wc -l`

LENGTH=`grep "Tests run:" $SDZ/docs/unittest.results | wc -l`
HAVE_ERROR="FALSE"
if [ $ALL_CLEAR != $E_ALL_CLEAR -o $ONE_ERROR != $E_ONE_ERROR -o $ONE_FAIL != $E_ONE_FAIL -o $TWO_FAIL != $E_TWO_FAIL -o $TWO_ERROR != $E_TWO_ERROR -o $ONE_EACH != $E_ONE_EACH  -o $FIVE_ERROR != $E_FIVE_ERROR  -o $THREE_ERROR != $E_THREE_ERROR ]
then
  echo "Problem with unit testing:: all clear: $ALL_CLEAR, one error: $ONE_ERROR, two error: $TWO_ERROR, one failure: $ONE_FAIL, two failures: $TWO_FAIL, one each: $ONE_EACH, five error: $FIVE_ERROR, three error: $THREE_ERROR"
  echo "                 Expected:: all clear: $E_ALL_CLEAR, one error: $E_ONE_ERROR, two error: $E_TWO_ERROR, one failure: $E_ONE_FAIL, two failures: $E_TWO_FAIL, one each: $E_ONE_EACH, five error: $E_FIVE_ERROR, three error: $E_THREE_ERROR"
  echo ""
  cat $SDZ/docs/unittest.results
  HAVE_ERROR="TRUE"
fi
if [ $LENGTH != $E_LENGTH ]
then
  echo "Probably need to add another failures/errors marker eg. TWO_FAIL_ONE_ERROR, length got is $LENGTH, expected $E_LENGTH, all clear length got $ALL_CLEAR, whereas expected $E_ALL_CLEAR"
  HAVE_ERROR="TRUE"
fi
if [ $HAVE_ERROR != "TRUE" ]
then
  echo ""
  echo "All clear with unit testing where number of test cases is $LENGTH of which all clear is $ALL_CLEAR (expected $E_ALL_CLEAR)"
  echo ""
fi

#Never exit here, as the locking won't be completed in the calling script 
#exit 0

#echo "JDepend taking too much time so start doing it on the desktop"
ant -f jDepend.xml
TITLE1=`grep -n "\- Package Dependency Cycles:" $SDZ/docs/jdepend-report.txt | cut -d: -f1`
TITLE2=`grep -n "\- Summary:" $SDZ/docs/jdepend-report.txt | cut -d: -f1`
DIFF=`expr $TITLE2 - $TITLE1`
#echo $DIFF
echo ""
if [ $DIFF != 5 ]
then
  echo "Problem with recursive deps"
else
  echo "All clear with recursive deps"
fi
