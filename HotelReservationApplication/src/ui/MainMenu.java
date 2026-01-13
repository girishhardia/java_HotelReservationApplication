package ui;

import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

/**
 * Main command line interface for the hotel reservation application.
 */
public class MainMenu {
    private static final HotelResource hotelResource = HotelResource.getInstance();
    private static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";

    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu();
        mainMenu.displayMenu();
    }

    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            printMainMenu();
            if (!scanner.hasNextLine()) {
                break;
            }
            String line = scanner.nextLine();
            int selection = 0;
            
            try {
                selection = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                continue;
            }

            switch (selection) {
                case 1:
                    findAndReserveRoom(scanner);
                    break;
                case 2:
                    seeMyReservations(scanner);
                    break;
                case 3:
                    createAccount(scanner);
                    break;
                case 4:
                    openAdminMenu(scanner);
                    break;
                case 5:
                    exit = true;
                    System.out.println("Exiting application.");
                    break;
                default:
                    System.out.println("Invalid selection. Please try again.");
            }
        }
        scanner.close(); // Careful with closing System.in if other parts use it, but here it's main.
    }

    private void printMainMenu() {
        System.out.println("\nWelcome to the Hotel Reservation Application");
        System.out.println("--------------------------------------------");
        System.out.println("1. Find and reserve a room");
        System.out.println("2. See my reservations");
        System.out.println("3. Create an account");
        System.out.println("4. Admin");
        System.out.println("5. Exit");
        System.out.println("--------------------------------------------");
        System.out.println("Please select a number for the menu option:");
    }

    private void findAndReserveRoom(Scanner scanner) {
        // Basic stub for search logic. Full 7-day logic to be refined if needed.
        System.out.println("Enter Check-In Date (" + DEFAULT_DATE_FORMAT + "):");
        Date checkIn = getDateInput(scanner);
        if (checkIn == null) return;

        System.out.println("Enter Check-Out Date (" + DEFAULT_DATE_FORMAT + "):");
        Date checkOut = getDateInput(scanner);
        if (checkOut == null) return;

        if (checkOut.before(checkIn)) {
            System.out.println("Check-Out date must be after Check-In date.");
            return;
        }

        Collection<IRoom> rooms = hotelResource.findARoom(checkIn, checkOut);

        if (rooms.isEmpty()) {
            System.out.println("No rooms found for the selected dates. Searching for recommended rooms (7 days shift)...");
             // Add 7 days logic
             Date newCheckIn = addDays(checkIn, 7);
             Date newCheckOut = addDays(checkOut, 7);
             rooms = hotelResource.findARoom(newCheckIn, newCheckOut);
             if (rooms.isEmpty()) {
                 System.out.println("No rooms found for recommended dates either.");
                 return;
             }
             System.out.println("Rooms found for recommended dates: " + newCheckIn + " - " + newCheckOut);
             // Should verify if user accepts these dates, but for now just show rooms.
             // Usually we would update checkIn/checkOut to these new dates if we proceed.
             checkIn = newCheckIn;
             checkOut = newCheckOut;
        }

        for (IRoom room : rooms) {
            System.out.println(room);
        }

        System.out.println("Would you like to book a room? (y/n)");
        String bookResponse = scanner.nextLine();
        if ("y".equalsIgnoreCase(bookResponse)) {
            System.out.println("Do you have an account with us? (y/n)");
            String accountResponse = scanner.nextLine();
            
            if ("y".equalsIgnoreCase(accountResponse)) {
                System.out.println("Enter Email format: name@domain.com");
                String email = scanner.nextLine();
                Customer customer = hotelResource.getCustomer(email);
                
                if (customer == null) {
                    System.out.println("Customer not found.");
                    return;
                }

                System.out.println("What room number would you like to reserve?");
                String roomNumber = scanner.nextLine();
                
                IRoom room = hotelResource.getRoom(roomNumber);
                // Ideally check if room is in the list of available rooms
                if (room == null) { 
                     System.out.println("Room not found.");
                     return;
                }
                
                // Assuming availability check again or relying on service
                Reservation reservation = hotelResource.bookARoom(email, room, checkIn, checkOut);
                System.out.println("Reservation created successfully!");
                System.out.println(reservation);

            } else {
                System.out.println("Please create an account first.");
                // Could call createAccount(scanner) here
            }
        }
    }

    private Date getDateInput(Scanner scanner) {
        String input = scanner.nextLine();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        try {
            return dateFormat.parse(input);
        } catch (ParseException e) {
            System.out.println("Invalid date format.");
            return null;
        }
    }
    
    // Helper to add days
    private Date addDays(Date date, int days) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        cal.add(java.util.Calendar.DATE, days);
        return cal.getTime();
    }

    private void seeMyReservations(Scanner scanner) {
        System.out.println("Enter Email format: name@domain.com");
        String email = scanner.nextLine();
        Collection<Reservation> reservations = hotelResource.getCustomersReservations(email);
        
        if (reservations.isEmpty()) {
            System.out.println("No reservations found for this email.");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        }
    }

    private void createAccount(Scanner scanner) {
        System.out.println("Enter Email format: name@domain.com");
        String email = scanner.nextLine();
        System.out.println("First Name:");
        String firstName = scanner.nextLine();
        System.out.println("Last Name:");
        String lastName = scanner.nextLine();

        try {
            hotelResource.createACustomer(email, firstName, lastName);
            System.out.println("Account created successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating account: " + e.getMessage());
        }
    }

    private void openAdminMenu(Scanner scanner) {
        AdminMenu adminMenu = new AdminMenu();
        adminMenu.displayMenu(scanner); 
    }
}
