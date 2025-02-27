package org.artisan.batch;

import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;

public class TechBlogProcessStep implements Step {
    @Override
    public String getName() {
        return "";
    }

    @Override
    public void execute(StepExecution stepExecution) throws JobInterruptedException {

    }
}
