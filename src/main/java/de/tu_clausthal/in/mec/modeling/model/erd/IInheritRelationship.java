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

import java.util.List;


/**
 * EMPTY
 * //TODO
 * @param <A>
 */
public interface IInheritRelationship<A extends IAttribute> extends IErdNode
{
    /**
     * method to set the parent entity
     *
     * @param p_entity entity
     * @return self-reference
     */
    IEntity<A> setParentEntity( @NonNull final IEntity<A> p_entity );

    /**
     * method to get the parent entity
     *
     * @return parent entity name
     */
    String getParentEntity();

    /**
     * connect new child entity
     *
     * @param p_entity entity which will be a child
     * @return self-reference
     */
    IEntity<A> connectChildEntity( @NonNull final IEntity<A> p_entity );

    /**
     * get all connected child entities from this relationship
     *
     * @return list with all child's
     */
    List<String> getChildEntities();

    /**
     * check if this is-a relationship is valid (syntactical correct)
     *
     * @return syntactical check
     */
    boolean isValidISARelationship();

}
