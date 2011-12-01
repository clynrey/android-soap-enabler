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
package fr.norsys.asoape.xml.soap;

import java.util.ArrayList;
import java.util.List;

import fr.norsys.asoape.xml.binding.annotation.XmlElement;
import fr.norsys.asoape.xml.binding.annotation.XmlElement.Cardinality;
import fr.norsys.asoape.xml.binding.annotation.XmlRootElement;
import fr.norsys.asoape.xml.binding.annotation.XmlType;

/**
 * Soap Envelope used to exchange data.
 * 
 * @since 1.0
 */
@XmlRootElement( name = "Envelope", namespace = "http://schemas.xmlsoap.org/soap/envelope/" )
@XmlType( propOrder = { "Header", "Body" } )
@SuppressWarnings( "rawtypes" )
public class SoapEnvelope
{
    @XmlElement( name="Header", targetType = Object.class, cardinality = Cardinality.MANY )
    private List Header = new ArrayList();

    @XmlElement( name="Body", targetType = Object.class, cardinality = Cardinality.MANY )
    private List Body = new ArrayList();

    /**
     * @return the header
     */
    public List getHeader()
    {
        return Header;
    }

    /**
     * @return the body
     */
    public List getBody()
    {
        return Body;
    };
}
