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

package de.tu_clausthal.in.mec.modeling.model.erd;

import de.tu_clausthal.in.mec.modeling.ErdGrammarBaseListener;
import de.tu_clausthal.in.mec.modeling.ErdGrammarLexer;
import de.tu_clausthal.in.mec.modeling.ErdGrammarParser;
import de.tu_clausthal.in.mec.modeling.model.storage.EModelStorage;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.HashMap;


/**
 * Implementation of data processing with AntLR4. Read-in data is formatted in json and
 * will processed here. After processing the graphical model will be available in the
 * predefined java data model.
 */
public class CErdLoader implements IErdLoader
{

    @Override
    public String loadJson( @NonNull final String p_input )
    {


        final ErdGrammarLexer l_lexer = new ErdGrammarLexer( CharStreams.fromString( p_input ) );
        final CommonTokenStream l_tokenstream = new CommonTokenStream( l_lexer );
        final ErdGrammarParser l_parser = new ErdGrammarParser( l_tokenstream );
        l_parser.setBuildParseTree( true );

        final ParseTree l_tree = l_parser.json();
        final ParseTreeWalker l_parsetreewalker = new ParseTreeWalker();

        final CErdProcessor l_erdprocessor = new CErdProcessor();
        l_parsetreewalker.walk( l_erdprocessor, l_tree );

        return l_erdprocessor.m_id.m_modelid;

    }

    /**
     * Main processor which handles the data.
     */
    //Checkstyle:OFF:ParameterName
    //Checkstyle:OFF:MultipleStringLiterals
    private static final class CErdProcessor extends ErdGrammarBaseListener
    {

        // model id
        private CErdProcessorSettings m_id;

        // collect basic information's of an entity
        private final HashMap<String, String> m_ecollector;

        // collect assigned attribute information's from the current entity
        private final HashMap<String, String> m_eattrcollector;

        // collect basic information's of a relationship
        private final HashMap<String, String> m_rcollector;

        // collect assigned attribute informationÄs from the current relationship
        private final HashMap<String, String> m_rattrcollector;

        // collect information's of a connection
        private final HashMap<String, String> m_ccollector;

        private CErdProcessor()
        {
            m_ecollector = new HashMap<>();
            m_eattrcollector = new HashMap<>();

            m_rcollector = new HashMap<>();
            m_rattrcollector = new HashMap<>();

            m_ccollector = new HashMap<>();
        }

        @Override
        public void enterModelId( final ErdGrammarParser.ModelIdContext ctx )
        {
            m_id = new CErdProcessorSettings( ctx.getChild( 2 ).getText().replaceAll( "\"", "" ) );
            EModelStorage.INSTANCE.add( new CErd( m_id.m_modelid ) );
        }

        @Override
        public void exitEObject( final ErdGrammarParser.EObjectContext ctx )
        {
            // purge collector
            m_ecollector.clear();
        }

        @Override
        public void exitRObject( final ErdGrammarParser.RObjectContext ctx )
        {
            // purge collector
            m_rcollector.clear();
        }

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        // entity processing
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        @Override
        public void enterEPairId( final ErdGrammarParser.EPairIdContext ctx )
        {
            m_ecollector.put( "id", ctx.getChild( 2 ).getText().replaceAll( "\"", "" ) );
        }

        @Override
        public void enterEPairWeakEntity( final ErdGrammarParser.EPairWeakEntityContext ctx )
        {
            m_ecollector.put( "weakEntity", ctx.getChild( 2 ).getText().replaceAll( "\"", "" ) );
        }

        @Override
        public void enterEAttrArray( final ErdGrammarParser.EAttrArrayContext ctx )
        {

            final String l_id;
            final boolean l_weakentity;

            if ( m_ecollector.get( "id" ) == null )
            {
                throw new RuntimeException( "you have an error in your request!" );
            }

            l_id = m_ecollector.get( "id" );
            l_weakentity = m_ecollector.get( "weakEntity" ) != null && Boolean.parseBoolean( m_ecollector.get( "weakEntity" ) );

            EModelStorage.INSTANCE.apply( m_id.m_modelid ).<IErd>raw().addEntity( l_id, l_weakentity );

        }

        @Override
        public void exitEAttrArray( final ErdGrammarParser.EAttrArrayContext ctx )
        {
            m_eattrcollector.clear();
        }

        @Override
        public void enterEAttrPairId( final ErdGrammarParser.EAttrPairIdContext ctx )
        {
            m_eattrcollector.put( "id", ctx.getChild( 2 ).getText().replaceAll( "\"", "" ) );
        }

