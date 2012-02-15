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
package fr.norsys.asoape.codegen.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import javax.wsdl.Definition;
import javax.wsdl.Types;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.schema.Schema;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.norsys.asoape.codegen.metamodel.WSDLComplexType;
import fr.norsys.asoape.codegen.metamodel.WSDLElement;
import fr.norsys.asoape.codegen.metamodel.WSDLNamespace;
import fr.norsys.asoape.codegen.metamodel.WSDLSimpleType;
import fr.norsys.asoape.codegen.metamodel.WSDLType;
import fr.norsys.asoape.codegen.metamodel.impl.WSDLComplexTypeBean;
import fr.norsys.asoape.codegen.metamodel.impl.WSDLElementBean;
import fr.norsys.asoape.codegen.metamodel.impl.WSDLNamespaceBean;
import fr.norsys.asoape.codegen.metamodel.impl.WSDLSimpleTypeBean;

/**
 * Repository that encapsulate the XSD Element and Type request like a small databases.
 */
class WSDLTypesRepository
{
    /**
     * Initialize the repository from the definition.
     * 
     * @param definition Analyzed definition.
     */
    public WSDLTypesRepository( Definition definition )
    {
        @SuppressWarnings( "unchecked" )
        Map<String, String> namespaces = definition.getNamespaces();
        for ( String prefix : namespaces.keySet() )
        {
            namespaceUriByPrefix.put( prefix, namespaces.get( prefix ) );
        }
        this.definitionTypes = definition.getTypes();
        this.xpath = XPathFactory.newInstance().newXPath();
        this.xpath.setNamespaceContext( new NamespaceContext() );
        this.namespacesImported = new ArrayList<String>();
    }

    /**
     * Find the element that match the qualified name.
     * 
     * @param elementName Qualified name of the researched element.
     * @return Return the found element which is at the root level of schema definition.
     */
    public WSDLElement findElementByQname( QName elementName )
    {
        WSDLElement element = elementsByQName.get( elementName );
        if ( element == null )
        {
            element = searchElement( elementName );
            elementsByQName.put( elementName, element );
        }
        return element;
    }

    private WSDLElement searchElement( QName elementName )
    {
        Element schemaRootElement = findSchemaRootElement( elementName.getNamespaceURI() );
        try
        {
            XPathExpression selectElement = xpath.compile( String.format( "./xsd:element[@name='%s']",
                                                                          elementName.getLocalPart() ) );
            Element elementElement = (Element) selectElement.evaluate( schemaRootElement, XPathConstants.NODE );
            WSDLNamespace namespace = findNamespaceByUri( elementName.getNamespaceURI() );
            WSDLElement element = buildElement( namespace, elementElement );
            element.setNamespace( namespace );
            return element;
        }
        catch ( XPathExpressionException cause )
        {
            cause.printStackTrace();
        }
        return null;
    }

    /**
     * Find the type that match the qualified type name.
     * 
     * @param typeName Name of the desired type.
     * @return Return the type found corresponding to this name.
     */
    public WSDLType findTypeByQname( QName typeName )
    {
        WSDLType type = typesByQName.get( typeName );
        if ( type == null )
        {
            type = findPendingType( typeName );
            if ( type == null )
            {
                type = searchType( typeName );
                typesByQName.put( typeName, type );
            }
        }
        return type;
    }

    private WSDLType findPendingType( QName typeName )
    {
        for ( WSDLComplexType type : pendingComplexType )
        {
            if ( typeName.getLocalPart().equals( type.getName() )
                 && typeName.getNamespaceURI().equals( typeName.getNamespaceURI() ) )
            {
                return type;
            }
        }
        return null;
    }

    private WSDLType searchType( QName typeName )
    {
        if ( XMLConstants.W3C_XML_SCHEMA_NS_URI.equals( typeName.getNamespaceURI() ) // XSD namespace
             || SOAPConstants.URI_NS_SOAP_ENCODING.equals( typeName.getNamespaceURI() ) ) // SOAP Enc namespace
        {
            WSDLSimpleType xsdType = new WSDLSimpleTypeBean();
            xsdType.setName( typeName.getLocalPart() );
            xsdType.setNamespace( findNamespaceByUri( typeName.getNamespaceURI() ) );
            return xsdType;
        }
        else
        {
            try
            {
                String selectTypesQuery = String.format( "./xsd:complexType[@name='%1$s']|xsd:simpleType[@name='%1$s']",
                                                         typeName.getLocalPart() );
                XPathExpression selectTypes = xpath.compile( selectTypesQuery );
                Element schemaRootElement = findSchemaRootElement( typeName.getNamespaceURI() );
                NodeList typesList = (NodeList) selectTypes.evaluate( schemaRootElement, XPathConstants.NODESET );
                if ( typesList.getLength() == 1 )
                {
                    Element typeElement = (Element) typesList.item( 0 );
                    WSDLType type = buildType( findNamespaceByUri( typeName.getNamespaceURI() ), typeElement );
                    return type;
                }
                else
                {
                    return null;
                }
            }
            catch ( XPathExpressionException cause )
            {
                cause.printStackTrace();
            }
            return null;
        }
    }

