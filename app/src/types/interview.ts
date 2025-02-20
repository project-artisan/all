export interface InterviewCategory {
  id: string;
  title: string;
  titleEn: string;
  description: string;
  icon?: string;
}

export const interviewCategories: InterviewCategory[] = [
  {
    id: 'frontend',
    title: '프론트엔드',
    titleEn: 'Frontend',
    description: '프론트엔드 개발자를 위한 기술 면접 질문들을 연습해보세요.',
  },
  {
    id: 'backend',
    title: '백엔드',
    titleEn: 'Backend',
    description: '백엔드 개발자를 위한 기술 면접 질문들을 연습해보세요.',
  },
  {
    id: 'devops',
    title: '데브옵스',
    titleEn: 'DevOps',
    description: '데브옵스 엔지니어를 위한 기술 면접 질문들을 연습해보세요.',
  }
];

export interface Question {
  id: string;
  content: string;
  followUps: FollowUpQuestion[];
}

export interface FollowUpQuestion {
  id: string;
  content: string;
  condition?: string; // 꼬리질문이 나타나는 조건 (예: 키워드 포함)
}

export interface QuestionSet {
  questionSetId: string;
  title: string;
  description: string;
  count: number;
  tailQuestionDepth: number;
  thumbnailUrl: string;
  questions?: Question[];
}

export const mockQuestionSets: Record<string, QuestionSet[]> = {
  frontend: [
    {
      questionSetId: '1',
      title: 'JavaScript 기초',
      description: 'JavaScript의 기본 개념과 동작 원리에 대한 질문들입니다.',
      count: 15,
      tailQuestionDepth: 3,
      thumbnailUrl: 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/javascript/javascript-original.svg'
    },
    {
      questionSetId: '2',
      title: 'React 핵심 개념',
      description: 'React의 핵심 개념과 최적화에 대한 질문들입니다.',
      count: 20,
      tailQuestionDepth: 4,
      thumbnailUrl: 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/react/react-original.svg'
    },
    {
      questionSetId: '3',
      title: '웹 성능 최적화',
      description: '웹 애플리케이션의 성능 최적화에 관한 질문들입니다.',
      count: 12,
      tailQuestionDepth: 3,
      thumbnailUrl: '/images/web-performance.svg'
    },
    {
      questionSetId: '4',
      title: '브라우저 동작 원리',
      description: '브라우저의 렌더링 과정과 내부 동작에 대한 질문들입니다.',
      count: 18,
      tailQuestionDepth: 4,
      thumbnailUrl: '/images/browser.svg'
    }
  ],
  backend: [
    {
      questionSetId: '5',
      title: '데이터베이스 설계',
      description: 'RDBMS 설계와 최적화에 대한 질문들입니다.',
      count: 15,
      tailQuestionDepth: 3,
      thumbnailUrl: '/images/database.svg'
    },
    {
      questionSetId: '6',
      title: 'Node.js 심화',
      description: 'Node.js의 내부 동작과 최적화에 대한 질문들입니다.',
      count: 20,
      tailQuestionDepth: 4,
      thumbnailUrl: 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/nodejs/nodejs-original.svg'
    },
    {
      questionSetId: '7',
      title: '시스템 설계',
      description: '대규모 시스템 설계와 아키텍처에 대한 질문들입니다.',
      count: 12,
      tailQuestionDepth: 5,
      thumbnailUrl: '/images/system-design.svg'
    },
    {
      questionSetId: '8',
      title: 'API 설계',
      description: 'RESTful API 설계와 보안에 대한 질문들입니다.',
      count: 18,
      tailQuestionDepth: 3,
      thumbnailUrl: '/images/api.svg'
    }
  ],
  devops: [
    {
      questionSetId: '9',
      title: '컨테이너 기술',
      description: 'Docker와 Kubernetes에 대한 질문들입니다.',
      count: 15,
      tailQuestionDepth: 4,
      thumbnailUrl: 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/docker/docker-original.svg'
    },
    {
      questionSetId: '10',
      title: 'CI/CD 파이프라인',
      description: '지속적 통합과 배포에 대한 질문들입니다.',
      count: 20,
      tailQuestionDepth: 3,
      thumbnailUrl: '/images/cicd.svg'
    },
    {
      questionSetId: '11',
      title: '클라우드 아키텍처',
      description: 'AWS 클라우드 서비스에 대한 질문들입니다.',
      count: 12,
      tailQuestionDepth: 4,
      thumbnailUrl: 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/amazonwebservices/amazonwebservices-original.svg'
    },
    {
      questionSetId: '12',
      title: '모니터링과 로깅',
      description: '시스템 모니터링과 로그 분석에 대한 질문들입니다.',
      count: 18,
      tailQuestionDepth: 3,
      thumbnailUrl: '/images/monitoring.svg'
    }
  ]
};

