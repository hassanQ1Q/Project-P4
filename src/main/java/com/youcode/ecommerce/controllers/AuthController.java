package com.youcode.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.youcode.ecommerce.config.JwtTokenUtil;
import com.youcode.ecommerce.entities.JwtRequest;
import com.youcode.ecommerce.entities.JwtResponse;
import com.youcode.ecommerce.services.imp.UserService;

@RestController
@CrossOrigin
public class AuthController {

	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAutheticationToken(@RequestBody JwtRequest authRequest) throws Exception {

		authenticate(authRequest.getUsername(), authRequest.getPassword());
		final UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		} catch (DisabledException e) {
			throw new Exception("user_disabled", e);
		} catch (BadCredentialsException e) {
			throw new Exception("invalid_credentials", e);
		}

	}

}
