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

import java.util.HashMap;

import fr.norsys.asoape.xml.binding.annotation.XmlRootElement;

/**
 * (Un)Marshalling context for Java / XML mapping.
 * 
 * @since 1.0
 */
public class BindingContext
{
    /**
     * Add classes to the binding context for XML/Java mapping.
     * 
     * @param classes Classes that must be annotated with {@link XmlRootElement} annotation.
     */
    public void add( Class<?>... classes )
    {
        for ( Class<?> clazz : classes )
        {
            XmlRootElement annotation = clazz.getAnnotation( XmlRootElement.class );
            if ( annotation != null )
            {
                classMap.put( encode( annotation.namespace(), annotation.name() ), clazz );
            }
        }
    }

    /**
     * @return Creates and returns a new {@link Marshaller} for Java to XML mapping.
     * @throws MarshallingException An error occurs during the marshalling initialization process.
     */
    public Marshaller createMarshaller()
        throws MarshallingException
    {
        return new Marshaller();
    }

    /**
     * @return Creates and returns a new {@link Unmarshaller} for XML to Java mapping.
     * @throws UnmarshallingException An error occurs during the unmarshalling initialization process.
     */
    public Unmarshaller createUnmarshaller()
        throws UnmarshallingException
    {
        return new Unmarshaller( this );
    }

    Class<?> get( String namespace, String name )
    {
        return classMap.get( encode( namespace, name ) );
    }

    private String encode( String namespace, String name )
    {
        return String.format( "{%s}%s", namespace, name );
    }

    private HashMap<String, Class<?>> classMap = new HashMap<String, Class<?>>();
}
