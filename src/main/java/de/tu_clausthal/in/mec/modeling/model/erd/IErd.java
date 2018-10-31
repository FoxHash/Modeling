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
import edu.umd.cs.findbugs.annotations.NonNull;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;


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
     * @param p_name name
     * @param p_weakentity flag for weak entity
     * @return self-reference
     */
    IErd addEntity( @NonNull final String p_name, final boolean p_weakentity );

    /**
     * add new attribute to an entity
     *
     * @param p_name name
     * @param p_property property of the attribute
     * @param p_entityid name of the entity
     * @return self-reference
     */
    IErd addAttributeToEntity( @NonNull final String p_name, @Nonnull final String p_property, @NonNull final String p_entityid );

    /**
     * add new relationship
     *
     * @param p_name name
     * @param p_description description of the relationship
     * @return self-reference
     */
    IErd addRelationship( @NonNull final String p_name, @NonNull final String p_description );

    /**
     * add new attribute to a relationship
     *
     * @param p_name name
     * @param p_property property of the attribute
     * @param p_relationshipid name of the relationship
     * @return self-referenced
     */
    IErd addAttributeToRelationship( @NonNull final String p_name, @Nonnull final String p_property, @NonNull final String p_relationshipid );

    /**
     * connect relationship with entity
     *
     * @param p_name connection name
     * @param p_entity id of the entity
     * @param p_relationship id of the relationship
     * @param p_cardinality cardinality between the relationship and the cardinality
     * @return self-reference
     */
    IErd connectEntityWithRelationship( @NonNull final String p_name, @NonNull final String p_entity, @NonNull final String p_relationship,
                                        @NonNull String p_cardinality
    );

    /**
     * add new is-a relationship
     *
     * @param p_name name
     * @return self-reference
     */
    IErd addISARelationship( @NonNull final String p_name );

    /**
     * connect parent entity with is-a relationship
     *
     * @param p_name connection name
     * @param p_parententity name of the parent entity
     * @param p_isarelationship name of the is-a relationship
     * @return self-reference
     */
    IErd connectParentEntityWithISARelationship( @NonNull final String p_name, @NonNull final String p_parententity, @NonNull final String p_isarelationship );

    /**
     * connect child entity with is-a relationship
     *
     * @param p_name connection name
     * @param p_childentity name of the child entity
     * @param p_isarelationship name of the is-a relationship
     * @return self-reference
     */
    IErd connectChildEntityWithISARelationship( @NonNull final String p_name, @NonNull final String p_childentity, @NonNull final String p_isarelationship );

    /**
     * general method to check a graphical model onto general things
     *
     * @return test result
     */
    boolean check();

    /**
     * general method to check a graphical model onto general things and return a list with errors
     *
     * @return list with errors
     */
    List<String> checkResult();

    /**
     * function to run the test if the erd can be normalized - all entities must have at least
     * one attribute with a key flag
     *
     * method return all entities, which has no key attribute connected
     *
     * @return map with all non normalized entities
     */
    Map<String, IEntity<IAttribute>> checkNormalization();
}
