package com.alx.etx.client;

interface CoordExchangeService {

    String start();

    String execute(String coordinationId, Participant participant);

}
