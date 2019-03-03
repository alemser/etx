package com.alx.etx.resource;

public class API {

    public static String ID_PATH_VAR = "id";

    public static String COORDINATIONS_PATH = "/coordinations";
    public static String ID_PATH = "/{".concat(ID_PATH_VAR).concat("}");
    public static String COORDINATIONS_ID_PATH = COORDINATIONS_PATH.concat(ID_PATH);

}
