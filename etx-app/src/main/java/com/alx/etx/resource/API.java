package com.alx.etx.resource;

public class API {

    public static String ID_PATH = "/{id}";
    public static String BLANK_PATH = "";

    public static String COORDINATIONS_PATH = "/coordinations";
    public static String PARTICIPANTS_PATH = COORDINATIONS_PATH.concat("/{cid}").concat("/participants");

}
