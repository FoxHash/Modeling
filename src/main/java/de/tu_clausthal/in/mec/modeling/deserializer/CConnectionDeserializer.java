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
import de.tu_clausthal.in.mec.modeling.model.erd.IErd;
import de.tu_clausthal.in.mec.modeling.model.storage.EModelStorage;

import java.io.IOException;

/**
 * Implementation of deserialization to create concrete objects considering the JSON schema settings.
 * Deserializer: connection
 */
public final class CConnectionDeserializer extends JsonDeserializer<Object> implements IConnectionDeserializer
{

    private final String m_model;

    public CConnectionDeserializer( final String p_model )
    {
        m_model = p_model;
    }

    @Override
    public Object deserialize( final JsonParser p_parser, final DeserializationContext p_ctxt ) throws IOException, JsonProcessingException
    {
        final ObjectCodec l_objectcodec = p_parser.getCodec();
        final JsonNode l_jsonnode = l_objectcodec.readTree( p_parser );

        final String l_id = ( l_jsonnode.get( "id" ).isNull() ) ? null : l_jsonnode.get( "id" ).asText();
        final String l_relationship = ( l_jsonnode.get( "relationship" ).isNull() ) ? null : l_jsonnode.get( "relationship" ).asText();
        final String l_entity = ( l_jsonnode.get( "entity" ).isNull() ) ? null : l_jsonnode.get( "entity" ).asText();
        final String l_cardinality = ( l_jsonnode.get( "cardinality" ).isNull() ) ? null : l_jsonnode.get( "cardinality" ).asText();

        return EModelStorage.INSTANCE.apply( m_model ).<IErd>raw().connectEntityWithRelationship( l_id, l_entity, l_relationship, l_cardinality );
    }
}
