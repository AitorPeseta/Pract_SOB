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

@XmlRootElement // Tanto el Comment como el topic deben tenerlo 
@Entity
public class Article implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @SequenceGenerator(name="Article_Gen", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Article_Gen") 
    private int id;
    
    private String title;
    
    private String content;
    
    private String summary;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Customer author;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishedDate = new Date();
    
    @ElementCollection
    @Column(name = "topic")
    private List<String> topics;
    
    private Boolean isPublic = true;
    
    private Integer views = 0;
    
    private String featuredImageUrl;
    
    public Integer getPopularity(){
        return views;
    }
    
    public void incrementViews(){
        this.views++; 
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getSummary() {
        return summary;
    }

    public Customer getAuthor() {
        return author;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public List<String> getTopics() {
        return topics;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public Integer getViews() {
        return views;
    }

    public String getFeaturedImageUrl() {
        return featuredImageUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setAuthor(Customer author) {
        this.author = author;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public void setFeaturedImageUrl(String featuredImageUrl) {
        this.featuredImageUrl = featuredImageUrl;
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
        return "Article{" + "id=" + id + ", title=" + title + ", content=" + content + ", summary=" + summary + ", author=" + author + ", publishedDate=" + publishedDate + ", topics=" + topics + ", isPublic=" + isPublic + ", views=" + views + ", featuredImageUrl=" + featuredImageUrl + '}';
    }
   
}
    