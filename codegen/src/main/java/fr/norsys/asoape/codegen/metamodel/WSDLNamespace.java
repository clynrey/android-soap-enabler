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

import java.util.List;

public interface WSDLNamespace
    extends HasName
{
    /**
     * Add this to the content of the namespace and update the namespace.
     * 
     * @param content Content to add to this namespace;
     */
    void add( HasNamespace content );

    /**
     * @return Returns all elements that are in this namespace, if this namespace has no element, returns empty list.
     * @see HasNamespace#setNamespace(WSDLNamespace)
     */
    List<HasNamespace> getContent();

    /**
     * Translate namespace name into Java package name.
     * @return Return the Java package name corresponding to this namespace as a package.
     */
    String toJavaPackage();
}
