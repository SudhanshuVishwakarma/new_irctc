package org.example;

import org.example.Util.UserServiceUtil;
import org.example.entities.Train;
import org.example.entities.User;
import org.example.services.UserBookingServices;

import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        System.out.println("App is Running now ");

        Scanner scanner = new Scanner(System.in);
        int option = 0;
        UserBookingServices userBookingServices = new UserBookingServices();

        while(option !=7){
            System.out.println("Choose option");
            System.out.println("1. Sign up");
            System.out.println("2. Login");
            System.out.println("3. Fetch Bookings");
            System.out.println("4. Search Trains");
            System.out.println("5. Book a Seat");
            System.out.println("6. Cancel my Booking");
            System.out.println("7. Exit the App");
            option = scanner.nextInt();
            Train trainSelectedForBooking = new Train();
            switch (option){

                case 1:
                    System.out.println("Enter the username to signup");
                    String nameToSignUp = scanner.next();
                    System.out.println("Enter the password to signup");
                    String passwordToSignUp = scanner.next();
                    User userToSignUp = new User(nameToSignUp,passwordToSignUp,UserServiceUtil.hashPasswordI(passwordToSignUp),new ArrayList<>(), UUID.randomUUID().toString());
                    userBookingServices.SignUpUser(userToSignUp);
                    break;

                case 2:
                    System.out.println("Enter the username to login");
                    String nameToLogin = scanner.next();
                    System.out.println("Enter the password to login");
                    String passwordToLogin = scanner.next();
                    User userToLogin = new User(nameToLogin,passwordToLogin,UserServiceUtil.hashPasswordI(passwordToLogin),new ArrayList<>(), UUID.randomUUID().toString());
                    userBookingServices = new UserBookingServices(userToLogin);
                    break;

                case 3:
                    System.out.println("Fetch Booking");
                    userBookingServices.fetchBooking();
                    break;

                case 4:


                    scanner.nextLine(); // consume newline from nextInt()

                    System.out.println("Type your Source Station");
                    String source = scanner.nextLine();

                    System.out.println("Type your Destination Station");
                    String destination = scanner.nextLine();

                    List<Train> trains =
                            userBookingServices.getTrains(source, destination);

                    if (trains.isEmpty()) {
                        System.out.println("No trains found.");
                        break;
                    }

                    int index = 1;
                    for (Train t : trains) {
                        System.out.println(index + " train ID: " + t.getTrainId());

                        for (Map.Entry<String,String> entry :
                                t.getStationTimes().entrySet()) {

                            System.out.println(
                                    "Station " + entry.getKey()
                                            + " Time: " + entry.getValue());
                        }
                        index++;
                    }

                    System.out.println("Select a train by typing 1,2,3...");
                    break;

                case 5:
                    System.out.println("Select a seat out of these seats");
                    List<List<Integer>> seats = userBookingServices.fetchSeats(trainSelectedForBooking);
                    for (List<Integer> row: seats){
                        for (Integer val: row){
                            System.out.print(val+" ");
                        }
                        System.out.println();
                    }
                    System.out.println("Select the seat by typing the row and column");
                    System.out.println("Enter the row");
                    int row = scanner.nextInt();
                    System.out.println("Enter the column");
                    int col = scanner.nextInt();
                    System.out.println("Booking your seat....");
                    Boolean booked = userBookingServices.bookTrainSeat(trainSelectedForBooking, row, col);
                    if(booked.equals(Boolean.TRUE)){
                        System.out.println("Booked! Enjoy your journey");
                    }else{
                        System.out.println("Can't book this seat");
                    }
                    break;
                default:
                    break;

            }
        }
    }

}