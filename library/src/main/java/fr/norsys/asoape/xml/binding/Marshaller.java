/**
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

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import static fr.norsys.asoape.xml.binding.ReflectUtils.*;
import fr.norsys.asoape.xml.binding.annotation.XmlElement;
import fr.norsys.asoape.xml.binding.annotation.XmlElement.Cardinality;
import fr.norsys.asoape.xml.binding.annotation.XmlRootElement;
import fr.norsys.asoape.xml.binding.annotation.XmlType;

/**
 * Object responsible for Java to XML mapping.
 * 
 * @since 1.0
 */
public class Marshaller
{
	private static final DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSS" );
    Marshaller()
        throws MarshallingException
    {
        try
        {
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParserFactory.setNamespaceAware( true );
            xmlSerializer = xmlPullParserFactory.newSerializer();
        }
        catch ( XmlPullParserException cause )
        {
            throw new MarshallingException( cause );
        }
    }

    public void marshall( Object object, Writer writer )
        throws IOException, MarshallingException
    {
        try
        {
            xmlSerializer.setOutput( writer );
            xmlSerializer.startDocument( "UTF-8", null );
            serializeObject( object );
            xmlSerializer.endDocument();
            xmlSerializer.flush();
        }
        catch ( IOException exception )
        {
            throw exception;
        }
        catch ( MarshallingException exception )
        {
            throw exception;
        }
        catch ( Exception cause )
        {
            throw new MarshallingException( cause );
        }
    }

    private void serializeObject( Object object )
        throws IllegalArgumentException, IllegalStateException, IOException, IllegalAccessException,
        MarshallingException
    {
        XmlRootElement annotation = object.getClass().getAnnotation( XmlRootElement.class );
        if ( annotation != null )
        {
            serializeObject( object, annotation.namespace(), annotation.name() );
        }
    }

    private void serializeObject( Object object, String namespace, String name )
        throws IOException, IllegalArgumentException, IllegalStateException, IllegalAccessException,
        MarshallingException
    {
        xmlSerializer.startTag( namespace, name );
        XmlType annotation = object.getClass().getAnnotation( XmlType.class );
        if ( annotation != null && !annotation.isEnum() )
        {
            serializeComplexType( object, namespace );
        }
        else
        {
        	if (object instanceof Date) {
                 Date date = (Date) object;
                 xmlSerializer.text( dateFormat.format(date) );
        	} else {
                 xmlSerializer.text( object.toString() );
         	}
        }
        xmlSerializer.endTag( namespace, name );
    }

    private void serializeComplexType( Object object, String namespace )
        throws MarshallingException, IOException, IllegalAccessException
    {
        List<String> props = findPropOrder( object );
        for ( String propertyName : props )
        {
            Field property = findProperty( object, propertyName );
            if ( property == null )
            {
                throw new MarshallingException( String.format( "property %s not found in class %s",
                                                               propertyName,
                                                               object.getClass().getName() ) );
            }
            property.setAccessible( true );
            XmlElement annotation = property.getAnnotation( XmlElement.class );
            // recuperation du nom de l'element
            String propertyNameElement = annotation.name();
            Object propertyValue = property.get( object );
            if ( propertyValue != null )
            {
                if ( Cardinality.MANY.equals( annotation.cardinality() ) )
                {
                    if ( Object.class.equals( annotation.targetType() ) )
                    {
                        xmlSerializer.startTag( namespace, propertyNameElement );
                        serializeCollection( (Collection<?>) propertyValue );
                        xmlSerializer.endTag( namespace, propertyNameElement );
                    }
                    else
                    {
                        serializeCollection( (Collection<?>) propertyValue, namespace, propertyNameElement );
                    }
                }
                else
                {
                    serializeObject( propertyValue, namespace, propertyNameElement );
                }
            }
            else if ( !annotation.nullable() )
            {
                throw new MarshallingException( String.format( "property '%s' in class '%s' must not be null",
                                                               propertyNameElement,
                                                               object.getClass().getName() ) );
            }
        }
    }

    private void serializeCollection( Collection<?> collection )
        throws IllegalArgumentException, IllegalStateException, IOException, IllegalAccessException,
        MarshallingException
    {
        Iterator<?> iterator = collection.iterator();
        while ( iterator.hasNext() )
        {
            serializeObject( iterator.next() );
        }
    }

    private void serializeCollection( Collection<?> collection, String namespace, String itemName )
        throws IllegalArgumentException, IllegalStateException, IOException, IllegalAccessException,
        MarshallingException
    {
        Iterator<?> iterator = collection.iterator();
        while ( iterator.hasNext() )
        {
            serializeObject( iterator.next(), namespace, itemName );
        }
    }

    private List<String> findPropOrder( Object object )
    {
        Class<?> clazz = object.getClass();
        List<String> props = new ArrayList<String>();
        while ( clazz != null )
        {
            XmlType annotation = clazz.getAnnotation( XmlType.class );
            if ( annotation != null )
            {
                props.addAll( 0, Arrays.asList( annotation.propOrder() ) );
            }
            clazz = clazz.getSuperclass();
        }
        return props;
    }

    private XmlSerializer xmlSerializer;
}
