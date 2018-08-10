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

import de.tu_clausthal.in.mec.modeling.model.graph.IBaseNode;
import edu.umd.cs.findbugs.annotations.NonNull;


/**
 * An attribute forms a description in detail about an entity. Entities are objects that come
 * from the real world and model a fact. This fact can be specified more precisely
 * by attributes, which happens here.
 *
 * Attributes can assume various properties.
 */
public class CAttribute extends IBaseNode implements IAttribute
{

    private final boolean m_keyattribute;
    private final boolean m_weakkeyattribute;
    private final boolean m_multivalue;
    private final boolean m_derivedvalue;

    protected CAttribute( @NonNull final String p_id, @NonNull final boolean p_keyattribute, @NonNull final boolean p_weakkeyattribute,
                          @NonNull final boolean p_multivalue, @NonNull final boolean p_derivedvalue
    )
    {
        super( p_id );

        m_keyattribute = p_keyattribute;
        m_weakkeyattribute = p_weakkeyattribute;
        m_multivalue = p_multivalue;
        m_derivedvalue = p_derivedvalue;
    }


    @Override
    public boolean isKeyAttribute()
    {
        return m_keyattribute;
    }

    @Override
    public boolean isWeakKeyAttribute()
    {
        return m_weakkeyattribute;
    }

    @Override
    public boolean isMultiValue()
    {
        return m_multivalue;
    }

    @Override
    public boolean isDerivedValue()
    {
        return m_derivedvalue;
    }
}
