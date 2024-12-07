package model.entities;

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

@XmlRootElement
@Entity
@NamedQueries({
    @NamedQuery(
        name = "Article.findAllPublic",
        query = "SELECT a FROM Article a WHERE a.isPublic = TRUE ORDER BY a.views DESC"
    ),
    @NamedQuery(
    name = "Article.findByAuthorAndBothTopics",
    query = "SELECT DISTINCT a FROM Article a " +
            "WHERE a.isPublic = TRUE " +
            "AND ((a.author.id = :author) " +
            "AND (:topic1 IN (SELECT t.id FROM a.topics t)) " +
            "AND (:topic2 IN (SELECT t.id FROM a.topics t)))" +
            "ORDER BY a.views DESC"
    ),
    @NamedQuery(
    name = "Article.findByAuthor",
    query = "SELECT DISTINCT a FROM Article a " +
            "WHERE a.isPublic = TRUE " +
            "AND (a.author.id = :author) " +
            "ORDER BY a.views DESC"
    ),
    @NamedQuery(
    name = "Article.findByTopic",
    query = "SELECT DISTINCT a FROM Article a " +
            "WHERE a.isPublic = TRUE " +
            "AND (:topic IN (SELECT t.id FROM a.topics t)) " +
            "ORDER BY a.views DESC"
    ),
    @NamedQuery(
    name = "Article.findByTwoTopic",
    query = "SELECT DISTINCT a FROM Article a " +
            "WHERE a.isPublic = TRUE " +
            "AND (:topic1 IN (SELECT t.id FROM a.topics t)) " +
            "AND (:topic2 IN (SELECT t.id FROM a.topics t)) " +
            "ORDER BY a.views DESC"
    ),
    @NamedQuery(
    name = "Article.findByAuthorAndSingleTopic",
    query = "SELECT DISTINCT a FROM Article a " +
            "WHERE a.isPublic = TRUE " +
            "AND (a.author.id = :author) " +
            "AND (:topic IN (SELECT t.id FROM a.topics t)) " +
            "ORDER BY a.views DESC"
    ),
    @NamedQuery(
            name = "Article.findArticleId",
            query = "SELECT a FROM Article a WHERE a.id = :id"
    ),
    @NamedQuery(
            name = "Article.isPublic",
            query = "SELECT a.isPublic FROM Article a WHERE a.id = :id"
    ),
    @NamedQuery(
            name = "Article.findAuthor",
            query = "SELECT a.author FROM Article a WHERE a.id = :id"
    )
})
public class Article implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @SequenceGenerator(name="Article_Gen", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Article_Gen") 
    private int id;
    
    private String content;
    
    private String featuredImageUrl;
    
    private boolean isPublic;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishedDate = new Date();
    
    private String summary;
    
    private String title;
    
    private Integer views;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", referencedColumnName = "id")
    private Customer author;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "article_topic",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    private List<Topic> topics;

    public int getId() {
        return id;
    }
    
    public String getContent() {
        return content;
    }
    
    public String getFeaturedImageUrl() {
        return featuredImageUrl;
    }
    
    public boolean getIsPublic() {
        return isPublic;
    }

     public Date getPublishedDate() {
        return publishedDate;
    }
    
    public String getSummary() {
        return summary;
    } 
     
    public String getTitle() {
        return title;
    }
    
    public Integer getViews() {
        return views;
    }
    
    public Customer getAuthor() {
        return author;
    }
    
    public List<Topic> getTopic() {
        return this.topics;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public void setFeaturedImageUrl(String featuredImageUrl) {
        this.featuredImageUrl = featuredImageUrl;
    }
    
     public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }
     
    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    } 
     
    public void setTitle(String title) {
        this.title = title;
    }

    public void setViews(Integer views) {
        this.views = views;
    }
    
    public void setAuthor(Customer author) {
        this.author = author;
    }
    
    public void setTopic(List<Topic> topics) {
        this.topics = topics;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this.id;
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
        final Article other = (Article) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "Article{" + "id=" + id + ", title=" + title + ", content=" + content + ", summary=" + summary + ", author=" + author + ", publishedDate=" + publishedDate + ", topics=" + topics.toString() + ", isPublic=" + isPublic + ", views=" + views + ", featuredImageUrl=" + featuredImageUrl + '}';
    }
    
     public Integer getPopularity(){
        return views;
    }
    
    public void incrementViews(){
        this.views++; 
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    
    public Article(){}
   
}
    
