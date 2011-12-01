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
package fr.norsys.asoape.ws.transport;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tempuri.CelsiusToFahrenheit;
import org.tempuri.CelsiusToFahrenheitResponse;

import fr.norsys.asoape.log.Logger;
import fr.norsys.asoape.xml.binding.BindingContext;
import fr.norsys.asoape.xml.soap.SoapEnvelope;

public class HttpTransportTest
    implements Logger
{
    private static final String URL = "http://www.w3schools.com/webservices/tempconvert.asmx";

    private HttpTransport transport;

    @Before
    public void setUp()
        throws Exception
    {
        BindingContext bindingContext = new BindingContext();
        bindingContext.add( SoapEnvelope.class, CelsiusToFahrenheitResponse.class );
        transport = new HttpTransport( bindingContext, null, null, this );
    }

    @After
    public void tearDown()
        throws Exception
    {
        transport = null;
    }

    @Test
    public void canConvertTemp()
        throws Exception
    {
        SoapEnvelope envelope = new SoapEnvelope();
        CelsiusToFahrenheit request = new CelsiusToFahrenheit();
        request.setCelsius( "100" );
        envelope.getBody().add( request );
        envelope = transport.send( new URL( URL ), envelope );
        assertThat( envelope.getBody().size(), is( equalTo( 1 ) ) );
        Object object = envelope.getBody().get( 0 );
        assertThat( object, instanceOf( CelsiusToFahrenheitResponse.class ) );
        CelsiusToFahrenheitResponse response = (CelsiusToFahrenheitResponse) object;
        assertThat( response.getCelsiusToFahrenheitResult(), is( equalTo( "212" ) ) );
    }

    public boolean isDebugEnabled()
    {
        return true;
    }

    public boolean isErrorEnabled()
    {
        return true;
    }

    public void debug( String message )
    {
        System.out.println( message );
    }

    public void debug( String message, Throwable throwable )
    {
        debug( message );
        throwable.printStackTrace( System.out );
    }

    public void error( String message )
    {
        System.err.println( message );
    }

    public void error( String message, Throwable throwable )
    {
        error( message );
        throwable.printStackTrace( System.err );
    }
}
