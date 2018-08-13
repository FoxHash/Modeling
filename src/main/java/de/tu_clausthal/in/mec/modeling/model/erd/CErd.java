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

import de.tu_clausthal.in.mec.modeling.model.IModel;
import de.tu_clausthal.in.mec.modeling.model.graph.IGraph;
import de.tu_clausthal.in.mec.modeling.model.graph.jung.CUndirectedGraph;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.text.MessageFormat;


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
        return null;
        //return new CSerializer( m_network ).toString();
    }

    @Override
    public IErd call() throws Exception
    {
        return this;
    }

    @Override
    public IErd addEntity( @NonNull final String p_id, final boolean p_weakentity )
    {
        m_network.addnode( new CEntity( p_id, p_weakentity ) );
        return this;
    }

    @Override
    public IErd addAttributeToEntity( @NonNull final String p_id, final boolean p_keyattribute, final boolean p_weakkeyattribute,
                                      final boolean p_multivalue, final boolean p_derivedvalue, @NonNull final String p_entityid
    )
    {
        final IErdNode l_entity = m_network.node( p_entityid );

        if ( !( l_entity instanceof IEntity ) )
        {
            throw new RuntimeException( MessageFormat.format( "entity[{0}] must be an entity", l_entity ) );
        }

        ( (IEntity) l_entity ).createAttribute( p_id, p_keyattribute, p_weakkeyattribute, p_multivalue, p_derivedvalue );
        return this;
    }


    @Override
    public IErd addRelationship( @NonNull final String p_id, @NonNull final String p_description )
    {
        if ( m_network.node( p_id ) != null )
        {
            throw new RuntimeException(
                MessageFormat.format( "There also exists an relationship with this id[{0}]", p_id )
            );
        }

        m_network.addnode( new CRelationship( p_id, p_description ) );
        return this;
    }

    @Override
    public IErd addAttributeToRelationship( @NonNull final String p_id, final boolean p_keyattribute, final boolean p_weakkeyattribute,
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

        ( (IRelationship) l_relationship ).createAttribute( p_id, p_keyattribute, p_weakkeyattribute, p_multivalue, p_derivedvalue );
        return this;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public IErd connectEntityWithRelationship( @NonNull final String p_id, @NonNull final String p_entity, @NonNull final String p_relationship,
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

        m_network.addedge( l_entity, l_relationship, new CErdEdge( p_id ) );
        return this;
    }

    public IGraph<IErdNode, IErdEdge> getNetwork()
    {
        return m_network;
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



    /*private static final class CSerializer
    {

        private final JsonObject m_jsonobj = new JsonObject();

        @SuppressWarnings( "unchecked" )
        private CSerializer( @NonNull final IGraph<IErdNode, IErdEdge> p_model )
        {
            m_jsonobj.addProperty( "model", p_model.id() );

            // array for all erd components
            final JsonArray l_entities = new JsonArray();
            final JsonArray l_relations = new JsonArray();
            final JsonArray l_connections = new JsonArray();

            p_model.nodes()
                   .filter( node -> node instanceof IEntity )
                   .forEach( node ->
                   {
                       // json object for entity and array for attributes
                       final JsonObject l_details = new JsonObject();
                       final JsonArray l_array = new JsonArray();

                       // fetch connected attributes and add to json array
                       ( (IEntity<IAttribute>) node ).getConnectedAttributes()
                                                     .entrySet()
                                                     .stream()
                                                     .forEach( elm ->
                                                     {
                                                         final JsonObject l_jsonobject = new JsonObject();
                                                         l_jsonobject.addProperty( "id", elm.getValue().id() );
                                                         l_jsonobject.addProperty( "key", elm.getValue().isKeyAttribute() );
                                                         l_jsonobject.addProperty( "weakKey", elm.getValue().isWeakKeyAttribute() );
                                                         l_jsonobject.addProperty( "multiVal", elm.getValue().isMultiValue() );
                                                         l_jsonobject.addProperty( "derivedVal", elm.getValue().isDerivedValue() );

                                                         l_array.add( l_jsonobject );
                                                     } );

                       // entity details for whole object description
                       l_details.addProperty( "id", node.id() );
                       l_details.addProperty( "weakEntity", ( (IEntity<IAttribute>) node ).isWeakEntity() );
                       l_details.add( "attributes", l_array );

                       // add entity to list of all entities
                       l_entities.add( l_details );

                   } );

            p_model.nodes()
                   .filter( node -> node instanceof IRelationship )
                   .forEach( node ->
                   {
                       // json object for relationships
                       final JsonObject l_details = new JsonObject();
                       final JsonArray l_array = new JsonArray();

                       ( (IRelationship<IAttribute>) node ).getConnectedAttributes()
                                                           .entrySet()
                                                           .stream()
                                                           .forEach( elm ->
                                                           {
                                                               final JsonObject l_jsonobject = new JsonObject();
                                                               l_jsonobject.addProperty( "id", elm.getValue().id() );
                                                               l_jsonobject.addProperty( "key", elm.getValue().isKeyAttribute() );
                                                               l_jsonobject.addProperty( "weakKey", elm.getValue().isWeakKeyAttribute() );
                                                               l_jsonobject.addProperty( "multiVal", elm.getValue().isMultiValue() );
                                                               l_jsonobject.addProperty( "derivedVal", elm.getValue().isDerivedValue() );

                                                               l_array.add( l_jsonobject );
                                                           } );

                       // fetch relationship information
                       l_details.addProperty( "id", node.id() );
                       l_details.addProperty( "description", ( (IRelationship<IAttribute>) node ).getDescription() );
                       l_details.addProperty( "recursive", ( (IRelationship<IAttribute>) node ).isRecursive() );
                       l_details.addProperty( "identifying", ( (IRelationship<IAttribute>) node ).isIdentifying() );
                       l_details.add( "attributes", l_array );

                       // add relationship to list of all relationships
                       l_relations.add( l_details );
                   } );

            p_model.edges()
                   .forEach( edge ->
                   {
                       final Map.Entry<IErdNode, IErdNode> l_points = p_model.endpoints( edge );

                       // json object for connections between the relationships and the entities
                       final JsonObject l_jsonobject = new JsonObject();
                       l_jsonobject.addProperty( "id", edge.id() );
                       l_jsonobject.addProperty( "from", l_points.getKey().id() );
                       l_jsonobject.addProperty( "to", l_points.getValue().id() );

                       final IErdEdge l_edge = p_model.edge( edge.id() );
                       l_jsonobject.addProperty( "cardinality", l_edge.getCardinality() );

                       l_connections.add( l_jsonobject );
                   } );

            // build json output
            m_jsonobj.add( "entities", l_entities );
            m_jsonobj.add( "relationships", l_relations );
            m_jsonobj.add( "connections", l_connections );

        }

        @Override
        public String toString()
        {
            return m_jsonobj.toString();
        }
    }*/

}
