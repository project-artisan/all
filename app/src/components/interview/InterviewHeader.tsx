import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { ArrowLeft } from "lucide-react";
import { InterviewState } from "@/types/interview";

interface InterviewHeaderProps {
  title: string;
  interviewState: InterviewState;
}

export function InterviewHeader({ title, interviewState }: InterviewHeaderProps) {
  return (
    <div className="flex items-center justify-between">
      <div className="space-y-2">
        <Button 
          variant="ghost" 
          onClick={() => window.history.back()} 
          className="mb-2 -ml-4 text-muted-foreground hover:text-foreground"
        >
          <ArrowLeft className="w-4 h-4 mr-2" />
          돌아가기
        </Button>
        <h1 className="text-4xl font-bold tracking-tight">{title}</h1>
        <div className="flex items-center gap-3">
          <p className="text-lg text-muted-foreground">
            면접 결과를 확인하고 개선할 점을 파악해보세요.
          </p>
          <Badge variant={
            interviewState === 'COMPLETED' ? 'default' :
            interviewState === 'FAILED' ? 'destructive' : 'secondary'
          }>
            {interviewState === 'COMPLETED' ? '완료' :
             interviewState === 'FAILED' ? '실패' : '진행 중'}
          </Badge>
        </div>
      </div>
    </div>
  );
}
