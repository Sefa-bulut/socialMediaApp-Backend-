package com.project.questapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.questapp.entities.Comment;
import com.project.questapp.entities.Like;
import com.project.questapp.entities.User;
import com.project.questapp.repos.CommentRepository;
import com.project.questapp.repos.LikeRepository;
import com.project.questapp.repos.PostRepository;
import com.project.questapp.repos.UserRepository;

@Service
public class UserService {
	
	private UserRepository userRepository;
	LikeRepository likeRepository;
	CommentRepository commentRepository;
	PostRepository postRepository;
	

	public UserService(UserRepository userRepository, LikeRepository likeRepository,
			CommentRepository commentRepository, PostRepository postRepository) {
		this.userRepository = userRepository;
		this.likeRepository = likeRepository;
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
	}

	public List<User> getAllUsers() {
		//Jpa repository'in altında hazır metotlardan yararlanıyoruz
		//UserRepository'imiz jpa'i extends ettiği için burada metotlara erişebiliyoruz
		//findAll da bu metotlardan biri ve select * from sorgusuna denktir
		return userRepository.findAll();
	}

	public User saveOneUser(User newUser) {
		//Gelen isteğin gövdesini requestbody ile alıp user nesnesine dönüştüryoruz
		//elimizdeki bu yeni user nesnesi ile de database'e kayıt yapıyoruz
		return userRepository.save(newUser);
	}

	public User getOneUser(Long userId) {
		return userRepository.findById(userId).orElse(null);
	}

	public User updateOneUser(Long userId, User newUser) {
		Optional<User> user = userRepository.findById(userId);
		//verilen id'de bir user varsa
		if(user.isPresent()) {
			User foundUser = user.get();
	        // Kısmi güncellemeler
	        if (newUser.getUserName() != null) {
	            foundUser.setUserName(newUser.getUserName());
	        }
	        if (newUser.getPassword() != null) {
	            foundUser.setPassword(newUser.getPassword());
	        }
	        if (newUser.getAvatar() != 0) {
	            foundUser.setAvatar(newUser.getAvatar());
	        }
			userRepository.save(foundUser);
			return foundUser;
		}else
			return null;
	}

	public void deleteOne(Long userId) {
		userRepository.deleteById(userId);
	}

	public User getOneUserByUserName(String userName) {
		// TODO Auto-generated method stub
		return userRepository.findByUserName(userName);
	}

	public List<Object> getUserActivity(Long userId) {
		List<Long> postIds = postRepository.findTopByUserId(userId);
		if(postIds.isEmpty())
			return null;
		List<Object> comments = commentRepository.findUserCommentsByPostId(postIds);
		List<Object> likes = likeRepository.findUserLikesByPostId(postIds);
		List<Object> result = new ArrayList<>();
		result.addAll(comments);
		result.addAll(likes);
		return result;
		
	}
	
}
