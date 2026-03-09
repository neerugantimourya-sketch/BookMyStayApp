/**
 * UseCase4RoomSearch - Demonstrates read-only room search and
 * availability check in the BookMyStay system.
 *
 * @author BookMyStay Team
 * @version 4.0
 */

import java.util.HashMap;
import java.util.Map;

// ── Abstract Room Class ─────────────────────────────────────────────────────

/**
 * Room - Abstract base class representing a generalized hotel room.
 *
 * <p>Defines common attributes and behavior shared by all room types.
 * Cannot be instantiated directly.</p>
 *
 * @author BookMyStay Team
 * @version 4.0
 */
abstract class Room {

    private String roomNumber;
    private String roomType;
    private int    numberOfBeds;
    private double pricePerNight;
    private double roomSizeSqFt;

    /**
     * Constructs a Room with the specified attributes.
     *
     * @param roomNumber    the unique room identifier
     * @param roomType      the type/category of the room
     * @param numberOfBeds  the number of beds in the room
     * @param pricePerNight the cost per night in USD
     * @param roomSizeSqFt  the size of the room in square feet
     */
    public Room(String roomNumber, String roomType, int numberOfBeds,
                double pricePerNight, double roomSizeSqFt) {
        this.roomNumber    = roomNumber;
        this.roomType      = roomType;
        this.numberOfBeds  = numberOfBeds;
        this.pricePerNight = pricePerNight;
        this.roomSizeSqFt  = roomSizeSqFt;
    }

    // Getters
    public String getRoomNumber()    { return roomNumber; }
    public String getRoomType()      { return roomType; }
    public int    getNumberOfBeds()  { return numberOfBeds; }
    public double getPricePerNight() { return pricePerNight; }
    public double getRoomSizeSqFt()  { return roomSizeSqFt; }

    /**
     * Abstract method to display room-specific details.
     */
    public abstract void displayRoomDetails();

} // end class Room


// ── SingleRoom Class ────────────────────────────────────────────────────────

/**
 * SingleRoom - Concrete class representing a Single room type.
 *
 * @author BookMyStay Team
 * @version 4.0
 */
class SingleRoom extends Room {

    /**
     * Constructs a SingleRoom with predefined attributes.
     *
     * @param roomNumber the unique room identifier
     */
    public SingleRoom(String roomNumber) {
        super(roomNumber, "Single Room", 1, 99.99, 200.0);
    }

    /**
     * Displays details specific to a Single Room.
     */
    @Override
    public void displayRoomDetails() {
        System.out.println("  Room Number   : " + getRoomNumber());
        System.out.println("  Room Type     : " + getRoomType());
        System.out.println("  Number of Beds: " + getNumberOfBeds());
        System.out.println("  Room Size     : " + getRoomSizeSqFt() + " sq ft");
        System.out.println("  Price/Night   : $" + getPricePerNight());
    }

} // end class SingleRoom


// ── DoubleRoom Class ────────────────────────────────────────────────────────

/**
 * DoubleRoom - Concrete class representing a Double room type.
 *
 * @author BookMyStay Team
 * @version 4.0
 */
class DoubleRoom extends Room {

    /**
     * Constructs a DoubleRoom with predefined attributes.
     *
     * @param roomNumber the unique room identifier
     */
    public DoubleRoom(String roomNumber) {
        super(roomNumber, "Double Room", 2, 149.99, 350.0);
    }

    /**
     * Displays details specific to a Double Room.
     */
    @Override
    public void displayRoomDetails() {
        System.out.println("  Room Number   : " + getRoomNumber());
        System.out.println("  Room Type     : " + getRoomType());
        System.out.println("  Number of Beds: " + getNumberOfBeds());
        System.out.println("  Room Size     : " + getRoomSizeSqFt() + " sq ft");
        System.out.println("  Price/Night   : $" + getPricePerNight());
    }

} // end class DoubleRoom


// ── SuiteRoom Class ─────────────────────────────────────────────────────────

/**
 * SuiteRoom - Concrete class representing a Suite room type.
 *
 * @author BookMyStay Team
 * @version 4.0
 */
class SuiteRoom extends Room {

    /**
     * Constructs a SuiteRoom with predefined attributes.
     *
     * @param roomNumber the unique room identifier
     */
    public SuiteRoom(String roomNumber) {
        super(roomNumber, "Suite Room", 3, 299.99, 600.0);
    }

    /**
     * Displays details specific to a Suite Room.
     */
    @Override
    public void displayRoomDetails() {
        System.out.println("  Room Number   : " + getRoomNumber());
        System.out.println("  Room Type     : " + getRoomType());
        System.out.println("  Number of Beds: " + getNumberOfBeds());
        System.out.println("  Room Size     : " + getRoomSizeSqFt() + " sq ft");
        System.out.println("  Price/Night   : $" + getPricePerNight());
    }

} // end class SuiteRoom


// ── RoomInventory Class ─────────────────────────────────────────────────────

/**
 * RoomInventory - Centralized inventory manager for hotel room availability.
 *
 * <p>Maintains a HashMap as a single source of truth for room availability.
 * Provides read-only access during search operations.</p>
 *
 * @author BookMyStay Team
 * @version 4.0
 */
class RoomInventory {

    // Single source of truth for room availability
    private HashMap<String, Integer> inventoryMap;

    /**
     * Constructs a RoomInventory and initializes room availability.
     */
    public RoomInventory() {
        inventoryMap = new HashMap<>();
        inventoryMap.put("Single Room", 5);
        inventoryMap.put("Double Room", 3);
        inventoryMap.put("Suite Room",  0); // intentionally unavailable
    }

