package weatherfit.weatherfit_back.service;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSenderGmail;
    private final JavaMailSender javaMailSenderNaver;
    // 인증 코드를 임시 저장할 Map (이메일 -> 코드)
    private final Map<String, String> verificationCodes = new HashMap<>();

    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            javaMailSenderGmail.send(message);
        } catch (Exception e) {
            System.out.println("이메일 전송 실패: " + e.getMessage());
            throw new RuntimeException("이메일 전송에 실패했습니다: " + e.getMessage());
        }
        
        // 인증 코드 저장 (text에서 코드 추출)
        String code = text.replace("인증 코드: ", "").trim();
        verificationCodes.put(to, code);
    }

    public Boolean verifyCode(String email, String code) {
        String savedCode = verificationCodes.get(email);
        return savedCode != null && savedCode.equals(code);
    }

    public void sendEmailByNaver(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSenderNaver.send(message);
        
        String code = text.replace("인증 코드: ", "").trim();
        verificationCodes.put(to, code);
    }
}

