package com.products.services;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.products.dtos.ProductDto;
import com.products.entities.FeedbackEntity;
import com.products.entities.ProductEntity;
import com.products.entities.ReservationEntity;
import com.products.entities.Type;
import com.products.repository.FeedbackRepository;
import com.products.repository.ProductRepository;
import com.products.repository.ReservationRepository;
import com.products.shared.Utils;

import jakarta.mail.internet.MimeMessage;

@Service
public class ProductService {

	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ReservationRepository reservationRepository;
	
	@Autowired
	Utils utils;
	
	@Autowired
	JavaMailSender javaMailSender;
	
	@Autowired
    EmailService emailService;

	@Autowired
	FeedbackRepository feedbackRepository;
	
	
	
	public List<ProductEntity> getProducts() {
		
		List<ProductEntity> productsEntity = productRepository.findAll();
		
		
		return productsEntity;
	};
	
	public void reserverTable(ReservationEntity reservation) {
        reservation.setReservationId(utils.generateStringId(32));
        reservationRepository.save(reservation);

        sendConfirmationEmail(reservation);
    }
	private void sendConfirmationEmail(ReservationEntity reservation) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
        	
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy 'à' HH:mm");
            String formattedDate = reservation.getReservationDate().format(formatter);
        	
            helper.setTo(reservation.getEmail());
            helper.setSubject("Confirmation de reservation d'une table from Restoran");
            
            String htmlContent = "<h4>Bonjour " + reservation.getName() + ",</h4>" +
                  
                    "<h5>Merci pour votre réservation.</h5>"+
                    "<p>Nombre de personnes: " + reservation.getTotal_person() + "</p>" +
                    "<h5>date et time" +  formattedDate + ".</h5>" +
                    "<p>Nous sommes impatients de vous accueillir !</p>";

            helper.setText(htmlContent, true);
        } catch (Exception e) {
            e.printStackTrace(); //e
        }
        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace(); 
        }
    }
	
	public Optional<ProductEntity> getOneProduct(long id) {
		
		return productRepository.findById(id);
	}
	
	public List<ProductEntity> getRelatedProducts(Type type) {
		
		return productRepository.findAllByType(type);
		
	}


	public List<FeedbackEntity> getAllReviews() {
		return feedbackRepository.findAll();
	}

	
	
	public ProductDto createProduct(ProductDto productDto) {
		
		ModelMapper modelMapper = new ModelMapper();
		
		ProductEntity productEntity = modelMapper.map(productDto, ProductEntity.class);
		
		productEntity.setProductId(utils.generateStringId(32));
		
		
		ProductEntity newProduct = productRepository.save(productEntity);
		
		ProductDto newDto = modelMapper.map(newProduct, ProductDto.class);
		
		return newDto;
		
	}
	
	public ProductDto updateProduct(String productId, ProductDto productDto) {
		
		
		ModelMapper modelMapper = new ModelMapper();
		
		ProductEntity productEntity = modelMapper.map(productDto, ProductEntity.class);
		
		productEntity.setName(productDto.getName());
		productEntity.setCategory(productDto.getCategory());
		productEntity.setPrix(productDto.getPrix());
		
		ProductEntity newProduct = productRepository.save(productEntity);
	
		ProductDto newDto = modelMapper.map(newProduct, ProductDto.class);
		
		return newDto; 
	}
	
	public void deleteProduct(String productId) {
		
		ProductEntity product = productRepository.findByProductId(productId);
		
		if(product != null) {
			productRepository.delete(product);
		}
	}
	
	public List<ProductDto> getAll(int page, int limit) {
		
		if(page > 0) page -= 1;
		
		ModelMapper modelMapper = new ModelMapper();
		
		List<ProductDto> productsDto = new ArrayList<>();
		
		Pageable pageableRequest = PageRequest.of(page, limit);
		
		Page<ProductEntity> productPage = productRepository.findAll(pageableRequest);
		
		List<ProductEntity> products = productPage.getContent();
		
		for(ProductEntity product: products) {
			
			ProductDto productDto = modelMapper.map(product, ProductDto.class);
			
			productsDto.add(productDto);
		}
		
		return productsDto;


	}
	
	public List<ProductDto> search() {
        
		ModelMapper modelMapper = new ModelMapper();
		
		List<ProductDto> productsDto = new ArrayList<>();
		
		List<ProductEntity> products = productRepository.findAllByQuery();
		
		for(ProductEntity product: products) {
			ProductDto productDto = modelMapper.map(product, ProductDto.class);
			
			productsDto.add(productDto);
		}
		
		return productsDto;
		
	}
	
	
	
	
	
	
	
	
	
	
}
