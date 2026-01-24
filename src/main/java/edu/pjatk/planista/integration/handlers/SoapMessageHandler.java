package edu.pjatk.planista.integration.handlers;

import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;

import javax.xml.namespace.QName;
import java.util.*;

public class SoapMessageHandler implements SOAPHandler<SOAPMessageContext> {

    public static volatile String sessionSid = "";

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (Boolean.TRUE.equals(outbound) && sessionSid != null && !sessionSid.isBlank()) {
            @SuppressWarnings("unchecked")
            Map<String, List<String>> headers =
                    (Map<String, List<String>>) context.get(MessageContext.HTTP_REQUEST_HEADERS);

            if (headers == null) headers = new HashMap<>();
            headers.put("sid", List.of(sessionSid));

            context.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
            context.setScope(MessageContext.HTTP_REQUEST_HEADERS, MessageContext.Scope.APPLICATION);
        }
        return true;
    }

    @Override public boolean handleFault(SOAPMessageContext context) { return true; }
    @Override public void close(MessageContext context) {}
    @Override public Set<QName> getHeaders() { return Collections.emptySet(); }
}