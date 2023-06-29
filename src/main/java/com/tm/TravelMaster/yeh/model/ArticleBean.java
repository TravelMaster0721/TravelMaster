package com.tm.TravelMaster.yeh.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tm.TravelMaster.chih.model.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "article")
public class ArticleBean {

	@Id
	@Column(name = "articleId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer articleId;

	@Column(name = "articleName")
	private String articleName;

	@Column(name = "articleContent")
	private String articleContent;

	@Column(name = "articleType")
	private String articleType;

	@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss EEEE", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "articleDate", nullable = false, updatable = false, columnDefinition = "datetime2 default getdate()")
	private Date articleDate;

	@Column(name = "articleStatus")
	private String articleStatus;

	@JsonIgnore
	@Lob
	private byte[] articlePic;

	@JsonIgnore
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memberNum")
	private Member member;
	
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
	private List<CommentBean> comments = new ArrayList<>(0);

	@JsonIgnore
	@JsonManagedReference
	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
	private List<ArticleLikeBean> likes = new ArrayList<>(0);

	@PrePersist
	public void onCreate() {
		if (articleDate == null) {
			articleDate = new Date();
		}

		if (articleStatus == null) {
			articleStatus = "公開";
		}
	}

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public String getArticleContent() {
		return articleContent;
	}

	public void setArticleContent(String articleContent) {
		this.articleContent = articleContent;
	}

	public Date getArticleDate() {
		return articleDate;
	}

	public void setArticleDate(Date articleDate) {
		this.articleDate = articleDate;
	}

	public String getArticleType() {
		return articleType;
	}

	public void setArticleType(String articleType) {
		this.articleType = articleType;
	}

	public String getArticleStatus() {
		return articleStatus;
	}

	public void setArticleStatus(String articleStatus) {
		this.articleStatus = articleStatus;
	}

	public byte[] getArticlePic() {
		return articlePic;
	}

	public void setArticlePic(byte[] articlePic) {
		this.articlePic = articlePic;
	}

	public List<CommentBean> getComments() {
		return comments;
	}

	public void setComments(List<CommentBean> comments) {
		this.comments = comments;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public List<ArticleLikeBean> getLikes() {
		return likes;
	}

	public void setLikes(List<ArticleLikeBean> likes) {
		this.likes = likes;
	}

}
