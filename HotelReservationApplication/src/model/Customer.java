package model;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Represents a customer in the hotel reservation system.
 */
public class Customer {
    private final String firstName;
    private final String lastName;
    private final String email;

    // Regex pattern for email validation: name@domain.extension
    private static final String EMAIL_REGEX = "^(.+)@(.+)\\.(.+)$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_REGEX);

    public Customer(String firstName, String lastName, String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    private boolean isValidEmail(String email) {
        return PATTERN.matcher(email).matches();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "First Name: " + firstName + " Last Name: " + lastName + " Email: " + email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(email, customer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
