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
package fr.norsys.asoape.codegen.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import fr.norsys.asoape.codegen.metamodel.WSDLComplexType;
import fr.norsys.asoape.codegen.metamodel.WSDLMessage;
import fr.norsys.asoape.codegen.metamodel.WSDLNamespace;
import fr.norsys.asoape.codegen.metamodel.WSDLService;
import fr.norsys.asoape.codegen.metamodel.WSDLSimpleType;

/**
 * String manipulation tools.
 */
public final class StringUtil
{
    private static final String HTTP_SCHEME = "http://";

    private static final String URN_SCHEME = "urn:";

    private static final String[] javaKeywordArray = new String[]{
    		"abstract",
    		"assert",
    		"boolean",
    		"break",
    		"byte",
    		"case",
    		"catch",
    		"char",
    		"class",
    		"const",
    		"continue",
    		"default",
    		"do",
    		"double",
    		"else",
    		"extends",
    		"false",
    		"final",
    		"finally",
    		"float",
    		"for",
    		"goto",
    		"if",
    		"implements",
    		"import",
    		"instanceof",
    		"int",
    		"interface",
    		"long",
    		"native",
    		"new",
    		"null",
    		"package",
    		"private",
    		"protected",
    		"public",
    		"return",
    		"short",
    		"static",
    		"strictfp",
    		"super",
    		"switch",
    		"synchronized",
    		"this",
    		"throw",
    		"throws",
    		"transient",
    		"true",
    		"try",
    		"void",
    		"volatile",
    		"while",
    		};
    private static final List<String> javaKeyword = Arrays.asList(javaKeywordArray);

    private static final Map<String, String> xml2javaTypes = new HashMap<String, String>();

    private static final Map<String, String> xml2fullQualifiedJavaTypes = new HashMap<String, String>();

    private static StringUtil instance = new StringUtil();

    static
    {
        xml2javaTypes.put( "{http://www.w3.org/2001/XMLSchema}anyURI", "java.net.URI" );
        xml2javaTypes.put( "{http://www.w3.org/2001/XMLSchema}base64Binary", "byte[]" );
        xml2javaTypes.put( "{http://www.w3.org/2001/XMLSchema}boolean", "Boolean" );
        xml2javaTypes.put( "{http://www.w3.org/2001/XMLSchema}date", "Date" );
        xml2javaTypes.put( "{http://www.w3.org/2001/XMLSchema}dateTime", "Date" );
        xml2javaTypes.put( "{http://www.w3.org/2001/XMLSchema}decimal", "BigDecimal" );
        xml2javaTypes.put( "{http://www.w3.org/2001/XMLSchema}double", "Double" );
        xml2javaTypes.put( "{http://www.w3.org/2001/XMLSchema}float", "Float" );
        xml2javaTypes.put( "{http://www.w3.org/2001/XMLSchema}int", "Integer" );
        xml2javaTypes.put( "{http://www.w3.org/2001/XMLSchema}integer", "BigInteger" );
        xml2javaTypes.put( "{http://www.w3.org/2001/XMLSchema}short", "Short" );
        xml2javaTypes.put( "{http://www.w3.org/2001/XMLSchema}string", "String" );

        xml2fullQualifiedJavaTypes.put( "{http://www.w3.org/2001/XMLSchema}anyURI", "java.net.URI" );
        xml2fullQualifiedJavaTypes.put( "{http://www.w3.org/2001/XMLSchema}base64Binary", "byte[]" );
        xml2fullQualifiedJavaTypes.put( "{http://www.w3.org/2001/XMLSchema}boolean", "java.lang.Boolean" );
        xml2fullQualifiedJavaTypes.put( "{http://www.w3.org/2001/XMLSchema}date", "java.util.Date" );
        xml2fullQualifiedJavaTypes.put( "{http://www.w3.org/2001/XMLSchema}dateTime", "java.util.Date" );
        xml2fullQualifiedJavaTypes.put( "{http://www.w3.org/2001/XMLSchema}decimal", "java.math.BigDecimal" );
        xml2fullQualifiedJavaTypes.put( "{http://www.w3.org/2001/XMLSchema}double", "java.lang.Double" );
        xml2fullQualifiedJavaTypes.put( "{http://www.w3.org/2001/XMLSchema}float", "java.lang.Float" );
        xml2fullQualifiedJavaTypes.put( "{http://www.w3.org/2001/XMLSchema}int", "java.lang.Integer" );
        xml2fullQualifiedJavaTypes.put( "{http://www.w3.org/2001/XMLSchema}integer", "java.math.BigInteger" );
        xml2fullQualifiedJavaTypes.put( "{http://www.w3.org/2001/XMLSchema}short", "java.lang.Short" );
        xml2fullQualifiedJavaTypes.put( "{http://www.w3.org/2001/XMLSchema}string", "java.lang.String" );
    }

