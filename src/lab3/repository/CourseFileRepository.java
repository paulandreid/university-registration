package lab3.repository;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import lab3.model.Course;
import java.util.List;
import java.io.File;

public class CourseFileRepository implements ICrudRepository<Course> {

    private List<Course> courses = new ArrayList<>();
    private File file;

    /**
     * Default constructor
     *
     * @throws Exception if the file cannot be opened
     */
    public CourseFileRepository() throws Exception {
        this.file = new File("files/courses.txt");
        this.readFromFile();
    }

    /**
     * @param id -the id of the entity to be returned id must not be null
     * @return the entity with the specified id or null - if there is no entity with the given id
     */
    @Override
    public Course findOne(Long id) {
        for (Course course : this.courses) {
            if (course.getCourseId() == id)
                return course;
        }
        return null;
    }

    /**
     * @return all entities
     */
    @Override
    public Iterable<Course> findAll() {
        return this.courses;
    }

    /**
     * @param entity entity must be not null
     * @return null- if the given entity is saved otherwise returns the entity (id already exists)
     * @throws Exception if file i/o fails
     */
    @Override
    public Course save(Course entity) throws Exception {
        for (Course course : this.courses) {
            if (course.getCourseId() == (entity).getCourseId())
                return entity;
        }
        this.courses.add(entity);
        this.saveToFile();
        return null;
    }

    /**
     * removes the entity with the specified id
     *
     * @param id id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws Exception if file i/o fails
     */
    @Override
    public Course delete(Long id) throws Exception {
        for (Course course : this.courses) {
            if (course.getCourseId() == id) {
                this.courses.remove(course);
                this.saveToFile();
                return course;
            }
        }
        return null;
    }

    /**
     * @param entity entity must be not null
     * @return null- if the given entity is saved otherwise returns the entity (id already exists)
     * @throws Exception if file i/o fails
     */
    @Override
    public Course update(Course entity) throws Exception {
        for (int i = 0; i < this.courses.size(); i++) {
            if (courses.get(i).getCourseId() == (entity).getCourseId()) {
                this.courses.set(i, entity);
                this.saveToFile();
                return null;
            }
        }
        return entity;
    }

    /**
     * saves all courses to a file
     *
     * @throws Exception if the file path is invalid
     */
    private void saveToFile() throws Exception {
        PrintWriter output = new PrintWriter(this.file);
        for (Course course : this.courses) {
            output.write(course.getCourseId() + ";"
                    + course.getName() + ";"
                    + course.getMaxEnrollment() + ";"
                    + course.getCredits() + ";"
                    + course.getTeacher() + ";");
            if (course.getStudentsEnrolled().size() == 0) {
                output.write("-");
            } else {
                for (long student : course.getStudentsEnrolled()) {
                    output.write(student + ",");
                }
            }
            output.write("\n");
        }
        output.close();
    }

    /**
     * Loads course information from a file
     *
     * @throws Exception if the file path is invalid
     */
    private void readFromFile() throws Exception {
        Scanner scanner = new Scanner(this.file);

        while (scanner.hasNextLine()) {
            String[] courseData = scanner.nextLine().split(";");

            Course course = new Course(Long.parseLong(courseData[0]), courseData[1], Integer.parseInt(courseData[2]), Integer.parseInt(courseData[3]));
            course.setTeacher(Long.parseLong(courseData[4]));

            if (courseData[5].compareTo("-") != 0) {
                String[] studentData = courseData[5].split(",");
                List<Long> studentsEnrolled = new ArrayList<>();
                for (String student : studentData) {
                    studentsEnrolled.add(Long.parseLong(student));
                }
                course.setStudentsEnrolled(studentsEnrolled);
            }

            this.save(course);
        }
        scanner.close();
    }
}
