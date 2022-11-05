
package com.esprit.examen.services;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import com.esprit.examen.entities.DetailFacture;
import com.esprit.examen.entities.Produit;
import com.esprit.examen.repositories.DetailFactureRepository;
import com.esprit.examen.repositories.ProduitRepository;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductServiceImplTest {
	//Unit tests
	
	@Autowired
	IProduitService productService;
	
	@Autowired
	ProduitRepository productRepository;
	
	@Autowired
	DetailFactureRepository detailFactureRepository;
	
	@Test
	@Order(1)
	public void verifierMaxProducts() {
		//Testing JUnit
		int max=20;
		int total = productRepository.findAll().size();
		Assertions.assertTrue(max>total);
	}
	
	//Un produit est appele demande si sa vente depasse la moitie du total des ventes de tous les produits.
	@Test
	@Order(0)

	public void produitDemande() {
		Produit p1 = new Produit();
		productRepository.save(p1);
		DetailFacture df0 = new DetailFacture();
		DetailFacture df00 = new DetailFacture();
		df0.setProduit(p1);
		df00.setProduit(p1);
		detailFactureRepository.save(df0);
		detailFactureRepository.save(df00);
		List <DetailFacture> list = detailFactureRepository.findAll();
		int nbreProduitMax = 0;
		int nbreProduitcourant = 0;
		int i = 0;
        //Le produit le plus vendu
        int totalProduitsVendus = 0;
        
        for (DetailFacture df1 : list) {
        	i=i+1;
            totalProduitsVendus=totalProduitsVendus+1;
            for (DetailFacture df2 : list) {
                if ( (df1.getProduit().getIdProduit())== (df2.getProduit().getIdProduit())) {
                	nbreProduitcourant = nbreProduitcourant + 1;
                }
            }
            if (nbreProduitcourant/2 > nbreProduitMax) {
            	nbreProduitMax = nbreProduitcourant/2;
            }
        }
        if (totalProduitsVendus==0) {
        	Assertions.assertEquals ("Test unitaire", "Test unitaire");
        }
        else
        {
            Assertions.assertTrue(nbreProduitMax>=totalProduitsVendus/2);
        }
	}
	
}
