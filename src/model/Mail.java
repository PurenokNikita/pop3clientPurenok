package model;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.james.mime4j.message.Message;
import utilitiy.MyBase64;

import javax.mail.Header;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;


public class Mail {
    String subject = "без темы";
    String from = "";
    ArrayList<String> to_list = new ArrayList<>();
    String content = "";

//    private StringBuffer txtBody;
//    private StringBuffer htmlBody;
//    private ArrayList<BodyPart> attachments;

    public Mail(ArrayList<String> lines) {
        try {
            this.initByLines(lines);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public byte[] to_byte(ArrayList<String> lines) {
//        byte[] bytes=new byte[lines.size()];
//        for (int i=0; i<lines.size(); i++) {
//            String buf = (String) lines.get(i);
//            bytes[i]=Byte.parseByte(buf);
//        }
//        return bytes;
//    }

    //    private InputStream is (ArrayList<String>){
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        DataOutputStream out = new DataOutputStream(baos);
//        for (String element : lines) {
//            out.writeUTF(element);
//        }
//        byte[] bytes = baos.toByteArray();
//        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
//    }
    private void initByLines(ArrayList<String> lines) throws Exception {
        boolean decodeWithBase64 = false;
        boolean isPreLine = false;
        boolean hasContent = false;
        String encoding = "";
        String name = "";
        String value = "";
//        byte [] buffer = new byte[lines.size()];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        for (String element : lines) {
            out.writeUTF(element);
        }
        byte[] bytes = baos.toByteArray();
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        DataInputStream in = new DataInputStream(byteStream);
        InputStream is = byteStream;
        /*MailParser mailParser = new MailParser();
        mailParser.parse(is);*/
        Session s = Session.getInstance(new Properties());
        MimeMessage message = new MimeMessage(s, is);
        String subject = message.getSubject();
        System.out.println(subject);
        message.getAllHeaderLines();
        for (Enumeration<Header> e = message.getAllHeaders(); e.hasMoreElements();) {
            Header h = e.nextElement();
            name = h.getName();
            value = h.getValue();
        }
        System.out.println(name);
        System.out.println(value);



        /*MailParser mailParser = new MailParser();
        mailParser.parse(in);*/

       /*
        while (in.available() > 0) {
            String element = in.readUTF();
            System.out.println(element);
        }*/
/*
        DataInputStream in = new DataInputStream(byteStream);
*/

// read from byte array
//        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
//        DataInputStream in = new DataInputStream(bais);
//        while (in.available() > 0) {
//            String element = in.readUTF();
//            System.out.println(element);
//        }

//        for (int i = 0; i < lines.size(); i++) {
//            String buf = (String) lines.get(i);
//            buffer = buf.getBytes();
//            System.out.println(buffer.toString());
//        }
//
//        String data = new String();
//        for (int i = 0; i < buffer.length; i++) {
//            data = new String(buffer);
//            System.out.println(data);
//
//
//        }
//
//


//            byteArray = buf.getBytes();
//            boolean CTE = false;
//            if (buf.startsWith("Content-Transfer-Encoding: ")){
//                encoding = buf.substring(27);
//                System.out.println(encoding);
//            }
//            this.content += buf;
//        }


//
//            if (buf.startsWith("From: ")){
//                from = buf.substring(6);
//                int index = from.indexOf("?B?", 2);
//                String fromEncoding = from.substring(2,index);
//                System.out.println(fromEncoding);
//
//
//
//                boolean isFromEncoding = false;
//                    String regex = "=\\?(?i)"+fromEncoding+"\\?B\\?(.*)\\?=";
//                    Pattern p = Pattern.compile(regex);
//
//
//                    for (Matcher m = p.matcher(buf.substring(5)); m.find(); this.from = MyBase64.getFromBASE64(m.group(1), fromEncoding)) {
//                        isFromEncoding = true;
//                        }
//                System.out.println(from);
//            }
//
//
//            if (buf.startsWith("Subject: ")){
//
//
//
//                subject = buf.substring(9);
//
//                if (subject.startsWith("=?")){
//                int index = subject.indexOf("?B?", 2);
//                String subjectEncoding = subject.substring(2,index);
//                System.out.println(subjectEncoding);
//
//
//
//                boolean isSubjectEncoding = false;
//                String regex = "=\\?(?i)"+subjectEncoding+"\\?B\\?(.*)\\?=";
//                Pattern p = Pattern.compile(regex);
//
//
//                for (Matcher m = p.matcher(buf.substring(5)); m.find(); this.subject = MyBase64.getFromBASE64(m.group(1), subjectEncoding)) {
//                    isSubjectEncoding = true;
//
//                }
//                }
//
//                System.out.println(subject);


//            isPreLine = false;
//
//            if (!buf.startsWith("Subject:")) {
//                if (buf.startsWith("From:")) {
//                    boolean isUTF8 = false;
//                    String regex = /*=\\?(?i)koi8-r\\?B\\?(.*)\\?= |*/ "=\\?(?i)UTF-8\\?B\\?(.*)\\?=";
//                    String regex2 ="";
//                    Pattern p = Pattern.compile(regex);
//                    Pattern p2 = Pattern.compile(regex2);
//
//
//                    for (Matcher m = p.matcher(buf.substring(5)); m.find(); this.from = m.group(1)/*= MyBase64.getFromBASE64(m.group(1))*/) {
//                        isUTF8 = true;
//
//                    }
//
//
//
//
//
//                } else if (buf.endsWith("base64")) {
//                    if (!hasContent) {
//                        decodeWithBase64 = true;
//                        isPreLine = true;
//                    }
//
//                    hasContent = true;
//                }else if (buf.endsWith(encoding)){
//                    if (!hasContent) {
//                        decodeWithBase64 = true;
//                        isPreLine = true;
//                    }
//
//                    hasContent = true;
//                }else if (buf.startsWith("--") || buf.endsWith(encoding)) {
//                    decodeWithBase64 = false;
//                }
//            } else {
//                boolean isUTF8 = false;
//                String regex = "=\\?(?i)UTF-8\\?B\\?(.*)\\?=";
//                Pattern p = Pattern.compile(regex);
//
//                for (Matcher m = p.matcher(buf); m.find(); this.subject = MyBase64.getFromBASE64(m.group(1))) {
//                    isUTF8 = true;
//                }
//
//                if (!isUTF8) {
//                    this.subject = buf.substring(8);
//                }
//                if (this.subject.isEmpty()){
//                    this.subject = "без темы";
//                }
//            }
//
//            if (decodeWithBase64 && !isPreLine){
//                this.content = this.content + buf/*MyBase64.getFromBASE64(buf)*/;
//                }

//                else {
//                this.content = this.content + buf;
//            }

    }





    public String getSubject() {
        return !this.subject.isEmpty() ? this.subject : "без темы";

    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return this.from!=null ? this.from : "аноним";

    }

    public void setFrom(String from) {
        this.from = from;
    }

    public ArrayList<String> getTo_list() {
        return this.to_list;
    }

    public void setTo_list(ArrayList<String> to_list) {
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
