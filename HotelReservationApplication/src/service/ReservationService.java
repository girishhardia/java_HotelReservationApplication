package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.*;

/**
 * Service class for managing reservations and rooms.
 */
public class ReservationService {
    // Static reference for Singleton pattern
    private static final ReservationService INSTANCE = new ReservationService();

    private final Map<String, IRoom> rooms = new HashMap<>();
    private final Set<Reservation> reservations = new HashSet<>();

    private ReservationService() {}

    public static ReservationService getInstance() {
        return INSTANCE;
    }

    public void addRoom(IRoom room) {
        if (room != null) {
            rooms.put(room.getRoomNumber(), room);
        }
    }

    public IRoom getARoom(String roomId) {
        return rooms.get(roomId);
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        if (isRoomReserved(room, checkInDate, checkOutDate)) {
            throw new IllegalArgumentException("Room is already reserved for this date range.");
        }
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(reservation);
        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        return findRooms(checkInDate, checkOutDate, rooms.values());
    }

    // Helper method to find rooms from a specific collection
    private Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate, Collection<IRoom> roomsToCheck) {
        List<IRoom> availableRooms = new ArrayList<>();
        
        for (IRoom room : roomsToCheck) {
            if (!isRoomReserved(room, checkInDate, checkOutDate)) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public Collection<IRoom> getAllRooms() {
        return rooms.values();
    }

    boolean isRoomReserved(IRoom room, Date checkInDate, Date checkOutDate) {
        for (Reservation reservation : reservations) {
            if (reservation.getRoom().equals(room)) {
                if (checkOverlap(reservation, checkInDate, checkOutDate)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkOverlap(Reservation reservation, Date checkInDate, Date checkOutDate) {
        // Overlap logic: StartA < EndB && EndA > StartB
        return checkInDate.before(reservation.getCheckOutDate()) && checkOutDate.after(reservation.getCheckInDate());
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        List<Reservation> customerReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getCustomer().equals(customer)) {
                customerReservations.add(reservation);
            }
        }
        return customerReservations;
    }

    public void printAllReservation() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        }
    }
    
    // Additional method possibly useful for Admin resource, strictly fitting the "Viewing all of the hotel reservations" scenario
    public Collection<Reservation> getAllReservations() {
        return new ArrayList<>(reservations);
    }
}
