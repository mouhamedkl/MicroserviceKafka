package com.example.produit.services;

import com.example.produit.entities.Produit;

import java.util.List;


public interface IProduitService {
    List<Produit> getall();
    Produit addProduit (Produit produit);
    Produit updateProduit (Long id , Produit produit);
    Produit getProduit(Long id);
    void deleteProduit(Long id);

}
