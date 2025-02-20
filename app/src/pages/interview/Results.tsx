import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { Button } from '@/components/ui/button';
import { Calendar } from '@/components/ui/calendar';
import { Progress } from '@/components/ui/progress';
import { Badge } from '@/components/ui/badge';
import { Calendar as CalendarIcon, ChevronRight, Timer } from 'lucide-react';

// 임시 데이터
const mockResults = [
  {
    id: 1,
    date: new Date('2025-02-20'),
    category: '프론트엔드',
    questionSet: 'JavaScript 기초',
    totalQuestions: 15,
    correctAnswers: 12,
    duration: 25,
    score: 80,
  },
  {
    id: 2,
    date: new Date('2025-02-18'),
    category: '백엔드',
    questionSet: '데이터베이스 설계',
    totalQuestions: 20,
    correctAnswers: 15,
    duration: 35,
    score: 75,
  },
];

export default function InterviewResults() {
  const [date, setDate] = useState<Date | undefined>(new Date());
  const navigate = useNavigate();

  // 면접 이력이 있는 날짜들을 추출
  const interviewDates = mockResults.map(result => result.date);

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold tracking-tight">면접 결과</h1>
        <p className="text-muted-foreground mt-2">
          지난 면접 연습 결과를 확인하고 개선할 점을 파악해보세요.
        </p>
      </div>

      <div className="grid gap-4 md:grid-cols-[300px-1fr]">
        <Card>
          <CardHeader>
            <CardTitle>달력</CardTitle>
            <CardDescription>날짜별 면접 결과를 확인해보세요.</CardDescription>
          </CardHeader>
          <CardContent>
            <Calendar
              mode="single"
              selected={date}
              onSelect={setDate}
              className="rounded-md border"
              modifiers={{
                hasInterview: (dateToCheck) => interviewDates.some(interviewDate => interviewDate.toDateString() === dateToCheck.toDateString())
              }}
              modifiersClassNames={{
                hasInterview: 'relative after:absolute after:content-["•"] after:-bottom-2 after:left-1/2 after:-translate-x-1/2 after:text-blue-500 after:text-lg'
              }}
            />
          </CardContent>
        </Card>

        <div className="space-y-4">
          <Tabs defaultValue="recent">
            <TabsList>
              <TabsTrigger value="recent">최근 결과</TabsTrigger>
              <TabsTrigger value="best">최고 점수</TabsTrigger>
            </TabsList>

            <TabsContent value="recent" className="space-y-4">
              {mockResults.map(result => (
                <Card key={result.id} className="cursor-pointer hover:bg-accent/50 transition-colors"
                      onClick={() => navigate(`/interview/results/${result.id}`)}>
                  <CardHeader>
                    <div className="flex items-center justify-between">
                      <div className="space-y-1">
                        <div className="flex items-center gap-2">
                          <Badge>{result.category}</Badge>
                          <ChevronRight className="h-4 w-4 text-muted-foreground" />
                          <span className="font-semibold">{result.questionSet}</span>
                        </div>
                        <div className="flex items-center gap-2 text-sm text-muted-foreground">
                          <CalendarIcon className="h-4 w-4" />
                          <span>{result.date.toLocaleDateString()}</span>
                          <Timer className="ml-2 h-4 w-4" />
                          <span>{result.duration}분</span>
                        </div>
                      </div>
                      <div className="text-right">
                        <div className="text-2xl font-bold">{result.score}점</div>
                        <div className="text-sm text-muted-foreground">
                          {result.correctAnswers}/{result.totalQuestions} 정답
                        </div>
                      </div>
                    </div>
                  </CardHeader>
                  <CardContent>
                    <Progress value={result.score} className="h-2" />
                    <div className="mt-2 text-right text-sm text-muted-foreground">
                      정답률 {Math.round((result.correctAnswers / result.totalQuestions) * 100)}%
                    </div>
                  </CardContent>
                </Card>
              ))}
            </TabsContent>

            <TabsContent value="best">
              {/* 최고 점수 탭 내용 */}
            </TabsContent>
          </Tabs>

          <div className="text-center">
            <Button variant="outline">더 보기</Button>
          </div>
        </div>
      </div>
    </div>
  );
}
