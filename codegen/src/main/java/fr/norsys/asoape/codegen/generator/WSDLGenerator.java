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
package fr.norsys.asoape.codegen.generator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.wsdl.Definition;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.norsys.asoape.codegen.analyzer.WSDLAnalyzer;
import fr.norsys.asoape.codegen.analyzer.WSDLAnalyzerDelegate;
import fr.norsys.asoape.codegen.metamodel.HasNamespace;
import fr.norsys.asoape.codegen.metamodel.WSDLComplexType;
import fr.norsys.asoape.codegen.metamodel.WSDLElement;
import fr.norsys.asoape.codegen.metamodel.WSDLMessage;
import fr.norsys.asoape.codegen.metamodel.WSDLMessagePart;
import fr.norsys.asoape.codegen.metamodel.WSDLService;
import fr.norsys.asoape.codegen.metamodel.WSDLSimpleType;
import fr.norsys.asoape.codegen.metamodel.WSDLType;
import fr.norsys.asoape.codegen.util.StringUtil;

public class WSDLGenerator
    implements WSDLAnalyzerDelegate
{
    private static final Logger LOGGER = LoggerFactory.getLogger( WSDLGenerator.class );

    static
    {
        Velocity.setProperty( "resource.loader", "class" );
        Velocity.setProperty( "class.resource.loader.class",
                              "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
        Velocity.setProperty( Velocity.COUNTER_INITIAL_VALUE, "0" );
        try
        {
            Velocity.init();
        }
        catch ( Exception cause )
        {
            LOGGER.error( "Error during Apache Velocity startup ...", cause );
        }
    }

    public WSDLGenerator()
    {
        this.analyzer = new WSDLAnalyzer();
        this.analyzer.setDelegate( this );
    }

    public void setOutputDirectory( File outputDirectory )
    {
        this.outputDirectory = outputDirectory;
    }

    public void setBeanSerializable( Boolean beanSerializable )
    {
        this.beanSerializable = beanSerializable;
    }

    public void generateFrom( Definition definition )
        throws WSDLGenerationException
    {
        this.startGenerationDate = new Date().getTime();
        this.generatedType.clear();
        this.analyzer.analyze( definition );
    }

    @Override
    public void foundService( WSDLService service )
    {
        String serviceName = service.getName();
        String packageName = service.getNamespace().toJavaPackage();
        File packageDirectory = new File( this.outputDirectory, packageDirectoryPathForPackage( packageName ) );
        // Generate service interface
        Context context = new VelocityContext();
        context.put( "type", service );
        context.put( "string", StringUtil.getInstance() );
        generateArtifact( "service-interface.java.vm", packageDirectory, serviceName + ".java", context );
        // Generate service implementation
        context = new VelocityContext();
        context.put( "type", service );
        context.put( "string", StringUtil.getInstance() );
        generateArtifact( "service-implementation.java.vm", packageDirectory, serviceName + "Impl.java", context );
    }

    @Override
    public void foundInput( WSDLMessage input )
        throws RuntimeException
    {
        WSDLMessagePart parameters = input.getPart( "parameters" );
        if ( parameters != null )
        {
            generateBeanForElement( parameters.getElement() );
        }
        else
        {
            generateBeanForMessage( input );
        }
    }

    @Override
    public void foundOutput( WSDLMessage output )
        throws RuntimeException
    {
        WSDLMessagePart parameters = output.getPart( "parameters" );
        if ( parameters != null )
        {
            generateBeanForElement( parameters.getElement() );
        }
        else
        {
            generateBeanForMessage( output );
        }
    }

    private void generateBeanForElement( WSDLElement element )
    {
        WSDLType type = element.getType();
        if (type instanceof WSDLComplexType) {
            WSDLComplexType complexType = (WSDLComplexType) type;
            File packageDirectory = packageDirectoryFor( type );
            Context context = new VelocityContext();
            context.put( "element", element );
            context.put( "type", complexType );
            context.put( "string", StringUtil.getInstance() );
            context.put( "isBeanSerializable", this.beanSerializable );
            String beanName = type.toJavaType();
            generateArtifact( "bean.java.vm", packageDirectory, beanName + ".java", context );
            // Even if the extensions are never used by the services, we must generate them
            if ( complexType.getExtension() != null )
            {
                generateBeanForType( complexType.getExtension() );
            }
            for ( WSDLElement member : complexType.getMembersAndInheritance() )
            {
                generateBeanForType( member.getType() );
            }
        }
    }

    @Override
    public void foundFault( WSDLMessage fault )
    {
        generateExceptionForMessage( fault );
    }

    private void generateExceptionForMessage( WSDLMessage fault )
    {
        File packageDirectory = packageDirectoryFor( fault );
        String exceptionName = fault.toJavaType();
        String exceptionFileName = exceptionName + ".java";
        Context context = new VelocityContext();
        context.put( "exception", fault );
        context.put( "string", StringUtil.getInstance() );
        generateArtifact( "exception.java.vm", packageDirectory, exceptionFileName, context );
        for ( WSDLMessagePart part : fault.getParts() )
        {
            generateBeanForType( part.getType() );
        }
    }

    private void generateBeanForMessage( WSDLMessage message )
    {
        File packageDirectory = packageDirectoryFor( message );
        String beanName = message.toJavaType();
        String beanFileName = beanName + ".java";
        Context context = new VelocityContext();
        context.put( "message", message );
        context.put( "string", StringUtil.getInstance() );
        generateArtifact( "messageBean.java.vm", packageDirectory, beanFileName, context );
        for ( WSDLMessagePart part : message.getParts() )
        {
            generateBeanForType( part.getType() );
        }
    }

    private void generateBeanForType( WSDLType type )
        throws RuntimeException
    {
        if ( !this.generatedType.contains( type ) )
        {
            this.generatedType.add( type );
            if ( type instanceof WSDLComplexType )
            {
                generateBeanForComplexType( (WSDLComplexType) type );
            }
            else
            {
                generateBeanForSimpleType( (WSDLSimpleType) type );
            }
        }
    }

    private void createDirectoryIfNecessary( File directory )
        throws IOException
    {
        File absolutePath = directory.getAbsoluteFile();
        if ( !absolutePath.exists() )
        {
            if ( !absolutePath.mkdirs() )
            {
                throw new IOException( "Can't create directory " + absolutePath );
            }
        }
        else if ( !absolutePath.isDirectory() )
        {
            throw new IOException( absolutePath.toString() + " is not a directory!" );
        }
    }

    private String packageDirectoryPathForPackage( String packageName )
    {
        return packageName.replaceAll( "\\.", File.separator );
    }

    private File packageDirectoryFor( HasNamespace hasNamespace )
    {
        File packageDirectory = new File( outputDirectory,
                                          packageDirectoryPathForPackage( hasNamespace.getNamespace().toJavaPackage() ) );
        return packageDirectory;
    }

    private void generateBeanForComplexType( WSDLComplexType type )
        throws RuntimeException
    {
        File packageDirectory = packageDirectoryFor( type );
        Context context = new VelocityContext();
        context.put( "type", type );
        context.put( "string", StringUtil.getInstance() );
        context.put( "isBeanSerializable", this.beanSerializable );
        String beanName = type.toJavaType();
        generateArtifact( "bean.java.vm", packageDirectory, beanName + ".java", context );
        // Even if the extensions are never used by the services, we must generate them
        if ( type.getExtension() != null )
        {
            generateBeanForType( type.getExtension() );
        }
        for ( WSDLElement member : type.getMembersAndInheritance() )
        {
            generateBeanForType( member.getType() );
        }
    }

    private void generateBeanForSimpleType( WSDLSimpleType type )
        throws RuntimeException
    {
        if ( type.isEnumeration() )
        {
            File packageDirectory = packageDirectoryFor( type );
            Context context = new VelocityContext();
            context.put( "type", type );
            context.put( "string", StringUtil.getInstance() );
            String beanName = type.toJavaType();
            generateArtifact( "enum.java.vm", packageDirectory, beanName + ".java", context );
        }
    }

    private void generateArtifact( String templateName, File directory, String fileName, Context context )
        throws RuntimeException
    {
        PrintWriter writer = null;
        try
        {
            createDirectoryIfNecessary( directory );
            File artifact = new File( directory, fileName );
            if ( !artifact.exists() || wasGeneratedPreviously( artifact ) )
            {
                writer = new PrintWriter( artifact );
                Template template = Velocity.getTemplate( templateName );
                template.merge( context, writer );
            }
        }
        catch ( Exception cause )
        {
            throw new RuntimeException( cause );
        }
        finally
        {
            if ( writer != null )
            {
                writer.close();
            }
        }
    }

    private boolean wasGeneratedPreviously( File artifact )
    {
        return artifact.lastModified() < this.startGenerationDate;
    }

    /* WebService definition analyzer */
    private WSDLAnalyzer analyzer;

    /* Target generation directory. */
    private File outputDirectory;

    /* Indicates that generated beans are serializable. */
    private Boolean beanSerializable;

    /* Date of the generation beginning */
    private long startGenerationDate;

    /* Generated types */
    private List<WSDLType> generatedType = new ArrayList<WSDLType>();
}
