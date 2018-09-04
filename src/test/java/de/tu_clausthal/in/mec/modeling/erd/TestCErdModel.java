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

package de.tu_clausthal.in.mec.modeling.erd;

import de.tu_clausthal.in.mec.modeling.model.erd.CErd;
import de.tu_clausthal.in.mec.modeling.model.erd.IErd;
import org.junit.Assert;
import org.junit.Test;


/**
 * Test cases for reading ERD models
 */
public final class TestCErdModel
{

    private static final String T_MODEL = "model";
    private static final String T_ENTITYCUSTOMER = "Customer";
    private static final String T_ENTITYPRIVATECUSTOMER = "Private-Customer";
    private static final String T_ENTITYBUSINESSCUSTOMER = "Business-Customer";
    private static final String T_ENTITYITEMS = "Items";
    private static final String T_ATTRIBUTENAME = "Name";
    private static final String T_ATTRIBUTEID = "ID-No";
    private static final String T_CARDINALITY11 = "1:1";
    private static final String T_CARDINALITY1N = "1:n";
    private static final String T_RELATIONSHIP = "rel01";
    private static final String T_ISARELATIONSHIP = "isa-rel01";

    /**
     * create minimal model with one entity and one recursive relationship
     *
     * @throws Exception if test failed
     */
    @Test
    public void minimalRecursiveModel() throws Exception
    {

        final IErd l_erd = new CErd( T_MODEL );

        l_erd.addEntity( T_ENTITYCUSTOMER, false );
        l_erd.addAttributeToEntity( T_ATTRIBUTENAME, false, false, false, false, false, T_ENTITYCUSTOMER );
        l_erd.addAttributeToEntity( T_ATTRIBUTEID, false, false, false, false, false, T_ENTITYCUSTOMER );

        l_erd.addRelationship( T_RELATIONSHIP, "testing" );
        l_erd.connectEntityWithRelationship( "con01", T_ENTITYCUSTOMER, T_RELATIONSHIP, T_CARDINALITY11 );
        l_erd.connectEntityWithRelationship( "con02", T_ENTITYCUSTOMER, T_RELATIONSHIP, T_CARDINALITY11 );

        Assert.assertTrue( l_erd.check() );

    }

    /**
     * create minimal model with 2 entities and one relationship
     *
     * @throws Exception if test failed
     */
    @Test
    public void minimalModel() throws Exception
    {

        final IErd l_erd = new CErd( T_MODEL );

        l_erd.addEntity( T_ENTITYCUSTOMER, false );
        l_erd.addAttributeToEntity( T_ATTRIBUTENAME, false, false, false, false, false, T_ENTITYCUSTOMER );
        l_erd.addAttributeToEntity( T_ATTRIBUTEID, false, false, false, false, false, T_ENTITYCUSTOMER );

        l_erd.addEntity( T_ENTITYITEMS, false );
        l_erd.addAttributeToEntity( "Item-Name", true, false, false, false, false, T_ENTITYITEMS );
        l_erd.addAttributeToEntity( "Item-Price", false, false, false, false, false, T_ENTITYITEMS );

        l_erd.addRelationship( T_RELATIONSHIP, "purchase" );
        l_erd.connectEntityWithRelationship( "con01", T_ENTITYCUSTOMER, T_RELATIONSHIP, T_CARDINALITY11 );
        l_erd.connectEntityWithRelationship( "con02", "Items", T_RELATIONSHIP, T_CARDINALITY11 );

        Assert.assertTrue( l_erd.check() );

    }

    /**
     * check if the validator recognize the model which make no sense, but is syntactically and semantically correct
     * model description: model can also contain one single entity with no connection to a relationship
     *
     * @throws Exception if test failed
     */
    @Test
    public void minimalModelOneEntity() throws Exception
    {

        final IErd l_erd = new CErd( T_MODEL );

        l_erd.addEntity( T_ENTITYCUSTOMER, false );
        l_erd.addAttributeToEntity( T_ATTRIBUTENAME, false, false, false, false, false, T_ENTITYCUSTOMER );
        l_erd.addAttributeToEntity( T_ATTRIBUTEID, false, false, false, false, false, T_ENTITYCUSTOMER );

        Assert.assertTrue( l_erd.check() );

    }

