import { useState, useEffect } from 'react';
import axios from 'axios';

interface InterviewResult {
  interviewId: number;
  title: string;
  interviewStatus: 'PROGRESS' | 'COMPLETE';
  questionCount: number;
  createdAt: string;
  updatedAt: string;
}

interface PageInfo {
  size: number;
  number: number;
  totalElements: number;
  totalPages: number;
}

interface InterviewResultsResponse {
  content: InterviewResult[];
  page: PageInfo;
}

interface UseInterviewResultsReturn {
  results: InterviewResult[];
  pageInfo: PageInfo | null;
  isLoading: boolean;
  error: Error | null;
  fetchPage: (pageNumber: number) => Promise<void>;
}

export function useInterviewResults(initialPage = 0, pageSize = 10): UseInterviewResultsReturn {
  const [results, setResults] = useState<InterviewResult[]>([]);
  const [pageInfo, setPageInfo] = useState<PageInfo | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  const fetchPage = async (pageNumber: number) => {
    setIsLoading(true);
    try {
      const response = await axios.get<InterviewResultsResponse>('/api/interviews/me', {
        params: {
          page: pageNumber,
          size: pageSize,
        },
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('access_token')}`
        }
      });
      
      setResults(response.data.content);
      setPageInfo(response.data.page);
      setError(null);
    } catch (err) {
      console.error('Failed to fetch interview results:', err);
      setError(err as Error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchPage(initialPage);
  }, [initialPage]);

  return { results, pageInfo, isLoading, error, fetchPage };
}

export type { InterviewResult, PageInfo };
