import { useNavigate } from 'react-router-dom';
import { format } from 'date-fns';
import { useInterviewResults } from '@/hooks/useInterviewResults';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import {
  Pagination,
  PaginationContent,
  PaginationEllipsis,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "@/components/ui/pagination";
import { Loader2, FileText, PlayCircle, Calendar, Clock, Hash, CheckCircle2, XCircle } from "lucide-react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle, CardFooter } from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";
import { Progress } from "@/components/ui/progress";

export default function InterviewResults() {
  const navigate = useNavigate();
  const { results, pageInfo, isLoading, error, fetchPage } = useInterviewResults(0, 10);

  const handlePageChange = (page: number) => {
    fetchPage(page);
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-[calc(100vh-4rem)]">
        <Loader2 className="h-8 w-8 animate-spin text-primary" />
      </div>
    );
  }

  if (error) {
    return (
      <div className="container py-10">
        <Card className="border-destructive">
          <CardHeader>
            <CardTitle className="text-destructive">오류가 발생했습니다</CardTitle>
            <CardDescription>{error.message}</CardDescription>
          </CardHeader>
        </Card>
      </div>
    );
  }

  return (
    <div className="container py-10 space-y-8">
      <div>
        <h1 className="text-3xl font-bold tracking-tight">면접 결과</h1>
        <p className="text-muted-foreground mt-2">
          지난 면접 연습 결과를 확인하고 개선할 점을 파악해보세요.
        </p>
      </div>

      <div className="flex-1 space-y-4 px-4 md:px-6">
        <div className="text-sm text-muted-foreground mb-4">
          총 {pageInfo?.totalElements || 0}개의 면접이 있습니다.
        </div>
        
        {results.length === 0 ? (
          <Card className="bg-muted/50">
            <CardContent className="flex flex-col items-center justify-center py-10 text-muted-foreground">
              <FileText className="h-12 w-12 mb-4" />
              <p>아직 면접 기록이 없습니다.</p>
            </CardContent>
          </Card>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {results.map((result) => (
              <Card key={result.interviewId} className="group hover:border-primary/50 transition-colors">
                <CardHeader>
                  <div className="flex justify-between items-start">
                    <CardTitle className="text-lg line-clamp-1">{result.title}</CardTitle>
                    <Badge 
                      variant={result.interviewStatus === 'DONE' ? 'default' : 'secondary'}
                      className="capitalize"
                    >
                      {result.interviewStatus === 'DONE' ? '완료' : '진행 중'}
                    </Badge>
                  </div>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div className="space-y-3">
                    <div className="flex items-center text-sm text-muted-foreground">
                      <Hash className="h-4 w-4 mr-2" />
                      <span>문제 {result.questionCount}개</span>
                    </div>
                    <div className="flex items-center text-sm text-muted-foreground">
                      <Calendar className="h-4 w-4 mr-2" />
                      <span>생성: {format(new Date(result.createdAt), 'yyyy-MM-dd')}</span>
                    </div>
                    <div className="flex items-center text-sm text-muted-foreground">
                      <Clock className="h-4 w-4 mr-2" />
                      <span>수정: {format(new Date(result.updatedAt), 'yyyy-MM-dd')}</span>
                    </div>
                  </div>

                  {/* 제출 및 통과 현황 */}
                  <div className="space-y-3 pt-2">
                    <div className="space-y-1.5">
                      <div className="flex justify-between items-center text-sm">
                        <div className="flex items-center">
                          <CheckCircle2 className="h-4 w-4 mr-2 text-green-500" />
                          <span>통과한 문제</span>
                        </div>
                        <span className="font-medium">0/0</span>
                      </div>
                      <Progress value={0} className="h-2" />
                    </div>
                    <div className="space-y-1.5">
                      <div className="flex justify-between items-center text-sm">
                        <div className="flex items-center">
                          <XCircle className="h-4 w-4 mr-2 text-red-500" />
                          <span>실패한 문제</span>
                        </div>
                        <span className="font-medium">0/0</span>
                      </div>
                      <Progress value={0} className="h-2" />
                    </div>
                  </div>
                </CardContent>
                <Separator className="my-2" />
                <CardFooter>
                  <Button
                    variant="ghost"
                    className="w-full justify-center group-hover:bg-primary group-hover:text-primary-foreground transition-colors"
                    onClick={() => navigate(
                      result.interviewStatus === 'DONE' 
                        ? `/interview/${result.interviewId}/detail`
                        : `/interview/${result.interviewId}`
                    )}
                  >
                    {result.interviewStatus === 'DONE' ? (
                      <>
                        <FileText className="mr-2 h-4 w-4" />
                        결과 보기
                      </>
                    ) : (
                      <>
                        <PlayCircle className="mr-2 h-4 w-4" />
                        계속하기
                      </>
                    )}
                  </Button>
                </CardFooter>
              </Card>
            ))}
          </div>
        )}

        {pageInfo && pageInfo.totalPages > 1 && (
          <div className="mt-8 flex justify-center">
            <Pagination>
              <PaginationContent>
                <PaginationItem>
                  <PaginationPrevious 
                    onClick={() => handlePageChange(pageInfo.number - 1)}
                    disabled={pageInfo.number === 0}
                  />
                </PaginationItem>
                
                {[...Array(pageInfo.totalPages)].map((_, index) => {
                  if (
                    index === 0 ||
                    index === pageInfo.totalPages - 1 ||
                    Math.abs(index - pageInfo.number) <= 1
                  ) {
                    return (
                      <PaginationItem key={index}>
                        <PaginationLink
                          onClick={() => handlePageChange(index)}
                          isActive={pageInfo.number === index}
                        >
                          {index + 1}
                        </PaginationLink>
                      </PaginationItem>
                    );
                  } else if (
                    index === 1 ||
                    index === pageInfo.totalPages - 2
                  ) {
                    return (
                      <PaginationItem key={index}>
                        <PaginationEllipsis />
                      </PaginationItem>
                    );
                  }
                  return null;
                })}

                <PaginationItem>
                  <PaginationNext
                    onClick={() => handlePageChange(pageInfo.number + 1)}
                    disabled={pageInfo.number === pageInfo.totalPages - 1}
                  />
                </PaginationItem>
              </PaginationContent>
            </Pagination>
          </div>
        )}
      </div>
    </div>
  );
}
