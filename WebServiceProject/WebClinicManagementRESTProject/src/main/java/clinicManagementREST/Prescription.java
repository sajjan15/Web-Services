package clinicManagementREST;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class Prescription {
    private String doctorId;
    private String doctorName;
    private String patientId;
    private String patientName;
    private String prescription;
    private LocalDate date;

    public Prescription(String doctorId, String doctorName, String patientId, String patientName, String prescription, LocalDate date) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.prescription = prescription;
        this.date = date;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPrescription() {
        return prescription;
    }

    public LocalDate getDate() {
        return date;
    }

    public void saveToFile() {
        String filePath = "D:\\Web Programming\\WebServiceProject\\WebClinicManagementRESTProject\\Prescriptions.in";
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(String.format("%s\t%s\t%s\t%s\t%s\t%s\n",
                doctorId, doctorName, patientId, patientName, prescription, date.toString()));
        } catch (IOException e) {
            System.err.println("Failed to save prescription: " + e.getMessage());
        }
    }
}
