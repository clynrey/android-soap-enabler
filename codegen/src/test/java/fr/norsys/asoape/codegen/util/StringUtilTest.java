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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.xml.XMLConstants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.norsys.asoape.codegen.metamodel.WSDLComplexType;
import fr.norsys.asoape.codegen.metamodel.WSDLMessage;
import fr.norsys.asoape.codegen.metamodel.WSDLNamespace;
import fr.norsys.asoape.codegen.metamodel.WSDLService;
import fr.norsys.asoape.codegen.metamodel.WSDLSimpleType;

public class StringUtilTest
{
    @Mock
    private WSDLNamespace namespace;

    @Mock
    private WSDLSimpleType simpleType;

    @Mock
    private WSDLComplexType complexType;

    @Mock
    private WSDLMessage message;

    @Mock
    private WSDLService service;

    @Before
    public void setUp()
        throws Exception
    {
        MockitoAnnotations.initMocks( this );
    }

    @Test
    public void defineJavaPackageWithHttpScheme()
    {
        when( namespace.getName() ).thenReturn( "http://asoape.org/services/" );
        assertThat( StringUtil.toJavaPackage( namespace ), equalTo( "org.asoape.services" ) );
    }

    @Test
    public void defineJavaPackageWithUrnScheme()
        throws Exception
    {
        when( namespace.getName() ).thenReturn( "urn:webservices.asoape.org" );
        assertThat( StringUtil.toJavaPackage( namespace ), equalTo( "org.asoape.webservices" ) );
    }

    @Test
    public void defineJavaLangStringType()
        throws Exception
    {
        when( namespace.getName() ).thenReturn( XMLConstants.W3C_XML_SCHEMA_NS_URI );
        when( simpleType.getNamespace() ).thenReturn( namespace );
        when( simpleType.getName() ).thenReturn( "string" );
        when( simpleType.isEnumeration() ).thenReturn( false );
        assertThat( StringUtil.toFullQualifiedJavaType( simpleType ), is( equalTo( "java.lang.String" ) ) );
    }

    @Test
    public void defineFullyQualifiedEnumType()
        throws Exception
    {
        when( namespace.getName() ).thenReturn( "urn:webservices.asoape.org" );
        when( simpleType.getNamespace() ).thenReturn( namespace );
        when( simpleType.getName() ).thenReturn( "ServiceKind" );
        when( simpleType.isEnumeration() ).thenReturn( true );
        assertThat( StringUtil.toFullQualifiedJavaType( simpleType ),
                    is( equalTo( "org.asoape.webservices.ServiceKind" ) ) );
    }

    @Test
    public void defineStringType()
        throws Exception
    {
        when( namespace.getName() ).thenReturn( XMLConstants.W3C_XML_SCHEMA_NS_URI );
        when( simpleType.getNamespace() ).thenReturn( namespace );
        when( simpleType.getName() ).thenReturn( "string" );
        when( simpleType.isEnumeration() ).thenReturn( false );
        assertThat( StringUtil.toJavaType( simpleType ), is( equalTo( "String" ) ) );
    }

    @Test
    public void defineEnumType()
        throws Exception
    {
        when( namespace.getName() ).thenReturn( "urn:webservices.asoape.org" );
        when( simpleType.getNamespace() ).thenReturn( namespace );
        when( simpleType.getName() ).thenReturn( "ServiceKind" );
        when( simpleType.isEnumeration() ).thenReturn( true );
        assertThat( StringUtil.toJavaType( simpleType ), is( equalTo( "ServiceKind" ) ) );
    }

    @Test
    public void knowsJavaKeyword()
        throws Exception
    {
        assertThat( StringUtil.isJavaKeyword( "class" ), is( true ) );
    }

    @Test
    public void defineFullQualifiedComplexType()
        throws Exception
    {
        when( namespace.getName() ).thenReturn( "urn:webservices.asoape.org" );
        when( complexType.getNamespace() ).thenReturn( namespace );
        when( complexType.getName() ).thenReturn( "ServiceType" );
        assertThat( StringUtil.toFullQualifiedJavaType( complexType ),
                    is( equalTo( "org.asoape.webservices.ServiceType" ) ) );
    }

    @Test
    public void defineComplexType()
        throws Exception
    {
        when( namespace.getName() ).thenReturn( "urn:webservices.asoape.org" );
        when( complexType.getNamespace() ).thenReturn( namespace );
        when( complexType.getName() ).thenReturn( "ServiceType" );
        assertThat( StringUtil.toJavaType( complexType ), is( equalTo( "ServiceType" ) ) );
    }

    @Test
    public void defineMessage()
        throws Exception
    {
        when( namespace.getName() ).thenReturn( "urn:webservices.asoape.org" );
        when( message.getNamespace() ).thenReturn( namespace );
        when( message.getName() ).thenReturn( "ServiceMessage" );
        assertThat( StringUtil.toJavaType( message ), is( equalTo( "ServiceMessage" ) ) );
    }

    @Test
    public void defineService()
        throws Exception
    {
        when( namespace.getName() ).thenReturn( "urn:webservices.asoape.org" );
        when( service.getNamespace() ).thenReturn( namespace );
        when( service.getName() ).thenReturn( "Service" );
        assertThat( StringUtil.toJavaType( service ), is( equalTo( "Service" ) ) );
    }
}
