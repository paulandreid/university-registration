package lab3.controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lab3.exceptions.*;
import lab3.model.Course;
import lab3.model.Student;
import lab3.model.Teacher;
import lab3.repository.*;

import java.io.IOException;
import java.util.*;

public class RegistrationSystem {

    public TextField studentIdField;
    public AnchorPane mainPane;
    public Button studentBtt;
    @FXML
    public Button teacherBtt;
    @FXML
    public Label studentId;
    @FXML
    public Label studentFirstName;
    @FXML
    public ListView courseList;
    @FXML
    private ListView enlist;
    @FXML
    public Label messages;
    @FXML
    public Label teacherId;
    @FXML
    public Label studentLastName;
    @FXML
    public Label courses;
    @FXML
    public Label credits;
    @FXML
    public TextField teacherIdField;
    @FXML
    public ListView courseEnlist;
    @FXML
    private Label teacherFirstName;
    @FXML
    private Label teacherLastName;
    @FXML
    public Label students;
    private TeacherJdbcRepository teacherJdbcRepository;
    private StudentJdbcRepository studentJdbcRepository;
    private PersonJdbcRepository personJdbcRepository;
    private CourseJdbcRepository courseJdbcRepository;
    /**
     * Constructor
     *
     * @throws Exception if file i/o fails
     */
    public RegistrationSystem() throws Exception {



        this.teacherJdbcRepository=new TeacherJdbcRepository();
        this.studentJdbcRepository=new StudentJdbcRepository();
        this.courseJdbcRepository=new CourseJdbcRepository();
        this.personJdbcRepository=new PersonJdbcRepository();
    }

    /**
     * Creates and inserts a new course
     *
     * @param courseId      course ID
     * @param name          name
     * @param maxEnrollment maximum enrollment
     * @param credits       credits
     * @throws Exception if file i/o fails
     */
    public Course addCourse(long courseId, String name, int maxEnrollment, int credits) throws Exception {
        if (courseId >= 0) {
            Course course = new Course(courseId, name, maxEnrollment, credits);
            if (this.courseJdbcRepository.save(course) == course) {
                return course;
               // throw new IDInUseException("Id is already in use.");
            }
        } else
            throw new IDInvalidException("Invalid id.");
        return null;
    }

    /**
     * Creates and inserts a new student
     *
     * @param studentId student ID
     * @param firstName first name
     * @param lastName  last name
     * @throws Exception if file i/o fails
     */
    public void addStudent(long studentId, String firstName, String lastName) throws Exception {
        if (studentId >= 0) {
            Student student = new Student(studentId, firstName, lastName);
            if (this.studentJdbcRepository.save(student) == student)
                throw new IDInUseException("Id is already in use.");
        } else
            throw new IDInvalidException("Invalid id.");
    }

    /**
     * Creates and inserts a new teacher
     *
     * @param teacherId teacher ID
     * @param firstName first name
     * @param lastName  last name
     * @throws Exception if file i/o fails
     */
    public void addTeacher(long teacherId, String firstName, String lastName) throws Exception {
        if (teacherId >= 0) {
            Teacher teacher = new Teacher(teacherId, firstName, lastName);
            if (this.teacherJdbcRepository.save(teacher) == teacher)
                throw new IDInUseException("Id is already in use.");
        } else
            throw new IDInvalidException("Invalid id.");
    }

    /**
     * Displays all courses
     */
    public List<String> showAllCourses() {
        List<String> courses = new ArrayList<>();
        for (Object o : courseJdbcRepository.findAll()) {
            Course course = (Course) o;
            if (course.getMaxEnrollment() - course.getStudentsEnrolled().size() < 0)
                throw new InvalidDataException("Invalid number of enrolled students.");
            courses.add(course.getCourseId() + " : " + course.getName()
                    + " | Credits: " + course.getCredits()
                    + " | Maximum enrollment: " + course.getMaxEnrollment()
                    + " | Available places: " + (course.getMaxEnrollment() - course.getStudentsEnrolled().size()));
        }
        return courses;
    }

