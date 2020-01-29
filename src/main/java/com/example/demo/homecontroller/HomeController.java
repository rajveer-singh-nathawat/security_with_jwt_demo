package com.example.demo.homecontroller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AuthenticationRequest;
import com.example.demo.dto.UserRegisterDetailsRequest;
import com.example.demo.service.HomeService;

@RestController()
@RequestMapping()
public class HomeController {
	@Autowired
	private HomeService homeService;

	@GetMapping("/")
	public String welcome() {
		return "<h1>Welcome</h1>";
	}

	@GetMapping("/user")
	public String user() {
		return "<h2>hello user</h2>";
	}

	@GetMapping("/admin")
	public String admin() {
		return "<h2>hello admin</h2>";
	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		ResponseEntity<?> createdToken = homeService.createAuthenticationToken(authenticationRequest);
		return createdToken;

	}
	
	@PostMapping("/register")
	public ResponseEntity<HttpStatus> registerUser(@Valid @RequestBody UserRegisterDetailsRequest userRegisterDetailsRequest){
		ResponseEntity<HttpStatus> status = homeService.registerUser(userRegisterDetailsRequest);
		return status;
	}

}
