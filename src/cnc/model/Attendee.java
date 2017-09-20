package cnc.model;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "name",
    "company",
    "email",
    "twitter"
})

@XmlRootElement (name="Attendee")
public class Attendee  {
  
  
    @XmlElement(name="company")
    private String company;
    
    @XmlElement(name="createdDate")
    private Timestamp createdDate;
    
    @XmlElement(name="email")
    private String email;
    
    private String image;
    private String imageThumb;
    private String imageMorph;
    private String imageAssoc;
    private String cncPath;

    public void setImageThumb(String imageThumb) {
        this.imageThumb = imageThumb;
    }

    public String getImageThumb() {
        return imageThumb;
    }

    public void setImageMorph(String imageMorph) {
        this.imageMorph = imageMorph;
    }

    public String getImageMorph() {
        return imageMorph;
    }

    public void setImageAssoc(String imageAssoc) {
        this.imageAssoc = imageAssoc;
    }

    public String getImageAssoc() {
        return imageAssoc;
    }

    public void setCncPath(String cncPath) {
        this.cncPath = cncPath;
    }

    public String getCncPath() {
        return cncPath;
    }

    @XmlElement(name="lastRequestedDate")
    private Timestamp lastRequestedDate;
   
    @XmlElement(name="name")
    private String name;
   
    @XmlElement(name="pk")
    private BigDecimal pk;
   
    @XmlElement(name="requestedCount")
    private BigDecimal requestedCount;
  
    @XmlElement(name="twitter")
    private String twitter;

    public Attendee() {
    }

    public Attendee(String name,String company, String email, String twitter, 
                    String image, String  imageThumb, String imageMorph, String imageAssoc, String cncPath) {
        
        this.company = company;
        this.email = email;
        this.name = name;
        this.twitter = twitter; 
        this.image = image;
        
        this.imageThumb = imageThumb;
        this.imageMorph = imageMorph;
        this.imageAssoc = imageAssoc;
        this.cncPath = cncPath;
                
    }
    
    public Attendee(BigDecimal pk, String name,  
                    String image, String  imageThumb, String imageMorph, String imageAssoc, String cncPath) {     
        this.pk = pk;
        this.name = name;        
        this.image = image;
        
        this.imageThumb = imageThumb;
        this.imageMorph = imageMorph;
        this.imageAssoc = imageAssoc;
        this.cncPath = cncPath;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Timestamp getLastRequestedDate() {
        return lastRequestedDate;
    }

    public void setLastRequestedDate(Timestamp lastRequestedDate) {
        this.lastRequestedDate = lastRequestedDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPk() {
        return pk;
    }

    public void setPk(BigDecimal pk) {
        this.pk = pk;
    }

    public BigDecimal getRequestedCount() {
        return requestedCount;
    }

    public void setRequestedCount(BigDecimal requestedCount) {
        this.requestedCount = requestedCount;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
}
