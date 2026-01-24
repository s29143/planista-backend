package edu.pjatk.planista.integration.services;

import edu.pjatk.gusregon.soap.IUslugaBIRzewnPubl;
import edu.pjatk.gusregon.soap.ObjectFactory;
import edu.pjatk.gusregon.soap.ParametryWyszukiwania;
import edu.pjatk.gusregon.soap.UslugaBIRzewnPubl;
import edu.pjatk.planista.integration.handlers.SoapHandlerResolver;
import edu.pjatk.planista.integration.handlers.SoapMessageHandler;
import edu.pjatk.planista.integration.models.CompanyFromRegonDto;
import edu.pjatk.planista.integration.models.GusRegonProperties;
import jakarta.persistence.EntityNotFoundException;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.soap.AddressingFeature;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

@Service
@RequiredArgsConstructor
public class GusRegonService {

    private final GusRegonProperties gusRegonProperties;

    public CompanyFromRegonDto findByNip(String nip) {
        IUslugaBIRzewnPubl port = createPort();

        ensureLoggedIn(port);

        ParametryWyszukiwania p = new ParametryWyszukiwania();
        p.setNip(wrap(nip));

        String raportXml = port.daneSzukajPodmioty(p);

        if (raportXml == null || raportXml.isBlank() || looksLikeSessionExpired(raportXml)) {
            SoapMessageHandler.sessionSid = "";
            ensureLoggedIn(port);
            raportXml = port.daneSzukajPodmioty(p);
        }

        return parseFirstCompany(raportXml);
    }

    private IUslugaBIRzewnPubl createPort() {
        UslugaBIRzewnPubl service = new UslugaBIRzewnPubl();
        service.setHandlerResolver(new SoapHandlerResolver());

        IUslugaBIRzewnPubl port = service.getE3(new AddressingFeature());

        BindingProvider bp = (BindingProvider) port;
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, gusRegonProperties.serviceUrl());

        bp.getRequestContext().put("com.sun.xml.ws.connect.timeout", 5000);
        bp.getRequestContext().put("com.sun.xml.ws.request.timeout", 15000);

        return port;
    }

    private void ensureLoggedIn(IUslugaBIRzewnPubl port) {
        if (SoapMessageHandler.sessionSid != null && !SoapMessageHandler.sessionSid.isBlank()) return;
        String sid = port.zaloguj(gusRegonProperties.apiKey());
        SoapMessageHandler.sessionSid = sid;
    }

    private JAXBElement<String> wrap(String value) {
        return new ObjectFactory().createParametryWyszukiwaniaNip(value);
    }

    private boolean looksLikeSessionExpired(String xml) {
        String s = xml.toLowerCase();
        return s.contains("brak aktywnej sesji") || s.contains("zaloguj") || s.contains("session");
    }

    private CompanyFromRegonDto parseFirstCompany(String raportXml) {
        try {
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new StringReader(raportXml)));

            CompanyFromRegonDto dto = new CompanyFromRegonDto();
            dto.setRegon(text(doc, "Regon"));
            dto.setNip(text(doc, "Nip"));
            dto.setName(text(doc, "Nazwa"));
            dto.setCity(text(doc, "Miejscowosc"));
            dto.setStreet(text(doc, "Ulica"));
            dto.setBuildingNo(text(doc, "NrNieruchomosci"));
            dto.setApartmentNo(text(doc, "NrLokalu"));
            dto.setPostalCode(text(doc, "KodPocztowy"));
            dto.setPostOffice(text(doc, "Poczta"));

            if ((dto.getRegon() == null || dto.getRegon().isBlank())
                    && (dto.getName() == null || dto.getName().isBlank())) {
                throw new EntityNotFoundException("Company not found");
            }
            return dto;
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    private String text(Document doc, String tag) {
        NodeList nodes = doc.getElementsByTagName(tag);
        if (nodes.getLength() == 0) return null;
        var n = nodes.item(0);
        return n == null ? null : n.getTextContent();
    }
}
