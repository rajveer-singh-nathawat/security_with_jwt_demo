package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AuthenticationRequest;
import com.example.demo.dto.AuthenticationResponse;
import com.example.demo.dto.UserRegisterDetailsRequest;
import com.example.demo.model.UserEntity;
import com.example.demo.model.UserProfileEntity;
import com.example.demo.model.UserRolesEntity;
import com.example.demo.model.UserRolesUserMappingEntity;
import com.example.demo.repository.UserRegisterDetailsRepository;
import com.example.demo.repository.UserReository;
import com.example.demo.repository.UserRolesMappUserRepository;
import com.example.demo.util.JwtUtil;

@Service
public class HomeService {
	@Autowired
	private UserRegisterDetailsRepository userRegisterDetailsRepository;
	
	@Autowired
	private UserReository userReository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailService userDetailService;
	
	@Autowired
	private JwtUtil jwtTokenUtil;
	@Autowired
	private UserRolesMappUserRepository userRolesMappUserRepo;
	

	public ResponseEntity<HttpStatus> registerUser(UserRegisterDetailsRequest userRegisterDetailsRequest) {
		
		saveUserDetails(userRegisterDetailsRequest);
		return new ResponseEntity<HttpStatus>(HttpStatus.CREATED);
		
	}

	private void saveUserDetails(UserRegisterDetailsRequest userRegisterDetailsRequest) {
		UserEntity user = new UserEntity();
		user.setUserName(userRegisterDetailsRequest.getUserName());
		user.setPassword(userRegisterDetailsRequest.getPassword());
		user.setActive(true);
		userReository.save(user);
		saveUserRoleMappingUser(user);
		saveRegisterUser(userRegisterDetailsRequest, user);
	}

	private void saveUserRoleMappingUser(UserEntity user) {
		UserRolesUserMappingEntity urum = new UserRolesUserMappingEntity();
		UserRolesEntity ure = new UserRolesEntity();
		ure.setUserRolesId((long) 2);
		ure.setRoles("ROLE_USER");
		
		urum.setUserEntity(user);
		urum.setUserRolesEntity(ure);
		
		userRolesMappUserRepo.save(urum);
	}

	private void saveRegisterUser(UserRegisterDetailsRequest userRegisterDetailsRequest, UserEntity user) {
		UserProfileEntity userRegisterDetails = new UserProfileEntity();
		userRegisterDetails.setEmail(userRegisterDetailsRequest.getEmail());
		userRegisterDetails.setFullName(userRegisterDetailsRequest.getFullName());
		userRegisterDetails.setMobileNo(userRegisterDetailsRequest.getMobileNo());
		userRegisterDetails.setUserEntity(user);
		userRegisterDetailsRepository.save(userRegisterDetails);
	}

	public ResponseEntity<?> createAuthenticationToken(AuthenticationRequest authenticationRequest) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}
		final UserDetails userDetails = userDetailService.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

}
