import java.util.*;

class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() { return serviceName; }
    public double getCost() { return cost; }

    @Override
    public String toString() {
        return serviceName + " (Cost: Rs." + cost + ")";
    }
}

class AddOnServiceManager {
    private Map<String, List<AddOnService>> reservationServices = new HashMap<>();

    public void addService(String reservationId, AddOnService service) {
        reservationServices.putIfAbsent(reservationId, new ArrayList<>());
        reservationServices.get(reservationId).add(service);
    }

    public List<AddOnService> getServices(String reservationId) {
        return reservationServices.getOrDefault(reservationId, new ArrayList<>());
    }

    public double calculateTotalCost(String reservationId) {
        double total = 0;
        for (AddOnService s : getServices(reservationId)) {
            total += s.getCost();
        }
        return total;
    }

    public void displayServices(String reservationId) {
        System.out.println("Add-On Services for Reservation " + reservationId + ":");
        List<AddOnService> services = getServices(reservationId);
        if (services.isEmpty()) {
            System.out.println("  No services selected.");
        } else {
            for (AddOnService s : services) {
                System.out.println("  - " + s);
            }
            System.out.println("  Total Add-On Cost: Rs." + calculateTotalCost(reservationId));
        }
    }
}

public class UseCase7AddOnServiceSelection {
    public static void main(String[] args) {

        System.out.println("=== BookMyStay: Use Case 7 - Add-On Service Selection ===\n");

        AddOnServiceManager manager = new AddOnServiceManager();

        // Define available services
        AddOnService breakfast   = new AddOnService("Breakfast", 300.0);
        AddOnService spa         = new AddOnService("Spa Session", 1200.0);
        AddOnService airportPick = new AddOnService("Airport Pickup", 800.0);
        AddOnService laundry     = new AddOnService("Laundry", 200.0);
        AddOnService lateCheckout= new AddOnService("Late Checkout", 500.0);

        // Guest 1 - Alice selects services for RES001
        System.out.println("Guest Alice selecting services for RES001...");
        manager.addService("RES001", breakfast);
        manager.addService("RES001", spa);
        manager.addService("RES001", airportPick);

        // Guest 2 - Bob selects services for RES002
        System.out.println("Guest Bob selecting services for RES002...");
        manager.addService("RES002", laundry);
        manager.addService("RES002", lateCheckout);

        System.out.println();

        // Display services
        manager.displayServices("RES001");
        System.out.println();
        manager.displayServices("RES002");

        System.out.println();
        System.out.println("Core booking and inventory state remains unchanged.");
        System.out.println("=== End of Use Case 7 ===");
    }
}
