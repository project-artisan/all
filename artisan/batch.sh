#!/bin/bash
# 사용법: ./script.sh <job> <techBlogCode> <requestDate>
# 예: ./script.sh "WebCrawlerBatchConfiguration" 111 2025-02-27

# 인자값 할당 (인자가 없으면 기본값 사용)
code=${1}
date=${2}
job="${3:-WebCrawlerBatchJob}"

echo "job  : ${job}"
echo "code : ${code}"
echo "date : ${date}"


# 두 번째 명령어 (인자값 적용)
./gradlew artisan-batch:artisan-techblog-batch:bootRun \
    --args="--spring.batch.job.name=${job} \
            --spring.profiles.active=local-dev \
            techBlogCode=${code} \
            requestDate=${date}"


