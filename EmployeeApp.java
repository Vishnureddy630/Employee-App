import java.sql.*;
import java.util.Scanner;

public class EmployeeApp {

    static final String URL = "jdbc:mysql://localhost:3306/EmployeeDB?createDatabaseIfNotExist=true";
    static final String USER = "root"; // your MySQL username
    static final String PASSWORD = "your_password"; // your MySQL password

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Create table if not exists
            String createTable = "CREATE TABLE IF NOT EXISTS employees (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(100)," +
                    "department VARCHAR(100)," +
                    "salary DOUBLE)";
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(createTable);
            }

            while (true) {
                System.out.println("\n--- Employee Management ---");
                System.out.println("1. Add Employee");
                System.out.println("2. View Employees");
                System.out.println("3. Update Employee");
                System.out.println("4. Delete Employee");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        sc.nextLine();
                        System.out.print("Enter name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter department: ");
                        String dept = sc.nextLine();
                        System.out.print("Enter salary: ");
                        double salary = sc.nextDouble();

                        String insertSQL = "INSERT INTO employees (name, department, salary) VALUES (?, ?, ?)";
                        try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                            ps.setString(1, name);
                            ps.setString(2, dept);
                            ps.setDouble(3, salary);
                            ps.executeUpdate();
                            System.out.println("Employee added successfully.");
                        }
                        break;

                    case 2:
                        String selectSQL = "SELECT * FROM employees";
                        try (Statement stmt = conn.createStatement();
                             ResultSet rs = stmt.executeQuery(selectSQL)) {
                            System.out.println("\n--- Employee List ---");
                            while (rs.next()) {
                                System.out.printf("ID: %d | Name: %s | Dept: %s | Salary: %.2f%n",
                                        rs.getInt("id"),
                                        rs.getString("name"),
                                        rs.getString("department"),
                                        rs.getDouble("salary"));
                            }
                        }
                        break;

                    case 3:
                        System.out.print("Enter employee ID to update: ");
                        int uid = sc.nextInt(); sc.nextLine();
                        System.out.print("New name: ");
                        String newName = sc.nextLine();
                        System.out.print("New department: ");
                        String newDept = sc.nextLine();
                        System.out.print("New salary: ");
                        double newSalary = sc.nextDouble();

                        String updateSQL = "UPDATE employees SET name=?, department=?, salary=? WHERE id=?";
                        try (PreparedStatement ps = conn.prepareStatement(updateSQL)) {
                            ps.setString(1, newName);
                            ps.setString(2, newDept);
                            ps.setDouble(3, newSalary);
                            ps.setInt(4, uid);
                            int rows = ps.executeUpdate();
                            System.out.println(rows > 0 ? "Updated successfully." : "Employee not found.");
                        }
                        break;

                    case 4:
                        System.out.print("Enter employee ID to delete: ");
                        int did = sc.nextInt();
                        String deleteSQL = "DELETE FROM employees WHERE id=?";
                        try (PreparedStatement ps = conn.prepareStatement(deleteSQL)) {
                            ps.setInt(1, did);
                            int rows = ps.executeUpdate();
                            System.out.println(rows > 0 ? "Deleted successfully." : "Employee not found.");
                        }
                        break;

                    case 5:
                        System.out.println("Goodbye!");
                        return;

                    default:
                        System.out.println("Invalid choice.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
