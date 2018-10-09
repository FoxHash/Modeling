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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.tu_clausthal.in.mec.modeling.deserializer.CConnectionDeserializer;
import de.tu_clausthal.in.mec.modeling.deserializer.CEntityDeserializer;
import de.tu_clausthal.in.mec.modeling.deserializer.CInheritConnectionDeserializer;
import de.tu_clausthal.in.mec.modeling.deserializer.CInheritRelationshipDeserializer;
import de.tu_clausthal.in.mec.modeling.deserializer.CModelDeserializer;
import de.tu_clausthal.in.mec.modeling.deserializer.CRelationshipDeserializer;
import de.tu_clausthal.in.mec.modeling.model.erd.CErd;
import de.tu_clausthal.in.mec.modeling.model.erd.IErd;
import de.tu_clausthal.in.mec.modeling.model.storage.EModelStorage;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * erd model rest controller
 */
@RestController
@RequestMapping( "/erd" )
public final class CErdController
{

    private static final String ERROR_MESSAGE = "You have an error in your request! Your input could not be read.";

    /**
     * create new erd
     *
     * @param p_model name of the model
     * @return model
     */
    @RequestMapping( value = "/create/{model}", produces = "application/json" )
    public Object createModel( @PathVariable( "model" ) final String p_model )
    {
        return EModelStorage.INSTANCE.add( new CErd( p_model ) ).serialize();
    }

    /**
     * create a new entity
     *
     * @param p_model name of the model
     * @param p_input json input
     * @return model|error
     */
    @RequestMapping( value = "/create/{model}/entity", produces = "application/json" )
    public Object createEntity( @PathVariable( "model" ) final String p_model, @RequestBody final String p_input )
    {
        try
        {
            final ObjectMapper l_objectmapper = new ObjectMapper();
            final SimpleModule l_simplemodule = new SimpleModule();
            l_simplemodule.addDeserializer( Object.class, new CEntityDeserializer( p_model ) );
            l_objectmapper.registerModule( l_simplemodule );
            l_objectmapper.readValue( p_input, Object.class );
        }
        catch ( final IOException l_e1 )
        {
            generateGeneralErrorMessage( ERROR_MESSAGE );
        }

        return new ResponseEntity<>( EModelStorage.INSTANCE.apply( p_model ).serialize(), HttpStatus.OK );
    }

    /**
     * create a new relationship
     *
     * @param p_model name of the model
     * @param p_input json input
     * @return model|error
     */
    @RequestMapping( value = "/create/{model}/relationship", produces = "application/json" )
    public Object createRelationship( @PathVariable( "model" ) final String p_model, @RequestBody final String p_input )
    {
        try
        {
            final ObjectMapper l_objectmapper = new ObjectMapper();
            final SimpleModule l_simplemodule = new SimpleModule();
            l_simplemodule.addDeserializer( Object.class, new CRelationshipDeserializer( p_model ) );
            l_objectmapper.registerModule( l_simplemodule );
            l_objectmapper.readValue( p_input, Object.class );
        }
        catch ( final IOException l_e1 )
        {
            generateGeneralErrorMessage( ERROR_MESSAGE );
        }

        return new ResponseEntity<>( EModelStorage.INSTANCE.apply( p_model ).serialize(), HttpStatus.OK );
    }

    /**
     * create new is-a relationship
     *
     * @param p_model name of the model
     * @param p_input json input
     * @return model|error
     */
    @RequestMapping( value = "/create/{model}/isa-relationship", produces = "application/json" )
    public Object createISARelationship( @PathVariable( "model" ) final String p_model, @RequestBody final String p_input )
    {
        try
        {
            final ObjectMapper l_objectmapper = new ObjectMapper();
            final SimpleModule l_simplemodule = new SimpleModule();
            l_simplemodule.addDeserializer( Object.class, new CInheritRelationshipDeserializer( p_model ) );
            l_objectmapper.registerModule( l_simplemodule );
            l_objectmapper.readValue( p_input, Object.class );
        }
        catch ( final IOException l_e1 )
        {
            generateGeneralErrorMessage( ERROR_MESSAGE );
        }

        return new ResponseEntity<>( EModelStorage.INSTANCE.apply( p_model ).serialize(), HttpStatus.OK );
    }

    /**
     * connect entity with relationship
     *
     * @param p_model name of the model
     * @param p_input json input
     * @return model|error
     */
    @RequestMapping( value = "/connect/relationship/{model}", produces = "application/json" )
    public Object connectEntityRelationship( @PathVariable( "model" ) final String p_model, @RequestBody final String p_input )
    {
        try
        {
            final ObjectMapper l_objectmapper = new ObjectMapper();
            final SimpleModule l_simplemodule = new SimpleModule();
            l_simplemodule.addDeserializer( Object.class, new CConnectionDeserializer( p_model ) );
            l_objectmapper.registerModule( l_simplemodule );
            l_objectmapper.readValue( p_input, Object.class );
        }
        catch ( final IOException l_e1 )
        {
            generateGeneralErrorMessage( ERROR_MESSAGE );
        }

        return new ResponseEntity<>( EModelStorage.INSTANCE.apply( p_model ).serialize(), HttpStatus.OK );
    }

