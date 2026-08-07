public enum Command {
    ADD, LIST, FILTER, UPDATE, DELETE, EXIT;

    public static Command fromString(String input) {
        if (input == null) return null;
        try {
            return Command.valueOf(input.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}