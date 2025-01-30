package weatherfit.weatherfit_back.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class EmailService {
    private final JavaMailSender gmailSender;
    private final JavaMailSender naverSender;
    // 인증 코드를 임시 저장할 Map (이메일 -> 코드)
    private final Map<String, String> verificationCodes = new HashMap<>();

    // 생성자에서 @Qualifier 사용
    public EmailService(@Qualifier("javaMailSenderGmail") JavaMailSender gmailSender,
                        @Qualifier("javaMailSenderNaver") JavaMailSender naverSender) {
        this.gmailSender = gmailSender;
        this.naverSender = naverSender;
    }

    // gmail 이메일 전송
    public void sendEmail(String toEmail, String subject, String text) {
        SimpleMailMessage emailForm = createEmailForm(toEmail, subject, text);
        gmailSender.send(emailForm);
        
        // 인증 코드 저장 (text에서 코드 추출)
        String code = text.replace("인증 코드: ", "").trim();
        verificationCodes.put(toEmail, code);
    }

    // naver 이메일 전송
    public void sendEmailByNaver(String to, String subject, String text) {
        SimpleMailMessage message = createEmailForm(to, subject, text);
        naverSender.send(message);
        
        // 네이버 메일도 인증 코드 저장이 필요하다면
        String code = text.replace("인증 코드: ", "").trim();
        verificationCodes.put(to, code);
    }

    // 보낼 이메일 형식 생성
    private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);
        return message;
    }

    public Boolean verifyCode(String email, String code) {
        String savedCode = verificationCodes.get(email);
        return savedCode != null && savedCode.equals(code);
    }
}

