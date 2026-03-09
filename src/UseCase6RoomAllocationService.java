/**
 * UseCase6RoomAllocationService - Demonstrates reservation confirmation
 * and room allocation with double-booking prevention in BookMyStay.
 *
 * @author BookMyStay Team
 * @version 6.0
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

// ── Abstract Room Class ─────────────────────────────────────────────────────

/**
 * Room - Abstract base class representing a generalized hotel room.
 *
 * @author BookMyStay Team
 * @version 6.0
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
 * @version 6.0
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
 * @version 6.0
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
 * @version 6.0
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
 * @author BookMyStay Team
 * @version 6.0
 */
class RoomInventory {

    private HashMap<String, Integer> inventoryMap;

    /**
     * Constructs a RoomInventory and initializes room availability.
     */
    public RoomInventory() {
        inventoryMap = new HashMap<>();
        inventoryMap.put("Single Room", 3);
        inventoryMap.put("Double Room", 2);
        inventoryMap.put("Suite Room",  1);
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
     * Decrements inventory count by 1 after successful allocation.
     *
     * <p>Inventory is updated immediately to ensure synchronization
     * with the current allocation state.</p>
     *
     * @param roomType the type of room to decrement
     */
    public void decrementAvailability(String roomType) {
        if (isAvailable(roomType)) {
            inventoryMap.put(roomType, inventoryMap.get(roomType) - 1);
        }
    }

    /**
     * Displays the current inventory state for all room types.
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

} // end class RoomInventory


// ── Reservation Class ───────────────────────────────────────────────────────

/**
 * Reservation - Represents a guest's intent to book a specific room type.
 *
 * @author BookMyStay Team
 * @version 6.0
 */
class Reservation {

    private String guestName;
    private String roomType;
    private int    numberOfNights;

    /**
     * Constructs a Reservation with guest and room details.
     *
     * @param guestName      the name of the guest making the request
     * @param roomType       the type of room being requested
     * @param numberOfNights the number of nights for the stay
     */
    public Reservation(String guestName, String roomType, int numberOfNights) {
        this.guestName      = guestName;
        this.roomType       = roomType;
        this.numberOfNights = numberOfNights;
    }

    // Getters
    public String getGuestName()       { return guestName; }
    public String getRoomType()        { return roomType; }
    public int    getNumberOfNights()  { return numberOfNights; }

    /**
     * Displays the reservation request details.
     */
    public void displayReservationDetails() {
        System.out.println("  Guest Name      : " + guestName);
        System.out.println("  Room Type       : " + roomType);
        System.out.println("  Number of Nights: " + numberOfNights);
    }

} // end class Reservation


// ── BookingRequestQueue Class ───────────────────────────────────────────────

/**
 * BookingRequestQueue - Manages incoming booking requests using FIFO ordering.
 *
 * @author BookMyStay Team
 * @version 6.0
 */
class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    /**
     * Constructs an empty BookingRequestQueue.
     */
    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    /**
     * Adds a booking request to the end of the queue.
     *
     * @param reservation the reservation request to enqueue
     */
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("  [Queued] Guest: " + reservation.getGuestName()
                + " | Room: "   + reservation.getRoomType()
                + " | Nights: " + reservation.getNumberOfNights());
    }

    /**
     * Retrieves and removes the next request from the front of the queue.
     *
     * @return the next Reservation in the queue, or null if empty
     */
    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    /**
     * Checks whether the queue is empty.
     *
     * @return true if no requests are pending, false otherwise
     */
    public boolean isEmpty() {
        return requestQueue.isEmpty();
    }

    /**
     * Returns the current number of requests in the queue.
     *
     * @return size of the request queue
     */
    public int getQueueSize() {
        return requestQueue.size();
    }

} // end class BookingRequestQueue


// ── RoomAllocationService Class ─────────────────────────────────────────────

/**
 * RoomAllocationService - Processes booking requests and allocates rooms
 * with guaranteed uniqueness and inventory synchronization.
 *
 * <p>Uses a HashMap<String, Set<String>> to map room types to their
 * allocated room IDs. The Set enforces uniqueness, preventing any
 * room from being assigned more than once (double-booking prevention).</p>
 *
 * @author BookMyStay Team
 * @version 6.0
 */
class RoomAllocationService {

    // Maps room type -> Set of allocated room IDs (Set enforces uniqueness)
    private HashMap<String, Set<String>> allocatedRoomsMap;

    // Counters per room type for generating sequential room IDs
    private HashMap<String, Integer> roomCounterMap;

    // Reference to centralized inventory
    private RoomInventory inventory;

    /**
     * Constructs a RoomAllocationService with the given inventory.
     *
     * @param inventory the centralized room inventory
     */
    public RoomAllocationService(RoomInventory inventory) {
        this.inventory       = inventory;
        allocatedRoomsMap    = new HashMap<>();
        roomCounterMap       = new HashMap<>();

        // Initialize allocation tracking for each room type
        allocatedRoomsMap.put("Single Room", new HashSet<>());
        allocatedRoomsMap.put("Double Room", new HashSet<>());
        allocatedRoomsMap.put("Suite Room",  new HashSet<>());

        // Initialize room ID counters
        roomCounterMap.put("Single Room", 100);
        roomCounterMap.put("Double Room", 200);
        roomCounterMap.put("Suite Room",  300);
    }

