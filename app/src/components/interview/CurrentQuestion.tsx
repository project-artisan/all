import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Progress } from "@/components/ui/progress";
import { CurrentQuestion as CurrentQuestionType } from "@/hooks/useCurrentQuestion";

interface CurrentQuestionProps {
  question: CurrentQuestionType;
}

export function CurrentQuestion({ question }: CurrentQuestionProps) {
  const progress = (question.index / question.size) * 100;

  return (
    <Card className="h-full">
      <CardHeader>
        <div className="space-y-2">
          <div className="flex justify-between text-sm text-muted-foreground">
            <span>Question {question.index} of {question.size}</span>
          </div>
          <Progress value={progress} className="h-2" />
        </div>
      </CardHeader>
      <CardContent className="pt-6">
        <CardTitle className="text-xl mb-6">{question.question}</CardTitle>
        {question.remainTailQuestionCount > 0 && (
          <p className="text-sm text-muted-foreground">
            This question has {question.remainTailQuestionCount} follow-up questions
          </p>
        )}
      </CardContent>
    </Card>
  );
}
