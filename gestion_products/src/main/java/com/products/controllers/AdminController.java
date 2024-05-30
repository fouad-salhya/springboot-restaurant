package com.products.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.products.entities.ProductEntity;
import com.products.services.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	
	@Autowired
	AdminService adminService;
	
	@GetMapping("/")
	public String getAllProducts(@RequestParam(value = "page",defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "10") int limit, Model model) {
	
		 List<ProductEntity> products = adminService.getAllProducts(page, limit);
		 
		 model.addAttribute("products",products);
		 
		 return "admin.html";
		
		
	}

	@PostMapping("/product/save")
	public String addproduct(@ModelAttribute("product") ProductEntity product, @RequestParam("image") MultipartFile image) {
		
		 try {
		        adminService.createProduct(product, image);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		    return "redirect:/admin";
		
	}
	
	 @GetMapping("/product/{id}")
	 public String getProductById(@PathVariable long id, Model model) {
	        ProductEntity product = adminService.getProductById(id);
	        if (product == null) {
	           
	            return "redirect:/products"; 
	        }
	        model.addAttribute("product", product);
	        return "product-details";
	 }
	 
	  @DeleteMapping("/delete/{id}")
	    public String deleteProduct(@PathVariable long id) {
	       adminService.deleteProduct(id);
	        return "redirect:/products";
	    }
	 
	 @PutMapping("/product/update/{id}")
	    public String updateProduct(@PathVariable long id, @ModelAttribute ProductEntity product, @RequestParam("image") MultipartFile image) {
	        try {
	           adminService.updateProduct(id, product, image);
	        } catch (IOException e) {
	            e.printStackTrace();
	         
	        }
	        return "redirect:/admin";
	
	 }
	 
	 
}
