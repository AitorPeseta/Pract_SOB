package model.entities;

import authn.Credentials;
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
    
    @JoinColumn(name = "Credentials_id", nullable = false)
    private Credentials credenciales;

    private String email;
    
    private Boolean isAuthor;
    
    private Long lastArticleId; // Para HATEOAS
        
    private Date registrationDate = new Date();
       
    @OneToMany(mappedBy = "author", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Article> articles;
  
    public int getId() {
        return id;
    }
    
    public Credentials getCredenciales() {
        return credenciales;
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

    @JsonbTransient
    public List<Article> getArticles() {
        return articles;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void setCredentials(Credentials credentials) {
        this.credenciales = credentials;
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

    public Customer() {
    }
    
    //Constructor sin datos confidenciales
    public Customer(int id, String username, String email, Boolean isAuthor, Long lastArticleId, Date registrationDate) {
        EntityManager em = null;
        this.id = id;
        this.email = email;
        this.isAuthor = isAuthor;
        this.lastArticleId = lastArticleId;
        this.registrationDate = registrationDate;
        Credentials credential = new Credentials();
        credential.setUsername(username);
        TypedQuery<Credentials> query = em.createNamedQuery("Credentials.findId", Credentials.class);
        int id2 = query.getFirstResult();
        credential.setId(id2); 
        
    }
    
}

