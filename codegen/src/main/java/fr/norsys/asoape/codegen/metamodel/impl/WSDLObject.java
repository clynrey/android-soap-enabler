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

import fr.norsys.asoape.codegen.metamodel.HasName;
import fr.norsys.asoape.codegen.metamodel.HasNamespace;
import fr.norsys.asoape.codegen.metamodel.WSDLNamespace;

public abstract class WSDLObject
    implements HasName, HasNamespace
{

    private String name;

    private WSDLNamespace namespace;

    @Override
    public String toString()
    {
        return String.format( "{%s}%s", getNamespace(), getName() );
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setName( String name )
    {
        this.name = name;
    }

    @Override
    public WSDLNamespace getNamespace()
    {
        return namespace;
    }

    @Override
    public void setNamespace( WSDLNamespace namespace )
    {
        if ( namespace != this.namespace )
        {
            this.namespace = namespace;
            namespace.add( this );
        }
    }
}