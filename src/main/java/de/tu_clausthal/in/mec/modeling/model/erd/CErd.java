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

import com.fasterxml.jackson.annotation.JsonProperty;
import de.tu_clausthal.in.mec.modeling.model.IModel;
import de.tu_clausthal.in.mec.modeling.model.graph.IGraph;
import de.tu_clausthal.in.mec.modeling.model.graph.INode;
import de.tu_clausthal.in.mec.modeling.model.graph.jung.CUndirectedGraph;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * This class forms the administrative unit for the individual objects. The central
 * administration happens here. Instantiations and all other central tasks are stored
 * here, so that the highest possible abstraction takes place and a low coupling arises.
 *
 * The low coupling is achieved through the factory design pattern, so here's the management.
 */
public class CErd implements IErd
{

    /**
     * define new graph to store graphical model
     */
    private final IGraph<IErdNode, IErdEdge> m_network;

    public CErd( @NonNull final String p_name )
    {
        this.m_network = new CUndirectedGraph<>( p_name );
    }

    @NonNull
    @Override
    public String id()
    {
        return m_network.id();
    }

    @NonNull
    @Override
    public String description()
    {
        return "erd";
    }

    // TODO
    @Override
    public boolean terminated()
    {
        return false;
    }

    @NonNull
    @Override
    @SuppressWarnings( "unchecked" )
    public <N extends IModel<?>> N raw()
    {
        return (N) this;
    }

    @NonNull
    @Override
    // TODO
    public Object serialize()
    {
        return new CSerializer( m_network );
    }

    @Override
    public IErd call() throws Exception
    {
        return this;
    }

    @Override
    public IErd addEntity( @NonNull final String p_name, final boolean p_weakentity )
    {
        m_network.addnode( new CEntity( p_name, p_weakentity ) );
        return this;
    }

    @Override
    public IErd addAttributeToEntity( @NonNull final String p_name, final boolean p_keyattribute, final boolean p_weakkeyattribute,
                                      final boolean p_multivalue, final boolean p_derivedvalue, @NonNull final String p_entityid
    )
    {
        final IErdNode l_entity = m_network.node( p_entityid );

        if ( !( l_entity instanceof IEntity ) )
        {
            throw new RuntimeException( MessageFormat.format( "entity[{0}] must be an entity", l_entity ) );
        }

        ( (IEntity) l_entity ).createAttribute( p_name, p_keyattribute, p_weakkeyattribute, p_multivalue, p_derivedvalue );
        return this;
    }


    @Override
    public IErd addRelationship( @NonNull final String p_name, @NonNull final String p_description )
    {
        if ( m_network.node( p_name ) != null )
        {
            throw new RuntimeException(
                MessageFormat.format( "There also exists an relationship with this id[{0}]", p_name )
            );
        }

        m_network.addnode( new CRelationship( p_name, p_description ) );
        return this;
    }

