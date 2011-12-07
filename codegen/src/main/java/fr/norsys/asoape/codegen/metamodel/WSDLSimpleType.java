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

public interface WSDLSimpleType
    extends WSDLType
{
    /**
     * Indicates whether this simpletype represents an enumeration type or not.
     * 
     * @return Returns <code>true</code> if this simpletype represents an enumeration, <code>false</code> otherwise.
     */
    boolean isEnumeration();

    /**
     * If {@link #isEnumeration()} returns <code>true</code>, then this method should return the enumeration values
     * available for this simpletype.
     * 
     * @return Returns empty list if this simple type is not an enumeration.
     * @see #isEnumeration()
     */
    List<String> getValues();
}
