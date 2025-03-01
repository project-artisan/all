import { useState, useRef } from 'react'
import { Link } from 'react-router-dom'
import { cn } from '@/lib/utils'
import { Button } from "./ui/button"
import { Sheet, SheetContent, SheetTrigger } from "./ui/sheet"
import { ScrollArea } from "./ui/scroll-area"
import { 
  ChevronDown,
  ChevronUp,
  Menu, 
  LayoutDashboard, 
  User, 
  Settings, 
  HelpCircle,
  FileText,
  Folder,
  BookOpen,
  Tag,
  LogIn,
  PlayCircle,
  MessageSquare,
  LogOut
} from "lucide-react"
import ThemeToggle from "./ui/theme-toggle"
import { interviewCategories } from "@/types/interview"
import { useAuth } from '@/contexts/auth';

interface MenuItem {
  title: string;
  path?: string;
  icon?: React.ReactNode;
  submenu?: MenuItem[];
}

const menuItems: MenuItem[] = [
  { 
    title: '대시보드', 
    path: '/', 
    icon: <LayoutDashboard className="h-5 w-5" /> 
  },
  {
    title: '외부 기술블로그',
    icon: <FileText className="h-5 w-5" />,
    submenu: [
      { title: '기술 블로그', path: '/blogs/tech', icon: <Folder className="h-4 w-4" /> },
      { title: '기술 블로그 회사 목록', path: '/blogs/companies', icon: <Folder className="h-4 w-4" /> },
      { title: '북마크', path: '/blogs/bookmarks', icon: <BookOpen className="h-4 w-4" /> }
    ]
  },
  {
    title: '인터뷰',
    icon: <MessageSquare className="h-5 w-5" />,
    submenu: [
      { title: '전체 보기', path: '/interview', icon: <BookOpen className="h-4 w-4" /> },
      ...interviewCategories.map(category => ({
        title: category.title,
        path: `/interview/${category.id}`
      })),
      { title: '면접 결과', path: '/interview/results', icon: <ChevronDown className="h-4 w-4" /> }
    ]
  },
  { title: '설정', path: '/settings', icon: <Settings className="h-5 w-5" /> }
]

export const Sidebar = () => {
  const [isOpen, setIsOpen] = useState(false)
  const [showText, setShowText] = useState(true)
  const [expandedMenus, setExpandedMenus] = useState<string[]>([])
  const panelRef = useRef<HTMLDivElement>(null)
  const { isAuthenticated, user, logout } = useAuth();

  // 로그인 메뉴는 별도로 관리
  const authMenuItem = {
    title: '로그인',
    path: '/login',
    icon: <LogIn className="h-5 w-5" />
  }

  const authenticatedMenuItems = [
    ...menuItems,
    {
      title: '인터뷰',
      path: '/interview',
      icon: <MessageSquare className="h-5 w-5" />
    },
    {
      title: '프로필',
      path: '/profile',
      icon: <User className="h-5 w-5" />
    }
  ];

  const menuItemsToDisplay = isAuthenticated ? authenticatedMenuItems : menuItems;

  const toggleSubmenu = (title: string) => {
    setExpandedMenus(prev => 
      prev.includes(title) 
        ? prev.filter(item => item !== title)
        : [...prev, title]
    )
  }

  const MenuItem = ({ item }: { item: MenuItem }) => {
    const hasSubmenu = item.submenu && item.submenu.length > 0
    const isExpanded = expandedMenus.includes(item.title)

    if (hasSubmenu) {
      return (
        <div>
          <button
            onClick={() => toggleSubmenu(item.title)}
            className="flex w-full items-center rounded-lg px-3 py-2 text-sm font-medium hover:bg-accent hover:text-accent-foreground"
          >
            <span className={`flex items-center ${!showText ? 'justify-center w-full' : ''}`}>
              {item.icon}
              {showText && (
                <>
                  <span className="ml-3 flex-1">{item.title}</span>
                  <ChevronDown className={`h-4 w-4 transition-transform ${isExpanded ? 'rotate-180' : ''}`} />
                </>
              )}
            </span>
          </button>
          {isExpanded && showText && (
            <div className="ml-4 mt-1 space-y-1">
              {item.submenu.map((subItem: MenuItem) => (
                <Link
                  key={subItem.path}
                  to={subItem.path}
                  className="flex items-center rounded-lg px-3 py-2 text-sm font-medium text-muted-foreground hover:bg-accent hover:text-accent-foreground"
                >
                  {subItem.icon && <span className="mr-2">{subItem.icon}</span>}
                  <span>{subItem.title}</span>
                </Link>
              ))}
            </div>
          )}
        </div>
      )
    }

    return (
      <Link
        to={item.path}
        className="flex items-center rounded-lg px-3 py-2 text-sm font-medium hover:bg-accent hover:text-accent-foreground"
      >
        <span className={`flex items-center ${!showText ? 'justify-center w-full' : ''}`}>
          {item.icon}
          {showText && <span className="ml-3">{item.title}</span>}
        </span>
      </Link>
    )
  }

  const SidebarContent = () => (
    <div className="flex h-full flex-col" ref={panelRef}>
      <div className="flex items-center justify-between px-4 py-2">
        {showText && <h2 className="text-lg font-semibold">메뉴</h2>}
      </div>
      
      <nav className="space-y-1 px-2 flex-1">
        {menuItemsToDisplay.map((item) => (
          <MenuItem key={item.path || item.title} item={item} />
        ))}
      </nav>

      {/* 로그인 메뉴 - 하단 고정 */}
      <div className="border-t p-2">
        {isAuthenticated ? (
          <div className="space-y-4">
            <div className="px-4 py-2">
              <div className="flex items-center gap-2">
                <img
                  src={user?.avatar}
                  alt="프로필"
                  className="h-8 w-8 rounded-full"
                />
                <span className="font-medium">{user?.nickname}</span>
              </div>
            </div>
            <Button
              variant="ghost"
              className="w-full justify-start text-red-500 hover:text-red-600 hover:bg-red-100"
              onClick={logout}
            >
              <LogOut className="h-5 w-5" />
              <span className="ml-2">로그아웃</span>
            </Button>
          </div>
        ) : (
          <Link
            to={authMenuItem.path}
            className="flex items-center rounded-lg px-3 py-2 text-sm font-medium hover:bg-accent hover:text-accent-foreground"
          >
            <span className={`flex items-center ${!showText ? 'justify-center w-full' : ''}`}>
              {authMenuItem.icon}
              {showText && <span className="ml-3">{authMenuItem.title}</span>}
            </span>
          </Link>
        )}
      </div>
    </div>
  )

  const onResize = () => {
    if (panelRef.current) {
      const width = panelRef.current.getBoundingClientRect().width
      setShowText(width > 150) // 150px 이하일 때 텍스트 숨김
    }
  }

  return (
    <>
      {/* 모바일 사이드바 */}
      <Sheet open={isOpen} onOpenChange={setIsOpen}>
        <SheetTrigger asChild className="lg:hidden">
          <Button variant="ghost" size="icon">
            <Menu className="h-6 w-6" />
          </Button>
        </SheetTrigger>
        <SheetContent className="w-64 p-0 h-full flex flex-col" side="left">
          <ScrollArea className="flex-1">
            <SidebarContent />
          </ScrollArea>
        </SheetContent>
      </Sheet>

      {/* 데스크톱 사이드바 */}
      <div className="hidden lg:block">
          <div className="h-full border-r bg-background flex flex-col">
            <ScrollArea className="flex-1">
              <SidebarContent />
            </ScrollArea>
          </div>
      </div>
    </>
  )
} 