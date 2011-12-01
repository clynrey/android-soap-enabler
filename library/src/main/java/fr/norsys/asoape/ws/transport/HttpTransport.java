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
package fr.norsys.asoape.ws.transport;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import fr.norsys.asoape.log.Logger;
import fr.norsys.asoape.xml.binding.BindingContext;
import fr.norsys.asoape.xml.binding.Marshaller;
import fr.norsys.asoape.xml.binding.MarshallingException;
import fr.norsys.asoape.xml.binding.Unmarshaller;
import fr.norsys.asoape.xml.binding.UnmarshallingException;
import fr.norsys.asoape.xml.soap.SoapEnvelope;

/**
 * HTTP Transport layer.
 * 
 * @since 1.0
 */
public class HttpTransport
{
    private String username;

    private String password;

    private BindingContext bindingContext;

	private Logger logger;

	private Integer connectionTimeout;

	private Integer socketTimeout;

    /**
     * Default transport initialization without any authentication.
     */
    public HttpTransport( BindingContext bindingContext )
    {
        this( bindingContext, null, null, null );
    }

    /**
     * Transport initialization with user/passord authentication capability enabled, user name and password must not by
     * null or empty otherwise no authentication will be send.
     * 
     * @param bindingContext Context for Java/XML mapping.
     * @param username The user name to use during network dialog.
     * @param password The user password to use during network dialog.
     * @param logger logger implementation
     */
    public HttpTransport( BindingContext bindingContext, String username, String password, Logger logger )
    {
    	this (bindingContext, username, password, null, null, logger);
    }
    
    /**
     * Transport initialization with user/passord authentication capability enabled, user name and password must not by
     * null or empty otherwise no authentication will be send.
     *
     * @param bindingContext Context for Java/XML mapping.
     * @param username The user name to use during network dialog.
     * @param password The user password to use during network dialog.
     * @param connectionTimeout timeout in milliseconds until a connection is established
     * @param socketTimeout timeout for waiting for data in milliseconds 
     * @param logger logger implementation
     */

    public HttpTransport( BindingContext bindingContext, String username, String password, Integer connectionTimeout, Integer socketTimeout, Logger logger )
    {
        this.bindingContext = bindingContext;
        this.username = username;
        this.password = password;
        this.logger = logger;
        this.connectionTimeout = connectionTimeout;
        this.socketTimeout = socketTimeout;
    }

    /**
     * {@link SoapEnvelope} transport from client to server and return {@link SoapEnvelope} recognition.
     * 
     * @param serviceUrl URL of the service to reach.
     * @param outgoingEnvelope Outgoing request message.
     * @return Return the incomming response message.
     * @throws MarshallingException Whenever a Java To XML mapping error occurs.
     * @throws IOException Whenever a communication problem occurs.
     * @deprecated Use instead {@link #send(URL, SoapEnvelope)}.
     */
    @Deprecated
    public SoapEnvelope send( String serviceUrl, SoapEnvelope outgoingEnvelope )
        throws MarshallingException, IOException
    {
        return send( new URL( serviceUrl ), outgoingEnvelope );
    }

    /**
     * {@link SoapEnvelope} transport from client to server and return {@link SoapEnvelope} recognition.
     * 
     * @param serviceUrl URL of the service to reach.
     * @param outgoingEnvelope Outgoing request message.
     * @return Return the incomming response message.
     * @throws MarshallingException Whenever a Java To XML mapping error occurs.
     * @throws IOException Whenever a communication problem occurs.
     */
    public SoapEnvelope send( URL serviceUrl, SoapEnvelope outgoingEnvelope )
        throws MarshallingException, IOException
    {
        // Preparing request
        HttpPost post = new HttpPost( serviceUrl.toString() );
        StringWriter writer = new StringWriter();
        Marshaller marshaller = bindingContext.createMarshaller();
        marshaller.marshall( outgoingEnvelope, writer );
        String soapMessage = writer.toString();
        debug(soapMessage);
        StringEntity message = new StringEntity( soapMessage , "UTF-8");
        message.setContentType( "text/xml; charset=utf-8" );
        post.setEntity( message );
        
        HttpParams httpParameters = new BasicHttpParams();
        if (connectionTimeout != null) {
	        // Set the timeout in milliseconds until a connection is established.
	        HttpConnectionParams.setConnectionTimeout(httpParameters, connectionTimeout);
        }
        if (socketTimeout != null) {
        	// Set the default socket timeout (SO_TIMEOUT) 
        	// in milliseconds which is the timeout for waiting for data.
        	HttpConnectionParams.setSoTimeout(httpParameters, socketTimeout);
        }

        ResponseHandler<SoapEnvelope> responseHandler = new SoapResponseHandler( bindingContext );
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
        if ( hasAuthentication() )
        {
            AuthScope authScope = new AuthScope( serviceUrl.getHost(), serviceUrl.getPort() );
            Credentials credentials = new UsernamePasswordCredentials( username, password );
            httpClient.getCredentialsProvider().setCredentials( authScope, credentials );
        }
        return httpClient.execute( post, responseHandler );
    }
    
