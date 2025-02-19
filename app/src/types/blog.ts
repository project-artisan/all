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
  viewCount: number;
  thumbnail: string;
  techBlog: TechBlog;
  categories: Category[];
  hasRead: boolean;
} 