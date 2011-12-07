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

import fr.norsys.asoape.codegen.metamodel.WSDLOperation;
import fr.norsys.asoape.codegen.metamodel.WSDLService;
import fr.norsys.asoape.codegen.util.StringUtil;

public class WSDLServiceBean
    extends WSDLObject
    implements WSDLService
{
    private String serviceUrl;
    
    private List<WSDLOperation> operations = new ArrayList<WSDLOperation>();

    @Override
    public void addOperation( WSDLOperation operation )
    {
        operations.add( operation );
    }

    @Override
    public List<WSDLOperation> getOperations()
    {
        return operations;
    }

    @Override
    public String getServiceUrl()
    {
        return this.serviceUrl;
    }

    @Override
    public void setServiceUrl( String serviceUrl )
    {
        this.serviceUrl = serviceUrl;
    }

    @Override
    public String toJavaType()
    {
        return StringUtil.toJavaType( this );
    }
}
