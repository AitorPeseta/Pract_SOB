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
        name = "Article.findByAuthorAndTopics",
        query = "SELECT a FROM Article a WHERE a.isPublic = TRUE " +
                "AND (:author IS NULL OR a.author.credenciales.username = :author) " +
                "AND ((:topic1 IS NULL OR :topic1 IN :topics) " +
                "AND (:topic2 IS NULL OR :topic2 IN :topics)) " +
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
    
    private Boolean isPublic;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishedDate = new Date();
    
    private String summary;
    
    private String title;
    
    private Integer views;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "article_topic",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id")
    )
    private List<Topic> topics;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Customer author;

    public int getId() {
        return id;
    }
    
    public String getContent() {
        return content;
    }
    
    public String getFeaturedImageUrl() {
        return featuredImageUrl;
    }
    
    public Boolean getIsPublic() {
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
    
    public List<Topic> getTopic() {
        return this.topics;
    }
    
    public Integer getViews() {
        return views;
    }
    
    @JsonbTransient
    public Customer getAuthor() {
        return author;
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
    
    public void setTopic(Topic topics) {
        this.topics.add(topics);
    }

    public void setViews(Integer views) {
        this.views = views;
    }
    
    @JsonbTransient
    public void setAuthor(Customer author) {
        this.author = author;
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
    
