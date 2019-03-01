package model;

import java.io.*;
import java.util.ArrayList;
import org.apache.james.mime4j.message.BinaryBody;
import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.message.Entity;
import org.apache.james.mime4j.message.Message;
import org.apache.james.mime4j.message.Multipart;
import org.apache.james.mime4j.message.TextBody;
import org.apache.james.mime4j.parser.Field;

public class MailParser {

    private StringBuffer txtBody;
    private StringBuffer htmlBody;
    private ArrayList<BodyPart> attachments;

    public void parse(InputStream is) {
        FileInputStream file = null;

        txtBody = new StringBuffer();
        htmlBody = new StringBuffer();
        attachments = new ArrayList<BodyPart>();

        try {

            Message mimeMsg = new Message(is);

            //Получаем стандартные заголовки
            System.out.println("To: " + mimeMsg.getTo());
            System.out.println("From: " + mimeMsg.getFrom());
            System.out.println("Subject: " + mimeMsg.getSubject());

            //Получение собственных заголовков по имени
            Field priorityFld = mimeMsg.getHeader().getField("X-Priority");
            //если заголовок не найден, то возвращаем null
            if (priorityFld != null) {
                System.out.println("Priority: " + priorityFld.getBody());
            }

            //Если сообщение содержит много частей, то разбираем все части
            if (mimeMsg.isMultipart()) {
                Multipart multipart = (Multipart) mimeMsg.getBody();
                parseBodyParts(multipart);
            } else {
                //если сообщение состоит с одной части, то просто выводим тело сообщения
                String text = getTxtPart(mimeMsg);
                txtBody.append(text);
            }

            //Выводим текст сообщение и HTML теги
            System.out.println("Text body: " + txtBody.toString());
            System.out.println("Html body: " + htmlBody.toString());

            for (BodyPart attach : attachments) {
                String attName = attach.getFilename();
                //Создайте файл с указанным именем
                FileOutputStream fos = new FileOutputStream(attName);
                try {
                    //Получаем поток прикрепленных файлов и записываем из в файл
                    BinaryBody bb = (BinaryBody) attach.getBody();
                    bb.writeTo(fos);
                } finally {
                    fos.close();
                }
            }

        } catch (IOException ex) {
            ex.fillInStackTrace();
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // Этот метод парсит прикрепленные файлы
    private void parseBodyParts(Multipart multipart) throws IOException {
        for (BodyPart part : multipart.getBodyParts()) {
            if (part.isMimeType("text/plain")) {
                String txt = getTxtPart(part);
                txtBody.append(txt);
            } else if (part.isMimeType("text/html")) {
                String html = getTxtPart(part);
                htmlBody.append(html);
            } else if (part.getDispositionType() != null && !part.getDispositionType().equals("")) {
                //Если DispositionType равен null или empty, это значит, что прикреплённые файлы отсуствуют
                attachments.add(part);
            }

            //Если сообщение содержит несколько прикрепленных файлов то вызываем метод рекурсивно
            if (part.isMultipart()) {
                parseBodyParts((Multipart) part.getBody());
            }
        }
    }

    private String getTxtPart(Entity part) throws IOException {
        //Получаем тело содержимого
        TextBody tb = (TextBody) part.getBody();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        tb.writeTo(baos);
        return new String(baos.toByteArray());
    }

}