import { SearchTechBlogPost } from '@/types/blog';
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { useLayout } from '@/contexts/LayoutContext';
import { cn } from '@/lib/utils';
import { ExternalLink, Eye } from 'lucide-react';

interface BlogCardProps {
  post: SearchTechBlogPost;
}

export function BlogCard({ post }: BlogCardProps) {
  const { layout } = useLayout();

  return (
    <Card className={cn(
      "hover:shadow-lg transition-shadow",
      layout === 'list' && "flex flex-row items-start"
    )}>
      <div className={cn(
        "relative",
        layout === 'list' ? "w-[200px] h-[120px]" : "w-full h-[200px]"
      )}>
        <img 
          src={post.thumbnail} 
          alt={post.title}
          className="w-full h-full object-cover rounded-l-lg"
        />
      </div>
      <div className={cn(
        "flex flex-col flex-1",
        layout === 'list' && "flex-1 flex flex-col md:flex-row"
      )}>
        <CardHeader className={cn(
          layout === 'list' && "flex-1"
        )}>
          <div className="flex justify-between items-start gap-2">
            <CardTitle className="text-xl font-bold hover:text-primary">
              <a href={post.link} target="_blank" rel="noopener noreferrer" className="flex items-center gap-2">
                {post.title}
                <ExternalLink className="h-4 w-4" />
              </a>
            </CardTitle>
            <Badge variant="outline">{post.techBlog.name}</Badge>
          </div>
          <div className="flex gap-2 mt-2 flex-wrap">
            {post.categories.map((category) => (
              <Badge key={category.id} variant="secondary">
                {category.name}
              </Badge>
            ))}
          </div>
        </CardHeader>
        <CardContent className={cn(
          layout === 'list' && "flex-1"
        )}>
          <p className="text-muted-foreground">{post.description}</p>
        </CardContent>
        <CardFooter className={cn(
          "flex justify-between items-center",
          layout === 'list' && "md:w-48"
        )}>
          <div className="flex items-center gap-2">
            <Eye className="h-4 w-4 text-muted-foreground" />
            <span className="text-sm text-muted-foreground">{post.viewCount}</span>
          </div>
          {post.hasRead && (
            <Badge variant="secondary">읽음</Badge>
          )}
        </CardFooter>
      </div>
    </Card>
  );
} 