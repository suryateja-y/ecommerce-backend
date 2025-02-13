package com.academy.projects.ecommerce.apigateway.configurations;

import com.academy.projects.ecommerce.apigateway.dtos.UserPermissionsDto;
import com.academy.projects.ecommerce.apigateway.exceptions.TokenValidationException;
import com.academy.projects.ecommerce.apigateway.models.UserState;
import com.academy.projects.ecommerce.apigateway.services.ITokenProvider;
import lombok.NoArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@SuppressWarnings("SpellCheckingInspection")
@Component
public class Authenticate extends AbstractGatewayFilterFactory<Authenticate.Config> {

    @Value("${application.authentication-service}")
    private String authserverName;

    private final RouteConfigurator routeConfigurator;
    private final DiscoveryClient discoveryClient;
    private final ITokenProvider tokenProvider;

    @Autowired
    public Authenticate(RouteConfigurator routeConfigurator, DiscoveryClient discoveryClient, ITokenProvider tokenProvider) {
        super(Config.class);
        this.routeConfigurator = routeConfigurator;
        this.discoveryClient = discoveryClient;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if(routeConfigurator.isSecured.test(exchange.getRequest())) {
                String requestPath = exchange.getRequest().getURI().getPath();
                if(!(requestPath.matches(".*/api/v1/users/.*/register") || requestPath.matches(".*/api/v1/authentication/.*"))) {
                    if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
                        throw new TokenValidationException("Token is missing!!! Please provide the Token for Authentication and Authorization!!!");
                    String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                    if ((token != null) && token.startsWith("Bearer ")) {
                        token = token.substring(7);
                    }

                    RestTemplate restTemplate = new RestTemplateBuilder().build();
                    String authServerURL = getAuthServerHost();
                    ResponseEntity<UserPermissionsDto> responseEntity = restTemplate.getForEntity("http://" + authServerURL + "/api/v1/authentication/isValid?token=" + token, UserPermissionsDto.class);
                    UserPermissionsDto userPermissionsDto = responseEntity.getBody();
                    if (userPermissionsDto == null)
                        throw new TokenValidationException("Failed to authenticate the user");
                    if ((userPermissionsDto.getMessage() == null) || (!userPermissionsDto.getMessage().equals("Token Validation Successful!!!")))
                        throw new TokenValidationException(userPermissionsDto.getMessage());
                    if (userPermissionsDto.getRolesAndPermissions().isEmpty())
                        throw new TokenValidationException("No Permissions to the user!!!");
                    if ((userPermissionsDto.getUserState() != UserState.APPROVED) && !exchange.getRequest().getURI().getPath().contains("/approvals/"))
                        throw new TokenValidationException("User is not approved yet!!! Please go through the approval process!!!");
                    ServerHttpRequest serverHttpRequest = exchange.getRequest();
                    String forwardToken = tokenProvider.generateToken(userPermissionsDto.getUserId(), userPermissionsDto.getRolesAndPermissions());
                    serverHttpRequest.mutate().header(HttpHeaders.AUTHORIZATION, "Gateway " + forwardToken).build();
                    exchange.mutate().request(serverHttpRequest).build();
                }
            }
            return chain.filter(exchange);
        });
    }

    @NoArgsConstructor
    public static class Config {

    }

    private String getAuthServerHost() {
        int iCounter = 0;
        do {
            try {
                String host = discoveryClient.getInstances(authserverName).get(0).getHost();
                int port = discoveryClient.getInstances(authserverName).get(0).getPort();
                return host + ":" + port;
            } catch (Exception ignored) {
                try { Thread.sleep(100); } catch (Exception ignored2) {}
            }
        } while(++iCounter < 300);
        throw new RuntimeException("Failed to get Authentication Server details!!!");
    }
}
