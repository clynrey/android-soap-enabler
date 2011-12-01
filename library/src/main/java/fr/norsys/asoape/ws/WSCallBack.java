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
package fr.norsys.asoape.ws;

/**
 * Asynchronous webservice callback specification.
 * 
 * @param <T> Return type of webservice synchronous call.
 * @since 1.0
 */
public interface WSCallBack<T>
{
    /**
     * WebService return handling.
     * 
     * @param result WebService return.
     */
    void onSuccess( T result );

    /**
     * WebService error handling.
     * 
     * @param throwable WebService call error.
     */
    void onFailure( Throwable throwable );
}
