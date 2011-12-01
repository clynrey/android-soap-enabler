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
import java.net.URL;
import java.util.List;

import fr.norsys.asoape.log.Logger;
import fr.norsys.asoape.ws.WSCallBack;
import fr.norsys.asoape.ws.transport.HttpTransport;
import fr.norsys.asoape.xml.binding.BindingContext;
import fr.norsys.asoape.xml.soap.SoapEnvelope;

/**
 * Default {@link ContactService} implementation.
 */
public class ContactServiceSoapImpl
    implements ContactService
{
    public static final String SOAPUI_URL = "http://localhost:8088/mockContactService";

    public static final String DEFAULT_URL = "http://localhost:8080/test-services/ContactService";

    public static Logger logger;

    private URL serviceUrl;

    public ContactServiceSoapImpl( URL url )
    {
        serviceUrl = url;
    }

    /*
     * (non-Javadoc)
     * @see org.asoape.sample.ContactService#findContactWithNameLike(java.lang.String)
     */
    @SuppressWarnings( "unchecked" )
    public List<Contact> findContactWithNameLike( String name )
        throws IOException
    {
        FindContactWithNameLike request = new FindContactWithNameLike();
        request.setNameLike( name );
        SoapEnvelope envelope = new SoapEnvelope();
        envelope.getBody().add( request );
        BindingContext bindingContext = new BindingContext();
        bindingContext.add( SoapEnvelope.class, FindContactWithNameLikeResponse.class );
        HttpTransport transport = new HttpTransport( bindingContext );
        envelope = transport.send( serviceUrl, envelope );
        FindContactWithNameLikeResponse response = (FindContactWithNameLikeResponse) envelope.getBody().get( 0 );
        List<Contact> result = response.getContact();
        return result;
    }

    /*
     * (non-Javadoc)
     * @see org.asoape.sample.ContactService#findContactWithNameLike(java.lang.String, fr.norsys.asoape.ws.WSCallBack)
     */
    public void findContactWithNameLike( final String name, final WSCallBack<List<Contact>> callback )
    {
        new Thread( new Runnable()
        {
            public void run()
            {
                try
                {
                    List<Contact> result = findContactWithNameLike( name );
                    try
                    {
                        callback.onSuccess( result );
                    }
                    catch ( Throwable cause )
                    {
                        if (logger != null && logger.isErrorEnabled())
                        {
                            logger.error( "Error handling result", cause );
                        }
                    }
                }
                catch ( Throwable cause )
                {
                    callback.onFailure( cause );
                }
            }
        } ).start();
    }

}
