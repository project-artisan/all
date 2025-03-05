package org.artisan.api;

import static org.artisan.core.TechBlogCode.BASE;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.artisan.attributes.Auth;
import org.artisan.core.TechBlogCode;
import org.artisan.core.User;
import org.artisan.payload.SearchTechBlogPostRequest;
import org.artisan.payload.SearchTechBlogPostResponse;
import org.artisan.payload.TechBlogCodeResponse;
import org.artisan.request.SearchRequest;
import org.artisan.service.TechBlogService;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/blogs")
public class TechBlogApi {

    private final TechBlogService techBlogService;


    @GetMapping
    Slice<SearchTechBlogPostResponse> getPosts(
            @ModelAttribute SearchTechBlogPostRequest searchTechBlogPostRequest,
            @ModelAttribute SearchRequest search
    ){
        return techBlogService.readAll(
                        searchTechBlogPostRequest.select(),
                        searchTechBlogPostRequest.title(),
                        search.toPageable()
                )
                .map(SearchTechBlogPostResponse::from);
    }

    @GetMapping("/tech")
    List<TechBlogCodeResponse> getBlogs(){
        return TechBlogCode.getAll()
                .stream()
                .filter(code -> code != BASE)
                .map(TechBlogCodeResponse::from)
                .toList();
    }

    @GetMapping("/read/{blogId}")
    public ResponseEntity<?> redirectBlogURL(
            @PathVariable ("blogId") Long blogId,
            @Auth User user
    ) throws URISyntaxException {

        var blog = techBlogService.read(user, blogId);

        URI redirectUri = new URI(blog.getBlogMetadata().blogLink().toUrl());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);

        return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
    }

}
