package org.artisan.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRequest {

    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;

    public long getOffset() {
        return (long) (page - 1) * size;
    }

    public Pageable toPageable(){
        return PageRequest.of(page, size);
    }
}
