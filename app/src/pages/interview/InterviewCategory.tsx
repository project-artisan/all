import { useParams } from 'react-router-dom';
import { useState, useMemo } from 'react';
import { interviewCategories, mockQuestionSets } from '@/types/interview';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Play, BookOpen, Search } from 'lucide-react';
import { Badge } from '@/components/ui/badge';
import { Input } from '@/components/ui/input';

export default function InterviewCategory() {
  const { categoryId = '' } = useParams();
  const category = interviewCategories.find(cat => cat.id === categoryId);
  const questionSets = mockQuestionSets[categoryId] || [];
  const [searchQuery, setSearchQuery] = useState('');

  const filteredQuestionSets = useMemo(() => {
    if (!searchQuery.trim()) return questionSets;
    
    const query = searchQuery.toLowerCase();
    return questionSets.filter(set => 
      set.title.toLowerCase().includes(query) ||
      set.description.toLowerCase().includes(query)
    );
  }, [questionSets, searchQuery]);

  if (!category) {
    return <div>카테고리를 찾을 수 없습니다.</div>;
  }

  return (
    <div className="space-y-6">
      <div className="space-y-4">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">{category.title} 면접 연습</h1>
          <p className="text-muted-foreground mt-2">{category.description}</p>
        </div>

        <div className="relative">
          <Search className="absolute left-2 top-2.5 h-4 w-4 text-muted-foreground" />
          <Input
            placeholder="질문 세트 검색..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="pl-8"
          />
        </div>
      </div>

      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-2">
        {filteredQuestionSets.map((set) => (
          <Card key={set.questionSetId}>
            <CardHeader className="space-y-1">
              <div className="flex items-center justify-between">
                <CardTitle className="text-2xl">{set.title}</CardTitle>
                <img 
                  src={set.thumbnailUrl} 
                  alt={set.title}
                  className="w-8 h-8" 
                />
              </div>
              <CardDescription className="text-base">{set.description}</CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="flex gap-2">
                <Badge variant="secondary">
                  <BookOpen className="mr-1 h-3 w-3" />
                  {set.count}개 질문
                </Badge>
                <Badge variant="secondary">
                  깊이 {set.tailQuestionDepth}
                </Badge>
              </div>
              <Button className="w-full">
                <Play className="mr-2 h-4 w-4" />
                시작하기
              </Button>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
}
