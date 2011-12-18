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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class TempConverterTest
{
    @Test
    public void canConvertFromCelsiusToFahrenheit()
        throws Exception
    {
        assertThat( convertFromtCelsiusToFahrenheit( 100d ), is( equalTo( 212d ) ) );
    }

    @Test
    public void canConvertFromFahrenheitToCelsius()
        throws Exception
    {
        assertThat( convertFromFahrenheitToCelsius( 212d ), is( equalTo( 100d ) ) );
    }
}
