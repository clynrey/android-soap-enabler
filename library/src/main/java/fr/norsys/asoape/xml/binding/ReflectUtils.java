/*
 * Android SOAP Enabler is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, 
 * or any later version.
 * 
 * Android SOAP Enabler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the Lesser GNU General Public License
 * along with Android SOAP Enabler.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * ©2011, Android SOAP Enabler Development Team
 */
package fr.norsys.asoape.xml.binding;

import static fr.norsys.asoape.xml.binding.StringUtils.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import fr.norsys.asoape.xml.binding.annotation.XmlElement;

/*
 * Reflection toolbox.
 */
class ReflectUtils
{
    /*
     * Add an instance value to the correspond property hosted by a collection.
     */
    static void addPropertyValue( Object targetInstance, Field targetProperty, Object propertyValue )
        throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException,
        InvocationTargetException
    {
        Object collectionInstance = getPropertyValue( targetInstance, targetProperty );
        Method add = findMethod( collectionInstance, "add", Object.class );
        add.invoke( collectionInstance, propertyValue );
    }

    static Object getPropertyValue( Object targetInstance, Field targetProperty )
        throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
        InvocationTargetException
    {
//        Method getter = findGetterMethod( targetInstance, targetProperty );
//        return getter.invoke( targetInstance );
        targetProperty.setAccessible( true );
        return targetProperty.get( targetInstance );
    }

    /*
     * Return the getter method for the property "targetProperty" inside the instance "targetInstance".
     */
    static Method findGetterMethod( Object targetInstance, Field targetProperty )
        throws SecurityException, NoSuchMethodException
    {
        return findMethod( targetInstance, String.format( "get%s", capitalize( targetProperty.getName() ) ) );
    }

    /*
     * Return the method "methodName" of the instance "targetInstance" using the corresponding "parameterTypes"
     */
    static Method findMethod( Object targetInstance, String methodName, Class<?>... parameterTypes )
        throws SecurityException, NoSuchMethodException
    {
        return targetInstance.getClass().getDeclaredMethod( methodName, parameterTypes );
    }

    /*
     * Return the field "propertyName" of the instance object "targetInstance".
     */
    static Field findProperty( Object targetInstance, String propertyName )
    {
        Class<?> clazz = targetInstance.getClass();
        while ( clazz != null )
        {
            try
            {
                return clazz.getDeclaredField( propertyName );
            }
            catch ( NoSuchFieldException e )
            {
                clazz = clazz.getSuperclass();
            }
        }
        return findPropertyByAlias(targetInstance, propertyName);
    }

    /*
     * Return the field that have the "propertyName" alias.
     */
    private static Field findPropertyByAlias( Object targetInstance, String alias )
    {
        Class<?> clazz = targetInstance.getClass();
        while (clazz != null)
        {
	        Field[] fields = clazz.getDeclaredFields();
	        for (Field field : fields)
	        {
				XmlElement elementAnnotation = field.getAnnotation( XmlElement.class );
				if (alias.equals(elementAnnotation.name()))
				{
					return field;
				}
			}
	        clazz = clazz.getSuperclass();
        }
		return findPropertyOfAxisArray( targetInstance, alias);
	}

    /*
     * Return the field corresponding of the content of array type defined by axis.
     */
	private static Field findPropertyOfAxisArray( Object targetInstance, String propertyName )
    {
        if ( targetInstance.getClass().getName().indexOf( "ArrayOf_" ) >= 0 )
        {
            return targetInstance.getClass().getDeclaredFields()[0];
        }
        return null;
    }

    /*
     * Return the setter method for the property "targetProperty" inside the instance of "targetInstance"/
     */
    static Method findSetterMethod( Object targetInstance, Field property )
        throws SecurityException, NoSuchMethodException
    {
        return findMethod( targetInstance,
                           String.format( "set%s", capitalize( property.getName() ) ),
                           property.getType() );
    }

    /*
     * Create a new instance of "clazz"
     */
    static Object instanciate( Class<?> clazz )
        throws InstantiationException, IllegalAccessException
    {
        return clazz.newInstance();
    }

    /*
     * Set a instance value "propertyValue" to the property "targetProperty" of the instance "targetInstance".
     */
    static void setPropertyValue( Object targetInstance, Field targetProperty, Object propertyValue )
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException,
        NoSuchMethodException
    {
        // Method setter = findSetterMethod( targetInstance, targetProperty );
        // setter.invoke( targetInstance, propertyValue );
        targetProperty.setAccessible( true );
        targetProperty.set( targetInstance, propertyValue );
    }
}
