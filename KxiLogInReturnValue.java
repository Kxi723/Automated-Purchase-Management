package Purchase_Order_Management;

public class KxiLogInReturnValue {
    private boolean success;
    private String message;
    private String userRole;
    private int remainingAttempts;
    private String userName;
    
    public KxiLogInReturnValue() {
        this.success = false;
        this.message = "";
        this.userRole = "";
        this.remainingAttempts = 0;
        this.userName = "";
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getUserRole() {
        return userRole;
    }
    
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public int getRemainingAttempts() {
        return remainingAttempts;
    }
    
    public void calcRemainingAttempts(int remainingAttempts) {
        this.remainingAttempts = remainingAttempts;
    }
}