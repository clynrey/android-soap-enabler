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
package fr.norsys.asoape.codegen.metamodel.impl;

import fr.norsys.asoape.codegen.metamodel.WSDLElement;
import fr.norsys.asoape.codegen.metamodel.WSDLMessagePart;
import fr.norsys.asoape.codegen.metamodel.WSDLType;

public class WSDLMessagePartBean
    extends WSDLObject
    implements WSDLMessagePart
{
    @Override
    public void setType( WSDLType type )
    {
        this.type = type;
    }

    @Override
    public void setElement( WSDLElement element )
    {
        this.element = element;
    }

    @Override
    public WSDLElement getElement()
    {
        return this.element;
    }

    @Override
    public WSDLType getType()
    {
        if ( this.element != null )
        {
            return this.element.getType();
        }
        return this.type;
    }

    /* Message part element representation */
    private WSDLElement element;

    /* Message part type. */
    private WSDLType type;
}
