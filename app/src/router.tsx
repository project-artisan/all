import { createBrowserRouter } from 'react-router-dom';
import InterviewLayout from '@/layouts/InterviewLayout';
import InterviewSession from '@/pages/interview/InterviewSession';
import OngoingInterviews from '@/pages/interview/OngoingInterviews';

export const router = createBrowserRouter([
  {
    path: '/interview',
    element: <InterviewLayout />,
    children: [
      {
        path: 'session/:interviewId',
        element: <InterviewSession />
      },
      {
        path: 'ongoing',
        element: <OngoingInterviews />
      }
    ]
  }
]);
