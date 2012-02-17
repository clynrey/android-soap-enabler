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
import java.io.Reader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import static fr.norsys.asoape.xml.binding.ReflectUtils.*;
import fr.norsys.asoape.xml.binding.annotation.XmlElement;
import fr.norsys.asoape.xml.binding.annotation.XmlElement.Cardinality;

/**
 * Object responsible for XML to Java mapping.
 * 
 * @since 1.0
 */
public class Unmarshaller
{
    Unmarshaller( BindingContext bindingContext )
        throws UnmarshallingException
    {
        try
        {
            context = bindingContext;
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware( true );
            parser = factory.newSAXParser();
        }
        catch ( Exception cause )
        {
            throw new UnmarshallingException( cause );
        }
    }

    public Object unmarshall( Reader reader )
        throws UnmarshallingException, IOException
    {
        try
        {
            UnmarshallHandler handler = new UnmarshallHandler( context );
            parser.parse( new InputSource( reader ), handler );
            return handler.getInstance();
        }
        catch ( SAXException cause )
        {
            throw new UnmarshallingException( cause );
        }
    }

    static class UnmarshallHandler
        extends DefaultHandler
    {
        private static final DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );

        public UnmarshallHandler( BindingContext context )
        {
            this.context = context;
        }

        public Object getInstance()
        {
            return instance;
        }

        @Override
        public void startElement( String uri, String localName, String qName, Attributes attributes )
            throws SAXException
        {
            //System.out.printf( "<{%s}%s>%n", uri, localName );
            try
            {
                if ( instance == null )
                {
                    instance = instanciate( context.get( uri, localName ) );
                }
                else if ( property == null )
                {
                    property = findProperty( instance, localName );
                }
                else
                {
                    stack.push( new InstancePropertyPair( instance, property ) );
                    XmlElement xmlElementAnnotation = property.getAnnotation( XmlElement.class );
                    Class<?> targetType = xmlElementAnnotation.targetType();
                    if ( Object.class.equals( targetType ) )
                    {
                        instance = instanciate( context.get( uri, localName ) );
                        property = null;
                    }
                    else
                    {
                        instance = instanciate( targetType );
                        property = findProperty( instance, localName );
                    }
                }
            }
            catch ( Exception exception )
            {
                throw new SAXException( exception );
            }
        }

        @Override
        public void characters( char[] ch, int start, int length )
            throws SAXException
        {
            propertyStringRepresentation.append( ch, start, length );
        }

        @Override
        public void endElement( String uri, String localName, String qName )
            throws SAXException
        {
            //System.out.printf( "</{%s}%s>%n", uri, localName );
            try
            {
                if ( property != null )
                {
                    String value = null;
                    if ( propertyHasStringRepresentation() ) {
                        value = propertyStringRepresentation.toString().trim();
                    }
 
                    if (value != null && value.length() > 0)
                    {
                        XmlElement xmlElementAnnotation = property.getAnnotation( XmlElement.class );
                        Class<?> propertyType = xmlElementAnnotation.targetType();
                        if ( Cardinality.ONE.equals( xmlElementAnnotation.cardinality() ) )
                        {
                            setPropertyValue( instance, property, convert( propertyStringRepresentation.toString(),
                                                                           propertyType ) );
                        }
                        else
                        {
                            addPropertyValue( instance, property, convert( propertyStringRepresentation.toString(),
                                                                           propertyType ) );
                        }
                    }
                    property = null;
                    propertyStringRepresentation = new StringBuilder();
                }
                else
                {
                    if ( !stack.isEmpty() )
                    {
                        InstancePropertyPair previous = stack.pop();
                        Object propertyInstance = instance;
                        instance = previous.getInstance();
                        property = previous.getProperty();
                        XmlElement xmlElementAnnotation = previous.getProperty().getAnnotation( XmlElement.class );
                        if ( Cardinality.ONE.equals( xmlElementAnnotation.cardinality() ) )
                        {
                            setPropertyValue( instance, property, propertyInstance );
                        }
                        else
                        {
                            addPropertyValue( instance, property, propertyInstance );
                        }
                        property = null;
                    }
                }
            }
            catch ( Exception exception )
            {
                throw new SAXException( exception );
            }
        }

        private boolean propertyHasStringRepresentation()
        {
            return propertyStringRepresentation != null
                   && propertyStringRepresentation.length() > 0
                   && !"".equals( propertyStringRepresentation.toString().trim() );
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
		private Object convert( String stringRepresentation, Class<?> propertyType )
            throws SAXException
        {
        	//System.out.printf("representation = %s%n", stringRepresentation);
            if ( BigDecimal.class.equals( propertyType ) )
            {
                return new BigDecimal( stringRepresentation.trim() );
            }
            else if ( BigInteger.class.equals( propertyType ) )
            {
                return new BigInteger( stringRepresentation.trim() );
            }
            else if ( Boolean.class.equals( propertyType ) )
            {
                return Boolean.valueOf( stringRepresentation.trim() );
            }
            else if ( byte[].class.equals( propertyType ) )
            {
            	//	Traduire une chaine base 64 en byte[]
            	return Base64Coder.decode( stringRepresentation.trim() );
            }
            else if ( Date.class.equals( propertyType ) )
            {
                try
                {
                    return dateFormat.parse( stringRepresentation.trim() );
                }
                catch ( ParseException e )
                {
                    return new Date();
                }
            }
            else if ( Double.class.equals( propertyType ) )
            {
                return Double.valueOf( stringRepresentation.trim() );
            }
            else if ( Float.class.equals( propertyType ) )
            {
                return Float.valueOf( stringRepresentation.trim() );
            }
            else if ( Integer.class.equals( propertyType ) )
            {
                return Integer.valueOf( stringRepresentation.trim() );
            }
            else if ( Long.class.equals( propertyType ) )
            {
                return Long.valueOf( stringRepresentation.trim() );
            }
            else if ( String.class.equals( propertyType ) )
            {
                return stringRepresentation.trim();
            }
            else if ( URI.class.equals( propertyType ) )
            {
                return URI.create( stringRepresentation.trim() );
            }
            else if ( propertyType.isEnum() )
            {
                return Enum.valueOf((Class<? extends Enum>) propertyType, stringRepresentation.trim());
            }
            throw new SAXException( String.format( "Can not convert '%s' into '%s' type",
                                                   stringRepresentation.trim(),
                                                   propertyType.getName() ) );

        }

        private static class InstancePropertyPair
        {
            private Object instance;

            private Field property;

            public InstancePropertyPair( Object instance, Field property )
            {
                this.instance = instance;
                this.property = property;
            }

            /**
             * @return the instance
             */
            public Object getInstance()
            {
                return instance;
            }

            /**
             * @return the property
             */
            public Field getProperty()
            {
                return property;
            }
        }

        private BindingContext context;

        private Object instance;

        private Field property;

        private Stack<InstancePropertyPair> stack = new Stack<InstancePropertyPair>();

        private StringBuilder propertyStringRepresentation = new StringBuilder();
    }

    private BindingContext context;

    private SAXParser parser;
}
