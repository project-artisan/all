import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Progress } from "@/components/ui/progress";
import { HoverCard, HoverCardContent, HoverCardTrigger } from "@/components/ui/hover-card";
import { CheckCircle2, XCircle, AlertCircle } from "lucide-react";

interface InterviewStatsProps {
  totalQuestions: number;
  answeredQuestions: number;
  passedQuestions: number;
  failedQuestions: number;
  skippedQuestions: number;
}

export function InterviewStats({
  totalQuestions,
  answeredQuestions,
  passedQuestions,
  failedQuestions,
  skippedQuestions
}: InterviewStatsProps) {
  return (
    <Card className="overflow-hidden border shadow-lg transition-all duration-300 bg-card hover:shadow-xl">
      <CardHeader className="pb-4 bg-muted/50">
        <CardTitle className="flex items-center text-2xl">
          전체 통계
          <Badge variant="secondary" className="ml-3">
            {answeredQuestions}/{totalQuestions} 완료
          </Badge>
        </CardTitle>
      </CardHeader>
      <CardContent className="space-y-6">
        <div className="grid gap-6">
          <StatItem
            icon={<CheckCircle2 className="w-5 h-5 mr-2 text-green-500" />}
            label="통과한 문제"
            count={passedQuestions}
            total={totalQuestions}
            color="text-green-500"
            progressColor="bg-green-100"
            progressValue={(passedQuestions / totalQuestions) * 100}
          />
          <StatItem
            icon={<XCircle className="w-5 h-5 mr-2 text-red-500" />}
            label="실패한 문제"
            count={failedQuestions}
            total={totalQuestions}
            color="text-red-500"
            progressColor="bg-red-100"
            progressValue={(failedQuestions / totalQuestions) * 100}
          />
          <StatItem
            icon={<AlertCircle className="w-5 h-5 mr-2 text-yellow-500" />}
            label="건너뛴 문제"
            count={skippedQuestions}
            total={totalQuestions}
            color="text-yellow-500"
            progressColor="bg-yellow-100"
            progressValue={(skippedQuestions / totalQuestions) * 100}
          />
        </div>
      </CardContent>
    </Card>
  );
}

interface StatItemProps {
  icon: React.ReactNode;
  label: string;
  count: number;
  total: number;
  color: string;
  progressColor: string;
  progressValue: number;
}

function StatItem({ icon, label, count, total, color, progressColor, progressValue }: StatItemProps) {
  return (
    <HoverCard>
      <HoverCardTrigger asChild>
        <div className="space-y-3 cursor-help">
          <div className="flex justify-between text-sm">
            <span className="flex items-center font-medium">
              {icon}
              {label}
            </span>
            <span className={`font-bold ${color}`}>
              {count}/{total}
            </span>
          </div>
          <Progress
            value={progressValue}
            className={`h-3 ${progressColor} rounded-full transition-all duration-500 ease-in-out`}
          />
        </div>
      </HoverCardTrigger>
      <HoverCardContent className="w-80">
        <div className="flex justify-between space-x-4">
          <div className="space-y-1">
            <h4 className="text-sm font-semibold">통계 상세</h4>
            <p className="text-sm text-muted-foreground">
              전체 {total}문제 중 {count}문제가 {label.replace(" 문제", "")}습니다.
            </p>
          </div>
        </div>
      </HoverCardContent>
    </HoverCard>
  );
}
