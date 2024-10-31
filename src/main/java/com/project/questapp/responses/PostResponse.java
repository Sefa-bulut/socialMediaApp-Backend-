package com.project.questapp.responses;

import java.util.List;

import com.project.questapp.entities.Post;

import lombok.Data;

@Data
public class PostResponse {
	
	Long id;
	Long userId;
	String userName;
	String title;
	String text;
	
	List<LikeResponse> postLikes;
	
	
	//Post objesinin içinde hem post fieldları var hem de User objesi var bunlar
	//tek bir sınıfta toplamak için PostResponse sınıfını oluşturduk ve şimdi onun
	//değişkenlerini initalize yapıyoruz
	public PostResponse (Post entity, List<LikeResponse> likes) {
		this.id = entity.getId();
		this.userId = entity.getUser().getId();
		this.userName = entity.getUser().getUserName();
		this.title = entity.getTitle();
		this.text = entity.getText();
		this.postLikes = likes;
	}
	
}
