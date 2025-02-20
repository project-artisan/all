import { useState, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import { interviewCategories, mockQuestionSets } from '@/types/interview';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Play, BookOpen, Search } from 'lucide-react';
import { Badge } from '@/components/ui/badge';
import { Input } from '@/components/ui/input';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog';
import { Label } from '@/components/ui/label';
import { Slider } from '@/components/ui/slider';
export default function AllQuestionSets() {
  const navigate = useNavigate();
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedQuestionCount, setSelectedQuestionCount] = useState(10);
  const [selectedSet, setSelectedSet] = useState<typeof allQuestionSets[0] | null>(null);
  const [isDialogOpen, setIsDialogOpen] = useState(false);

  // 모든 질문 세트를 하나의 배열로 만듦
  const allQuestionSets = useMemo(() => 
    Object.entries(mockQuestionSets).flatMap(([categoryId, sets]) => 
      sets.map(set => ({
        ...set,
        categoryId,
        categoryName: interviewCategories.find(cat => cat.id === categoryId)?.title || ''
      }))
    ), []);

  const filteredQuestionSets = useMemo(() => {
    if (!searchQuery.trim()) return allQuestionSets;
    
    const query = searchQuery.toLowerCase();
    return allQuestionSets.filter(set => 
      set.title.toLowerCase().includes(query) ||
      set.description.toLowerCase().includes(query) ||
      set.categoryName.toLowerCase().includes(query)
    );
  }, [allQuestionSets, searchQuery]);

  return (
    <div className="space-y-6">
      <div className="space-y-4">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">전체 면접 질문</h1>
          <p className="text-muted-foreground mt-2">
            모든 카테고리의 면접 질문들을 한눈에 살펴보세요.
          </p>
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

      <Tabs defaultValue="all" className="space-y-4">
        <TabsList>
          <TabsTrigger value="all">전체</TabsTrigger>
          {interviewCategories.map(category => (
            <TabsTrigger key={category.id} value={category.id}>
              {category.title}
            </TabsTrigger>
          ))}
        </TabsList>

        <TabsContent value="all" className="space-y-4">
          <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
            {filteredQuestionSets.map((set) => (
              <Card key={set.questionSetId}>
                <CardHeader className="space-y-1">
                  <div className="flex items-center justify-between">
                    <div className="space-y-1">
                      <Badge variant="outline" className="mb-2">
                        {set.categoryName}
                      </Badge>
                      <CardTitle className="text-2xl">{set.title}</CardTitle>
                    </div>
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
                  <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
                    <DialogTrigger asChild>
                      <Button className="w-full" onClick={() => setSelectedSet(set)}>
                        <Play className="mr-2 h-4 w-4" />
                        시작하기
                      </Button>
                    </DialogTrigger>
                    <DialogContent>
                      <DialogHeader>
                        <DialogTitle>문항 수 선택</DialogTitle>
                        <DialogDescription>
                          면접에서 풀어볼 문항 수를 선택해주세요.
                        </DialogDescription>
                      </DialogHeader>
                      <div className="py-6 space-y-6">
                        <div className="space-y-2">
                          <div className="flex justify-between">
                            <Label>문항 수</Label>
                            <span className="text-muted-foreground">{selectedQuestionCount}문항</span>
                          </div>
                          <Slider
                            value={[selectedQuestionCount]}
                            onValueChange={(value) => setSelectedQuestionCount(value[0])}
                            min={5}
                            max={20}
                            step={1}
                            className="w-full"
                          />
                        </div>
                        <div className="flex justify-between text-sm text-muted-foreground">
                          <span>최소 5문항</span>
                          <span>최대 20문항</span>
                        </div>
                      </div>
                      <div className="flex justify-end gap-2">
                        <DialogTrigger asChild>
                          <Button variant="outline" onClick={() => setIsDialogOpen(false)}>
                            취소
                          </Button>
                        </DialogTrigger>
                        <Button
                          onClick={() => {
                            if (selectedSet) {
                              setIsDialogOpen(false);
                              navigate(`/interview/session/${selectedSet.questionSetId}`, {
                                state: {
                                  questionSet: selectedSet,
                                  questionCount: selectedQuestionCount
                                }
                              });
                            }
                          }}
                        >
                          면접 시작
                        </Button>
                      </div>
                    </DialogContent>
                  </Dialog>
                </CardContent>
              </Card>
            ))}
          </div>
        </TabsContent>

        {interviewCategories.map(category => (
          <TabsContent key={category.id} value={category.id} className="space-y-4">
            <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
              {filteredQuestionSets
                .filter(set => set.categoryId === category.id)
                .map((set) => (
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
          </TabsContent>
        ))}
      </Tabs>
    </div>
  );
}
