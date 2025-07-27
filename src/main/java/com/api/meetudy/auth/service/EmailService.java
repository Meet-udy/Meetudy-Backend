package com.api.meetudy.auth.service;

import com.api.meetudy.global.config.SecurityConfig;
import com.api.meetudy.global.response.exception.CustomException;
import com.api.meetudy.global.response.status.ErrorStatus;
import com.api.meetudy.member.entity.Member;
import com.api.meetudy.member.repository.MemberRepository;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String sender;
    private String senderName = "MEETUDY";
    private final MemberRepository memberRepository;
    private final SecurityConfig securityConfig;
    private final JavaMailSender mailSender;
    private final Map<String, String> verificationCodes = new HashMap<>();
    private final Map<String, Long> verificationExpiry = new HashMap<>();
    private static final long EXPIRY_TIME = 5 * 60 * 1000;

    public String sendPasswordResetEmail(String username, String sendTo) throws Exception {
        Member member = memberRepository.findByUsernameAndEmail(username, sendTo)
                .orElseThrow(() -> new CustomException(ErrorStatus.MEMBER_NOT_FOUND));

        String password = generateRandomCode();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        helper.setFrom(new InternetAddress(sender, senderName));
        helper.setTo(sendTo);
        helper.setSubject("<MEETUDY> 임시 비밀번호 발급 관련 안내");
        helper.setText("안녕하세요. MEETUDY 임시 비밀번호 안내 관련 이메일 입니다.\n\n" + "회원님의 임시 비밀번호는 " + password + " 입니다. " + "로그인 후 비밀번호를 변경해 주세요.");

        mailSender.send(mimeMessage);
        updatePassword(sendTo, password);

        return "Email sent successfully";
    }

    public String sendVerificationEmail(String sendTo) throws Exception {
        String verificationCode = generateRandomCode();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        helper.setFrom(new InternetAddress(sender, senderName));
        helper.setTo(sendTo);
        helper.setSubject("<MEETUDY> 이메일 본인 인증 관련 안내");
        helper.setText("안녕하세요. MEETUDY 이메일 본인 인증 안내 관련 이메일 입니다.\n\n" + "본인 인증을 위해 아래 인증 코드를 입력해 주세요.\n\n" + "인증 코드 : " + verificationCode);

        mailSender.send(mimeMessage);
        storeVerificationCode(sendTo, verificationCode);

        return "Verification email sent successfully";
    }

    @Async
    public void sendVerificationEmailAsync(String sendTo) throws Exception {
        sendVerificationEmail(sendTo);
    }

    @Async
    public void sendPasswordResetEmailAsync(String username, String sendTo) throws Exception {
        sendPasswordResetEmail(username, sendTo);
    }

    public boolean verifyCode(String email, String code) {
        String storedCode = verificationCodes.get(email);
        Long expiryTime = verificationExpiry.get(email);

        if (storedCode != null && storedCode.equals(code) && System.currentTimeMillis() < expiryTime) {
            verificationCodes.remove(email);
            verificationExpiry.remove(email);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public void updatePassword(String email, String password) {
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorStatus.MEMBER_NOT_FOUND));

        String encodedPassword = securityConfig.passwordEncoder().encode(password);
        member.setPassword(encodedPassword);

        memberRepository.save(member);
    }

    private String generateRandomCode() {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=";
        StringBuilder temporaryPassword = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int idx = (int) (Math.random() * str.length());
            temporaryPassword.append(str.charAt(idx));
        }

        return temporaryPassword.toString();
    }

    private void storeVerificationCode(String email, String code) {
        verificationCodes.put(email, code);
        verificationExpiry.put(email, System.currentTimeMillis() + EXPIRY_TIME);
    }

}