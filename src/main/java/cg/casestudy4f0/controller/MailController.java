package cg.casestudy4f0.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/send")
public class MailController {
    @Autowired
    JavaMailSender mailSender;

    @PostMapping
    public ResponseEntity<?> sendMail(@RequestParam("to") String to,
                                      @RequestParam("subject") String subject,
                                      @RequestParam("content") String content){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);
        mailSender.send(simpleMailMessage);
        return new ResponseEntity<>(simpleMailMessage, HttpStatus.OK);
    }
}
