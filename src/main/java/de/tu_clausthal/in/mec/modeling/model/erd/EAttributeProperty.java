/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the TU-Clausthal Mobile and Enterprise Computing Modeling     #
 * # Copyright (c) 2018-19                                                              #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU Lesser General Public License as                     #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU Lesser General Public License for more details.                                #
 * #                                                                                    #
 * # You should have received a copy of the GNU Lesser General Public License           #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package de.tu_clausthal.in.mec.modeling.model.erd;

import javax.annotation.Nonnull;
import java.util.Arrays;


/**
 * This enum class describes the possible attribute properties for an attribute.
 */
public enum EAttributeProperty implements IAttributeProperty
{

    /**
     * possible properties
     */
    ATTRIBUTE( "attribute" ),
    KEY( "key attribute" ),
    WEAKKEY( "weak key attribute" ),
    COMPOUNDEDVALUE( "compounded value attribute" ),
    MULTIVALUE( "multi value attribute" ),
    DERIVEDVALUE( "derived value attribute" );

    /**
     * member to store property
     */
    private String m_property;

    /**
     * constructor to create a new property for an attribute
     *
     * @param p_property property
     */
    EAttributeProperty( final String p_property )
    {
        m_property = p_property;
    }

    @Override
    public String getProperty()
    {
        return m_property;
    }

    /**
     * static method to return enum to given string value
     *
     * @param p_value string represented value of the property
     * @return enum property
     */
    public static EAttributeProperty of( @Nonnull final String p_value )
    {
        return Arrays.stream( EAttributeProperty.values() )
                     .filter( e -> e.getProperty().equalsIgnoreCase( p_value ) )
                     .findFirst()
                     .orElseThrow( () -> new EnumConstantNotPresentException( EAttributeProperty.class, p_value ) );
    }
}
