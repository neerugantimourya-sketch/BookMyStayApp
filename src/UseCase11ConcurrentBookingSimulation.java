import java.util.*;

// Represents a booking request
class BookingRequest {
    private String requestId;
    private String guestName;
    private String roomType;

    public BookingRequest(String requestId, String guestName, String roomType) {
        this.requestId = requestId;
        this.guestName = guestName;
        this.roomType  = roomType;
    }

    public String getRequestId() { return requestId; }
    public String getGuestName() { return guestName; }
    public String getRoomType()  { return roomType; }

    @Override
    public String toString() {
        return "RequestID: " + requestId +
                " | Guest: " + guestName +
                " | RoomType: " + roomType;
    }
}

// Thread-safe Room Inventory
class SharedRoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public SharedRoomInventory() {
        inventory.put("Standard", 3);
        inventory.put("Deluxe",   2);
        inventory.put("Suite",    1);
    }

    // Synchronized to prevent race conditions
    public synchronized boolean allocateRoom(String roomType) {
        int count = inventory.getOrDefault(roomType, 0);
        if (count > 0) {
            inventory.put(roomType, count - 1);
            return true;
        }
        return false;
    }

    public synchronized int getAvailableRooms(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public synchronized void displayInventory() {
        System.out.println("\n--- Final Room Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("  " + entry.getKey() + " : " + entry.getValue() + " available");
        }
    }
}

// Shared booking queue and confirmed bookings list
class SharedBookingQueue {
    private Queue<BookingRequest> queue = new LinkedList<>();
    private List<String> confirmedBookings = new ArrayList<>();

    // Synchronized enqueue
    public synchronized void enqueue(BookingRequest request) {
        queue.add(request);
    }

    // Synchronized dequeue
    public synchronized BookingRequest dequeue() {
        return queue.poll();
    }

    // Synchronized confirm
    public synchronized void confirmBooking(String info) {
        confirmedBookings.add(info);
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public List<String> getConfirmedBookings() {
        return confirmedBookings;
    }
}

// Booking Worker Thread
class BookingWorker implements Runnable {
    private String workerName;
    private SharedBookingQueue bookingQueue;
    private SharedRoomInventory inventory;

    public BookingWorker(String workerName,
                         SharedBookingQueue bookingQueue,
                         SharedRoomInventory inventory) {
        this.workerName   = workerName;
        this.bookingQueue = bookingQueue;
        this.inventory    = inventory;
    }

    @Override
    public void run() {
        while (true) {
            BookingRequest request;

            // Synchronized dequeue from shared queue
            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) break;
                request = bookingQueue.dequeue();
                if (request == null) break;
            }

            // Process the booking request
            String roomType = request.getRoomType();
            boolean allocated = inventory.allocateRoom(roomType);

            if (allocated) {
                String confirmation = "[" + workerName + "] SUCCESS - " +
                        request.getRequestId() +
                        " | Guest: " + request.getGuestName() +
                        " | Room: " + roomType +
                        " | Remaining: " + inventory.getAvailableRooms(roomType);
                System.out.println(confirmation);
                bookingQueue.confirmBooking(request.getRequestId());
            } else {
                System.out.println("[" + workerName + "] FAILED  - " +
                        request.getRequestId() +
                        " | Guest: " + request.getGuestName() +
                        " | Room: " + roomType +
                        " | No rooms available.");
            }

            // Simulate processing delay
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

public class UseCase11ConcurrentBookingSimulation {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== BookMyStay: Use Case 11 - Concurrent Booking Simulation ===\n");

        // Step 1: Create shared resources
        SharedRoomInventory inventory   = new SharedRoomInventory();
        SharedBookingQueue  bookingQueue = new SharedBookingQueue();

        // Step 2: Display initial inventory
        System.out.println("--- Initial Room Inventory ---");
        System.out.println("  Standard : 3 available");
        System.out.println("  Deluxe   : 2 available");
        System.out.println("  Suite    : 1 available");

        // Step 3: Add multiple booking requests to shared queue
        System.out.println("\n--- Adding Booking Requests to Shared Queue ---");
        List<BookingRequest> requests = Arrays.asList(
                new BookingRequest("REQ001", "Alice",   "Deluxe"),
                new BookingRequest("REQ002", "Bob",     "Suite"),
                new BookingRequest("REQ003", "Charlie", "Standard"),
                new BookingRequest("REQ004", "Diana",   "Deluxe"),
                new BookingRequest("REQ005", "Eve",     "Standard"),
                new BookingRequest("REQ006", "Frank",   "Suite"),
                new BookingRequest("REQ007", "Grace",   "Standard"),
                new BookingRequest("REQ008", "Henry",   "Deluxe"),
                new BookingRequest("REQ009", "Isla",    "Standard"),
                new BookingRequest("REQ010", "Jack",    "Standard")
        );

        for (BookingRequest req : requests) {
            bookingQueue.enqueue(req);
            System.out.println("  Enqueued -> " + req);
        }

        // Step 4: Create and start multiple worker threads
        System.out.println("\n--- Starting Concurrent Booking Workers ---\n");

        Thread worker1 = new Thread(new BookingWorker("Worker-1", bookingQueue, inventory));
        Thread worker2 = new Thread(new BookingWorker("Worker-2", bookingQueue, inventory));
        Thread worker3 = new Thread(new BookingWorker("Worker-3", bookingQueue, inventory));

        worker1.start();
        worker2.start();
        worker3.start();

        // Step 5: Wait for all threads to finish
        worker1.join();
        worker2.join();
        worker3.join();

        // Step 6: Display confirmed bookings
        System.out.println("\n--- Confirmed Bookings ---");
        List<String> confirmed = bookingQueue.getConfirmedBookings();
        if (confirmed.isEmpty()) {
            System.out.println("  No confirmed bookings.");
        } else {
            for (String id : confirmed) {
                System.out.println("  Confirmed Request ID: " + id);
            }
        }
        System.out.println("  Total Confirmed: " + confirmed.size());

        // Step 7: Display final inventory
        inventory.displayInventory();

        System.out.println("\nAll threads completed. System state is consistent.");
        System.out.println("=== End of Use Case 11 ===");
    }
}