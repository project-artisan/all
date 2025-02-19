import React from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { ExternalLink } from 'lucide-react';
import { Badge } from '@/components/ui/badge';

interface TechCompany {
  id: number;
  name: string;
  nameEn: string;
  description: string;
  blogUrl: string;
  logoUrl: string;
  categories: string[];
}

const TECH_COMPANIES: TechCompany[] = [
  {
    id: 1,
    name: '네이버',
    nameEn: 'NAVER',
    description: '네이버 개발자들의 다양한 기술 이야기와 개발 문화를 공유합니다.',
    blogUrl: 'https://d2.naver.com',
    logoUrl: 'https://upload.wikimedia.org/wikipedia/commons/thumb/2/23/Naver_Logotype.svg/2560px-Naver_Logotype.svg.png',
    categories: ['웹', '모바일', 'AI', '클라우드', '보안']
  },
  {
    id: 2,
    name: '카카오',
    nameEn: 'Kakao',
    description: '카카오 서비스를 만드는 개발자들의 실제 개발 경험과 기술을 공유합니다.',
    blogUrl: 'https://tech.kakao.com',
    logoUrl: 'https://t1.kakaocdn.net/kakaocorp/corp_thumbnail/Kakao.png',
    categories: ['모바일', '웹', '인프라', 'AI', '보안']
  },
  {
    id: 3,
    name: '우아한형제들',
    nameEn: 'Woowa Bros',
    description: '배달의민족을 만드는 우아한형제들의 개발 문화와 기술을 공유합니다.',
    blogUrl: 'https://techblog.woowahan.com',
    logoUrl: 'https://www.woowahan.com/img/pc/common-logo.png',
    categories: ['웹', '모바일', 'MSA', '데이터']
  },
  {
    id: 4,
    name: '라인',
    nameEn: 'LINE',
    description: '라인의 엔지니어들이 개발하면서 경험한 기술적 시행착오와 해결 과정을 공유합니다.',
    blogUrl: 'https://engineering.linecorp.com/ko/blog',
    logoUrl: 'https://upload.wikimedia.org/wikipedia/commons/thumb/4/41/LINE_logo.svg/2560px-LINE_logo.svg.png',
    categories: ['모바일', '웹', '인프라', 'AI', '보안']
  },
  {
    id: 5,
    name: '당근마켓',
    nameEn: 'Daangn',
    description: '당근마켓 개발자들의 기술 스택과 개발 문화를 소개합니다.',
    blogUrl: 'https://medium.com/daangn',
    logoUrl: 'https://www.daangn.com/logo.png',
    categories: ['웹', '모바일', '인프라', '데이터']
  },
  {
    id: 6,
    name: '토스',
    nameEn: 'Toss',
    description: '토스팀이 쌓은 기술과 경험, 그리고 문화를 공유합니다.',
    blogUrl: 'https://toss.tech',
    logoUrl: 'https://static.toss.im/assets/homepage/toss.png',
    categories: ['핀테크', '웹', '모바일', '보안', '인프라']
  }
];

export default function Companies() {
  return (
    <div className="p-6 space-y-6">
      <div className="space-y-2">
        <h1 className="text-3xl font-bold tracking-tight">기술 블로그 회사 목록</h1>
        <p className="text-muted-foreground">
          국내 IT 기업들의 기술 블로그 모음입니다. 각 회사의 개발 문화와 기술을 확인해보세요.
        </p>
      </div>
      
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {TECH_COMPANIES.map((company) => (
          <Card key={company.id} className="flex flex-col">
            <CardHeader className="space-y-4">
              <div className="h-12 flex items-center">
                <img
                  src={company.logoUrl}
                  alt={`${company.name} 로고`}
                  className="max-h-full max-w-[200px] object-contain"
                />
              </div>
              <div>
                <CardTitle className="flex items-center justify-between">
                  <span>{company.name}</span>
                  <span className="text-sm text-muted-foreground">{company.nameEn}</span>
                </CardTitle>
                <CardDescription className="mt-2 line-clamp-2">
                  {company.description}
                </CardDescription>
              </div>
            </CardHeader>
            <CardContent className="flex-1 space-y-4">
              <div className="flex flex-wrap gap-2">
                {company.categories.map((category) => (
                  <Badge key={category} variant="secondary">
                    {category}
                  </Badge>
                ))}
              </div>
              <Button
                className="w-full"
                onClick={() => window.open(company.blogUrl, '_blank')}
              >
                <ExternalLink className="w-4 h-4 mr-2" />
                블로그 방문하기
              </Button>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
}