        @Override
        public void enterEAttrPairKey( final ErdGrammarParser.EAttrPairKeyContext ctx )
        {
            m_eattrcollector.put( "key", ctx.getChild( 2 ).getText().replaceAll( "\"", "" ) );
        }

        @Override
        public void enterEAttrPairWeakKey( final ErdGrammarParser.EAttrPairWeakKeyContext ctx )
        {
            m_eattrcollector.put( "weakKey", ctx.getChild( 2 ).getText().replaceAll( "\"", "" ) );
        }

        @Override
        public void enterEAttrPairMultiVal( final ErdGrammarParser.EAttrPairMultiValContext ctx )
        {
            m_eattrcollector.put( "multiVal", ctx.getChild( 2 ).getText().replaceAll( "\"", "" ) );
        }

        @Override
        public void enterEAttrPairDerivedVal( final ErdGrammarParser.EAttrPairDerivedValContext ctx )
        {
            m_eattrcollector.put( "derivedVal", ctx.getChild( 2 ).getText().replaceAll( "\"", "" ) );
        }

        @Override
        public void exitEAttrPairPair( final ErdGrammarParser.EAttrPairPairContext ctx )
        {
            final String l_entity;
            final String l_id;
            final boolean l_key;
            final boolean l_weakkey;
            final boolean l_multival;
            final boolean l_derivedval;

            if ( ( m_eattrcollector.get( "id" ) == null ) || ( m_ecollector.get( "id" ) == null ) )
            {
                throw new RuntimeException( "you have an error in your request!" );
            }

            l_entity = m_ecollector.get( "id" );
            l_id = m_eattrcollector.get( "id" );
            l_key = m_eattrcollector.get( "key" ) != null && Boolean.parseBoolean( m_eattrcollector.get( "key" ) );
            l_weakkey = m_eattrcollector.get( "weakKey" ) != null && Boolean.parseBoolean( m_eattrcollector.get( "weakKey" ) );
            l_multival = m_eattrcollector.get( "multiVal" ) != null && Boolean.parseBoolean( m_eattrcollector.get( "multiVal" ) );
            l_derivedval = m_eattrcollector.get( "derivedVal" ) != null && Boolean.parseBoolean( m_eattrcollector.get( "derivedVal" ) );

            EModelStorage.INSTANCE.apply( m_id.m_modelid ).<IErd>raw().addAttributeToEntity( l_id, l_key, l_weakkey, l_multival, l_derivedval, l_entity );
        }

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        // relationship processing
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        @Override
        public void enterRPairId( final ErdGrammarParser.RPairIdContext ctx )
        {
            m_rcollector.put( "id", ctx.getChild( 2 ).getText().replaceAll( "\"", "" ) );
        }

        @Override
        public void enterRPairDescription( final ErdGrammarParser.RPairDescriptionContext ctx )
        {
            m_rcollector.put( "description", ctx.getChild( 2 ).getText().replaceAll( "\"", "" ) );
        }

        @Override
        public void enterRPairRecursive( final ErdGrammarParser.RPairRecursiveContext ctx )
        {
            m_rcollector.put( "recursive", ctx.getChild( 2 ).getText().replaceAll( "\"", "" ) );
        }

        @Override
        public void enterRPairIdentifying( final ErdGrammarParser.RPairIdentifyingContext ctx )
        {
            m_rcollector.put( "identifying", ctx.getChild( 2 ).getText().replaceAll( "\"", "" ) );
        }

        @Override
        public void enterRAttrArray( final ErdGrammarParser.RAttrArrayContext ctx )
        {
            final String l_id;
            final String l_description;
            final boolean l_recursive;
            final boolean l_identifying;

            if ( m_rcollector.get( "id" ) == null )
            {
                throw new RuntimeException( "you have an error in your request" );
            }

            l_id = m_rcollector.get( "id" );
            l_description = m_rcollector.get( "description" );
            l_recursive = m_rcollector.get( "recursive" ) != null && Boolean.parseBoolean( m_rcollector.get( "recursive" ) );
            l_identifying = m_rcollector.get( "identifying" ) != null && Boolean.parseBoolean( m_rcollector.get( "identifying" ) );

            EModelStorage.INSTANCE.apply( m_id.m_modelid ).<IErd>raw().addRelationship( l_id, l_description, l_recursive, l_identifying );
        }

        @Override
        public void exitRAttrArray( final ErdGrammarParser.RAttrArrayContext ctx )
        {
            m_rattrcollector.clear();
        }

        @Override
        public void enterRAttrPairId( final ErdGrammarParser.RAttrPairIdContext ctx )
        {
            m_rattrcollector.put( "id", ctx.getChild( 2 ).getText().replaceAll( "\"", "" ) );
        }

