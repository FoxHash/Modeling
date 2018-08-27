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

import java.util.List;


/**
 * Interface to validate a given erd model. The validation can be only one
 * specific test or test the complete list.
 */
public interface IErdChecker
{
    /**
     * method to run all test below
     *
     * @return test result
     */
    boolean runAllTests();

    /**
     * method to validate the relationships
     * all relationships contain exactly 2 entities or be recursive
     *
     * @return test result
     */
    boolean validateRelationships();

    /**
     * entities should contain at least one attribute
     * without attributes an entity make no sense
     *
     * @return test result
     */
    boolean validateEntities();

    /**
     * validate is-a relationships
     * child entities are not connected to other relationships,
     * this is not allowed, because child entities are the most
     * abstract elements in a graphical model and has no connections
     *
     * @return test result
     */
    boolean validateISARelationships();

    /**
     * method to fetch all errors for output generating
     *
     * @return list with all errors
     */
    List<String> fetchErrors();

}
