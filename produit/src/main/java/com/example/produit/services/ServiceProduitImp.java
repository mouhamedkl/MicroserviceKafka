package com.example.produit.services;

import com.example.produit.entities.Produit;
import com.example.produit.repository.ProduitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceProduitImp implements IProduitService {
    private final ProduitRepository produitRepository;

    public ServiceProduitImp(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    @Override
    public List<Produit> getall() {
        return produitRepository.findAll();
    }

    @Override
    public Produit addProduit(Produit produit) {
         return produitRepository.save(produit);
    }

    @Override
    public Produit updateProduit(Long id, Produit p) {
        Produit updatedProduit = produitRepository.findById(id).orElse(null);
        if (updatedProduit != null) {
            updatedProduit.setName(p.getName());
            updatedProduit.setType(p.getType());
            updatedProduit.setPrice(p.getPrice());
            return produitRepository.save(updatedProduit);
        }
        return null;
    }


    @Override
    public Produit getProduit(Long id) {
        return produitRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteProduit(Long id) {
        produitRepository.deleteById(id);
    }




}
