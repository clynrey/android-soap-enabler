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
package org.asoape.sample;

import java.util.ArrayList;

import fr.norsys.asoape.xml.binding.annotation.XmlElement;
import fr.norsys.asoape.xml.binding.annotation.XmlRootElement;
import fr.norsys.asoape.xml.binding.annotation.XmlType;
import fr.norsys.asoape.xml.binding.annotation.XmlElement.Cardinality;

@XmlRootElement( name = "findContactWithNameLikeResponse", namespace = "http://asoape.norsys.fr/sample/" )
@XmlType( propOrder = { "contact" } )
public class FindContactWithNameLikeResponse
{
    @XmlElement( name = "contact", targetType = Contact.class, cardinality = Cardinality.MANY )
    private ArrayList<Contact> contact = new ArrayList<Contact>();

    public ArrayList<Contact> getContact()
    {
        return contact;
    }

    public void setContact( ArrayList<Contact> contact )
    {
        this.contact = contact;
    }
}
