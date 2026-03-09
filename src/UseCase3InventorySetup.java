/**
 * UseCase3InventorySetup - Demonstrates centralized room inventory
 * management using HashMap in the BookMyStay system.
 *
 * @author BookMyStay Team
 * @version 3.0
 */

import java.util.HashMap;
import java.util.Map;

// ── RoomInventory Class ─────────────────────────────────────────────────────

/**
 * RoomInventory - Centralized inventory manager for hotel room availability.
 *
 * <p>This class replaces scattered availability variables with a single
 * HashMap data structure, providing a consistent and scalable way to
 * manage room availability across the system.</p>
 *
 * @author BookMyStay Team
 * @version 3.0
 */
class RoomInventory {

    // Single source of truth for room availability
    // Key   : Room type (e.g., "Single Room")
    // Value : Number of available rooms
    private HashMap<String, Integer> inventoryMap;

    /**
     * Constructs a RoomInventory and initializes room availability.
     *
     * <p>Room types are registered with their initial available counts
     * during construction, establishing the centralized inventory state.</p>
     */
    public RoomInventory() {
        inventoryMap = new HashMap<>();

        // Register room types with initial availability counts
        inventoryMap.put("Single Room", 5);
        inventoryMap.put("Double Room", 3);
        inventoryMap.put("Suite Room",  2);
    }

    /**
     * Retrieves the current availability count for a given room type.
     *
     * <p>Uses O(1) average-time HashMap lookup to retrieve availability.</p>
     *
     * @param roomType the type of room to check
     * @return the number of available rooms, or 0 if room type not found
     */
    public int getAvailability(String roomType) {
        return inventoryMap.getOrDefault(roomType, 0);
    }

    /**
     * Updates the availability count for a given room type.
     *
     * <p>Controlled update method ensures inventory state remains
     * consistent. Negative counts are prevented.</p>
     *
     * @param roomType the type of room to update
     * @param count    the new availability count
     */
    public void updateAvailability(String roomType, int count) {
        if (inventoryMap.containsKey(roomType)) {
            if (count >= 0) {
                inventoryMap.put(roomType, count);
                System.out.println("  [Updated] " + roomType
                        + " availability set to: " + count);
            } else {
                System.out.println("  [Error] Availability count cannot"
                        + " be negative for: " + roomType);
            }
        } else {
            System.out.println("  [Error] Room type not found: " + roomType);
        }
    }

    /**
     * Displays the current inventory state for all room types.
     *
     * <p>Iterates over all entries in the HashMap and prints
     * each room type with its current availability count.</p>
     */
    public void displayInventory() {
        System.out.println("------------------------------------------------");
        System.out.println("  CURRENT ROOM INVENTORY");
        System.out.println("------------------------------------------------");
        for (Map.Entry<String, Integer> entry : inventoryMap.entrySet()) {
            System.out.println("  " + entry.getKey()
                    + "  ->  Available: " + entry.getValue());
        }
        System.out.println("------------------------------------------------");
    }

    /**
     * Checks whether a given room type is available.
     *
     * @param roomType the type of room to check
     * @return true if at least one room is available, false otherwise
     */
    public boolean isAvailable(String roomType) {
        return getAvailability(roomType) > 0;
    }

} // end class RoomInventory


// ── Main Application Class ──────────────────────────────────────────────────

/**
 * UseCase3InventorySetup - Entry point for Use Case 3.
 *
 * <p>Initializes the RoomInventory, demonstrates availability retrieval,
 * controlled updates, and displays the centralized inventory state.</p>
 *
 * @author BookMyStay Team
 * @version 3.0
 */
public class UseCase3InventorySetup {

    /**
     * Entry point for Use Case 3.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {

        // ── Application Header ──────────────────────────────────────────
        System.out.println("================================================");
        System.out.println("    BookMyStay - Hotel Booking System  v3.0     ");
        System.out.println("    Use Case 3 : Centralized Room Inventory     ");
        System.out.println("                Management                      ");
        System.out.println("================================================");
        System.out.println();

        // ── Initialize Inventory ────────────────────────────────────────
        RoomInventory inventory = new RoomInventory();
        System.out.println("  Inventory initialized successfully.");
        System.out.println();

        // ── Display Initial Inventory ───────────────────────────────────
        inventory.displayInventory();
        System.out.println();

        // ── Check Availability ──────────────────────────────────────────
        System.out.println("------------------------------------------------");
        System.out.println("  AVAILABILITY CHECK");
        System.out.println("------------------------------------------------");
        System.out.println("  Single Room Available : "
                + inventory.isAvailable("Single Room"));
        System.out.println("  Double Room Available : "
                + inventory.isAvailable("Double Room"));
        System.out.println("  Suite Room  Available : "
                + inventory.isAvailable("Suite Room"));
        System.out.println();

        // ── Update Availability ─────────────────────────────────────────
        System.out.println("------------------------------------------------");
        System.out.println("  UPDATING AVAILABILITY");
        System.out.println("------------------------------------------------");
        inventory.updateAvailability("Single Room", 3);
        inventory.updateAvailability("Suite Room",  0);
        inventory.updateAvailability("Double Room", -1); // invalid update
        System.out.println();

        // ── Display Updated Inventory ───────────────────────────────────
        System.out.println("  Inventory after updates:");
        System.out.println();
        inventory.displayInventory();
        System.out.println();

        // ── Footer ──────────────────────────────────────────────────────
        System.out.println("================================================");
        System.out.println("  Inventory setup complete.");
        System.out.println("================================================");

    } // end main

} // end class UseCase3InventorySetup