    /**
     * Displays courses that are not fully enrolled
     */
    public List<String> showAvailableCourses() {
        List<String> courses = new ArrayList<>();
        for (Object o : courseJdbcRepository.findAll()) {
            Course course = (Course) o;
            if (course.getMaxEnrollment() - course.getStudentsEnrolled().size() < 0)
                throw new InvalidDataException("Invalid number of enrolled students.");
            if (course.getMaxEnrollment() - course.getStudentsEnrolled().size() > 0)
                courses.add(course.getCourseId() + " : " + course.getName()
                        + " | Credits: " + course.getCredits()
                        + " | Maximum enrollment: " + course.getMaxEnrollment()
                        + " | Available places: " + (course.getMaxEnrollment() - course.getStudentsEnrolled().size()));
        }
        return courses;
    }

    /**
     * Displays all teachers
     */
    public List<String> showAllTeachers() {
        List<String> teachers = new ArrayList<>();
        for (Object o : teacherJdbcRepository.findAll()) {
            Teacher teacher = (Teacher) o;
            teachers.add(teacher.getTeacherId() + " : " + teacher.getFirstName() + " " + teacher.getLastName());
        }
        return teachers;
    }

    /**
     * Displays all students
     */
    public List<String> showAllStudents() {
        List<String> students = new ArrayList<>();
        for (Object o : studentJdbcRepository.findAll()) {
            Student student = (Student) o;
            students.add(student.getStudentId() + " : "
                    + student.getFirstName() + " "
                    + student.getLastName() + " | Credits: "
                    + student.getTotalCredits());
        }
        return students;
    }

    /**
     * Displays all students enrolled in a certain course
     *
     * @param courseId course ID
     */
    public List<String> showEnrolledStudents(long courseId) {
        List<String> students = new ArrayList<>();
        Course course = courseJdbcRepository.findOne(courseId);
        if (course != null) {
            for (long studentId : course.getStudentsEnrolled()) {
                if (studentJdbcRepository.findOne(studentId) != null)
                    students.add((studentJdbcRepository.findOne(studentId)).getFirstName() + " "
                            + (studentJdbcRepository.findOne(studentId)).getLastName());
            }
        }
        return students;
    }

    public List<String> showSortedStudents() {
        List<String> students = new ArrayList<>();
        for (Object o : studentJdbcRepository.findAll()) {
            Student student = (Student) o;
            students.add(student.getStudentId() + " : "
                    + student.getFirstName() + " "
                    + student.getLastName() + " | Credits: "
                    + student.getTotalCredits());
        }

        students.sort((o1, o2) -> {
            String[] split1 = o1.split(": ");
            String[] split2 = o2.split(": ");
            int credits1 = Integer.parseInt(split1[split1.length - 1]);
            int credits2 = Integer.parseInt(split2[split2.length - 1]);
            return Integer.compare(credits2, credits1);
        });

        return students;
    }

    /**
     * Assigns a teacher to a course
     *
     * @param teacherId teacher ID
     * @param courseId  course ID
     * @throws Exception if file i/o fails
     */
    public void assignTeacherToCourse(long teacherId, long courseId) throws Exception {
        Teacher teacher = teacherJdbcRepository.findOne(teacherId);
        Course course = courseJdbcRepository.findOne(courseId);
        if (teacher != null && course != null) {
            if (teacher.getCourses().contains(courseId))
                throw new AssignationException("This course is already being taught by this teacher.");
            else if (course.getTeacher() != -1)
                throw new AssignationException("This course is already being taught by another teacher.");
            else {
                course.setTeacher((int) teacherId);
                List<Long> courses = teacher.getCourses();
                courses.add(courseId);
                teacher.setCourses(courses);

                this.teacherJdbcRepository.update(teacher);
                this.courseJdbcRepository.update(course);
            }

        } else {
            throw new IDInvalidException("Invalid course or teacher ID.");
        }
    }

