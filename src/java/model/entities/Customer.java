package model.entities;

import authn.Credentials;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTransient;
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
import jakarta.security.enterprise.credential.Credential;
import jakarta.validation.constraints.NotNull;

@XmlRootElement
@Entity
@NamedQueries({
    @NamedQuery(
        name = "Customer.findAllWithoutSensitiveData",
        query = "SELECT NEW model.entities.Customer(c.id, c.credenciales, c.email, c.isAuthor, c.lastArticleId, c.registrationDate) " +
                "FROM Customer c"
    ),
    @NamedQuery(
        name = "Customer.findCustomerWithoutSensitiveData",
        query = "SELECT NEW model.entities.Customer(c.id, c.credenciales, c.email, c.isAuthor, c.lastArticleId, c.registrationDate) " +
                "FROM Customer c WHERE c.id = :id" 
    ),
    @NamedQuery(
            name = "Customer.existAuthor",
            query = "SELECT COUNT(c) FROM Customer c WHERE c.id = :id"
    )
})
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="Customer_Gen", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Customer_Gen") 
    private int id;

    private String email;
    
    private boolean isAuthor;
    
    private Long lastArticleId; // Para HATEOAS
        
    private Date registrationDate = new Date();
    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "credentials_id", referencedColumnName = "id")
    private Credentials credenciales;
       
    @OneToMany(mappedBy = "author", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Article> articles;
  
    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
    
    public boolean getIsAuthor() {
        return isAuthor;
    }
    
    public Long getLastArticleId() {
        return lastArticleId;
    }
    
    public Date getRegistrationDate() {
        return registrationDate;
    }
    
    public Credentials getCredenciales() {
        return credenciales;
    }

    @JsonbTransient
    public List<Article> getArticles() {
        return articles;
    }

    public void setId(int id) {
        this.id = id;
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
    
    public void setCredenciales(Credentials credentials) {
        this.credenciales = credentials;
    }

    @JsonbTransient
    public void setArticles(Article articles) {
        this.articles.add(articles);
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
        return "Customer{" + "id=" + id + ", credenciales=" + credenciales + ", email=" + email + ", isAuthor=" + isAuthor + ", lastArticleId=" + lastArticleId + ", registrationDate=" + registrationDate + ", articles=" + articles + '}';
    }
    
     public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    //Constructor sin datos confidenciales
    public Customer(int id, Credentials credenciales, String email, boolean isAuthor, Long lastArticleId, Date registrationDate) {
        this.id = id;
        this.email = email;
        this.isAuthor = isAuthor;
        this.lastArticleId = lastArticleId;
        this.registrationDate = registrationDate;
        this.credenciales = credenciales; 
    }
    
    public Customer(){}
    
}

