package com.bookstore.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com"); // Địa chỉ SMTP server của Gmail
        mailSender.setPort(587); // Port cho SMTP (587 cho TLS)

        mailSender.setUsername("hethongvellrel@gmail.com"); // Email của tài khoản gửi
        mailSender.setPassword("QEVA ZAMC FPPK QZZ H"); // Mật khẩu ứng dụng (nếu dùng 2FA)

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
