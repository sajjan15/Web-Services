package clinicManagementREST;

import jakarta.ws.rs.Consumes;
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
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Path("WebClinic")//URL Mapping
public class WebClinicResource {
    private Map<String, Doctor> doctorMap;
    private Map<String, Patient> patientMap;

    public WebClinicResource() {
        doctorMap = new HashMap<>();
        patientMap = new HashMap<>();
        loadDoctorsFromFiles();
        loadPatientsFromFiles();
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

    @GET
    @Path("/info")
    @Produces(MediaType.TEXT_HTML)
    public String getClinicInfo() {
        if (doctorMap.isEmpty() && patientMap.isEmpty()) {
            return "<html><body><h2>No data available</h2></body></html>";
        }

        StringBuilder clinicInfo = new StringBuilder("<html><body><h2>Print Clinic Records Collection</h2>");

        if (!doctorMap.isEmpty()) {
            Map<String, Integer> specsCount = new HashMap<>();
            Map<String, Double> feeTotals = new HashMap<>();
            Map<String, Integer> feeCounts = new HashMap<>();
            double totalRevenue = 0.0;

            for (Doctor doctor : doctorMap.values()) {
                String specialization = doctor.getSpecialization();
                double fee = doctor.getConsultationFee();

                specsCount.put(specialization, specsCount.getOrDefault(specialization, 0) + 1);
                feeTotals.put(specialization, feeTotals.getOrDefault(specialization, 0.0) + fee);
                feeCounts.put(specialization, feeCounts.getOrDefault(specialization, 0) + 1);
                totalRevenue += fee;
            }

            clinicInfo.append("<h3>Printing the total number of Doctors by Specialization:</h3>")
                .append("<table border='1'><tr><th>Specialization</th><th>Total Count</th></tr>");
            for (Map.Entry<String, Integer> entry : specsCount.entrySet()) {
                clinicInfo.append("<tr><td>").append(entry.getKey()).append("</td><td>").append(entry.getValue()).append("</td></tr>");
            }
            clinicInfo.append("</table>");

            clinicInfo.append("<h3>Printing Doctor's Average Consultation Fee by Specialization:</h3>")
                .append("<table border='1'><tr><th>Specialization</th><th>Average Consultation Fee</th></tr>");
            for (String specialization : feeTotals.keySet()) {
                double averageFee = feeTotals.get(specialization) / feeCounts.get(specialization);
                clinicInfo.append("<tr><td>").append(specialization).append("</td><td>").append(String.format("%.2f$", averageFee)).append("</td></tr>");
            }
            clinicInfo.append("</table>");

            clinicInfo.append("<h3>The Total number of Doctors is: ").append(doctorMap.size()).append("</h3>");
            clinicInfo.append("<h3>The Total Consultation Fees is: ").append(String.format("%.2f$", totalRevenue)).append("</h3>");
        } else {
            clinicInfo.append("<h3>No doctors available</h3>");
        }

        if (!patientMap.isEmpty()) {
            int childrenCount = 0;
            int adultsCount = 0;
            int seniorCitizenCount = 0;

            for (Patient patient : patientMap.values()) {
                int age = patient.getAge();
                if (age < 18) {
                    childrenCount++;
                } else if (age < 60) {
                    adultsCount++;
                } else {
                    seniorCitizenCount++;
                }
            }

            clinicInfo.append("<h3>Print total number of Patients by age:</h3>")
                .append("<table border='1'><tr><th>Age Group</th><th>Total Count</th></tr>")
                .append("<tr><td>Children</td><td>").append(childrenCount).append("</td></tr>")
                .append("<tr><td>Adults</td><td>").append(adultsCount).append("</td></tr>")
                .append("<tr><td>Senior Citizens</td><td>").append(seniorCitizenCount).append("</td></tr>")
                .append("</table>");

            clinicInfo.append("<h3>The Total number of Patients is: ").append(patientMap.size()).append("</h3>");
            
        } else {
            clinicInfo.append("<h3>No patients available</h3>");
        }

        clinicInfo.append("</body></html>");
        return clinicInfo.toString();
    }
    
    @GET
    @Path("/patients")
    @Produces(MediaType.TEXT_HTML)
    public String getPatientsAsHTML() {
        Set<String> seenPatientIds = new HashSet<>();
        List<Patient> uniquePatients = new ArrayList<>();

        for (Patient patient : patientMap.values()) {
            if (!seenPatientIds.contains(patient.getPatientId())) {
                uniquePatients.add(patient);
                seenPatientIds.add(patient.getPatientId());
            }
        }

        uniquePatients.sort(Comparator.comparing(Patient::getPatientId));

        StringBuilder html = new StringBuilder();
        uniquePatients.forEach(patient -> {
            html.append("<tr>")
                .append("<td>").append(patient.getPatientId()).append("</td>")
                .append("<td>").append(patient.getPatientFName()).append(" ").append(patient.getPatientLName()).append("</td>")
                .append("<td>").append(patient.getAge()).append("</td>")
                .append("<td>").append(patient.getCondition()).append("</td>")
                .append("<td>").append(patient.getAssignedDoctor()).append("</td>")
                .append("<td>")
                .append("<a href='PrescriptionForm.html?doctorId=").append(patient.getAssignedDoctor())
                .append("&patientId=").append(patient.getPatientId())
                .append("' class='btn'>Prescribe</a>")
                .append("</td>")
                .append("</tr>");
        });

        return html.toString();
    }


    private Doctor getDoctorById(String doctorId) {
        return doctorMap.get(doctorId);
    }

    private Patient getPatientById(String patientId) {
        return patientMap.get(patientId);
    }

    @POST
    @Path("/addPrescription")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String addPrescription(@FormParam("doctorId") String doctorId,
                                   @FormParam("patientId") String patientId,
                                   @FormParam("prescription") String prescription) {
        Doctor doctor = getDoctorById(doctorId);
        Patient patient = getPatientById(patientId);

        if (doctor == null || patient == null) {
            return "<html><body><h3>Error: Invalid Doctor ID or Patient ID.</h3></body></html>";
        }

        Prescription newPrescription = new Prescription(
                doctorId,
                doctor.getDoctorFName() + " " + doctor.getDoctorLName(),
                patientId,
                patient.getPatientFName() + " " + patient.getPatientLName(),
                prescription,
                LocalDate.now()
        );

        newPrescription.saveToFile();

        return "<html><body><h3>Prescription Submitted Successfully!</h3></body></html>";
    }
}