package com.tm.TravelMaster.yeh.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tm.TravelMaster.yeh.service.ArticleLikeService;
import com.tm.TravelMaster.yeh.service.ArticleService;
import com.tm.TravelMaster.yeh.service.CommentService;
import com.tm.TravelMaster.yeh.model.ArticleBean;
import com.tm.TravelMaster.yeh.model.ArticleLikeBean;
import com.tm.TravelMaster.yeh.model.CommentBean;
import com.tm.TravelMaster.yeh.model.CommentDTO;
import com.tm.TravelMaster.chih.dao.MemberService;
import com.tm.TravelMaster.chih.model.Member;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForumController {

	@Autowired
	private ArticleService aService;
	@Autowired
	private CommentService cService;
	@Autowired
	private ArticleLikeService alService;
	@Autowired
	private MemberService mService;

	/*----------------------------------------------頁面跳轉相關--------------------------------------------------*/

	// 進入論壇首頁
	@GetMapping("/forum/intoForumPage")
	public String intoForumPage(Model model, HttpSession session) {
		Member member = (Member) session.getAttribute("mbsession");
		List<ArticleBean> articles = aService.findPublicArticle();
		model.addAttribute("articles", articles);
		if (member != null) {
			model.addAttribute("member", member);
		}
		return "yeh/forumPage";
	}

	// 進入會員個人文章頁面
	@GetMapping("/forum/personalPage")
	public String intoPersonalPage(HttpSession session, Model model) {
		Member member = (Member) session.getAttribute("mbsession");
		if (member != null) {
			List<ArticleBean> articles = aService
					.findPublicArticlesByMemberNumOrderByArticleIdDesc(member.getMemberNum());
			model.addAttribute("articles", articles);
			return "yeh/personalPage";
		} else {
			return "redirect:/login.controller";
		}
	}

	// 進入新增文章頁面
	@GetMapping("/forum/insertArticle")
	public String intoInsertArticle(HttpSession session) {
		Member member = (Member) session.getAttribute("mbsession");
		if (member != null) {
			return "yeh/insertArticle";
		} else {
			return "redirect:/login.controller";
		}
	}

	// 進入後台頁面
	@GetMapping("/forum/forumBackstage")
	public String intoForumBackstage(Model model) {
		List<ArticleBean> articles = aService.findAllArticle();
		model.addAttribute("articles", articles);
		return "yeh/forumBackstage";
	}

	// 搜尋特定照片
	@GetMapping("/forum/getImage/{articleId}")
	public ResponseEntity<byte[]> getImage(@PathVariable Integer articleId) {
		ArticleBean article = aService.findById(articleId);
		byte[] articlePic = article.getArticlePic();
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<byte[]>(articlePic, header, HttpStatus.OK);
	}

	/*----------------------------------------------文章相關操作--------------------------------------------------*/

	// 執行新增文章
	@PostMapping("/forum/insertArticle")
	public String doInsertArticle(HttpSession session, @RequestParam("articleName") String articleName,
			@RequestParam("articleType") String articleType, @RequestParam("articleContent") String articleContent,
			@RequestParam("articlePic") MultipartFile articlePic, Model model) {

		Member member = (Member) session.getAttribute("mbsession");

		try {
			ArticleBean article = new ArticleBean();
			article.setArticleName(articleName);
			article.setArticleContent(articleContent);
			article.setArticleType(articleType);
			article.setArticlePic(articlePic.getBytes());
			article.setMember(member);
			aService.insert(article);
			return "redirect:/forum/personalPage";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/forum/personalPage";
	}

	// 取得欲修改文章
	@GetMapping("/forum/intoEdit")
	public String getEditArticle(@RequestParam("articleId") Integer articleId, Model model) {
		ArticleBean result = aService.findById(articleId);
		model.addAttribute("article", result);
		return "yeh/editArticle";
	}

	// 執行文章修改
	@PostMapping("/forum/doEdit")
	public String doEditArticle(HttpSession session, @RequestParam("articleId") Integer articleId,
			@RequestParam("articleName") String articleName, @RequestParam("articleType") String articleType,
			@RequestParam("articleContent") String articleContent,
			@RequestParam("articlePic") MultipartFile articlePic) {
		Member member = (Member) session.getAttribute("mbsession");
		try {
			ArticleBean article = new ArticleBean();
			article.setArticleId(articleId);
			article.setArticleName(articleName);
			article.setArticleContent(articleContent);
			article.setArticleType(articleType);
			article.setArticlePic(articlePic.getBytes());
			article.setMember(member);
			aService.edit(article);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:/forum/personalPage";
	}

	// 執行刪除文章
	@GetMapping("/forum/deleteArticle")
	public String doDeleteArticle(@RequestParam("articleId") Integer articleId) {
		ArticleBean article = aService.findById(articleId);
		article.setArticleStatus("刪除");
		aService.edit(article);
		return "redirect:/forum/personalPage";
	}

	/*----------------------------------------------留言相關操作--------------------------------------------------*/

	// 取得特定文章的留言
	@ResponseBody
	@GetMapping("/forum/findComment")
	public List<CommentDTO> findCommentByArticleId(@RequestParam("articleId") Integer articleId) {
		List<CommentBean> comments = cService.findAllByArticleId(articleId);
		List<CommentDTO> dtos = new ArrayList<>();
		for (CommentBean comment : comments) {
			CommentDTO dto = new CommentDTO();
			dto.setCommentId(comment.getCommentId());
			dto.setCommentDate(comment.getCommentDate());
			dto.setCommentContent(comment.getCommentContent());
			dto.setMemberNum(comment.getMember().getMemberNum());
			dto.setMemberName(comment.getMember().getMemberName());
			dtos.add(dto);
		}
		return dtos;
	}

	// 新增留言
	@ResponseBody
	@PostMapping("/forum/insertComment")
	public ResponseEntity<?> insertComment(HttpSession session, @RequestParam("commentContent") String commentContent,
			@RequestParam("articleId") Integer articleId) {
		Member member = (Member) session.getAttribute("mbsession");
		if (member != null) {
			ArticleBean article = aService.findById(articleId);
			CommentBean NewComment = new CommentBean();
			NewComment.setCommentContent(commentContent);
			NewComment.setArticle(article);
			NewComment.setMember(member);
			cService.insertComment(NewComment);
			List<CommentBean> comments = cService.findAllByArticleId(articleId);
			List<CommentDTO> dtos = new ArrayList<>();
			for (CommentBean comment : comments) {
				CommentDTO dto = new CommentDTO();
				dto.setCommentId(comment.getCommentId());
				dto.setCommentDate(comment.getCommentDate());
				dto.setCommentContent(comment.getCommentContent());
				dto.setMemberNum(comment.getMember().getMemberNum());
				dto.setMemberName(comment.getMember().getMemberName());
				dtos.add(dto);
			}
			return ResponseEntity.ok(dtos);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("請先登入!!");
		}
	}

	// 刪除留言
	@ResponseBody
	@PostMapping("/forum/deleteComment")
	public ResponseEntity<List<CommentDTO>> deleteComment(@RequestParam("commentId") Integer commentId,
			@RequestParam("articleId") Integer articleId) {

		cService.deleteById(commentId);
		List<CommentBean> comments = cService.findAllByArticleId(articleId);
		List<CommentDTO> dtos = new ArrayList<>();
		for (CommentBean comment : comments) {
			CommentDTO dto = new CommentDTO();
			dto.setCommentId(comment.getCommentId());
			dto.setCommentDate(comment.getCommentDate());
			dto.setCommentContent(comment.getCommentContent());
			dto.setMemberNum(comment.getMember().getMemberNum());
			dto.setMemberName(comment.getMember().getMemberName());
			dtos.add(dto);
		}
		return ResponseEntity.ok(dtos);

	}

	@ResponseBody
	@PutMapping("/forum/editComment")
	public String editMessage(@RequestBody CommentDTO commentDTO) {
		CommentBean newComment = cService.editCommentById(commentDTO.getCommentId(), commentDTO.getCommentContent());
		return newComment.getCommentContent();
	}

	/*----------------------------------------------按讚相關操作--------------------------------------------------*/

	// 按讚相關操作

	@ResponseBody
	@GetMapping("/forum/articleLike")
	public boolean likeControl(@RequestParam("memberNum") String memberNum, @RequestParam("articleId") Integer articleId) {
		List<ArticleLikeBean> list = alService.findBymemberIdAndArticleId(memberNum, articleId);
		ArticleBean article = aService.findById(articleId);
		Member member = mService.findByMemberNum(memberNum);
		if (list != null && list.size() > 0) {
			ArticleLikeBean like = list.get(0);
			alService.removeLike(like);
			return false;
		} else {
			ArticleLikeBean like = new ArticleLikeBean();
			like.setArticle(article);
			like.setMember(member);
			alService.addLike(like);
			return true;
			
		}

	}

}