    /**
     * connect an entity with a is-a relationship
     *
     * @param p_model name of the model
     * @param p_input json input
     * @return model|error
     */
    @RequestMapping( value = "/connect/isa-relationship/{model}", produces = "application/json" )
    public Object connectEntityISARelationship( @PathVariable( "model" ) final String p_model, @RequestBody final String p_input )
    {
        try
        {
            final ObjectMapper l_objectmapper = new ObjectMapper();
            final SimpleModule l_simplemodule = new SimpleModule();
            l_simplemodule.addDeserializer( Object.class, new CInheritConnectionDeserializer( p_model ) );
            l_objectmapper.registerModule( l_simplemodule );
            l_objectmapper.readValue( p_input, Object.class );
        }
        catch ( final IOException l_e1 )
        {
            generateGeneralErrorMessage( ERROR_MESSAGE );
        }

        return new ResponseEntity<>( EModelStorage.INSTANCE.apply( p_model ).serialize(), HttpStatus.OK );
    }

    /**
     * read-in a complete graphical erd model in json representation
     *
     * @param p_input erd model in json
     * @return model|error
     */
    @RequestMapping( value = "/read", produces = "application/json" )
    public Object createModelFromJson( @RequestBody final String p_input )
    {
        try
        {
            final ObjectMapper l_objectmapper = new ObjectMapper();
            final SimpleModule l_simplemodule = new SimpleModule();
            l_simplemodule.addDeserializer( Object.class, new CModelDeserializer() );
            l_objectmapper.registerModule( l_simplemodule );
            l_objectmapper.readValue( p_input, Object.class );
        }
        catch ( final IOException l_e1 )
        {
            generateGeneralErrorMessage( ERROR_MESSAGE );
        }

        return new ResponseEntity<>( EModelStorage.INSTANCE.apply( "testmodel" ).serialize(), HttpStatus.OK );
    }

    /**
     * return a model by given id
     *
     * @param p_model model id
     * @return model|error
     */
    @RequestMapping( value = "/model/{model}", produces = "application/json" )
    public Object getModel( @PathVariable( "model" ) final String p_model )
    {
        try
        {
            return new ResponseEntity<>( EModelStorage.INSTANCE.apply( p_model ).serialize(), HttpStatus.OK );
        }
        catch ( final RuntimeException l_e1 )
        {
            return CErdController.generateGeneralErrorMessage( "The model you requested was not found on this server!" );
        }
    }

    /**
     * check existing model
     *
     * @param p_model model id
     * @return check result | error
     */
    @RequestMapping( value = "/validate/{model}", produces = "application/json" )
    public Object validateModel( @PathVariable( "model" ) final String p_model )
    {
        try
        {
            return CErdController.generateValidationReport( EModelStorage.INSTANCE.apply( p_model ).<IErd>raw().checkResult() );
        }
        catch ( final RuntimeException l_e1 )
        {
            return CErdController.generateGeneralErrorMessage( "The model you requested was not found on this server!" );
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //
    // USEFUL STATIC METHODS
    //
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * create general error message
     *
     * @param p_errordescription description of the error
     * @return response and http status code
     */
    private static ResponseEntity<Object> generateGeneralErrorMessage( @NonNull final String p_errordescription )
    {
        // get current date and time
        final DateFormat l_dateformat = new SimpleDateFormat( "dd.MM.yyy HH:mm:ss" );
        final Date l_date = new Date();

        final Map<Object, Object> l_responsemap = new HashMap<>();

        l_responsemap.put( "date/time", l_dateformat.format( l_date ) );
        l_responsemap.put( "description", p_errordescription );

        return new ResponseEntity<>( l_responsemap, HttpStatus.BAD_REQUEST );
    }

    /**
     * generate validation report for the model
     *
     * @param p_resultlist list with errors and semantically errors
     * @return response and http status code
     */
    private static ResponseEntity<Object> generateValidationReport( @NonNull final List<String> p_resultlist )
    {
        final DateFormat l_dateformat = new SimpleDateFormat( "dd.MM.yyyy HH:mm:ss" );
        final Date l_date = new Date();

        final Map<Object, Object> l_responsemap = new HashMap<>();

        l_responsemap.put( "date/time", l_dateformat.format( l_date ) );
        l_responsemap.put( "error counter", p_resultlist.size() );
        l_responsemap.put( "error list", p_resultlist );

        return new ResponseEntity<>( l_responsemap, HttpStatus.OK );
    }
}
