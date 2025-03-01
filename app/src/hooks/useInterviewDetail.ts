import { useQuery } from '@tanstack/react-query';
import axios from 'axios';

export type AnswerState = 'INIT' | 'PASS' | 'FAIL' | 'SKIP';
export type InterviewState = 'PROGRESS' | 'DONE';

export interface InterviewQuestion {
  interviewQuestionId: number;
  answerState: AnswerState;
  question: string;
  answer: string;
  referenceLinks: string[];
  feedback: string;
  remainTailQuestionCount: number;
}

export interface InterviewDetailResponse {
  interviewId: number;
  interviewState: InterviewState;
  question: string;
  interviewQuestions: InterviewQuestion[];
}

const fetchInterviewDetail = async (interviewId: string): Promise<InterviewDetailResponse> => {
  const accessToken = localStorage.getItem('access_token');
  const response = await axios.get(`/api/interviews/${interviewId}/result`, {
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  });
  return response.data;
};

export const useInterviewDetail = (interviewId: string) => {
  return useQuery({
    queryKey: ['interview', interviewId],
    queryFn: () => fetchInterviewDetail(interviewId),
  });
};
