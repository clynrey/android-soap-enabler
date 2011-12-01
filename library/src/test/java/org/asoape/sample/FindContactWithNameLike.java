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
package org.asoape.sample;

import fr.norsys.asoape.xml.binding.annotation.XmlElement;
import fr.norsys.asoape.xml.binding.annotation.XmlRootElement;
import fr.norsys.asoape.xml.binding.annotation.XmlType;
import fr.norsys.asoape.xml.binding.annotation.XmlElement.Cardinality;

@XmlRootElement(name="findContactWithNameLike", namespace="http://asoape.norsys.fr/sample/")
@XmlType(propOrder={"nameLike"})
public class FindContactWithNameLike
{
    @XmlElement(name="nameLike", targetType=String.class, cardinality=Cardinality.ONE)
    private String nameLike;

    public void setNameLike( String nameLike )
    {
        this.nameLike = nameLike;
    }

    public String getNameLike()
    {
        return nameLike;
    }
}
