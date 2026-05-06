package br.com.joaogabriel.lumio.service.impl;

import br.com.joaogabriel.lumio.client.IpClient;
import br.com.joaogabriel.lumio.client.dto.response.IpResponse;
import br.com.joaogabriel.lumio.model.entity.UserProvisioning;
import br.com.joaogabriel.lumio.service.IpGeolocationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@ApplicationScoped
public class IpGeolocationServiceImpl implements IpGeolocationService {
    private final static Logger LOG = LoggerFactory.getLogger(IpGeolocationServiceImpl.class);
    private final IpClient ipClient;

    public IpGeolocationServiceImpl(@RestClient IpClient ipClient) {
        this.ipClient = ipClient;
    }

    @Override
    public Optional<IpResponse> getDetails(UserProvisioning provisioning) {
        LOG.info("Trying to get details for ip geolocation service from user: {}", provisioning.getUsername());
        final String ip = provisioning.getIp();
        if (!isLocal(ip)) {
            try (Response response = ipClient.getDetails(ip)) {
                if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                    IpResponse ipResponse = response.readEntity(IpResponse.class);
                    if (ipResponse.status().equals("success")) {
                        LOG.info("Details obtained successfully from ip: {} ", ip);
                        return Optional.of(ipResponse);
                    } else {
                        LOG.info("Details not obtained successfully from ip: {} ", ip);
                        return Optional.empty();
                    }

                } else {
                    LOG.warn("Failed to get ip details from ip: {}", ip);
                    return Optional.empty();
                }
            } catch (Exception ex) {
                LOG.error("Failed to get ip details from ip: {}", ip, ex);
                return Optional.empty();
            }
        } else {
            LOG.warn("Ip address is Local/Private, skipping Geolocation service.");
            return Optional.empty();
        }
    }

    private boolean isLocal(String ip) {
        if (ip == null) return true;
        return ip.startsWith("127.") ||
                ip.startsWith("192.168.") ||
                ip.startsWith("10.") ||
                ip.startsWith("172.16.") ||
                ip.equals("0:0:0:0:0:0:0:1");
    }
}
