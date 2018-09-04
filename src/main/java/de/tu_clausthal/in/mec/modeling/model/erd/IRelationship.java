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
     * check the relationship, if the two connected entities are the same
     *
     * @return recursive
     */
    boolean isRecursive();

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
     * @param p_compoundedvalue compounded value flag
     * @param p_multivalue multi value flag
     * @param p_derivedvalue derived value flag
     * @return self-reference
     */
    A createAttribute( @NonNull final String p_id, final boolean p_keyattribute, final boolean p_weakkeyattribute,
                       final boolean p_compoundedvalue, final boolean p_multivalue, final boolean p_derivedvalue
    );

    /**
     * get all connected attributes from the relationship in a map
     *
     * @return map with all attributes
     */
    Map<String, IAttribute> getConnectedAttributes();

    /**
     * connect entity incl. cardinality to the relationship
     *
     * @param p_entity name of the entity
     * @param p_cardinality cardinality
     * @return self-reference
     */
    IEntity<A> connectEntity( @NonNull final IEntity<IAttribute> p_entity, @NonNull final String p_cardinality );

    /**
     * return the connected entities in a map
     *
     * @return connected entities
     */
    Map<String, String> getConnectedEntities();

}
