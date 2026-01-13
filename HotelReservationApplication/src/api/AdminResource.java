package api;

import model.Customer;
import model.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.List;

/**
 * Interface for the admin operations.
 */
public class AdminResource {
    private static final AdminResource INSTANCE = new AdminResource();
    private final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();

    private AdminResource() {}

    public static AdminResource getInstance() {
        return INSTANCE;
    }

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public void addRoom(List<IRoom> rooms) {
        for (IRoom room : rooms) {
            reservationService.addRoom(room);
        }
    }

    public Collection<IRoom> getAllRooms() {
        // ReservationService didn't explicitly ask for getAllRooms, but we need it here.
        // The prompt for ReservationService asked for "public Collection<IRoom> findRooms(Date checklnDate, Date checkOutDate)".
        // It didn't ask for a method to get *all* rooms. 
        // I will need to iterate through all possible rooms or add a method to ReservationService. 
        // Let's first check ReservationService. Java's Collection interface usually allows access. 
        // The prompt for ReservationService "public IRoom getARoom(String roomld)" and "public void addRoom(IRoom room)".
        // It didn't strictly say getAllRooms.
        // However, I can implement it in ReservationService for this purpose, or just iterate if I had access.
        // Wait, I am writing AdminResource, I should probably add getAllRooms to ReservationService or use a workaround.
        // The prompt for AdminResource says "Collection<IRoom> getA11Rooms()".
        // Let's modify ReservationService to verify if I missed it or should add it.
        // Ah, checked ReservationService requirements:
        // "public Collection<IRoom> findRooms(Date checklnDate, Date checkOutDate)"
        // "public void printA11Reservation()"
        // It seems I need to add getAllRooms to ReservationService to support AdminResource properly.
        // I'll add it to ReservationService first? Or just try to cast/access? 
        // Better to add `getAllRooms()` to `ReservationService`. 
        // For now, I'll assume I can add it or it exists (I'll patch ReservationService if needed).
        // Wait, I just wrote ReservationService. It has `rooms` map.
        // I will add `getAllRooms` to ReservationService or call `rooms.values()` if accessible. 
        // It's private.
        // I will assume I should update ReservationService. But first, let's write AdminResource relying on it.
        // I will execute a multi_replace on ReservationService to add getAllRooms.
        return reservationService.getAllRooms(); 
    }

    public Collection<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    public void displayAllReservations() {
        reservationService.printAllReservation();
    }
}