    private Element findSchemaRootElement( String namespaceURI )
    {
        @SuppressWarnings( "rawtypes" )
        Iterator extensibilityElementsIterator = definitionTypes.getExtensibilityElements().iterator();
        namespacesImported.clear();
        while ( extensibilityElementsIterator.hasNext() )
        {
            ExtensibilityElement extensibilityElement = (ExtensibilityElement) extensibilityElementsIterator.next();
            Schema schema = (Schema) extensibilityElement;
            Element schemaRootElement = schema.getElement();
            String targetNamespace = schemaRootElement.getAttribute( "targetNamespace" );
            if ( namespaceURI.equals( targetNamespace ) )
            {
                NamedNodeMap schemaAttributes = schemaRootElement.getAttributes();
                for ( int i = 0; i < schemaAttributes.getLength(); i++ )
                {
                    Node attributeNode = schemaAttributes.item( i );
                    String nodeName = attributeNode.getNodeName();
                    if ( nodeName.startsWith( "xmlns:" ) )
                    {
                        String namespacePrefix = nodeName.substring( nodeName.indexOf( ":" ) + 1 );
                        String namespaceUri = attributeNode.getNodeValue();
                        if ( namespaceByUri.get( namespaceUri ) == null )
                        {
                            WSDLNamespace namespace = new WSDLNamespaceBean();
                            namespace.setName( namespaceUri );
                            namespaceByUri.put( namespaceUri, namespace );
                        }
                        if ( namespaceUriByPrefix.get( namespacePrefix ) == null )
                        {
                            namespaceUriByPrefix.put( namespacePrefix, namespaceUri );
                        }
                    }
                }
                saveSchemaImports( schemaRootElement );
                return schemaRootElement;
            }
        }
        return null;
    }

    private void saveSchemaImports( Element schemaRootElement )
    {
        try
        {
            XPathExpression selectTypes = xpath.compile( "./xsd:import" );
            NodeList typesList = null;
            typesList = (NodeList) selectTypes.evaluate( schemaRootElement, XPathConstants.NODESET );
            for ( int i = 0; i < typesList.getLength(); i++ )
            {
                Element typeElement = (Element) typesList.item( 0 );
                String namespaceName = typeElement.getAttribute( "namespace" );
                namespacesImported.add( namespaceName );
            }
        }
        catch ( XPathExpressionException e )
        {
            e.printStackTrace();
        }
    }

    private WSDLType buildType( WSDLNamespace namespace, Element typeElement )
        throws XPathExpressionException
    {
        WSDLType type = null;
        if ( "simpleType".equals( typeElement.getLocalName() ) )
        {
            type = buildSimpleType( namespace, typeElement );
        }
        else if ( "complexType".equals( typeElement.getLocalName() ) )
        {
            type = buildComplexType( namespace, typeElement );
        }
        return type;
    }

    private WSDLType buildSimpleType( WSDLNamespace namespace, Element typeElement )
        throws XPathExpressionException
    {
        WSDLSimpleType type = new WSDLSimpleTypeBean();
        type.setName( typeElement.getAttribute( "name" ) );
        type.setNamespace( namespace );
        XPathExpression selectRestriction = xpath.compile( "./xsd:restriction" );
        Element restrictionElement = (Element) selectRestriction.evaluate( typeElement, XPathConstants.NODE );
        String baseType = restrictionElement.getAttribute( "base" );
        QName baseTypeName = prefixedNameToQName( baseType );
        type.setBaseType( findTypeByQname( baseTypeName ) );
        XPathExpression selectEnumerations = xpath.compile( "./xsd:enumeration" );
        NodeList enumerations = (NodeList) selectEnumerations.evaluate( restrictionElement, XPathConstants.NODESET );
        if ( enumerations.getLength() == 0 )
        {
            // FIXME Treat fittingly the restriction that is not an enumeration
            return type.getBaseType();
        }
        else
        {
            for ( int i = 0; i < enumerations.getLength(); i++ )
            {
                Element enumerationElement = (Element) enumerations.item( i );
                String value = enumerationElement.getAttribute( "value" );
                type.getValues().add( value );
            }
        }
        return type;
    }

