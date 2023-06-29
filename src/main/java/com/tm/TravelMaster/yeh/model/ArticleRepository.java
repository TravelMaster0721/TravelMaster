package com.tm.TravelMaster.yeh.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<ArticleBean, Integer> {

    // 透過會員編號找該會員發過文章，並按照文章ID降序排列
    @Query("SELECT articles FROM ArticleBean articles WHERE articles.member.memberNum = :memberNum AND articles.articleStatus = '公開' ORDER BY articles.articleId DESC")
    List<ArticleBean> findPublicArticlesByMemberNumOrderByArticleIdDesc(@Param("memberNum") String memberNum);
	
	//查詢所有公開發表的文章
	@Query("SELECT articles FROM ArticleBean articles WHERE articles.articleStatus = '公開'")
    List<ArticleBean> findPublicArticles();

}
