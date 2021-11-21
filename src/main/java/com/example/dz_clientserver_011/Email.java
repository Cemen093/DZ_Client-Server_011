package com.example.dz_clientserver_011;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Email {
    private static final String DIR = "./login/";
    private static final String FILENAME_EMAILS = "userEmail.txt";
    private String email;
    private String password;

    public Email(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public static List<Email> getEmails(String login){
        List<Email> mails = new ArrayList<>();
        String fileContent = readFile(DIR+FILENAME_EMAILS);
        if (fileContent == null){
            return mails;
        }

        final String regex = "Login:"+login+";Email:([^;]+)"+";Password:([^;]+);";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(fileContent);
        while (matcher.find()){
            mails.add(new Email(matcher.group(1), matcher.group(2)));
        }
        return mails;
    }

    public boolean save(String login){
        File file = new File(DIR, FILENAME_EMAILS);
        if (!file.getParentFile().exists()){
            if (!file.getParentFile().mkdirs()){
                return false;
            }
        }

        if (!file.exists()){
            try {
                if (!file.createNewFile()){
                    return false;
                }
            } catch (IOException e) {
                return false;
            }
        }

        try(FileWriter writer = new FileWriter(file.getAbsolutePath(), true))
        {
            writer.write("Login:"+login+";Email:"+email+";Password:"+password+";\r\n");
            writer.flush();
            writer.close();
        }
        catch(IOException ex){
            return false;
        }

        return true;
    }

    public List<Letter> getLetters(){
        List<Letter> letters = new ArrayList<>();
        try {
            final String host = "imap.gmail.com";

            // Создание свойств
            Properties props = new Properties();

            //Указываем протокол - IMAP с SSL
            props.put("mail.store.protocol", "imaps");
            Session session = Session.getInstance(props);
            Store store = session.getStore();

            //подключаемся к почтовому серверу
            store.connect(host, email, password);

            //получаем папку с входящими сообщениями
            Folder inbox = store.getFolder("INBOX");

            //открываем её только для чтения
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();
            for (Message message : messages) {
                letters.add(new Letter(message));
            }

            store.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return letters;
    }

    private static String readFile(String path){
        StringBuilder sb = new StringBuilder();
        try(FileReader reader = new FileReader(path))
        {
            char[] buf = new char[256];
            int c;
            while((c = reader.read(buf))>0){

                if(c < 256){
                    buf = Arrays.copyOf(buf, c);
                }
                sb.append(buf);
            }
        }
        catch(IOException ex){
            return null;
        }
        return sb.toString();
    }

    public void sendLetter(String[] toEmails, String subject, String text, String[] pathsAttachment){
        Session session;
        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);


            Properties props = (Properties) System.getProperties().clone();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.port", "587");
            props.put("mail.smtp.auth", true);
            props.put("mail.smtp.ssl.enable", false);
            props.put("mail.smtp.starttls.enable", true);

            session = Session.getInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(email, password);
                }
            });
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return;
        }

        for (String toEmail: toEmails) {
            try {
                // Create a default MimeMessage object.
                Message message = new MimeMessage(session);

                // Set From: header field of the header.
                message.setFrom(new InternetAddress(email));

                // Set To: header field of the header.
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(toEmail));

                // Set Subject: header field
                message.setSubject(subject);

                // Create the message part
                BodyPart messageBodyPart = new MimeBodyPart();

                // Now set the actual message
                messageBodyPart.setText(text);

                // Create a multipar message
                Multipart multipart = new MimeMultipart();

                // Set text message part
                multipart.addBodyPart(messageBodyPart);

                // Part two is attachment
                for (String path : pathsAttachment) {
                    messageBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(path);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(new File(path).getName());
                    multipart.addBodyPart(messageBodyPart);
                }

                // Send the complete message parts
                message.setContent(multipart);

                // Send message
                Transport.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
