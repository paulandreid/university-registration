package lab3.controller;
import lab3.model.Course;
import org.junit.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.Test;
import sun.management.snmp.jvmmib.JVM_MANAGEMENT_MIBOidTable;

public class ControllerTest {



    @Test
    public void testShowAllCourses() {

        try {
            RegistrationSystem controller = new RegistrationSystem();
            Scanner scanner = new Scanner("files/courses.txt");
            List<String> courses = new ArrayList<>();

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

                courses.add(course.getCourseId() + " : " + course.getName()
                        + " | Credits: " + course.getCredits()
                        + " | Maximum enrollment: " + course.getMaxEnrollment()
                        + " | Available places: " + (course.getMaxEnrollment() - course.getStudentsEnrolled().size()));
            }
            scanner.close();
            Assert.assertEquals(controller.showAllCourses(), courses);// show all courses test
        } catch (Exception ignored) {
        }
    }
    @Test
    public void testAvailableCourses(){
        try {
            RegistrationSystem controller = new RegistrationSystem();
            Scanner scanner = new Scanner("files/courses.txt");
            List<String> courses = new ArrayList<>();
            controller.addCourse(100,"MAP",1,3);
            controller.addStudent(101,"S1","S2");
            controller.assignStudentToCourse(101,100);

            while (scanner.hasNextLine()) {
                String[] courseData = scanner.nextLine().split(";");

                Course course = new Course(Long.parseLong(courseData[0]), courseData[1], Integer.parseInt(courseData[2]), Integer.parseInt(courseData[3]));
                if(courseData[2]==courseData[5])
                    continue;
                if (courseData[5].compareTo("-") != 0) {
                    String[] studentData = courseData[5].split(",");
                    List<Long> studentsEnrolled = new ArrayList<>();
                    for (String student : studentData) {
                        studentsEnrolled.add(Long.parseLong(student));
                    }
                    course.setStudentsEnrolled(studentsEnrolled);
                }

                courses.add(course.getCourseId() + " : " + course.getName()
                        + " | Credits: " + course.getCredits()
                        + " | Maximum enrollment: " + course.getMaxEnrollment()
                        + " | Available places: " + (course.getMaxEnrollment() - course.getStudentsEnrolled().size()));
            }
            scanner.close();
            Assert.assertEquals(controller.showAllCourses().contains("MAP"), "MAP");// show all courses test
        } catch (Exception ignored) {  }


    }
    @Test
    public void testCourse(){
        try {
            RegistrationSystem controller = new RegistrationSystem();
            Scanner scanner = new Scanner("files/courses.txt");
            List<String> courses = new ArrayList<>();
            controller.deleteCourse(100);
            controller.deleteStudent(101);

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

                courses.add(course.getCourseId() + " : " + course.getName()
                        + " | Credits: " + course.getCredits()
                        + " | Maximum enrollment: " + course.getMaxEnrollment()
                        + " | Available places: " + (course.getMaxEnrollment() - course.getStudentsEnrolled().size()));
            }
            scanner.close();
            Assert.assertEquals(controller.showAllCourses(), courses);// show all courses test
        } catch (Exception ignored) {
        }
    }
}








































/**
 try{
 //addCourse
 RegistrationSystem controller = new RegistrationSystem();
 // controller.addCourse(100,"MAP",100,3);
 // Scanner scanner2 = new Scanner("files/courses.txt");
 //  Paths path="files";
 try {
 List<String> lines = Files.readAllLines(Paths.get("files/courses", "courses.txt"));

 boolean cointains = false;
 if (lines.contains("MAP"))
 cointains = true;

 Assert.assertTrue(cointains);

 controller.deleteCourse(100);
 lines= Files.readAllLines(Paths.get("files/courses","courses.txt"));
 cointains=false;
 if(lines.contains("MAP"))
 cointains=true;
 Assert.assertFalse(cointains);

 }catch (IOException ex){System.out.println(4444);}

 //deleteCourse

 // scanner2.close();

 //showAvailableCourses

 /**  try {
 controller.addCourse(100,"MAP",1,3);
 controller.addStudent(101,"t1","t2");
 controller.assignStudentToCourse(101,100);

 Scanner scanner = new Scanner("files/courses.txt");
 List<String> courses = new ArrayList<>();

 while (scanner.hasNextLine()) {
 String[] courseData = scanner.nextLine().split(";");

 Course course = new Course(Long.parseLong(courseData[0]), courseData[1], Integer.parseInt(courseData[2]), Integer.parseInt(courseData[3]));
 course.setTeacher(Long.parseLong(courseData[4]));

 if(courseData[2]==courseData[5])
 continue;

 if (courseData[5].compareTo("-") != 0) {
 String[] studentData = courseData[5].split(",");
 List<Long> studentsEnrolled = new ArrayList<>();
 for (String student : studentData) {
 studentsEnrolled.add(Long.parseLong(student));
 }
 course.setStudentsEnrolled(studentsEnrolled);
 }

 courses.add(course.getCourseId() + " : " + course.getName()
 + " | Credits: " + course.getCredits()
 + " | Maximum enrollment: " + course.getMaxEnrollment()
 + " | Available places: " + (course.getMaxEnrollment() - course.getStudentsEnrolled().size()));
 }
 scanner.close();
 Assert.assertEquals(controller.showAvailableCourses(), courses);// show all courses test
 } catch (Exception ignored) { }

 controller.deleteCourse(100);
 controller.deleteStudent(101);
 */
