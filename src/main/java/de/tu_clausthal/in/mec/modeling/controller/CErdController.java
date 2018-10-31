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
import de.tu_clausthal.in.mec.modeling.deserializer.erd.CConnectionDeserializer;
import de.tu_clausthal.in.mec.modeling.deserializer.erd.CEntityDeserializer;
import de.tu_clausthal.in.mec.modeling.deserializer.erd.CInheritConnectionDeserializer;
import de.tu_clausthal.in.mec.modeling.deserializer.erd.CInheritRelationshipDeserializer;
import de.tu_clausthal.in.mec.modeling.deserializer.erd.CModelDeserializer;
import de.tu_clausthal.in.mec.modeling.deserializer.erd.CRelationshipDeserializer;
import de.tu_clausthal.in.mec.modeling.model.erd.CErd;
import de.tu_clausthal.in.mec.modeling.model.erd.IAttribute;
import de.tu_clausthal.in.mec.modeling.model.erd.IEntity;
import de.tu_clausthal.in.mec.modeling.model.erd.IErd;
import de.tu_clausthal.in.mec.modeling.model.storage.EModelStorage;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import java.util.concurrent.atomic.AtomicReference;


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
    @RequestMapping( value = "/create/{model}", produces = MediaType.APPLICATION_JSON_VALUE )
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
    @RequestMapping( value = "/create/{model}/entity", produces = MediaType.APPLICATION_JSON_VALUE )
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
    @RequestMapping( value = "/create/{model}/relationship", produces = MediaType.APPLICATION_JSON_VALUE )
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
    @RequestMapping( value = "/create/{model}/isa-relationship", produces = MediaType.APPLICATION_JSON_VALUE )
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
    @RequestMapping( value = "/connect/relationship/{model}", produces = MediaType.APPLICATION_JSON_VALUE )
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
    @RequestMapping( value = "/connect/isa-relationship/{model}", produces = MediaType.APPLICATION_JSON_VALUE )
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
    @RequestMapping( value = "/read", produces = MediaType.APPLICATION_JSON_VALUE )
    public Object createModelFromJson( @RequestBody final String p_input )
    {
        final AtomicReference<String> l_modelid = new AtomicReference<>();

        try
        {
            final ObjectMapper l_objectmapper = new ObjectMapper();
            final SimpleModule l_simplemodule = new SimpleModule();
            l_simplemodule.addDeserializer( Object.class, new CModelDeserializer() );
            l_objectmapper.registerModule( l_simplemodule );
            l_modelid.compareAndSet( null, l_objectmapper.readValue( p_input, Object.class ).toString() );
        }
        catch ( final IOException l_e1 )
        {
            generateGeneralErrorMessage( ERROR_MESSAGE );
        }

        return new ResponseEntity<>( EModelStorage.INSTANCE.apply( l_modelid.get() ).serialize(), HttpStatus.OK );
    }

    /**
     * return a model by given id
     *
     * @param p_model model id
     * @return model|error
     */
    @RequestMapping( value = "/model/{model}", produces = MediaType.APPLICATION_JSON_VALUE )
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
    @RequestMapping( value = "/validate/{model}", produces = MediaType.APPLICATION_JSON_VALUE )
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

    /**
     * check if erd model can be normalized and return entities which can not be normalized
     *
     * @param p_model model id
     * @return check result
     */
    @RequestMapping( value = "/normalization/{model}", produces = MediaType.APPLICATION_JSON_VALUE )
    public Object normaizationCheck( @PathVariable( "model" ) final String p_model )
    {
        final Map<String, IEntity<IAttribute>> l_nonnormalizedentities = EModelStorage.INSTANCE.apply( p_model ).<IErd>raw().checkNormalization();
        final Map<Object, Object> l_responsemap = new HashMap<>();
        final DateFormat l_dateformat = new SimpleDateFormat( "dd.MM.yyyy HH:mm:ss" );
        final Date l_date = new Date();

        if ( l_nonnormalizedentities.size() > 0 )
        {
            l_responsemap.put( "description", "the following entities can't be normalized, because they have no key attribute connected" );
            l_responsemap.put( "date/time", l_dateformat.format( l_date ) );
            l_responsemap.put( "error counter", l_nonnormalizedentities.size() );
            l_responsemap.put( "non normalized entities", l_nonnormalizedentities );

            return new ResponseEntity<>( l_responsemap, HttpStatus.BAD_REQUEST );
        }
        else
        {
            l_responsemap.put( "date/time", l_dateformat.format( l_date ) );
            l_responsemap.put( "description", "all entities contains at least one key attribute" );

            return new ResponseEntity<>( l_responsemap, HttpStatus.OK );
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
