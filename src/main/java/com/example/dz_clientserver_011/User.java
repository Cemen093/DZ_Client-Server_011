package com.example.dz_clientserver_011;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class User {
    private static final String DIR = "./login/";
    private static final String FILENAME_LOGINS = "userLogin.txt";
    private static final String FILENAME_REMEMBER_USER = "rememberUser.txt";
    private String login;
    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    private static List<User> getUsers(){
        List<User> users = new ArrayList<>();
        String fileContent = readFile(DIR+FILENAME_LOGINS);
        if (fileContent == null){
            return users;
        }

        final String regex = "Login:([^;]+);Password:([^;]+);";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(fileContent);
        while (matcher.find()){
            users.add(new User(matcher.group(1), matcher.group(2)));
        }
        return users;
    }

    public static boolean contains(User user){
        return User.getUsers().contains(user);
    }

    public boolean saveUser(){
        File file = new File(DIR, FILENAME_LOGINS);
        return saveUser(file);
    }

    public void saveAsRememberUser(){
        File file = new File(DIR, FILENAME_REMEMBER_USER);
        saveUser(file);
    }

    public boolean saveUser(File file){
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
            writer.write("Login:"+login+";Password:"+password+";\r\n");
            writer.flush();
            writer.close();
        }
        catch(IOException ex){
            return false;
        }

        return true;
    }

    public static User getUser(String login){
        User _user = new User(login, "");
        for (User user : getUsers()) {
            if (user.equals(_user)){
                return user;
            }
        }
        return null;
    }

    public static User getRememberUser(){
        User user = null;
        String fileContent = readFile(DIR+FILENAME_REMEMBER_USER);
        if (fileContent == null){
            return user;
        }

        final String regex = "Login:([^;]+);Password:([^;]+);";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(fileContent);
        if (matcher.find()){
            user = new User(matcher.group(1), matcher.group(2));
        }
        return user;
    }

    public static void cleanRememberUser(){
        File file = new File(DIR, FILENAME_REMEMBER_USER);
        file.delete();
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)){
            return false;
        }
        return this.login.equals(((User)obj).getLogin());
    }
}
