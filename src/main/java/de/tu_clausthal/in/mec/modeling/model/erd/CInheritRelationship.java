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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


/**
 * EMPTY
 * //TODO
 */
public class CInheritRelationship extends IBaseNode implements IInheritRelationship<IAttribute>
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
        if ( m_parententity.get() == null )
        {
            throw new RuntimeException( "You set already a parent entity for this relationship" );
        }
        else
        {
            m_parententity.compareAndSet( null, p_entity );
        }
        return p_entity;
    }

    @Override
    public IEntity<IAttribute> getParentEntity()
    {
        return m_parententity.get();
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
        m_childentities.add( p_entity );
        return p_entity;
    }

    @Override
    public boolean isValidISARelationship()
    {
        return ( m_parententity.get() != null ) && m_childentities.size() >= 2;
    }
}
