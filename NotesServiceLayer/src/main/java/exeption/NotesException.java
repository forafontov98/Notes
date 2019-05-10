package exeption;

public class NotesException extends RuntimeException{
    public NotesException(Throwable cause) { super(cause);}
    public NotesException(String message, Throwable cause) {super(message, cause);}
    public NotesException(String message) {super(message);}
}
