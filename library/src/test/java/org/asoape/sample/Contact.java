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
import fr.norsys.asoape.xml.binding.annotation.XmlType;
import fr.norsys.asoape.xml.binding.annotation.XmlElement.Cardinality;

@XmlType( propOrder = { "firstname", "lastname", "email", "phone" } )
public class Contact
{
    @XmlElement( name = "firstname", targetType = String.class, cardinality = Cardinality.ONE )
    private String firstname;

    @XmlElement( name = "lastname", targetType = String.class, cardinality = Cardinality.ONE )
    private String lastname;

    @XmlElement( name = "email", targetType = ContactEmail.class, cardinality = Cardinality.MANY )
    private ArrayList<ContactEmail> email = new ArrayList<ContactEmail>();

    @XmlElement( name = "phone", targetType = ContactPhone.class, cardinality = Cardinality.MANY )
    private ArrayList<ContactPhone> phone = new ArrayList<ContactPhone>();

    public String getFirstname()
    {
        return firstname;
    }

    public void setFirstname( String firstname )
    {
        this.firstname = firstname;
    }

    public String getLastname()
    {
        return lastname;
    }

    public void setLastname( String lastname )
    {
        this.lastname = lastname;
    }

    public ArrayList<ContactEmail> getEmail()
    {
        return email;
    }

    public void setEmail( ArrayList<ContactEmail> email )
    {
        this.email = email;
    }

    public ArrayList<ContactPhone> getPhone()
    {
        return phone;
    }

    public void setPhone( ArrayList<ContactPhone> phone )
    {
        this.phone = phone;
    }
}
