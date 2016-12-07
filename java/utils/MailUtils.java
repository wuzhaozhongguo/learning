package com.hans.jhd.domain.message.core.utils;

import org.simplejavamail.email.Email;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.config.ServerConfig;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;

public class MailUtils {

    private static String SMTP = "smtp.139.com";

    private static String userName = "13991544720@139.com";

    private static String passWord = "xxx";

    private static int PORT = 25;

    private static Mailer mailer = config();


    private static Mailer config(){
        Mailer mailer = new Mailer(new ServerConfig(SMTP, PORT, userName, passWord));
        return mailer;
    }

    public static boolean sendEmail(String subject,String content,List<String> receivers){
        Email email = new Email();
        email.setFromAddress(userName,userName);
        email.setSubject(subject);
        email.setText(content);
        for (String receiver : receivers){
            email.addRecipient(receiver,receiver, Message.RecipientType.TO);
        }

        mailer.sendMail(email,true);
        return true;
    }

    public static void main(String[] args) {
        List<String> rec = new ArrayList<>();
        rec.add("765105646@qq.com");
        sendEmail("成功","内容",rec);
    }
}
