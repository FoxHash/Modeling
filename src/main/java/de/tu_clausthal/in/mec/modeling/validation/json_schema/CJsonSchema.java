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

package de.tu_clausthal.in.mec.modeling.validation.json_schema;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;


/**
 * EMPTY
 * //TODO
 */
public class CJsonSchema implements IJsonSchema
{

    private final JSONObject m_jsonschema;
    private final AtomicReference<Schema> m_schema = new AtomicReference<>();
    private final AtomicReference<ValidationException> m_exception = new AtomicReference<>();

    /**
     * constructor //TODO
     * @param p_schema
     */
    public CJsonSchema( @NonNull final String p_schema )
    {
        final InputStream l_input = getClass().getResourceAsStream( p_schema );
        m_jsonschema = new JSONObject( new JSONTokener( l_input ) );
        m_schema.compareAndSet( null, SchemaLoader.load( m_jsonschema ) );
    }

    @Override
    public boolean validateJson( @NonNull final JSONObject p_jsonobject )
    {
        try
        {
            m_schema.get().validate( p_jsonobject );
        }
        catch ( final ValidationException l_e1 )
        {
            m_exception.compareAndSet( null, l_e1 );
            return false;
        }

        return true;
    }

    @Override
    public ValidationException getException()
    {
        if ( m_exception != null )
        {
            return m_exception.get();
        }
        throw new RuntimeException( "You try to get invalid data" );
    }
}
