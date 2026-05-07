package br.com.joaogabriel.lumio.service.impl;

import br.com.joaogabriel.lumio.client.BrasilAPIClient;
import br.com.joaogabriel.lumio.client.dto.response.BrasilAddressResponse;
import br.com.joaogabriel.lumio.exception.IntegrationServiceException;
import br.com.joaogabriel.lumio.exception.InvalidZipCodeException;
import br.com.joaogabriel.lumio.exception.ZipCodeNotFoundException;
import br.com.joaogabriel.lumio.service.ZipCodeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ZipCodeServiceImpl implements ZipCodeService {
    private static final Logger LOG = LoggerFactory.getLogger(ZipCodeServiceImpl.class);
    private final BrasilAPIClient brasilAPIClient;

    public ZipCodeServiceImpl(@RestClient BrasilAPIClient brasilAPIClient) {
        this.brasilAPIClient = brasilAPIClient;
    }

    @Override
    public BrasilAddressResponse getAddressByCep(String cep) {
        LOG.info("Getting address by cep {}", cep);
        try (Response response = this.brasilAPIClient.getAddress(cep)) {
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                LOG.info("Successfully retrieved address by cep {}", cep);
                return response.readEntity(BrasilAddressResponse.class);
            } else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                throw new ZipCodeNotFoundException("Cep not found. Cep: " + cep);
            }
            throw new IntegrationServiceException("ZipCode service is temporarily unavailable.");
        } catch (WebApplicationException | ProcessingException e) {
            throw new InvalidZipCodeException("Failed to retrieve address by cep " + cep, e);
        } catch (Exception e) {
            LOG.error("Unexpected error while retrieving address by cep {}", cep, e);
            throw e;
        }
    }
}
