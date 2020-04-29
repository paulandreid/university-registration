package lab3.repository;
import lab3.model.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseJdbcRepository implements ICrudRepository<Course> {

    private Connection con;
    private Statement st;
    private ResultSet rs;

    public CourseJdbcRepository(){
        try{
            Class.forName("com.mysql.jdbc.Driver");

            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/lab6","root","");
            st=con.createStatement();

        }catch (Exception ex){
            System.out.println("error "+ ex);
        }
    }

    public String getData(){
        try{
            String querry="select * from student";
            rs=st.executeQuery(querry);
            while(rs.next()){
                int age=rs.getInt("age");
                System.out.println(age);
            }

        }catch (Exception ex){
            System.out.println(ex);
        }
        return "red";
    }

    @Override
    public Course findOne(Long id) {
        String returnResultOfQuerry = null;
        List<Long> studentsEnrolled=new ArrayList<Long>();
        Course course=new Course();
        try{
            String querry="select * from course where courseId="+id.toString();
            rs=st.executeQuery(querry);
            while(rs.next()){
                course=new Course(((long) rs.getInt("courseId")),rs.getString("name"),rs.getInt("maxEnrollment"),rs.getInt("Credits"));
            }
            querry="select * from studentsenrolled where courseId="+id.toString();
            rs=st.executeQuery(querry);
            while(rs.next())
                studentsEnrolled.add(Long.valueOf(rs.getString("studentId")));
            course.setStudentsEnrolled(studentsEnrolled);
            return course;

        }catch (Exception ex){
            System.out.println("Find one in Course: "+ex);
        }
        return null;
    }

    @Override
    public Iterable<Course> findAll() {

        List<Course> courses = new ArrayList<>();
        try{
            String querry="select * from course";
            rs=st.executeQuery(querry);
            while(rs.next()){
                courses.add(new Course(((long) rs.getInt("courseId")),rs.getString("name"),rs.getInt("maxEnrollment"),rs.getInt("Credits")));
            }

        }catch (Exception ex){
            System.out.println("Find all: "+ex);
        }
        return courses;
    }

    @Override
    public Course save(Course entity) {
       try{
           try{
               String querry="select * from course where courseId="+entity.getCourseId();
               rs=st.executeQuery(querry);
               while(rs.next()) {
                   int id = rs.getInt("courseId");
                   if(id==entity.getCourseId())
                       return entity;
               }
               querry="INSERT INTO `course` (`courseId`, `name`, `maxEnrollment`, `credits`, `teacher`) VALUES ('"+entity.getCourseId() +"', '"+entity.getName()+
                       "', '"+entity.getMaxEnrollment()+
                       "', '"+entity.getCredits()+
                       "', '"+entity.getTeacher()+"');";
               st.executeUpdate(querry);


           }catch (Exception ex){
               System.out.println(ex);
           }


       }catch(Exception ex){
           System.out.println("Eroare la salvare: "+ex);
       }
       return null;
    }

    @Override
    public Course delete(Long id) {
        try{
            String querry="select * from course where courseId="+id.toString();
            rs=st.executeQuery(querry);
            while(rs.next()){
                int courseId=rs.getInt("courseId");
                if(id.intValue()==courseId){
                    Course course=new Course(courseId,rs.getString("name"),rs.getInt("maxEnrollment"),rs.getInt("credits"));
                    querry="delete from course where courseId="+id.toString();
                    st.executeUpdate(querry);
                    return course;
                }

            }

        }catch (Exception ex){
            System.out.println("Delete: "+ex);
        }
        return null;
    }

    @Override
    public Course update(Course entity){
        try{
            String querry="select * from course where courseId="+entity.getCourseId();
            rs=st.executeQuery(querry);
            while(rs.next()) {
                int id = rs.getInt("courseId");
                if(id==entity.getCourseId()) {
                    querry = "update course set " +
                            "courseId='" + entity.getCourseId() + "', " +
                            "name='" + entity.getName() +
                            "', maxEnrollment='" + entity.getMaxEnrollment() +
                            "', credits='" + entity.getCredits() +
                            "', teacher='" + entity.getTeacher() + "' where courseId="+entity.getCourseId()+";";
                    st.executeUpdate(querry);
                    querry="delete from studentsenrolled where courseId="+entity.getCourseId();
                    st.executeUpdate(querry);
                    String querry2;
                    for(long crs:entity.getStudentsEnrolled()) {
                        querry2 = "insert into studentsenrolled(courseId,studentId) values(" +
                                entity.getCourseId() + ", " + crs + ");";
                        st.executeUpdate(querry2);
                    }
                    return null;
                }

                }}catch (Exception ex){
            System.out.println("Update in course: "+ex);
        }
        return entity;
    }
}
