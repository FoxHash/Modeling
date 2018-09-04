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

/**
 * A simple attribute that can be assigned in an ERD to an entity or relationship.
 * An attribute can take on certain properties that can be queried by the methods described below.
 */
public interface IAttribute
{

    /**
     * return the name of the attribute
     *
     * @return name
     */
    String attributeName();

    /**
     * is key attribute
     *
     * @return key flag
     */
    boolean isKeyAttribute();

    /**
     * is weak key attribute
     *
     * @return weak key flag
     */
    boolean isWeakKeyAttribute();

    /**
     * is compounded value attribute
     *
     * @return compounded value flag
     */
    boolean isCompoundedValue();

    /**
     * is multi value attribute
     *
     * @return multi value flag
     */
    boolean isMultiValue();

    /**
     * is derived value attribute
     *
     * @return derived value flag
     */
    boolean isDerivedValue();

}
