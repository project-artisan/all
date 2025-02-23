export interface BlogPost {
  title: string;
  date: string;
  tags: string[];
  readTime: string;
  excerpt: string;
  author: string;
}

export interface TechBlog {
  code: string;
  name: string;
}

export interface Category {
  id: number;
  name: string;
}

export interface SearchTechBlogPost {
  id: number;
  title: string;
  description: string;
  link: string;
  thumbnail?: string;
  techBlog: string;
  viewCount: number;
  likeCount: number;
  /** ISO date string from Spring LocalDateTime (e.g., "2024-02-22T20:34:31") */
  createdAt: string;
  techBlogName: string;
  categories: Category[];
  hasRead: boolean;
}

export interface Sort {
  empty: boolean;
  sorted: boolean;
  unsorted: boolean;
}

export interface Pageable {
  offset: number;
  sort: Sort;
  paged: boolean;
  pageNumber: number;
  pageSize: number;
  unpaged: boolean;
}

export interface BlogResponse {
  content: SearchTechBlogPost[];
  last: boolean;
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  numberOfElements: number;
  pageable: Pageable;
  first: boolean;
  empty: boolean;
}