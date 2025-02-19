import { Link } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Github } from "lucide-react"
import { Separator } from "@/components/ui/separator"

export default function LoginPage() {
  const handleGithubLogin = () => {
    // GitHub OAuth 로그인 처리
    console.log('GitHub 로그인 시도')
  }

  return (
    <Card className="w-[350px]">
      <CardHeader className="space-y-1">
        <CardTitle className="text-2xl">로그인</CardTitle>
        <CardDescription>
          계속하려면 GitHub로 로그인하세요
        </CardDescription>
      </CardHeader>
      <CardContent className="grid gap-4">
        <Button 
          variant="outline" 
          onClick={handleGithubLogin}
          className="w-full"
        >
          <Github className="mr-2 h-4 w-4" />
          GitHub로 계속하기
        </Button>
        <div className="relative">
          <div className="absolute inset-0 flex items-center">
            <Separator />
          </div>
        </div>
      </CardContent>
    </Card>
  )
} 