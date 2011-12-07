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

import java.util.Collection;
import java.util.Iterator;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Fault;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.xml.namespace.QName;

import fr.norsys.asoape.codegen.metamodel.WSDLMessage;
import fr.norsys.asoape.codegen.metamodel.WSDLMessagePart;
import fr.norsys.asoape.codegen.metamodel.WSDLNamespace;
import fr.norsys.asoape.codegen.metamodel.WSDLOperation;
import fr.norsys.asoape.codegen.metamodel.WSDLService;
import fr.norsys.asoape.codegen.metamodel.impl.WSDLMessageBean;
import fr.norsys.asoape.codegen.metamodel.impl.WSDLMessagePartBean;
import fr.norsys.asoape.codegen.metamodel.impl.WSDLNamespaceBean;
import fr.norsys.asoape.codegen.metamodel.impl.WSDLOperationBean;
import fr.norsys.asoape.codegen.metamodel.impl.WSDLServiceBean;

/**
 * Nouvelle tentative d'analyse de définition WSDL.
 */
public class WSDLAnalyzer
{
    /**
     * Lance l'analyse d'une définition WSDL.
     * 
     * @param definition Définition WSDL à lire.
     */
    public void analyze( Definition definition )
    {
        repository = new WSDLTypesRepository( definition );
        definitionNamespace = new WSDLNamespaceBean();
        definitionNamespace.setName( definition.getTargetNamespace() );
        @SuppressWarnings( "rawtypes" )
        Iterator serviceKeyIterator = definition.getAllServices().keySet().iterator();
        while ( serviceKeyIterator.hasNext() )
        {
            QName serviceName = (QName) serviceKeyIterator.next();
            Service service = definition.getService( serviceName );
            analyzeService( service );
        }
    }

    private void analyzeService( Service service )
    {
        Iterator portsIterator = service.getPorts().values().iterator();
        while (portsIterator.hasNext())
        {
            Port port = (Port) portsIterator.next();
            Object extensibilityElement = port.getExtensibilityElements().iterator().next();
            if (extensibilityElement instanceof SOAPAddress)
            {
                SOAPAddress soapAddress = (SOAPAddress) extensibilityElement;
                String serviceUrl = soapAddress.getLocationURI();
                WSDLService _service = new WSDLServiceBean();
                _service.setNamespace( definitionNamespace );
                _service.setServiceUrl( serviceUrl );
                Binding binding = port.getBinding();
                SOAPBinding soapBinding = (SOAPBinding) binding.getExtensibilityElements().iterator().next();
                soapBindingStyle = soapBinding.getStyle();
                PortType portType = binding.getPortType();
                _service.setName( portType.getQName().getLocalPart() );
                @SuppressWarnings( "rawtypes" )
                Iterator operationIterator = portType.getOperations().iterator();
                while ( operationIterator.hasNext() )
                {
                    Operation operation = (Operation) operationIterator.next();
                    _service.addOperation( analyzeOperation( operation ) );
                }
                delegate.foundService( _service );
            }
        }
    }

    private WSDLOperation analyzeOperation( Operation operation )
    {
        WSDLOperation _operation = new WSDLOperationBean();
        _operation.setName( operation.getName() );
        WSDLMessage input = analyzeInput( operation.getInput() );
        if ( "rpc".equals( soapBindingStyle ) )
        {
            input.setName( _operation.getName() );
        }
        _operation.setInput( input );
        delegate.foundInput( input );
        if ( operation.getOutput() != null ) // One Way services does not have output.
        {
            _operation.setOutput( analyzeOutput( operation.getOutput() ) );
        }
        @SuppressWarnings( "rawtypes" )
        Iterator faultIterator = (Iterator) operation.getFaults().values().iterator();
        while ( faultIterator.hasNext() )
        {
            Fault fault = (Fault) faultIterator.next();
            _operation.addFault( analyzeFault( fault ) );
        }
        return _operation;
    }

    private WSDLMessage analyzeFault( Fault fault )
    {
        Message message = fault.getMessage();
        WSDLMessage _fault = new WSDLMessageBean();
        _fault.setName( message.getQName().getLocalPart() );
        _fault.setNamespace( definitionNamespace );
        analyzeMessageParts( _fault, message.getOrderedParts( null ) );
        delegate.foundFault( _fault );
        return _fault;
    }

    private WSDLMessage analyzeInput( Input input )
    {
        Message message = input.getMessage();
        WSDLMessage _inputMessage = new WSDLMessageBean();
        _inputMessage.setName( message.getQName().getLocalPart() );
        _inputMessage.setNamespace( definitionNamespace );
        analyzeMessageParts( _inputMessage, message.getOrderedParts( null ) );
        return _inputMessage;
    }

    private WSDLMessage analyzeOutput( Output output )
    {
        Message message = output.getMessage();
        WSDLMessage _outputMessage = new WSDLMessageBean();
        _outputMessage.setName( message.getQName().getLocalPart() );
        _outputMessage.setNamespace( definitionNamespace );
        analyzeMessageParts( _outputMessage, message.getOrderedParts( null ) );
        delegate.foundOutput( _outputMessage );
        return _outputMessage;
    }

    private void analyzeMessageParts( WSDLMessage message, @SuppressWarnings( "rawtypes" ) Collection parts )
    {
        @SuppressWarnings( "rawtypes" )
        Iterator partIterator = parts.iterator();
        while ( partIterator.hasNext() )
        {
            Part part = (Part) partIterator.next();
            String partName = part.getName();
            WSDLMessagePart _part = new WSDLMessagePartBean();
            _part.setName( partName );
            if ( part.getElementName() != null )
            {
                _part.setElement( repository.findElementByQname( part.getElementName() ) );
            }
            else if ( part.getTypeName() != null )
            {
                _part.setType( repository.findTypeByQname( part.getTypeName() ) );
            }
            message.addPart( _part );
        }
    }

    /**
     * Positionne le délégué assurant le traitement des éléments trouvés.
     * 
     * @param delegate Délégué recevant les éléments trouvé durant l'analyse pour traitement.
     */
    public void setDelegate( WSDLAnalyzerDelegate delegate )
    {
        this.delegate = delegate;
    }

    /* Délégué traitant les éléments trouvé durant l'analyse. */
    private WSDLAnalyzerDelegate delegate;

    /* Namespace globale de la définition. */
    private WSDLNamespace definitionNamespace;

    /* Dépôt permettant l'accès aux types contenu dans la définition. */
    private WSDLTypesRepository repository;

    /* Style d'appel distant. */
    private String soapBindingStyle;
}
