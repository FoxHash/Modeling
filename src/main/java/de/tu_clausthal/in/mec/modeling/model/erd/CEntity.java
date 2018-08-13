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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


/**
 * An entity is the description of an object, which is specified more precisely by attributes. Entities can
 * taken on attributes. Likewise, a distinction is made between a weak entity and a normal entity. This
 * specification can also be done in this object.
 */
public class CEntity extends IBaseNode implements IEntity<IAttribute>
{

    private final boolean m_weakentity;
    private Map<String, IAttribute> m_attributes = new HashMap<>();

    /**
     * constructor to create new entity
     *
     * @param p_id name
     * @param p_weakentity weak entity flag
     */
    CEntity( @NonNull final String p_id, final boolean p_weakentity )
    {
        super( p_id );
        m_weakentity = p_weakentity;
    }

    @Override
    public IAttribute createAttribute( @NonNull final String p_id, final boolean p_keyattribute, final boolean p_weakkeyattribute,
                                       final boolean p_multivalue, final boolean p_derivedvalue
    )
    {

        final IAttribute l_attr = new CAttribute( p_id, p_keyattribute, p_weakkeyattribute, p_multivalue, p_derivedvalue );
        m_attributes.put( l_attr.attributeName(), l_attr );

        return l_attr;
    }

    @Override
    public boolean isWeakEntity()
    {
        return m_weakentity;
    }

    @Override
    public Map<String, IAttribute> getConnectedAttributes()
    {
        return Collections.unmodifiableMap( m_attributes );
    }

    @Override
    public Stream<IAttribute> getConnectedAttributesStream()
    {
        return m_attributes.values().stream();
    }
}
