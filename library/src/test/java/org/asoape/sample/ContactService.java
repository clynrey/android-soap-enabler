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

import java.io.IOException;
import java.util.List;

import fr.norsys.asoape.ws.WSCallBack;

/**
 * The ContractWebService Java interface.
 */
public interface ContactService
{
    /**
     * Synchronous remote call.
     * 
     * @param name Name that de found contacts should contain.
     * @return Return all contacts matching the given name.
     * @throws IOException An error occurs during the communication process.
     */
    List<Contact> findContactWithNameLike( String name )
        throws IOException;

    /**
     * Asynchronous remote call.
     * 
     * @param name Name that de found contacts should contain.
     * @param callback receiving all contacts matching the given name.
     */
    void findContactWithNameLike( String name, WSCallBack<List<Contact>> callback );
}
