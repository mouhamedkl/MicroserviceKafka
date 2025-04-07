package com.example.user.services;

import com.example.user.dto.KeycloakProperties;
import com.example.user.dto.Tokenreq;
import com.example.user.dto.UserRequest;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class KeycloakService {


    private final WebClient webClient;
    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;

    public KeycloakService(WebClient webClient, KeycloakProperties keycloakProperties) {
        this.webClient = webClient;
        this.keycloakProperties = keycloakProperties;
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getServerUrl())
                .realm(keycloakProperties.getRealm())
                .clientId(keycloakProperties.getClientId())
                .clientSecret(keycloakProperties.getClientSecret())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }

    public String registerUser(UserRequest userRequest) {
        System.out.println(        keycloakProperties.toString());
        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        UsersResource usersResource = realmResource.users();

        List<UserRepresentation> existingUsers = usersResource.search(userRequest.getUsername());
        if (!existingUsers.isEmpty()) {
            return "Erreur : L'utilisateur existe déjà.";
        }

        UserRepresentation user = new UserRepresentation();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setEnabled(userRequest.getEnabled());
        user.setCredentials(Collections.singletonList(createPasswordCredentials(userRequest.getPassword())));
        user.setFirstName(userRequest.getFirstname());
        user.setLastName(userRequest.getLastname());
        Response response = usersResource.create(user);
        System.out.println(response.getStatus());

        if (response.getStatus() == 201) {
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            System.out.println("User ID: " + userId);
            addRoleToUser(userRequest.getUsername(),"USER");

                return "Utilisateur créé et rôle 'USER' attribué avec succès.";

        } else {
            return "Erreur lors de la création de l'utilisateur : " + response.getStatus();
        }
    }


    public void addRoleToUser(String userName, String role_name){
        String client_id = keycloak
            .realm(keycloakProperties.getRealm())
            .clients()
            .findByClientId(keycloakProperties.getClientId())
            .get(0)
            .getId();
        String userId = keycloak
            .realm(keycloakProperties.getRealm())
            .users()
            .search(userName)
            .get(0)
            .getId();
        UserResource user = keycloak
            .realm(keycloakProperties.getRealm())
            .users()
            .get(userId);
        List<RoleRepresentation> roleToAdd = new LinkedList<>();
        roleToAdd.add(keycloak
            .realm(keycloakProperties.getRealm())
            .clients()
            .get(client_id)
            .roles()
            .get(role_name)
            .toRepresentation()
    );
        user.roles().clientLevel(client_id).add(roleToAdd);
    }
    public List<String> getAllRoles(){
        ClientRepresentation clientRep = keycloak
                .realm(keycloakProperties.getRealm())
                .clients()
                .findByClientId(keycloakProperties.getClientId())
                .get(0);
        List<String> availableRoles = keycloak
                .realm(keycloakProperties.getRealm())
                .clients()
                .get(clientRep.getId())
                .roles()
                .list()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.toList());
        return availableRoles;
    }
    public void addRealmRole(String new_role_name){
        if(!getAllRoles().contains(new_role_name)){
            RoleRepresentation roleRep = new  RoleRepresentation();
            roleRep.setName(new_role_name);
            roleRep.setDescription("role_" + new_role_name);
            ClientRepresentation clientRep = keycloak
                    .realm(keycloakProperties.getRealm())
                    .clients()
                    .findByClientId(keycloakProperties.getClientId())
                    .get(0);
            keycloak.realm(keycloakProperties.getRealm())
                    .clients()
                    .get(clientRep.getId())
                    .roles()
                    .create(roleRep);
        }
    }
    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);
        return passwordCred;
    }
    public Mono<String> loginUser(String username, String password) {
        String tokenUrl = keycloakProperties.getServerUrl() + "/realms/" + keycloakProperties.getRealm() + "/protocol/openid-connect/token";

        return webClient.post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("client_id=" + keycloakProperties.getClientId() +
                        "&client_secret=" + keycloakProperties.getClientSecret() +
                        "&grant_type="+OAuth2Constants.CLIENT_CREDENTIALS+
                        "&username=" + username +
                        "&password=" + password)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("access_token"))
                .onErrorResume(e -> Mono.error(new RuntimeException("Erreur de connexion à Keycloak", e)));


    }
    public Mono<Void> logoutUser(Tokenreq tokenreq) {
        String logoutUrl = keycloakProperties.getServerUrl() + "/realms/" + keycloakProperties.getRealm() + "/protocol/openid-connect/logout";

        return webClient.post()
                .uri(logoutUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenreq.getToken())
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(e -> {
                    return Mono.error(new RuntimeException("Erreur lors de la déconnexion de Keycloak", e));
                });
    }

}
