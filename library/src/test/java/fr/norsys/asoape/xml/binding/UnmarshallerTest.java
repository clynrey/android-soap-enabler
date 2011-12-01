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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.List;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;
import org.tempuri.CelsiusToFahrenheitResponse;

import fr.norsys.asoape.xml.soap.SoapEnvelope;

public class UnmarshallerTest
{
    private Unmarshaller unmarshaller;

    @Before
    public void setUp()
        throws Exception
    {
        BindingContext bindingContext = new BindingContext();
        bindingContext.add( SoapEnvelope.class, CelsiusToFahrenheitResponse.class );
        unmarshaller = bindingContext.createUnmarshaller();
    }

    @Test
    public void canUnmarshallEmptySoapEnvelope()
        throws Exception
    {
        String envelope = "<?xml version=\"1.0\"?>"
                          + "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                          + "<soap:Header/>"
                          + "<soap:Body/>"
                          + "</soap:Envelope>";
        StringReader reader = new StringReader( envelope );
        Object object = unmarshaller.unmarshall( reader );
        assertNotNull( object );
        assertThat( object, is( instanceOf( SoapEnvelope.class ) ) );
    }

    @Test
    public void canUnmarshallCelsiusToFahrenheitResponse()
        throws Exception
    {
        InputStreamReader reader = new InputStreamReader( UnmarshallerTest.class.getResourceAsStream( "/CelsiusToFahrenheitResponse.xml" ) );
        Object object = unmarshaller.unmarshall( reader );
        assertThat( object, is( notNullValue() ) );
        assertThat( object, instanceOf( SoapEnvelope.class ) );
        SoapEnvelope envelope = (SoapEnvelope) object;
        assertThat( envelope.getBody().size(), is( equalTo( 1 ) ) );
        object = envelope.getBody().get( 0 );
        assertThat( object, instanceOf( CelsiusToFahrenheitResponse.class ) );
        CelsiusToFahrenheitResponse response = (CelsiusToFahrenheitResponse) object;
        assertThat( response.getCelsiusToFahrenheitResult(), is( equalTo( "212" ) ) );
    }
}
