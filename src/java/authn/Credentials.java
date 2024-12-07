package authn;
import java.io.Serializable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@NamedQueries({
    @NamedQuery(name="Credentials.findUser", 
                query="SELECT c FROM Credentials c WHERE c.username = :username"),
    @NamedQuery(name="Credentials.findId", 
                query="SELECT c.id FROM Credentials c WHERE c.username = :username")
})
@XmlRootElement
public class Credentials implements Serializable { 
    @Id
    @SequenceGenerator(name="Credentials_Gen", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Credentials_Gen") 
    private int id;
    @Column(unique=true)
    @NotNull(message="Password can't be null")
    private String password;
    @NotNull(message="Username can't be null")
    private String username;

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
