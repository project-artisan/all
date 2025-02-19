import { blogPosts } from '@/data/blogPosts';
import { BlogCard } from './BlogCard';
import { LayoutToggle } from './LayoutToggle';
import { useInfiniteScroll } from '@/hooks/useInfiniteScroll';
import { useLayout } from '@/contexts/LayoutContext';
import { cn } from '@/lib/utils';

export function BlogList() {
  const { posts, loaderRef, hasMore } = useInfiniteScroll(blogPosts);
  const { layout } = useLayout();

  return (
    <>
      <div className="flex justify-end mb-4">
        <LayoutToggle />
      </div>
      
      <div className={cn(
        layout === 'grid' 
          ? "grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"
          : "flex flex-col gap-4"
      )}>
        {posts.map((post, index) => (
          <BlogCard key={index} post={post} />
        ))}
      </div>
      
      {hasMore && (
        <div 
          ref={loaderRef} 
          className="w-full h-20 flex items-center justify-center mt-8"
        >
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary" />
        </div>
      )}
    </>
  );
} 