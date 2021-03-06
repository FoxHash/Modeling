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

import de.tu_clausthal.in.mec.modeling.model.graph.IGraph;
import de.tu_clausthal.in.mec.modeling.model.graph.INode;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


/**
 * implement model checker
 */
public final class CErdChecker implements IErdChecker
{

    private static final String ERROR_RELATIONSHIP = ": You have an error in your relationship. One of the two entities is not set.";
    private static final String ERROR_ENTITYATTRIBUT = ": The entity has less than 1 attribute. This is semantically not valid.";
    private static final String ERROR_ISARELATIONSHIP = ": This child entity is connected to a is-a relationship and a normal relationship. This is not allowed!";
    private static final String ERROR_WEAKATTRIBUTEENTITY = ": The attribute from the entity (see name in brackets) has an invalid key property.";

    private final IGraph<IErdNode, IErdEdge> m_model;
    private final List<String> m_errors = new ArrayList<>();

    /**
     * construct new model checker for graphical erd models
     *
     * @param p_model erd model
     */
    CErdChecker( @NonNull final IGraph<IErdNode, IErdEdge> p_model )
    {
        m_model = p_model;
    }

    @Override
    public boolean runAllTests()
    {
        return validateEntities() && validateRelationships() && validateISARelationships() && validateWeakEntityKeys();
    }

    @Override
    public boolean validateRelationships()
    {
        final long l_allrelationships = m_model.nodes()
                                               .filter( i -> i instanceof IRelationship )
                                               .count();

        // relationship is valid if the two entities are set
        final long l_validrelationship = m_model.nodes()
                                                .filter( i -> i instanceof IRelationship )
                                                .filter(
                                                    i -> i.<IRelationship<IAttribute>>raw().getConnectedEntities().size() == 2
                                                         || i.<IRelationship<IAttribute>>raw().isRecursive() )
                                                .count();

        m_model.nodes()
               .filter( i -> i instanceof IRelationship )
               .filter( i -> i.<IRelationship<IAttribute>>raw().getConnectedEntities().size() <= 1 && !i.<IRelationship<IAttribute>>raw().isRecursive() )
               .forEach( i -> m_errors.add( i.id() + ERROR_RELATIONSHIP ) );

        return l_allrelationships == l_validrelationship;
    }

    @Override
    public boolean validateEntities()
    {
        final long l_allentities = m_model.nodes()
                                          .filter( i -> i instanceof IEntity )
                                          .count();

        // entity is valid if it has more than one attribute
        final long l_validentities = m_model.nodes()
                                            .filter( i -> i instanceof IEntity )
                                            .filter( i -> i.<IEntity<IAttribute>>raw().getConnectedAttributes().size() >= 1 )
                                            .count();

        m_model.nodes()
               .filter( i -> i instanceof IEntity )
               .filter( i -> i.<IEntity<IAttribute>>raw().getConnectedAttributes().size() < 1 )
               .forEach( i -> m_errors.add( i.id() + ERROR_ENTITYATTRIBUT ) );

        return l_allentities == l_validentities;
    }

    @Override
    public boolean validateISARelationships()
    {
        final List<String> l_childnames = new ArrayList<>();
        final List<String> l_relationshipconnections = new ArrayList<>();
        final List<String> l_invalidchilds;

        m_model.nodes()
               .filter( i -> i instanceof IRelationship )
               .forEach( i -> l_relationshipconnections.addAll( i.<IRelationship<IAttribute>>raw().getConnectedEntities().keySet() ) );


        m_model.nodes()
               .filter( i -> i instanceof IInheritRelationship )
               .forEach( i -> l_childnames.addAll( i.<IInheritRelationship<IAttribute>>raw().getChildEntities() ) );


        final long l_invalidisarelationships = l_childnames.stream().filter( l_relationshipconnections::contains ).count();

        l_invalidchilds = l_childnames.stream()
                                      .filter( l_relationshipconnections::contains )
                                      .collect( Collectors.toList() );

        l_invalidchilds.forEach( i -> m_errors.add( i + ERROR_ISARELATIONSHIP ) );

        return l_invalidisarelationships == 0;
    }

    @Override
    public boolean validateWeakEntityKeys()
    {
        final AtomicReference<Integer> l_counter = new AtomicReference<>( 0 );

        m_model.nodes()
               .filter( i -> i instanceof IEntity )
               .filter( i -> i.<IEntity<IAttribute>>raw().isWeakEntity() )
               .forEach( p_iErdNode -> p_iErdNode.<IEntity<IAttribute>>raw().getConnectedAttributes().forEach( ( p_str, p_attribute ) ->
               {
                   if ( p_attribute.getProperty().equals( EAttributeProperty.KEY.getProperty() ) )
                   {
                       l_counter.set( l_counter.get() + 1 );
                       m_errors.add( p_attribute.attributeName() + " (" + p_iErdNode.id() + ")" + ERROR_WEAKATTRIBUTEENTITY );
                   }
               } ) );

        return l_counter.get() == 0;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Map<String, IEntity<IAttribute>> validateNormalization()
    {
        final Map<String, IEntity<IAttribute>> l_nonnormalizedentities;

        l_nonnormalizedentities = m_model.nodes()
                                         .filter( i -> i instanceof IEntity )
                                         .map( INode::<IEntity>raw )
                                         .collect( Collectors.toMap( INode::id, i -> i ) );

        m_model.nodes()
               .filter( i -> i instanceof IEntity )
               .forEach( p_iErdNode -> p_iErdNode.<IEntity<IAttribute>>raw().getConnectedAttributes().forEach( ( p_str, p_iattribute ) ->
               {
                   if ( p_iattribute.getProperty().equals( EAttributeProperty.KEY.getProperty() ) )
                   {
                       l_nonnormalizedentities.remove( p_iErdNode.id() );
                   }
               } ) );

        return Collections.unmodifiableMap( l_nonnormalizedentities );
    }

    @Override
    public List<String> fetchErrors()
    {
        runAllTests();
        return Collections.unmodifiableList( m_errors );
    }
}
