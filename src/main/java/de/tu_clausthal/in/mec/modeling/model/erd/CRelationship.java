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

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;


/**
 * The relationships between the entities describe their behavior to each other.
 * Various properties can be assumed. Thus, a relationship can be constructed
 * recursively if the destination and source nodes are identical. But also
 * identifying relationships are possible. At the same time, the relationship
 * assumes the role of a relationship and an entity.
 */
public final class CRelationship extends IBaseNode implements IRelationship<IAttribute>
{

    private final String m_description;
    private final Map<String, IAttribute> m_attributes = new HashMap<>();
    private final AtomicReference<AbstractMap.SimpleEntry<IEntity<IAttribute>, EErdCardinality>> m_entityone = new AtomicReference<>( null );
    private final AtomicReference<AbstractMap.SimpleEntry<IEntity<IAttribute>, EErdCardinality>> m_entitytwo = new AtomicReference<>( null );

    /**
     * constructor to create new relationship
     *
     * @param p_id id
     * @param p_description name/description of the relationship
     */
    CRelationship( @NonNull final String p_id, @NonNull final String p_description )
    {
        super( p_id );
        m_description = p_description;
    }

    @Override
    public boolean isRecursive()
    {
        return ( m_entityone.get() != null ) && ( m_entitytwo.get() != null ) && m_entityone.get().equals( m_entitytwo.get() );
    }

    @Override
    public String getDescription()
    {
        return m_description;
    }

    @Override
    public IAttribute createAttribute( @NonNull final String p_id, final boolean p_keyattribute, final boolean p_weakkeyattribute,
                                       final boolean p_compoundedvalue, final boolean p_multivalue, final boolean p_derivedvalue
    )
    {
        final IAttribute l_attr = new CAttribute( p_id, p_keyattribute, p_weakkeyattribute, p_compoundedvalue, p_multivalue, p_derivedvalue );
        m_attributes.put( l_attr.attributeName(), l_attr );

        return l_attr;
    }

    @Override
    public Map<String, IAttribute> getConnectedAttributes()
    {
        return Collections.unmodifiableMap( m_attributes );
    }

    @Override
    public IEntity<IAttribute> connectEntity( @NonNull final IEntity<IAttribute> p_entity, @NonNull final String p_cardinality )
    {
        final IEntity<IAttribute> l_entity = p_entity;
        if ( m_entityone.get() == null )
        {
            m_entityone.compareAndSet( null, new AbstractMap.SimpleEntry<>( l_entity, EErdCardinality.of( p_cardinality ) ) );
            return l_entity;
        }

        if ( m_entitytwo.get() == null )
        {
            m_entitytwo.compareAndSet( null, new AbstractMap.SimpleEntry<>( l_entity, EErdCardinality.of( p_cardinality ) ) );
            return l_entity;
        }
        else
        {
            throw new RuntimeException( "The number of maximum elements has been exceeded. No more entity can connect to this relationship: " + m_id );
        }

    }

    @Override
    public Map<String, String> getConnectedEntities()
    {
        final Map<String, String> l_entities = new HashMap<>();

        if ( m_entityone.get() != null )
        {
            l_entities.put( m_entityone.get().getKey().id(), m_entityone.get().getValue().getCardinality() );
        }

        if ( m_entitytwo.get() != null )
        {
            l_entities.put( m_entitytwo.get().getKey().id(), m_entitytwo.get().getValue().getCardinality() );
        }

        return Collections.unmodifiableMap( l_entities );
    }
}