    private WSDLType buildComplexType( WSDLNamespace namespace, Element typeElement )
        throws XPathExpressionException
    {
        WSDLComplexType type = new WSDLComplexTypeBean();
        type.setName( typeElement.getAttribute( "name" ) );
        type.setNamespace( namespace );
        pendingComplexType.push( type );
        populateComplexType( namespace, type, typeElement );
        addExtensionIfAny( namespace, type, typeElement );
        addRestrictionIfAny( namespace, type, typeElement );
        pendingComplexType.pop();
        return type;
    }

    private void addExtensionIfAny( WSDLNamespace namespace, WSDLComplexType type, Element typeElement )
        throws XPathExpressionException
    {
        XPathExpression selectExtension = xpath.compile( "./xsd:complexContent/xsd:extension" );
        NodeList extensionElmements = (NodeList) selectExtension.evaluate( typeElement, XPathConstants.NODESET );
        if ( extensionElmements.getLength() == 1 )
        {
            Element extensionElement = (Element) extensionElmements.item( 0 );
            String ancestor = extensionElement.getAttribute( "base" );
            QName ancestorName = prefixedNameToQName( ancestor );
            type.setExtension( (WSDLComplexType) findTypeByQname( ancestorName ) );
            populateComplexType( namespace, type, extensionElement );
        }
    }

    private void addRestrictionIfAny( WSDLNamespace namespace, WSDLComplexType type, Element typeElement )
        throws XPathExpressionException
    {
        XPathExpression selectRestriction = xpath.compile( "./xsd:complexContent/xsd:restriction" );
        NodeList restrictionElements = (NodeList) selectRestriction.evaluate( typeElement, XPathConstants.NODESET );
        if ( restrictionElements.getLength() == 1 )
        {
            Element restrictionElement = (Element) restrictionElements.item( 0 );
            String baseType = restrictionElement.getAttribute( "base" );
            QName baseTypeName = prefixedNameToQName( baseType );
            type.setBaseType( findTypeByQname( baseTypeName ) );
            if ( SOAPConstants.URI_NS_SOAP_ENCODING.equals( baseTypeName.getNamespaceURI() )
                 && "Array".equals( baseTypeName.getLocalPart() ) )
            {
                XPathExpression selectAttribute = xpath.compile( "./xsd:attribute" );
                Element attributeElement = (Element) selectAttribute.evaluate( restrictionElement, XPathConstants.NODE );
                String arrayType = attributeElement.getAttributeNS( "http://schemas.xmlsoap.org/wsdl/", "arrayType" );
                QName arrayTypeName = prefixedNameToQName( arrayType );
                arrayType = arrayTypeName.getLocalPart();
                arrayTypeName = new QName( arrayTypeName.getNamespaceURI(),
                                           arrayType.substring( 0, arrayType.length() - 2 ) );
                WSDLElement member = new WSDLElementBean();
                member.setName( "array" );
                member.setType( findTypeByQname( arrayTypeName ) );
                member.setNamespace( namespace );
                member.setMinOccurs( WSDLElement.ZERO );
                member.setMaxOccurs( WSDLElement.UNBOUNDED );
                type.getMembers().add( member );
            }
        }
    }

    private void populateComplexType( WSDLNamespace namespace, WSDLComplexType type, Element typeElement )
        throws XPathExpressionException
    {
        XPathExpression selectNestedElements = xpath.compile( "./xsd:sequence/xsd:element|./xsd:all/xsd:element" );
        NodeList nestedElements = (NodeList) selectNestedElements.evaluate( typeElement, XPathConstants.NODESET );
        for ( int i = 0; i < nestedElements.getLength(); i++ )
        {
            Element nestedElement = (Element) nestedElements.item( i );
            WSDLElement member = buildElement( namespace, nestedElement );
            member.setNamespace( namespace );
            type.getMembers().add( member );
        }
    }

