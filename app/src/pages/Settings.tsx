import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Label } from "@/components/ui/label"
import { Switch } from "@/components/ui/switch"

export default function Settings() {
  return (
    <div className="space-y-6 p-6">
      <div>
        <h3 className="text-2xl font-medium">설정</h3>
        <p className="text-sm text-muted-foreground">
          앱 설정을 관리합니다.
        </p>
      </div>
      <div className="grid gap-4">
        <Card>
          <CardHeader>
            <CardTitle>알림 설정</CardTitle>
            <CardDescription>
              알림 수신 여부를 설정합니다.
            </CardDescription>
          </CardHeader>
          <CardContent className="grid gap-4">
            <div className="flex items-center justify-between">
              <Label htmlFor="push">푸시 알림</Label>
              <Switch id="push" />
            </div>
            <div className="flex items-center justify-between">
              <Label htmlFor="email">이메일 알림</Label>
              <Switch id="email" />
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
} 