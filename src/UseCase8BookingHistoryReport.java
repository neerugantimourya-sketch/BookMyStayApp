import java.util.*;

class Reservation8 {
    private String reservationId;
    private String guestName;
    private String roomNumber;
    private String roomType;
    private double totalCost;

    public Reservation8(String reservationId, String guestName, String roomNumber, String roomType, double totalCost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.totalCost = totalCost;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName()     { return guestName; }
    public String getRoomNumber()    { return roomNumber; }
    public String getRoomType()      { return roomType; }
    public double getTotalCost()     { return totalCost; }

    @Override
    public String toString() {
        return "ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomNumber +
                " | Type: " + roomType +
                " | Cost: Rs." + totalCost;
    }
}

class BookingHistory {
    private List<Reservation8> history = new ArrayList<>();

    public void addBooking(Reservation8 reservation) {
        history.add(reservation);
        System.out.println("Booking confirmed and added to history: " + reservation.getReservationId());
    }

    public List<Reservation8> getHistory() {
        return history;
    }

    public void displayHistory() {
        System.out.println("\n--- Booking History (Insertion Order) ---");
        if (history.isEmpty()) {
            System.out.println("No bookings found.");
        } else {
            for (Reservation8 r : history) {
                System.out.println("  " + r);
            }
        }
    }
}

class BookingReportService {
    private BookingHistory bookingHistory;

    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    public void generateSummaryReport() {
        List<Reservation8> history = bookingHistory.getHistory();
        double totalRevenue = 0;
        for (Reservation8 r : history) {
            totalRevenue += r.getTotalCost();
        }
        System.out.println("\n--- Booking Summary Report ---");
        System.out.println("Total Bookings : " + history.size());
        System.out.println("Total Revenue  : Rs." + totalRevenue);
    }

    public void generateRoomTypeReport() {
        List<Reservation8> history = bookingHistory.getHistory();
        Map<String, Integer> roomTypeCount = new LinkedHashMap<>();
        for (Reservation8 r : history) {
            roomTypeCount.put(r.getRoomType(),
                    roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1);
        }
        System.out.println("\n--- Room Type Report ---");
        for (Map.Entry<String, Integer> entry : roomTypeCount.entrySet()) {
            System.out.println("  " + entry.getKey() + " : " + entry.getValue() + " booking(s)");
        }
    }

    public void generateGuestReport() {
        List<Reservation8> history = bookingHistory.getHistory();
        System.out.println("\n--- Guest Booking Report ---");
        for (Reservation8 r : history) {
            System.out.println("  Guest: " + r.getGuestName() +
                    " | Reservation: " + r.getReservationId() +
                    " | Room: " + r.getRoomNumber() +
                    " | Cost: Rs." + r.getTotalCost());
        }
    }
}

public class UseCase8BookingHistoryReport {
    public static void main(String[] args) {

        System.out.println("=== BookMyStay: Use Case 8 - Booking History & Reporting ===\n");

        BookingHistory bookingHistory = new BookingHistory();

        bookingHistory.addBooking(new Reservation8("RES001", "Alice",   "101", "Deluxe",   3500.0));
        bookingHistory.addBooking(new Reservation8("RES002", "Bob",     "202", "Suite",    7000.0));
        bookingHistory.addBooking(new Reservation8("RES003", "Charlie", "103", "Deluxe",   3500.0));
        bookingHistory.addBooking(new Reservation8("RES004", "Diana",   "301", "Standard", 2000.0));
        bookingHistory.addBooking(new Reservation8("RES005", "Eve",     "204", "Suite",    7000.0));

        bookingHistory.displayHistory();

        BookingReportService reportService = new BookingReportService(bookingHistory);
        reportService.generateSummaryReport();
        reportService.generateRoomTypeReport();
        reportService.generateGuestReport();

        System.out.println("\nReporting does not modify stored booking data.");
        System.out.println("=== End of Use Case 8 ===");
    }
}