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
import de.tu_clausthal.in.mec.modeling.validation.json_schema.CJsonSchema;
import de.tu_clausthal.in.mec.modeling.validation.json_schema.IJsonSchema;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.everit.json.schema.ValidationException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
//Checkstyle:OFF:MultipleStringLiterals
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

        // json schema validation
        final IJsonSchema l_jsonschema = new CJsonSchema( "/json_schema/entity.schema.json" );
        if ( !l_jsonschema.validateJson( l_json ) )
        {
            return CErdController.generateErrorMessage( l_jsonschema.getException(), "You have an error in you relationship section!" );
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
        l_json.getJSONArray( "attributes" ).forEach( elm -> CErdController.createAttributeToEntity( (JSONObject) elm, p_model, l_entityid ) );

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
        // convert map to json object for validation process
        final JSONObject l_json = new JSONObject( p_input );

        // json schema validation
        final IJsonSchema l_jsonschema = new CJsonSchema( "/json_schema/relationship.schema.json" );
        if ( !l_jsonschema.validateJson( l_json ) )
        {
            return CErdController.generateErrorMessage( l_jsonschema.getException(), "You have an error in you relationship section!" );
        }

        // create new relationship
        final String l_relationshipid = l_json.get( "id" ).toString();
        final String l_relationshipdescription = l_json.get( "description" ).toString();

        EModelStorage.INSTANCE.apply( p_model ).<IErd>raw().addRelationship( l_relationshipid, l_relationshipdescription );

        // add attributes
        if ( l_json.has( "attributes" ) )
        {
            l_json.getJSONArray( "attributes" ).forEach( elm -> CErdController.createAttributeToRelationship( (JSONObject) elm, p_model, l_relationshipid ) );
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
    @RequestMapping( value = "/connect/{model}", produces = "application/json" )
    public Object connectEntityRelationship( @PathVariable( "model" ) final String p_model, @RequestBody final Map<String, Object> p_input )
    {
        // convert map to json object for validation
        final JSONObject l_json = new JSONObject( p_input );

        // json schema validation
        final IJsonSchema l_jsonschema = new CJsonSchema( "/json_schema/connection.schema.json" );
        if ( !l_jsonschema.validateJson( l_json ) )
        {
            return CErdController.generateErrorMessage( l_jsonschema.getException(), "You have an error in your connection section!" );
        }

        // create new connection
        final String l_connectionid = l_json.get( "id" ).toString();
        final String l_relationshipname = l_json.get( "relationship" ).toString();
        final String l_entityname = l_json.get( "entity" ).toString();
        final String l_cardinality = l_json.get( "cardinality" ).toString();

        EModelStorage.INSTANCE.apply( p_model ).<IErd>raw().connectEntityWithRelationship( l_connectionid, l_entityname, l_relationshipname, l_cardinality );

        return new ResponseEntity<>( EModelStorage.INSTANCE.apply( p_model ).serialize(), HttpStatus.OK );
    }

    /**
     * read-in a complete graphical erd model in json representation
     *
     * @param p_input erd model in json
     * @return model|error
     */
    @RequestMapping( value = "/read", produces = "application/json" )
    public Object createModelFromJson( @RequestBody final Map<String, Object> p_input )
    {
        // convert map to json object for validation
        final JSONObject l_json = new JSONObject( p_input );

        // json schema validation
        final IJsonSchema l_jsonschema = new CJsonSchema( "/json_schema/erd_json.schema.json" );
        if ( !l_jsonschema.validateJson( l_json ) )
        {
            return CErdController.generateErrorMessage(
                l_jsonschema.getException(), "You have an error in your request. It was not possible to create a new erd model with the given json string!" );
        }

        // base informations
        final String l_model = l_json.get( "model_id" ).toString();

        // create model
        EModelStorage.INSTANCE.add( new CErd( l_model ) );

        // entities
        if ( l_json.has( "entities" ) )
        {
            l_json.getJSONArray( "entities" ).forEach( entity ->
            {
                final String l_entityid = ( (JSONObject) entity ).get( "id" ).toString();
                final Boolean l_weakentity = Boolean.parseBoolean( ( (JSONObject) entity ).get( "weak_entity" ).toString() );

                EModelStorage.INSTANCE.apply( l_model ).<IErd>raw().addEntity( l_entityid, l_weakentity );

                ( (JSONObject) entity ).getJSONArray( "attributes" )
                                       .forEach( attribute -> CErdController.createAttributeToEntity( (JSONObject) attribute, l_model, l_entityid ) );
            } );
        }

        // relationships
        if ( l_json.has( "relationships" ) )
        {
            l_json.getJSONArray( "relationships" ).forEach( relationship ->
            {
                final String l_relationshipid = ( (JSONObject) relationship ).get( "id" ).toString();
                final String l_relationshipdescription = ( (JSONObject) relationship ).get( "description" ).toString();

                EModelStorage.INSTANCE.apply( l_model ).<IErd>raw().addRelationship( l_relationshipid, l_relationshipdescription );

                if ( ( (JSONObject) relationship ).has( "attributes" ) )
                {
                    ( (JSONObject) relationship ).getJSONArray( "attributes" )
                                                 .forEach( attribute -> CErdController
                                                     .createAttributeToRelationship( (JSONObject) attribute, l_model, l_relationshipid ) );
                }

            } );
        }

        // connections
        if ( l_json.has( "connections" ) )
        {
            l_json.getJSONArray( "connections" ).forEach( connection ->
            {
                final String l_connectionid = ( (JSONObject) connection ).get( "id" ).toString();
                final String l_relationship = ( (JSONObject) connection ).get( "relationship" ).toString();
                final String l_entity = ( (JSONObject) connection ).get( "entity" ).toString();
                final String l_cardinality = ( (JSONObject) connection ).get( "cardinality" ).toString();

                EModelStorage.INSTANCE.apply( l_model ).<IErd>raw().connectEntityWithRelationship( l_connectionid, l_entity, l_relationship, l_cardinality );
            } );
        }

        return new ResponseEntity<>( EModelStorage.INSTANCE.apply( l_model ).serialize(), HttpStatus.OK );
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

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //
    // USEFUL STATIC METHODS
    //
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * static method to create a attribute to an entity
     *
     * @param p_obj json object with the attribute properties
     * @param p_model model id
     * @param p_entity entity id
     */
    private static void createAttributeToEntity( @NonNull final JSONObject p_obj, @NonNull final String p_model, @NonNull final String p_entity )
    {
        final String l_attributname = p_obj.get( "name" ).toString();
        final Boolean l_attributekey = Boolean.parseBoolean( p_obj.get( "key" ).toString() );
        final Boolean l_attributeweakkey = Boolean.parseBoolean( p_obj.get( "weak_key" ).toString() );
        final Boolean l_multivalue = Boolean.parseBoolean( p_obj.get( "multi_value" ).toString() );
        final Boolean l_derivedvalue = Boolean.parseBoolean( p_obj.get( "derived_value" ).toString() );

        EModelStorage.INSTANCE.apply( p_model ).<IErd>raw().addAttributeToEntity(
            l_attributname, l_attributekey, l_attributeweakkey, l_multivalue, l_derivedvalue, p_entity );
    }

    /**
     * static method to create a attribute to an relationship
     *
     * @param p_obj json object with the attribute properties
     * @param p_model model id
     * @param p_relationship relationship id
     */
    private static void createAttributeToRelationship( @NonNull final JSONObject p_obj, @NonNull final String p_model, @NonNull final String p_relationship )
    {
        final String l_attributname = p_obj.get( "name" ).toString();
        final Boolean l_attributekey = Boolean.parseBoolean( p_obj.get( "key" ).toString() );
        final Boolean l_attributeweakkey = Boolean.parseBoolean( p_obj.get( "weak_key" ).toString() );
        final Boolean l_multivalue = Boolean.parseBoolean( p_obj.get( "multi_value" ).toString() );
        final Boolean l_derivedvalue = Boolean.parseBoolean( p_obj.get( "derived_value" ).toString() );

        EModelStorage.INSTANCE.apply( p_model ).<IErd>raw().addAttributeToRelationship( l_attributname, l_attributekey, l_attributeweakkey,
                                                                                        l_multivalue, l_derivedvalue, p_relationship
        );
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
//Checkstyle:ON:MultipleStringLiterals
