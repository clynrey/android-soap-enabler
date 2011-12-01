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

import static org.custommonkey.xmlunit.XMLAssert.*;
import static org.junit.Assert.*;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;
import org.tempuri.CelsiusToFahrenheit;
import org.xml.sax.InputSource;

import fr.norsys.asoape.xml.soap.SoapEnvelope;

public class MarshallerTest
{
    private Marshaller marshaller;

    @Before
    public void setUp()
        throws Exception
    {
        BindingContext context = new BindingContext();
        marshaller = context.createMarshaller();
    }

    @Test
    public void canMarshallEmptySoapEnvelope()
        throws Exception
    {
        SoapEnvelope envelope = new SoapEnvelope();
        StringWriter writer = new StringWriter();
        marshaller.marshall( envelope, writer );
        String actual = writer.toString();
        String expected = "<?xml version=\"1.0\"?>"
                          + "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                          + "<soap:Header/>"
                          + "<soap:Body/>"
                          + "</soap:Envelope>";
        assertXMLEqual( "Bad empty envelope", expected, actual );
    }

    @Test
    public void canMarshallCeliusToFahrenheitRequest()
        throws Exception
    {
        SoapEnvelope envelope = new SoapEnvelope();
        CelsiusToFahrenheit request = new CelsiusToFahrenheit();
        request.setCelsius( "100" );
        envelope.getBody().add( request );
        StringWriter stringWriter = new StringWriter();
        marshaller.marshall( envelope, stringWriter );
        StringReader stringReader = new StringReader( stringWriter.toString() );
        InputStreamReader isReader = new InputStreamReader( MarshallerTest.class.getResourceAsStream( "/CelsiusToFahrenheit.xml" ) );
        assertXMLEqual( "Bad SOAP envelope", new InputSource(isReader ), new InputSource( stringReader ) );
    }
}
