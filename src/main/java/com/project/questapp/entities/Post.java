package com.project.questapp.entities;

import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name="post")
@Data
public class Post {
	
	@Id
	//id'yi auto increment yaptık
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	//Long userId; tablonun create aşamasında kullanıldı artık ihtiyaç kalmadı 
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id",nullable=false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	User user;
	
	String title;
	@Lob
	//hibernate'in mysqlde stringi text olarak algılamaması için yoksa varchar(255) olarak kabul ediyor
	@Column(columnDefinition="text")
	String text;
	
	@Temporal(TemporalType.TIMESTAMP)
	Date createDate;

}
