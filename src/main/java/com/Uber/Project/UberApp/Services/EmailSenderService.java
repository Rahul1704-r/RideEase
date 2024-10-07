package com.Uber.Project.UberApp.Services;

public interface EmailSenderService {

    public void sendEmail(String toEmail,String subject, String body);

    //if want to sent same email to everyone
    public void sendEmail(String toEmail[],String subject, String body);
}
