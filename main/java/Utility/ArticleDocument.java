package Utility;

import com.fasterxml.jackson.annotation.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.*;
import java.io.Serializable;
import java.time.*;
import org.springframework.beans.factory.annotation.*;
@Component
public class ArticleDocument implements Serializable{

    @Id

    //The Id of the document
    @JsonProperty(value = "id")
    private String id;
    @JsonProperty(value = "content")
    //The document's content
    private String content;
    //The document's source
    @JsonProperty(value = "source")
    private String source;
    //The document's mediaType
    @JsonProperty(value = "mediaType")
    private String mediaType;
    //The document's published field
    @JsonProperty(value = "published")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime publishedLocalDateTime;

    //A constant representing the "id" field in a retrieved or posted record
    public final static String ID_FIELD = "id";

    //A constant representing the "content" field in a retrieved or posted record
    public final static String CONTENT_FIELD = "content";

    //A constant representing the "media-Type" field in a retrieved or posted record
    public final static String MEDIA_TYPE_FIELD = "media-type";

    //A constant representing the "source" field in a retrieved or posted record
    public final static String SOURCE_FIELD = "source";

    //A constant representing the "published" field in a retrieved or posted record
    public final static String PUBLISHED_FIELD  ="published";

    //Constructor
    @JsonCreator
    public ArticleDocument(){

    }
    public ArticleDocument(@JsonProperty("id") String id,
                           @JsonProperty("content") String content,
                           @JsonProperty("mediaType") String mediaType,
                           @JsonProperty("source") String source,
                           @JsonProperty("published")
                           @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
                                   LocalDateTime publishedLocalDateTime){
        this.id = id;
        this.content = content;
        this.mediaType = mediaType;
        this.source = source;
        this.publishedLocalDateTime = publishedLocalDateTime;

    }

    //Constructor
    public ArticleDocument(int num){

    }

    //Getter method
    public String getId(){
        return id;
    }

    //Getter method
    public String getContent(){
        return content;
    }

    //Getter method
    public String getSource(){
        return source;
    }

    //Getter method
    public String getMediaType(){
        return mediaType;
    }

    //Getter method
    public LocalDateTime getPublishedLocalDateTime(){
        return publishedLocalDateTime;
    }

    //Setter method
    public ArticleDocument setId(String id){
        this.id = id;
        return this;
    }

    //Setter method
    public ArticleDocument setContent(String content){
        this.content = content;
        return this;
    }

    //Setter method
    public ArticleDocument setSource(String source){
        this.source = source;
        return this;
    }

    //Setter method
    public ArticleDocument setMediaType(String mediaType){
        this.mediaType = mediaType;
        return this;
    }

    //Setter method
    public ArticleDocument setPublishedLocalDateTime(LocalDateTime publishedLocalDateTime){
        this.publishedLocalDateTime = publishedLocalDateTime;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public String toString(){
        return "Id: " + id + " Content: " + content + " Source: " + source + " Media Type: " + mediaType + " Published LocalDateTime: " + publishedLocalDateTime;
    }
}
