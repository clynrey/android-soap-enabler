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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.wsdl.Definition;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.norsys.asoape.codegen.metamodel.WSDLMessage;
import fr.norsys.asoape.codegen.metamodel.WSDLMessagePart;
import fr.norsys.asoape.codegen.metamodel.WSDLOperation;
import fr.norsys.asoape.codegen.metamodel.WSDLService;

public class ContactWSDLAnalyzerTestCase
{
    private WSDLAnalyzer analyzer;

    @Mock
    private WSDLAnalyzerDelegate delegate;

    @Before
    public void setUp()
        throws Exception
    {
        MockitoAnnotations.initMocks( this );
        analyzer = new WSDLAnalyzer();
    }

    @Test
    public void canAnalyzeContactWsdl()
        throws Exception
    {
        WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
        Definition definition = reader.readWSDL( ContactWSDLAnalyzerTestCase.class.getResource( "/contact.wsdl" )
                                                                                  .toString() );
        analyzer.setDelegate( delegate );
        analyzer.analyze( definition );
        ArgumentCaptor<WSDLService> serviceCaptor = ArgumentCaptor.forClass( WSDLService.class );
        verify( delegate ).foundService( serviceCaptor.capture() );
        WSDLService service = serviceCaptor.getValue();
        assertThat( service.getName(), is( equalTo( "ContactServiceType" ) ) );
        assertThat( service.getOperations().size(), is( equalTo( 1 ) ) );
        WSDLOperation operation = service.getOperations().get( 0 );
        assertThat( operation.getName(), is( equalTo( "findContactWithNameLike" ) ) );
        assertThat( operation.getInput(), is( notNullValue() ) );
        WSDLMessage input = operation.getInput();
        assertThat( input.getName(), is( equalTo( "findContactWithNameLikeRequest" ) ) );
        WSDLMessagePart inputPart = input.getPart( "parameters" );
        assertThat( inputPart, is( notNullValue() ) );
        assertThat( inputPart.getElement().getName(), is( equalTo( "findContactWithNameLike" ) ) );
        WSDLMessage output = operation.getOutput();
        assertThat( output.getName(), is( equalTo( "findContactWithNameLikeResponse" ) ) );
        WSDLMessagePart outputPart = output.getPart( "parameters" );
        assertThat( outputPart, is( notNullValue() ) );
        assertThat( outputPart.getElement().getName(), is( equalTo( "findContactWithNameLikeResponse" ) ) );
    }
}
