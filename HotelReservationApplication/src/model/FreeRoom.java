package model;

/**
 * Represents a free room in the hotel.
 */
public class FreeRoom extends Room {
    public FreeRoom(String roomNumber, RoomType enumeration) {
        super(roomNumber, 0.0, enumeration);
    }

    @Override
    public String toString() {
        return "FreeRoom Number: " + getRoomNumber() + " Price: $Free Enumeration: " + getRoomType();
    }
}
