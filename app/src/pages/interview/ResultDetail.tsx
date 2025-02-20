import { useParams } from 'react-router-dom';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Progress } from '@/components/ui/progress';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { Timer, Calendar as CalendarIcon, CheckCircle2, XCircle, MessageSquare, ArrowRight, BookOpen } from 'lucide-react';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';

// 임시 데이터
const mockResultDetail = {
  id: '1',
  date: new Date('2024-02-19'),
  category: '프론트엔드',
  questionSet: 'JavaScript 기초',
  totalQuestions: 15,
  correctAnswers: 12,
  duration: 25,
  score: 80,
  questions: [
    {
      id: 1,
      question: 'JavaScript에서 클로저(Closure)란 무엇인가요?',
      answer: '클로저는 함수와 그 함수가 선언된 렉시컬 환경의 조합입니다. 내부함수가 외부함수의 변수에 접근할 수 있는 것을 의미합니다.',
      userAnswer: '클로저는 함수가 선언될 때의 환경을 기억하는 함수입니다. 외부 함수의 변수를 참조할 수 있습니다.',
      isCorrect: true,
      feedback: '클로저의 핵심 개념을 잘 이해하고 있습니다. 실제 사용 사례를 들어 설명하면 더 좋았을 것 같습니다.',
      duration: 120,
      tags: ['JavaScript', '스코프', '함수']
    },
    {
      id: 2,
      question: 'React의 가상 DOM(Virtual DOM)이 무엇이며 어떤 장점이 있나요?',
      answer: '가상 DOM은 실제 DOM의 가벼운 복사본입니다. React는 가상 DOM을 사용하여 실제 DOM 업데이트를 최적화하고 성능을 향상시킵니다.',
      userAnswer: '가상 DOM은 메모리에 있는 DOM의 복사본입니다.',
      isCorrect: false,
      feedback: '가상 DOM의 개념은 이해했으나, 구체적인 작동 방식과 장점에 대한 설명이 부족합니다.',
      duration: 90,
      tags: ['React', '성능 최적화']
    }
  ]
};

export default function ResultDetail() {
  const { resultId } = useParams();

  return (
    <div className="space-y-6">
      <div className="flex items-start justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">면접 결과 상세</h1>
          <div className="flex items-center gap-2 mt-2 text-muted-foreground">
            <CalendarIcon className="h-4 w-4" />
            <span>{mockResultDetail.date.toLocaleDateString()}</span>
            <Timer className="ml-2 h-4 w-4" />
            <span>{mockResultDetail.duration}분</span>
          </div>
        </div>
        <div className="text-right">
          <div className="text-3xl font-bold">{mockResultDetail.score}점</div>
          <div className="text-sm text-muted-foreground">
            {mockResultDetail.correctAnswers}/{mockResultDetail.totalQuestions} 정답
          </div>
        </div>
      </div>

      <Card>
        <CardHeader>
          <div className="flex items-center gap-2">
            <Badge>{mockResultDetail.category}</Badge>
            <ArrowRight className="h-4 w-4 text-muted-foreground" />
            <span className="font-semibold">{mockResultDetail.questionSet}</span>
          </div>
        </CardHeader>
        <CardContent>
          <div className="space-y-2">
            <Progress value={mockResultDetail.score} className="h-2" />
            <div className="flex justify-between text-sm">
              <div className="flex items-center gap-4">
                <div className="flex items-center gap-1">
                  <CheckCircle2 className="h-4 w-4 text-green-500" />
                  <span>{mockResultDetail.correctAnswers} 정답</span>
                </div>
                <div className="flex items-center gap-1">
                  <XCircle className="h-4 w-4 text-red-500" />
                  <span>{mockResultDetail.totalQuestions - mockResultDetail.correctAnswers} 오답</span>
                </div>
              </div>
              <div>정답률 {Math.round((mockResultDetail.correctAnswers / mockResultDetail.totalQuestions) * 100)}%</div>
            </div>
          </div>
        </CardContent>
      </Card>

      <Tabs defaultValue="all" className="space-y-4">
        <TabsList>
          <TabsTrigger value="all">전체 질문</TabsTrigger>
          <TabsTrigger value="correct">정답</TabsTrigger>
          <TabsTrigger value="incorrect">오답</TabsTrigger>
        </TabsList>

        <TabsContent value="all" className="space-y-4">
          {mockResultDetail.questions.map((q, index) => (
            <Card key={q.id}>
              <CardHeader>
                <div className="flex items-start justify-between">
                  <div className="space-y-1">
                    <div className="flex items-center gap-2">
                      <Avatar className="h-8 w-8">
                        <AvatarFallback>{index + 1}</AvatarFallback>
                      </Avatar>
                      <div className="font-semibold">{q.question}</div>
                    </div>
                    <div className="flex gap-1">
                      {q.tags.map(tag => (
                        <Badge key={tag} variant="outline">{tag}</Badge>
                      ))}
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <Timer className="h-4 w-4 text-muted-foreground" />
                    <span className="text-sm text-muted-foreground">{q.duration}초</span>
                    {q.isCorrect ? (
                      <CheckCircle2 className="h-5 w-5 text-green-500" />
                    ) : (
                      <XCircle className="h-5 w-5 text-red-500" />
                    )}
                  </div>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="space-y-2">
                  <div className="flex items-start gap-2">
                    <BookOpen className="h-5 w-5 text-muted-foreground mt-0.5" />
                    <div>
                      <div className="font-medium">모범 답안</div>
                      <div className="text-sm text-muted-foreground">{q.answer}</div>
                    </div>
                  </div>
                  <div className="flex items-start gap-2">
                    <MessageSquare className="h-5 w-5 text-muted-foreground mt-0.5" />
                    <div>
                      <div className="font-medium">내 답변</div>
                      <div className="text-sm text-muted-foreground">{q.userAnswer}</div>
                    </div>
                  </div>
                </div>
                <Card className="bg-muted">
                  <CardContent className="p-4">
                    <div className="font-medium">피드백</div>
                    <div className="text-sm text-muted-foreground">{q.feedback}</div>
                  </CardContent>
                </Card>
              </CardContent>
            </Card>
          ))}
        </TabsContent>

        <TabsContent value="correct" className="space-y-4">
          {mockResultDetail.questions.filter(q => q.isCorrect).map((q, index) => (
            // 동일한 카드 컴포넌트
            <Card key={q.id}>
              {/* ... 카드 내용 ... */}
            </Card>
          ))}
        </TabsContent>

        <TabsContent value="incorrect" className="space-y-4">
          {mockResultDetail.questions.filter(q => !q.isCorrect).map((q, index) => (
            // 동일한 카드 컴포넌트
            <Card key={q.id}>
              {/* ... 카드 내용 ... */}
            </Card>
          ))}
        </TabsContent>
      </Tabs>

      <div className="flex justify-end gap-2">
        <Button variant="outline">다시 풀기</Button>
        <Button>연관 문제 풀기</Button>
      </div>
    </div>
  );
}
