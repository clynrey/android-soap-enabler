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
package org.tempuri;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import fr.norsys.asoape.ws.WSCallBack;
import fr.norsys.asoape.ws.transport.HttpTransport;
import fr.norsys.asoape.xml.binding.BindingContext;
import fr.norsys.asoape.xml.soap.SoapEnvelope;

public class TempConvertSoapImpl
    implements TempConvertSoap
{
    public static final String DEFAULT_SERVICE_URL = "http://localhost:8080/test-services/TempConvert";

    public static final String PUBLIC_SERVICE_URL = "http://www.w3schools.com/webservices/tempconvert.asmx";

    private URL serviceUrl;

    public TempConvertSoapImpl( String url )
        throws MalformedURLException
    {
        this.serviceUrl = new URL( url );
    }

    @SuppressWarnings( "unchecked" )
    public String CelsiusToFahrenheit( String celsius )
        throws IOException
    {
        CelsiusToFahrenheit request = new CelsiusToFahrenheit();
        request.setCelsius( celsius );
        SoapEnvelope envelope = new SoapEnvelope();
        envelope.getBody().add( request );
        BindingContext bindingContext = new BindingContext();
        bindingContext.add( SoapEnvelope.class, CelsiusToFahrenheitResponse.class );
        HttpTransport transport = new HttpTransport( bindingContext  );
        envelope = transport.send( serviceUrl, envelope );
        CelsiusToFahrenheitResponse response = (CelsiusToFahrenheitResponse) envelope.getBody().get( 0 );
        return response.getCelsiusToFahrenheitResult();
    }

    public String FahrenheitToCelsius( String fahrenheit )
        throws IOException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void CelsiusToFahrenheit( String celsius, WSCallBack<String> callback )
    {
        // TODO Auto-generated method stub

    }

    public void FahrenheitToCelsius( String fahrenheit, WSCallBack<String> callback )
    {
        // TODO Auto-generated method stub

    }
}
