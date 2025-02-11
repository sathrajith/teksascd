package com.chusit.backend.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.chusit.backend.entities.ContactForm;
import com.chusit.backend.entities.ServiceProvider;
import com.chusit.backend.entities.Subscriber;
import com.chusit.backend.repository.ContactFormRepository;
import com.chusit.backend.repository.ServiceProviderRepository;
import com.chusit.backend.repository.SubscriberRepository;
import com.chusit.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api")
public class FormController {

    @Autowired
    private ContactFormRepository contactFormRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private EmailService emailService;

    // Contact Form Submission
    @PostMapping("/contact")
    public ResponseEntity<Map<String, String>> submitContactForm(@RequestBody ContactForm contactForm) {
        try {
            if (contactForm.getName() == null || contactForm.getEmail() == null || contactForm.getMessage() == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "All fields are required!"));
            }

            contactFormRepository.save(contactForm);

            // Attempt to send an email, but catch failures
            try {
                emailService.sendEmail("admin@example.com", "New Contact Form Submission", contactForm.toString());
            } catch (Exception e) {
                System.err.println("Email sending failed: " + e.getMessage());
                return ResponseEntity.ok(Map.of("message", "Contact form submitted, but email failed to send."));
            }

            return ResponseEntity.ok(Map.of("message", "Contact form submitted successfully!"));

        } catch (Exception e) {
            System.err.println("Server error: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("message", "Server error. Please try again later."));
        }
    }




    // Newsletter Subscription
    @PostMapping("/subscribe")
    public ResponseEntity<Map<String, String>> subscribe(@RequestParam String email) {
        Logger logger = LoggerFactory.getLogger(FormController.class);
        logger.info("Received subscription request for email: {}", email);

        try {
            if (email == null || email.trim().isEmpty()) {
                logger.warn("Email is required");
                return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
            }

            // Check if email already exists
            if (subscriberRepository.existsByEmail(email)) {
                logger.warn("Email already subscribed: {}", email);
                return ResponseEntity.badRequest().body(Map.of("message", "Email already subscribed!"));
            }

            // Save subscriber
            Subscriber subscriber = new Subscriber();
            subscriber.setEmail(email);
            subscriberRepository.save(subscriber);
            logger.info("Subscriber saved successfully: {}", email);

            // Send confirmation email (Handled in Fix 2)
            try {
                emailService.sendEmail(email, "Welcome to Chusit!", "Thank you for subscribing to our newsletter!");
            } catch (Exception e) {
                logger.error("Failed to send email to: {}", email, e);
                return ResponseEntity.ok(Map.of("message", "Subscribed successfully, but failed to send confirmation email."));
            }

            return ResponseEntity.ok(Map.of("message", "Subscribed successfully!"));

        } catch (Exception e) {
            logger.error("Error processing subscription for email: {}", email, e);
            return ResponseEntity.status(500).body(Map.of("message", "An error occurred while processing your request."));
        }
    }




    // Service Provider Registration
    @PostMapping("/register-provider")
    public ResponseEntity<Map<String, String>> registerProvider(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
        }

//        // Check if email already exists
//        if (serviceProviderRepository.existsByEmail(email)) {
//            return ResponseEntity.badRequest().body(Map.of("message", "Email already registered"));
//        }

        // Save the service provider
        ServiceProvider provider = new ServiceProvider();
        provider.setEmail(email);
        serviceProviderRepository.save(provider);

        // Send confirmation email
        emailService.sendEmail(email, "Service Provider Registration", "Thank you for registering with Chusit!");

        return ResponseEntity.ok(Map.of("message", "Service provider registered successfully!"));
    }
}
