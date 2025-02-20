import { Outlet } from 'react-router-dom';
import { PlayCircle, PlusCircle, ListChecks, History } from 'lucide-react';

export default function InterviewLayout() {
  return (
    <div className="min-h-screen bg-background">
      <main className="h-screen overflow-hidden">
        <div className="container h-full max-w-full py-6">
          <Outlet />
        </div>
      </main>
    </div>
  );
}
