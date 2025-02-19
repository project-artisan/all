import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import { Suspense } from 'react'
import DefaultLayout from '@/layouts/DefaultLayout'
import AuthLayout from '@/layouts/AuthLayout'
import NotFoundPage from '@/pages/error/NotFound'
import WaitingView from '@/components/WaitingView'
import React from 'react'

// Lazy loading for pages
const Dashboard = React.lazy(() => import('@/pages/Dashboard'))
const TechBlog = React.lazy(() => import('@/pages/blogs/TechBlog'))
const FrontendInterview = React.lazy(() => import('@/pages/interview/Frontend'))
const Settings = React.lazy(() => import('@/pages/Settings'))
const LoginPage = React.lazy(() => import('@/pages/auth/Login'))
const SignupPage = React.lazy(() => import('@/pages/auth/Signup'))

export default function AppRoutes() {
  const router = createBrowserRouter([
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
        { path: '/blogs/tech', element: <TechBlog /> },
        { path: '/interview/frontend', element: <FrontendInterview /> },
        { path: '/settings', element: <Settings /> }
      ]
    },
    {
      path: '/auth',
      element: (
        <Suspense fallback={<WaitingView />}>
          <AuthLayout />
        </Suspense>
      ),
      errorElement: <NotFoundPage />,
      children: [
        { index: true, element: <LoginPage /> },
        { path: '/auth/signup', element: <SignupPage /> }
      ]
    }
  ])

  return <RouterProvider router={router} />
} 