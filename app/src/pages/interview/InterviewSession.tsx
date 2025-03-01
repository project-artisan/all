import { useParams } from 'react-router-dom';
import { useState, useRef } from 'react';
import { useInterview, InterviewQuestion } from '@/hooks/useInterview';
import { useCurrentQuestion } from '@/hooks/useCurrentQuestion';
import { QuestionList } from '@/components/interview/QuestionList';
import { ChatMessage } from '@/components/interview/ChatMessage';
import { ChatInput } from '@/components/interview/ChatInput';
import { Card } from '@/components/ui/card';
import { Progress } from '@/components/ui/progress';
import axios from 'axios';

interface SubmitResponse {
  data: {
    tailQuestionId: number | null;
    question: string;
  }
}

interface ChatMessageType {
  id: number;
  type: 'question' | 'answer' | 'system';
  content: string;
  timestamp: number;
}

export default function InterviewSession() {
  const { interviewId } = useParams();
  const { interview, isLoading: isLoadingInterview, error: interviewError } = useInterview(interviewId);
  const { 
    currentQuestion, 
    isLoading: isLoadingQuestion, 
    error: questionError,
    refetch: refetchQuestion 
  } = useCurrentQuestion(interviewId);

  const [chatMessages, setChatMessages] = useState<ChatMessageType[]>([]);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [currentTailQuestionId, setCurrentTailQuestionId] = useState<number | null>(null);
  const chatInputRef = useRef<HTMLTextAreaElement>(null);
  
  const handleQuestionSelect = (question: InterviewQuestion) => {
    // TODO: 질문 선택 처리
    console.log('Selected question:', question);
  };

  const clearChat = () => {
    setChatMessages([]);
  };

  const addMessage = (message: Omit<ChatMessageType, 'id' | 'timestamp'>) => {
    setChatMessages(prev => [...prev, {
      ...message,
      id: Date.now(),
      timestamp: Date.now()
    }]);
  };

  const handleSubmitAnswer = async (content: string) => {
    if (!currentQuestion || !interviewId) return;

    setIsSubmitting(true);
    addMessage({ type: 'answer', content });
    addMessage({ type: 'system', content: '답변을 평가하는 중입니다...' });

    try {
      let response: SubmitResponse;
      console.log(currentQuestion)
      if (currentTailQuestionId) {
        // 꼬리 질문 답변 제출
        const { data } = await axios.post(`/api/tail-questions/${currentTailQuestionId}/submit`, {
          interviewQuestionId: currentQuestion.interviewQuestionId,
          tailQuestionId: currentTailQuestionId,
          answerState: 'COMPLETE',
          timeToAnswer: 0, // TODO: 시간 측정 추가
          answerContent: content
        }, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('access_token')}`
          }
        });
        response = data;
      } else {
        // 일반 질문 답변 제출
        const { data } = await axios.post(`/api/interviews/${interviewId}/submit`, {
          interviewQuestionId: currentQuestion.interviewQuestionId,
          answerState: 'COMPLETE',
          timeToAnswer: 0, // TODO: 시간 측정 추가
          answerContent: content
        }, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('access_token')}`
          }
        });
        response = data;
      }

      // 시스템 메시지 제거
      setChatMessages(prev => prev.filter(msg => msg.type !== 'system'));

      if (response.tailQuestionId) {
        setCurrentTailQuestionId(response.tailQuestionId);
        addMessage({ type: 'question', content: response.question });
      } else {
        setCurrentTailQuestionId(null);
        await refetchQuestion();
        clearChat();
      }
    } catch (error) {
      console.error('Failed to submit answer:', error);
      addMessage({ type: 'system', content: '답변 제출에 실패했습니다.' });
    } finally {
      setIsSubmitting(false);
      chatInputRef.current?.focus();
    }
  };

  const handleSkipQuestion = () => {
    // TODO: 질문 건너뛰기 처리
    console.log('Skip question');
  };

  if (isLoadingInterview || isLoadingQuestion) {
    return (
      <div className="flex items-center justify-center h-full">
        <Card className="p-6">
          <p>Loading...</p>
        </Card>
      </div>
    );
  }

  if (interviewError || questionError) {
    return (
      <div className="flex items-center justify-center h-full">
        <Card className="p-6 border-destructive">
          <p className="text-destructive">
            Error: {interviewError?.message || questionError?.message}
          </p>
        </Card>
      </div>
    );
  }

  if (!interview || !currentQuestion) {
    return (
      <div className="flex items-center justify-center h-full">
        <Card className="p-6">
          <p>No interview or question found</p>
        </Card>
      </div>
    );
  }

  const progress = (currentQuestion.index / currentQuestion.size) * 100;

  return (
    <div className="flex h-full">
      <QuestionList 
        questions={interview.interviewQuestions}
        onQuestionSelect={handleQuestionSelect}
      />
      <div className="flex-1 flex flex-col h-full">
        {/* Progress Bar */}
        <div className="p-4 border-b">
          <div className="flex justify-between text-sm text-muted-foreground mb-2">
            <span>Question {currentQuestion.index} of {currentQuestion.size}</span>
          </div>
          <Progress value={progress} className="h-2" />
        </div>

        {/* Chat Messages */}
        <div className="flex-1 overflow-y-auto p-6 space-y-6">
          <ChatMessage
            type="question"
            content={currentQuestion.question}
          />
          {chatMessages.map(message => (
            <ChatMessage
              key={message.id}
              type={message.type}
              content={message.content}
            />
          ))}
        </div>

        {/* Chat Input */}
        <div className="p-4 border-t">
          <ChatInput
            ref={chatInputRef}
            onSubmit={handleSubmitAnswer}
            onSkip={handleSkipQuestion}
            disabled={isSubmitting}
          />
        </div>
      </div>
    </div>
  );
}
