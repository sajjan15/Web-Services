package clinicManagementREST;


import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Path("WebDoctor")//URL Mapping
public class WebDoctorResource {
    private Map<String, Doctor> doctorMap;

    public WebDoctorResource() {
        doctorMap = new HashMap<>();
        loadDoctorsFromFiles();
    }

    private void loadDoctorsFromFiles() {
        try {
        	 Scanner doctorFile = new Scanner(new FileReader("D:\\Web Programming\\WebServiceProject\\WebClinicManagementRESTProject\\Doctors.in"));
        	    while (doctorFile.hasNext()) {
        	        String doctorId = doctorFile.next();
        	        String doctorFName = doctorFile.next();
        	        String doctorLName = doctorFile.next();
        	        String specialization = doctorFile.next();
        	        int maxPatients = doctorFile.nextInt();
        	        double consultationFee = doctorFile.nextDouble();

        	        Doctor doctor = new Doctor(doctorId, doctorFName, doctorLName, specialization, maxPatients, consultationFee);
        	        doctorMap.put(doctorId, doctor);
        	    }
            doctorFile.close();
        } catch (FileNotFoundException e) {
            System.err.println("Data not found");
            e.printStackTrace();
        }
    }
    
    private void writeDoctorsFile() {
        try (FileWriter writer = new FileWriter("D:\\Web Programming\\WebServiceProject\\WebClinicManagementRESTProject\\Doctors.in")) {
            for (Doctor doctor : doctorMap.values()) {
                writer.write(String.format("%s\t%s\t%s\t%s\t%d\t%.2f\n",
                    doctor.getDoctorId(),
                    doctor.getDoctorFName(),
                    doctor.getDoctorLName(),
                    doctor.getSpecialization(),
                    doctor.getMaxPatients(),
                    doctor.getConsultationFee()
                ));
            }
        } catch (IOException e) {
            System.err.println("Failed to update the Doctors.in file.");
            e.printStackTrace();
        }
    }

    
    @GET
    @Path("/doctors")
    @Produces(MediaType.TEXT_HTML)
    public String displayHTMLDoctorInfo() {
        if (doctorMap.isEmpty()) {
            return "<html><body><h2>No doctors found in the system</h2></body></html>";
        }

        StringBuilder tableFormat = new StringBuilder();
        double totalRevenue = doctorMap.values().stream()
            .mapToDouble(doctor -> doctor.calculateTotalRevenue())
            .sum();

        tableFormat.append("<html><body>")
            .append("<h2>Print Doctors Record Collection</h2>")
            .append("<table border='1'>")
            .append("<tr><th>Doctor ID</th><th>Doctor Name</th><th>Specialization</th>")
            .append("<th>Max Patients</th><th>Consultation Fee</th><th>Total Revenue</th></tr>");

        doctorMap.values().stream()
            .sorted(Comparator.comparing(doctor -> doctor.getDoctorId()))
            .forEach(doctor -> {
                String name = doctor.getDoctorFName().concat(" ").concat(doctor.getDoctorLName());
                tableFormat.append("<tr><td>")
                    .append(doctor.getDoctorId()).append("</td><td>")
                    .append(name).append("</td><td>")
                    .append(doctor.getSpecialization()).append("</td><td>")
                    .append(doctor.getMaxPatients()).append("</td><td>")
                    .append(String.format("%.2f$", doctor.getConsultationFee())).append("</td><td>")
                    .append(String.format("%.2f$", doctor.calculateTotalRevenue())).append("</td></tr>");
            });

        tableFormat.append("</table>")
            .append("<h3>The Total Clinic Revenue is: ").append(String.format("%.2f$", totalRevenue)).append("</h3>")
            .append("</body></html>");

        return tableFormat.toString();
    }
    
    @GET
    @Path("/doctor/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Doctor getDoctorById(@PathParam("id") String doctorId) {
        return doctorMap.getOrDefault(doctorId, null);
    }
    
    @GET
    @Path("/doctorSpecialization/{spec}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Doctor> getDoctorsBySpecialization(@PathParam("spec") String specialization) {
        return doctorMap.values().stream()
            .filter(d -> d.getSpecialization().equalsIgnoreCase(specialization))
            .collect(Collectors.toList());
    }
    
    @GET
    @Path("/searchDoctor")
    @Produces(MediaType.APPLICATION_JSON)
    public Doctor searchDoctorById(@QueryParam("id") String doctorId) {
        return doctorMap.getOrDefault(doctorId, null);
    }
    
    @GET
    @Path("/searchSpecsDoctor")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Doctor> searchDoctorsBySpecialization(@QueryParam("spec") String specialization) {
        return doctorMap.values().stream()
            .filter(d -> d.getSpecialization().equalsIgnoreCase(specialization))
            .collect(Collectors.toList());
    }
    
    @POST
    @Path("/addDoctor")
    @Produces(MediaType.TEXT_HTML)
    public String addNewDoctor(
            @FormParam("doctorId") String doctorId,
            @FormParam("doctorFName") String doctorFName,
            @FormParam("doctorLName") String doctorLName,
            @FormParam("specialization") String specialization,
            @FormParam("maxPatients") int maxPatients,
            @FormParam("consultationFee") double consultationFee) {

        if (doctorMap.containsKey(doctorId)) {
            return "<html><body><h3 style='color:red;'>Error: Doctor with ID " + doctorId + " already exists.</h3></body></html>";
        }

        Doctor newDoctor = new Doctor(doctorId, doctorFName, doctorLName, specialization, maxPatients, consultationFee);

        doctorMap.put(doctorId, newDoctor);

        writeDoctorsFile();

        return "<html><body>"
                + "<h3>Doctor Added Successfully</h3>"
                + "<p>Doctor ID: " + doctorId + "</p>"
                + "<p>Name: " + doctorFName + " " + doctorLName + "</p>"
                + "<p>Specialization: " + specialization + "</p>"
                + "<p>Max Patients: " + maxPatients + "</p>"
                + "<p>Consultation Fee: " + String.format("%.2f$", consultationFee) + "</p>"
                + displayHTMLDoctorInfo()
                + "</body></html>";
    }
    
    @DELETE
    @Path("/deleteDoctor")
    @Produces(MediaType.TEXT_HTML)
    public String deleteDoctorById(@QueryParam("doctorId") String doctorId) {
        if (!doctorMap.containsKey(doctorId)) {
            return "<html><body><h3 style='color:red;'>Error: Doctor with ID " + doctorId + " does not exist.</h3></body></html>";
        }

        doctorMap.remove(doctorId);

        writeDoctorsFile();

        return "<html><body>"
                + "<h3 style='color:green;'>Doctor with ID " + doctorId + " deleted successfully.</h3>"
                + displayHTMLDoctorInfo()
                + "</body></html>";
    }





}