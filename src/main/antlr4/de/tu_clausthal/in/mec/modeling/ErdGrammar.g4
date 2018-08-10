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

grammar ErdGrammar;

json
    :   object
    |   array
    ;

object
    :   '{' pair (',' pair)* '}'
    |   '{' '}'
    ;

pair
    :   '"model"' ':'  value                                            #modelId
    |   '"entities"' ':' entities                                       #entityArray
    |   '"relationships"' ':' relationships                             #relationshipArray
    |   '"connections"' ':' connections                                 #connectionArray
//    |   value ':' value
    ;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// erd components processing
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

// entities
entities
    :   '[' eObject (',' eObject)* ']'
    |   '[' ']'
    ;

eObject
    :   '{' ePair (',' ePair)* '}'
    |   '{' '}'
    ;

ePair
    :   '"id"' ':' erdValue                                             #ePairId
    |   '"weakEntity"' ':' erdBool                                      #ePairWeakEntity
    |   '"attributes"' ':' eAttrArray                                   #ePairAttributes
    ;

eAttrArray
    :   '[' eAttrObject (',' eAttrObject)* ']'
    |   '[' ']'
    ;

eAttrObject
    :   '{' eAttrPair (',' eAttrPair)* '}'                              #eAttrPairPair
    |   '{' '}'                                                         #eAttrPairEmpty
    ;

eAttrPair
    :   '"id"' ':' erdValue                                             #eAttrPairId
    |   '"key"' ':' erdBool                                             #eAttrPairKey
    |   '"weakKey"' ':' erdBool                                         #eAttrPairWeakKey
    |   '"multiVal"' ':' erdBool                                        #eAttrPairMultiVal
    |   '"derivedVal"' ':' erdBool                                      #eAttrPairDerivedVal
    ;

// relationships
relationships
    :   '[' rObject (',' rObject)* ']'
    |   '[' ']'
    ;

rObject
    :   '{' rPair (',' rPair)* '}'
    |   '{' '}'
    ;

rPair
    :   '"id"' ':' erdValue                                             #rPairId
    |   '"description"' ':' erdValue                                    #rPairDescription
    |   '"recursive"' ':' erdBool                                       #rPairRecursive
    |   '"identifying"' ':' erdBool                                     #rPairIdentifying
    |   '"attributes"' ':' rAttrArray                                   #rPairAttributes
    ;

rAttrArray
    :   '[' rAttrObject (',' rAttrObject)* ']'
    |   '[' ']'
    ;

rAttrObject
    :   '{' rAttrPair (',' rAttrPair)* '}'                              #rAttrPairPair
    |   '{' '}'                                                         #rAttrPairEmpty
    ;

rAttrPair
    :   '"id"' ':' erdValue                                             #rAttrPairId
    |   '"key"' ':' erdBool                                             #rAttrPairKey
    |   '"weakKey"' ':' erdBool                                         #rAttrPairWeakKey
    |   '"multiVal"' ':' erdBool                                        #rAttrPairMultiVal
    |   '"derivedVal"' ':' erdBool                                      #rAttrPairDerivedVal
    ;

// connections -> edges between relationships and entities
connections
    :   '[' cObject (',' cObject)* ']'
    |   '[' ']'
    ;

cObject
    :   '{' cPair (',' cPair)* '}'                                      #cPairPair
    |   '{' '}'                                                         #cPairEmpty
    ;

cPair
    :   '"id"' ':' erdValue                                             #cId
    |   '"from"' ':' erdValue                                           #cFrom
    |   '"to"' ':' erdValue                                             #cTo
    |   '"cardinality"' ':' erdCardinality                              #cCardinality
    ;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// default rules
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

erdValue
    :   (QUOTE) (TEXT) (QUOTE)
    ;

erdBool
    :   'true'
    |   'false'
    ;

erdCardinality
    :   (QUOTE) (CARDINALITY) (QUOTE)
    ;

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// default processing
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
array
    :   '[' value (',' value)* ']'
    |   '[' ']'
    ;

value
    :   (QUOTE) (TEXT) (QUOTE)
    |   NUMBER
    |   array
    |   object
    |   'true'
    |   'false'
    |   'null'
    ;

QUOTE
    :   '"'
    ;

TEXT
    :   [0-9a-zA-ZöäüÖÄÜ]+
    ;

NUMBER
    :   NEG? INT
    ;

CARDINALITY
    :   '1:1'
    |   '1:n'
    |   'n:m'
    ;

fragment INT
    :   '0' | [1-9] [0-9]*
    ;

fragment NEG
    :   '-'
    ;

WS
    :   [ \t\n\r]+ -> skip
    ;
