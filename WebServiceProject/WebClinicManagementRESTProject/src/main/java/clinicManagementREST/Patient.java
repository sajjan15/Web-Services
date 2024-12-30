package clinicManagementREST;

public class Patient {
    private String patientId;
    private String patientFName;
    private String patientLName;
    private int age;
    private String condition;
    private String assignedDoctor;
    private String appointmentDate;

    public Patient() {
        this.patientId = "";
        this.patientFName = "";
        this.patientLName = "";
        this.age = 0;
        this.condition = "";
        this.assignedDoctor = "";
        this.appointmentDate = "";
    }

    public Patient(String patientId, String patientFName, String patientLName, int age, String condition, 
                  String assignedDoctor, String appointmentDate) {
        this.patientId = patientId;
        this.patientFName = patientFName;
        this.patientLName = patientLName;
        this.age = age;
        this.condition = condition;
        this.assignedDoctor = assignedDoctor;
        this.appointmentDate = appointmentDate;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientFName() {
        return patientFName;
    }

    public void setPatientFName(String patientFName) {
        this.patientFName = patientFName;
    }
    
    public String getPatientLName() {
        return patientLName;
    }
    
    public void setPatientLName(String patientLName) {
        this.patientLName = patientLName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getAssignedDoctor() {
        return assignedDoctor;
    }

    public void setAssignedDoctor(String assignedDoctor) {
        this.assignedDoctor = assignedDoctor;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    @Override
    public String toString() {
        return "PatientId: " + patientId + ", PatientFName: " + patientFName + ", PatientLName: " + patientLName + 
               ", Age: " + age + ", Condition: " + condition + ", AssignedDoctor: " + assignedDoctor + 
               ", AppointmentDate: " + appointmentDate;
    }
}