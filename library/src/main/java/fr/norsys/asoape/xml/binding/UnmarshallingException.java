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
package fr.norsys.asoape.xml.binding;

/**
 * Exception thrown when an umarshalling error occurs during XML to Java mapping.
 * 
 * @since 1.0
 */
public class UnmarshallingException
    extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public UnmarshallingException()
    {
    }

    public UnmarshallingException( String message )
    {
        super( message );
    }

    public UnmarshallingException( Throwable cause )
    {
        super( cause );
    }

    public UnmarshallingException( String message, Throwable cause )
    {
        super( message, cause );
    }
}
