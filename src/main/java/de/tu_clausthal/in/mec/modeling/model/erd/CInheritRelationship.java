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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.tu_clausthal.in.mec.modeling.deserializer.CInheritRelationshipDeserializer;
import de.tu_clausthal.in.mec.modeling.model.graph.IBaseNode;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Is-a relationship implementation of the model.
 * This type of relationship make it possible to specify an entity more in detail.
 */
@JsonDeserialize( using = CInheritRelationshipDeserializer.class )
public final class CInheritRelationship extends IBaseNode implements IInheritRelationship<IAttribute>
{

    private final AtomicReference<IEntity<IAttribute>> m_parententity = new AtomicReference<>();
    private final List<IEntity<IAttribute>> m_childentities = new ArrayList<>();

    CInheritRelationship( @NonNull final String p_id )
    {
        super( p_id );
    }

    @Override
    public IEntity<IAttribute> setParentEntity( @NonNull final IEntity<IAttribute> p_entity )
    {
        if ( m_parententity.compareAndSet( null, p_entity ) )
        {
            return p_entity;
        }
        else
        {
            throw new RuntimeException( "You set already a parent entity for this relationship" );
        }
    }

    @Override
    public String getParentEntity()
    {
        if ( Objects.isNull( m_parententity.get() ) )
        {
            return "";
        }
        return m_parententity.get().id();
    }

    @Override
    public List<String> getChildEntities()
    {
        final List<String> l_childnames = new ArrayList<>();
        m_childentities.forEach( elm -> l_childnames.add( elm.id() ) );
        return Collections.unmodifiableList( l_childnames );
    }

    @Override
    public IEntity<IAttribute> connectChildEntity( @NonNull final IEntity<IAttribute> p_entity )
    {
        if ( m_childentities.contains( p_entity ) )
        {
            throw new RuntimeException( MessageFormat.format( "You already set this entity[{0}] to the child entities", p_entity.id() ) );
        }
        m_childentities.add( p_entity );
        return p_entity;
    }

    @Override
    public boolean isValidISARelationship()
    {
        return ( Objects.nonNull( m_parententity.get() ) ) && m_childentities.size() >= 1;
    }
}
