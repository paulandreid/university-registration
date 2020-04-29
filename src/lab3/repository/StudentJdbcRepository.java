package lab3.repository;

import lab3.model.Course;
import lab3.model.Student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentJdbcRepository implements ICrudRepository<Student>{

    private Connection con;
    private Statement st;
    private ResultSet rs;

    public StudentJdbcRepository(){
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
    public Student findOne(Long id) {
        String returnResultOfQuerry = null;
        List<Long> courseList=new ArrayList<Long>();
        Student student=new Student();
        try{
            String querry="select * from student where studentId="+id.toString();
            rs=st.executeQuery(querry);
            while(rs.next()){
                student=new Student(rs.getInt("studentId"),rs.getString("firstName"),rs.getString("lastName"));
                student.setTotalCredits(rs.getInt("totalCredits"));
            }
            querry="select * from studentsenrolled where studentId="+id.toString();
            rs=st.executeQuery(querry);
            while(rs.next()){
                courseList.add(Long.valueOf(rs.getString("courseId")));
            }
            student.setEnrolledCourses(courseList);
            return student;

        }catch (Exception ex){
            System.out.println("Find one in Student: "+ex);
        }
        return null;
    }

    @Override
    public Iterable<Student> findAll() {

        List<Student> student = new ArrayList<>();
        List<Long> courseList=new ArrayList<Long>();
        try{
            String querry="select * from student";
            rs=st.executeQuery(querry);
            while(rs.next()){

                Student studentNew =new Student(rs.getInt("studentId"),rs.getString("firstName"),rs.getString("lastName"));
                studentNew.setTotalCredits(rs.getInt("totalCredits"));
                Statement st2=con.createStatement();
                ResultSet rs2;
                String querry2="select * from studentsenrolled where studentId="+studentNew.getStudentId();


                rs2=st2.executeQuery(querry2);

                while(rs2.next()){
                    courseList.add(Long.valueOf(rs2.getInt("courseId")));

                }
                studentNew.setEnrolledCourses(courseList);


                student.add(studentNew);
            }


        }catch (Exception ex){
            System.out.println("Find all: "+ex);
        }
        return student;
    }

    @Override
    public Student save(Student entity) {
        try{
            try{
                String querry="select * from student where studentId="+entity.getStudentId();
                rs=st.executeQuery(querry);
                while(rs.next()) {
                    int id = rs.getInt("studentId");
                    if(id==entity.getStudentId())
                        return entity;
                }
                querry="INSERT INTO student (`studentId`, `firstName`, `lastName`) VALUES ('"+entity.getStudentId() +"', '"+entity.getFirstName()+
                        "', '"+entity.getLastName()+"');";
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
    public Student delete(Long id) {
        try{
            String querry="select * from student where studentId="+id.toString();
            rs=st.executeQuery(querry);
            while(rs.next()){
                int studentId=rs.getInt("studentId");
                if(id.intValue()==studentId){
                    Student student=new Student(rs.getInt("studentId"),rs.getString("firstName"),rs.getString("lastName"));
                    querry="delete from student where studentId="+id.toString();
                    st.executeUpdate(querry);
                    return student;
                }

            }

        }catch (Exception ex){
            System.out.println("Delete: "+ex);
        }
        return null;
    }

    @Override
    public Student update(Student entity){
        try{
            String querry="select * from student where studentId="+entity.getStudentId();
            rs=st.executeQuery(querry);
            while(rs.next()) {
                int id = rs.getInt("studentId");
                if(id==entity.getStudentId()) {
                    querry = "update student set " +
                            "studentId='" + entity.getStudentId() + "', " +
                            "firstName='" + entity.getFirstName() +
                            "', lastName='" + entity.getLastName() +
                            "', totalCredits="+entity.getTotalCredits()+" where studentId="+entity.getStudentId()+";";
                    st.executeUpdate(querry);
                    querry="delete from studentsenrolled where studentId="+entity.getStudentId();
                    st.executeUpdate(querry);
                    String querry2;
                    for(long crs:entity.getEnrolledCourses()) {
                        querry2 = "insert into studentsenrolled(studentId,courseId) values(" +
                                entity.getStudentId() + ", " + crs + ");";
                        st.executeUpdate(querry2);
                    }
                    return null;
                }

            }}catch (Exception ex){
            System.out.println("Update in student: "+ex);
        }
        return entity;
    }
}