    /**
     * Removes a teacher from a course
     *
     * @param courseId course ID
     * @throws Exception if file i/o fails
     */
    public void removeTeacherFromCourse(long courseId) throws Exception {
        Course course = courseJdbcRepository.findOne(courseId);
        System.out.println(course.getMaxEnrollment());




        if (course != null) {
            long teacherId = course.getTeacher();
            Teacher teacher = teacherJdbcRepository.findOne(teacherId);
            if (teacher != null) {
                List<Long> courses = teacher.getCourses();
                courses.remove(courseId);
                teacher.setCourses(courses);
                teacherJdbcRepository.update(teacher);
            } else {
                throw new AssignationException("No teacher is assigned to this course");
            }
            course.setTeacher(-1);
            courseJdbcRepository.update(course);
        }
        else {
            throw new IDInvalidException("Invalid course ID.");
       }
    }

    /**
     * Assigns a student to a course
     *
     * @param studentId student ID
     * @param courseId  course ID
     * @throws Exception if file i/o fails
     */
    public void assignStudentToCourse(long studentId, long courseId) throws Exception {
        Student student = studentJdbcRepository.findOne(studentId);
        Course course = courseJdbcRepository.findOne(courseId);
        if (student != null && course != null) {
            if (student.getEnrolledCourses().contains(courseId) || course.getStudentsEnrolled().contains(studentId))
                this.messages.setText("This student is already enrolled in this course.");
               // throw new AssignationException("This student is already enrolled in this course.");
            else if (course.getCredits() + student.getTotalCredits() > 30)
                this.messages.setText("The total credit count is too high.");
               // throw new AssignationException("The total credit count is too high.");
            else if (course.getMaxEnrollment() - course.getStudentsEnrolled().size() == 0)
                this.messages.setText("This course is already fully enrolled.");
               // throw new AssignationException("This course is already fully enrolled.");
            else {
                List<Long> courses = student.getEnrolledCourses();
                courses.add(courseId);
                student.setEnrolledCourses(courses);

                List<Long> students = course.getStudentsEnrolled();
                students.add(studentId);
                course.setStudentsEnrolled(students);

                student.setTotalCredits(student.getTotalCredits() + course.getCredits());

                this.studentJdbcRepository.update(student);
                this.courseJdbcRepository.update(course);
                this.credits.setText(student.getTotalCredits()+"");
            }

        } else {
            throw new IDInvalidException("Invalid course or student ID.");
        }
    }

    /**
     * Removes a student from a course
     *
     * @param studentId student ID
     * @param courseId  course ID
     * @throws Exception if file i/o fails
     */
    public void removeStudentFromCourse(long studentId, long courseId) throws Exception {
        Student student = studentJdbcRepository.findOne(studentId);
        Course course = courseJdbcRepository.findOne(courseId);
        if (student != null && course != null) {
            if (!student.getEnrolledCourses().contains(courseId))
                throw new AssignationException("This student is not enrolled in this course.");
            else {
                List<Long> courses = student.getEnrolledCourses();
                courses.remove(courseId);
                student.setEnrolledCourses(courses);

                List<Long> students = course.getStudentsEnrolled();
                students.remove(studentId);
                course.setStudentsEnrolled(students);

                student.setTotalCredits(student.getTotalCredits() - course.getCredits());

                this.studentJdbcRepository.update(student);
                this.courseJdbcRepository.update(course);
            }

        } else {
            throw new IDInvalidException("Invalid course or student ID.");
        }
    }

