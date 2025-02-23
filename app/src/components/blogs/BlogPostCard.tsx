import React, { forwardRef } from 'react';
import { Link } from 'react-router-dom';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { SearchTechBlogPost } from '@/types/blog';
import { Eye, Heart, Image as ImageIcon } from 'lucide-react';

const DEFAULT_THUMBNAIL = 'https://placehold.co/600x400/e2e8f0/64748b?text=No+Image';

interface BlogPostCardProps {
  post: SearchTechBlogPost;
  viewMode: 'grid' | 'list';
}

const BlogPostCard = forwardRef<HTMLDivElement, BlogPostCardProps>(
  ({ post, viewMode }, ref) => {
    const [imageError, setImageError] = React.useState(false);

    const handleImageError = () => {
      setImageError(true);
    };

    const thumbnailUrl = imageError || !post.thumbnail ? DEFAULT_THUMBNAIL : post.thumbnail;

  return (
    <Card 
      ref={ref}
        className={viewMode === 'list' ? 'flex overflow-hidden' : ''}
    >
        <div className={viewMode === 'list' ? 'w-48 shrink-0' : 'relative pb-[56.25%]'}>
          <div className={
            viewMode === 'list'
              ? 'relative h-full w-full'
              : 'absolute inset-0'
          }>
              <img
              src={thumbnailUrl}
                alt={post.title}
              onError={handleImageError}
              className={
                viewMode === 'list'
                  ? 'h-full w-full object-cover'
                  : 'h-full w-full object-cover'
              }
            />
            {(imageError || !post.thumbnail) && (
              <div className="absolute inset-0 flex items-center justify-center bg-muted/50">
                <ImageIcon className="h-8 w-8 text-muted-foreground" />
              </div>
            )}
          </div>
            </div>
        <div className="flex-1">
              <CardHeader>
                <div className="flex items-center justify-between mb-2">
                  <Badge variant="outline" className="text-xs">
                    {post.techBlog}
                  </Badge>
                  <div className="flex items-center space-x-4 text-sm text-muted-foreground">
                    <div className="flex items-center space-x-1">
                  <Eye className="h-4 w-4" />
                  <span>{post.viewCount}</span>
                    </div>
                    <div className="flex items-center space-x-1">
                  <Heart className="h-4 w-4" />
                  <span>{post.likeCount}</span>
                    </div>
                  </div>
                </div>
            <CardTitle className="line-clamp-2">
              <Link to={post.link} target="_blank" rel="noopener noreferrer" className="hover:underline">
                {post.title}
              </Link>
            </CardTitle>
              </CardHeader>
          <CardContent>
            <p className="mb-2 line-clamp-2 text-sm text-muted-foreground">
              {post.description}
            </p>
            <div className="flex flex-wrap gap-2">
              {post.categories.map((category) => (
                <Badge key={category.id} variant="secondary">
                  {category.name}
                </Badge>
              ))}
            </div>
          </CardContent>
          </div>
    </Card>
  );
  }
);

BlogPostCard.displayName = 'BlogPostCard';

export default BlogPostCard;
