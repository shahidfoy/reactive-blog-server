package com.shahidfoy.reactiveblog.controllers;

import com.shahidfoy.reactiveblog.exception_handlers.PostNotFoundException;
import com.shahidfoy.reactiveblog.models.Count;
import com.shahidfoy.reactiveblog.models.Post;
import com.shahidfoy.reactiveblog.models.enums.Status;
import com.shahidfoy.reactiveblog.repositories.PostRepository;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import static java.util.Comparator.comparing;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController()
@RequestMapping(value = "/posts")
public class PostController {

    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this. postRepository = postRepository;
    }

    @GetMapping("")
    public Flux<Post> all(@RequestParam(value = "q", required = false) String q,
                          @RequestParam(value = "page", defaultValue = "0") long page,
                          @RequestParam(value = "size", defaultValue = "10") long size) {

//
//        return filterPublishedPostsByKeyword(q)
//                .sort(comparing(Post::getCreatedDate).reversed())
//                .skip(page * size).take(size);

       return this.postRepository.findAll().skip(page * size).take(size);
    }

    @GetMapping(value = "/count")
    public Mono<Count> count(@RequestParam(value = "q", required = false) String q) {
        return filterPublishedPostsByKeyword(q).count().log().map(Count::new);
    }

    private Flux<Post> filterPublishedPostsByKeyword(String q) {
        return this.postRepository.findAll()
                .filter(p -> Status.DRAFT == p.getStatus())
                .filter(
                        p -> Optional.ofNullable(q)
                                .map(key -> p.getTitle().contains(key) || p.getContent().contains(key))
                                .orElse(true)
                );
    }

    @PostMapping("")
    public Mono<Post> create(@RequestBody Post post) {
        return this.postRepository.save(post);
    }

    @GetMapping("/{id}")
    public Mono<Post> get(@PathVariable("id") String id) {
        return this.postRepository.findById(id).switchIfEmpty(Mono.error(new PostNotFoundException(id)));
    }

    @PutMapping("/{id}")
    public Mono<Post> update(@PathVariable("id") String id, @RequestBody Post post) {
        return this.postRepository.findById(id)
                .map(p -> {
                    p.setTitle(post.getTitle());
                    p.setContent(post.getContent());

                    return p;
                })
                .flatMap(p -> this.postRepository.save(p));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public Mono<Void> delete(@PathVariable("id") String id) {
        return this.postRepository.deleteById(id);
    }

}
