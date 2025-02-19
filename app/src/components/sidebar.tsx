import { useRef, useState } from "react"
import { Link } from "react-router-dom"
import { Button } from "./ui/button"
import { Sheet, SheetContent, SheetTrigger } from "./ui/sheet"
import { ScrollArea } from "./ui/scroll-area"
import { ResizablePanel, ResizableHandle } from "./ui/resizable"
import { 
  ChevronDown,
  Menu, 
  LayoutDashboard, 
  User, 
  Settings, 
  HelpCircle,
  FileText,
  Folder,
  BookOpen,
  Tag,
  LogIn
} from "lucide-react"
import ThemeToggle from "./ui/theme-toggle"
const menuItems = [
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
      title: '면접 연습',
      icon: <Tag className="h-5 w-5" />,
      submenu: [
        { title: '프론트엔드', path: '/category/frontend' },
        { title: '백엔드', path: '/category/backend' },
        { title: '데브옵스', path: '/category/devops' }
      ]
    },
    { title: '설정', path: '/settings', icon: <Settings className="h-5 w-5" /> }
  ]

export const Sidebar = () => {
  const [isOpen, setIsOpen] = useState(false)
  const [showText, setShowText] = useState(true)
  const [expandedMenus, setExpandedMenus] = useState<string[]>([])
  const panelRef = useRef<HTMLDivElement>(null)

  // 로그인 메뉴는 별도로 관리
  const authMenuItem = {
    title: '로그인',
    path: '/auth',
    icon: <LogIn className="h-5 w-5" />
  }

  const toggleSubmenu = (title: string) => {
    setExpandedMenus(prev => 
      prev.includes(title) 
        ? prev.filter(item => item !== title)
        : [...prev, title]
    )
  }

  const MenuItem = ({ item }: { item: any }) => {
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
              {item.submenu.map((subItem: any) => (
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
      
      <ScrollArea className="flex-1">
        <nav className="space-y-1 px-2">
          {menuItems.map((item) => (
            <MenuItem key={item.path || item.title} item={item} />
          ))}
        </nav>
      </ScrollArea>

      {/* 로그인 메뉴 - 하단 고정 */}
      <div className="border-t p-2">
        <Link
          to={authMenuItem.path}
          className="flex items-center rounded-lg px-3 py-2 text-sm font-medium hover:bg-accent hover:text-accent-foreground"
        >
          <span className={`flex items-center ${!showText ? 'justify-center w-full' : ''}`}>
            {authMenuItem.icon}
            {showText && <span className="ml-3">{authMenuItem.title}</span>}
          </span>
        </Link>
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
        <SheetContent side="left" className="w-64 p-0">
          <SidebarContent />
        </SheetContent>
      </Sheet>

      {/* 데스크톱 사이드바 */}
      <div className="hidden lg:block">
          <div className="h-full border-r bg-background">
            <SidebarContent />
          </div>
      </div>
    </>
  )
} 