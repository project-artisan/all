import { Outlet } from 'react-router-dom';

import Navbar from '@/components/navbar';
import { Sidebar } from '@/components/sidebar';
import { ResizableHandle, ResizablePanel, ResizablePanelGroup } from '@/components/ui/resizable';

export default function DefaultLayout() {
  return (
    <>
      <div className='flex flex-col h-screen fixed inset-0'>
        <Navbar />
        <div className='flex-1 flex overflow-hidden'>
          <ResizablePanelGroup direction='horizontal'>
            <ResizablePanel
              defaultSize={20}
              maxSize={30}
              minSize={20}
              className='flex-none'
            >

              <main className='h-full overflow-y-auto overscroll-none'>
              <Sidebar />
              </main>
            </ResizablePanel>
            <ResizableHandle withHandle />
            <ResizablePanel>
              <main className='h-full overflow-y-auto overscroll-none'>
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

