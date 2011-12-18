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

import static org.tempuri.TempConverter.*;

import java.text.DecimalFormat;

import javax.jws.WebService;

/**
 * Default temperatur converture.
 */
@WebService( name = "TempConvert", portName = "TempConvertSoap", serviceName = "TempConvert", endpointInterface = "org.tempuri.TempConvertSoap" )
public class DefaultTempConvertSoap
    implements TempConvertSoap
{
    private DecimalFormat formatter = new DecimalFormat( "0.##" );

    /*
     * (non-Javadoc)
     * @see org.tempuri.TempConvertSoap#fahrenheitToCelsius(java.lang.String)
     */
    @Override
    public String fahrenheitToCelsius( String fahrenheit )
    {
        Double celsius = convertFromFahrenheitToCelsius( new Double( fahrenheit ) );
        return formatter.format( celsius );
    }

    /*
     * (non-Javadoc)
     * @see org.tempuri.TempConvertSoap#celsiusToFahrenheit(java.lang.String)
     */
    @Override
    public String celsiusToFahrenheit( String celsius )
    {
        Double fahrenheit = convertFromtCelsiusToFahrenheit( new Double( celsius ) );
        return formatter.format( fahrenheit );
    }

}
