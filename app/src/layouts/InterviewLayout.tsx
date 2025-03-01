import { History, ListChecks, PlayCircle, PlusCircle } from 'lucide-react';
import { Outlet } from 'react-router-dom';

export default function InterviewLayout() {
  return (
    <div className='fixed inset-0 flex h-screen flex-col'>
      <div className='min-h-screen bg-background'>
        <main className='h-screen overflow-hidden'>
          <div className='container h-full max-w-full py-6'>
            <Outlet />
          </div>
        </main>
      </div>
    </div>
  );
}