    /**
     * Changes a course's credits
     *
     * @param courseId course ID
     * @param credits  new credit count
     * @throws Exception if file i/o fails
     */
    public void changeCredits(long courseId, int credits) throws Exception {
        if (credits >= 0) {
            Course course = courseJdbcRepository.findOne(courseId);
            if (course != null) {

                int delta = credits - course.getCredits();
                int numberOfStudents = course.getStudentsEnrolled().size();
                Long[] studentsEnrolledArray = new Long[numberOfStudents];
                studentsEnrolledArray = course.getStudentsEnrolled().toArray(studentsEnrolledArray);

                for (long studentId : course.getStudentsEnrolled()) {
                    Student student = studentJdbcRepository.findOne(studentId);
                    if (student == null)
                        throw new InvalidDataException("Student with invalid ID enrolled in course.");
                    else {
                        student.setTotalCredits(student.getTotalCredits() + delta);
                        if (student.getTotalCredits() > 30) {
                            student.setTotalCredits(student.getTotalCredits() - credits);
                            List<Long> enrolledCourses = student.getEnrolledCourses();
                            enrolledCourses.remove(courseId);
                            student.setEnrolledCourses(enrolledCourses);

                            for (int i = 0; i < numberOfStudents; i++) {
                                if (studentsEnrolledArray[i] == studentId) {
                                    if (numberOfStudents - 1 - i >= 0)
                                        System.arraycopy(studentsEnrolledArray, i + 1, studentsEnrolledArray, i, numberOfStudents - 1 - i);
                                    numberOfStudents--;
                                }
                            }
                        }
                        studentJdbcRepository.update(student);
                    }
                }

                List<Long> studentsEnrolled = new ArrayList<>(Arrays.asList(studentsEnrolledArray).subList(0, numberOfStudents));

                course.setStudentsEnrolled(studentsEnrolled);
                course.setCredits(credits);
                courseJdbcRepository.update(course);
            } else {
                throw new IDInvalidException("Invalid course ID.");
            }
        } else {
            throw new InvalidDataException("Negative credit count is not permitted.");
        }
    }

    /**
     * Deletes a course
     *
     * @param courseId course ID
     * @throws Exception if file i/o fails
     */
    public void deleteCourse(long courseId) throws Exception {
        //Course course=courseJdbcRepository.delete(courseId);


        Course course = courseJdbcRepository.findOne(courseId);
        if (course != null) {
            for (long studentId : course.getStudentsEnrolled()) {
                Student student = studentJdbcRepository.findOne(studentId);
                if (student == null)
                    throw new InvalidDataException("Student with invalid ID enrolled in course.");
                else {
                    List<Long> enrolledCourses = student.getEnrolledCourses();
                    enrolledCourses.remove(courseId);
                    student.setEnrolledCourses(enrolledCourses);
                    student.setTotalCredits(student.getTotalCredits() - course.getCredits());
                }
                studentJdbcRepository.update(student);
            }
            courseJdbcRepository.delete(courseId);
        } else {
            throw new IDInvalidException("Invalid course ID.");
        }
    }

    /**
     * Deletes a teacher
     *
     * @param teacherId teacher ID
     * @throws Exception if file i/o fails
     */
    public void deleteTeacher(long teacherId) throws Exception {
        Teacher teacher = teacherJdbcRepository.findOne(teacherId);
        if (teacher != null) {
            for (long courseId : teacher.getCourses()) {
                Course course = courseJdbcRepository.findOne(courseId);
                if (course != null) {
                    course.setTeacher(-1);
                    courseJdbcRepository.update(course);
                } else {
                    throw new InvalidDataException("Teacher assigned to non-existent course.");
                }
            }
            teacherJdbcRepository.delete(teacherId);
        } else {
            throw new IDInvalidException("Invalid teacher ID.");
        }
    }

    /**
     * Deletes a student
     *
     * @param studentId student ID
     * @throws Exception if file i/o fails
     */
    public void deleteStudent(long studentId) throws Exception {
        Student student = studentJdbcRepository.findOne(studentId);
        if (student != null) {
            for (long courseId : student.getEnrolledCourses()) {
                Course course = courseJdbcRepository.findOne(courseId);
                if (course != null) {
                    List<Long> studentsEnrolled = course.getStudentsEnrolled();
                    studentsEnrolled.remove(studentId);
                    course.setStudentsEnrolled(studentsEnrolled);
                    courseJdbcRepository.update(course);
                } else {
                    throw new InvalidDataException("Student assigned to non-existent course.");
                }
            }
            studentJdbcRepository.delete(studentId);
        } else {
            throw new IDInvalidException("Invalid student ID.");
        }
    }


