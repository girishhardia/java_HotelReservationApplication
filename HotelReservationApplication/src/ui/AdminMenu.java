package ui;

import api.AdminResource;
import model.Customer;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

/**
 * Admin command line interface for the hotel reservation application.
 */
public class AdminMenu {
    private static final AdminResource adminResource = AdminResource.getInstance();

    public void displayMenu(Scanner scanner) { // Accept scanner from MainMenu
        boolean backToMain = false;

        while (!backToMain) {
            printAdminMenu();
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
                    displayAllCustomers();
                    break;
                case 2:
                    displayAllRooms();
                    break;
                case 3:
                    displayAllReservations();
                    break;
                case 4:
                    addRoom(scanner); // Pass existing scanner
                    break;
                case 5:
                    backToMain = true;
                    break;
                default:
                    System.out.println("Invalid selection. Please try again.");
            }
        }
    }

    private void printAdminMenu() {
        System.out.println("\nAdmin Menu");
        System.out.println("--------------------------------------------");
        System.out.println("1. See all Customers");
        System.out.println("2. See all Rooms");
        System.out.println("3. See all Reservations");
        System.out.println("4. Add a Room");
        System.out.println("5. Back to Main Menu");
        System.out.println("--------------------------------------------");
        System.out.println("Please select a number for the menu option:");
    }

    private void displayAllCustomers() {
        Collection<Customer> customers = adminResource.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            for (Customer customer : customers) {
                System.out.println(customer);
            }
        }
    }

    private void displayAllRooms() {
        Collection<IRoom> rooms = adminResource.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("No rooms found.");
        } else {
            for (IRoom room : rooms) {
                System.out.println(room);
            }
        }
    }

    private void displayAllReservations() {
        adminResource.displayAllReservations();
    }

    private void addRoom(Scanner scanner) {
        System.out.println("Enter room number:");
        String roomNumber = scanner.nextLine();
        
        System.out.println("Enter price per night:");
        Double price = 0.0;
        try {
            price = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid price. Room creation failed.");
            return;
        }

        System.out.println("Enter room type (1 for SINGLE, 2 for DOUBLE):");
        String typeInput = scanner.nextLine();
        RoomType roomType = null;
        if ("1".equals(typeInput)) {
            roomType = RoomType.SINGLE;
        } else if ("2".equals(typeInput)) {
            roomType = RoomType.DOUBLE;
        } else {
            System.out.println("Invalid type. Defaulting to SINGLE."); // Prompt didn't specify strictness
            roomType = RoomType.SINGLE;
        }

        Room room;
        if (price == 0.0) {
            room = new model.FreeRoom(roomNumber, roomType);
        } else {
            room = new Room(roomNumber, price, roomType);
        }
        adminResource.addRoom(Collections.singletonList(room));
        System.out.println("Room added successfully!");
        
        System.out.println("Would you like to add another room? (y/n)");
        String response = scanner.nextLine();
        if ("y".equalsIgnoreCase(response)) {
            addRoom(scanner);
        }
    }
}
