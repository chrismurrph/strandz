#hiding Kodo
mv $SDZ/lib/kodo.jar $SDZ/lib/kodo._jar
mv $SDZ/lib/jdo2-api-2.0.jar $SDZ/lib/jdo2-api-2.0._jar

#showing JPOX
mv $SDZ/lib/jdo._jar $SDZ/lib/jdo.jar
mv $SDZ/lib/jpox-1.1.1._jar $SDZ/lib/jpox-1.1.1.jar
#Never enhance on server
#mv $SDZ/lib/jpox-enhancer-1.1.1._jar $SDZ/lib/jpox-enhancer-1.1.1.jar
