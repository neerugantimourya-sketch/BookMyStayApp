/**
 * UseCase5BookingRequestQueue - Demonstrates fair booking request handling
 * using a Queue (FIFO) in the BookMyStay system.
 *
 * @author BookMyStay Team
 * @version 5.0
 */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

// ── Abstract Room Class ─────────────────────────────────────────────────────

/**
 * Room - Abstract base class representing a generalized hotel room.
 *
 * @author BookMyStay Team
 * @version 5.0
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
 * @version 5.0
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
 * @version 5.0
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
 * @version 5.0
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
 * @version 5.0
 */
class RoomInventory {

    private HashMap<String, Integer> inventoryMap;

    /**
     * Constructs a RoomInventory and initializes room availability.
     */
    public RoomInventory() {
        inventoryMap = new HashMap<>();
        inventoryMap.put("Single Room", 5);
        inventoryMap.put("Double Room", 3);
        inventoryMap.put("Suite Room",  2);
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
 * <p>Each reservation captures the guest name, requested room type,
 * and number of nights. This object is queued for processing and
 * does NOT modify inventory at creation time.</p>
 *
 * @author BookMyStay Team
 * @version 5.0
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
    public String getGuestName()      { return guestName; }
    public String getRoomType()       { return roomType; }
    public int    getNumberOfNights() { return numberOfNights; }

    /**
     * Displays the reservation request details.
     */
    public void displayReservationDetails() {
        System.out.println("  Guest Name     : " + guestName);
        System.out.println("  Room Type      : " + roomType);
        System.out.println("  Number of Nights: " + numberOfNights);
    }

} // end class Reservation


// ── BookingRequestQueue Class ───────────────────────────────────────────────

/**
 * BookingRequestQueue - Manages incoming booking requests using FIFO ordering.
 *
 * <p>Uses a Queue<Reservation> to store requests in arrival order.
 * Ensures fair, first-come-first-served handling of all booking intents.
 * No inventory mutation occurs at this stage.</p>
 *
 * @author BookMyStay Team
 * @version 5.0
 */
class BookingRequestQueue {

    // Queue to store booking requests in FIFO order
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
     * <p>Preserves arrival order automatically. No inventory
     * is modified at this stage.</p>
     *
     * @param reservation the reservation request to enqueue
     */
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("  [Queued] Guest: " + reservation.getGuestName()
                + " | Room: " + reservation.getRoomType()
                + " | Nights: "  + reservation.getNumberOfNights());
    }

    /**
     * Retrieves and removes the next request from the front of the queue.
     *
     * <p>Implements FIFO - the earliest request is returned first.</p>
     *
     * @return the next Reservation in the queue, or null if empty
     */
    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    /**
     * Peeks at the next request without removing it from the queue.
     *
     * @return the next Reservation, or null if empty
     */
    public Reservation peekNextRequest() {
        return requestQueue.peek();
    }

    /**
     * Returns the current number of requests in the queue.
     *
     * @return size of the request queue
     */
    public int getQueueSize() {
        return requestQueue.size();
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
     * Displays all pending requests in queue order.
     */
    public void displayQueue() {
        System.out.println("------------------------------------------------");
        System.out.println("  PENDING BOOKING REQUESTS (FIFO ORDER)");
        System.out.println("------------------------------------------------");

        if (requestQueue.isEmpty()) {
            System.out.println("  No pending requests in the queue.");
            System.out.println("------------------------------------------------");
            return;
        }

        int position = 1;
        for (Reservation r : requestQueue) {
            System.out.println("  Position " + position++ + ":");
            r.displayReservationDetails();
            System.out.println("  ............................................");
        }
        System.out.println("  Total Requests : " + requestQueue.size());
        System.out.println("------------------------------------------------");
    }

} // end class BookingRequestQueue


// ── Main Application Class ──────────────────────────────────────────────────

/**
 * UseCase5BookingRequestQueue - Entry point for Use Case 5.
 *
 * <p>Demonstrates fair booking request intake using a Queue.
 * Requests are stored in FIFO order without modifying inventory.</p>
 *
 * @author BookMyStay Team
 * @version 5.0
 */
public class UseCase5BookingRequestQueue {

    /**
     * Entry point for Use Case 5.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {

        // ── Application Header ──────────────────────────────────────────
        System.out.println("================================================");
        System.out.println("    BookMyStay - Hotel Booking System  v5.0     ");
        System.out.println("    Use Case 5 : Booking Request                ");
        System.out.println("               (First-Come-First-Served)        ");
        System.out.println("================================================");
        System.out.println();

        // ── Initialize Inventory ────────────────────────────────────────
        RoomInventory inventory = new RoomInventory();
        System.out.println("  Inventory initialized successfully.");
        System.out.println();
        inventory.displayInventory();
        System.out.println();

        // ── Initialize Booking Request Queue ────────────────────────────
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        System.out.println("  Booking request queue initialized.");
        System.out.println();

        // ── Guests Submit Booking Requests ──────────────────────────────
        System.out.println("------------------------------------------------");
        System.out.println("  INCOMING BOOKING REQUESTS");
        System.out.println("------------------------------------------------");

        bookingQueue.addRequest(new Reservation("Alice",   "Single Room", 2));
        bookingQueue.addRequest(new Reservation("Bob",     "Double Room", 3));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room",  1));
        bookingQueue.addRequest(new Reservation("Diana",   "Single Room", 4));
        bookingQueue.addRequest(new Reservation("Edward",  "Double Room", 2));

        System.out.println();

        // ── Display All Queued Requests ─────────────────────────────────
        bookingQueue.displayQueue();
        System.out.println();

        // ── Peek at Next Request ────────────────────────────────────────
        System.out.println("------------------------------------------------");
        System.out.println("  NEXT REQUEST TO BE PROCESSED (PEEK)");
        System.out.println("------------------------------------------------");
        Reservation next = bookingQueue.peekNextRequest();
        if (next != null) {
            next.displayReservationDetails();
        }
        System.out.println("------------------------------------------------");
        System.out.println();

        // ── Confirm Inventory Unchanged ─────────────────────────────────
        System.out.println("  Inventory after queuing requests");
        System.out.println("  (No changes - intake stage only):");
        System.out.println();
        inventory.displayInventory();
        System.out.println();

        // ── Footer ──────────────────────────────────────────────────────
        System.out.println("================================================");
        System.out.println("  Booking requests queued successfully.");
        System.out.println("  Ready for allocation processing.");
        System.out.println("================================================");

    } // end main

} // end class UseCase5BookingRequestQueue