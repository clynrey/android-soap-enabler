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

import fr.norsys.asoape.codegen.metamodel.WSDLMessage;
import fr.norsys.asoape.codegen.metamodel.WSDLOperation;

public class WSDLOperationBean
    extends WSDLObject
    implements WSDLOperation
{
    private WSDLMessage input;

    private WSDLMessage output;

    private List<WSDLMessage> faults;

    @Override
    public String toString()
    {
        return String.format( "%s( %s ) : %s", getName(), input, output );
    }

    @Override
    public WSDLMessage getInput()
    {
        return input;
    }

    @Override
    public WSDLMessage getOutput()
    {
        return output;
    }

    @Override
    public List<WSDLMessage> getFaults()
    {
        return faults;
    }

    @Override
    public void setInput( WSDLMessage inputMessage )
    {
        this.input = inputMessage;
    }

    @Override
    public void setOutput( WSDLMessage outputMessage )
    {
        this.output = outputMessage;
    }

    @Override
    public boolean hasFaults()
    {
        if (faults != null)
        {
            return !faults.isEmpty();
        }
        return false;
    }

    @Override
    public void addFault( WSDLMessage fault )
    {
        if (faults == null)
        {
            faults = new ArrayList<WSDLMessage>();
        }
        faults.add( fault );
    }
}
