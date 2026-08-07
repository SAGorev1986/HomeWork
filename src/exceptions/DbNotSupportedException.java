package exceptions;

public class DbNotSupportedException extends Exception {
    public DbNotSupportedException(String message) {
        super(message);
    }
}