export enum AnswerState {
  INIT = "INIT",
  PASS = "PASS",
  COMPLETE = "COMPLETE"
}

export interface InterviewQuestionResponse {
  interviewId: number;
  interviewQuestionId: number;
  question: string;
  index: number;
  size: number;
  remainTailQuestionCount: number;
}

export interface InterviewSubmitRequest {
  interviewId: number;
  interviewQuestionId: number;
  currentIndex: number;
  answerState: AnswerState;
  aiFeedback: string;
  tailQuestion: string;
  timeToAnswer: number;
  answerContent: string;
  score: number;
  referenceLinks: string[];
}

export interface OngoingInterview {
  id: number;
  title: string;
  category: InterviewCategory;
  questionSet: QuestionSet;
  progress: {
    currentQuestionIndex: number;
    totalQuestions: number;
    completedQuestions: number;
    remainingQuestions: number;
  };
  status: 'in-progress' | 'paused';
  startedAt: string;
  lastUpdated: string;
  answers: {
    questionId: number;
    content: string;
    timestamp: string;
  }[];
}

export const mockOngoingInterviews: OngoingInterview[] = [
  {
    id: 1,
    title: "React 프론트엔드 개발자 면접",
    category: interviewCategories[0],
    questionSet: mockQuestionSets.frontend[1], // React 핵심 개념
    progress: {
      currentQuestionIndex: 2,
      totalQuestions: 5,
      completedQuestions: 2,
      remainingQuestions: 3
    },
    status: 'in-progress',
    startedAt: "2025-02-20T13:30:00",
    lastUpdated: "2025-02-20T14:00:00",
    answers: [
      {
        questionId: 1,
        content: "Virtual DOM은 실제 DOM의 가벼운 복사본입니다. React는 상태가 변경될 때마다 Virtual DOM을 새로 생성하고, 이전 Virtual DOM과 비교하여 실제로 변경이 필요한 부분만 실제 DOM에 적용합니다.",
        timestamp: "2025-02-20T13:35:00"
      },
      {
        questionId: 2,
        content: "Virtual DOM이 실제 DOM보다 빠른 것은 아닙니다. Virtual DOM의 장점은 DOM 조작을 최소화하는 것에 있습니다. 실제 DOM 조작은 브라우저 리플로우와 리페인트를 발생시키는데, 이는 비용이 큰 작업입니다.",
        timestamp: "2025-02-20T13:45:00"
      }
    ]
  },
  {
    id: 2,
    title: "JavaScript 기초 면접",
    category: interviewCategories[0],
    questionSet: mockQuestionSets.frontend[0], // JavaScript 기초
    progress: {
      currentQuestionIndex: 6,
      totalQuestions: 10,
      completedQuestions: 6,
      remainingQuestions: 4
    },
    status: 'paused',
    startedAt: "2025-02-20T10:00:00",
    lastUpdated: "2025-02-20T10:45:00",
    answers: []
  }
];
