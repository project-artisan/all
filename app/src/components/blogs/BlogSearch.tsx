import React from 'react';
import { Search, X } from 'lucide-react';
import { Input } from '@/components/ui/input';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Badge } from "@/components/ui/badge";

export type SortOption = 'latest' | 'oldest' | 'popular';

export type TechBlogOption = 'NAVER' | 'KAKAO' | 'LINE' | 'COUPANG' | 'WOOWAHAN' | 'TOSS';

const SORT_OPTIONS = [
  { value: 'latest', label: '최신 순' },
  { value: 'oldest', label: '과거 순' },
  { value: 'popular', label: '인기 순' },
] as const;

const TECH_BLOG_OPTIONS = [
  { value: 'NAVER', label: '네이버' },
  { value: 'KAKAO', label: '카카오' },
  { value: 'LINE', label: '라인' },
  { value: 'COUPANG', label: '쿠팡' },
  { value: 'WOOWAHAN', label: '우아한형제들' },
  { value: 'TOSS', label: '토스' },
] as const;

interface BlogSearchProps {
  onSearch: (value: string) => void;
  onSortChange: (value: SortOption) => void;
  onTechBlogChange: (value: TechBlogOption[]) => void;
  sortOption: SortOption;
  selectedTechBlogs: TechBlogOption[];
}

export default function BlogSearch({ 
  onSearch, 
  onSortChange, 
  onTechBlogChange,
  sortOption,
  selectedTechBlogs 
}: BlogSearchProps) {
  const handleTechBlogSelect = (value: string) => {
    const techBlogValue = value as TechBlogOption;
    if (selectedTechBlogs.includes(techBlogValue)) {
      onTechBlogChange(selectedTechBlogs.filter(blog => blog !== techBlogValue));
    } else {
      onTechBlogChange([...selectedTechBlogs, techBlogValue]);
    }
  };

  const handleRemoveTechBlog = (blogToRemove: TechBlogOption) => {
    onTechBlogChange(selectedTechBlogs.filter(blog => blog !== blogToRemove));
  };

  return (
    <div className="flex flex-col w-full space-y-2">
      <div className="flex flex-col md:flex-row items-center gap-2 w-full">
        <div className="relative flex-1 w-full md:w-auto">
          <Search className="absolute left-2 top-2.5 h-4 w-4 text-muted-foreground" />
          <Input
            placeholder="Search posts..."
            className="pl-8"
            onChange={(e) => onSearch(e.target.value)}
          />
        </div>
        <div className="flex items-center gap-2 shrink-0">
          <Select
            value={selectedTechBlogs.length === 1 ? selectedTechBlogs[0] : undefined}
            onValueChange={handleTechBlogSelect}
          >
            <SelectTrigger className="w-[140px]">
              <SelectValue placeholder="Select blog" />
            </SelectTrigger>
            <SelectContent>
              {TECH_BLOG_OPTIONS.map((option) => (
                <SelectItem 
                  key={option.value} 
                  value={option.value}
                  className={selectedTechBlogs.includes(option.value as TechBlogOption) ? 'bg-secondary' : ''}
                >
                  {option.label}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
          <Select
            value={sortOption}
            onValueChange={(value) => onSortChange(value as SortOption)}
          >
            <SelectTrigger className="w-[120px]">
              <SelectValue placeholder="Sort by" />
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
      {selectedTechBlogs.length > 0 && (
        <div className="flex flex-wrap gap-2">
          {selectedTechBlogs.map(blogValue => {
            const blog = TECH_BLOG_OPTIONS.find(option => option.value === blogValue);
            return blog && (
              <Badge 
                key={blog.value}
                variant="secondary" 
                className="flex items-center gap-1 cursor-pointer hover:bg-secondary/80"
                onClick={() => handleRemoveTechBlog(blogValue)}
              >
                {blog.label}
                <X className="h-3 w-3" />
              </Badge>
            );
          })}
        </div>
      )}
    </div>
  );
}
