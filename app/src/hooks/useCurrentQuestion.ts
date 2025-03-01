import { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useToast } from '@/hooks/use-toast';

interface CurrentQuestion {
  interviewId: number;
  interviewQuestionId: number;
  question: string;
  index: number;
  size: number;
  remainTailQuestionCount: number;
  interviewStatus: 'PROGRESS' | 'DONE';
}

interface UseCurrentQuestionReturn {
  currentQuestion: CurrentQuestion | null;
  isLoading: boolean;
  error: Error | null;
  refetch: () => Promise<void>;
}

export function useCurrentQuestion(interviewId: string | undefined): UseCurrentQuestionReturn {
  const navigate = useNavigate();
  const { toast } = useToast();
  const [currentQuestion, setCurrentQuestion] = useState<CurrentQuestion | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  const fetchCurrentQuestion = async () => {
    if (!interviewId) {
      setIsLoading(false);
      return;
    }

    try {
      const response = await axios.get(`/api/interviews/${interviewId}/current/problem`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('access_token')}`
        }
      });
      setCurrentQuestion(response.data);
      
      // DONE 상태일 경우 결과 페이지로 이동
      if (response.data.interviewStatus === 'DONE') {
        toast({
          title: "인터뷰 완료",
          description: "모든 질문에 대한 답변이 완료되었습니다. 결과 페이지로 이동합니다.",
          duration: 3000,
        });
        
        // 토스트 메시지를 보여주기 위해 잠시 대기
        setTimeout(() => {
          navigate(`/interview/${interviewId}/detail`);
        }, 1000);
      }
      
      setError(null);
    } catch (error) {
      console.error('Failed to fetch current question:', error);
      setError(error as Error);
      toast({
        variant: "destructive",
        title: "오류 발생",
        description: "질문을 불러오는데 실패했습니다.",
      });
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchCurrentQuestion();
  }, [interviewId]);

  return { currentQuestion, isLoading, error, refetch: fetchCurrentQuestion };
}

export type { CurrentQuestion };
