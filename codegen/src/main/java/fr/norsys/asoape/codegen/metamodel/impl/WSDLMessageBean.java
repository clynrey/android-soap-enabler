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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fr.norsys.asoape.codegen.metamodel.WSDLMessage;
import fr.norsys.asoape.codegen.metamodel.WSDLMessagePart;
import fr.norsys.asoape.codegen.util.StringUtil;

public class WSDLMessageBean
    extends WSDLObject
    implements WSDLMessage
{
    private Map<String, WSDLMessagePart> partsByName = new HashMap<String, WSDLMessagePart>();

    private List<WSDLMessagePart> parts = new ArrayList<WSDLMessagePart>();

    @Override
    public String toJavaType()
    {
        return StringUtil.toJavaType( this );
    }

    @Override
    public String toString()
    {
        StringBuilder buffer = new StringBuilder();
        for ( Iterator<WSDLMessagePart> messagePartIterator = getParts().iterator(); messagePartIterator.hasNext(); )
        {
            WSDLMessagePart messagePart = messagePartIterator.next();
            if ( messagePart.getElement() != null )
            {
                buffer.append( messagePart.getElement() );
            }
            else
            {
                buffer.append( String.format( "%s %s", messagePart.getName(), messagePart.getType() ) );
            }
            if ( messagePartIterator.hasNext() )
            {
                buffer.append( ", " );
            }
        }
        return buffer.toString();
    }

    @Override
    public void addPart( WSDLMessagePart messagePart )
    {
        partsByName.put( messagePart.getName(), messagePart );
        parts.add( messagePart );
    }

    @Override
    public WSDLMessagePart getPart( String partName )
    {
        return partsByName.get( partName );
    }

    @Override
    public List<WSDLMessagePart> getParts()
    {
        return parts;
    }

}
