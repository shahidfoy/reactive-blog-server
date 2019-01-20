package com.shahidfoy.reactiveblog;

import com.shahidfoy.reactiveblog.models.Post;
import com.shahidfoy.reactiveblog.models.User;
import com.shahidfoy.reactiveblog.repositories.PostRepository;
import com.shahidfoy.reactiveblog.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public DataInitializer(PostRepository postRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;

        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String[] args) {
        log.info("start data initialization ...");
        this.postRepository.deleteAll()
                .thenMany(
                        Flux.just("Post one", "Post two",
                                "Post one", "Post two",
                                "Post one", "Post two",
                                "Post one", "Post two",
                                "Post one", "Post two",
                                "Post one", "Post two",
                                "Post one", "Post two",
                                "Post one", "Post two",
                                "Post one", "Post two",
                                "Post one", "Post two",
                                "Post one", "Post two",
                                "Post one", "Post two",
                                "Post one", "Post two",
                                "Post one", "Post two",
                                "Post one", "Post two",
                                "Post one", "Post two",
                                "Post one", "Post two",
                                "Post one", "Post two")
                        .flatMap(
                                title -> this.postRepository.save(Post.builder().title(title).content("content of " + title).build())
                        )
                )
                .log()
                .subscribe(
                        null,
                        null,
                        () -> log.info("done initialization...")
                );

        this.userRepository.deleteAll()
            .thenMany(
                    Flux.just("user", "admin")
                    .flatMap(
                            username -> {
                                List<String> roles = "user".equals(username)
                                        ? Arrays.asList("ROLE_USER")
                                        : Arrays.asList("ROLE_USER", "ROLE_ADMIN");

                                User user = User.builder()
                                        .roles(roles)
                                        .username(username)
                                        .password(passwordEncoder.encode("password"))
                                        .email(username + "@auth.com")
                                        .build();
                                return this.userRepository.save(user);
                            }
                    )
            )
                .log()
                .subscribe(
                        null,
                        null,
                        () -> log.info("done users initialization...")
                );
    }
}
