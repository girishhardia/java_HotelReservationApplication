import model.Customer;

public class Driver {
    public static void main(String[] args) {
        System.out.println("This is a test class");
        // Customer customer = new Customer("first", "second", "j@domain.com");
        Customer customer = new Customer("first", "second", "email");
        System.out.println(customer);
    }
}
