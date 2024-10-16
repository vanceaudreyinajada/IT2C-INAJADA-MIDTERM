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

    public int getCourseId() {
        return courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Name: %s, Code: %s, Credits: %d, Semester: %s, Year: %d",
                courseId, courseName, courseCode, credits, semester, year);
    }
}

class CourseManagement {
    private ArrayList<Course> courses;
    private int nextId;

    public CourseManagement() {
        courses = new ArrayList<>();
        nextId = 1;
    }

    public void addCourse(String courseName, String courseCode, int credits, String semester, int year) {
        Course course = new Course(nextId++, courseName, courseCode, credits, semester, year);
        courses.add(course);
        System.out.println("Course added successfully.");
    }

    public void updateCourse(int courseId, String courseName, String courseCode, Integer credits, String semester, Integer year) {
        for (Course course : courses) {
            if (course.getCourseId() == courseId) {
                if (courseName != null) course.setCourseName(courseName);
                if (courseCode != null) course.setCourseCode(courseCode);
                if (credits != null) course.setCredits(credits);
                if (semester != null) course.setSemester(semester);
                if (year != null) course.setYear(year);
                System.out.println("Course updated successfully.");
                return;
            }
        }
        System.out.println("Course not found.");
    }

    public void viewCourses() {
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
            return;
        }
        for (Course course : courses) {
            System.out.println(course);
        }
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nCourse Management Menu:");
            System.out.println("1. Add Course");
            System.out.println("2. Update Course");
            System.out.println("3. View Courses");
            System.out.println("4. Exit");
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
                    addCourse(courseName, courseCode, credits, semester, year);
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
                    updateCourse(courseId, 
                                 newCourseName.isEmpty() ? null : newCourseName, 
                                 newCourseCode.isEmpty() ? null : newCourseCode, 
                                 newCredits, 
                                 newSemester.isEmpty() ? null : newSemester, 
                                 newYear);
                    break;

                case 3:
                    viewCourses();
                    break;

                case 4:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }
}

public class CourseManagementApp {
    public static void main(String[] args) {
        CourseManagement cms = new CourseManagement();
        cms.menu();
    }
}
