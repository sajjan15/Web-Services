package clientClinicManagementREST;

import java.util.Scanner;
import org.glassfish.jersey.client.ClientConfig;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class ClientPatientManagementREST {
    private static final String BASE_URL = "http://localhost:8080/WebClinicManagementRESTProject/rest/WebPatient";

    public static void main(String[] args) {
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        WebTarget target = client.target(BASE_URL);
        Scanner scanner = new Scanner(System.in);

        try {
            String choice;
            do {
                System.out.println("\n--- Patient Management System ---");
                System.out.println("1. View All Patients (HTML)");
                System.out.println("2. Search Patient by ID");
                System.out.println("3. Search Patients by Doctor ID");
                System.out.println("4. Search Patients by Appointment Date");
                System.out.println("5. Book an Appointment");
                System.out.println("6. Reschedule an Appointment");
                System.out.println("7. Delete Patient");
                System.out.println("8. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        displayAllPatients(target);
                        break;
                    case "2":
                        searchPatientById(target, scanner);
                        break;
                    case "3":
                        searchPatientsByDoctor(target, scanner);
                        break;
                    case "4":
                        searchPatientsByDate(target, scanner);
                        break;
                    case "5":
                        addNewPatient(target, scanner);
                        break;
                    case "6":
                        updateAppointmentDate(target, scanner);
                        break;
                    case "7":
                        deletePatient(target, scanner);
                        break;
                    case "8":
                        System.out.println("Exiting Patient Management System. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (!choice.equals("8"));
        } finally {
            scanner.close();
            client.close();
        }
    }

    private static void displayAllPatients(WebTarget target) {
        System.out.println("\nPatients HTML Output:");
        String response = target.path("/patients")
                .request()
                .accept(MediaType.TEXT_HTML)
                .get(String.class);
        System.out.println(response);
    }

    private static void searchPatientById(WebTarget target, Scanner scanner) {
        System.out.print("\nEnter Patient ID to search: ");
        String patientId = scanner.nextLine();
        String response = target.path("/patient/" + patientId)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(String.class);
        System.out.println("\nPatient JSON Output:");
        System.out.println(response);
    }

    private static void searchPatientsByDoctor(WebTarget target, Scanner scanner) {
        System.out.print("\nEnter Doctor ID: ");
        String doctorId = scanner.nextLine();
        String response = target.path("/doctorPatients/" + doctorId)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(String.class);
        System.out.println("\nPatients under Doctor " + doctorId + ":");
        System.out.println(response);
    }

    private static void searchPatientsByDate(WebTarget target, Scanner scanner) {
        System.out.print("\nEnter Appointment Date (YYYY-MM-DD): ");
        String appointmentDate = scanner.nextLine();
        String response = target.path("/appointmentDate/" + appointmentDate)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get(String.class);
        System.out.println("\nPatients with Appointment Date " + appointmentDate + ":");
        System.out.println(response);
    }

    private static void addNewPatient(WebTarget target, Scanner scanner) {
        System.out.print("\nEnter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Age: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Condition: ");
        String condition = scanner.nextLine();
        System.out.print("Enter Assigned Doctor: ");
        String doctor = scanner.nextLine();
        System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        Response response = target.path("/addPatient")
                .request(MediaType.APPLICATION_FORM_URLENCODED)
                .post(Entity.form(new jakarta.ws.rs.core.Form()
                        .param("patientFName", firstName)
                        .param("patientLName", lastName)
                        .param("age", String.valueOf(age))
                        .param("condition", condition)
                        .param("assignedDoctor", doctor)
                        .param("appointmentDate", date)));

        System.out.println("\nResponse:");
        System.out.println(response.readEntity(String.class));
    }

    private static void updateAppointmentDate(WebTarget target, Scanner scanner) {
        System.out.print("\nEnter Patient ID: ");
        String patientId = scanner.nextLine();
        System.out.print("Enter New Appointment Date (YYYY-MM-DD): ");
        String newDate = scanner.nextLine();

        String response = target.path("/updateAppointmentDate")
                .queryParam("patientId", patientId)
                .queryParam("newDate", newDate)
                .request()
                .put(Entity.text(""), String.class);

        System.out.println("\nResponse:");
        System.out.println(response);
    }

    private static void deletePatient(WebTarget target, Scanner scanner) {
        System.out.print("\nEnter Patient ID to delete: ");
        String patientId = scanner.nextLine();

        String response = target.path("/deletePatient")
                .queryParam("patientId", patientId)
                .request()
                .delete(String.class);

        System.out.println("\nResponse:");
        System.out.println(response);
    }
}
