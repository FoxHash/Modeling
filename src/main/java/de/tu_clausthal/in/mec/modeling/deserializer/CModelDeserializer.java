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

package de.tu_clausthal.in.mec.modeling.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import de.tu_clausthal.in.mec.modeling.model.erd.CErd;
import de.tu_clausthal.in.mec.modeling.model.erd.IErd;
import de.tu_clausthal.in.mec.modeling.model.storage.EModelStorage;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.io.IOException;


/**
 * FOO //TODO
 */
public final class CModelDeserializer extends JsonDeserializer<Object> implements IModelDeserializer
{

    private static final String NULL = "NULL";
    private static final String ID = "id";

    @Override
    public Object deserialize( final JsonParser p_parser, final DeserializationContext p_ctxt ) throws IOException, JsonProcessingException
    {
        final ObjectCodec l_objectcodec = p_parser.getCodec();
        final JsonNode l_jsonnode = l_objectcodec.readTree( p_parser );

        final String l_modelid = ( l_jsonnode.get( "model_id" ).asText().equalsIgnoreCase( NULL ) ) ? NULL : l_jsonnode.get( "model_id" ).asText();
        final Object l_model = EModelStorage.INSTANCE.add( new CErd( l_modelid ) );

        if ( l_jsonnode.has( "entities" ) )
        {
            deserializeEntities( l_modelid, l_jsonnode );
        }

        if ( l_jsonnode.has( "relationships" ) )
        {
            deserializeRelationships( l_modelid, l_jsonnode );
        }

        if ( l_jsonnode.has( "isa-relationships" ) )
        {
            deserializeISARelationships( l_modelid, l_jsonnode );
        }

        if ( l_jsonnode.has( "connections" ) )
        {
            deserializeConnections( l_modelid, l_jsonnode );
        }

        if ( l_jsonnode.has( "isa-connections" ) )
        {
            deserializeISAConnections( l_modelid, l_jsonnode );
        }

        return l_model;
    }


    private static void deserializeEntities( @NonNull final String p_modelid, final JsonNode p_jsonnode )
    {

        p_jsonnode.get( "entities" ).elements().forEachRemaining( entity ->
        {
            final String l_id = ( entity.get( ID ).asText().equalsIgnoreCase( NULL ) ) ? NULL : entity.get( ID ).asText();
            final boolean l_weakentity = Boolean.parseBoolean( entity.get( "weak_entity" ).asText() );

            EModelStorage.INSTANCE.apply( p_modelid ).<IErd>raw().addEntity( l_id, l_weakentity );

            if ( entity.has( "attributes" ) )
            {
                entity.get( "attributes" ).elements().forEachRemaining( attribute ->
                {

                    final String l_name = ( attribute.get( "name" ).asText().equalsIgnoreCase( NULL ) ) ? NULL : attribute.get( "name" ).asText();
                    final String l_property = attribute.get( "property" ).asText().equalsIgnoreCase( NULL ) ? NULL : attribute.get( "property" ).asText();

                    EModelStorage.INSTANCE.apply( p_modelid ).<IErd>raw().addAttributeToEntity( l_name, l_property, l_id );

                } );
            }
        } );

    }

    private static void deserializeRelationships( @NonNull final String p_modelid, final JsonNode p_jsonnode )
    {
        p_jsonnode.get( "relationships" ).elements().forEachRemaining( relationships ->
        {

            final String l_id = ( relationships.get( ID ).asText().equalsIgnoreCase( NULL ) ) ? NULL : relationships.get( ID ).asText();
            final String l_description = ( relationships.get( "description" ).asText().equalsIgnoreCase( NULL ) ) ? NULL : relationships.get( "description" )
                                                                                                                                        .asText();

            EModelStorage.INSTANCE.apply( p_modelid ).<IErd>raw().addRelationship( l_id, l_description );

        } );
    }

    private static void deserializeISARelationships( @NonNull final String p_modelid, final JsonNode p_jsonnode )
    {
        p_jsonnode.get( "isa-relationships" ).elements().forEachRemaining( isarelationships ->
        {

            final String l_id = ( isarelationships.get( ID ).asText().equalsIgnoreCase( NULL ) ) ? NULL : isarelationships.get( ID ).asText();
            EModelStorage.INSTANCE.apply( p_modelid ).<IErd>raw().addISARelationship( l_id );

        } );
    }

    private static void deserializeConnections( @NonNull final String p_modelid, final JsonNode p_jsonnode )
    {
        p_jsonnode.get( "connections" ).elements().forEachRemaining( connections ->
        {

            final String l_id = ( connections.get( ID ).asText().equalsIgnoreCase( NULL ) ) ? NULL : connections.get( ID ).asText();
            final String l_relationship = ( connections.get( "relationship" ).asText().equalsIgnoreCase( NULL ) ) ? NULL : connections.get( "relationship" ).asText();
            final String l_entity = ( connections.get( "entity" ).asText().equalsIgnoreCase( NULL ) ) ? NULL : connections.get( "entity" ).asText();
            final String l_cardinality = ( connections.get( "cardinality" ).asText().equalsIgnoreCase( NULL ) ) ? NULL : connections.get( "cardinality" ).asText();

            EModelStorage.INSTANCE.apply( p_modelid ).<IErd>raw().connectEntityWithRelationship( l_id, l_entity, l_relationship, l_cardinality );

        } );
    }

    private static void deserializeISAConnections( @NonNull final String p_modelid, final JsonNode p_jsonnode )
    {
        p_jsonnode.get( "isa-connections" ).elements().forEachRemaining( isaconnections ->
        {

            final String l_id = ( isaconnections.get( ID ).asText().equalsIgnoreCase( NULL ) ) ? NULL : isaconnections.get( ID ).asText();
            final String l_connectiontype = ( isaconnections.get( "connection_type" ).asText().equalsIgnoreCase( NULL ) ) ? NULL : isaconnections.get(
                "connection_type" ).asText();
            final String l_relationship = ( isaconnections.get( "isarelationship" ).asText().equalsIgnoreCase( NULL ) ) ? NULL : isaconnections.get(
                "isarelationship" ).asText();
            final String l_entity = ( isaconnections.get( "entity" ).asText().equalsIgnoreCase( NULL ) ) ? NULL : isaconnections.get( "entity" ).asText();

            if ( "child".equalsIgnoreCase( l_connectiontype ) )
            {
                EModelStorage.INSTANCE.apply( p_modelid ).<IErd>raw().connectChildEntityWithISARelationship( l_id, l_entity, l_relationship );
            }

            if ( "parent".equalsIgnoreCase( l_connectiontype ) )
            {
                EModelStorage.INSTANCE.apply( p_modelid ).<IErd>raw().connectParentEntityWithISARelationship( l_id, l_entity, l_relationship );
            }

            throw new RuntimeException(
                "You have an error in your request. It was not possible to connect the entity with a relationship! Your relationship definition was wrong." );

        } );
    }
}
