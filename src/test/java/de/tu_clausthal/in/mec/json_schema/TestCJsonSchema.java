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

package de.tu_clausthal.in.mec.json_schema;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.apache.commons.io.IOUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;


/**
 * test class to test the different json schema if they work correctly.
 * each schema has different components that must be included so that the input is valid.
 * other components are optional.
 */
public class TestCJsonSchema
{

    private final AtomicReference<Schema> m_jsonschema = new AtomicReference<>();
    private final AtomicReference<JSONObject> m_jsontestobject = new AtomicReference<>();

    /**
     * simple entity with all needed components
     *
     * @throws Exception if test failed
     */
    @Test
    public void entityCorrect() throws Exception
    {
        testSchemaWithFile( "json_schema/entity.schema.json", "json_schema_test/entity_correct.json" );
    }

    /**
     * simple entity with not all needed components
     * expected ValidationException (!)
     *
     * @throws Exception if test failed
     */
    @Test( expected = ValidationException.class )
    public void entityIncorrect() throws Exception
    {
        testSchemaWithFile( "json_schema/entity.schema.json", "json_schema_test/entity_incorrect.json" );
    }

    /**
     * simple relationship with all needed components
     *
     * @throws Exception if test failed
     */
    @Test
    public void relationshipCorrect() throws Exception
    {
        testSchemaWithFile( "json_schema/relationship.schema.json", "json_schema_test/relationship_correct.json" );
    }

    /**
     * simple relationship with one wrong defined component
     * expected ValidationException (!)
     *
     * @throws Exception if test failed
     */
    @Test( expected = ValidationException.class )
    public void relationshipIncorrect() throws Exception
    {
        testSchemaWithFile( "json_schema/relationship.schema.json", "json_schema_test/relationship_incorrect.json" );
    }

    /**
     * simple is-a relationship with all needed components
     *
     * @throws Exception if test failed
     */
    @Test
    public void isarelationshipCorrect() throws Exception
    {
        testSchemaWithFile( "json_schema/isarelationship.schema.json", "json_schema_test/isarelationship_correct.json" );
    }

    /**
     * simple connection between entity and relationship with all needed components
     *
     * @throws Exception if test failed
     */
    @Test
    public void connectionCorrect() throws Exception
    {
        testSchemaWithFile( "json_schema/connection.schema.json", "json_schema_test/connection_correct.json" );
    }

    /**
     * simple is-a connection between entity and is-a relationship with all needed components
     *
     * @throws Exception if test failed
     */
    @Test
    public void isaconnectionParentCorrect() throws Exception
    {
        testSchemaWithFile( "json_schema/isaconnection.schema.json", "json_schema_test/isaconnection_parent_correct.json" );
    }

    /**
     * simple is-a connection between entity and is-a relationship with all needed components
     *
     * @throws Exception if test failed
     */
    @Test
    public void isaconnectionChildorrect() throws Exception
    {
        testSchemaWithFile( "json_schema/isaconnection.schema.json", "json_schema_test/isaconnection_child_correct.json" );
    }

    /**
     * test complete graphical model represented in json
     *
     * @throws Exception if test failed
     */
    @Test
    public void completeModel() throws Exception
    {
        testSchemaWithFile( "json_schema/erd_json.schema.json", "json_schema_test/erd_json_correct.json" );
    }

    /**
     * test complete graphical model represented in json incl. is-a relationship
     *
     * @throws Exception if test failed
     */
    @Test
    public void completeWithISARelationshipModel() throws Exception
    {
        testSchemaWithFile( "json_schema/erd_json.schema.json", "json_schema_test/erd_json_isa_correct.json" );
    }





    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //
    // USEFUL STATIC METHODS
    //
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * load json schema for testing process
     *
     * @param p_schema json schema location (resources folder)
     * @throws JSONException if reading failure
     */
    private void loadJsonSchema( @NonNull final String p_schema ) throws JSONException
    {
        final AtomicReference<String> l_input = new AtomicReference<>();
        final ClassLoader l_classloader = getClass().getClassLoader();

        try
        {
            l_input.compareAndSet( null, IOUtils.toString( l_classloader.getResourceAsStream( p_schema ) ) );
        }
        catch ( final IOException l_stack )
        {
            l_stack.printStackTrace();
        }

        final JSONObject l_jsonobjectschema = new JSONObject( new JSONTokener( l_input.get() ) );
        final Schema l_schema = SchemaLoader.load( l_jsonobjectschema );
        m_jsonschema.set( SchemaLoader.load( l_jsonobjectschema ) );

    }

    /**
     * load json example file for testing process
     *
     * @param p_jsonfile json test file location (resources folder)
     * @throws JSONException if reading failure
     */
    private void loadJsonFile( @NonNull final String p_jsonfile ) throws JSONException
    {
        final AtomicReference<String> l_input = new AtomicReference<>();
        final ClassLoader l_classloader = getClass().getClassLoader();

        try
        {
            l_input.compareAndSet( null, IOUtils.toString( l_classloader.getResourceAsStream( p_jsonfile ) ) );
        }
        catch ( final IOException l_stack )
        {
            l_stack.printStackTrace();
        }

        m_jsontestobject.set( new JSONObject( new JSONTokener( l_input.get() ) ) );
    }

    /**
     * method to execute the test case with the schema and the test file
     *
     * @param p_schema file name of the schema
     * @param p_jsontestfile file name of the test file
     */
    private void testSchemaWithFile( @NonNull final String p_schema, @NonNull final String p_jsontestfile )
    {
        loadJsonSchema( p_schema );
        loadJsonFile( p_jsontestfile );
        m_jsonschema.get().validate( m_jsontestobject.get() );
    }

}
