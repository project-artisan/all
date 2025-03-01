import React, { Suspense } from 'react';
import { createBrowserRouter, RouterProvider, Outlet } from 'react-router-dom';

import WaitingView from '@/components/WaitingView';
import AuthLayout from '@/layouts/AuthLayout';
import DefaultLayout from '@/layouts/DefaultLayout';
import InterviewLayout from '@/layouts/InterviewLayout';
import NotFoundPage from '@/pages/error/NotFound';
import OngoingInterviews from '@/pages/interview/OngoingInterviews';
import { Toaster } from '@/components/ui/toaster';
import { AuthProvider } from '@/contexts/auth';

// Lazy loading for pages
const Dashboard = React.lazy(() => import('@/pages/Dashboard'));
const TechBlog = React.lazy(() => import('@/pages/blogs/TechBlog'));
const Companies = React.lazy(() => import('@/pages/blogs/Companies'));
const InterviewCategory = React.lazy(() => import('@/pages/interview/InterviewCategory'));
const AllQuestionSets = React.lazy(() => import('@/pages/interview/AllQuestionSets'));
const InterviewResults = React.lazy(() => import('@/pages/interview/Results'));
const ResultDetail = React.lazy(() => import('@/pages/interview/ResultDetail'));
const InterviewSession = React.lazy(() => import('@/pages/interview/InterviewSession'));
const Settings = React.lazy(() => import('@/pages/Settings'));
const LoginPage = React.lazy(() => import('@/pages/auth/Login'));
const SignupPage = React.lazy(() => import('@/pages/auth/Signup'));
const GithubCallback = React.lazy(() => import('@/pages/auth/GithubCallback'));
const InterviewDetail = React.lazy(() => import('@/pages/interview/InterviewDetail'));

const Root = () => {
  return (
    <AuthProvider>
      <Outlet />
      <Toaster />
    </AuthProvider>
  );
};

const router = createBrowserRouter([
  {
    element: <Root />,
    children: [
      {
        path: '/',
        element: (
          <Suspense fallback={<WaitingView />}>
            <DefaultLayout />
          </Suspense>
        ),
        errorElement: <NotFoundPage />,
        children: [
          { index: true, element: <Dashboard /> },
          { path: 'blogs/tech', element: <TechBlog /> },
          { path: 'blogs/companies', element: <Companies /> },
          {
            path: 'interview',
            children: [
              { index: true, element: <AllQuestionSets /> },
              { path: ':categoryId', element: <InterviewCategory /> },
              { path: 'ongoing', element: <OngoingInterviews/> },
              { path: 'results', element: <InterviewResults /> },
              { path: ':interviewId/detail', element: <InterviewDetail/> }
            ]
          },
          { path: 'settings', element: <Settings /> }
        ]
      },
      {
        path: '/',
        element: (
          <Suspense fallback={<WaitingView />}>
            <AuthLayout />
          </Suspense>
        ),
        children: [
          { path: 'login', element: <LoginPage /> },
          { path: 'signup', element: <SignupPage /> }
        ]
      },
      {
        path: 'auth/github',
        element: <GithubCallback />
      },
      {
        path: '/interviews/',
        element: (
          <Suspense fallback={<WaitingView />}>
            <InterviewLayout />
          </Suspense>
        ),
        children: [{ path: ':interviewId', element: <InterviewSession /> }]
      }
    ]
  }
]);

export default function AppRoutes() {
  return (
    <RouterProvider router={router} />
  );
}
