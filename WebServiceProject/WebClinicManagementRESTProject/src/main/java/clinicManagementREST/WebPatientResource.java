package clinicManagementREST;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Path("WebPatient")//URL Mapping
public class WebPatientResource {
    private Map<String, Patient> patientMap;

    public WebPatientResource() {
        patientMap = new HashMap<>();
        loadPatientsFromFiles();
    }

    
    private void loadPatientsFromFiles() {
    	try {
    	Scanner patientFile = new Scanner(new FileReader("D:\\Web Programming\\WebServiceProject\\WebClinicManagementRESTProject\\Patients.in"));
        while (patientFile.hasNext()) {
            String patientId = patientFile.next();
            String patientFName = patientFile.next();
            String patientLName = patientFile.next();
            int age = patientFile.nextInt();
            String condition = patientFile.next();
            String assignedDoctor = patientFile.next();
            String appointmentDate = patientFile.next();

            Patient patient = new Patient(patientId, patientFName, patientLName, age, condition, assignedDoctor, appointmentDate);
            patientMap.put(patientId, patient);
        }
        patientFile.close();
    	 } catch (FileNotFoundException e) {
             System.err.println("Data not found");
             e.printStackTrace();
         }
    	
    }
    
    private void writePatientsFile() {
        try (FileWriter writer = new FileWriter("D:\\Web Programming\\WebServiceProject\\WebClinicManagementRESTProject\\Patients.in")) {
            for (Patient patient : patientMap.values()) {
                writer.write(String.format("%s\t%s\t%s\t%d\t%s\t%s\t%s\n",
                    patient.getPatientId(),
                    patient.getPatientFName(),
                    patient.getPatientLName(),
                    patient.getAge(),
                    patient.getCondition().replace(" ", "_"),
                    patient.getAssignedDoctor(),
                    patient.getAppointmentDate()
                ));
            }
        } catch (IOException e) {
            System.err.println("Failed to update the Patients.in file.");
            e.printStackTrace();
        }
    }
    
    @GET
    @Path("/patients")
    @Produces(MediaType.TEXT_HTML)
    public String displayHTMLPatientInfo() {
        if (patientMap.isEmpty()) {
            return "<html><body><h2>No patients found in the system</h2></body></html>";
        }

        StringBuilder tableFormat = new StringBuilder();

        tableFormat.append("<html><body>")
            .append("<h2>Print Patient Record Collection</h2>")
            .append("<table border='1'>")
            .append("<tr><th>Patient ID</th><th>Patient Name</th><th>Age</th>")
            .append("<th>Condition</th><th>Assigned Doctor</th><th>Appointment Date</th></tr>");

        patientMap.values().stream()
            .sorted(Comparator.comparing(patient -> patient.getPatientId()))
            .forEach(patient -> {
        	String name = patient.getPatientFName().concat(" ").concat(patient.getPatientLName());
        	String condition = patient.getCondition().replaceAll("_", " ");
        	
            tableFormat.append("<tr><td>")
                .append(patient.getPatientId()).append("</td><td>")
                .append(name).append("</td><td>")
                .append(patient.getAge()).append("</td><td>")
                .append(condition).append("</td><td>")
                .append(patient.getAssignedDoctor()).append("</td><td>")
                .append(patient.getAppointmentDate()).append("</td></tr>");
        });

        tableFormat.append("</table>")
            .append("<h3>Total Patients: ").append(patientMap.size()).append("</h3>")
            .append("</body></html>");

        return tableFormat.toString();
    }
    
    @GET
    @Path("/patient/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Patient getPatientById(@PathParam("id") String patientId) {
        return patientMap.getOrDefault(patientId, null);
    }
    
    @GET
    @Path("/doctorPatients/{doctorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Patient> getPatientsByDoctor(@PathParam("doctorId") String doctorId) {
        return patientMap.values().stream()
            .filter(p -> p.getAssignedDoctor().equalsIgnoreCase(doctorId))
            .collect(Collectors.toList());
    }

    @GET
    @Path("/appointmentDate/{date}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Patient> getPatientsByAppointmentDate(@PathParam("date") String date) {
        return patientMap.values().stream()
            .filter(p -> p.getAppointmentDate().equals(date))
            .collect(Collectors.toList());
    }
    
    @GET
    @Path("/searchPatient")
    @Produces(MediaType.APPLICATION_JSON)
    public Patient searchPatientById(@QueryParam("id") String patientId) {
        return patientMap.getOrDefault(patientId, null);
    }

    
    @POST
    @Path("/addPatient")
    @Produces(MediaType.TEXT_HTML)
    public String addNewPatient(
        @FormParam("patientFName") String patientFName,
        @FormParam("patientLName") String patientLName,
        @FormParam("age") int age,
        @FormParam("condition") String condition,
        @FormParam("assignedDoctor") String assignedDoctor,
        @FormParam("appointmentDate") String appointmentDate)
     {
        String newPatientId = generateNextPatientId();

        Patient newPatient = new Patient(newPatientId, patientFName, patientLName, age, condition, assignedDoctor, appointmentDate);

        patientMap.put(newPatientId, newPatient);

        writePatientsFile();

        return "<html><body><h2>Appointment Booked sucessfully</h2>"
        	+ "<h3>Appointment is sucessfully booked with the following Details: </h3>"
            + "<p>Patient ID: " + newPatientId + "</p>"
            + "<p>Name: " + patientFName + " " + patientLName + "</p>"
            + "<p>Age: " + age + "</p><p>Condition: " + condition + "</p>"
            + "<p>Assigned Doctor: " + assignedDoctor + "</p>"
            + "<p>Appointment Date: " + appointmentDate + "</p>"
            + "</body></html>";
    }
    
