package com.example.easynotes.controller;

import com.example.easynotes.dto.ProductDTO;
import com.example.easynotes.exception.ResourceNotFoundException;
import com.example.easynotes.model.*;
import com.example.easynotes.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/products")//view products
    public List<ProductDTO> getAllProducts(){
        List<ProductDTO> productDTOList = new ArrayList<>();
        List<Product> products = productRepository.findAll();
        for(Product product : products){
            ProductDTO productDTO = productToDTO(product);
            productDTOList.add(productDTO);
        }
        return productDTOList;
    }

    private ProductDTO productToDTO(Product product){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCompany(product.getCompany());
        productDTO.setDescription(product.getDescription());
        productDTO.setId(product.getId());
        productDTO.setImage(product.getImage());
        productDTO.setPrice(product.getPrice());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setTitle(product.getTitle());
        return productDTO;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/products")//add products
    public Product createProduct(@Valid @RequestBody ProductDTO productDTO){
        Product product = new Product();
        product.setTitle(productDTO.getTitle());
        product.setDescription(productDTO.getDescription());
        product.setQuantity(productDTO.getQuantity());
        product.setPrice(productDTO.getPrice());
        System.err.println(productDTO.getImagePath());
        byte[] image = Base64.getDecoder().decode(productDTO.getImagePath());
        System.err.println(image);
        product.setImage(image);
        product.setCompany(productDTO.getCompany());

        return productRepository.save(product);//saving to the DB
    }

    @GetMapping("/products/{id}")//specific product with the id
    public ProductDTO getProductById(@PathVariable(value = "id") Long productID) {
//        return productRepository.findById(productID)
    //            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productID));

        return productToDTO(productRepository.findById(productID).get());
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/products/{id}")//edit product
    public Product updateProduct(@PathVariable(value = "id")Long productID,
                                 @Valid @RequestBody ProductDTO productDetails){
        Product product = productRepository.findById(productID)
                .orElseThrow(()-> new ResourceNotFoundException("Product", "id", productID));

        product.setTitle(productDetails.getTitle());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setQuantity(productDetails.getQuantity());
        byte[] image = Base64.getDecoder().decode(productDetails.getImagePath());
        product.setImage(image);
        product.setCompany(productDetails.getCompany());

        Product updatedProduct = productRepository.save(product);//saving to the database.
        return updatedProduct;
    }


    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/products/{id}")//deleting the product
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "id") Long productID) {
        Product note = productRepository.findById(productID)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productID));

        productRepository.delete(note);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('CONSUMER')")
        @GetMapping("/products/qtyavailable/{id}/{qty}")
        public ResponseEntity<Object> getQty(@PathVariable long id,@PathVariable int qty){
            Optional<Product> product = productRepository.findById(id);
            if(product.isPresent()){
            if(qty<=product.get().getQuantity() && qty>0){
                return new ResponseEntity<Object>(product.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<Object>(null, HttpStatus.NOT_FOUND);
    }
}
