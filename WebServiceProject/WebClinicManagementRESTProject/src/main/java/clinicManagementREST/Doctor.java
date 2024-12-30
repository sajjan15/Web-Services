package clinicManagementREST;

public class Doctor {
    private String doctorId;
    private String doctorFName;
    private String doctorLName;
    private String specialization;
    private int maxPatients;
    private double consultationFee;

    public Doctor() {
        this.doctorId = "";
        this.doctorFName = "";
        this.doctorLName = "";
        this.specialization = "";
        this.maxPatients = 0;
        this.consultationFee = 0.0;
    }

    public Doctor(String doctorId, String doctorFName, String doctorLName, String specialization, int maxPatients, double consultationFee) {
        this.doctorId = doctorId;
        this.doctorFName = doctorFName;
        this.doctorLName = doctorLName;
        this.specialization = specialization;
        this.maxPatients = maxPatients;
        this.consultationFee = consultationFee;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorFName() {
        return doctorFName;
    }

    public void setDoctorFName(String doctorFName) {
        this.doctorFName = doctorFName;
    }
    
    public String getDoctorLName() {
        return doctorLName;
    }
    
    public void setDoctorLName(String doctorLName) {
        this.doctorLName = doctorLName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getMaxPatients() {
        return maxPatients;
    }

    public void setMaxPatients(int maxPatients) {
        this.maxPatients = maxPatients;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public double calculateTotalRevenue() {
        return maxPatients * consultationFee;
    }

    @Override
    public String toString() {
        return "DoctorId: " + doctorId + ", DoctorFName: " + doctorFName + 
        	   ", DoctorFName: " + doctorFName + ", Specialization: " + 
        		specialization + ", MaxPatients: " + maxPatients + 
        		String.format("%.2f", consultationFee);
    }
}