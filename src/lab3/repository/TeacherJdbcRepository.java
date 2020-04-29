package lab3.repository;

import lab3.model.Course;
import lab3.model.Teacher;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherJdbcRepository implements ICrudRepository<Teacher> {

    private Connection con;
    private Statement st;
    private ResultSet rs;

    public TeacherJdbcRepository(){
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
    public Teacher findOne(Long id) {
        List<Long> courseList=new ArrayList<Long>();
        Teacher teacher=new Teacher();
        int age=0;
        try{
            String querry="select * from teacher where teacherId="+id.toString();
            rs=st.executeQuery(querry);
            while(rs.next()){
               teacher= new Teacher(rs.getInt("teacherId"),rs.getString("firstName"),rs.getString("lastName"));
            }
            querry="select * from teachercourse where teacherId="+id.toString();
            rs=st.executeQuery(querry);
            while(rs.next())
                courseList.add(Long.valueOf(rs.getString("courseId")));
            teacher.setCourses(courseList);
            return teacher;

        }catch (Exception ex){
            System.out.println("Find one in Teacher: "+ex);
        }
        return null;
    }

    @Override
    public Iterable<Teacher> findAll() {

        List<Teacher> teacher = new ArrayList<>();
        try{
            String querry="select * from teacher";
            rs=st.executeQuery(querry);
            while(rs.next()){
                teacher.add(new Teacher(rs.getInt("teacherId"),rs.getString("firstName"),rs.getString("lastName")));
            }

        }catch (Exception ex){
            System.out.println("Find all: "+ex);
        }
        return teacher;
    }

    @Override
    public Teacher save(Teacher entity) {
        try{
            try{
                String querry="select * from teacher where teacherId="+entity.getTeacherId();
                rs=st.executeQuery(querry);
                while(rs.next()) {
                    int id = rs.getInt("teacherId");
                    if(id==entity.getTeacherId())
                        return entity;
                }
                querry="INSERT INTO teacher (`teacherId`, `firstName`, `lastName`) VALUES ('"+entity.getTeacherId() +"', '"+entity.getFirstName()+
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
    public Teacher delete(Long id) {
        try{
            String querry="select * from teacher where teacherId="+id.toString();
            rs=st.executeQuery(querry);
            while(rs.next()){
                int courseId=rs.getInt("teacherId");
                if(id.intValue()==courseId){
                    Teacher teacher=new Teacher(rs.getInt("teacherId"),rs.getString("firstName"),rs.getString("lastName"));
                    querry="delete from teacher where teacherId="+id.toString();
                    st.executeUpdate(querry);
                    return teacher;
                }

            }

        }catch (Exception ex){
            System.out.println("Delete: "+ex);
        }
        return null;
    }

    @Override
    public Teacher update(Teacher entity){
        try{
            String querry="select * from teacher where teacherId="+entity.getTeacherId();
            rs=st.executeQuery(querry);
            while(rs.next()) {
                int id = rs.getInt("teacherId");
                if(id==entity.getTeacherId()) {
                    querry = "update teacher set " +
                            "teacherId='" + entity.getTeacherId() + "', " +
                            "firstName='" + entity.getFirstName() +
                            "', lastName='" + entity.getLastName() +"' where teacherId="+entity.getTeacherId()+";";
                    st.executeUpdate(querry);
                    querry="delete from teachercourse where teacherId="+entity.getTeacherId();
                    st.executeUpdate(querry);
                    String querry2;
                    for(long crs:entity.getCourses()) {
                        querry2 = "insert into teachercourse(teacherId,courseId) values(" +
                                entity.getTeacherId() + ", " + crs + ");";
                        st.executeUpdate(querry2);
                    }
                    return null;
                }


            }}catch (Exception ex){
            System.out.println("Update: "+ex);
        }
        return entity;
    }
}
