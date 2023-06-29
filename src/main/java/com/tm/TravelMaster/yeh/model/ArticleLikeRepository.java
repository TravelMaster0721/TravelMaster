package com.tm.TravelMaster.yeh.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleLikeRepository extends JpaRepository<ArticleLikeBean, Integer> {
	
	//比對會員是否已針對特定文章按讚
	List<ArticleLikeBean> findByMemberMemberNumAndArticleArticleId(String memberNum, Integer articleId);

	
    List<ArticleLikeBean> findByArticleArticleId(Integer articleId);

}
