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
import java.util.stream.Stream;


/**
 * An entity is a fundamental part of an ERD. An entity describes a real object that can be
 * specified in more detail with attributes and then linked to a relationship with other entities.
 * This allows facts to be modeled.
 *
 * @param <A> the type of an attribute
 */
public interface IEntity<A extends IAttribute> extends IErdNode
{

    /**
     * create new attribute to entity
     *
     * @param p_id name of the attribute
     * @param p_keyattribute key attribute flag
     * @param p_weakkeyattribute weak key attribute flag
     * @param p_multivalue multi value flag
     * @param p_derivedvalue derived value flag
     * @return self-reference
     */
    A createAttribute( @NonNull final String p_id, final boolean p_keyattribute, final boolean p_weakkeyattribute,
                       final boolean p_multivalue, final boolean p_derivedvalue
    );

    /**
     * return weak entity flag
     *
     * @return flag weak entity
     */
    boolean isWeakEntity();

    /**
     * get all connected attributes from the entity in a map
     *
     * @return map with all attributes
     */
    Map<String, A> getConnectedAttributes();


}
