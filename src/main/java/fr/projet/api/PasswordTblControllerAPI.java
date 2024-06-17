package fr.projet.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import fr.projet.repo.PasswordTblRepository;

@RestController
@RequestMapping("/api/password")
@CrossOrigin("*")
public class PasswordTblControllerAPI {

    private static final Logger log = LoggerFactory.getLogger(PasswordTblControllerAPI.class);

    //@Autowired
    //private PasswordTblRepository repo  ;

    @GetMapping("/{password}")
    public Boolean getPasswordById(@PathVariable String  password ) {
        boolean bool = Boolean.FALSE ;
        String id ="";
        System.out.println(password);
        try (Connection connection = 
        DriverManager.getConnection("jdbc:clickhouse://127.0.0.1:8123/passworddb", "default", "")) {
     

            String sql = "select  id  from passwordtbl where id= (?) LIMIT 2";
            PreparedStatement statement = connection.prepareStatement(sql);
        
            statement.setString(1, password);
           
            statement.executeQuery();
            ResultSet rs = statement.getResultSet();
            
            System.out.println("detail");
            //System.out.println(rs.first());
            
            while(rs.next()) {
                id = rs.getString("id");
                System.out.println(id);
                System.out.println(password);
                if (id.equals(password) ){  return Boolean.TRUE; } 
               

            }
            
            return Boolean.FALSE; 
         
        } 
        catch (Exception ex) {
            ex.printStackTrace();
            log.error("Impossible de se connecter ...");
            System.out.println("exception");
            return bool;
        }
        
    }

      @PostMapping("/{password}")
    public void create( @PathVariable("password") String password) {
     
      try (Connection connection = 
      DriverManager.getConnection("jdbc:clickhouse://127.0.0.1:8123/passworddb", "default", "")) {
      connection.setAutoCommit(true);

      try (PreparedStatement statement = connection.prepareStatement("INSERT INTO passwordtbl  VALUES (?)")) {
         
              statement.setString(1, password);

              statement.execute();
              connection.commit();
           
          }
      }
          
      //}
      catch (Exception ex) {
          ex.printStackTrace();
          log.error("Impossible de se connecter ...");
      }

    }
}
