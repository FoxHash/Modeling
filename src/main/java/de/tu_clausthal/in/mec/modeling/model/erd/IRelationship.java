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

import java.util.Map;


/**
 * A relationship is a fundamental part of an ERD. A relationship describes the
 * relation between one, two or more entities in detail. Relationships can also
 * contains attributes for a better specification.
 * This allows facts to be modeled.
 */
public interface IRelationship<A extends IAttribute> extends IErdNode
{

    /**
     * is relationship recursive
     * @return recursive flag
     */
    boolean isRecursive();

    /**
     * is relationship identifying
     *
     * @return identifying flag
     */
    boolean isIdentifying();

    /**
     * return the description of the relationship
     *
     * @return description
     */
    String getDescription();

    /**
     * create new attribute to relationship
     *
     * @param p_id name of the attribute
     * @param p_keyattribute key attribute flag
     * @param p_weakkeyattribute weak key attribute flag
     * @param p_multivalue multi value flag
     * @param p_derivedvalue derived value flag
     * @return self-reference
     */
    A createAttribute( @NonNull final String p_id, @NonNull final boolean p_keyattribute, @NonNull final boolean p_weakkeyattribute,
                       @NonNull final boolean p_multivalue, @NonNull final boolean p_derivedvalue
    );

    /**
     * get all connected attributes from the relationship in a map
     *
     * @return map with all attributes
     */
    Map<Integer, IAttribute> getConnectedAttributes();

}
