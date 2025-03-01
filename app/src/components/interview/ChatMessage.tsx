import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { cn } from "@/lib/utils";

interface ChatMessageProps {
  type: 'question' | 'answer' | 'system';
  content: string;
  timestamp?: string;
}

export function ChatMessage({ type, content, timestamp }: ChatMessageProps) {
  const isQuestion = type === 'question';
  const isSystem = type === 'system';

  // 긴 문자열에 자동 개행 추가
  const formattedContent = content?.replace(/(.{80})/g, "$1\n");

  if (isSystem) {
    return (
      <div className="flex justify-center">
        <div className="bg-muted px-4 py-2 rounded-full">
          <p className="text-sm text-muted-foreground">{content}</p>
        </div>
      </div>
    );
  }

  return (
    <div className={cn(
      "flex gap-3",
      isQuestion 
        ? "mr-auto pr-8" 
        : "ml-auto pl-8 flex-row-reverse",
      "max-w-[85%]"
    )}>
      <Avatar className="h-8 w-8 flex-shrink-0">
        {isQuestion ? (
          <>
            <AvatarImage src="/interviewer-avatar.png" />
            <AvatarFallback>AI</AvatarFallback>
          </>
        ) : (
          <>
            <AvatarImage src="/user-avatar.png" />
            <AvatarFallback>ME</AvatarFallback>
          </>
        )}
      </Avatar>
      <div className={cn(
        "rounded-lg p-3 break-all min-w-0",
        isQuestion 
          ? "bg-muted text-foreground rounded-tl-none" 
          : "bg-primary text-primary-foreground rounded-tr-none"
      )}>
        <p className="text-sm whitespace-pre-wrap overflow-hidden">
          {formattedContent}
        </p>
        {timestamp && (
          <p className="text-xs mt-1 opacity-70">{timestamp}</p>
        )}
      </div>
    </div>
  );
}