        @Override
        public void enterRAttrPairKey( final ErdGrammarParser.RAttrPairKeyContext ctx )
        {
            m_rattrcollector.put( "key", ctx.getChild( 2 ).getText().replaceAll( "\"", "" ) );
        }

        @Override
        public void enterRAttrPairWeakKey( final ErdGrammarParser.RAttrPairWeakKeyContext ctx )
        {
            m_rattrcollector.put( "weakKey", ctx.getChild( 2 ).getText().replaceAll( "\"", "" ) );
        }

        @Override
        public void enterRAttrPairMultiVal( final ErdGrammarParser.RAttrPairMultiValContext ctx )
        {
            m_rattrcollector.put( "multiVal", ctx.getChild( 2 ).getText().replaceAll( "\"", "" ) );
        }

        @Override
        public void enterRAttrPairDerivedVal( final ErdGrammarParser.RAttrPairDerivedValContext ctx )
        {
            m_rattrcollector.put( "derivedVal", ctx.getChild( 2 ).getText().replaceAll( "\"", "" ) );
        }

        @Override
        public void exitRAttrPairPair( final ErdGrammarParser.RAttrPairPairContext ctx )
        {
            final String l_relationship;
            final String l_id;
            final boolean l_key;
            final boolean l_weakkey;
            final boolean l_multival;
            final boolean l_derivedval;

            if ( ( m_rattrcollector.get( "id" ) == null ) || ( m_rcollector.get( "id" ) == null ) )
            {
                throw new RuntimeException( "you have an error in your request!" );
            }

            l_relationship = m_rcollector.get( "id" );
            l_id = m_rattrcollector.get( "id" );
            l_key = m_rattrcollector.get( "key" ) != null && Boolean.parseBoolean( m_rattrcollector.get( "key" ) );
            l_weakkey = m_rattrcollector.get( "weakKey" ) != null && Boolean.parseBoolean( m_rattrcollector.get( "weakKey" ) );
            l_multival = m_rattrcollector.get( "multiVal" ) != null && Boolean.parseBoolean( m_rattrcollector.get( "multiVal" ) );
            l_derivedval = m_rattrcollector.get( "derivedVal" ) != null && Boolean.parseBoolean( m_rattrcollector.get( "derivedVal" ) );

            EModelStorage.INSTANCE.apply( m_id.m_modelid ).<IErd>raw().addAttributeToRelationship(
                l_id, l_key, l_weakkey, l_multival, l_derivedval, l_relationship );
        }

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        // connection processing
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -



        @Override
        public void enterCId( final ErdGrammarParser.CIdContext ctx )
        {
            m_ccollector.put( "id", ctx.getChild( 2 ).getText().replaceAll( "\"", "" ) );
        }

        @Override
        public void enterCFrom( final ErdGrammarParser.CFromContext ctx )
        {
            m_ccollector.put( "from", ctx.getChild( 2 ).getText().replaceAll( "\"", "" ) );
        }

        @Override
        public void enterCTo( final ErdGrammarParser.CToContext ctx )
        {
            m_ccollector.put( "to", ctx.getChild( 2 ).getText().replaceAll( "\"", "" ) );
        }

        @Override
        public void enterCCardinality( final ErdGrammarParser.CCardinalityContext ctx )
        {
            m_ccollector.put( "cardinality", ctx.getChild( 2 ).getText().replaceAll( "\"", "" ) );
        }

        @Override
        public void exitCPairPair( final ErdGrammarParser.CPairPairContext ctx )
        {
            // add connection
            final String l_id;
            final String l_from;
            final String l_to;
            final String l_cardinality;

            if ( m_ccollector.get( "id" ) == null )
            {
                throw new RuntimeException( "you have an error in your request" );
            }

            l_id = m_ccollector.get( "id" );
            l_from = m_ccollector.get( "from" );
            l_to = m_ccollector.get( "to" );
            l_cardinality = m_ccollector.get( "cardinality" );

            EModelStorage.INSTANCE.apply( m_id.m_modelid ).<IErd>raw().connectEntityWithRelationship( l_id, l_from, l_to, l_cardinality );

            // purge collector
            m_ccollector.clear();
        }
    }
    //Checkstyle:ON:MultipleStringLiterals
    //Checkstyle:ON:ParameterName

    /**
     * Helping class to store the current model id for the processing procedure.
     */
    private static final class CErdProcessorSettings
    {

        private final String m_modelid;

        private CErdProcessorSettings( @NonNull final String p_modelid )
        {
            m_modelid = p_modelid;
        }

    }

}
