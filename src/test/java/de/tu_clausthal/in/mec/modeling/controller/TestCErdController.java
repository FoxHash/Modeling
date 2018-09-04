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

package de.tu_clausthal.in.mec.modeling.controller;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


/**
 * TODO EMPTY
 */
@RunWith( SpringRunner.class )
@SpringBootTest
@FixMethodOrder( MethodSorters.JVM )
public final class TestCErdController
{
    private static final String TEST_MODEL_NAME = "foo";
    private static final String TEST_ENTITY = "{ \"id\" : \"Customer\", \"weak_entity\" : false, \"attributes\" : "
                                              + "[ { \"name\" : \"Name\", \"key\" : true, \"weak_key\" : false, \"compounded_value\" : false, "
                                              + "\"multi_value\" : false, \"derived_value\" : false } ] }";
    private static final String TEST_RELATIONSHIP = "{ \"id\" : \"rel01\", \"description\" : \"verfassen\" }";
    private static final String TEST_ISARELATIONSHIP = "{ \"id\": \"isa01\" }";
    private static final String TEST_CONNECTION = "{ \"id\" : \"con01\", \"relationship\" : \"rel01\", \"entity\" : \"Customer\", \"cardinality\" : \"1:1\" }";
    private static final String TEST_ISACONNECTION_PARENT = "{ \"id\": \"isacon01\", \"connection_type\": \"parent\", \"isarelationship\": \"isa01\", "
                                                            + "\"entity\": \"Customer\" }";
    private static final String TEST_ISACONNECTION_CHILD = "{ \"id\": \"isacon02\", \"connection_type\": \"child\", \"isarelationship\": \"isa01\", "
                                                           + "\"entity\": \"Customer\" }";
    private static final String TEST_COMPLETE_JSON_MODEL = "{ \"model_id\" : \"testmodel\", \"entities\" : [ { \"id\" : \"Customer\", "
                                                           + "\"weak_entity\" : false, \"attributes\" : [ { \"name\" : \"Name\", \"key\" : true, "
                                                           + "\"weak_key\" : false, \"compounded_value\" : false, \"multi_value\" : false, "
                                                           + "\"derived_value\" : false }, { \"name\" : \"Customer-No\", \"key\" : false, "
                                                           + "\"weak_key\" : false, \"compounded_value\" : false, \"multi_value\" : false, "
                                                           + "\"derived_value\" : false } ] }, { \"id\" : \"Product\", \"weak_entity\" : false, "
                                                           + "\"attributes\" : [ { \"name\" : \"Date\", \"key\" : false, \"weak_key\" : false, "
                                                           + "\"compounded_value\" : false, \"multi_value\" : false, \"derived_value\" : true }, "
                                                           + "{ \"name\" : \"Product-No\", \"key\" : true, \"weak_key\" : false, \"compounded_value\" : false, "
                                                           + "\"multi_value\" : false, \"derived_value\" : false } ] } ], \"relationships\" : [ { "
                                                           + "\"id\" : \"rel01\", \"description\" : \"purchase\" } ], \"connections\" : [ { "
                                                           + "\"id\" : \"con01\", \"relationship\" : \"rel01\", \"entity\" : \"Customer\", "
                                                           + "\"cardinality\" : \"1:1\" }, { \"id\" : \"con02\", \"relationship\" : \"rel01\", "
                                                           + "\"entity\" : \"Product\", \"cardinality\" : \"1:n\" } ], \"isa-relationships\": [], "
                                                           + "\"isa-connections\" : [] }";

    private MockMvc m_mockmvc;

    @Autowired
    private CErdController m_erdcontroller;

    @Before
    public void setup()
    {
        m_mockmvc = MockMvcBuilders.standaloneSetup( m_erdcontroller ).build();
    }

    /**
     * test if the controller exists
     *
     * @throws Exception excnull
     */
    @Test
    public void initialControllerTest() throws Exception
    {
        Assertions.assertThat( m_erdcontroller ).isNotNull();
    }

