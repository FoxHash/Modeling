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

import javax.annotation.Nonnull;


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
    private final EAttributeProperty m_property;

    CAttribute( @NonNull final String p_name, @Nonnull final String p_property )
    {
        m_name = p_name;
        m_property = EAttributeProperty.of( p_property );
    }

    @Override
    public String attributeName()
    {
        return m_name;
    }

    @Override
    public String getProperty()
    {
        return m_property.getProperty();
    }
}
