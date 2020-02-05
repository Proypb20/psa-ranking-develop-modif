package com.psa.ranking.web.rest;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.psa.ranking.domain.User;
import com.psa.ranking.repository.UserRepository;
import com.psa.ranking.security.SecurityUtils;
import com.psa.ranking.service.MailService;
import com.psa.ranking.service.UserService;
import com.psa.ranking.service.dto.PasswordChangeDTO;
import com.psa.ranking.service.dto.UserDTO;
import com.psa.ranking.web.rest.errors.BadRequestAlertException;
import com.psa.ranking.web.rest.errors.EmailAlreadyUsedException;
import com.psa.ranking.web.rest.errors.EmailNotFoundException;
import com.psa.ranking.web.rest.errors.InvalidPasswordException;
import com.psa.ranking.web.rest.errors.LoginAlreadyUsedException;
import com.psa.ranking.web.rest.vm.KeyAndPasswordVM;
import com.psa.ranking.web.rest.vm.ManagedUserVM;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

	private static class AccountResourceException extends RuntimeException {
		private AccountResourceException(String message) {
			super(message);
		}
	}

	private final Logger log = LoggerFactory.getLogger(AccountResource.class);

	private final UserRepository userRepository;

	private final UserService userService;

	private final MailService mailService;

	private static final String ENTITY_NAME = "Account";

	public AccountResource(UserRepository userRepository, UserService userService, MailService mailService) {

		this.userRepository = userRepository;
		this.userService = userService;
		this.mailService = mailService;
	}

	/**
	 * {@code POST  /register} : register the user.
	 *
	 * @param managedUserVM the managed user View Model.
	 * @return
	 * @throws InvalidPasswordException  {@code 400 (Bad Request)} if the password
	 *                                   is incorrect.
	 * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is
	 *                                   already used.
	 * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is
	 *                                   already used.
	 */
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
		HttpHeaders textPlainHeaders = new HttpHeaders();
		textPlainHeaders.setContentType(MediaType.TEXT_PLAIN);

		if (!checkPasswordLength(managedUserVM.getPassword())) {
			throw new InvalidPasswordException();
		}

		return userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase())
				.map(user -> new ResponseEntity<>("register.error.userexists", textPlainHeaders, HttpStatus.BAD_REQUEST))
				.orElseGet(() -> userRepository.findOneWithAuthoritiesByEmailIgnoreCase(managedUserVM.getEmail()).map(
						user -> new ResponseEntity<>("register.error.emailexists", textPlainHeaders, HttpStatus.BAD_REQUEST))
						.orElseGet(() -> {
							User user = userService
							     .createUser(managedUserVM.getLogin(), managedUserVM.getPassword(),
									managedUserVM.getFirstName(), managedUserVM.getLastName(),
									managedUserVM.getEmail().toLowerCase(), managedUserVM.getLangKey(),
									managedUserVM.getPhone());

							mailService.sendActivationEmail(user);
							return new ResponseEntity<>(HttpStatus.CREATED);
						})
						);
	}

	/**
	 * {@code GET  /activate} : activate the registered user.
	 *
	 * @param key the activation key.
	 * @throws RuntimeException {@code 500 (Internal Server Error)} if the user
	 *                          couldn't be activated.
	 */
	@GetMapping("/activate")
	public void activateAccount(@RequestParam(value = "key") String key) {
		Optional<User> user = userService.activateRegistration(key);
		if (!user.isPresent()) {
			throw new AccountResourceException("No user was found for this activation key");
		}
	}

	/**
	 * {@code GET  /authenticate} : check if the user is authenticated, and return
	 * its login.
	 *
	 * @param request the HTTP request.
	 * @return the login if the user is authenticated.
	 */
	@GetMapping("/authenticate")
	public String isAuthenticated(HttpServletRequest request) {
		log.debug("REST request to check if the current user is authenticated");
		return request.getRemoteUser();
	}

	/**
	 * {@code GET  /account} : get the current user.
	 *
	 * @return the current user.
	 * @throws RuntimeException {@code 500 (Internal Server Error)} if the user
	 *                          couldn't be returned.
	 */
	@GetMapping("/account")
	public UserDTO getAccount() {
		return userService.getUserWithAuthorities()
		        .map(UserDTO::new)
				.orElseThrow(() -> new AccountResourceException("User could not be found"));
	}

	/**
	 * {@code POST  /account} : update the current user information.
	 *
	 * @param userDTO the current user information.
	 * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is
	 *                                   already used.
	 * @throws RuntimeException          {@code 500 (Internal Server Error)} if the
	 *                                   user login wasn't found.
	 */
	@PostMapping("/account")
	public void saveAccount(@Valid @RequestBody ManagedUserVM userDTO) {
	    log.debug(userDTO.toString());
		String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AccountResourceException("Current user login not found"));
		Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
		if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
			throw new EmailAlreadyUsedException();
		}
		Optional<User> user = userRepository.findOneByLogin(userLogin);
		if (!user.isPresent()) {
			throw new AccountResourceException("User could not be found");
		}
		userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
		    userDTO.getLangKey(), userDTO.getImageUrl());
	}

	/**
	 * {@code POST  /account/change-password} : changes the current user's password.
	 *
	 * @param passwordChangeDto current and new password.
	 * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new
	 *                                  password is incorrect.
	 */
	@PostMapping(path = "/account/change-password")
	public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
		if (!checkPasswordLength(passwordChangeDto.getNewPassword())) {
			throw new InvalidPasswordException();
		}
		userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
	}

	/**
	 * {@code POST   /account/reset-password/init} : Send an email to reset the
	 * password of the user.
	 *
	 * @param mail the mail of the user.
	 * @throws EmailNotFoundException {@code 400 (Bad Request)} if the email address
	 *                                is not registered.
	 */
	@PostMapping(path = "/account/reset-password/init")
	public void requestPasswordReset(@RequestBody String mail) {
		mailService.sendPasswordResetMail(
		   userService.requestPasswordReset(mail)
		       .orElseThrow(EmailNotFoundException::new)
			   );
	}

	/**
	 * {@code POST   /account/reset-password/finish} : Finish to reset the password
	 * of the user.
	 *
	 * @param keyAndPassword the generated key and the new password.
	 * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is
	 *                                  incorrect.
	 * @throws RuntimeException         {@code 500 (Internal Server Error)} if the
	 *                                  password could not be reset.
	 */
	@PostMapping(path = "/account/reset-password/finish")
	public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
		if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
			throw new InvalidPasswordException();
		}
		Optional<User> user = 
		userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

		if (!user.isPresent()) {
			throw new AccountResourceException("No user was found for this reset key");
		}
	}

	private static boolean checkPasswordLength(String password) {
		return !StringUtils.isEmpty(password) &&
		 password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH && 
		 password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
	}
}
