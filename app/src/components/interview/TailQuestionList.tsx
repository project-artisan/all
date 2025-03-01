import { HoverCard, HoverCardContent, HoverCardTrigger } from "@/components/ui/hover-card";
import { Link as LinkIcon } from "lucide-react";
import { TailQuestion } from "@/types/interview";
import { getStatusBadge } from "@/utils/interviewUtils";

interface TailQuestionListProps {
  tailQuestions: TailQuestion[];
}

export function TailQuestionList({ tailQuestions }: TailQuestionListProps) {
  return (
    <div className="space-y-4">
      <h4 className="font-medium">꼬리 질문</h4>
      <div className="space-y-6 pl-4">
        {tailQuestions.map((tailQ) => (
          <div key={tailQ.tailQuestionId} className="space-y-4">
            <div className="flex items-start gap-2">
              {getStatusBadge(tailQ.answerState)}
              <HoverCard>
                <HoverCardTrigger asChild>
                  <div className="space-y-1">
                    <h3 className="font-medium">{tailQ.question}</h3>
                  </div>
                </HoverCardTrigger>
                <HoverCardContent className="w-80">
                  <div className="space-y-1">
                    <h4 className="text-sm font-semibold">질문 상세</h4>
                    <p className="text-sm text-muted-foreground">{tailQ.question}</p>
                  </div>
                </HoverCardContent>
              </HoverCard>
            </div>

            {tailQ.answer && (
              <HoverCard>
                <HoverCardTrigger asChild>
                  <div className="space-y-1.5 pl-4">
                    <h4 className="font-medium">답변</h4>
                    <p className="text-sm text-muted-foreground">{tailQ.answer}</p>
                  </div>
                </HoverCardTrigger>
                <HoverCardContent className="w-80">
                  <div className="space-y-1">
                    <h4 className="text-sm font-semibold">답변 상세</h4>
                    <p className="text-sm text-muted-foreground">{tailQ.answer}</p>
                  </div>
                </HoverCardContent>
              </HoverCard>
            )}

            {tailQ.feedback && (
              <HoverCard>
                <HoverCardTrigger asChild>
                  <div className="space-y-1.5 pl-4">
                    <h4 className="font-medium">피드백</h4>
                    <p className="text-sm text-muted-foreground">{tailQ.feedback}</p>
                  </div>
                </HoverCardTrigger>
                <HoverCardContent className="w-80">
                  <div className="space-y-1">
                    <h4 className="text-sm font-semibold">피드백 상세</h4>
                    <p className="text-sm text-muted-foreground">{tailQ.feedback}</p>
                  </div>
                </HoverCardContent>
              </HoverCard>
            )}

            {tailQ.referenceLinks?.length > 0 && (
              <div className="space-y-1.5 pl-4">
                <h4 className="font-medium">참고 자료</h4>
                <div className="space-y-2">
                  {tailQ.referenceLinks.map((link, index) => (
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
          </div>
        ))}
      </div>
    </div>
  );
}
