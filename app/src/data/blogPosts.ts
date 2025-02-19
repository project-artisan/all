import { SearchTechBlogPost } from '../types/blog';

export const blogPosts: SearchTechBlogPost[] = [
  {
    id: 1,
    title: "React 18에서 달라진 점",
    description: "React 18의 주요 변경사항과 새로운 기능들에 대해 알아봅니다.",
    link: "https://example.com/react-18",
    viewCount: 1234,
    thumbnail: "https://picsum.photos/seed/react/400/300",
    techBlog: {
      code: "NAVER",
      name: "네이버"
    },
    categories: [
      { id: 1, name: "React" },
      { id: 2, name: "Frontend" }
    ],
    hasRead: false
  },
  {
    id: 2,
    title: "TypeScript 5.0 새로운 기능 살펴보기",
    description: "TypeScript 5.0에서 추가된 새로운 기능과 개선사항을 자세히 알아봅니다.",
    link: "https://example.com/typescript-5",
    viewCount: 856,
    thumbnail: "https://picsum.photos/seed/typescript/400/300",
    techBlog: {
      code: "KAKAO",
      name: "카카오"
    },
    categories: [
      { id: 3, name: "TypeScript" },
      { id: 4, name: "JavaScript" }
    ],
    hasRead: true
  },
  {
    id: 3,
    title: "Next.js와 서버 컴포넌트",
    description: "Next.js의 서버 컴포넌트를 활용한 성능 최적화 방법을 알아봅니다.",
    link: "https://example.com/nextjs",
    viewCount: 2431,
    thumbnail: "https://picsum.photos/seed/nextjs/400/300",
    techBlog: {
      code: "LINE",
      name: "라인"
    },
    categories: [
      { id: 5, name: "Next.js" },
      { id: 1, name: "React" }
    ],
    hasRead: false
  }
]; 