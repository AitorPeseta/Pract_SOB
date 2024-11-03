package model.entities;

import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;
import jakarta.persistence.*;

@XmlRootElement
@Entity
@NamedQuery(
    name = "Customer.findAllWithoutSensitiveData",
    query = "SELECT NEW model.entities.Customer(c.id, c.username, c.email, c.isAuthor, c.lastArticleId, c.registrationDate) " +
            "FROM Customer c"
)
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="Customer_Gen", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Customer_Gen") 
    private int id;
    
    private String username;
    
    private String password;
    
    private String email;
    
    private Boolean isAuthor = false;
    
    private Long lastArticleId; // Para HATEOAS
    
    private Date registrationDate = new Date();
    
    @OneToMany(mappedBy = "author", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Article> articles;
    
    //Constructor sin datos confidenciales
    public Customer(int id, String username, String email, Boolean isAuthor, Long lastArticleId, Date registrationDate) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.isAuthor = isAuthor;
    this.lastArticleId = lastArticleId;
    this.registrationDate = registrationDate;
    }
    
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getIsAuthor() {
        return isAuthor;
    }

    public Long getLastArticleId() {
        return lastArticleId;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIsAuthor(Boolean isAuthor) {
        this.isAuthor = isAuthor;
    }

    public void setLastArticleId(Long lastArticleId) {
        this.lastArticleId = lastArticleId;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Customer other = (Customer) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "Customer{" + "id=" + id + ", username=" + username + ", password=" + password + ", email=" + email + ", isAuthor=" + isAuthor + ", lastArticleId=" + lastArticleId + ", registrationDate=" + registrationDate + ", articles=" + articles + '}';
    }

    
}
