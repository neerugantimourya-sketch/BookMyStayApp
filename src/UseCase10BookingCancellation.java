import java.util.*;

// Represents a confirmed reservation
class Reservation10 {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation10(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName     = guestName;
        this.roomType      = roomType;
        this.roomId        = roomId;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName()     { return guestName; }
    public String getRoomType()      { return roomType; }
    public String getRoomId()        { return roomId; }

    @Override
    public String toString() {
        return "ID: " + reservationId +
                " | Guest: " + guestName +
                " | RoomType: " + roomType +
                " | RoomID: " + roomId;
    }
}

// Manages room inventory
class RoomInventory10 {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory10() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe",   2);
        inventory.put("Suite",    1);
    }

    public int getAvailableRooms(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decreaseInventory(String roomType) {
        if (inventory.containsKey(roomType) && inventory.get(roomType) > 0) {
            inventory.put(roomType, inventory.get(roomType) - 1);
        }
    }

    public void increaseInventory(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("  " + entry.getKey() + " : " + entry.getValue() + " available");
        }
    }
}

// Cancellation Service with Stack-based Rollback
class CancellationService {
    private Map<String, Reservation10> confirmedBookings;
    private RoomInventory10 inventory;
    private Stack<String> rollbackStack;       // Stores recently released room IDs
    private List<String> cancelledBookings;    // Tracks cancelled reservation IDs

    public CancellationService(Map<String, Reservation10> confirmedBookings,
                               RoomInventory10 inventory) {
        this.confirmedBookings = confirmedBookings;
        this.inventory         = inventory;
        this.rollbackStack     = new Stack<>();
        this.cancelledBookings = new ArrayList<>();
    }

    public void cancelBooking(String reservationId) {
        System.out.println("\nCancellation Request -> Reservation ID: " + reservationId);

        // Validate: check if reservation exists
        if (!confirmedBookings.containsKey(reservationId)) {
            System.out.println("[ERROR] Reservation ID '" + reservationId +
                    "' not found or already cancelled.");
            return;
        }

        // Validate: check if already cancelled
        if (cancelledBookings.contains(reservationId)) {
            System.out.println("[ERROR] Reservation ID '" + reservationId +
                    "' has already been cancelled.");
            return;
        }

        // Get reservation details
        Reservation10 reservation = confirmedBookings.get(reservationId);

        // Step 1: Push released room ID onto rollback stack (LIFO)
        rollbackStack.push(reservation.getRoomId());

        // Step 2: Restore inventory count
        inventory.increaseInventory(reservation.getRoomType());

        // Step 3: Remove from confirmed bookings
        confirmedBookings.remove(reservationId);

        // Step 4: Add to cancelled list
        cancelledBookings.add(reservationId);

        System.out.println("[SUCCESS] Booking cancelled successfully!");
        System.out.println("  Released Room   : " + reservation.getRoomId());
        System.out.println("  Room Type       : " + reservation.getRoomType());
        System.out.println("  Guest           : " + reservation.getGuestName());
        System.out.println("  Rollback Stack  : " + rollbackStack);
        System.out.println("  Updated Inventory for '" + reservation.getRoomType() +
                "': " + inventory.getAvailableRooms(reservation.getRoomType()) +
                " available");
    }

    public void displayRollbackStack() {
        System.out.println("\n--- Rollback Stack (LIFO - Released Room IDs) ---");
        if (rollbackStack.isEmpty()) {
            System.out.println("  Rollback stack is empty.");
        } else {
            System.out.println("  Stack (top -> bottom): " + rollbackStack);
        }
    }

    public void displayCancelledBookings() {
        System.out.println("\n--- Cancelled Bookings ---");
        if (cancelledBookings.isEmpty()) {
            System.out.println("  No cancelled bookings.");
        } else {
            for (String id : cancelledBookings) {
                System.out.println("  Reservation ID: " + id);
            }
        }
        System.out.println("  Total Cancelled: " + cancelledBookings.size());
    }

    public void displayConfirmedBookings() {
        System.out.println("\n--- Remaining Confirmed Bookings ---");
        if (confirmedBookings.isEmpty()) {
            System.out.println("  No confirmed bookings remaining.");
        } else {
            for (Map.Entry<String, Reservation10> entry : confirmedBookings.entrySet()) {
                System.out.println("  " + entry.getValue());
            }
        }
        System.out.println("  Total Confirmed: " + confirmedBookings.size());
    }
}

public class UseCase10BookingCancellation {
    public static void main(String[] args) {

        System.out.println("=== BookMyStay: Use Case 10 - Booking Cancellation & Inventory Rollback ===\n");

        // Step 1: Setup inventory
        RoomInventory10 inventory = new RoomInventory10();

        // Step 2: Pre-load confirmed bookings
        Map<String, Reservation10> confirmedBookings = new LinkedHashMap<>();
        confirmedBookings.put("RES001", new Reservation10("RES001", "Alice",   "Deluxe",   "D101"));
        confirmedBookings.put("RES002", new Reservation10("RES002", "Bob",     "Suite",    "S201"));
        confirmedBookings.put("RES003", new Reservation10("RES003", "Charlie", "Standard", "ST301"));
        confirmedBookings.put("RES004", new Reservation10("RES004", "Diana",   "Deluxe",   "D102"));
        confirmedBookings.put("RES005", new Reservation10("RES005", "Eve",     "Standard", "ST302"));

        // Adjust inventory to reflect pre-loaded bookings
        inventory.decreaseInventory("Deluxe");
        inventory.decreaseInventory("Deluxe");
        inventory.decreaseInventory("Suite");
        inventory.decreaseInventory("Standard");
        inventory.decreaseInventory("Standard");

        System.out.println("--- Initial Confirmed Bookings ---");
        for (Map.Entry<String, Reservation10> entry : confirmedBookings.entrySet()) {
            System.out.println("  " + entry.getValue());
        }
        inventory.displayInventory();

        // Step 3: Create Cancellation Service
        CancellationService cancellationService =
                new CancellationService(confirmedBookings, inventory);

        // Step 4: Valid cancellations
        cancellationService.cancelBooking("RES002");
        cancellationService.cancelBooking("RES004");
        cancellationService.cancelBooking("RES001");

        // Step 5: Invalid cancellations
        cancellationService.cancelBooking("RES999"); // Does not exist
        cancellationService.cancelBooking("RES002"); // Already cancelled

        // Step 6: Display final state
        cancellationService.displayRollbackStack();
        cancellationService.displayCancelledBookings();
        cancellationService.displayConfirmedBookings();
        inventory.displayInventory();

        System.out.println("\nSystem state restored consistently after cancellations.");
        System.out.println("=== End of Use Case 10 ===");
    }
}