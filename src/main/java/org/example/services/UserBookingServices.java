package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import org.example.Util.UserServiceUtil;
import org.example.entities.Train;
import org.example.entities.User;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Scanner;

public class UserBookingServices {
    private User user;

    // ObjectMapper ka use serialization aur deserialization ke liye hota hai
    // (Java objects ko JSON me convert karna aur JSON ko object me convert karna)
    //    Class
    // ├── variables
    // ├── constructor
    // ├── methods


    private final ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    private List<User> userList = new ArrayList<>();

    private static final String USER_PATH = "src/main/java/org/example/localDB/Users.json";

    public UserBookingServices(User user1) {
        this.user = user1;
        loadUser();

    }
    public UserBookingServices(){
        loadUser();
    }

    // Make this method resilient: try classpath resource first, then fallback to file system.
    // Do not throw IOException here; if loading fails return an empty list so callers don't have to handle.
    public List<User> loadUser() {
        try (InputStream is = getClass().getResourceAsStream("/org/example/localDB/Users.json")) {
            if (is != null) {
                userList = objectMapper.readValue(is, new TypeReference<List<User>>(){});
                return userList;
            }
        } catch (IOException ignored) {
            // we'll try file fallback below
        }

        try {
            File users = new File(USER_PATH);
            if (users.exists()) {
                userList = objectMapper.readValue(users, new TypeReference<List<User>>(){});
            } else {
                userList = new ArrayList<>();
            }
        } catch (IOException ex) {
            // If reading still fails, initialize an empty list to avoid throwing from constructor
            userList = new ArrayList<>();
        }
        return userList;
    }
    //          Object UserServiceUtil = null; tum class ko variable bana rahe ho, isliye checkPassword() red aa raha ho
    //          Ab compiler confuse ho gaya:
    //          UserServiceUtil.checkPassword()//Compiler soch raha:
    //          "Ye Object type variable hai — isme checkPassword method nahi hai."  // Isliye red underline.

    public Boolean loginUser() {
        Optional<User> foundUser = userList.stream().filter(user1 ->
                user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword())
        ).findFirst();

        return foundUser.isPresent();
    }

    public boolean SignUpUser(User user1) {
        try {
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (IOException ex) {
            return Boolean.FALSE;
        }

    }

    private void saveUserListToFile() throws IOException {
        File userFile = new File(USER_PATH);
        objectMapper.writeValue(userFile, userList);
    }

    public void  fetchBooking(){
        user.printTickets();
    }

    public Boolean cancelBooking(String ticketId){

        Scanner s = new Scanner(System.in);
        System.out.println("Enter the ticket id to cancel");
        ticketId = s.next();

        if (ticketId == null || ticketId.isEmpty()) {
            System.out.println("Ticket ID cannot be null or empty.");
            return Boolean.FALSE;
        }

        String finalTicketId1 = ticketId;  //Because strings are immutable
        boolean removed = user.getTicketBooked().removeIf(ticket -> ticket.getTicketId().equals(finalTicketId1));

        String finalTicketId = ticketId;
        user.getTicketBooked().removeIf(Ticket -> Ticket.getTicketId().equals(finalTicketId));
        if (removed) {
            System.out.println("Ticket with ID " + ticketId + " has been canceled.");
            return Boolean.TRUE;
        }else{
            System.out.println("No ticket found with ID " + ticketId);
            return Boolean.FALSE;
        }
    }


    public List<Train> getTrains(String source, String destination){
        try{
            TrainServices trainService = new TrainServices();
            return trainService.searchTrains(source, destination);
        }catch(IOException ex){
            return new ArrayList<>();
        }
    }
    public List<List<Integer>> fetchSeats(Train train){
        return train.getSeats();
    }
    public Boolean bookTrainSeat(Train train, int row, int seat){
        try{
            TrainServices trainServices= new TrainServices();
            List<List<Integer>> seats = train.getSeats();
            if(row >= 0 && row < seats.size() && seat >= 0 && seat < seats.get(row).size()){
                if (seats.get(row).get(seat)==0){
                    seats.get(row).set(seat, 1);
                    train.setSeats(seats);
                    trainServices.addTrain(train);
                    return true;
                }else {
                    return false;
                }
            }else {
                return false;
            }
        }catch (IOException ex){
            return Boolean.FALSE;

        }
    }
}
