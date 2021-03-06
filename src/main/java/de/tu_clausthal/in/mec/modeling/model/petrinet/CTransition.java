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

package de.tu_clausthal.in.mec.modeling.model.petrinet;

import de.tu_clausthal.in.mec.modeling.model.graph.IBaseNode;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.stream.Stream;


/**
 * transition class
 */
public final class CTransition extends IBaseNode implements ITransition
{
    /**
     * ctor
     *
     * @param p_id transition id
     */
    public CTransition( @NonNull final String p_id )
    {
        super( p_id );
    }

    @Override
    public final Stream<IMark> apply( @NonNull final Stream<IMark> p_stream )
    {
        return p_stream;
    }

}
