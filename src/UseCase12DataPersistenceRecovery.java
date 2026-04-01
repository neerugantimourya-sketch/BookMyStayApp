import java.util.*;
import java.io.*;

// Represents a confirmed reservation (Serializable for persistence)
class Reservation12 implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private double totalCost;

    public Reservation12(String reservationId, String guestName,
                         String roomType, String roomId, double totalCost) {
        this.reservationId = reservationId;
        this.guestName     = guestName;
        this.roomType      = roomType;
        this.roomId        = roomId;
        this.totalCost     = totalCost;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName()     { return guestName; }
    public String getRoomType()      { return roomType; }
    public String getRoomId()        { return roomId; }
    public double getTotalCost()     { return totalCost; }

    @Override
    public String toString() {
        return "ID: " + reservationId +
                " | Guest: "    + guestName +
                " | RoomType: " + roomType  +
                " | RoomID: "   + roomId    +
                " | Cost: Rs."  + totalCost;
    }
}

// System State - holds both inventory and booking history (Serializable)
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer>     inventory;
    private List<Reservation12>      bookingHistory;

    public SystemState(Map<String, Integer> inventory,
                       List<Reservation12> bookingHistory) {
        this.inventory      = inventory;
        this.bookingHistory = bookingHistory;
    }

    public Map<String, Integer>  getInventory()      { return inventory; }
    public List<Reservation12>   getBookingHistory() { return bookingHistory; }
}

// Persistence Service - handles save and restore operations
class PersistenceService {
    private static final String FILE_PATH = "system_state.dat";

    // Serialize and save system state to file
    public void saveState(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(state);
            System.out.println("[PERSISTENCE] System state saved to '" + FILE_PATH + "'");
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to save system state: " + e.getMessage());
        }
    }

    // Deserialize and restore system state from file
    public SystemState loadState() {
        File file = new File(FILE_PATH);

        // Handle missing file gracefully
        if (!file.exists()) {
            System.out.println("[RECOVERY] No persistence file found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            SystemState state = (SystemState) ois.readObject();
            System.out.println("[RECOVERY] System state restored from '" + FILE_PATH + "'");
            return state;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[ERROR] Failed to load system state: " + e.getMessage());
            System.out.println("[RECOVERY] Starting with default state.");
            return null;
        }
    }

    // Delete persistence file (simulate fresh start)
    public void clearState() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            file.delete();
            System.out.println("[PERSISTENCE] Persistence file cleared.");
        }
    }
}

// Booking System - manages inventory and booking history
class BookingSystem12 {
    private Map<String, Integer> inventory;
    private List<Reservation12>  bookingHistory;

    public BookingSystem12() {
        // Default initial state
        inventory = new LinkedHashMap<>();
        inventory.put("Standard", 3);
        inventory.put("Deluxe",   2);
        inventory.put("Suite",    1);
        bookingHistory = new ArrayList<>();
    }

    // Restore state from persistence
    public void restoreState(SystemState state) {
        if (state != null) {
            this.inventory      = state.getInventory();
            this.bookingHistory = state.getBookingHistory();
        }
    }

    // Get current system state (for saving)
    public SystemState captureState() {
        return new SystemState(inventory, bookingHistory);
    }

    // Add a booking
    public void addBooking(Reservation12 reservation) {
        String roomType = reservation.getRoomType();
        int available   = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            inventory.put(roomType, available - 1);
            bookingHistory.add(reservation);
            System.out.println("  [BOOKED] " + reservation);
        } else {
            System.out.println("  [FAILED] No rooms available for: " + roomType);
        }
    }

    // Display current inventory
    public void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("  " + entry.getKey() + " : " + entry.getValue() + " available");
        }
    }

    // Display booking history
    public void displayBookingHistory() {
        System.out.println("\n--- Booking History ---");
        if (bookingHistory.isEmpty()) {
            System.out.println("  No bookings found.");
        } else {
            for (Reservation12 r : bookingHistory) {
                System.out.println("  " + r);
            }
        }
        System.out.println("  Total Bookings: " + bookingHistory.size());
    }

    public Map<String, Integer> getInventory()      { return inventory; }
    public List<Reservation12>  getBookingHistory() { return bookingHistory; }
}

public class UseCase12DataPersistenceRecovery {
    public static void main(String[] args) {

        System.out.println("=================================================");
        System.out.println(" BookMyStay: Use Case 12 - Data Persistence & Recovery");
        System.out.println("=================================================\n");

        PersistenceService persistenceService = new PersistenceService();

        // -------------------------------------------------------
        // PHASE 1: First Run - Add bookings and save state
        // -------------------------------------------------------
        System.out.println("--- PHASE 1: Initial System Run ---\n");

        BookingSystem12 system = new BookingSystem12();

        System.out.println("Initial Inventory:");
        system.displayInventory();

        System.out.println("\nAdding Bookings...");
        system.addBooking(new Reservation12("RES001", "Alice",   "Deluxe",   "D101", 3500.0));
        system.addBooking(new Reservation12("RES002", "Bob",     "Suite",    "S201", 7000.0));
        system.addBooking(new Reservation12("RES003", "Charlie", "Standard", "ST301",2000.0));
        system.addBooking(new Reservation12("RES004", "Diana",   "Deluxe",   "D102", 3500.0));
        system.addBooking(new Reservation12("RES005", "Eve",     "Standard", "ST302",2000.0));

        system.displayInventory();
        system.displayBookingHistory();

        // Save state before shutdown
        System.out.println("\n[SYSTEM] Preparing for shutdown...");
        persistenceService.saveState(system.captureState());
        System.out.println("[SYSTEM] System shutdown complete.\n");

        // -------------------------------------------------------
        // PHASE 2: System Restart - Restore state from file
        // -------------------------------------------------------
        System.out.println("--- PHASE 2: System Restart & Recovery ---\n");

        BookingSystem12 recoveredSystem = new BookingSystem12();

        System.out.println("[SYSTEM] Starting system recovery...");
        SystemState savedState = persistenceService.loadState();
        recoveredSystem.restoreState(savedState);

        System.out.println("[SYSTEM] Recovery complete. Resuming operations.\n");

        recoveredSystem.displayInventory();
        recoveredSystem.displayBookingHistory();

        // Add more bookings after recovery
        System.out.println("\nAdding New Bookings After Recovery...");
        recoveredSystem.addBooking(new Reservation12("RES006", "Frank", "Standard", "ST303", 2000.0));
        recoveredSystem.addBooking(new Reservation12("RES007", "Grace", "Suite",    "S202",  7000.0));

        recoveredSystem.displayInventory();
        recoveredSystem.displayBookingHistory();

        // Save updated state again
        System.out.println("\n[SYSTEM] Saving updated state...");
        persistenceService.saveState(recoveredSystem.captureState());

        // -------------------------------------------------------
        // PHASE 3: Handle missing file gracefully
        // -------------------------------------------------------
        System.out.println("\n--- PHASE 3: Missing File Recovery Test ---\n");

        persistenceService.clearState();

        BookingSystem12 freshSystem = new BookingSystem12();
        SystemState missingState = persistenceService.loadState();

        if (missingState == null) {
            System.out.println("[SYSTEM] No saved state found. Starting with default inventory.");
        } else {
            freshSystem.restoreState(missingState);
        }

        freshSystem.displayInventory();
        freshSystem.displayBookingHistory();

        System.out.println("\nSystem operates safely with or without persisted data.");
        System.out.println("=================================================");
        System.out.println(" End of Use Case 12");
        System.out.println("=================================================");
    }
}