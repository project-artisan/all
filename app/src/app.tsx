import { useState } from 'react';
import { BlogList } from './components/blog/BlogList';
import { LayoutProvider } from './contexts/LayoutContext';
import { Card, CardContent, CardHeader, CardTitle } from "./components/ui/card";
import { ScrollArea } from "./components/ui/scroll-area";
import { Checkbox } from "./components/ui/checkbox";
import { blogPosts } from './data/blogPosts';
import { cn } from '@/lib/utils';
import ThemeProvider from './providers/theme';
import Navbar from './components/navbar';
import { BrowserRouter } from 'react-router-dom';

// 중복 제거하여 unique한 techBlog 목록 생성
const techBlogs = Array.from(
  new Set(blogPosts.map(post => JSON.stringify(post.techBlog)))
)
.map(str => JSON.parse(str));

// unique한 카테고리 목록 생성
const categories = Array.from(
  new Set(blogPosts.flatMap(post => post.categories.map(cat => JSON.stringify(cat))))
)
.map(str => JSON.parse(str));

function App() {
  const [selectedBlogs, setSelectedBlogs] = useState<string[]>([]);
  const [selectedCategories, setSelectedCategories] = useState<number[]>([]);

  const handleBlogSelect = (code: string) => {
    setSelectedBlogs(prev => 
      prev.includes(code)
        ? prev.filter(c => c !== code)
        : [...prev, code]
    );
  };

  const handleCategorySelect = (id: number) => {
    setSelectedCategories(prev =>
      prev.includes(id)
        ? prev.filter(c => c !== id)
        : [...prev, id]
    );
  };

  const filteredPosts = blogPosts.filter(post => {
    const blogMatch = selectedBlogs.length === 0 || selectedBlogs.includes(post.techBlog.code);
    const categoryMatch = selectedCategories.length === 0 || 
      post.categories.some(cat => selectedCategories.includes(cat.id));
    return blogMatch && categoryMatch;
  });

  return (
    <BrowserRouter>

    <ThemeProvider>
    <Navbar/>
    
    <LayoutProvider>
      <div className="min-h-screen bg-background">
        <header className="sticky top-0 z-50 w-full border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
          <div className="container flex h-14 items-center">
            <div className="mr-4 hidden md:flex">
              <a className="mr-6 flex items-center space-x-2" href="/">
                <span className="hidden font-bold sm:inline-block">
                  Tech Blog Reader
                </span>
              </a>
            </div>
          </div>
        </header>

        <div className="container flex-1 items-start md:grid md:grid-cols-[220px_minmax(0,1fr)] md:gap-6 lg:grid-cols-[240px_minmax(0,1fr)] lg:gap-10">
          <aside className="fixed top-14 z-30 -ml-2 hidden h-[calc(100vh-3.5rem)] w-full shrink-0 overflow-y-auto border-r md:sticky md:block">
            <ScrollArea className="py-6 pr-6 lg:py-8">
              <Card className="border-0 shadow-none">
                <CardHeader>
                  <CardTitle>필터</CardTitle>
                </CardHeader>
                <CardContent className="p-0">
                  <div className="space-y-4">
                    <div>
                      <h3 className="mb-4 text-sm font-medium">기술 블로그</h3>
                      <div className="space-y-3">
                        {techBlogs.map((blog) => (
                          <div key={blog.code} className="flex items-center space-x-2">
                            <Checkbox 
                              id={blog.code}
                              checked={selectedBlogs.includes(blog.code)}
                              onCheckedChange={() => handleBlogSelect(blog.code)}
                            />
                            <label
                              htmlFor={blog.code}
                              className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                            >
                              {blog.name}
                            </label>
                          </div>
                        ))}
                      </div>
                    </div>
                    
                    <div>
                      <h3 className="mb-4 text-sm font-medium">카테고리</h3>
                      <div className="space-y-3">
                        {categories.map((category) => (
                          <div key={category.id} className="flex items-center space-x-2">
                            <Checkbox 
                              id={`category-${category.id}`}
                              checked={selectedCategories.includes(category.id)}
                              onCheckedChange={() => handleCategorySelect(category.id)}
                            />
                            <label
                              htmlFor={`category-${category.id}`}
                              className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                            >
                              {category.name}
                            </label>
                          </div>
                        ))}
                      </div>
                    </div>
                  </div>
                </CardContent>
              </Card>
            </ScrollArea>
          </aside>
          
          <main className="flex w-full flex-col overflow-hidden">
            <div className="mx-auto w-full min-w-0">
              <div className="pb-12 pt-8">
                <div className="mb-8">
                  <h1 className="scroll-m-20 text-4xl font-extrabold tracking-tight lg:text-5xl mb-2">
                    기술 블로그
                  </h1>
                  <p className="text-muted-foreground">
                    최신 웹 개발 트렌드와 기술을 공유합니다
                  </p>
                </div>
                <BlogList posts={filteredPosts} />
              </div>
            </div>
          </main>
        </div>
      </div>
    </LayoutProvider>
</ThemeProvider>
    </BrowserRouter>
  );
}

export default App;

