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

package de.tu_clausthal.in.mec.modeling.deserializer.erd;

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
import java.text.MessageFormat;


/**
 * Implementation of deserialization to create concrete objects considering the JSON schema settings.
 * Deserializer: complete model
 */
public final class CModelDeserializer extends JsonDeserializer<Object> implements IModelDeserializer
{

    private static final String ID = "id";

    @Override
    public Object deserialize( final JsonParser p_parser, final DeserializationContext p_ctxt ) throws IOException, JsonProcessingException
    {
        final ObjectCodec l_objectcodec = p_parser.getCodec();
        final JsonNode l_jsonnode = l_objectcodec.readTree( p_parser );

        final String l_modelid = ( l_jsonnode.get( "model_id" ).isNull() ) ? null : l_jsonnode.get( "model_id" ).asText();
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

        return l_modelid;
    }


    private static void deserializeEntities( @NonNull final String p_modelid, final JsonNode p_jsonnode )
    {

        p_jsonnode.get( "entities" ).elements().forEachRemaining( entity ->
        {
            final String l_id = ( entity.get( ID ).isNull() ) ? null : entity.get( ID ).asText();
            final boolean l_weakentity = entity.get( "weak_entity" ).asBoolean();

            if ( !entity.has( "attributes" ) )
            {
                throw new RuntimeException( MessageFormat.format( "the entity [{0}] has no attributes connected", l_id ) );
            }

            if ( !entity.get( "attributes" ).elements().hasNext() )
            {
                throw new RuntimeException( MessageFormat.format( "the entity [{0}] has no attributes connected", l_id ) );
            }

            EModelStorage.INSTANCE.apply( p_modelid ).<IErd>raw().addEntity( l_id, l_weakentity );

            if ( entity.has( "attributes" ) && entity.get( "attributes" ).elements().hasNext() )
            {
                entity.get( "attributes" ).elements().forEachRemaining( attribute ->
                {

                    final String l_name = ( attribute.get( "name" ).isNull() ) ? null : attribute.get( "name" ).asText();
                    final String l_property = attribute.get( "property" ).isNull() ? null : attribute.get( "property" ).asText();

                    EModelStorage.INSTANCE.apply( p_modelid ).<IErd>raw().addAttributeToEntity( l_name, l_property, l_id );

                } );
            }
        } );

    }

    private static void deserializeRelationships( @NonNull final String p_modelid, final JsonNode p_jsonnode )
    {
        p_jsonnode.get( "relationships" ).elements().forEachRemaining( relationships ->
        {

            final String l_id = ( relationships.get( ID ).isNull() ) ? null : relationships.get( ID ).asText();
            final String l_description = ( relationships.get( "description" ).isNull() ) ? null : relationships.get( "description" ).asText();

            EModelStorage.INSTANCE.apply( p_modelid ).<IErd>raw().addRelationship( l_id, l_description );

        } );
    }

    private static void deserializeISARelationships( @NonNull final String p_modelid, final JsonNode p_jsonnode )
    {
        p_jsonnode.get( "isa-relationships" ).elements().forEachRemaining( isarelationships ->
        {

            final String l_id = ( isarelationships.get( ID ).isNull() ) ? null : isarelationships.get( ID ).asText();
            EModelStorage.INSTANCE.apply( p_modelid ).<IErd>raw().addISARelationship( l_id );

        } );
    }

    private static void deserializeConnections( @NonNull final String p_modelid, final JsonNode p_jsonnode )
    {
        p_jsonnode.get( "connections" ).elements().forEachRemaining( connections ->
        {

            final String l_id = ( connections.get( ID ).isNull() ) ? null : connections.get( ID ).asText();
            final String l_relationship = ( connections.get( "relationship" ).isNull() ) ? null : connections.get( "relationship" ).asText();
            final String l_entity = ( connections.get( "entity" ).isNull() ) ? null : connections.get( "entity" ).asText();
            final String l_cardinality = ( connections.get( "cardinality" ).isNull() ) ? null : connections.get( "cardinality" ).asText();

            EModelStorage.INSTANCE.apply( p_modelid ).<IErd>raw().connectEntityWithRelationship( l_id, l_entity, l_relationship, l_cardinality );

        } );
    }

    private static void deserializeISAConnections( @NonNull final String p_modelid, final JsonNode p_jsonnode )
    {
        p_jsonnode.get( "isa-connections" ).elements().forEachRemaining( isaconnections ->
        {

            final String l_id = ( isaconnections.get( ID ).isNull() ) ? null : isaconnections.get( ID ).asText();
            final String l_connectiontype = ( isaconnections.get( "connection_type" ).isNull() ) ? null : isaconnections.get( "connection_type" ).asText();
            final String l_relationship = ( isaconnections.get( "isarelationship" ).isNull() ) ? null : isaconnections.get( "isarelationship" ).asText();
            final String l_entity = ( isaconnections.get( "entity" ).isNull() ) ? null : isaconnections.get( "entity" ).asText();

            if ( "child".equalsIgnoreCase( l_connectiontype ) )
            {
                EModelStorage.INSTANCE.apply( p_modelid ).<IErd>raw().connectChildEntityWithISARelationship( l_id, l_entity, l_relationship );
            }
            else if ( "parent".equalsIgnoreCase( l_connectiontype ) )
            {
                EModelStorage.INSTANCE.apply( p_modelid ).<IErd>raw().connectParentEntityWithISARelationship( l_id, l_entity, l_relationship );
            }
            else
            {
                throw new RuntimeException(
                    "You have an error in your request. It was not possible to connect the entity with a relationship! Your relationship definition was wrong." );
            }


        } );
    }
}
