package com.example.produit.controller;

import com.example.produit.dto.User;
import com.example.produit.entities.Produit;
import com.example.produit.services.IProduitService;
import com.example.produit.services.ProduitKafkaProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
public class ProduitController {
    private final IProduitService iProduitService;


    private final WebClient.Builder webClientBuilder;
    private final ProduitKafkaProducer kafkaTemplate;

    private static final String USER_SERVICE_URL = "http://localhost:8222/api/users/produit/";

    public ProduitController(IProduitService iProduitService, WebClient.Builder webClientBuilder, ProduitKafkaProducer kafkaTemplate) {
        this.iProduitService = iProduitService;
        this.webClientBuilder = webClientBuilder;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public ResponseEntity<?> ajouterProduit(@RequestBody Produit produit,
                                            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");

        try {
            User user = webClientBuilder.build()
                    .get()
                    .uri(USER_SERVICE_URL + produit.getUserId())
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> Mono.error(new RuntimeException("Erreur utilisateur")))
                    .bodyToMono(User.class)
                    .block();

            if (user != null) {
                produit.setUserId(user.getId());
                Produit nouveauProduit = iProduitService.addProduit(produit);
                String message = "Nouveau produit ajouté : " + nouveauProduit.getId() + " par l'utilisateur " + user.getId();
                kafkaTemplate.sendMessage("produit-topic", message);
                System.out.println("Message envoyé : " + message);
                return ResponseEntity.status(HttpStatus.CREATED).body(nouveauProduit);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'appel au service utilisateur: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossible d'ajouter le produit.");
    }

    @PostMapping("/add")
    public ResponseEntity<String> addProduit(@RequestBody Produit produit) {
        Produit createdProduit = iProduitService.addProduit(produit);
        if (createdProduit != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Produit added successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding produit");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Produit>> getAllProduits() {
        List<Produit> produits = iProduitService.getall();
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduit(@PathVariable Long id) {
        Produit produit = iProduitService.getProduit(id);
        if (produit != null) {
            return ResponseEntity.ok(produit);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduit(@PathVariable Long id, @RequestBody Produit produit) {
        Produit updatedProduit = iProduitService.updateProduit(id, produit);
        if (updatedProduit != null) {
            return ResponseEntity.status(HttpStatus.OK).body("Produit updated successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produit not found");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduit(@PathVariable Long id) {
        iProduitService.deleteProduit(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Produit deleted successfully");
    }
}
