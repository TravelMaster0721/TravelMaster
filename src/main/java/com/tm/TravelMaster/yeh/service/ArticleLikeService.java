package com.tm.TravelMaster.yeh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tm.TravelMaster.yeh.model.ArticleLikeBean;
import com.tm.TravelMaster.yeh.model.ArticleLikeRepository;

@Service
public class ArticleLikeService {
	
	@Autowired
	private ArticleLikeRepository alRepo;
	
	//新增讚
	public void addLike(ArticleLikeBean like) {
		alRepo.save(like);
	}
	
	//移除讚
	public void removeLike(ArticleLikeBean like) {
		alRepo.delete(like);
	}
	
	//透過文章ID 查該文章的讚有哪些
    public List<ArticleLikeBean> getLikesByArticleId(Integer articleId) {
        return alRepo.findByArticleArticleId(articleId);
    }
    
    public List<ArticleLikeBean>  findBymemberIdAndArticleId(String memberId,Integer articleId){
    	return alRepo.findByMemberMemberNumAndArticleArticleId(memberId,articleId);
    }
	
}
