package clientClinicManagementREST;

import java.util.Scanner;
import org.glassfish.jersey.client.ClientConfig;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

public class ClientDoctorManagementREST {
    private static final String BASE_URL = "http://localhost:8080/WebClinicManagementRESTProject/rest/WebDoctor";

    public static void main(String[] args) {
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget target = client.target(BASE_URL);
        Scanner scanner = new Scanner(System.in);

        try {
            boolean exit = false;

            while (!exit) {
                System.out.println("\n--- Clinic Management ---");
                System.out.println("1. Display All Doctors (HTML)");
                System.out.println("2. Search Doctor by ID (JSON)");
                System.out.println("3. Search Doctors by Specialization (JSON)");
                System.out.println("4. Add New Doctor");
                System.out.println("5. Delete Doctor by ID");
                System.out.println("6. Exit");
                System.out.print("Select an option (1-6): ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        displayAllDoctors(target);
                        break;
                    case 2:
                        searchDoctorById(target, scanner);
                        break;
                    case 3:
                        searchDoctorsBySpecialization(target, scanner);
                        break;
                    case 4:
                        addNewDoctor(target, scanner);
                        break;
                    case 5:
                        deleteDoctorById(target, scanner);
                        break;
                    case 6:
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
            client.close();
        }
    }

    private static void displayAllDoctors(WebTarget target) {
        try {
            System.out.println("\nDoctors HTML Output:");
            String response = target.path("/doctors")
                    .request()
                    .accept(MediaType.TEXT_HTML)
                    .get(String.class);
            System.out.println(response);
        } catch (Exception e) {
            System.out.println("Error displaying doctors: " + e.getMessage());
        }
    }

    private static void searchDoctorById(WebTarget target, Scanner scanner) {
        System.out.print("\nEnter Doctor ID to search: ");
        String doctorId = scanner.nextLine();

        try {
            String response = target.path("/doctor/" + doctorId)
                    .request()
                    .accept(MediaType.APPLICATION_JSON)
                    .get(String.class);
            System.out.println("\nDoctor JSON Output:");
            System.out.println(response);
        } catch (Exception e) {
            System.out.println("Error searching for doctor: " + e.getMessage());
        }
    }

    private static void searchDoctorsBySpecialization(WebTarget target, Scanner scanner) {
        System.out.print("\nEnter Specialization to search: ");
        String specialization = scanner.nextLine();

        try {
            String response = target.path("/doctorSpecialization/" + specialization)
                    .request()
                    .accept(MediaType.APPLICATION_JSON)
                    .get(String.class);
            System.out.println("\nDoctors by Specialization JSON Output:");
            System.out.println(response);
        } catch (Exception e) {
            System.out.println("Error searching by specialization: " + e.getMessage());
        }
    }

    private static void addNewDoctor(WebTarget target, Scanner scanner) {
        System.out.print("\nEnter Doctor ID: ");
        String doctorId = scanner.nextLine();
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Specialization: ");
        String specialization = scanner.nextLine();
        System.out.print("Enter Max Patients: ");
        int maxPatients = scanner.nextInt();
        System.out.print("Enter Consultation Fee: ");
        double consultationFee = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        try {
            String response = target.path("/addDoctor")
                    .request(MediaType.TEXT_HTML)
                    .post(Entity.form(new jakarta.ws.rs.core.Form()
                            .param("doctorId", doctorId)
                            .param("doctorFName", firstName)
                            .param("doctorLName", lastName)
                            .param("specialization", specialization)
                            .param("maxPatients", String.valueOf(maxPatients))
                            .param("consultationFee", String.valueOf(consultationFee))),
                            String.class);
            System.out.println("\nResponse:");
            System.out.println(response);
        } catch (Exception e) {
            System.out.println("Error adding doctor: " + e.getMessage());
        }
    }

    private static void deleteDoctorById(WebTarget target, Scanner scanner) {
        System.out.print("\nEnter Doctor ID to delete: ");
        String doctorId = scanner.nextLine();

        try {
            String response = target.path("/deleteDoctor")
                    .queryParam("doctorId", doctorId)
                    .request()
                    .accept(MediaType.TEXT_HTML)
                    .delete(String.class);
            System.out.println("\nResponse:");
            System.out.println(response);
        } catch (Exception e) {
            System.out.println("Error deleting doctor: " + e.getMessage());
        }
    }
}
