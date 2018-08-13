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

import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.Arrays;


/**
 * This enum class describes the cardinality between a relationship and an entity.
 * This class allows to store the cardinality.
 */
public enum EErdCardinality implements IErdCardinality
{

    /**
     * possible cardinalities
     */
    ONEONE( "1:1" ),
    ONEN( "1:n" ),
    NM( "n:m" ),
    INHERIT( "inheritance" );

    /**
     * members to store cardinality value
     */
    private String m_cardinality;

    /**
     * constructor to create a new cardinality for a relationship
     *
     * @param p_cardinality cardinality
     */
    EErdCardinality( final String p_cardinality )
    {
        m_cardinality = p_cardinality;
    }


    @Override
    public String getCardinality()
    {
        return m_cardinality;
    }

    /**
     * static method to return enum to given string value
     *
     * @param p_value string represented value of the cardinality
     * @return enum cardinality
     */
    public static EErdCardinality of( @NonNull final String p_value )
    {
        return Arrays.stream( EErdCardinality.values() )
                     .filter( e -> e.getCardinality().equalsIgnoreCase( p_value ) )
                     .findFirst()
                     .orElseThrow( () -> new EnumConstantNotPresentException( EErdCardinality.class, p_value ) );
    }
}
