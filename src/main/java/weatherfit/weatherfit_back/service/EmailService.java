package weatherfit.weatherfit_back.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class EmailService {
    private final JavaMailSender gmailSender;
    private final JavaMailSender naverSender;

    // 생성자에서 @Qualifier 사용
    public EmailService(@Qualifier("javaMailSenderGmail") JavaMailSender gmailSender,
                        @Qualifier("javaMailSenderNaver") JavaMailSender naverSender) {
        this.gmailSender = gmailSender;
        this.naverSender = naverSender;
    }

    // gmail 이메일 전송
    public void sendEmail(String toEmail, String subject, String text) {
        SimpleMailMessage emailForm = createEmailForm(toEmail, subject, text);

        emailForm.setTo(toEmail);
        emailForm.setSubject(subject);
        emailForm.setText(text);

        gmailSender.send(emailForm);
    }

    // naver 이메일 전송
    public void sendEmailByNaver(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        naverSender.send(message);
    }

    // 보낼 이메일 형식 생성
    private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);
        return message;
    }
}

