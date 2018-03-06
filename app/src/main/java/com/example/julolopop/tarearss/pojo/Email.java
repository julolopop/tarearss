package com.example.julolopop.tarearss.pojo;

/**
 * Created by Julolopop on 06/03/2018.
 */

public class Email {

    private String to;
    private String subject;

    public Email(String to, String subject) {
        super();
        this.to = to;
        this.subject = subject;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
