import { useState } from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Progress } from "@/components/ui/progress";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { PlayCircle, PauseCircle, Timer, ArrowRight } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { OngoingInterview, mockOngoingInterviews } from '@/types/interview';

export default function OngoingInterviews() {
  const navigate = useNavigate();
  const [interviews, setInterviews] = useState<OngoingInterview[]>(mockOngoingInterviews);

  const formatTime = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' });
  };

  const getStatusColor = (status: OngoingInterview['status']) => {
    return status === 'in-progress' ? 'bg-green-100 text-green-700' : 'bg-yellow-100 text-yellow-700';
  };

  const getStatusText = (status: OngoingInterview['status']) => {
    return status === 'in-progress' ? '진행중' : '일시정지';
  };

  const handleContinue = (interviewId: number) => {
    navigate(`/interview/session/${interviewId}`);
  };

  return (
    <div className="container mx-auto py-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">진행중인 면접</h1>
      </div>
      
      <div className="grid gap-4">
        {interviews.map((interview) => (
          <Card key={interview.id} className="hover:bg-accent/5">
            <CardHeader>
              <div className="flex justify-between items-start">
                <div>
                  <CardTitle className="text-xl">{interview.title}</CardTitle>
                  <CardDescription className="mt-1">
                    <span className="font-medium">{interview.category.title}</span>
                    <span className="mx-2">•</span>
                    <span>{interview.questionSet.title}</span>
                  </CardDescription>
                </div>
                <Badge className={getStatusColor(interview.status)}>
                  {interview.status === 'in-progress' ? (
                    <PlayCircle className="w-4 h-4 mr-1" />
                  ) : (
                    <PauseCircle className="w-4 h-4 mr-1" />
                  )}
                  {getStatusText(interview.status)}
                </Badge>
              </div>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                <div>
                  <div className="flex justify-between text-sm text-muted-foreground mb-2">
                    <span>진행률 {Math.round((interview.progress.completedQuestions / interview.progress.totalQuestions) * 100)}%</span>
                    <span>{interview.progress.completedQuestions} / {interview.progress.totalQuestions} 문제</span>
                  </div>
                  <Progress 
                    value={(interview.progress.completedQuestions / interview.progress.totalQuestions) * 100} 
                    className="h-2" 
                  />
                </div>
                <div className="flex items-center justify-between">
                  <div className="flex flex-col gap-1 text-sm text-muted-foreground">
                    <div className="flex items-center">
                      <Timer className="w-4 h-4 mr-1" />
                      시작: {formatTime(interview.startedAt)}
                    </div>
                    <div className="flex items-center">
                      <ArrowRight className="w-4 h-4 mr-1" />
                      마지막 응답: {formatTime(interview.lastUpdated)}
                    </div>
                  </div>
                  <Button onClick={() => handleContinue(interview.id)}>
                    면접 계속하기
                  </Button>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
}
