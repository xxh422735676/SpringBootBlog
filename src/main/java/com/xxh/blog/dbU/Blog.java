package com.xxh.blog.dbU;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_blog")

public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;


    @Basic(fetch=FetchType.LAZY)
    @Lob  //建立LONGTEXT类型
    private  String content;

    private String firstPic;
    private  String flag;
    private Integer viewCount;
    private boolean appreciationEnabled;
    private  boolean shareInfoEnabled;
    private boolean commentEnabled;
    private  boolean publishedEnabled;
    private boolean recommandEnabled;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @ManyToOne
    private Type type;

    @ManyToMany(cascade = {CascadeType.PERSIST})  //新增级联关系
    private List<Tag> tags=new ArrayList<>();

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "blog") //many一方维护关系
    private List<Comment> comments=new ArrayList<>();

    @Transient //不保存在数据库中
    private String tagIds;

    private String description;

    public Blog() {
    }

    public void init(){
        this.tagIds=tagsToIds(this.getTags());
    }

    //处理tagIds转换成values
    private String tagsToIds(List<Tag> tags) {
        if (!tags.isEmpty()) {
            StringBuffer ids = new StringBuffer();
            boolean flag = false;
            for (Tag tag : tags) {
                if (flag) {
                    ids.append(",");
                } else {
                    flag = true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        } else {
            return tagIds;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", firstPic='" + firstPic + '\'' +
                ", flag='" + flag + '\'' +
                ", viewCount=" + viewCount +
                ", appreciationEnabled=" + appreciationEnabled +
                ", shareInfoEnabled=" + shareInfoEnabled +
                ", commentEnabled=" + commentEnabled +
                ", publishedEnabled=" + publishedEnabled +
                ", recommandEnabled=" + recommandEnabled +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", type=" + type +
                ", tags=" + tags +
                ", user=" + user +
                ", comments=" + comments +
                ", tagIds='" + tagIds + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFirstPic() {
        return firstPic;
    }

    public void setFirstPic(String firstPic) {
        this.firstPic = firstPic;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public boolean isAppreciationEnabled() {
        return appreciationEnabled;
    }

    public void setAppreciationEnabled(boolean appreciationEnabled) {
        this.appreciationEnabled = appreciationEnabled;
    }

    public boolean isShareInfoEnabled() {
        return shareInfoEnabled;
    }

    public void setShareInfoEnabled(boolean shareInfoEnabled) {
        this.shareInfoEnabled = shareInfoEnabled;
    }

    public boolean isCommentEnabled() {
        return commentEnabled;
    }

    public void setCommentEnabled(boolean commentEnabled) {
        this.commentEnabled = commentEnabled;
    }

    public boolean isPublishedEnabled() {
        return publishedEnabled;
    }

    public void setPublishedEnabled(boolean publishedEnabled) {
        this.publishedEnabled = publishedEnabled;
    }

    public boolean isRecommandEnabled() {
        return recommandEnabled;
    }

    public void setRecommandEnabled(boolean recommandEnabled) {
        this.recommandEnabled = recommandEnabled;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }
}