    /**
     * check if the validator recognize the model with the is-a relationship which is correct set
     *
     * @throws Exception if test failed
     */
    @Test
    public void isaRelationshipTest() throws Exception
    {

        final IErd l_erd = new CErd( T_MODEL );

        l_erd.addEntity( T_ENTITYPRIVATECUSTOMER, false );
        l_erd.addAttributeToEntity( "Private-Address", false, false, false, false, false, T_ENTITYPRIVATECUSTOMER );
        l_erd.addAttributeToEntity( "Day of Birth", false, false, false, false, false, T_ENTITYPRIVATECUSTOMER );

        l_erd.addEntity( T_ENTITYBUSINESSCUSTOMER, false );
        l_erd.addAttributeToEntity( "Discount", false, false, false, false, false, T_ENTITYBUSINESSCUSTOMER );
        l_erd.addAttributeToEntity( "Company", false, false, false, false, false, T_ENTITYBUSINESSCUSTOMER );

        l_erd.addEntity( T_ENTITYCUSTOMER, false );
        l_erd.addAttributeToEntity( T_ATTRIBUTENAME, false, false, false, false, false, T_ENTITYCUSTOMER );
        l_erd.addAttributeToEntity( T_ATTRIBUTEID, false, false, false, false, false, T_ENTITYCUSTOMER );

        l_erd.addEntity( T_ENTITYITEMS, false );
        l_erd.addAttributeToEntity( "Item-Name", true, false, false, false, false, T_ENTITYITEMS );
        l_erd.addAttributeToEntity( "Item-Price", false, false, false, false, false, T_ENTITYITEMS );

        l_erd.addRelationship( T_RELATIONSHIP, "purchase" );
        l_erd.connectEntityWithRelationship( "con01", T_ENTITYCUSTOMER, T_RELATIONSHIP, T_CARDINALITY11 );
        l_erd.connectEntityWithRelationship( "con02", "Items", T_RELATIONSHIP, T_CARDINALITY1N );

        // is-a relationship
        l_erd.addISARelationship( T_ISARELATIONSHIP );
        l_erd.connectParentEntityWithISARelationship( "isa-con01", T_ENTITYCUSTOMER, T_ISARELATIONSHIP );
        l_erd.connectChildEntityWithISARelationship( "isa-con02", T_ENTITYPRIVATECUSTOMER, T_ISARELATIONSHIP );
        l_erd.connectChildEntityWithISARelationship( "isa-con03", T_ENTITYBUSINESSCUSTOMER, T_ISARELATIONSHIP );

        Assert.assertTrue( l_erd.check() );

    }

    /**
     * check if the validator recognize the wrong model
     * model description: only one entity and relationship - no recursive connection -> relationship is not valid
     *
     * @throws Exception if test failed
     */
    @Test
    public void nonValidModelT1() throws Exception
    {

        final IErd l_erd = new CErd( T_MODEL );

        l_erd.addEntity( T_ENTITYCUSTOMER, false );
        l_erd.addAttributeToEntity( T_ATTRIBUTENAME, false, false, false, false, false, T_ENTITYCUSTOMER );
        l_erd.addAttributeToEntity( T_ATTRIBUTEID, false, false, false, false, false, T_ENTITYCUSTOMER );

        l_erd.addRelationship( T_RELATIONSHIP, "testing" );
        l_erd.connectEntityWithRelationship( "con02", T_ENTITYCUSTOMER, T_RELATIONSHIP, T_CARDINALITY11 );

        Assert.assertFalse( l_erd.check() );

        System.out.println( new Object()
        {
        }.getClass().getEnclosingMethod().getName() + " - Results: Errors found in:" );
        System.out.println( l_erd.checkResult().toString() );

    }

    /**
     * check if the validator recognize the wrong model
     * model description: one entity is connected to a is-a relationship and a normal relationship - this is not allowed
     *
     * @throws Exception if test failed
     */
    @Test
    public void nonValidModelT2() throws Exception
    {

        final IErd l_erd = new CErd( T_MODEL );

        l_erd.addEntity( T_ENTITYPRIVATECUSTOMER, false );
        l_erd.addAttributeToEntity( "Private-Address", false, false, false, false, false, T_ENTITYPRIVATECUSTOMER );
        l_erd.addAttributeToEntity( "Day of Birth", false, false, false, false, false, T_ENTITYPRIVATECUSTOMER );

        l_erd.addEntity( T_ENTITYBUSINESSCUSTOMER, false );
        l_erd.addAttributeToEntity( "Discount", false, false, false, false, false, T_ENTITYBUSINESSCUSTOMER );
        l_erd.addAttributeToEntity( "Company", false, false, false, false, false, T_ENTITYBUSINESSCUSTOMER );

        l_erd.addEntity( T_ENTITYCUSTOMER, false );
        l_erd.addAttributeToEntity( "Name", false, false, false, false, false, T_ENTITYCUSTOMER );
        l_erd.addAttributeToEntity( "ID-No", false, false, false, false, false, T_ENTITYCUSTOMER );

        l_erd.addEntity( T_ENTITYITEMS, false );
        l_erd.addAttributeToEntity( "Item-Name", true, false, false, false, false, T_ENTITYITEMS );
        l_erd.addAttributeToEntity( "Item-Price", false, false, false, false, false, T_ENTITYITEMS );

        l_erd.addRelationship( T_RELATIONSHIP, "purchase" );
        l_erd.connectEntityWithRelationship( "con01", T_ENTITYCUSTOMER, T_RELATIONSHIP, T_CARDINALITY11 );
        l_erd.connectEntityWithRelationship( "con02", "Items", T_RELATIONSHIP, T_CARDINALITY1N );

        l_erd.addRelationship( "rel02", "purchase" );
        l_erd.connectEntityWithRelationship( "con03", T_ENTITYBUSINESSCUSTOMER, "rel02", T_CARDINALITY11 );
        l_erd.connectEntityWithRelationship( "con04", "Items", "rel02", T_CARDINALITY1N );

        // is-a relationship
        l_erd.addISARelationship( T_ISARELATIONSHIP );
        l_erd.connectParentEntityWithISARelationship( "isa-con01", T_ENTITYCUSTOMER, T_ISARELATIONSHIP );
        l_erd.connectChildEntityWithISARelationship( "isa-con02", T_ENTITYPRIVATECUSTOMER, T_ISARELATIONSHIP );
        l_erd.connectChildEntityWithISARelationship( "isa-con03", T_ENTITYBUSINESSCUSTOMER, T_ISARELATIONSHIP );

        Assert.assertFalse( l_erd.check() );

        System.out.println( new Object()
        {
        }.getClass().getEnclosingMethod().getName() + " - Results: Errors found in:" );
        System.out.println( l_erd.checkResult().toString() );

    }

}
