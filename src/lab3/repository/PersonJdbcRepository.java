package lab3.repository;

import lab3.model.*;
import lab3.repository.ICrudRepository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonJdbcRepository {

    private Connection con;
    private Statement st;
    private ResultSet rs;

}
