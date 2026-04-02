package org.example;

import org.example.Util.UserServiceUtil;
import org.example.entities.User;
import org.example.services.UserBookingServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        System.out.println("App is Running now ");
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        UserBookingServices userBookingServices;
        try {
            userBookingServices = new UserBookingServices();
        } catch (IOException ex) {
            System.out.println("Their is something wrong");
            return;
        }
        while(option !=7){
            System.out.println("1. Choose option");
            System.out.println("2. Login");
            System.out.println("3. Fetch Booking");
            System.out.println("4. Search Train");
            System.out.println("5. Book a seat");
            System.out.println("6. Cancel Booking");
            System.out.println("7. Exit");
            option = scanner.nextInt();
            switch (option){
                case 1:
                    System.out.println("Enter the username to signup");
                    String nameToSignUp = scanner.next();
                    System.out.println("Enter the password to signup");
                    String passwordToSignUp = scanner.next();
                    User userToSignUp = new User(nameToSignUp,passwordToSignUp, UserServiceUtil.hashPasswordI(passwordToSignUp),new ArrayList<>(), UUID.randomUUID().toString());
                    userBookingServices.SignUpUser(userToSignUp);
                    break;
                case 2:
                    System.out.println("Enter the username to login");
                    String nameToLogin = scanner.next();
                    System.out.println("Enter the password to login");
                    String passwordToLogin = scanner.next();
                    User userToLogin = new User(nameToLogin,passwordToLogin,UserServiceUtil.hashPasswordI(passwordToLogin),new ArrayList<>(), UUID.randomUUID().toString());
                    try {
                        userBookingServices = new UserBookingServices(userToLogin);
                    } catch (IOException ex) {
                        System.out.println("Their is something wrong");
                        return;
                    }
                    break;
            }
        }
    }

}