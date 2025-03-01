import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { interviewCategories } from '@/types/interview';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Play, BookOpen, Search, ChevronLeft, ChevronRight } from 'lucide-react';
import { Badge } from '@/components/ui/badge';
import { Input } from '@/components/ui/input';
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import { Label } from '@/components/ui/label';
import { Slider } from '@/components/ui/slider';
import { axiosInstance } from '@/lib/axios';

interface QuestionSet {
  questionSetId: number;
  description: string;
  title: string;
  count: number;
  tailQuestionDepth: number;
  thumbnailUrl: string;
}

interface PageInfo {
  empty: boolean;
  sorted: boolean;
  unsorted: boolean;
}

interface Pageable {
  offset: number;
  sort: PageInfo;
  unpaged: boolean;
  paged: boolean;
  pageNumber: number;
  pageSize: number;
}

interface QuestionSetResponse {
  content: QuestionSet[];
  last: boolean;
  number: number;
  size: number;
  sort: PageInfo;
  pageable: Pageable;
  numberOfElements: number;
  first: boolean;
  empty: boolean;
  totalPages: number;
  totalElements: number;
}

interface StartInterviewRequest {
  questionSetId: number;
  tailQuestionDepth: number;
  totalProblemCount: number;
  timeToAnswer: number;
  timeToThink: number;
}

interface StartInterviewResponse {
  interviewId: number;
}

const fetchQuestionSets = async (page: number): Promise<QuestionSetResponse> => {
  const { data } = await axiosInstance.get<QuestionSetResponse>('/api/v1/question-sets', {
    params: {
      page,
      size: 12
    }
  });
  return data;
};

const startInterview = async (request: StartInterviewRequest): Promise<StartInterviewResponse> => {
  const { data } = await axiosInstance.post<StartInterviewResponse>('/api/interviews', request);
  return data;
};

export default function AllQuestionSets() {
  const navigate = useNavigate();
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedQuestionCount, setSelectedQuestionCount] = useState(10);
  const [selectedSet, setSelectedSet] = useState<QuestionSet | null>(null);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [isStarting, setIsStarting] = useState(false);
  
  const {
    data,
    isLoading,
    isError
  } = useQuery({
    queryKey: ['questionSets', currentPage],
    queryFn: () => fetchQuestionSets(currentPage),
  });

  const questionSets = data?.content ?? [];
  const totalPages = data?.totalPages ?? 0;
  
  const filteredQuestionSets = searchQuery.trim() === '' 
    ? questionSets 
    : questionSets.filter((set: QuestionSet) => 
        set.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
        set.description.toLowerCase().includes(searchQuery.toLowerCase())
      );

  return (
    <div className="space-y-6">
      <div className="space-y-4">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">전체 면접 질문</h1>
          <p className="text-muted-foreground mt-2">
            모든 카테고리의 면접 질문들을 한눈에 살펴보세요.
          </p>
        </div>

        <div className="relative">
          <Search className="absolute left-2 top-2.5 h-4 w-4 text-muted-foreground" />
          <Input
            placeholder="질문 세트 검색..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="pl-8"
          />
        </div>
      </div>

      <div className="space-y-4">
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
          {isLoading ? (
            <div>Loading...</div>
          ) : isError ? (
            <div>Error fetching question sets</div>
          ) : (
            filteredQuestionSets.map((set) => (
              <Card key={set.questionSetId}>
                <CardHeader className="space-y-1">
                  <div className="flex items-center justify-between">
                    <div className="space-y-1">
                      <CardTitle className="text-2xl">{set.title}</CardTitle>
                    </div>
                    {set.thumbnailUrl && (
                      <img 
                        src={set.thumbnailUrl} 
                        alt={set.title}
                        className="w-8 h-8" 
                      />
                    )}
                  </div>
                  <CardDescription className="text-base">{set.description}</CardDescription>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div className="flex gap-2">
                    <Badge variant="secondary">
                      <BookOpen className="mr-1 h-3 w-3" />
                      {set.count}개 질문
                    </Badge>
                    <Badge variant="secondary">
                      깊이 {set.tailQuestionDepth}
                    </Badge>
                  </div>
                  <div className="flex gap-2">
                    <Button 
                      className="flex-1" 
                      onClick={() => {
                        setSelectedSet(set);
                        setIsDialogOpen(true);
                      }}
                    >
                      <Play className="mr-2 h-4 w-4" />
                      시작하기
                    </Button>
                  </div>
                </CardContent>
              </Card>
            ))
          )}
        </div>

        {/* Pagination */}
        {!isLoading && !isError && totalPages > 0 && (
          <div className="flex items-center justify-center space-x-2">
            <Button
              variant="outline"
              size="icon"
              onClick={() => setCurrentPage(prev => Math.max(0, prev - 1))}
              disabled={currentPage === 0}
            >
              <ChevronLeft className="h-4 w-4" />
            </Button>
            <div className="flex items-center gap-1">
              {Array.from({ length: totalPages }, (_, i) => (
                <Button
                  key={i}
                  variant={currentPage === i ? "default" : "outline"}
                  size="icon"
                  onClick={() => setCurrentPage(i)}
                >
                  {i + 1}
                </Button>
              ))}
            </div>
            <Button
              variant="outline"
              size="icon"
              onClick={() => setCurrentPage(prev => Math.min(totalPages - 1, prev + 1))}
              disabled={currentPage === totalPages - 1}
            >
              <ChevronRight className="h-4 w-4" />
            </Button>
          </div>
        )}
      </div>

      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>{selectedSet?.title}</DialogTitle>
            <DialogDescription>{selectedSet?.description}</DialogDescription>
          </DialogHeader>
          <div className="space-y-4">
            <div className="space-y-2">
              <Label>질문 개수</Label>
              <div className="flex items-center space-x-2">
                <Slider
                  value={[selectedQuestionCount]}
                  onValueChange={([value]) => setSelectedQuestionCount(value)}
                  max={selectedSet?.count || 20}
                  min={1}
                  step={1}
                />
                <span className="w-12 text-right">{selectedQuestionCount}</span>
              </div>
            </div>
            <Button 
              className="w-full" 
              disabled={isStarting}
              onClick={async () => {
                if (selectedSet) {
                  try {
                    setIsStarting(true);
                    const response = await startInterview({
                      questionSetId: selectedSet.questionSetId,
                      tailQuestionDepth: selectedSet.tailQuestionDepth,
                      totalProblemCount: selectedQuestionCount,
                      timeToAnswer: 300, // 기본값 5분
                      timeToThink: 60,   // 기본값 1분
                    });
                    navigate(`/interviews/${response.interviewId}`);
                  } catch (error) {
                    console.error('Failed to start interview:', error);
                  } finally {
                    setIsStarting(false);
                  }
                }
              }}
            >
              {isStarting ? (
                "면접 시작 중..."
              ) : (
                <>
                  <Play className="mr-2 h-4 w-4" />
                  면접 시작하기
                </>
              )}
            </Button>
          </div>
        </DialogContent>
      </Dialog>
    </div>
  );
}
