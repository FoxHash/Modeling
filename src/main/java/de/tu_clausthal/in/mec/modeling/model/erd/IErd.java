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

import de.tu_clausthal.in.mec.modeling.model.IModel;
import de.tu_clausthal.in.mec.modeling.model.graph.IGraph;
import edu.umd.cs.findbugs.annotations.NonNull;


/**
 * The main modeling of the graphical model for an entity relationship diagram (ERD) is implemented
 * through this interface. Special base operations must be provided to build the graphical model.
 *
 * Only the main components, which are needed for the creation, are considered.
 */
public interface IErd extends IModel<IErd>
{

    /**
     * add new entity
     *
     * @param p_id name
     * @param p_weakentity flag for weak entity
     * @return self-reference
     */
    IErd addEntity( @NonNull final String p_id, final boolean p_weakentity );

    /**
     * add new attribute to an entity
     *
     * @param p_id name
     * @param p_keyattribute key flag
     * @param p_weakkeyattribute weak key flag
     * @param p_multivalue multi value flag
     * @param p_derivedvalue derived value flag
     * @param p_entityid name of the entity
     * @return self-reference
     */
    IErd addAttributeToEntity( @NonNull final String p_id, final boolean p_keyattribute, final boolean p_weakkeyattribute,
                               final boolean p_multivalue, final boolean p_derivedvalue, @NonNull final String p_entityid
    );

    /**
     * add new relationship
     *
     * @param p_id name
     * @param p_description description of the relationship
     * @return self-reference
     */
    IErd addRelationship( @NonNull final String p_id, @NonNull final String p_description );

    /**
     * add new attribute to a relationship
     *
     * @param p_id name
     * @param p_keyattribute key flag
     * @param p_weakkeyattribute weak key flag
     * @param p_multivalue multi value flag
     * @param p_derivedvalue derived value flag
     * @param p_relationshipid name of the relationship
     * @return self-referenced
     */
    IErd addAttributeToRelationship( @NonNull final String p_id, final boolean p_keyattribute, final boolean p_weakkeyattribute,
                                     final boolean p_multivalue, final boolean p_derivedvalue, @NonNull final String p_relationshipid
    );

    /**
     * connect relationship with entity
     *
     * @param p_id relationship name
     * @param p_entity id of the entity
     * @param p_relationship id of the relationship
     * @param p_cardinality cardinality between the relationship and the cardinality
     * @return self-reference
     */
    IErd connectEntityWithRelationship( @NonNull final String p_id, @NonNull final String p_entity, @NonNull final String p_relationship,
                                        @NonNull String p_cardinality
    );

    IGraph<IErdNode, IErdEdge> getNetwork();

}
