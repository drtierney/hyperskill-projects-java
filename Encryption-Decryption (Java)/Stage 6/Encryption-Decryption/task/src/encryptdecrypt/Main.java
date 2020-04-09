package encryptdecrypt;

public class Main {
    public static void main(String[] args) {
        Task task = new Task();
        try {
            task.run(args);
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }
}