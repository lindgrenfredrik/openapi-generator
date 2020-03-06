<?php

/**
 * OpenAPI Petstore
 * This spec is mainly for testing Petstore server and contains fake endpoints, models. Please do not use this for any other purpose. Special characters: \" \\
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI-Generator
 * https://openapi-generator.tech
 * Do not edit the class manually.
 *
 * Source files are located at:
 *
 * > https://github.com/OpenAPITools/openapi-generator/blob/master/modules/openapi-generator/src/main/resources/mustache/php-laravel/
 */


namespace App\Http\Controllers;

use Illuminate\Support\Facades\Request;

class AnotherFakeController extends Controller
{
    /**
     * Constructor
     */
    public function __construct()
    {
    }

    /**
     * Operation call123TestSpecialTags
     *
     * To test special tags.
     *
     *
     * @return Http response
     */
    public function call123TestSpecialTags()
    {
        $input = Request::all();

        //path params validation


        //not path params validation
        if (!isset($input['body'])) {
            throw new \InvalidArgumentException('Missing the required parameter $body when calling call123TestSpecialTags');
        }
        $body = $input['body'];


        return response('How about implementing call123TestSpecialTags as a patch method ?');
    }
}
