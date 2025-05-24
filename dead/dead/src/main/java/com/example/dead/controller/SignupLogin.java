package com.example.dead.controller;


import com.example.dead.model.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SignupLogin {
    private static SignupLogin instance;

    private SignupLogin() {
    }

    public static SignupLogin getInstance() {
        if (instance == null) {
            instance = new SignupLogin();
        }
        return instance;
    }

    public List<Object> login(String username, String password) {
        List<Object> list = new ArrayList<>();
        User user = DataBaseController.getInstance().getUsername(username);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                list.add("login success");
                list.add(user);
            } else
                list.add("login invalidPassword");
        } else
            list.add("login invalidUsername");
        return list;
    }

    public List<Object> checkSignup(String username, String email) {
        List<Object> list = new ArrayList<>();
        if (DataBaseController.getInstance().getUsernames().contains(username))
            list.add("checkSignup usernameExists");
        else {
            list.add("checkSignup success");
            EmailVerification(email, "signup");
        }
        return list;
    }

    public List<Object> signup(List<String> values) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(values.get(6), formatter);
        User user = new User(Long.parseLong(values.get(1)), values.get(2), values.get(3), values.get(4), values.get(5), localDate, values.get(7), values.get(8), values.get(9), new HashSet<>(Arrays.asList(values.get(10).split(" , "))), new HashSet<>());
        user.setId(DataBaseController.getInstance().getNextId());

        register(user);

        List<Object> list = new ArrayList<>();
        list.add("signup success");
        list.add(user);

        return list;
    }

    private void register(User user) {
        DataBaseController.getInstance().getTrieTree().insert(user.getUserName(), user);
        DataBaseController.getInstance().getUsernames().add(user.getUserName());
        DataBaseController.getInstance().getUsers().add(user);

        GraphController.getInstance().addUser(user);
        GraphController.getInstance().addToDegreeMap(new ArrayList<>() {{
            add(user);
        }});

        DataBaseController.getInstance().addToTable(user);
    }

    public List<Object> checkParameter(String username, String email) {
        List<Object> list = new ArrayList<>();
        User user = DataBaseController.getInstance().getUsername(username);

        if (user == null)
            list.add("checkParameter usernameIsNotFound");
        else if (!user.getEmail().equals(email))
            list.add("checkParameter theEmailDoesNotMatch");
        else {
            list.add("checkParameter success");
            EmailVerification(email, "login");
        }

        return list;
    }

    private void EmailVerification(String email, String text) {
        String code = generateVerificationCode();
        storeCode(email, code);

        sendEmail(email, code, text);
    }

    public List<Object> verifyCode(String username, String email, String enteredCode) {
        String str = "";
        User user = DataBaseController.getInstance().getUsername(username);
        if (user != null)
            str += "login";
        else
            str += "signup";

        List<Object> list = new ArrayList<>();
        if (DataBaseController.getInstance().getVerificationCodes().containsKey(email) && DataBaseController.getInstance().getVerificationCodes().get(email).equals(enteredCode)) {
            list.add("verifyCode valid " + str);
            if (str.equals("login"))
                list.add(user);
        } else
            list.add("verifyCode invalid");

        return list;
    }

    private void storeCode(String email, String code) {
        DataBaseController.getInstance().getVerificationCodes().put(email, code);
    }

    private void sendEmail(String recipientEmail, String code, String text) {
        String senderEmail = "tierconnectionup@gmail.com";
        String senderPassword = "auxa qwtj uyss ugyi";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        session.setDebug(false);


        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            if (text.equalsIgnoreCase("signup"))
                message.setSubject("Confirmation code");
            else
                message.setSubject("Password Reset Code");
            message.setText("Your verification code is: " + code);

            Transport.send(message);
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
}
