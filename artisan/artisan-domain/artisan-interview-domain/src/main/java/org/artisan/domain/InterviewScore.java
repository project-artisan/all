package org.artisan.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewScore {

    @Column(name = "success_count")
    private int success;

    @Column(name = "pass_count")
    private int pass;

    @Column(name = "fail_count")
    private int fail;

    public void success(){
        this.success += 1;
    }

    public void fail(){
        this.fail += 1;
    }

    public void pass(){
        this.pass += 1;
    }

}

