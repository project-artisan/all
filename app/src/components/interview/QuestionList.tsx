import { InterviewQuestion } from '@/hooks/useInterview';

interface QuestionListProps {
  questions: InterviewQuestion[];
  onQuestionSelect?: (question: InterviewQuestion) => void;
}

export function QuestionList({ questions, onQuestionSelect }: QuestionListProps) {
  return (
    <div className="w-80 h-full overflow-y-auto border-r p-4">
      <h2 className="text-lg font-semibold mb-4">Questions</h2>
      <div className="space-y-3">
        {questions.map((question, index) => (
          <QuestionItem 
            key={question.interviewQuestionId} 
            question={question} 
            index={index}
            onClick={() => onQuestionSelect?.(question)}
          />
        ))}
      </div>
    </div>
  );
}

interface QuestionItemProps {
  question: InterviewQuestion;
  index: number;
  onClick?: () => void;
}

function QuestionItem({ question, index, onClick }: QuestionItemProps) {
  return (
    <div
      onClick={onClick}
      className="p-4 rounded-lg bg-muted hover:bg-muted/80 cursor-pointer transition-colors"
    >
      <div className="flex justify-between items-center mb-2">
        <span className="font-medium">Question {index + 1}</span>
        <span className="text-sm text-muted-foreground">
          {question.answerState}
        </span>
      </div>
      <p className="text-sm line-clamp-2">{question.question}</p>
      {question.remainTailQuestionCount > 0 && (
        <div className="mt-2 text-xs text-muted-foreground">
          +{question.remainTailQuestionCount} follow-up questions
        </div>
      )}
    </div>
  );
}