    /**
     * Retrieves the current availability count for a given room type.
     *
     * @param roomType the type of room to check
     * @return the number of available rooms, or 0 if not found
     */
    public int getAvailability(String roomType) {
        return inventoryMap.getOrDefault(roomType, 0);
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

    /**
     * Returns the full inventory map for read-only iteration.
     *
     * @return unmodifiable view of the inventory map
     */
    public HashMap<String, Integer> getInventoryMap() {
        return inventoryMap;
    }

} // end class RoomInventory


// ── RoomSearchService Class ─────────────────────────────────────────────────

/**
 * RoomSearchService - Handles read-only search operations on room inventory.
 *
 * <p>Retrieves availability data from inventory and room details from
 * domain objects. Ensures no inventory state is modified during search.</p>
 *
 * @author BookMyStay Team
 * @version 4.0
 */
class RoomSearchService {

    // Reference to centralized inventory (read-only usage)
    private RoomInventory inventory;

    // Room domain objects used for detail lookup
    private HashMap<String, Room> roomCatalog;

    /**
     * Constructs a RoomSearchService with the given inventory.
     *
     * @param inventory the centralized room inventory
     */
    public RoomSearchService(RoomInventory inventory) {
        this.inventory   = inventory;
        this.roomCatalog = new HashMap<>();

        // Register room domain objects in catalog
        roomCatalog.put("Single Room", new SingleRoom("R101"));
        roomCatalog.put("Double Room", new DoubleRoom("R201"));
        roomCatalog.put("Suite Room",  new SuiteRoom("R301"));
    }

    /**
     * Searches and displays all available room types.
     *
     * <p>Filters out room types with zero availability.
     * Retrieves room details from domain objects.
     * Does NOT modify inventory state.</p>
     */
    public void searchAvailableRooms() {
        System.out.println("------------------------------------------------");
        System.out.println("  AVAILABLE ROOMS");
        System.out.println("------------------------------------------------");

        boolean anyAvailable = false;

        for (Map.Entry<String, Integer> entry
                : inventory.getInventoryMap().entrySet()) {

            String roomType = entry.getKey();
            int    count    = entry.getValue();

            // Defensive check - only show rooms with availability > 0
            if (count > 0) {
                anyAvailable = true;
                System.out.println();
                roomCatalog.get(roomType).displayRoomDetails();
                System.out.println("  Rooms Available : " + count);
                System.out.println("------------------------------------------------");
            }
        }

        if (!anyAvailable) {
            System.out.println("  No rooms are currently available.");
            System.out.println("------------------------------------------------");
        }
    }

    /**
     * Searches for a specific room type and displays its details.
     *
     * <p>Read-only operation. Validates availability before displaying.</p>
     *
     * @param roomType the type of room to search for
     */
    public void searchByRoomType(String roomType) {
        System.out.println("------------------------------------------------");
        System.out.println("  SEARCH RESULT FOR : " + roomType.toUpperCase());
        System.out.println("------------------------------------------------");

        // Defensive check - validate room type exists
        if (!roomCatalog.containsKey(roomType)) {
            System.out.println("  [Error] Room type not found: " + roomType);
            System.out.println("------------------------------------------------");
            return;
        }

        // Check availability
        if (inventory.isAvailable(roomType)) {
            roomCatalog.get(roomType).displayRoomDetails();
            System.out.println("  Rooms Available : "
                    + inventory.getAvailability(roomType));
        } else {
            System.out.println("  Sorry, " + roomType
                    + " is currently not available.");
        }
        System.out.println("------------------------------------------------");
    }

} // end class RoomSearchService


// ── Main Application Class ──────────────────────────────────────────────────

/**
 * UseCase4RoomSearch - Entry point for Use Case 4.
 *
 * <p>Demonstrates read-only room search and availability check
 * without modifying system state.</p>
 *
 * @author BookMyStay Team
 * @version 4.0
 */
public class UseCase4RoomSearch {

    /**
     * Entry point for Use Case 4.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {

        // ── Application Header ──────────────────────────────────────────
        System.out.println("================================================");
        System.out.println("    BookMyStay - Hotel Booking System  v4.0     ");
        System.out.println("    Use Case 4 : Room Search &                  ");
        System.out.println("                Availability Check              ");
        System.out.println("================================================");
        System.out.println();

        // ── Initialize Inventory ────────────────────────────────────────
        RoomInventory inventory = new RoomInventory();
        System.out.println("  Inventory initialized successfully.");
        System.out.println();

        // ── Initialize Search Service ───────────────────────────────────
        RoomSearchService searchService = new RoomSearchService(inventory);
        System.out.println("  Search service initialized successfully.");
        System.out.println();

        // ── Search All Available Rooms ──────────────────────────────────
        searchService.searchAvailableRooms();
        System.out.println();

        // ── Search by Specific Room Type ────────────────────────────────
        searchService.searchByRoomType("Double Room");
        System.out.println();

        // ── Search Unavailable Room ─────────────────────────────────────
        searchService.searchByRoomType("Suite Room");
        System.out.println();

        // ── Search Invalid Room Type ────────────────────────────────────
        searchService.searchByRoomType("Penthouse");
        System.out.println();

        // ── Footer ──────────────────────────────────────────────────────
        System.out.println("================================================");
        System.out.println("  Room search completed. No state was modified.");
        System.out.println("================================================");

    } // end main

} // end class UseCase4RoomSearch