package com.example.user.dto;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {
    private String serverUrl;
    private String realm;
    private String clientId;
    private String clientSecret;


    public String getServerUrl() {
        return serverUrl;
    }

    public KeycloakProperties setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
        return this;
    }

    public String getRealm() {
        return realm;
    }

    public KeycloakProperties setRealm(String realm) {
        this.realm = realm;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public KeycloakProperties setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public KeycloakProperties setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }


    @Override
    public String toString() {
        return "KeycloakProperties{" +
                "serverUrl='" + serverUrl + '\'' +
                ", realm='" + realm + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +

                '}';
    }
}
