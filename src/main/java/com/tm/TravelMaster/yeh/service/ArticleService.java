package com.tm.TravelMaster.yeh.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tm.TravelMaster.yeh.model.ArticleBean;
import com.tm.TravelMaster.yeh.model.ArticleRepository;

@Service
public class ArticleService {

	@Autowired
	private ArticleRepository arRepo;

	// 新增一篇文章
	public void insert(ArticleBean article) {
		arRepo.save(article);
	}
	
	//查詢所有文章
	public List<ArticleBean> findAllArticle(){
		return arRepo.findAll();
	}
	
	//查詢所有公開的文章
	public List<ArticleBean> findPublicArticle(){
		return arRepo.findPublicArticles();
	}
	
	//透過文章編號查詢特定文章
	public ArticleBean findById(Integer articleId) {
		Optional<ArticleBean> optional = arRepo.findById(articleId);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	//透過文章編號刪除文章
	public void deleteById(Integer articleId) {
		if(articleId != null ) {
			arRepo.deleteById(articleId);
		}
	}
	//更新文章
	public void edit(ArticleBean article) {
		arRepo.save(article);
	}
	
	//透過會員編號查詢該會員文章且為公開的並按照降序排列
	public List<ArticleBean> findPublicArticlesByMemberNumOrderByArticleIdDesc(String memberNum){
		List<ArticleBean> articles = arRepo.findPublicArticlesByMemberNumOrderByArticleIdDesc(memberNum);
		return articles;
	}
	
	
	

}
