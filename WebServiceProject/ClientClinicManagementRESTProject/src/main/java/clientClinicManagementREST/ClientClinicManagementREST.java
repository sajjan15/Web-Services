package clientClinicManagementREST;

import java.util.Scanner;
import org.glassfish.jersey.client.ClientConfig;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

public class ClientClinicManagementREST {
    private static final String BASE_URL = "http://localhost:8080/WebClinicManagementRESTProject/rest/WebClinic";

    public static void main(String[] args) {
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget target = client.target(BASE_URL);
        Scanner scanner = new Scanner(System.in);

        try {
            boolean exit = false;

            while (!exit) {
                System.out.println("\n--- Clinic Management ---");
                System.out.println("1. Display Clinic Information (HTML)");
                System.out.println("2. Display All Patients (HTML)");
                System.out.println("3. Add Prescription");
                System.out.println("4. Exit");
                System.out.print("Select an option (1-4): ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        displayClinicInfo(target);
                        break;
                    case 2:
                        displayAllPatients(target);
                        break;
                    case 3:
                        addPrescription(target, scanner);
                        break;
                    case 4:
                        System.out.println("Exiting... Thank you!");
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void displayClinicInfo(WebTarget target) {
        String response = target.path("info")
                                .request(MediaType.TEXT_HTML)
                                .get(String.class);
        System.out.println("Clinic Information:");
        System.out.println(response);
    }

    private static void displayAllPatients(WebTarget target) {
        String response = target.path("patients")
                                .request(MediaType.TEXT_HTML)
                                .get(String.class);
        System.out.println("Patients List:");
        System.out.println(response);
    }

    private static void addPrescription(WebTarget target, Scanner scanner) {
        System.out.print("Enter Doctor ID: ");
        String doctorId = scanner.nextLine();

        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();

        System.out.print("Enter Prescription: ");
        String prescription = scanner.nextLine();

        String formData = "doctorId=" + doctorId + "&patientId=" + patientId + "&prescription=" + prescription;

        String response = target.path("addPrescription")
                                .request(MediaType.TEXT_HTML)
                                .post(Entity.entity(formData, MediaType.APPLICATION_FORM_URLENCODED), String.class);
        System.out.println(response);
    }
}
