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
package fr.norsys.asoape.codegen.analyzer;

import fr.norsys.asoape.codegen.metamodel.WSDLMessage;
import fr.norsys.asoape.codegen.metamodel.WSDLService;

/**
 * Analyzer's delegate responsible of what to do with found elements.
 * 
 * @since 1.9
 * @see fr.norsys.android.ws.newanalyzer.WSDLAnalyzer
 */
public interface WSDLAnalyzerDelegate
{
    /**
     * Analyzer has just found a service definition.
     * 
     * @param service Service definition found.
     * @see javax.wsdl.Service
     */
    void foundService( WSDLService service );

    /**
     * Analyzer has just found incomming message.
     * 
     * @param input Incomming message.
     * @see javax.wsdl.Input
     */
    void foundInput( WSDLMessage input );

    /**
     * Analyzer has just found outcomming message.
     * 
     * @param output Outcomming message.
     * @see javax.wsdl.Output
     */
    void foundOutput( WSDLMessage output );

    /**
     * Analyzer has just found fault.
     * 
     * @param fault Fault expted to be thrown on service call.
     * @see javax.wsdl.Fault
     */
    void foundFault( WSDLMessage fault );
}
