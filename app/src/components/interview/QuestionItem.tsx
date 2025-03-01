import { HoverCard, HoverCardContent, HoverCardTrigger } from "@/components/ui/hover-card";
import { ChevronDown, ChevronUp, Link as LinkIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import { InterviewQuestion } from "@/types/interview";
import { getStatusBadge } from "@/utils/interviewUtils";
import { TailQuestionList } from "./TailQuestionList";

interface QuestionItemProps {
  question: InterviewQuestion;
  isExpanded: boolean;
  onToggle: () => void;
}

export function QuestionItem({ question, isExpanded, onToggle }: QuestionItemProps) {
  return (
    <div className="space-y-6">
      <div className="flex items-start justify-between gap-4">
        <div className="space-y-1.5">
          <div className="flex items-center gap-2">
            {getStatusBadge(question.answerState)}
            <span className="text-sm text-muted-foreground">
              점수: {question.score}점
            </span>
          </div>
          <h3 className="text-lg font-semibold">{question.question}</h3>
        </div>
        <Button
          variant="ghost"
          size="sm"
          onClick={onToggle}
          className="shrink-0"
        >
          {isExpanded ? (
            <ChevronUp className="h-4 w-4" />
          ) : (
            <ChevronDown className="h-4 w-4" />
          )}
        </Button>
      </div>

      {isExpanded && (
        <div className="space-y-4 pl-4">
          {question.answer && (
            <HoverCard>
              <HoverCardTrigger asChild>
                <div className="space-y-1.5">
                  <h4 className="font-medium">답변</h4>
                  <p className="text-sm text-muted-foreground">{question.answer}</p>
                </div>
              </HoverCardTrigger>
              <HoverCardContent className="w-80">
                <div className="space-y-1">
                  <h4 className="text-sm font-semibold">답변 상세</h4>
                  <p className="text-sm text-muted-foreground">{question.answer}</p>
                </div>
              </HoverCardContent>
            </HoverCard>
          )}

          {question.feedback && (
            <HoverCard>
              <HoverCardTrigger asChild>
                <div className="space-y-1.5">
                  <h4 className="font-medium">피드백</h4>
                  <p className="text-sm text-muted-foreground">{question.feedback}</p>
                </div>
              </HoverCardTrigger>
              <HoverCardContent className="w-80">
                <div className="space-y-1">
                  <h4 className="text-sm font-semibold">피드백 상세</h4>
                  <p className="text-sm text-muted-foreground">{question.feedback}</p>
                </div>
              </HoverCardContent>
            </HoverCard>
          )}

          {question.referenceLinks?.length > 0 && (
            <div className="space-y-1.5">
              <h4 className="font-medium">참고 자료</h4>
              <div className="space-y-2">
                {question.referenceLinks.map((link, index) => (
                  <a
                    key={index}
                    href={link}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="flex items-center text-sm text-blue-500 hover:underline"
                  >
                    <LinkIcon className="w-3 h-3 mr-1" />
                    {link}
                  </a>
                ))}
              </div>
            </div>
          )}

          {question.tailQuestions?.length > 0 && (
            <TailQuestionList tailQuestions={question.tailQuestions} />
          )}
        </div>
      )}
    </div>
  );
}