    private WSDLElement buildElement( WSDLNamespace namespace, Element elementElement )
    {
        WSDLElement element = new WSDLElementBean();
        element.setName( elementElement.getAttribute( "name" ) );
        // Un element n'a pas pour objet d'exister dans un namespace en dehors
        // des éléments à la racine des schémas.
        // element.setNamespace( namespace );
        String nillable = elementElement.getAttribute( "nillable" ); // Merci
                                                                     // Axis!
        if ( nillable != null && !nillable.isEmpty() )
        {
            if ( "true".equals( nillable ) )
            {
                element.setMinOccurs( WSDLElement.ZERO );
            }
            else if ( "false".equals( nillable ) )
            {
                element.setMinOccurs( WSDLElement.ONE );
            }
        }
        else
        {
            String minOccurs = elementElement.getAttribute( "minOccurs" );
            if ( minOccurs != null && !minOccurs.isEmpty() )
            {
                element.setMinOccurs( minOccurs );
            }
        }
        String maxOccurs = elementElement.getAttribute( "maxOccurs" );
        if ( maxOccurs != null && !maxOccurs.isEmpty() )
        {
            element.setMaxOccurs( maxOccurs );
        }
        String typeString = elementElement.getAttribute( "type" );
        if ( typeString.isEmpty() )
        {
            try
            {
                XPathExpression selectNestedType = xpath.compile( "./xsd:complexType|xsd:simpleType" );
                Element nestedTypeElement = (Element) selectNestedType.evaluate( elementElement, XPathConstants.NODE );
                WSDLType elementType = buildType( namespace, nestedTypeElement );
                elementType.setName( element.getName() );
                element.setType( elementType );
            }
            catch ( XPathExpressionException cause )
            {
                cause.printStackTrace();
            }
        }
        else
        {
            WSDLType type;
            QName typeName = prefixedNameToQName( typeString );
            type = findTypeByQname( typeName );
            if ( type == null )
            {
                type = findTypeWithImports( typeString );
            }
            element.setType( type );
        }
        return element;
    }

    private WSDLType findTypeWithImports( String typeString )
    {
        WSDLType type = null;
        String localPart = typeString.substring( typeString.indexOf( ":" ) + 1 );
        for ( String namespaceName : namespacesImported )
        {
            type = findTypeByQname( new QName( namespaceName, localPart ) );
            if ( type != null )
            {
                return type;
            }
        }
        return type;
    }

    private QName prefixedNameToQName( String prefixedName )
    {
        if ( prefixedName.indexOf( ":" ) < 0 )
        {
            return new QName( XMLConstants.W3C_XML_SCHEMA_NS_URI, prefixedName );
        }
        else
        {
            String prefix = prefixedName.substring( 0, prefixedName.indexOf( ":" ) );
            String localPart = prefixedName.substring( prefixedName.indexOf( ":" ) + 1 );
            return new QName( namespaceUriByPrefix.get( prefix ), localPart );
        }
    }

    private WSDLNamespace findNamespaceByUri( String namespaceUri )
    {
        WSDLNamespace namespace = namespaceByUri.get( namespaceUri );
        if ( namespace == null )
        {
            namespace = new WSDLNamespaceBean();
            namespace.setName( namespaceUri );
            namespaceByUri.put( namespaceUri, namespace );
        }
        return namespace;
    }

    private class NamespaceContext
        implements javax.xml.namespace.NamespaceContext
    {
        private Map<String, String> namespaceUriByPrefix = new HashMap<String, String>();

        private Map<String, String> prefixByNamespaceUri = new HashMap<String, String>();

        public NamespaceContext()
        {
            namespaceUriByPrefix.put( "xsd", XMLConstants.W3C_XML_SCHEMA_NS_URI );
            prefixByNamespaceUri.put( XMLConstants.W3C_XML_SCHEMA_NS_URI, "xsd" );
        }

        @Override
        public String getNamespaceURI( String prefix )
        {
            return namespaceUriByPrefix.get( prefix );
        }

        @Override
        public String getPrefix( String namespaceURI )
        {
            return prefixByNamespaceUri.get( namespaceURI );
        }

        @Override
        @SuppressWarnings( "rawtypes" )
        public Iterator getPrefixes( String namespaceURI )
        {
            return null;
        }
    }

    /* Definition type source. */
    private Types definitionTypes;

    /* XPath query tool. */
    private XPath xpath;

    /* Found Element cache. */
    private Map<QName, WSDLElement> elementsByQName = new HashMap<QName, WSDLElement>();

    /* Found type cache. */
    private Map<QName, WSDLType> typesByQName = new HashMap<QName, WSDLType>();

    /* Found namespace. */
    private Map<String, WSDLNamespace> namespaceByUri = new HashMap<String, WSDLNamespace>();

    /* Namespace URI indexed by prefix. */
    private Map<String, String> namespaceUriByPrefix = new HashMap<String, String>();

    /* Pending complexType enabling recursivity. */
    private Stack<WSDLComplexType> pendingComplexType = new Stack<WSDLComplexType>();

    /* Namespaces imported by the last SchemaRootElement used */
    private ArrayList<String> namespacesImported;
}