    /**
     * {@link SoapEnvelope} transport from client to server without response (one way).
     * 
     * @param serviceUrl URL of the service to reach.
     * @param outgoingEnvelope Outgoing request message.
     * @throws MarshallingException Whenever a Java To XML mapping error occurs.
     * @throws IOException Whenever a communication problem occurs.
     */
    public void sendAsync( URL serviceUrl, SoapEnvelope outgoingEnvelope )
        throws MarshallingException, IOException
    {
        // Preparing request
        HttpPost post = new HttpPost( serviceUrl.toString() );
        StringWriter writer = new StringWriter();
        Marshaller marshaller = bindingContext.createMarshaller();
        marshaller.marshall( outgoingEnvelope, writer );
        String soapMessage = writer.toString();
        debug(soapMessage);
        StringEntity message = new StringEntity( soapMessage , "UTF-8");
        message.setContentType( "text/xml; charset=utf-8" );
        post.setEntity( message );
        
        HttpParams httpParameters = new BasicHttpParams();
        if (connectionTimeout != null) {
	        // Set the timeout in milliseconds until a connection is established.
	        HttpConnectionParams.setConnectionTimeout(httpParameters, connectionTimeout);
        }
        if (socketTimeout != null) {
        	// Set the default socket timeout (SO_TIMEOUT) 
        	// in milliseconds which is the timeout for waiting for data.
        	HttpConnectionParams.setSoTimeout(httpParameters, socketTimeout);
        }

        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
        if ( hasAuthentication() )
        {
            AuthScope authScope = new AuthScope( serviceUrl.getHost(), serviceUrl.getPort() );
            Credentials credentials = new UsernamePasswordCredentials( username, password );
            httpClient.getCredentialsProvider().setCredentials( authScope, credentials );
        }
        httpClient.execute( post );
    }

    private boolean hasAuthentication()
    {
        return username != null && !"".equals( username ) && password != null && !"".equals( password );
    }

    class SoapResponseHandler
        implements ResponseHandler<SoapEnvelope>
    {
        private BindingContext bindingContext;

        public SoapResponseHandler( BindingContext bindingContext )
        {
            this.bindingContext = bindingContext;
        }

        public SoapEnvelope handleResponse( HttpResponse response )
            throws ClientProtocolException, IOException
        {
            if ( 200 != response.getStatusLine().getStatusCode() )
            {
                throw new IOException( String.format( "%s - %s",
                                                      response.getStatusLine().getStatusCode(),
                                                      response.getStatusLine().getReasonPhrase() ) );
            }
            // Analysing response
            try
            {
                Unmarshaller unmarshaller = bindingContext.createUnmarshaller();
                InputStream in = response.getEntity().getContent();
                if (logger != null && logger.isDebugEnabled()){
                	return (SoapEnvelope) unmarshaller.unmarshall( new InputStreamReader(new ByteArrayInputStream(logResponse(in))));
                }
                return  (SoapEnvelope) unmarshaller.unmarshall( new InputStreamReader(in));
            }
            catch ( UnmarshallingException cause )
            {
                throw new IOException( cause.getMessage() );
            }
        }
    }

	public void debug(String responseStr) {
		if (logger != null && logger.isDebugEnabled()) {
			logger.debug(responseStr);
		}
	}
	
	private byte[] logResponse(InputStream is) throws IOException{
		byte[] data = new byte[0];
		byte[] buffer = new byte[256];
		int length = 0;
		length = is.read(buffer);
			while(length > 0) {
				byte[] temp = new byte[data.length + length];
				System.arraycopy(data, 0, temp, 0, data.length);
				System.arraycopy(buffer, 0, temp, data.length, length);
				data = temp;
				length =  is.read(buffer);
			}
		String responseStr = new String (data, "UTF8");
		debug(responseStr);
		return data;
	}
	
	              
}
