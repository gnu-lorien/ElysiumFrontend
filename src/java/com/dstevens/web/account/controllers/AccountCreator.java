package com.dstevens.web.account.controllers;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.Year;
import java.util.Date;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dstevens.mail.MailMessage;
import com.dstevens.mail.MailMessageFactory;
import com.dstevens.suppliers.ClockSupplier;
import com.dstevens.user.Role;
import com.dstevens.user.User;
import com.dstevens.user.UserDao;
import com.dstevens.user.patronage.Patronage;
import com.dstevens.user.patronage.PatronagePaymentReceipt;
import com.dstevens.user.patronage.PaymentType;

import static com.dstevens.collections.Sets.set;

@Service
public class AccountCreator {

	private static final Logger logger = Logger.getLogger(AccountCreator.class);
	
	private final UserDao userDao;
	private final MailMessageFactory messageFactory;
	private final ClockSupplier clockSupplier;
	private final Supplier<PasswordEncoder> passwordEncoderSupplier;

	@Autowired
	public AccountCreator(UserDao userDao, Supplier<PasswordEncoder> passwordEncoderSupplier, MailMessageFactory messageFactory, ClockSupplier clockSupplier) {
		this.userDao = userDao;
		this.passwordEncoderSupplier = passwordEncoderSupplier;
		this.messageFactory = messageFactory;
		this.clockSupplier = clockSupplier;
	}
	
	public User createUser(String email,
			               String password,
			               String firstName,
			               String lastName,
			               String originalUsername,
			               String paymentReceiptIdentifier) {
		User user = new User(email, passwordEncoderSupplier.get().encode(password), set(Role.USER)).withFirstName(firstName).withLastName(lastName);
		Instant now = clockSupplier.get().instant();
		if(!StringUtils.isBlank(originalUsername)) {
			Patronage patronage = new Patronage(Year.now(clockSupplier.get()).getValue(), Date.from(now), null);
			patronage = patronage.withOriginalUsername(originalUsername.trim());
			if(!StringUtils.isBlank(paymentReceiptIdentifier)) {
				patronage = patronage.withPayment(new PatronagePaymentReceipt(PaymentType.PAYPAL, new BigDecimal("20.00"), paymentReceiptIdentifier, Date.from(now)));
			}
			user = user.withPatronage(patronage);
		}
		User newUser = userDao.save(user);
		sendConfirmatoryEmailTo(email);
		sendAdminEmailFor(newUser);
		return newUser;
	}
	
	private void sendAdminEmailFor(User user) {
		StringBuilder body = new StringBuilder();
		body.append(user.getEmail() + " has just created an account on the database.");
		if(user.getPatronage() != null) {
			body.append("\nTheir original username in the old database is " + user.getPatronage().getOriginalUsername());
			if(!user.getPatronage().getPayments().isEmpty()) {
				body.append("\nTheir paypal receipt id for paying for patronage is " + user.getPatronage().getPayments().get(0).getPaymentReceiptIdentifier());
			}
		}
		send(messageFactory.message().
		     from("services@undergroundtheater.org", "UT Database").
		     to("services@undergroundtheater.org").
		     subject("A new Underground Theater User Account has been created: " + user.getEmail()).
		     body(body.toString()));
	}

	private void sendConfirmatoryEmailTo(String email) {
		send(messageFactory.message().
		     from("services@undergroundtheater.org", "UT Database").
		     to(email).
		     subject("Your Underground Theater User Account has been created").
		     body("Thank you for creating an account with Underground Theater's character database, The Green Room!"));
	}
	
	private void send(MailMessage mailMessage) {
		try {
			mailMessage.send();
		} catch(Exception e) {
			logger.error("Failed to send " + mailMessage, e);
		}
	}
}
