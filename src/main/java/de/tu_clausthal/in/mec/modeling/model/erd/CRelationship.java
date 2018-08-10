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

import java.util.HashMap;
import java.util.Map;


/**
 * The relationships between the entities describe their behavior to each other.
 * Various properties can be assumed. Thus, a relationship can be constructed
 * recursively if the destination and source nodes are identical. But also
 * identifying relationships are possible. At the same time, the relationship
 * assumes the role of a relationship and an entity.
 */
public class CRelationship extends IBaseNode implements IRelationship<IAttribute>
{

    private final String m_description;
    private final boolean m_recursive;
    private final boolean m_identifying;
    private final Map<Integer, IAttribute> m_attributes;

    /**
     * constructor to create new relationship
     *
     * @param p_id name
     * @param p_recursive recursive relationship flag
     * @param p_identifying identifying relationship flag
     */
    protected CRelationship( @NonNull final String p_id, @NonNull final String p_description, @NonNull final boolean p_recursive, @NonNull final boolean p_identifying )
    {
        super( p_id );
        m_description = p_description;
        m_recursive = p_recursive;
        m_identifying = p_identifying;
        m_attributes = new HashMap<>();
    }

    @Override
    public boolean isRecursive()
    {
        return m_recursive;
    }

    @Override
    public boolean isIdentifying()
    {
        return m_identifying;
    }

    @Override
    public String getDescription()
    {
        return m_description;
    }

    @Override
    public IAttribute createAttribute( @NonNull final String p_id, @NonNull final boolean p_keyattribute, @NonNull final boolean p_weakkeyattribute,
                                       @NonNull final boolean p_multivalue, @NonNull final boolean p_derivedvalue
    )
    {
        final IAttribute l_attr = new CAttribute( p_id, p_keyattribute, p_weakkeyattribute, p_multivalue, p_derivedvalue );
        m_attributes.put( l_attr.hashCode(), l_attr );

        return l_attr;
    }

    @Override
    public Map<Integer, IAttribute> getConnectedAttributes()
    {
        return m_attributes;
    }
}
