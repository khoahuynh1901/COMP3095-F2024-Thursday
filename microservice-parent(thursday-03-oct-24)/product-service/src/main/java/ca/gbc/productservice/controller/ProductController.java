package ca.gbc.productservice.controller;

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.service.ProductService;
import ca.gbc.productservice.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController  // Marks this class as a Spring MVC controller, making it capable of handling HTTP requests.
@RequestMapping("api/product")  // Defines the base URL for all the endpoints in this controller.
@RequiredArgsConstructor  // Generates a constructor with required fields (e.g., the final fields).
public class ProductController {
    private final ProductServiceImpl productService;  // Service layer that handles the business logic for products.

    // CREATE - Adds a new product to the system.
    @PostMapping  // Maps HTTP POST requests to this method.
    @ResponseStatus(HttpStatus.CREATED)  // Returns HTTP 201 Created when a new product is successfully added.
    public ResponseEntity <ProductResponse> createProduct(@RequestBody ProductRequest productRequest){
        ProductResponse createdProduct = productService.createProduct(productRequest);  // Calls the service layer to create the product.

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "api/product" + createdProduct.id());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdProduct);

    }

    // READ - Retrieves a list of all products.
    @GetMapping  // Maps HTTP GET requests to this method.
    @ResponseStatus(HttpStatus.OK)  // Returns HTTP 200 OK when the products are successfully retrieved.
    public List<ProductResponse> getAllProducts(){
        return productService.getAllProducts();  // Calls the service layer to get a list of all products.
    }

    //http:localhost:8072/api/product/jlahfhfjlfgsdg
    // UPDATE - Updates an existing product.
    @PutMapping("/{productId}")  // Maps HTTP PUT requests to this method. Expects a product ID as a path variable.
    public ResponseEntity<?> updateProduct(@PathVariable("productId") String productId,
                                           @RequestBody ProductRequest productRequest){
        String updateProductId = productService.updateProduct(productId, productRequest);  // Calls the service layer to update the product.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "api/product" + updateProductId);  // Adds a Location header for the updated product.
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);  // Returns HTTP 204 No Content indicating the product was updated successfully.
    }

    //http:localhost:8072/api/product/jlahfhfjlfgsdg the product id in mapping will be appending in the url
    // DELETE - Deletes an existing product.
    @DeleteMapping("/{productId}")  // Maps HTTP DELETE requests to this method. Expects a product ID as a path variable.
    public ResponseEntity<?> deleteProduct(@PathVariable("productId") String productId ){
        productService.deleteProduct(productId);  // Calls the service layer to delete the product.
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // Returns HTTP 204 No Content indicating the product was deleted successfully.
    }
}


//Create: createProduct() - Handles product creation (POST request).
//Read: getAllProducts() - Retrieves all products (GET request).
//Update: updateProduct() - Updates an existing product based on its ID (PUT request).
//Delete: deleteProduct() - Deletes an existing product based on its ID (DELETE request)