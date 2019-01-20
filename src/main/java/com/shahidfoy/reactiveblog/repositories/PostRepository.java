package com.shahidfoy.reactiveblog.repositories;

import com.shahidfoy.reactiveblog.models.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PostRepository extends ReactiveMongoRepository<Post, String> {
}
