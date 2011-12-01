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
package fr.norsys.asoape.sample;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

/**
 * Default service implementation.
 */
@WebService( name = "Contact", endpointInterface = "fr.norsys.asoape.sample.ContactServiceType", portName = "ContactServicePort", serviceName = "ContactService", targetNamespace = "http://asoape.norsys.fr/sample/" )
public class DefaultContactServiceType
    implements ContactServiceType
{
    /*
     * (non-Javadoc)
     * @see fr.norsys.asoape.sample.ContactServiceType#findContactWithNameLike(java.lang.String)
     */
    @Override
    public List<ContactType> findContactWithNameLike( String nameLike )
    {
        List<ContactType> contactList = new ArrayList<ContactType>();
        ContactType contact = new ContactType();
        contact.setFirstname( "Sophie" );
        contact.setLastname( "Dupont" );
        contact.getEmail().add( createEmail( "sophie.dupont@perso.eml", ContactKindType.PERSONNAL ) );
        contact.getEmail().add( createEmail( "sdupont@company.eml", ContactKindType.PROFESSIONNAL ) );
        contact.getPhone().add( createPhone( "+33328123456", ContactKindType.PROFESSIONNAL ) );
        contactList.add( contact );
        contact = new ContactType();
        contact.setFirstname( "Lucie" );
        contact.setLastname( "Durant" );
        contact.getEmail().add( createEmail( "lucie.durant@perso.eml", ContactKindType.PERSONNAL ) );
        contact.getPhone().add( createPhone( "+33328123456", ContactKindType.PROFESSIONNAL ) );
        contact.getPhone().add( createPhone( "+33654321098", ContactKindType.OTHERS ) );
        contactList.add( contact );
        return contactList;
    }

    private ContactEmailType createEmail( String address, ContactKindType kind )
    {
        ContactEmailType email;
        email = new ContactEmailType();
        email.setKind( kind );
        email.setEmail( address );
        return email;
    }

    private ContactPhoneType createPhone( String number, ContactKindType kind )
    {
        ContactPhoneType phone;
        phone = new ContactPhoneType();
        phone.setKind( kind );
        phone.setPhone( number );
        return phone;
    }
}
