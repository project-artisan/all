import { useState, useEffect } from 'react';
import axios from 'axios';

interface InterviewQuestion {
  interviewQuestionId: number;
  answerState: 'INIT' | 'PROGRESS' | 'COMPLETE';
  question: string;
  remainTailQuestionCount: number;
}

interface Interview {
  interviewId: number;
  interviewState: 'PROGRESS' | 'COMPLETE';
  question: string;
  interviewQuestions: InterviewQuestion[];
}

interface UseInterviewReturn {
  interview: Interview | undefined;
  isLoading: boolean;
  error: Error | null;
}

export function useInterview(interviewId: string | undefined): UseInterviewReturn {
  const [interview, setInterview] = useState<Interview>();
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    const fetchInterview = async () => {
      if (!interviewId) {
        setIsLoading(false);
        return;
      }

      try {
        const response = await axios.get(`/api/interviews/${interviewId}`, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('access_token')}`
          }
        });
        setInterview(response.data);
        setError(null);
      } catch (error) {
        console.error('Failed to fetch interview:', error);
        setError(error as Error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchInterview();
  }, [interviewId]);

  return { interview, isLoading, error };
}

export type { Interview, InterviewQuestion };
