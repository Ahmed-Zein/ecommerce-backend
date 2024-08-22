package com.github.ahmed_zein.ecommerce_backend.service;

import com.github.ahmed_zein.ecommerce_backend.exception.EmailFailureException;
import com.github.ahmed_zein.ecommerce_backend.model.VerificationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${mail-server.from}")
    public String fromAddress;

    @Value("${app.frontend-url}")
    public String url;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public SimpleMailMessage createEmailMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        return message;
    }

    public void sendVerificationEmail(VerificationToken verificationToken) throws EmailFailureException {
        SimpleMailMessage simpleMailMessage = createEmailMessage();
        simpleMailMessage.setTo(verificationToken.getUser().getEmail());
        simpleMailMessage.setSubject("Verify your email to activate you account.");
        simpleMailMessage.setText("Follow the link below to verify your account.\n"
                + url + "/auth/verify/?token=" + verificationToken.getToken());

        try {
            javaMailSender.send(simpleMailMessage);
        } catch (MailException e) {
            throw new EmailFailureException();
        }
    }
}
