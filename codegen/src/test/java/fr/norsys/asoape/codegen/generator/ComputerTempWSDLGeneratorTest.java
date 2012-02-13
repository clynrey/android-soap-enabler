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

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import javax.wsdl.Definition;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ComputerTempWSDLGeneratorTest
{
    private File outputDirectory;

    @Before
    public void setUp()
        throws Exception
    {
        outputDirectory = new File( new File( "." ), "target/generated/source/" ).getAbsoluteFile();
        if ( !outputDirectory.exists() )
        {
            assertTrue( outputDirectory.mkdirs() );
        }
        assertTrue( outputDirectory.exists() );
        assertTrue( outputDirectory.isDirectory() );
        assertTrue( outputDirectory.canWrite() );
    }

    @After
    public void tearDown()
        throws Exception
    {
        outputDirectory.deleteOnExit();
    }

    @Test
    public void canGenerateSources()
        throws Exception
    {
        WSDLGenerator generator = new WSDLGenerator();
        generator.setOutputDirectory( outputDirectory );
        //URL wsdlUrl = new URL("http://www.w3schools.com/webservices/tempconvert.asmx?WSDL");
        URL wsdlUrl = ContactWSDLGeneratorTest.class.getResource( "/tempconvert.wsdl" );
        assertNotNull( wsdlUrl );
        WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
        Definition definition = reader.readWSDL( wsdlUrl.toString() );
        generator.generateFrom( definition );
        File beanPackage = new File( outputDirectory, "org/tempuri" );
        assertTrue( beanPackage.exists() );
        assertTrue( beanPackage.isDirectory() );
        File contactServiceTypeSourceFile = new File( beanPackage, "CelsiusToFahrenheit.java" );
        assertTrue( contactServiceTypeSourceFile.exists() );
        assertTrue( contactServiceTypeSourceFile.isFile() );
    }
}
