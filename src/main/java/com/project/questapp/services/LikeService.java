package com.project.questapp.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.project.questapp.entities.Like;
import com.project.questapp.entities.Post;
import com.project.questapp.entities.User;
import com.project.questapp.repos.LikeRepository;
import com.project.questapp.requests.LikeCreateRequest;
import com.project.questapp.responses.LikeResponse;

@Service
public class LikeService {
	
	private LikeRepository likeRepository;
	private UserService userService;
	private PostService postService;

	public LikeService(LikeRepository likeRepository, UserService userService, PostService postService) {
		this.likeRepository = likeRepository;
		this.userService = userService;
		this.postService = postService;
	}

	public List<LikeResponse> getAllLike(Optional<Long> postId, Optional<Long> userId) {
		List<Like> list; 
		if(postId.isPresent() && userId.isPresent())
			list = likeRepository.findByUserIdAndPostId(userId.get(), postId.get());
		else if(postId.isPresent())
			list = likeRepository.findByPostId(postId.get());
		else if(userId.isPresent())
			list = likeRepository.findByUserId(userId.get());
		else
			list = likeRepository.findAll();
		return list.stream().map(like -> new LikeResponse(like)).collect(Collectors.toList());
	}

	public Like getOneLikes(Long likeId) {
		return likeRepository.findById(likeId).orElse(null);
	}

	public Like createOneLikes(LikeCreateRequest likeRequest) {
		User user = userService.getOneUser(likeRequest.getUserId());
		Post post = postService.getOnePostById(likeRequest.getPostId());
		
		if(user != null && post != null) {
			Like toSave = new Like();
			toSave.setId(likeRequest.getId());
			toSave.setPost(post);
			toSave.setUser(user);
			return likeRepository.save(toSave);
		}
		return null;
	}

	public void deleteOneLikeById(Long likeId) {
		likeRepository.deleteById(likeId);
	}

}
