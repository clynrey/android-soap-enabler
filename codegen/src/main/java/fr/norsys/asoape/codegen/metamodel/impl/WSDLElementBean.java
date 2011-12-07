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
import fr.norsys.asoape.codegen.metamodel.WSDLType;

public class WSDLElementBean
    extends WSDLObject
    implements WSDLElement
{
    private WSDLType type;

    private String maxOccurs = WSDLElement.ONE;

    private String minOccurs = WSDLElement.ZERO;

    @Override
    public boolean equals( Object obj )
    {
        if ( obj instanceof WSDLElement )
        {
            WSDLElement other = (WSDLElement) obj;
            return getName().equals( other.getName() )
                   && getNamespace().equals( other.getNamespace() )
                   && getMinOccurs().equals( other.getMinOccurs() )
                   && getMaxOccurs().equals( other.getMaxOccurs() )
                   && getType().equals( other.getType() );
        }
        return false;
    }

    @Override
    public String toString()
    {
        return String.format( "%s:[%s..%s]%s", super.toString(), getMinOccurs(), getMaxOccurs(), getType() );
    }

    @Override
    public WSDLType getType()
    {
        return type;
    }

    @Override
    public void setType( WSDLType type )
    {
        this.type = type;
    }

    @Override
    public String getMaxOccurs()
    {
        return maxOccurs;
    }

    @Override
    public String getMinOccurs()
    {
        return minOccurs;
    }

    @Override
    public void setMaxOccurs( String maxOccurs )
    {
        this.maxOccurs = maxOccurs;
    }

    @Override
    public void setMinOccurs( String minOccurs )
    {
        this.minOccurs = minOccurs;
    }

}
