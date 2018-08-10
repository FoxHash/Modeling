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
import de.tu_clausthal.in.mec.modeling.model.erd.CErdLoader;
import de.tu_clausthal.in.mec.modeling.model.erd.IErd;
import de.tu_clausthal.in.mec.modeling.model.storage.EModelStorage;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
     * @return model
     */
    @RequestMapping( value = "/create/{model}", produces = "application/json" )
    public Object createModel( @PathVariable( "model" ) final String p_model )
    {
        return EModelStorage.INSTANCE.add( new CErd( p_model ) ).serialize();
    }

    /**
     * create new entity
     *
     * @param p_model
     * @param p_name
     * @param p_weak
     * @return model
     */
    @RequestMapping( value = "/entity/{model}/{name}/{weak}", produces = "application/json" )
    public Object createEntity( @PathVariable( "model" ) final String p_model, @PathVariable( "name" ) final String p_name,
                                @PathVariable( "weak" ) final String p_weak
    )
    {

        final boolean l_weak = Boolean.parseBoolean( p_weak );
        return EModelStorage.INSTANCE.apply( p_model ).<IErd>raw().addEntity( p_name, l_weak ).serialize();

    }

    /**
     * create new attribute and connect it to an entity
     *
     * @param p_model
     * @param p_entity
     * @param p_name
     * @param p_key
     * @param p_weakkey
     * @param p_multivalue
     * @param p_derived
     * @return model
     */
    @RequestMapping( value = "/connect/attribute/entity/{model}/{entity_id}/{name}/{key}/{weakkey}/{multivalue}/{derived}", produces = "application/json" )
    public Object connectAttributeToEntity( @PathVariable( "model" ) final String p_model, @PathVariable( "entity_id" ) final String p_entity,
                                            @PathVariable( "name" ) final String p_name, @PathVariable( "key" ) final String p_key,
                                            @PathVariable( "weakkey" ) final String p_weakkey, @PathVariable( "multivalue" ) final String p_multivalue,
                                            @PathVariable( "derived" ) final String p_derived
    )
    {
        final boolean l_key = Boolean.parseBoolean( p_key );
        final boolean l_weakkey = Boolean.parseBoolean( p_weakkey );
        final boolean l_multivalue = Boolean.parseBoolean( p_multivalue );
        final boolean l_derived = Boolean.parseBoolean( p_derived );

        return EModelStorage.INSTANCE.apply( p_model ).<IErd>raw().addAttributeToEntity( p_name, l_key, l_weakkey, l_multivalue, l_derived, p_entity )
                                                                  .serialize();
    }

    /**
     * create new relationship
     *
     * @param p_model
     * @param p_name
     * @param p_description
     * @param p_recursive
     * @param p_identifying
     * @return model
     */
    @RequestMapping( value = "/relationship/{model}/{name}/{description}/{recursive}/{identifying}", produces = "application/json" )
    public Object createRelationship( @PathVariable( "model" ) final String p_model, @PathVariable( "name" ) final String p_name,
                                      @PathVariable( "description" ) final String p_description, @PathVariable( "recursive" ) final String p_recursive,
                                      @PathVariable( "identifying" ) final String p_identifying
    )
    {
        final boolean l_recursive = Boolean.parseBoolean( p_recursive );
        final boolean l_identifying = Boolean.parseBoolean( p_identifying );

        return EModelStorage.INSTANCE.apply( p_model ).<IErd>raw().addRelationship( p_name, p_description, l_recursive, l_identifying ).serialize();
    }

    /**
     * create new attribute and connect it to a relationship
     *
     * @param p_model
     * @param p_relationship
     * @param p_name
     * @param p_key
     * @param p_weakkey
     * @param p_multivalue
     * @param p_derived
     * @return model
     */
    @RequestMapping( value = "/connect/attribute/relationship/{model}/{relationship_id}/{name}/{key}/{weakkey}/{multival}/{derived}",
                     produces = "application/json" )
    public Object connectAttributeToRelationship( @PathVariable( "model" ) final String p_model, @PathVariable( "relationship_id" ) final String p_relationship,
                                                  @PathVariable( "name" ) final String p_name, @PathVariable( "key" ) final String p_key,
                                                  @PathVariable( "weakkey" ) final String p_weakkey, @PathVariable( "multival" ) final String p_multivalue,
                                                  @PathVariable( "derived" ) final String p_derived
    )
    {
        final boolean l_key = Boolean.parseBoolean( p_key );
        final boolean l_weakkey = Boolean.parseBoolean( p_weakkey );
        final boolean l_multivalue = Boolean.parseBoolean( p_multivalue );
        final boolean l_derived = Boolean.parseBoolean( p_derived );

        return EModelStorage.INSTANCE.apply( p_model ).<IErd>raw().addAttributeToRelationship(
            p_name, l_key, l_weakkey, l_multivalue, l_derived, p_relationship ).serialize();
    }

    /**
     * make connection between entity and relationship incl. cardinality
     *
     * @param p_model
     * @param p_name
     * @param p_entity
     * @param p_relationship
     * @param p_cardinality
     * @return model
     */
    @RequestMapping( value = "/connection/{model}/{name}/{entity}/{relationship}/{cardinality}", produces = "application/json" )
    public Object connection( @PathVariable( "model" ) final String p_model, @PathVariable( "name" ) final String p_name,
                              @PathVariable( "entity" ) final String p_entity, @PathVariable( "relationship" ) final String p_relationship,
                              @PathVariable( "cardinality" ) final String p_cardinality
    )
    {
        return EModelStorage.INSTANCE.apply( p_model ).<IErd>raw().connectEntityWithRelationship( p_name, p_entity, p_relationship, p_cardinality ).serialize();
    }

    /**
     * read graphical model from post body and parse it
     *
     * @param p_body
     * @return model
     */
    @RequestMapping( value = "/read", produces = "application/json" )
    public Object createFromPostRequest( @RequestBody final String p_body )
    {
        return EModelStorage.INSTANCE.apply( new CErdLoader().loadJson( p_body ) ).serialize();
    }
}
