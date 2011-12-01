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

import java.io.IOException;
import java.net.URI;

import javax.xml.namespace.QName;

import fr.norsys.asoape.xml.binding.annotation.XmlElement;
import fr.norsys.asoape.xml.binding.annotation.XmlElement.Cardinality;
import fr.norsys.asoape.xml.binding.annotation.XmlRootElement;
import fr.norsys.asoape.xml.binding.annotation.XmlType;

/**
 * Class representing a SOAP Fault return after a remote service call.
 * 
 * @todo Missing detail content
 * @since 1.0
 * @see <a href="http://www.w3.org/2001/12/soap-envelope">The SOAP Envelope schema definition</a>
 */
@XmlRootElement( name = "Fault", namespace = "http://schemas.xmlsoap.org/soap/envelope/" )
@XmlType( propOrder = { "faultcode", "faultstring", "faultactor" } )
public class SoapFault
    extends IOException
{
    private static final long serialVersionUID = 1L;

    @XmlElement( name = "faultcode", targetType = QName.class, cardinality = Cardinality.ONE, nullable = true )
    private QName faultcode;

    @XmlElement( name = "faultstring", targetType = String.class, cardinality = Cardinality.ONE, nullable = true )
    private String faultstring;

    @XmlElement( name = "faultactor", targetType = URI.class, cardinality = Cardinality.ONE, nullable = true )
    private URI faultactor;

    public SoapFault()
    {
    }

    public QName getFaultcode()
    {
        return this.faultcode;
    }

    public void setFaultcode( QName faultcode )
    {
        this.faultcode = faultcode;
    }

    public String getFaultstring()
    {
        return this.faultstring;
    }

    public void setFaultstring( String faultstring )
    {
        this.faultstring = faultstring;
    }

    public URI getFaultactor()
    {
        return this.faultactor;
    }

    public void setFaultactor( URI faultactor )
    {
        this.faultactor = faultactor;
    }
}