    @Override
    public IErd addAttributeToRelationship( @NonNull final String p_name, final boolean p_keyattribute, final boolean p_weakkeyattribute,
                                            final boolean p_multivalue, final boolean p_derivedvalue, @NonNull final String p_relationshipid
    )
    {
        final IErdNode l_relationship = m_network.node( p_relationshipid );

        if ( !( l_relationship instanceof IRelationship ) )
        {
            throw new RuntimeException(
                MessageFormat.format( "relationship[{0}] must be an relationship", l_relationship )
            );
        }

        ( (IRelationship) l_relationship ).createAttribute( p_name, p_keyattribute, p_weakkeyattribute, p_multivalue, p_derivedvalue );
        return this;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public IErd connectEntityWithRelationship( @NonNull final String p_name, @NonNull final String p_entity, @NonNull final String p_relationship,
                                               @NonNull final String p_cardinality
    )
    {
        final IErdNode l_entity = m_network.node( p_entity );
        final IErdNode l_relationship = m_network.node( p_relationship );

        if ( !( l_entity instanceof IEntity && l_relationship instanceof IRelationship ) )
        {
            throw new RuntimeException(
                MessageFormat.format( "one of the following assignments has an error: entity[{0}] or relationship[{1}]", l_entity, l_relationship )
            );
        }

        ( (IRelationship) l_relationship ).connectEntity( (IEntity<IAttribute>) l_entity, p_cardinality );

        m_network.addedge( l_entity, l_relationship, new CErdEdge( p_name ) );
        return this;
    }

    @Override
    public IErd addISARelationship( @NonNull final String p_name )
    {
        if ( m_network.node( p_name ) != null )
        {
            throw new RuntimeException(
                MessageFormat.format( "There also exists an is-a relationship with this id[0]", p_name )
            );
        }

        m_network.addnode( new CInheritRelationship( p_name ) );
        return this;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public IErd connectParentEntityWithISARelationship( @NonNull final String p_name, @NonNull final String p_parententity,
                                                        @NonNull final String p_isarelationship
    )
    {
        final IErdNode l_entity = m_network.node( p_parententity );
        final IErdNode l_isarelationship = m_network.node( p_isarelationship );

        if ( !( l_entity instanceof IEntity && l_isarelationship instanceof IInheritRelationship ) )
        {
            throw new RuntimeException(
                MessageFormat.format( "one of the following assignments has an error: entity[{0}] or relationship[{1}]", l_entity, l_isarelationship )
            );
        }

        ( (IInheritRelationship) l_isarelationship ).setParentEntity( (IEntity) l_entity );

        m_network.addedge( l_entity, l_isarelationship, new CErdEdge( p_name ) );
        return this;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public IErd connectChildEntityWithISARelationship( @NonNull final String p_name, @NonNull final String p_childentity,
                                                       @NonNull final String p_isarelationship
    )
    {
        final IErdNode l_entity = m_network.node( p_childentity );
        final IErdNode l_isarelationship = m_network.node( p_isarelationship );

        if ( !( l_entity instanceof IEntity && l_isarelationship instanceof IInheritRelationship ) )
        {
            throw new RuntimeException(
                MessageFormat.format( "one of the following assignments has an error: entity[{0}] or relationship[{1}]", l_entity, l_isarelationship )
            );
        }

        ( (IInheritRelationship) l_isarelationship ).connectChildEntity( (IEntity) l_entity );

        m_network.addedge( l_entity, l_isarelationship, new CErdEdge( p_name ) );
        return this;
    }

    /**
     * calculate the hash code for the graph
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        return m_network.hashCode();
    }

    /**
     * equals current object with a given object by comparing the hash code
     *
     * @param p_object object to compare
     * @return equality
     */
    @Override
    public boolean equals( final Object p_object )
    {
        return p_object instanceof IModel<?> && p_object.hashCode() == this.hashCode();
    }


    @SuppressWarnings( "unchecked" )
    private static final class CSerializer
    {

        @JsonProperty( "entities" )
        private final Map<String, IEntity<IAttribute>> m_entities;

        @JsonProperty( "relationships" )
        private final Map<String, IRelationship<IAttribute>> m_relationships;

        @JsonProperty( "inherit_relationships" )
        private final Map<String, IInheritRelationship<IAttribute>> m_inheritrelationships;

        @JsonProperty( "stats" )
        private final Map<String, Integer> m_stats = new HashMap<>();

        CSerializer( @NonNull final IGraph<IErdNode, IErdEdge> p_model )
        {
            m_entities = p_model.nodes()
                                .filter( i -> i instanceof IEntity )
                                .map( INode::<IEntity>raw )
                                .collect( Collectors.toMap( INode::id, i -> i ) );

            m_relationships = p_model.nodes()
                                     .filter( i -> i instanceof IRelationship )
                                     .map( INode::<IRelationship>raw )
                                     .collect( Collectors.toMap( INode::id, i -> i ) );

            m_inheritrelationships = p_model.nodes()
                                            .filter( i -> i instanceof IInheritRelationship )
                                            .map( INode::<IInheritRelationship>raw )
                                            .collect( Collectors.toMap( INode::id, i -> i ) );

            m_stats.put( "entities", m_entities.size() );
            m_stats.put( "relationships", m_relationships.size() );
        }

    }

}
