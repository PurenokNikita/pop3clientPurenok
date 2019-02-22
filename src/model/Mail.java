package model;

import java.io.UnsupportedEncodingException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utilitiy.MyBase64;

public class Mail {
    String subject = "";
    String from = "";
    Vector<String> to_list = new Vector();
    String content = "";

    public Mail(String subject, String from, Vector<String> to_list, String content) {
        this.subject = subject;
        this.from = from;
        this.to_list = to_list;
        this.content = content;
    }

    public Mail(Vector<String> lines) {
        this.initByLines(lines);
    }

    private void initByLines(Vector<String> lines) {
        boolean decodeWithBase64 = false;
        boolean isPreLine = false;
        boolean hasContent = false;


        for (int i = 0; i < lines.size(); ++i) {
            String buf = (String) lines.get(i);

            isPreLine = false;
            if (buf.startsWith("Return-Path:")) {
                String regex = "<(.*)>";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(buf);
                boolean cont = true;

                while (m.find()) {
                    if (cont) {
                        this.to_list = new Vector();
                        this.to_list.addElement(m.group(1));
                        cont = false;
                    }
                }
            } else if (!buf.startsWith("Subject:")) {
                if (buf.startsWith("From:")) {
                    boolean isUTF8 = false;
                    String regex = "=\\?(?i)UTF-8\\?B\\?(.*)\\?=";
                    Pattern p = Pattern.compile(regex);


                    for (Matcher m = p.matcher(buf.substring(5)); m.find(); this.from = MyBase64.getFromBASE64(m.group(1))) {
                        isUTF8 = true;

                    }
                    if (this.from.isEmpty()){
                        this.from = "без темы";
                    }

                } else if (buf.endsWith("base64")) {
                    if (!hasContent) {
                        decodeWithBase64 = true;
                        isPreLine = true;
                    }

                    hasContent = true;
                } else if (buf.startsWith("--")) {
                    decodeWithBase64 = false;
                }
            } else {
                boolean isUTF8 = false;
                String regex = "=\\?(?i)UTF-8\\?B\\?(.*)\\?=";
                Pattern p = Pattern.compile(regex);

                for (Matcher m = p.matcher(buf); m.find(); this.subject = MyBase64.getFromBASE64(m.group(1))) {
                    isUTF8 = true;
                }

                if (!isUTF8) {
                    this.subject = buf.substring(8);
                }
            }

            if (decodeWithBase64 && !isPreLine){
                this.content = this.content + MyBase64.getFromBASE64(buf);
                }

            }

        }


    public String getSubject() {
        return !this.subject.isEmpty() ? this.subject : "без темы";

    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return !this.from.isEmpty() ? this.from : "аноним";

    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Vector<String> getTo_list() {
        return this.to_list;
    }

    public void setTo_list(Vector<String> to_list) {
        this.to_list = to_list;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String toString() {

        return "Тема:" + this.subject + "\r\n" + "От:" + this.from + "\r\n" + "Текст:" + this.content;
    }
}
