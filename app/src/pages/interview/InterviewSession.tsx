import { useState, useRef, useEffect, KeyboardEvent } from 'react';
import { Button } from '@/components/ui/button';
import { Progress } from '@/components/ui/progress';
import { Textarea } from '@/components/ui/textarea';
import { Badge } from '@/components/ui/badge';
import { useParams } from 'react-router-dom';
import { InterviewQuestionResponse, InterviewSubmitRequest, AnswerState, OngoingInterview } from '@/types/interview';
import { mockOngoingInterviews } from '@/types/interview';
import { Computer, User } from 'lucide-react';

interface QuestionListItem extends InterviewQuestionResponse {
  status: 'not-started' | 'in-progress' | 'completed' | 'skipped';
  isFollowUp?: boolean;
}

interface ChatMessage {
  id: number;
  type: 'question' | 'answer';
  content: string;
  isFollowUp?: boolean;
  timestamp: number;
}

export default function InterviewSession() {
  const { interviewId } = useParams();
  const [currentQuestion, setCurrentQuestion] = useState<InterviewQuestionResponse>();
  const [answer, setAnswer] = useState('');
  const [timeElapsed, setTimeElapsed] = useState(0);
  const [isThinking, setIsThinking] = useState(false);
  const [chatMessages, setChatMessages] = useState<ChatMessage[]>([]);
  const [interview, setInterview] = useState<OngoingInterview | undefined>();

  // Load interview data
  useEffect(() => {
    if (interviewId) {
      const foundInterview = mockOngoingInterviews.find(i => i.id === Number(interviewId));
      if (foundInterview) {
        setInterview(foundInterview);
        // Initialize chat messages from previous answers
        const initialMessages: ChatMessage[] = [];
        foundInterview.answers.forEach((answer, index) => {
          if (index > 0) {
            initialMessages.push({
              id: answer.questionId * 2 - 1,
              type: 'question',
              content: foundInterview.questionSet.questions?.[index]?.content || '',
              isFollowUp: index > 0,
              timestamp: Date.now() - (foundInterview.answers.length - index) * 60000
            });
          }
          initialMessages.push({
            id: answer.questionId * 2,
            type: 'answer',
            content: answer.content,
            timestamp: new Date(answer.timestamp).getTime()
          });
        });
        
        // Add current question
        if (foundInterview.progress.currentQuestionIndex < foundInterview.progress.totalQuestions) {
          initialMessages.push({
            id: foundInterview.progress.currentQuestionIndex * 2 + 1,
            type: 'question',
            content: foundInterview.questionSet.questions?.[foundInterview.progress.currentQuestionIndex]?.content || '',
            timestamp: Date.now()
          });
        }
        
        setChatMessages(initialMessages);
      }
    }
  }, [interviewId]);

  // Update current question when interview changes
  useEffect(() => {
    if (interview && interview.questionSet.questions) {
      const currentQ = interview.questionSet.questions[interview.progress.currentQuestionIndex];
      if (currentQ) {
        setCurrentQuestion({
          interviewId: interview.id,
          interviewQuestionId: Number(currentQ.id),
          question: currentQ.content,
          index: interview.progress.currentQuestionIndex + 1,
          size: interview.progress.totalQuestions,
          remainTailQuestionCount: currentQ.followUps?.length || 0
        });
      }
    }
  }, [interview]);

  const textareaRef = useRef<HTMLTextAreaElement>(null);

  const progress = currentQuestion ? (currentQuestion.index / currentQuestion.size) * 100 : 0;

  const handleSubmit = async () => {
    if (!currentQuestion || !interviewId) return;

    const submitRequest: InterviewSubmitRequest = {
      interviewId: parseInt(interviewId),
      interviewQuestionId: currentQuestion.interviewQuestionId,
      currentIndex: currentQuestion.index,
      answerState: AnswerState.COMPLETE,
      aiFeedback: '', // Will be filled by backend
      tailQuestion: '', // Will be filled by backend
      timeToAnswer: timeElapsed,
      answerContent: answer,
      score: 0, // Will be filled by backend
      referenceLinks: [] // Will be filled by backend
    };

    // TODO: Submit answer and handle response
  };

  const canNavigateToQuestion = (targetQuestion: QuestionListItem) => {
    // 현재 진행 중인 문제는 이동 가능
    if (currentQuestion?.interviewQuestionId === targetQuestion.interviewQuestionId) return true;
    // 완료된 문제만 이동 가능
    return targetQuestion.status === 'completed';
  };

  const getStatusColor = (status: QuestionListItem['status']) => {
    switch (status) {
      case 'completed':
        return 'bg-green-100 text-green-700 dark:bg-green-900 dark:text-green-300';
      case 'in-progress':
        return 'bg-blue-100 text-blue-700 dark:bg-blue-900 dark:text-blue-300';
      case 'not-started':
        return 'bg-gray-100 text-gray-700 dark:bg-gray-800 dark:text-gray-300';
      default:
        return 'bg-gray-100 text-gray-700 dark:bg-gray-800 dark:text-gray-300';
    }
  };

  const handleKeyDown = (e: KeyboardEvent<HTMLTextAreaElement>) => {
    // Submit - CMD + Enter
    if (e.metaKey && e.key === 'Enter') {
      e.preventDefault();
      handleSubmit();
    }
    // Pass - CMD + P
    if (e.metaKey && e.key === 'p') {
      e.preventDefault();
      handlePass();
    }
    // End Interview - CMD + Q
    if (e.metaKey && e.key === 'q') {
      e.preventDefault();
      handleEndInterview();
    }
  };

  const handleTextareaChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    const textarea = e.target;
    textarea.style.height = 'auto';
    textarea.style.height = Math.min(textarea.scrollHeight, 96) + 'px'; // Max 4 lines (24px per line)
    setAnswer(e.target.value);
  };

  const handlePass = () => {
    // TODO: Implement pass logic
    console.log('Question passed');
  };

  const handleEndInterview = () => {
    // TODO: Implement end interview logic
    console.log('Interview ended');
  };

  return (
    <div className="flex h-full gap-6">
      {/* Question List Sidebar */}
      <div className="w-80 h-full overflow-y-auto border-r pr-4">
        <div className="space-y-2">
          <h2 className="text-lg font-semibold mb-4">Questions</h2>
          {interview && interview.questionSet.questions.map((q, index) => (
            <div
              key={q.id}
              className={`p-3 rounded-lg transition-colors ${getStatusColor(index < interview.progress.currentQuestionIndex ? 'completed' : index === interview.progress.currentQuestionIndex ? 'in-progress' : 'not-started')} 
                ${currentQuestion?.interviewQuestionId === Number(q.id) ? 'ring-2 ring-primary' : ''}
                ${canNavigateToQuestion({ ...q, status: index < interview.progress.currentQuestionIndex ? 'completed' : index === interview.progress.currentQuestionIndex ? 'in-progress' : 'not-started' }) ? 'cursor-pointer hover:opacity-80' : 'opacity-50 cursor-not-allowed'}`}
              onClick={() => canNavigateToQuestion({ ...q, status: index < interview.progress.currentQuestionIndex ? 'completed' : index === interview.progress.currentQuestionIndex ? 'in-progress' : 'not-started' }) && setCurrentQuestion({ ...q, interviewId: interview.id, interviewQuestionId: Number(q.id), index: index + 1, size: interview.progress.totalQuestions, remainTailQuestionCount: q.followUps?.length || 0 })}
            >
              <div className="flex items-center justify-between mb-2">
                <span className="text-sm font-medium">Question {index + 1}</span>
                <Badge variant="outline" className="text-xs">
                  {index < interview.progress.currentQuestionIndex ? 'completed' : index === interview.progress.currentQuestionIndex ? 'in-progress' : 'not-started'}
                </Badge>
              </div>
              <p className="text-sm line-clamp-2">{q.content}</p>
              {q.followUps?.length > 0 && (
                <div className="mt-2 text-xs opacity-70">
                  {q.followUps.length} follow-up questions
                </div>
              )}
            </div>
          ))}
        </div>
      </div>

      {/* Main Content */}
      <div className="flex-1 flex flex-col h-full">
        {/* Progress Bar */}
        <div className="space-y-2 p-4 border-b">
          <div className="flex justify-between text-sm text-muted-foreground">
            <span>Question {currentQuestion?.index} of {currentQuestion?.size}</span>
            <div className="flex items-center gap-4">
              <Badge variant={isThinking ? 'secondary' : 'default'}>
                {isThinking ? 'Thinking Time' : 'Answering'}
              </Badge>
              <span>{Math.floor(timeElapsed / 60)}:{(timeElapsed % 60).toString().padStart(2, '0')}</span>
            </div>
          </div>
          <Progress value={progress} className="h-2" />
        </div>

        {/* Chat Area */}
        <div className="flex-1 overflow-y-auto p-4 space-y-4">
          {chatMessages.map((message) => (
            <div
              key={message.id}
              className={`flex gap-2 max-w-[80%] ${
                message.type === 'answer' ? 'ml-auto flex-row-reverse' : ''
              }`}
            >
              {message.type === 'question' ? (
                <>
                  <div className="w-10 h-10 shrink-0 rounded-full bg-primary flex items-center justify-center text-primary-foreground">
                    <Computer className="w-6 h-6" />
                  </div>
                  <div className="flex flex-col gap-2">
                    <div className={`p-4 rounded-2xl rounded-tl-none ${
                      message.isFollowUp ? 'bg-muted/50' : 'bg-muted'
                    }`}>
                      <p className={`${message.isFollowUp ? 'text-sm text-muted-foreground' : 'text-base'}`}>
                        {message.content}
                      </p>
                    </div>
                  </div>
                </>
              ) : (
                <>
                  <div className="flex flex-col items-end gap-2">
                    <div className="bg-primary p-4 rounded-2xl rounded-tr-none">
                      <p className="text-primary-foreground">{message.content}</p>
                    </div>
                  </div>
                  <div className="w-10 h-10 shrink-0 rounded-full bg-secondary flex items-center justify-center">
                    <User className="w-6 h-6" />
                  </div>
                </>
              )}
            </div>
          ))}

          {/* Current Answer Input (if thinking) */}
          {!isThinking && answer && (
            <div className="flex gap-2 max-w-[80%] ml-auto flex-row-reverse">
              <div className="w-10 h-10 shrink-0 rounded-full bg-secondary flex items-center justify-center">
                <User className="w-6 h-6" />
              </div>
              <div className="flex flex-col items-end gap-2">
                <div className="bg-primary/10 p-4 rounded-2xl rounded-tr-none">
                  <p className="text-primary-foreground">{answer}</p>
                </div>
              </div>
            </div>
          )}
        </div>

        {/* Answer Input Area */}
        <div className="p-4 border-t">
          <Textarea
            ref={textareaRef}
            value={answer}
            onChange={handleTextareaChange}
            onKeyDown={handleKeyDown}
            placeholder="Type your answer here... (CMD + Enter to submit, CMD + P to pass, CMD + Q to end)"
            className="min-h-[24px] max-h-[96px] resize-none"
          />
          <div className="flex justify-between mt-2">
            <div className="text-sm text-muted-foreground">
              <span>CMD + Enter to submit</span>
              <span className="mx-2">•</span>
              <span>CMD + P to pass</span>
              <span className="mx-2">•</span>
              <span>CMD + Q to end</span>
            </div>
            <Button onClick={handleSubmit}>Submit</Button>
          </div>
        </div>
      </div>
    </div>
  );
}
