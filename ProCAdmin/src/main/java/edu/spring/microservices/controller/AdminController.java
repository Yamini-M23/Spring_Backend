package edu.spring.microservices.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.spring.microservices.dto.AdminRequest;
import edu.spring.microservices.entity.Admin;
import edu.spring.microservices.entity.AdminCredentials;
import edu.spring.microservices.entity.Email;
import edu.spring.microservices.repository.AdminRepository;
import edu.spring.microservices.service.AdminService;
import edu.spring.microservices.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@CrossOrigin(originPatterns = "http://localhost:4200")
public class AdminController {
	
	private final AdminService adminService;
	
	private final EmailService emailService;
	
	private final AdminRepository adminRepository;
	
	@GetMapping("/getadmins")
	public List<Admin> getAdmins()
	{
		return adminRepository.findAll();
	}
	
	
	@PostMapping("/postadmins")
//	@ResponseBody
	public String addAdmin(@RequestBody AdminRequest adminRequest)
	{
		System.out.println(adminRequest);
		return adminService.createAdmin(adminRequest);
	}
	
	@PostMapping("/approveadmins")
	public String approveAdmin(@RequestBody Admin admin)
	{
	  adminRepository.save(admin);
		return "Admin Approved";
	}
	
	@PostMapping("/deleteadmins")
	public String deleteAdmin(@RequestBody Admin admin)
	{
		System.out.println(admin);
	  adminRepository.delete(admin);
		return "Admin Deleted";
	}
	@PostMapping("/credentialsValidate")
	public ResponseEntity<AdminCredentials> getAdminCredentials(@RequestBody Map<String,String> credentials)
	{
		String email = credentials.get("email");
	    String password = credentials.get("password");
	    System.out.println(email);
	    return adminService.ValidateUser(email, password);
	}
	
	
	@CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/email/{id}")

    public ResponseEntity<Email> sendEmail(@PathVariable String id) throws MessagingException {

		System.out.println("Email");
        Admin admin = adminService.getAdminById(id);

        if (admin == null) {
        	System.out.println("Email sent successfully");


            //return ResponseEntity.badRequest().body("Customer not found with ID: " + customerId);


            return new ResponseEntity<Email>(new Email("Admin not found with ID: " + id),HttpStatus.OK);


//        	return new ResponseEntity<String>("Customer not found");
        }


        // Send email using emailService

 

        String emailContent = "Dear " + admin.getAdminName() + ",\n\n"

 

                + "Through this mail, we are happy to let you know that your profile has been verified successfully.Now you are Approved to add more fantasy colors to our ProVue "

 

                + "You are verified admin now and you have the access to add products and offerings to our ProVue "

 

                + "Thank you for your cooperation. Welcome to Provue";

 

        emailService.sendEmail(admin.getEmail(), "Subject: Approval Confirmation", emailContent);


        //return ResponseEntity.ok("Email sent successfully to customer with ID: " + customerId);

 

        return new ResponseEntity<Email>(new Email("Email sent successfully to Admin with ID:"+ id),HttpStatus.OK);

 

    }


}
