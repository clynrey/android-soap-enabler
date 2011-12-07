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
package fr.norsys.asoape.codegen.metamodel;

/**
 * Message part of a WebService message. It has either an element or a type.
 * 
 * @see javax.wsdl.Message
 * @see javax.wsdl.Part
 */
public interface WSDLMessagePart
    extends HasName, HasType
{
    void setElement( WSDLElement element );

    WSDLElement getElement();

    /**
     * @return Return the type of this part message, that is the type of the element or the defined type of this part.
     */
    @Override
    public WSDLType getType();
}
