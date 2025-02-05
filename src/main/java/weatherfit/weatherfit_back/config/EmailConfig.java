package weatherfit.weatherfit_back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Bean
    @Primary
    public JavaMailSender javaMailSenderGmail() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com"); // Gmail SMTP 서버 주소
        mailSender.setPort(587); // Gmail SMTP 포트

        mailSender.setUsername("lee940706@gmail.com"); // Gmail 계정
        mailSender.setPassword("dull emao dbaq nnkx"); // Gmail 비밀번호

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    @Bean
    public JavaMailSender javaMailSenderNaver() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.naver.com"); // Naver SMTP 서버 주소
        mailSender.setPort(465); // Naver SMTP 포트

        mailSender.setUsername("ljs2894@naver.com"); // Naver 계정
        mailSender.setPassword("Lee289473007216!"); // Naver 비밀번호

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
