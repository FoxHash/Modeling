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

package de.tu_clausthal.in.mec.modeling.validation.json_schema;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.everit.json.schema.ValidationException;
import org.json.JSONObject;


/**
 * EMPTY
 * //TODO
 */
public interface IJsonSchema
{
    /**
     * method to validate a given json object with the defined schema
     *
     * @param p_jsonobject json object which will be validated
     * @return bool result of the schema validation
     */
    boolean validateJson( @NonNull final JSONObject p_jsonobject );

    /**
     * method to return the exception to generate error messages
     *
     * @return exception
     */
    ValidationException getException();
}
