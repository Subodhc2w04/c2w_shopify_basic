package com.core2web.controller;

import java.io.File;
import java.util.List;

import com.core2web.dao.ProductDao;
import com.core2web.model.Product;


public class ProductController {


    ProductDao productDao = new ProductDao();

    public boolean addProduct(Product product) {
        return productDao.addProduct(product);
    }

    // Uploads the image to Cloudinary (if one was chosen), then saves the product in Firestore
    public boolean addProduct(Product product, File imageFile) {
        if (imageFile != null) {
            String imageUrl = ImageUploadController.imageUpload(imageFile);
            if (imageUrl == null) {
                return false;
            }
            product.setImageUrl(imageUrl);
        }
        return productDao.addProduct(product);
    }

    public Product getProductById(String id) {
        return productDao.getProductById(id);
    }

    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }
}
