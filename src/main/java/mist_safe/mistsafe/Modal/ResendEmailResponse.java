package mist_safe.mistsafe.Modal;

public class ResendEmailResponse {
    private String result;
    private String email;

    public ResendEmailResponse(String result, String email) {
        this.result = result;
        this.email = email;
    }

    public String getResult() {
        return result;
    }

    public String getEmail() {
        return email;
    }
} 
