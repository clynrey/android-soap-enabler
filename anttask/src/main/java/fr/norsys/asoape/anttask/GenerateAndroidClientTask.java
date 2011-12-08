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
package fr.norsys.asoape.anttask;

import java.io.File;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import fr.norsys.asoape.codegen.generator.WSDLGenerationException;
import fr.norsys.asoape.codegen.generator.WSDLGenerator;

/**
 * Generate WebService stub for Android applications.
 */
public class GenerateAndroidClientTask
    extends Task
{
    /** Where to look for WSDL files. */
    private File wsdlDir;

    /** Where to place the generated files.*/
    private File destDir;

    /** Flag to render java beans serializable. */
    private boolean beanSerializable;

    @Override
    public void execute()
        throws BuildException
    {
        WSDLGenerator generator = new WSDLGenerator();
        generator.setOutputDirectory( destDir );
        generator.setBeanSerializable( beanSerializable );
        for ( File definitionFile : FileUtils.listFiles( wsdlDir, new String[] { "wsdl" }, true ) )
        {
            Definition definition = readDefinitionFrom( definitionFile );
            try
            {
                generator.generateFrom( definition );
            }
            catch ( WSDLGenerationException cause )
            {
                throw new BuildException( "Error occurs during code generation process", cause );
            }
        }
    }

    private Definition readDefinitionFrom( File definitionFile )
        throws BuildException
    {
        try
        {
            WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
            return reader.readWSDL( definitionFile.getAbsolutePath() );
        }
        catch ( WSDLException cause )
        {
            throw new BuildException( String.format( "Unable to read definition file %s",
                                                     definitionFile.getAbsoluteFile() ), cause );
        }
    }

    public void setWsdlDir( File wsdlDir )
    {
        this.wsdlDir = wsdlDir;
    }

    public void setDestDir( File destDir )
    {
        this.destDir = destDir;
    }

    public void setBeanSerializable( boolean beanSerializable )
    {
        this.beanSerializable = beanSerializable;
    }
}
