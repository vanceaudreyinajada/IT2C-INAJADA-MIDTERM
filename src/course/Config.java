package course;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

class Course {
    private int courseId;
    private String courseName;
    private String courseCode;
    private int credits;
    private String semester;
    private int year;

    public Course(int courseId, String courseName, String courseCode, int credits, String semester, int year) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.credits = credits;
        this.semester = semester;
        this.year = year;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Name: %s, Code: %s, Credits: %d, Semester: %s, Year: %d",
                courseId, courseName, courseCode, credits, semester, year);
    }

    public int getCourseId() {
        return courseId;
    }

   
}

public class Config {
    private static final String DATABASE_URL = "jdbc:sqlite:appv2.db";

    public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection(DATABASE_URL);
            System.out.println("Connection Successful");
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e);
        }
        return con;
    }

    public void addCourse(String courseName, String courseCode, int credits, String semester, int year) {
        String sql = "INSERT INTO Course(course_name, course_code, credits, semester, year) VALUES(?, ?, ?, ?, ?)";
        addRecord(sql, courseName, courseCode, credits, semester, year);
    }

    public void addRecord(String sql, Object... values) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) values[i]);
                } else {
                    pstmt.setString(i + 1, values[i].toString());
                }
            }

            pstmt.executeUpdate();
            System.out.println("Record added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding record: " + e.getMessage());
        }
    }

    public void updateCourse(int courseId, String courseName, String courseCode, Integer credits, String semester, Integer year) {
        StringBuilder sql = new StringBuilder("UPDATE Course SET ");
        ArrayList<Object> values = new ArrayList<>();

        if (courseName != null) {
            sql.append("course_name = ?, ");
            values.add(courseName);
        }
        if (courseCode != null) {
            sql.append("course_code = ?, ");
            values.add(courseCode);
        }
        if (credits != null) {
            sql.append("credits = ?, ");
            values.add(credits);
        }
        if (semester != null) {
            sql.append("semester = ?, ");
            values.add(semester);
        }
        if (year != null) {
            sql.append("year = ?, ");
            values.add(year);
        }

        sql.setLength(sql.length() - 2); 
        sql.append(" WHERE course_id = ?");
        values.add(courseId);

        updateRecord(sql.toString(), values.toArray());
    }

    public void updateRecord(String sql, Object... values) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) values[i]);
                } else {
                    pstmt.setString(i + 1, values[i].toString());
                }
            }

            pstmt.executeUpdate();
            System.out.println("Record updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating record: " + e.getMessage());
        }
    }

    public void viewCourses() {
        String sqlQuery = "SELECT * FROM Course";
        String[] columnHeaders = {"Course ID", "Course Name", "Course Code", "Credits", "Semester", "Year"};
        String[] columnNames = {"course_id", "course_name", "course_code", "credits", "semester", "year"};
        viewRecords(sqlQuery, columnHeaders, columnNames);
    }

    public void viewRecords(String sqlQuery, String[] columnHeaders, String[] columnNames) {
        if (columnHeaders.length != columnNames.length) {
            System.out.println("Error: Mismatch between column headers and column names.");
            return;
        }

        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
             ResultSet rs = pstmt.executeQuery()) {

            StringBuilder headerLine = new StringBuilder();
            headerLine.append("---------------------------------------------------------------------------------------------\n| ");
            for (String header : columnHeaders) {
                headerLine.append(String.format("%-20s | ", header));
            }
            headerLine.append("\n---------------------------------------------------------------------------------------------");

            System.out.println(headerLine.toString());

            while (rs.next()) {
                StringBuilder row = new StringBuilder("| ");
                for (String colName : columnNames) {
                    String value = rs.getString(colName);
                    row.append(String.format("%-20s | ", value != null ? value : ""));
                }
                System.out.println(row.toString());
            }
            System.out.println("---------------------------------------------------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("Error retrieving records: " + e.getMessage());
        }
    }

    public void deleteCourse(int courseId) {
        String sql = "DELETE FROM Course WHERE course_id = ?";
        deleteRecord(sql, courseId);
    }

    public void deleteRecord(String sql, Object... values) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) values[i]);
                } else {
                    pstmt.setString(i + 1, values[i].toString());
                }
            }

            pstmt.executeUpdate();
            System.out.println("Record deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting record: " + e.getMessage());
        }
    }
}

public class CourseManagementApp {
    public static void main(String[] args) {
        Config config = new Config();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nCourse Management Menu:");
            System.out.println("1. Add Course");
            System.out.println("2. Update Course");
            System.out.println("3. View Courses");
            System.out.println("4. Delete Course");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter course name: ");
                    String courseName = scanner.nextLine();
                    System.out.print("Enter course code: ");
                    String courseCode = scanner.nextLine();
                    System.out.print("Enter credits: ");
                    int credits = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter semester: ");
                    String semester = scanner.nextLine();
                    System.out.print("Enter year: ");
                    int year = scanner.nextInt();
                    config.addCourse(courseName, courseCode, credits, semester, year);
                    break;

                case 2:
                    System.out.print("Enter course ID to update: ");
                    int courseId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter new course name (leave blank to keep current): ");
                    String newCourseName = scanner.nextLine();
                    System.out.print("Enter new course code (leave blank to keep current): ");
                    String newCourseCode = scanner.nextLine();
                    System.out.print("Enter new credits (leave blank to keep current): ");
                    String creditsInput = scanner.nextLine();
                    Integer newCredits = creditsInput.isEmpty() ? null : Integer.parseInt(creditsInput);
                    System.out.print("Enter new semester (leave blank to keep current): ");
                    String newSemester = scanner.nextLine();
                    System.out.print("Enter new year (leave blank to keep current): ");
                    String yearInput = scanner.nextLine();
                    Integer newYear = yearInput.isEmpty() ? null : Integer.parseInt(yearInput);
                    config.updateCourse(courseId, 
                                        newCourseName.isEmpty() ? null : newCourseName, 
                                        newCourseCode.isEmpty() ? null : newCourseCode, 
                                        newCredits, 
                                        newSemester.isEmpty() ? null : newSemester, 
                                        newYear);
                    break;

                case 3:
                    config.viewCourses();
                    break;

                case 4:
                    System.out.print("Enter course ID to delete: ");
                    int deleteId = scanner.nextInt();
                    config.deleteCourse(deleteId);
                    break;

                case 5:
                    System.out.println
