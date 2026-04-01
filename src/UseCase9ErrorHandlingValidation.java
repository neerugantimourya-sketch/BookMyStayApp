import java.util.*;

// Custom Exception for Invalid Room Type
class InvalidRoomTypeException extends Exception {
    public InvalidRoomTypeException(String message) {
        super(message);
    }
}

// Custom Exception for Unavailable Room
class RoomNotAvailableException extends Exception {
    public RoomNotAvailableException(String message) {
        super(message);
    }
}

// Custom Exception for Invalid Guest Name
class InvalidGuestNameException extends Exception {
    public InvalidGuestNameException(String message) {
        super(message);
    }
}

// Custom Exception for Invalid Booking ID
class InvalidBookingIdException extends Exception {
    public InvalidBookingIdException(String message) {
        super(message);
    }
}

// Room Inventory
class RoomInventory9 {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory9() {
        inventory.put("Standard", 3);
        inventory.put("Deluxe",   2);
        inventory.put("Suite",    1);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public int getAvailableRooms(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decreaseInventory(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }
}

// Validator class
class InvalidBookingValidator {

    private static final List<String> VALID_ROOM_TYPES =
            Arrays.asList("Standard", "Deluxe", "Suite");

    // Validate guest name
    public void validateGuestName(String guestName) throws InvalidGuestNameException {
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidGuestNameException(
                    "[ERROR] Guest name cannot be null or empty.");
        }
    }

    // Validate booking ID
    public void validateBookingId(String bookingId) throws InvalidBookingIdException {
        if (bookingId == null || bookingId.trim().isEmpty()) {
            throw new InvalidBookingIdException(
                    "[ERROR] Booking ID cannot be null or empty.");
        }
    }

    // Validate room type (case sensitive)
    public void validateRoomType(String roomType) throws InvalidRoomTypeException {
        if (!VALID_ROOM_TYPES.contains(roomType)) {
            throw new InvalidRoomTypeException(
                    "[ERROR] Invalid room type: '" + roomType +
                            "'. Valid types are: " + VALID_ROOM_TYPES);
        }
    }

    // Validate room availability
    public void validateRoomAvailability(String roomType, RoomInventory9 inventory)
            throws RoomNotAvailableException {
        if (inventory.getAvailableRooms(roomType) <= 0) {
            throw new RoomNotAvailableException(
                    "[ERROR] No rooms available for type: " + roomType);
        }
    }
}

// Booking Processor
class BookingProcessor9 {
    private RoomInventory9 inventory;
    private InvalidBookingValidator validator;
    private List<String> confirmedBookings = new ArrayList<>();

    public BookingProcessor9() {
        this.inventory  = new RoomInventory9();
        this.validator  = new InvalidBookingValidator();
    }

    public void processBooking(String bookingId, String guestName, String roomType) {
        System.out.println("\nProcessing Booking -> ID: " + bookingId +
                " | Guest: " + guestName +
                " | RoomType: " + roomType);
        try {
            // Step 1: Validate booking ID
            validator.validateBookingId(bookingId);

            // Step 2: Validate guest name
            validator.validateGuestName(guestName);

            // Step 3: Validate room type (case sensitive)
            validator.validateRoomType(roomType);

            // Step 4: Validate room availability
            validator.validateRoomAvailability(roomType, inventory);

            // Step 5: All validations passed - confirm booking
            inventory.decreaseInventory(roomType);
            confirmedBookings.add(bookingId);
            System.out.println("[SUCCESS] Booking confirmed! ID: " + bookingId +
                    " | Guest: " + guestName +
                    " | Room Type: " + roomType +
                    " | Remaining: " + inventory.getAvailableRooms(roomType));

        } catch (InvalidBookingIdException e) {
            System.out.println(e.getMessage());
        } catch (InvalidGuestNameException e) {
            System.out.println(e.getMessage());
        } catch (InvalidRoomTypeException e) {
            System.out.println(e.getMessage());
        } catch (RoomNotAvailableException e) {
            System.out.println(e.getMessage());
        }
    }

    public void displayConfirmedBookings() {
        System.out.println("\n--- Confirmed Bookings ---");
        if (confirmedBookings.isEmpty()) {
            System.out.println("No confirmed bookings.");
        } else {
            for (String id : confirmedBookings) {
                System.out.println("  Booking ID: " + id);
            }
        }
        System.out.println("Total Confirmed: " + confirmedBookings.size());
    }
}

public class UseCase9ErrorHandlingValidation {
    public static void main(String[] args) {

        System.out.println("=== BookMyStay: Use Case 9 - Error Handling & Validation ===");

        BookingProcessor9 processor = new BookingProcessor9();

        // Valid bookings
        processor.processBooking("BK001", "Alice",   "Deluxe");
        processor.processBooking("BK002", "Bob",     "Suite");
        processor.processBooking("BK003", "Charlie", "Standard");

        // Invalid - wrong room type (case sensitive)
        processor.processBooking("BK004", "Diana",   "deluxe");
        processor.processBooking("BK005", "Eve",     "SUITE");

        // Invalid - empty guest name
        processor.processBooking("BK006", "",        "Standard");

        // Invalid - null booking ID
        processor.processBooking("",      "Frank",   "Deluxe");

        // Invalid - room type does not exist
        processor.processBooking("BK007", "Grace",   "Presidential");

        // Valid - fill remaining Standard rooms
        processor.processBooking("BK008", "Henry",   "Standard");
        processor.processBooking("BK009", "Isla",    "Standard");

        // Invalid - Standard now fully booked
        processor.processBooking("BK010", "Jack",    "Standard");

        // Display all confirmed bookings
        processor.displayConfirmedBookings();

        System.out.println("\nSystem remains stable after all errors.");
        System.out.println("=== End of Use Case 9 ===");
    }
}