    /**
     * create new empty model
     *
     * @throws Exception excnull
     */
    @Test
    public void createNewModel() throws Exception
    {
        final MockHttpServletResponse l_response = m_mockmvc.perform(
            MockMvcRequestBuilders.get( "/erd/create/{model}", TEST_MODEL_NAME ).accept( MediaType.APPLICATION_JSON ) )
                                                            .andReturn()
                                                            .getResponse();

        Assertions.assertThat( l_response.getStatus() ).isEqualTo( HttpStatus.OK.value() );
    }

    /**
     * create new entity and connect to model
     *
     * @throws Exception excnull
     */
    @Test
    public void createEntity() throws Exception
    {
        Assertions.assertThat( preparePostRequest( "/erd/create/{model}/entity", TEST_MODEL_NAME, TEST_ENTITY ).getStatus() ).isEqualTo(
            HttpStatus.OK.value() );
    }

    /**
     * create new entity and connect to model
     *
     * @throws Exception excnull
     */
    @Test
    public void createRelationship() throws Exception
    {
        Assertions.assertThat( preparePostRequest( "/erd/create/{model}/relationship", TEST_MODEL_NAME, TEST_RELATIONSHIP ).getStatus() ).isEqualTo(
            HttpStatus.OK.value() );
    }

    /**
     * create new entity and connect to model
     *
     * @throws Exception excnull
     */
    @Test
    public void createISARelationship() throws Exception
    {
        Assertions.assertThat( preparePostRequest( "/erd/create/{model}/isa-relationship", TEST_MODEL_NAME, TEST_ISARELATIONSHIP ).getStatus() ).isEqualTo(
            HttpStatus.OK.value() );
    }

    /**
     * create new entity and connect to model
     *
     * @throws Exception excnull
     */
    @Test
    public void createConnection() throws Exception
    {
        Assertions.assertThat( preparePostRequest( "/erd/connect/relationship/{model}", TEST_MODEL_NAME, TEST_CONNECTION ).getStatus() ).isEqualTo(
            HttpStatus.OK.value() );
    }

    /**
     * create new entity and connect to model
     *
     * @throws Exception excnull
     */
    @Test
    public void createISAConnectionParent() throws Exception
    {
        Assertions.assertThat( preparePostRequest( "/erd/connect/isa-relationship/{model}", TEST_MODEL_NAME, TEST_ISACONNECTION_PARENT ).getStatus() )
                  .isEqualTo( HttpStatus.OK.value() );
    }

    /**
     * create new entity and connect to model
     *
     * @throws Exception excnull
     */
    @Test
    public void createISAConnectionChild() throws Exception
    {
        Assertions.assertThat( preparePostRequest( "/erd/connect/isa-relationship/{model}", TEST_MODEL_NAME, TEST_ISACONNECTION_CHILD ).getStatus() ).isEqualTo(
            HttpStatus.OK.value() );
    }

    /**
     * create new entity and connect to model
     *
     * @throws Exception excnull
     */
    @Test
    public void createModelFromJSON() throws Exception
    {
        Assertions.assertThat( preparePostRequest( "/erd/read", "", TEST_COMPLETE_JSON_MODEL ).getStatus() ).isEqualTo( HttpStatus.OK.value() );
    }


    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //
    // USEFUL METHODS
    //
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * prepare request for processing
     *
     * @param p_uri uri
     * @param p_modelname name of the model
     * @param p_postbody body
     * @return servlet response
     *
     * @throws Exception excnull
     */
    private MockHttpServletResponse preparePostRequest( @NonNull final String p_uri, @NonNull final String p_modelname, @NonNull final String p_postbody )
        throws Exception
    {
        return m_mockmvc.perform( MockMvcRequestBuilders.post( p_uri, p_modelname ).contentType( MediaType.APPLICATION_JSON ).content( p_postbody ) )
                        .andReturn().getResponse();
    }

}
