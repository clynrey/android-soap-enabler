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
package fr.norsys.asoape.codegen.metamodel;

public interface WSDLType
    extends HasName, HasNamespace
{
    /**
     * @return Return the Java corresponding type name.
     */
    String toJavaType();

    /**
     * @return Return teh full qualified Java corresponding type name, including package prefix.
     */
    String toFullQualifiedJavaType();

    /**
     * @return Return the base type indicating that this type is en extension.
     */
    WSDLType getBaseType();

    /**
     * @param baseType Base type of this type.
     */
    void setBaseType( WSDLType baseType );
}
