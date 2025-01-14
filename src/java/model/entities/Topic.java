/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entities;

import jakarta.persistence.CascadeType;
import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement // Tanto el Comment como el topic deben tenerlo 
@Entity
@NamedQueries({
    @NamedQuery(
        name = "Topic.existTopic",
        query = "SELECT COUNT(t) FROM Topic t WHERE t.id = :id "
    ),
    @NamedQuery(
        name = "Topic.getName",
        query = "SELECT t.name FROM Topic t WHERE t.id = :id "
    ),
    @NamedQuery(
        name = "Topic.getArticles",
        query = "SELECT t.articles FROM Topic t WHERE t.id = :id "
    )
})
public class Topic implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="Topic_Gen", allocationSize=1) // Els identificadors creixeran un a un
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Topic_Gen") 
    private Long id;
    private String name;
    
    @ManyToMany(mappedBy = "topics") // "topics" es el nombre del atributo en Article
    private List<Article> articles;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public void setArticles(List<Article> article){
        this.articles = article;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Topic)) {
            return false;
        }
        Topic other = (Topic) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.entities.Topic[ id=" + id + " ]";
    }
    
}
