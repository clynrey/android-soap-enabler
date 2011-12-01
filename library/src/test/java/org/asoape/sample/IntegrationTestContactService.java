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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.net.URL;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fr.norsys.asoape.ws.WSCallBack;

public class IntegrationTestContactService
{
    private ContactService service;

    @Before
    public void setUp()
        throws Exception
    {
        service = new ContactServiceSoapImpl( new URL( ContactServiceSoapImpl.DEFAULT_URL ) );
    }

    @Test
    public void syncTest()
        throws Exception
    {
        List<Contact> contacts = service.findContactWithNameLike( "du" );
        assertGoodContactList( contacts );
    }

    @Test
    public void asyncTest()
        throws Exception
    {
        final Object lock = new Object();
        service.findContactWithNameLike( "du", new WSCallBack<List<Contact>>()
        {
            public void onSuccess( List<Contact> result )
            {
                assertGoodContactList( result );
                synchronized ( lock )
                {
                    lock.notify();
                }
            }

            public void onFailure( Throwable throwable )
            {
                fail( "An error occurs during remote call :" + throwable.getMessage() );
            }
        } );
        synchronized ( lock )
        {
            lock.wait();
        }
    }

    private void assertGoodContactList( List<Contact> contacts )
    {
        assertThat( contacts, is( notNullValue() ) );
        for ( Contact contact : contacts )
        {
            assertThat( contact.getFirstname(), is( notNullValue() ) );
            assertThat( contact.getLastname(), is( notNullValue() ) );
            assertThat( contact.getEmail(), is( notNullValue() ) );
            for ( ContactEmail email : contact.getEmail() )
            {
                assertThat( email.getEmail(), is( notNullValue() ) );
                assertThat( email.getKind(), is( notNullValue() ) );
            }
            assertThat( contact.getPhone(), is( notNullValue() ) );
            for ( ContactPhone phone : contact.getPhone() )
            {
                assertThat( phone.getPhone(), is( notNullValue() ) );
                assertThat( phone.getKind(), is( notNullValue() ) );
            }
        }
        System.out.println( "good contact list!" );
    }
}
