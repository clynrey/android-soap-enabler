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
package fr.norsys.asoape.xml.binding.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that this field is mapped with an XSD element.
 * 
 * @since 1.0
 */
@Target( ElementType.FIELD )
@Retention( RetentionPolicy.RUNTIME )
@Inherited
public @interface XmlElement
{
    /**
     * Type of multiplicity.
     */
    public enum Cardinality
    {
        /**
         * Equivalent to 0..1 or 1..1 multiplicity.
         */
        ONE,

        /**
         * Equivalent to 0..* or 1..* multiplicity.
         */
        MANY;
    }

    /**
     * @return Returns the target java type of this element.
     */
    Class<?> targetType();

    /**
     * @return Returns the multiplicity of this element.
     */
    Cardinality cardinality();

    /**
     * @return Returns the WSDL name of this element.
     */
    String name();
    
    /**
     * @return Returns if the property can be null according to the XML schema contract, defaults returns
     *         <code>true</code>.
     */
    boolean nullable() default true;
}