    protected StringUtil()
    {
    }

    public static StringUtil getInstance()
    {
        return instance;
    }

    public static String toJavaType( WSDLSimpleType type )
    {
        if ( type.isEnumeration() )
        {
            return capitalize( type.getName() );
        }
        String foundType = xml2javaTypes.get( String.format( "{%s}%s", type.getNamespace().getName(), type.getName() ) );
        if ( foundType != null )
        {
            return foundType;
        }
        return type.getName();
    }

    public static String toJavaType( WSDLComplexType type )
    {
        return capitalize( type.getName() );
    }

    private static String toFullQualifiedJavaTypeName( WSDLNamespace namespace, String name )
    {
        return String.format( "%s.%s", toJavaPackage( namespace ), capitalize( name ) );
    }

    public static String capitalize( String name )
    {
        return String.format( "%s%s", name.substring( 0, 1 ).toUpperCase(), name.substring( 1 ) );
    }

    public static String toJavaPackage( WSDLNamespace namespace )
    {
        String namespaceUri = namespace.getName();
        if ( namespaceUri.startsWith( HTTP_SCHEME ) )
        {
            namespaceUri = namespaceUri.substring( HTTP_SCHEME.length() );
        }
        else if ( namespaceUri.startsWith( URN_SCHEME ) )
        {
            namespaceUri = namespaceUri.substring( URN_SCHEME.length() );
        }
        StringBuffer buffer = new StringBuffer();
        String[] splits = namespaceUri.split( "/" );
        StringTokenizer tokenizer = new StringTokenizer( splits[0], "." );
        buffer.append( tokenizer.nextToken() );
        while ( tokenizer.hasMoreElements() )
        {
            buffer.insert( 0, tokenizer.nextToken() + "." );
        }
        for ( int i = 1; i < splits.length; i++ )
        {
            buffer.append( "." + splits[i] );
        }
        return buffer.toString().toLowerCase();
    }

    public static String toFullQualifiedJavaType( WSDLComplexType type )
    {
        return toFullQualifiedJavaTypeName( type.getNamespace(), type.getName() );
    }

    public static String toFullQualifiedJavaType( WSDLSimpleType type )
    {
        if ( type.isEnumeration() )
        {
            return toFullQualifiedJavaTypeName( type.getNamespace(), type.getName() );
        }
        String foundType = xml2fullQualifiedJavaTypes.get( String.format( "{%s}%s", type.getNamespace().getName(), type.getName() ) );
        if ( foundType != null )
        {
            return foundType;
        }
        return type.getName();
    }

    public static String toJavaType( WSDLService wsdlService )
    {
        return capitalize( wsdlService.getName() );
    }

    public static boolean isJavaKeyword( String text )
    {
    	return javaKeyword.contains( text );
    }

    public static String toJavaType( WSDLMessage wsdlMessage )
    {
        return capitalize( wsdlMessage.getName() );
    }
}
