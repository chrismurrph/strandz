package org.strandz.lgpl.tablelayout;

import org.strandz.lgpl.util.MethodUtil;

import java.beans.DefaultPersistenceDelegate;
import java.beans.Expression;
import java.beans.Encoder;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * User: Chris
 * Date: 23/10/2008
 * Time: 23:38:56
 */
public class ConstraintsPersistenceDelegate extends DefaultPersistenceDelegate
{
    private String[] constructor;

    public ConstraintsPersistenceDelegate(String[] constructorPropertyNames)
    {
        super(constructorPropertyNames);
        this.constructor = constructorPropertyNames;
    }

    /**
     * Doing this method the old way
     */
    protected Expression instantiate(Object oldInstance, Encoder out) {
        int nArgs = constructor.length;
        Class type = oldInstance.getClass();
        // System.out.println("writeObject: " + oldInstance);
        Object[] constructorArgs = new Object[nArgs];
        for(int i = 0; i < nArgs; i++) {
            /*
            1.2 introduces "public double getX()" et al. which return values
            which cannot be used in the constructors (they are the wrong type).
            In constructors, use public fields in preference to getters
            when they are defined.
            */
            String name = constructor[i];

            Field f = null;
            try {
                // System.out.println("Trying field " + name + " in " + type);
                f = type.getDeclaredField(name);
                f.setAccessible(true);
            }
            catch (NoSuchFieldException e) {}
            try {
                constructorArgs[i] = (f != null && !Modifier.isStatic(f.getModifiers())) ?
                f.get(oldInstance) :
                MethodUtil.invoke(ReflectionUtils.getPublicMethod(type, "get" + NameGenerator.capitalize(name),
                    new Class[0]), oldInstance, new Object[0]);
            }
            catch (Exception e) {
                // handleError(e, "Warning: Failed to get " + name + " property for " + oldInstance.getClass() + " constructor");
                out.getExceptionListener().exceptionThrown(e);
            }
        }
        return new Expression(oldInstance, oldInstance.getClass(), "new", constructorArgs);
    }
}
