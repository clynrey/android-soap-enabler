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
package fr.norsys.asoape.mojo;

import java.io.File;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;

import fr.norsys.asoape.codegen.generator.WSDLGenerationException;
import fr.norsys.asoape.codegen.generator.WSDLGenerator;

/**
 * Generate WebService stub for Android applications.
 * 
 * @goal generate-soap-stub
 * @phase generate-sources
 */
public class GenerateAndroidClientMojo
    extends AbstractMojo
    implements LogChute
{
    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * Source directory that contains WSDL files.
     * @parameter expression="${project.basedir}/src/main/wsdl"
     * @required
     */
    private File definitionsDirectory;

    /**
     * Target directory tha will contain generated code.
     * @parameter expression="${project.build.directory}/generated-sources/asoape"
     * @required
     */
    private File outputDirectory;

    /**
     * Indicates whether generated java bean should implements java.io.Serializable.
     * @parameter default-value="false"
     * @required
     */
    private Boolean beanSerializable;

    @Override
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        //  Redirecting Velocity log to the Maven output.
        Velocity.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM, this );
        WSDLGenerator generator = new WSDLGenerator();
        generator.setOutputDirectory( outputDirectory );
        generator.setBeanSerializable( beanSerializable );
        for ( File definitionFile : FileUtils.listFiles( definitionsDirectory, new String[] { "wsdl" }, true ) )
        {
            Definition definition = readDefinitionFrom( definitionFile );
            try
            {
                generator.generateFrom( definition );
            }
            catch ( WSDLGenerationException cause )
            {
                throw new MojoExecutionException( "Error occurs during code generation process", cause );
            }
        }
        project.addCompileSourceRoot( outputDirectory.getAbsolutePath() );
    }

    private Definition readDefinitionFrom( File definitionFile )
        throws MojoExecutionException
    {
        try
        {
            WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
            return reader.readWSDL( definitionFile.getAbsolutePath() );
        }
        catch ( WSDLException cause )
        {
            throw new MojoExecutionException( String.format( "Unable to read definition file %s",
                                                             definitionFile.getAbsoluteFile() ), cause );
        }
    }

    @Override
    public void init( RuntimeServices rs )
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isLevelEnabled( int level )
    {
        switch ( level )
        {
            case LogChute.TRACE_ID:
                return getLog().isDebugEnabled();

            case LogChute.DEBUG_ID:
                return getLog().isDebugEnabled();

            case LogChute.INFO_ID:
                return getLog().isInfoEnabled();

            case LogChute.WARN_ID:
                return getLog().isWarnEnabled();

            case LogChute.ERROR_ID:
                return getLog().isErrorEnabled();

            default:
                return false;
        }
    }

    @Override
    public void log( int level, String message )
    {
        switch ( level )
        {
            case LogChute.TRACE_ID:
                getLog().debug(message);
                break;

            case LogChute.DEBUG_ID:
                getLog().debug(message);
                break;

            case LogChute.INFO_ID:
                getLog().info(message);
                break;

            case LogChute.WARN_ID:
                getLog().warn(message);
                break;

            case LogChute.ERROR_ID:
                getLog().error(message);
                break;

            default:
                break;
        }
    }

    @Override
    public void log( int level, String message, Throwable t )
    {
        switch ( level )
        {
            case LogChute.TRACE_ID:
                getLog().debug(message, t);
                break;

            case LogChute.DEBUG_ID:
                getLog().debug(message, t);
                break;

            case LogChute.INFO_ID:
                getLog().info(message, t);
                break;

            case LogChute.WARN_ID:
                getLog().warn(message, t);
                break;

            case LogChute.ERROR_ID:
                getLog().error(message, t);
                break;

            default:
                break;
        }
    }
}
