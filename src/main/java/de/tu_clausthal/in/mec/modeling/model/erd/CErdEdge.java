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

import de.tu_clausthal.in.mec.modeling.model.graph.IBaseEdge;
import edu.umd.cs.findbugs.annotations.NonNull;


/**
 * This class represents the edges between the relationships and the entities.
 * The edge also contains the cardinality, based on the enum class EErdCardinality.
 */
public final class CErdEdge extends IBaseEdge implements IErdEdge
{

    /**
     * constructor to create a new element
     *
     * @param p_id
     */
    CErdEdge( @NonNull final String p_id )
    {
        super( p_id );
    }

}
