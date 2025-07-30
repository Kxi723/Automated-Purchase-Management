package Purchase_Order_Management;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class KxiCheckAccount {
    private static final int MAX_ATTEMPTS = 3;
    private static int usedAttempts = 0;
    private final String userFile = "Name List.txt";
    
    public KxiLogInReturnValue authenticate(String idInput, String inputPassword, String selectedRole) {
        KxiLogInReturnValue logInResult = new KxiLogInReturnValue();
        logInResult.calcRemainingAttempts(MAX_ATTEMPTS - usedAttempts);
        
        // log in 3 time but incorrect
        if (usedAttempts == MAX_ATTEMPTS) {
            logInResult.setMessage("You have entered too many incorrect passwords or ID.");
            logInResult.setSuccess(false);
            return logInResult;
        }
        
        // no id
        if (idInput == null || idInput.trim().isEmpty()) {
            logInResult.setMessage("ID cannot be empty.");
            logInResult.setSuccess(false);
            return logInResult;
        }
        
        // no password
        if (inputPassword == null || inputPassword.trim().isEmpty()) {
            logInResult.setMessage("Password cannot be empty.");
            logInResult.setSuccess(false);
            return logInResult;
        }
        
        // read file
        try (BufferedReader fileReader = new BufferedReader(new FileReader(userFile))) {
            String line;
            boolean idFound = false;
            
            while ((line = fileReader.readLine()) != null) {
                String[] parts = line.split(";");
                
                if (parts.length < 6) {
                    continue;
                }
                
                String readUserRole = parts[0];
                String readUserID = parts[1];
                String readUserName = parts[2];
                String readUserPassword = parts[3];
                
                // check id
                if (readUserID.equals(idInput)) {
                    idFound = true;
                    
                    // check password same
                    if (readUserPassword.equals(inputPassword)) {
                        // check role same
                        if (readUserRole.equals(selectedRole)) {
                            logInResult.setUserRole(readUserRole);
                            logInResult.setUserName(readUserName);
                            logInResult.setSuccess(true);
                        } else {
                            // role is not same
                            logInResult.setMessage("Sorry you don't have permission to access " + selectedRole);
                            usedAttempts++;
                            logInResult.calcRemainingAttempts(MAX_ATTEMPTS - usedAttempts);
                            logInResult.setSuccess(false);
                        }
                        return logInResult;
                    } else {
                        // password not same
                        logInResult.setMessage("Password incorrect");
                        usedAttempts++;
                        logInResult.calcRemainingAttempts(MAX_ATTEMPTS - usedAttempts);
                        logInResult.setSuccess(false);
                        return logInResult;
                    }
                }
            }
            
            // id not found
            if (idFound == false) {
                logInResult.setMessage("ID not found");
                usedAttempts++;
                logInResult.calcRemainingAttempts(MAX_ATTEMPTS - usedAttempts);
                logInResult.setSuccess(false);
                return logInResult;
            }
            
        // file not found
        } catch (IOException e) {
            try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(userFile, true))) {
                fileWriter.write("Administrator;ID027;Jason Lai Kwang Xi;Kxi6!4723;01110272128;tp080522@mail.apu.edu.my");
                fileWriter.newLine();

                logInResult.setMessage("Back up file has just loaded.");
                logInResult.calcRemainingAttempts(0);

            } catch (IOException createException) {
                logInResult.setMessage("System Error:- " + createException.getMessage());
            }
            logInResult.setSuccess(false);
        }
        
        return logInResult;
    }
}