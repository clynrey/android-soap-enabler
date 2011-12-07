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
import java.util.List;

import fr.norsys.asoape.codegen.metamodel.WSDLComplexType;
import fr.norsys.asoape.codegen.metamodel.WSDLElement;
import fr.norsys.asoape.codegen.util.StringUtil;

public class WSDLComplexTypeBean
    extends WSDLTypeBean
    implements WSDLComplexType
{
    private boolean abstrait;

    private WSDLComplexType extension;

    private List<WSDLElement> members = new ArrayList<WSDLElement>();

    @Override
    public boolean equals( Object obj )
    {
        if ( obj instanceof WSDLComplexType )
        {
            WSDLComplexType other = (WSDLComplexType) obj;
            return getName().equals( other.getName() ) && getNamespace().equals( other.getNamespace() );
        }
        return false;
    }

    @Override
    public List<WSDLElement> getMembers()
    {
        return members;
    }

    @Override
    public List<WSDLElement> getMembersAndInheritance()
    {
        List<WSDLElement> result = new ArrayList<WSDLElement>( members );
        if ( null != extension )
        {
            result.addAll( extension.getMembersAndInheritance() );
        }
        return result;
    }

    @Override
    public String toJavaType()
    {
        return StringUtil.toJavaType( this );
    }

    @Override
    public String toFullQualifiedJavaType()
    {
        return StringUtil.toFullQualifiedJavaType( this );
    }

    @Override
    public WSDLComplexType getExtension()
    {
        return this.extension;
    }

    @Override
    public void setExtension( WSDLComplexType baseComplexType )
    {
        this.extension = baseComplexType;
    }

    @Override
    public void setAbstract( boolean mustBeAbstract )
    {
        abstrait = mustBeAbstract;
    }

    @Override
    public boolean isAbstract()
    {
        return abstrait;
    }
}
