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

import de.tu_clausthal.in.mec.modeling.model.erd.CErd;
import de.tu_clausthal.in.mec.modeling.model.erd.IErd;
import de.tu_clausthal.in.mec.modeling.model.storage.EModelStorage;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public Object createEntity( @PathVariable( "model" ) final String p_model, @RequestBody final Map<String, Object> p_input )
    {
        // convert map to json object for validation process
        final JSONObject l_json = new JSONObject( p_input );

        // json schema - validation
        try ( final InputStream l_input = getClass().getResourceAsStream( "/json_schema/entity.schema.json" ) )
        {
            final JSONObject l_jsonraw = new JSONObject( new JSONTokener( l_input ) );
            final Schema l_schema = SchemaLoader.load( l_jsonraw );
            l_schema.validate( l_json );
        }
        catch ( final IOException l_e2 )
        {
            l_e2.printStackTrace();
        }
        catch ( final ValidationException l_e1 )
        {
            return CErdController.generateErrorMessage( l_e1, "You have an error in you entity section!" );
        }

        if ( !l_json.has( "attributes" ) )
        {
            return CErdController.generateGeneralErrorMessage( "Your syntax has an error: no array for attributes was defined!" );
        }

        // create new entity
        final String l_entityid = l_json.get( "id" ).toString();
        final Boolean l_weakentity = Boolean.parseBoolean( l_json.get( "weak_entity" ).toString() );

        EModelStorage.INSTANCE.apply( p_model ).<IErd>raw().addEntity( l_entityid, l_weakentity );

        // add attributes
        l_json.getJSONArray( "attributes" ).forEach( elm ->
        {

            final String l_attributname = ( (JSONObject) elm ).get( "name" ).toString();
            final Boolean l_attributekey = Boolean.parseBoolean( ( (JSONObject) elm ).get( "key" ).toString() );
            final Boolean l_attributeweakkey = Boolean.parseBoolean( ( (JSONObject) elm ).get( "weak_key" ).toString() );
            final Boolean l_multivalue = Boolean.parseBoolean( ( (JSONObject) elm ).get( "multi_value" ).toString() );
            final Boolean l_derivedvalue = Boolean.parseBoolean( ( (JSONObject) elm ).get( "derived_value" ).toString() );

            EModelStorage.INSTANCE.apply( p_model ).<IErd>raw().addAttributeToEntity(
                l_attributname, l_attributekey, l_attributeweakkey, l_multivalue, l_derivedvalue, l_entityid );

        } );

        return new ResponseEntity<>( EModelStorage.INSTANCE.apply( p_model ).serialize(), HttpStatus.OK );
    }

    /**
     * create a new relationship
     *
     * @param p_model nme of the model
     * @param p_input json input
     * @return model|error
     */
    @RequestMapping( value = "/create/{model}/relationship", produces = "application/json" )
    public Object createRelationship( @PathVariable( "model" ) final String p_model, @RequestBody final Map<String, Object> p_input )
    {
        // convert mao to json object for validation process
        final JSONObject l_json = new JSONObject( p_input );

        // json schema - validation
        try ( final InputStream l_input = getClass().getResourceAsStream( "/json_schema/relationship.schema.json" ) )
        {
            final JSONObject l_jsonraw = new JSONObject( new JSONTokener( l_input ) );
            final Schema l_schema = SchemaLoader.load( l_jsonraw );
            l_schema.validate( l_json );
        }
        catch ( final IOException l_e2 )
        {
            l_e2.printStackTrace();
        }
        catch ( final ValidationException l_e1 )
        {
            CErdController.generateErrorMessage( l_e1, "You have an error in you relationship section!" );
        }

        // create new relationship
        final String l_relationshipid = l_json.get( "id" ).toString();
        final String l_relationshipdescription = l_json.get( "description" ).toString();

        EModelStorage.INSTANCE.apply( p_model ).<IErd>raw().addRelationship( l_relationshipid, l_relationshipdescription );

        // add attributes
        if ( l_json.has( "attributes" ) )
        {
            l_json.getJSONArray( "attributes" ).forEach( elm ->
            {

                final String l_attributname = ( (JSONObject) elm ).get( "name" ).toString();
                final Boolean l_attributekey = Boolean.parseBoolean(
                    ( (JSONObject) elm ).get( "key" ).toString() );
                final Boolean l_attributeweakkey = Boolean.parseBoolean(
                    ( (JSONObject) elm ).get( "weak_key" ).toString() );
                final Boolean l_multivalue = Boolean.parseBoolean(
                    ( (JSONObject) elm ).get( "multi_value" ).toString() );
                final Boolean l_derivedvalue = Boolean.parseBoolean(
                    ( (JSONObject) elm ).get( "derived_value" ).toString() );

                EModelStorage.INSTANCE.apply( p_model ).<IErd>raw().addAttributeToRelationship(
                    l_attributname, l_attributekey, l_attributeweakkey, l_multivalue, l_derivedvalue,
                    l_relationshipid
                );

            } );
        }

        return new ResponseEntity<>( EModelStorage.INSTANCE.apply( p_model ).serialize(), HttpStatus.OK );
    }

    /**
     * connect entity with relationship via uri
     *
     * @param p_model name of the model
     * @param p_connection name of the connection
     * @param p_relationship name of the relationship
     * @param p_entity name of the entity
     * @param p_cardinality cardinality
     * @return model|error
     */
    @RequestMapping( value = "/connect/{model}/{connection}/{relationship}/{entity}/{cardinality}", produces = "application/json" )
    public Object connectEntityRelationship( @PathVariable( "model" ) final String p_model, @PathVariable( "connection" ) final String p_connection,
                                             @PathVariable( "relationship" ) final String p_relationship, @PathVariable( "entity" ) final String p_entity,
                                             @PathVariable( "cardinality" ) final String p_cardinality
    )
    {
        EModelStorage.INSTANCE.apply( p_model ).<IErd>raw().connectEntityWithRelationship( p_connection, p_entity, p_relationship, p_cardinality );
        return new ResponseEntity<>( EModelStorage.INSTANCE.apply( p_model ).serialize(), HttpStatus.OK );
    }

    /**
     * create error message incl. error stack
     *
     * @param p_exception exception stack
     * @param p_errordescription description of the error
     * @return response and http status code
     */
    private static ResponseEntity<Object> generateErrorMessage( final ValidationException p_exception, final String p_errordescription )
    {
        // get current date and time
        final DateFormat l_dateformat = new SimpleDateFormat( "dd.MM.yyy HH:mm:ss" );
        final Date l_date = new Date();

        final Map<Object, Object> l_responsemap = new HashMap<>();
        final List<Object> l_errorstack = new ArrayList<>();

        l_responsemap.put( "date/time", l_dateformat.format( l_date ) );
        l_responsemap.put( "description", p_errordescription );
        l_responsemap.put( "error", p_exception.getMessage() );

        p_exception.getCausingExceptions().stream().map( ValidationException::getMessage ).forEach( l_errorstack::add );
        l_responsemap.put( "error_stack", l_errorstack );

        return new ResponseEntity<>( l_responsemap, HttpStatus.BAD_REQUEST );
    }

    /**
     * create general error message
     *
     * @param p_errordescription description of the error
     * @return response and http status code
     */
    private static ResponseEntity<Object> generateGeneralErrorMessage( final String p_errordescription )
    {
        // get current date and time
        final DateFormat l_dateformat = new SimpleDateFormat( "dd.MM.yyy HH:mm:ss" );
        final Date l_date = new Date();

        final Map<Object, Object> l_responsemap = new HashMap<>();

        l_responsemap.put( "date/time", l_dateformat.format( l_date ) );
        l_responsemap.put( "description", p_errordescription );

        return new ResponseEntity<>( l_responsemap, HttpStatus.BAD_REQUEST );
    }
}
