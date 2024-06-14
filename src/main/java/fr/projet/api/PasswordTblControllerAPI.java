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
    public boolean getPasswordById(@PathVariable String  password ) {

        try (Connection connection = 
        DriverManager.getConnection("jdbc:clickhouse://127.0.0.1:8123/passworddb", "default", "")) {
     

            String sql = "select  id  from passwordtbl where id= (?)";
            PreparedStatement statement = connection.prepareStatement(sql);
        
            statement.setString(1, password);
           
            statement.executeQuery();
            ResultSet rs = statement.getResultSet();
            boolean bool = false ;
            
            while(rs.next()) {
                String id = rs.getString("id");
                if (id == password){ bool = true; } 
                else {bool =  false;}

            }
            
            if (bool){return true; } else {return false;}
         
        } 
        catch (Exception ex) {
            ex.printStackTrace();
            log.error("Impossible de se connecter ...");
            return false;
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