    public void studentBtt(ActionEvent actionEvent) throws IOException {
       // this.studentIdField.setText("43");

        List<Student> students=new ArrayList<Student>();
        for (Object o : studentJdbcRepository.findAll()){
            Student student = (Student) o;
            students.add(student);
        }


        for (Student st:students
             ) {
            if(st.getLastName().equals(this.studentIdField.getText().split(" ")[0]) && st.getFirstName().equals(this.studentIdField.getText().split(" ")[1])) {

/*
                mainPane.getChildren().clear();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("studentWindow.fxml"));
                loader.setController(this);
                // loader.setController(this);
                mainPane.getChildren().setAll((Node) loader.load());

 */
                FXMLLoader loader = new FXMLLoader(getClass().getResource("studentWindow.fxml"));
                loader.setController(this);
                Parent root = (Parent) loader.load();
                Stage stage=new Stage();
                stage.setScene(new Scene(root,1000,500));
                stage.show();
                List<String> courseList = new ArrayList<String>();
                for (Object o : courseJdbcRepository.findAll()) {
                    Course course = (Course) o;
                    this.enlist.getItems().add(course.getCourseId() + "." + course.getName());
                }



                this.studentFirstName.setText(st.getFirstName());
                this.studentLastName.setText(st.getLastName());
                this.studentId.setText(st.getStudentId()+"");
                this.credits.setText(st.getTotalCredits()+"");


            }
      }
    }

    public void enlist(ActionEvent actionEvent) throws Exception {
        List<Course> courses=new ArrayList<Course>();
        for (Object o : courseJdbcRepository.findAll()){
            Course course = (Course) o;
            courses.add(course);
        }
        try {
            String selected= (String) this.enlist.getSelectionModel().getSelectedItem();
                this.assignStudentToCourse(Long.parseLong(this.studentId.getText()),Long.parseLong(selected.split("\\.")[0]));
                try {
                this.showStudentsBtt(null);
            }
            catch(Exception ex){}
        } catch (AssignationException | IDInvalidException ex) {
            System.out.println(ex.getMessage());
        }

    }


    public void teacherBtt(ActionEvent actionEvent) throws IOException {

        List<Teacher> teachers=new ArrayList<Teacher>();
        for (Object o : teacherJdbcRepository.findAll()){
            Teacher teacher = (Teacher) o;
            teachers.add(teacher);
        }


        for (Teacher st:teachers
        ) {
            if(st.getLastName().equals(this.teacherIdField.getText().split(" ")[0]) && st.getFirstName().equals(this.teacherIdField.getText().split(" ")[1])) {

              // Parent root;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("teacherWindow.fxml"));
                loader.setController(this);
                Parent root = (Parent) loader.load();
               Stage stage=new Stage();
               stage.setScene(new Scene(root,1000,500));
               stage.show();

                Teacher teacher=this.teacherJdbcRepository.findOne(st.getTeacherId());
                List<String> courseList = new ArrayList<String>();
                for (Object o : teacher.getCourses()) {
                    Course course = courseJdbcRepository.findOne((Long) o);
                    this.courseList.getItems().add(course.getCourseId() + "." + course.getName());
                }



                this.teacherFirstName.setText(st.getFirstName());
                this.teacherLastName.setText(st.getLastName());
                this.teacherId.setText(st.getTeacherId()+"");
                //this.credits.setText(st.getTotalCredits()+"");


            }
        }
    }

    public void showStudentsBtt(ActionEvent actionEvent){
        try {
            String toLabel = "";
            String selected= (String) this.courseList.getSelectionModel().getSelectedItem();
            List<String> students=this.showEnrolledStudents(Long.parseLong(selected.split("\\.")[0]));
            for (String st:students
                 ) {
                toLabel+=st+" || ";
            }
            this.students.setText(toLabel);
        } catch (AssignationException | IDInvalidException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

