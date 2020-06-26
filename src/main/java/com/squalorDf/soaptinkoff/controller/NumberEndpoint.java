package com.squalorDf.soaptinkoff.controller;

import com.squalorDf.soaptinkoff.database.NumberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.squalodf.ws.numbers.FindNumberRequest;
import org.squalodf.ws.numbers.FindNumberResponse;

/**
 * Endpoint controller class for SOAP application.
 */
@Endpoint
public class NumberEndpoint {

    Logger logger = LoggerFactory.getLogger(NumberEndpoint.class);

    /**
     * Default namespace of SOAP web-service.
     */
    private static final String NAMESPACE_URI = "http://squalodf.org/ws/numbers";

    private NumberRepository numberRepository;

    @Autowired
    public NumberEndpoint(NumberRepository numberRepository) {
        this.numberRepository = numberRepository;
    }

    /**
     * Method of search number and response formation to SOAP.
     * @param request request containing number to search.
     * @return response with results of searching of number.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "findNumberRequest")
    @ResponsePayload
    public FindNumberResponse getNumber(@RequestPayload FindNumberRequest request) {

        logger.info("Start handling request to find number");

        FindNumberResponse response = new FindNumberResponse();

        response.setResult(numberRepository.findNumber(request.getNumber()));

        return response;
    }
}
