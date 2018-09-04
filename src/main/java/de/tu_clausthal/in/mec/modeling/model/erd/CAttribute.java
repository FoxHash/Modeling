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


/**
 * An attribute forms a description in detail about an entity. Entities are objects that come
 * from the real world and model a fact. This fact can be specified more precisely
 * by attributes, which happens here.
 *
 * Attributes can assume various properties.
 */
public final class CAttribute implements IAttribute
{

    private final String m_name;
    private final boolean m_keyattribute;
    private final boolean m_weakkeyattribute;
    private final boolean m_compoundedvalue;
    private final boolean m_multivalue;
    private final boolean m_derivedvalue;

    CAttribute( @NonNull final String p_name, final boolean p_keyattribute, final boolean p_weakkeyattribute,
                final boolean p_compoundedvalue, final boolean p_multivalue, final boolean p_derivedvalue
    )
    {
        m_name = p_name;
        m_keyattribute = p_keyattribute;
        m_weakkeyattribute = p_weakkeyattribute;
        m_compoundedvalue = p_compoundedvalue;
        m_multivalue = p_multivalue;
        m_derivedvalue = p_derivedvalue;
    }

    @Override
    public String attributeName()
    {
        return m_name;
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
    public boolean isCompoundedValue()
    {
        return m_compoundedvalue;
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
