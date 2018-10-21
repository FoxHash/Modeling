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
import de.tu_clausthal.in.mec.modeling.model.erd.IErd;
import de.tu_clausthal.in.mec.modeling.model.storage.EModelStorage;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.io.IOException;
import java.text.MessageFormat;


/**
 * Implementation of deserialization to create concrete objects considering the JSON schema settings.
 * Deserializer: entity
 */
public final class CEntityDeserializer extends JsonDeserializer<Object> implements IEntityDeserializer
{

    private final String m_model;

    public CEntityDeserializer( @NonNull final String p_model )
    {
        m_model = p_model;
    }

    @Override
    public Object deserialize( final JsonParser p_parser, final DeserializationContext p_ctxt ) throws IOException, JsonProcessingException
    {
        final ObjectCodec l_objectcodec = p_parser.getCodec();
        final JsonNode l_jsonnode = l_objectcodec.readTree( p_parser );

        final String l_id = ( l_jsonnode.get( "id" ).isNull() ) ? null : l_jsonnode.get( "id" ).asText();
        final boolean l_weakentity = l_jsonnode.get( "weak_entity" ).asBoolean();

        if ( !l_jsonnode.has( "attributes" ) )
        {
            throw new RuntimeException( MessageFormat.format( "the entity [{0}] has no attributes connected", l_id ) );
        }

        if ( !l_jsonnode.get( "attributes" ).elements().hasNext() )
        {
            throw new RuntimeException( MessageFormat.format( "the entity [{0}] has no attributes connected", l_id ) );
        }

        final Object l_entity = EModelStorage.INSTANCE.apply( m_model ).<IErd>raw().addEntity( l_id, l_weakentity );

        l_jsonnode.get( "attributes" ).elements().forEachRemaining( attributes ->
        {
            final String l_name = ( attributes.get( "name" ).isNull() ) ? null : attributes.get( "name" ).asText();
            final String l_property = ( attributes.get( "property" ).isNull() ) ? null : attributes.get( "property" ).asText();

            EModelStorage.INSTANCE.apply( m_model ).<IErd>raw().addAttributeToEntity( l_name, l_property, l_id );
        } );

        return l_entity;
    }
}
