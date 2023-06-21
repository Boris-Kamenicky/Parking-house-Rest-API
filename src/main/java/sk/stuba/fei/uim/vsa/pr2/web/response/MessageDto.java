package sk.stuba.fei.uim.vsa.pr2.web.response;


public class MessageDto {

    private String message;
    private Boolean error;

    public static MessageDto buildError(String message) {
        return new MessageDto(message, true);
    }

    public MessageDto(String message, Boolean error) {
        this.message = message;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }
}
