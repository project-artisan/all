import { Outlet } from 'react-router-dom';

import Navbar from '@/components/navbar';
import { Sidebar } from '@/components/sidebar';
import { ResizableHandle, ResizablePanel, ResizablePanelGroup } from '@/components/ui/resizable';

export default function DefaultLayout() {
  return (
    <>
      <div className='flex h-screen flex-col'>
      <Navbar />
        <div className='flex flex-1'>
          <ResizablePanelGroup direction='horizontal'>
            <ResizablePanel defaultSize={25} maxSize={15} minSize={10}>
              <Sidebar />
            </ResizablePanel>
            <ResizableHandle withHandle />
            <ResizablePanel>
              <main className='h-full overflow-y-auto'>
                <div className='container p-4'>
                  <Outlet />
                </div>
              </main>
            </ResizablePanel>
          </ResizablePanelGroup>
        </div>
      </div>
    </>
  );
}
