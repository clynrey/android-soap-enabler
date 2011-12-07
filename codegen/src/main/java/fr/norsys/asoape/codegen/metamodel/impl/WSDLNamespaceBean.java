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
import java.util.List;
import java.util.Map;

import fr.norsys.asoape.codegen.metamodel.HasNamespace;
import fr.norsys.asoape.codegen.metamodel.WSDLNamespace;
import fr.norsys.asoape.codegen.metamodel.WSDLType;
import fr.norsys.asoape.codegen.util.StringUtil;

public class WSDLNamespaceBean
    extends WSDLObject
    implements WSDLNamespace
{
    private static Map<String, WSDLNamespaceBean> namespacesByUri = new HashMap<String, WSDLNamespaceBean>();

    private Map<String, WSDLTypeBean> typesByName = new HashMap<String, WSDLTypeBean>();

    private List<HasNamespace> content = new ArrayList<HasNamespace>();

    private Map<String, WSDLElementBean> elementsByName = new HashMap<String, WSDLElementBean>();

    private Map<String, WSDLMessageBean> messagesByName = new HashMap<String, WSDLMessageBean>();

    private Map<String, WSDLServiceBean> servicesByName = new HashMap<String, WSDLServiceBean>();

    private static final Map<String, WSDLNamespaceBean> namespacesByPrefix = new HashMap<String, WSDLNamespaceBean>();

    @Override
    public boolean equals( Object obj )
    {
        if (obj instanceof WSDLNamespace)
        {
            WSDLNamespace other = (WSDLNamespace) obj;
            return getName().equals( other.getName() );
        }
        return false;
    }

    @Override
    public String toString()
    {
        return getName();
    }

    @Override
    public void setName( String name )
    {
        super.setName( name );
        namespacesByUri.put( name, this );
    }

    @Override
    public void add( HasNamespace content )
    {
    	content.setNamespace( this );
        if ( !this.content.contains( content ) )
        {
            this.content.add( content );
            if ( content instanceof WSDLTypeBean )
            {
                WSDLTypeBean type = (WSDLTypeBean) content;
                typesByName.put( type.getName(), type );
            }
            if ( content instanceof WSDLElementBean )
            {
                WSDLElementBean element = (WSDLElementBean) content;
                elementsByName.put( element.getName(), element );
            }
            if ( content instanceof WSDLMessageBean )
            {
                WSDLMessageBean message = (WSDLMessageBean) content;
                messagesByName.put( message.getName(), message );
            }
            if ( content instanceof WSDLServiceBean )
            {
                WSDLServiceBean service = (WSDLServiceBean) content;
                servicesByName.put( service.getName(), service );
            }
        }
    }

    @Override
    public List<HasNamespace> getContent()
    {
        return content;
    }

    @Override
    public String toJavaPackage()
    {
        return StringUtil.toJavaPackage(this);
    }

    public static void addPrefix( String prefix, WSDLNamespaceBean namespace )
    {
        namespacesByPrefix.put( prefix, namespace );
    }

    public static WSDLNamespaceBean findByPrefix( String prefix )
    {
        return namespacesByPrefix.get( prefix );
    }

    public WSDLType findTypeByName( String typeName )
    {
        return typesByName.get( typeName );
    }

    public static WSDLNamespaceBean findByUri( String uri )
    {
        return namespacesByUri.get( uri );
    }

    public WSDLElementBean findElementByName( String elementName )
    {
        return elementsByName.get( elementName );
    }

    public WSDLMessageBean findMessageByName( String messageName )
    {
        return messagesByName.get( messageName );
    }

    public WSDLServiceBean findServiceByName( String serviceName )
    {
        return servicesByName.get( serviceName );
    }

}
