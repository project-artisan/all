import React, { useState, useEffect, useRef, useCallback } from 'react';
import { SearchTechBlogPost } from '@/types/blog';
import { Button } from '@/components/ui/button';
import { Link } from 'react-router-dom';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { LayoutGrid, List, Search } from 'lucide-react';
import { Separator } from '@/components/ui/separator';
import { Input } from '@/components/ui/input';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

type ViewMode = 'grid' | 'list';
type SortOption = 'latest' | 'oldest' | 'popular';

const SORT_OPTIONS = [
  { value: 'latest', label: '최신 순' },
  { value: 'oldest', label: '과거 순' },
  { value: 'popular', label: '인기 순' },
] as const;

const ITEMS_PER_PAGE = 12;

export default function TechBlog() {
  const [viewMode, setViewMode] = useState<ViewMode>('grid');
  const [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);
  const [hasMore, setHasMore] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [sortOption, setSortOption] = useState<SortOption>('latest');
  const searchTimeoutRef = useRef<NodeJS.Timeout>();
  const observer = useRef<IntersectionObserver>();
  
  // 검색어 변경 시 페이지 초기화 (디바운스 처리)
  const handleSearch = (value: string) => {
    if (searchTimeoutRef.current) {
      clearTimeout(searchTimeoutRef.current);
    }
    searchTimeoutRef.current = setTimeout(() => {
      setSearchQuery(value);
      setPage(1);
      setPosts([]);
    }, 300);
  };

  // 정렬 옵션 변경 시 페이지 초기화
  const handleSort = (value: SortOption) => {
    setSortOption(value);
    setPage(1);
    setPosts([]);
  };
  
  // 전체 데이터 배열
  const allPosts = new Array(100).fill(0).flatMap(() => [{

    id: 1,
    title: '마이크로서비스 아키텍처에서의 효율적인 데이터 동기화 전략',
    description: '분산 시스템에서 발생하는 데이터 동기화 문제를 해결하기 위한 다양한 패턴과 실제 적용 사례를 소개합니다.',
    link: 'https://techblog.woowahan.com/2707/',
    viewCount: 1234,
    thumbnail: 'https://picsum.photos/seed/ms-arch/800/600',
    techBlog: {
      code: 'woowahan',
      name: '우아한형제들 기술블로그'
    },
    categories: [
      { id: 1, name: 'Architecture' },
      { id: 2, name: 'MSA' }
    ],
    hasRead: false
  },
  {
    id: 2,
    title: 'React Query로 서버 상태 관리하기',
    description: 'React Query를 활용한 효율적인 서버 상태 관리와 캐싱 전략에 대해 알아봅니다.',
    link: 'https://techblog.kakao.com/react-query/',
    viewCount: 856,
    thumbnail: 'https://picsum.photos/seed/react-query/800/600',
    techBlog: {
      code: 'kakao',
      name: '카카오 테크'
    },
    categories: [
      { id: 3, name: 'React' },
      { id: 4, name: 'Frontend' }
    ],
    hasRead: false
  },
  {
    id: 3,
    title: 'Kotlin Coroutines로 비동기 프로그래밍 구현하기',
    description: 'Kotlin Coroutines의 기본 개념부터 실제 프로덕션 적용 사례까지 상세히 다룹니다.',
    link: 'https://tech.naver.com/kotlin-coroutines/',
    viewCount: 567,
    thumbnail: 'https://picsum.photos/seed/kotlin/800/600',
    techBlog: {
      code: 'naver',
      name: '네이버 D2'
    },
    categories: [
      { id: 5, name: 'Kotlin' },
      { id: 6, name: 'Backend' }
    ],
    hasRead: false
  }]);

  // 현재 표시할 포스트들
  const [posts, setPosts] = useState<SearchTechBlogPost[]>([]);

  // 마지막 요소 참조 콜백
  const lastPostElementRef = useCallback((node: HTMLDivElement) => {
    if (loading) return;
    if (observer.current) observer.current.disconnect();

    observer.current = new IntersectionObserver(entries => {
      if (entries[0].isIntersecting && hasMore) {
        setPage(prevPage => prevPage + 1);
      }
    });

    if (node) observer.current.observe(node);
  }, [loading, hasMore]);

  // 페이지가 변경될 때마다 데이터 로드
  useEffect(() => {
    setLoading(true);
    const startIndex = 0;
    const endIndex = page * ITEMS_PER_PAGE;
    
    // 검색어로 필터링된 포스트
    const filteredPosts = allPosts.filter(post => {
      const searchLower = searchQuery.toLowerCase();
      return (
        post.title.toLowerCase().includes(searchLower) ||
        post.description.toLowerCase().includes(searchLower) ||
        post.techBlog.name.toLowerCase().includes(searchLower) ||
        post.categories.some(cat => cat.name.toLowerCase().includes(searchLower))
      );
    });

    // 정렬 적용
    const sortedPosts = [...filteredPosts].sort((a, b) => {
      switch (sortOption) {
        case 'latest':
          return b.id - a.id; // 임시로 id로 정렬, 실제로는 날짜 필드 필요
        case 'oldest':
          return a.id - b.id;
        case 'popular':
          return b.viewCount - a.viewCount;
        default:
          return 0;
      }
    });
    
    const newPosts = sortedPosts.slice(startIndex, endIndex);
    
    // 실제 API 호출을 시뮬레이션하기 위한 지연
    const timer = setTimeout(() => {
      setPosts(newPosts);
      setHasMore(endIndex < allPosts.length);
      setLoading(false);
    }, 500);

    return () => clearTimeout(timer);
  }, [page]);

  return (
    <div className="p-6 space-y-6">
      <div className="space-y-4">
        <div className="flex justify-between items-center">
          <div className="flex items-center gap-4">
            <h1 className="text-3xl font-bold tracking-tight">기술 블로그</h1>
            <Button variant="outline" size="sm" asChild>
              <Link to="/blogs/companies">
                회사 목록 보기
              </Link>
            </Button>
          </div>

          <div className="flex gap-2">
            <Button
              variant={viewMode === 'grid' ? 'default' : 'outline'}
              size="sm"
              onClick={() => setViewMode('grid')}
            >
              <LayoutGrid className="h-4 w-4 mr-2" />
              그리드 뷰
            </Button>
            <Button
              variant={viewMode === 'list' ? 'default' : 'outline'}
              size="sm"
              onClick={() => setViewMode('list')}
            >
              <List className="h-4 w-4 mr-2" />
              리스트 뷰
            </Button>
          </div>
        </div>
        
        <div className="flex items-center space-x-4">
          <div className="grid flex-1 items-center gap-1.5">
            <div className="relative">
              <Search className="absolute left-2 top-2.5 h-4 w-4 text-muted-foreground" />
              <Input
                id="search"
                placeholder="제목, 내용, 카테고리 등으로 검색..."
                className="pl-8"
                onChange={(e) => handleSearch(e.target.value)}
              />
            </div>
          </div>
          <div className="grid w-48 items-center gap-1.5">
            <Select
              value={sortOption}
              onValueChange={(value) => handleSort(value as SortOption)}
            >
              <SelectTrigger id="sort">
                <SelectValue placeholder="정렬 기준 선택" />
              </SelectTrigger>
              <SelectContent>
                {SORT_OPTIONS.map((option) => (
                  <SelectItem key={option.value} value={option.value}>
                    {option.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>
        </div>

      </div>
      <Separator />
      <div className={`${viewMode === 'grid' ? 'grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4' : 'flex flex-col gap-4'}`}>
        {posts.map((post, index) => (
          <Card
            ref={index === posts.length - 1 ? lastPostElementRef : null}
            key={post.id} className={viewMode === 'list' ? 'flex' : ''}>            
            <div className={`${viewMode === 'list' ? 'flex gap-4 p-4' : ''}`}>
              <img 
                src={post.thumbnail} 
                alt={post.title}
                className={`${viewMode === 'grid' ? 'w-full h-48' : 'w-48 h-32'} object-cover rounded-lg`}
              />
              <div className={`${viewMode === 'list' ? 'flex-1' : 'p-4'}`}>
                <CardHeader className="p-0 mb-2">
                  <CardTitle className="text-xl">{post.title}</CardTitle>
                </CardHeader>
                <CardContent className="p-0 space-y-4">
                  <p className="text-muted-foreground line-clamp-2">{post.description}</p>
                  <div className="flex items-center justify-between">
                    <div className="text-sm text-muted-foreground space-x-4">
                      <span>{post.techBlog.name}</span>
                      <span>조회수: {post.viewCount}</span>
                    </div>
                    <div className="flex gap-2">
                      {post.categories.map((category) => (
                        <Badge 
                          key={category.id} 
                          variant="secondary"
                        >
                          {category.name}
                        </Badge>
                      ))}
                    </div>
                  </div>
                </CardContent>
              </div>
            </div>
          </Card>
        ))}
        {loading && (
          <div className="col-span-full flex justify-center p-4">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
          </div>
        )}
      </div>
    </div>
  );
} 