    /**
     * Processes all requests in the queue and allocates rooms.
     *
     * <p>Dequeues each request in FIFO order, checks availability,
     * generates a unique room ID, records it in the Set to prevent
     * reuse, and decrements inventory atomically.</p>
     *
     * @param bookingQueue the queue of pending booking requests
     */
    public void processRequests(BookingRequestQueue bookingQueue) {
        System.out.println("------------------------------------------------");
        System.out.println("  PROCESSING BOOKING REQUESTS");
        System.out.println("------------------------------------------------");

        while (!bookingQueue.isEmpty()) {
            Reservation request = bookingQueue.getNextRequest();
            System.out.println();
            System.out.println("  Processing request for: "
                    + request.getGuestName());
            allocateRoom(request);
            System.out.println("  ............................................");
        }
    }

    /**
     * Allocates a room for the given reservation request.
     *
     * <p>Generates a unique room ID and checks against the Set to
     * prevent double-booking. Updates inventory immediately after
     * successful allocation.</p>
     *
     * @param reservation the reservation to process
     */
    private void allocateRoom(Reservation reservation) {
        String roomType = reservation.getRoomType();

        // Check availability before allocation
        if (!inventory.isAvailable(roomType)) {
            System.out.println("  [DENIED]  No rooms available for: "
                    + roomType);
            System.out.println("  Guest " + reservation.getGuestName()
                    + " could not be accommodated.");
            return;
        }

        // Generate unique room ID
        int    counter = roomCounterMap.get(roomType);
        String roomId  = roomType.substring(0, 1).toUpperCase()
                + roomType.split(" ")[1].substring(0, 1).toUpperCase()
                + "-" + counter;

        // Uniqueness check using Set (prevents double-booking)
        if (allocatedRoomsMap.get(roomType).contains(roomId)) {
            System.out.println("  [ERROR] Room ID already allocated: " + roomId);
            return;
        }

        // Record allocation - Set enforces no duplicates
        allocatedRoomsMap.get(roomType).add(roomId);

        // Increment counter for next allocation
        roomCounterMap.put(roomType, counter + 1);

        // Atomically decrement inventory
        inventory.decrementAvailability(roomType);

        // Confirm reservation
        System.out.println("  [CONFIRMED] Guest   : "
                + reservation.getGuestName());
        System.out.println("              Room ID : " + roomId);
        System.out.println("              Type    : " + roomType);
        System.out.println("              Nights  : "
                + reservation.getNumberOfNights());
    }

    /**
     * Displays all allocated room IDs grouped by room type.
     */
    public void displayAllocations() {
        System.out.println("------------------------------------------------");
        System.out.println("  ALLOCATED ROOMS SUMMARY");
        System.out.println("------------------------------------------------");
        for (Map.Entry<String, Set<String>> entry
                : allocatedRoomsMap.entrySet()) {
            System.out.println("  " + entry.getKey() + " : "
                    + entry.getValue());
        }
        System.out.println("------------------------------------------------");
    }

} // end class RoomAllocationService


// ── Main Application Class ──────────────────────────────────────────────────

/**
 * UseCase6RoomAllocationService - Entry point for Use Case 6.
 *
 * <p>Demonstrates reservation confirmation and room allocation with
 * double-booking prevention using Set and HashMap.</p>
 *
 * @author BookMyStay Team
 * @version 6.0
 */
public class UseCase6RoomAllocationService {

    /**
     * Entry point for Use Case 6.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {

        // ── Application Header ──────────────────────────────────────────
        System.out.println("================================================");
        System.out.println("    BookMyStay - Hotel Booking System  v6.0     ");
        System.out.println("    Use Case 6 : Reservation Confirmation &     ");
        System.out.println("                Room Allocation                 ");
        System.out.println("================================================");
        System.out.println();

        // ── Initialize Inventory ────────────────────────────────────────
        RoomInventory inventory = new RoomInventory();
        System.out.println("  Inventory initialized successfully.");
        System.out.println();
        inventory.displayInventory();
        System.out.println();

        // ── Initialize Booking Queue ────────────────────────────────────
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // ── Guests Submit Booking Requests ──────────────────────────────
        System.out.println("------------------------------------------------");
        System.out.println("  INCOMING BOOKING REQUESTS");
        System.out.println("------------------------------------------------");
        bookingQueue.addRequest(new Reservation("Alice",   "Single Room", 2));
        bookingQueue.addRequest(new Reservation("Bob",     "Double Room", 3));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room",  1));
        bookingQueue.addRequest(new Reservation("Diana",   "Single Room", 4));
        bookingQueue.addRequest(new Reservation("Edward",  "Single Room", 2));
        bookingQueue.addRequest(new Reservation("Frank",   "Double Room", 2));
        bookingQueue.addRequest(new Reservation("Grace",   "Single Room", 3));// exceeds
        bookingQueue.addRequest(new Reservation("Henry",   "Suite Room",  1));// exceeds
        System.out.println();

        // ── Initialize Allocation Service ───────────────────────────────
        RoomAllocationService allocationService =
                new RoomAllocationService(inventory);

        // ── Process All Requests ────────────────────────────────────────
        allocationService.processRequests(bookingQueue);
        System.out.println();

        // ── Display Allocations Summary ─────────────────────────────────
        allocationService.displayAllocations();
        System.out.println();

        // ── Display Updated Inventory ───────────────────────────────────
        System.out.println("  Inventory after all allocations:");
        System.out.println();
        inventory.displayInventory();
        System.out.println();

        // ── Footer ──────────────────────────────────────────────────────
        System.out.println("================================================");
        System.out.println("  Room allocation completed successfully.");
        System.out.println("================================================");

    } // end main

} // end class UseCase6RoomAllocationService