    @POST
    @Path("/AddPatient")
    @Produces(MediaType.TEXT_HTML)
    public String addPatient(
        @FormParam("patientFName") String patientFName,
        @FormParam("patientLName") String patientLName,
        @FormParam("age") int age,
        @FormParam("condition") String condition,
        @FormParam("assignedDoctor") String assignedDoctor,
        @FormParam("appointmentDate") String appointmentDate)
     {
        String newPatientId = generateNextPatientId();

        Patient newPatient = new Patient(newPatientId, patientFName, patientLName, age, condition, assignedDoctor, appointmentDate);

        patientMap.put(newPatientId, newPatient);

        writePatientsFile();

        return "<html><body><h3>Patient Added Sucessfully! </h3>"
            + "<p>Patient ID: " + newPatientId + "</p>"
            + "<p>Name: " + patientFName + " " + patientLName + "</p>"
            + "<p>Age: " + age + "</p><p>Condition: " + condition + "</p>"
            + "<p>Assigned Doctor: " + assignedDoctor + "</p>"
            + "<p>Appointment Date: " + appointmentDate + "</p>"
            + displayHTMLPatientInfo()
            + "</body></html>";
    }

    @PUT
    @Path("/updateAppointmentDate")
    @Produces(MediaType.TEXT_HTML)
    public String updateAppointmentDate(
        @QueryParam("patientId") String patientId,
        @QueryParam("newDate") String newDate)
     {
    	
        Patient patient = patientMap.get(patientId);
        if (patient != null) {
            String oldDate = patient.getAppointmentDate();
            patient.setAppointmentDate(newDate);

            writePatientsFile();
            
            return "<html><body><h3>Appointment Date Updated Successfully</h3>"
                + "<p>Patient ID: " + patientId + "</p>"                
                + "<p>Old Appointment Date: " + oldDate + "</p>"
                + "<p>New Appointment Date: " + newDate + "</p>"
                + displayHTMLPatientInfo()
                + "</body></html>";
        } else {
            return "<html><body><h3>Patient Not Found</h3>"
                + "<p>No patient exists with ID: " + patientId + "</p>"
                + displayHTMLPatientInfo()
                + "</body></html>";
        }
    }
    
    @DELETE
    @Path("/deletePatient")
    @Produces(MediaType.TEXT_HTML)
    public String deletePatient(@QueryParam("patientId") String patientId) {
        Patient patient = patientMap.get(patientId);
        
        if (patient != null) {
            patientMap.remove(patientId);

            writePatientsFile();
            
            return "<html><body><h3>Patient Deleted Successfully</h3>"
                + "<p>Patient ID: " + patientId + " has been deleted.</p>"
                + displayHTMLPatientInfo()
                + "</body></html>";
        } else {
            return "<html><body><h3>Patient Not Found</h3>"
                + "<p>No patient exists with ID: " + patientId + "</p>"
                + "</body></html>";
        }
    }

    private String generateNextPatientId() {
        Optional<String> maxId = patientMap.keySet().stream()
            .max(Comparator.comparing(id -> Integer.parseInt(id.split("_")[1])));
        if (maxId.isPresent()) {
            int nextId = Integer.parseInt(maxId.get().split("_")[1]) + 1;
            return String.format("PAT_%03d", nextId);
        } else {
            return "PAT_001";
        }
    }
    
    @GET
    @Path("/patientSearch")
    @Produces(MediaType.APPLICATION_JSON)
    public String searchPatientsByappointmentDate(
        @QueryParam("doctorId") String doctorId, 
        @QueryParam("appointmentDate") String appointmentDate) {

        LocalDate selectedDate = LocalDate.parse(appointmentDate);
   
        System.out.println("Selected Date: " + selectedDate);

        List<Patient> patientsAfterDate = patientMap.values().stream()
            .filter(p -> {
                LocalDate patientAppointmentDate = LocalDate.parse(p.getAppointmentDate());

                System.out.println("Patient ID: " + p.getPatientId() + " Appointment Date: " + patientAppointmentDate);
                
                return p.getAssignedDoctor().equalsIgnoreCase(doctorId) &&
                        (patientAppointmentDate.isEqual(selectedDate) || 
                        patientAppointmentDate.isAfter(selectedDate));
            })
            .collect(Collectors.toList());

        System.out.println("Filtered Patients Count: " + patientsAfterDate.size());

        if (patientsAfterDate.isEmpty()) {
            return "There are no appointments after " + selectedDate;
        }
        
        StringBuilder result = new StringBuilder();

        result.append("Upcoming Appointments after " + selectedDate + " are: \n\n");

        for (Patient patient : patientsAfterDate) {
            result.append("Patient ID: ").append(patient.getPatientId()).append("\n")
                  .append("Name: ").append(patient.getPatientFName()).append(" ").append(patient.getPatientLName()).append("\n")
                  .append("Condition: ").append(patient.getCondition()).append("\n")
                  .append("Appointment Date: ").append(patient.getAppointmentDate()).append("\n")
                  .append("\n");
        }
        
        return result.toString();
    }
    
    

}
