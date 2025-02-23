import React, { useState, useEffect, useRef, useCallback } from 'react';
import { SearchTechBlogPost, BlogResponse } from '@/types/blog';
import { Button } from '@/components/ui/button';
import { LayoutGrid, List } from 'lucide-react';
import { Separator } from '@/components/ui/separator';
import { axiosInstance } from '@/lib/axios';
import BlogPostCard from '@/components/blogs/BlogPostCard';
import BlogSearch, { SortOption, TechBlogOption } from '@/components/blogs/BlogSearch';

type ViewMode = 'grid' | 'list';

const ITEMS_PER_PAGE = 12;

export default function TechBlog() {
  const [viewMode, setViewMode] = useState<ViewMode>('grid');
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const [hasMore, setHasMore] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [sortOption, setSortOption] = useState<SortOption>('latest');
  const [selectedTechBlogs, setSelectedTechBlogs] = useState<TechBlogOption[]>([]);
  const [posts, setPosts] = useState<SearchTechBlogPost[]>([]);
  const searchTimeoutRef = useRef<NodeJS.Timeout>();
  const observer = useRef<IntersectionObserver>();

  const fetchPosts = useCallback(async () => {
    try {
      setLoading(true);
      const response = await axiosInstance.get<BlogResponse>('/api/v1/blogs', {
        params: {
          title: searchQuery,
          page: page,
          size: ITEMS_PER_PAGE,
          techBlogs: selectedTechBlogs.length > 0 ? selectedTechBlogs : undefined,
        }
      });

      const newPosts = response.data.content;
      setPosts(prevPosts => page === 0 ? newPosts : [...prevPosts, ...newPosts]);
      setHasMore(!response.data.last);
    } catch (error) {
      console.error('Failed to fetch posts:', error);
    } finally {
      setLoading(false);
    }
  }, [page, searchQuery, selectedTechBlogs]);

  const handleSearch = (value: string) => {
    if (searchTimeoutRef.current) {
      clearTimeout(searchTimeoutRef.current);
    }
    searchTimeoutRef.current = setTimeout(() => {
      setSearchQuery(value);
      setPage(0);
      setPosts([]);
    }, 300);
  };

  const handleSortChange = (value: SortOption) => {
    setSortOption(value);
    setPage(0);
    setPosts([]);
  };

  const handleTechBlogChange = (blogs: TechBlogOption[]) => {
    setSelectedTechBlogs(blogs);
    setPage(0);
    setPosts([]);
  };

  const lastPostElementRef = useCallback((node: HTMLElement | null) => {
    if (loading) return;
    
    if (observer.current) {
      observer.current.disconnect();
    }

    observer.current = new IntersectionObserver(entries => {
      if (entries[0].isIntersecting && hasMore) {
        setPage(prevPage => prevPage + 1);
      }
    });

    if (node) {
      observer.current.observe(node);
    }
  }, [loading, hasMore]);

  useEffect(() => {
    fetchPosts();
  }, [fetchPosts]);

  return (
    <div className="min-h-screen">
      <div className="sticky top-0 z-10 bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
        <div className="container mx-auto p-4 space-y-2">
          <div className="flex items-center justify-between gap-4">
            <BlogSearch
              onSearch={handleSearch}
              onSortChange={handleSortChange}
              onTechBlogChange={handleTechBlogChange}
              sortOption={sortOption}
              selectedTechBlogs={selectedTechBlogs}
            />
            <div className="flex items-center space-x-2 shrink-0">
              <Button
                variant={viewMode === 'grid' ? 'default' : 'outline'}
                size="icon"
                onClick={() => setViewMode('grid')}
              >
                <LayoutGrid className="h-4 w-4" />
              </Button>
              <Button
                variant={viewMode === 'list' ? 'default' : 'outline'}
                size="icon"
                onClick={() => setViewMode('list')}
              >
                <List className="h-4 w-4" />
              </Button>
            </div>
          </div>
          <Separator />
        </div>
      </div>

      <div className="container mx-auto p-4">
        <div className={viewMode === 'grid' ? 'grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-3' : 'space-y-4'}>
          {posts.map((post, index) => {
            const isLastElement = index === posts.length - 1;
                return (
                    <BlogPostCard
                key={post.id}
                ref={isLastElement ? lastPostElementRef : null}
                      post={post}
                      viewMode={viewMode}
                    />
                );
              })}
        </div>

        {loading && (
          <div className="flex justify-center p-4">
            <div className="h-6 w-6 animate-spin rounded-full border-2 border-primary border-t-transparent" />
          </div>
        )}
      </div>
    </div>
  );
}