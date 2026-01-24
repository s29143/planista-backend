package edu.pjatk.planista.integrations.handlers;

import jakarta.xml.ws.handler.Handler;
import jakarta.xml.ws.handler.HandlerResolver;
import jakarta.xml.ws.handler.PortInfo;

import java.util.List;

public class SoapHandlerResolver implements HandlerResolver {
    @Override
    public List<Handler> getHandlerChain(PortInfo portInfo) {
        return List.of(new SoapMessageHandler());
    }
}