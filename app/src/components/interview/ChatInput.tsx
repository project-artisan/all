import { KeyboardEvent, useRef, useState, useEffect, forwardRef } from "react";
import { Button } from "@/components/ui/button";
import { Textarea } from "@/components/ui/textarea";
import { cn } from "@/lib/utils";

interface ChatInputProps {
  onSubmit: (content: string) => void;
  onSkip?: () => void;
  disabled?: boolean;
  className?: string;
}

const MAX_LENGTH = 500;

export const ChatInput = forwardRef<HTMLTextAreaElement, ChatInputProps>(
  ({ onSubmit, onSkip, disabled, className }, ref) => {
    const [content, setContent] = useState("");
    const localRef = useRef<HTMLTextAreaElement>(null);
    const textareaRef = (ref || localRef) as React.RefObject<HTMLTextAreaElement>;

    const focus = () => {
      textareaRef.current?.focus();
    };

    const handleSubmit = () => {
      if (!content.trim() || disabled) return;
      onSubmit(content.trim());
      setContent("");
      if (textareaRef.current) {
        textareaRef.current.style.height = "auto";
      }
    };

    useEffect(() => {
      focus();
    }, []);

    const handleKeyDown = (e: KeyboardEvent<HTMLTextAreaElement>) => {
      // Submit - CMD + Enter
      if (e.metaKey && e.key === "Enter") {
        e.preventDefault();
        handleSubmit();
      }
      // Skip - CMD + P
      if (e.metaKey && e.key === "p" && onSkip) {
        e.preventDefault();
        onSkip();
      }
    };

    const handleTextareaChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
      const newValue = e.target.value;
      if (newValue.length <= MAX_LENGTH) {
        const textarea = e.target;
        textarea.style.height = "auto";
        textarea.style.height = Math.min(textarea.scrollHeight, 120) + "px";
        setContent(newValue);
      }
    };

    return (
      <div className={cn("space-y-2", className)}>
        <div className="relative">
          <Textarea
            ref={textareaRef}
            value={content}
            onChange={handleTextareaChange}
            onKeyDown={handleKeyDown}
            placeholder="Type your answer here... (CMD + Enter to submit, CMD + P to skip)"
            className="min-h-[60px] max-h-[120px] resize-none pr-16"
            disabled={disabled}
            maxLength={MAX_LENGTH}
          />
          <div className="absolute bottom-2 right-2 text-xs text-muted-foreground">
            {content.length}/{MAX_LENGTH}
          </div>
        </div>
        <div className="flex justify-between items-center">
          <div className="text-sm text-muted-foreground space-x-2">
            <span>CMD + Enter to submit</span>
            {onSkip && (
              <>
                <span>•</span>
                <span>CMD + P to skip</span>
              </>
            )}
          </div>
          <div className="space-x-2">
            {onSkip && (
              <Button 
                variant="outline" 
                onClick={onSkip}
                disabled={disabled}
              >
                Skip
              </Button>
            )}
            <Button 
              onClick={handleSubmit}
              disabled={disabled || !content.trim()}
            >
              Submit
            </Button>
          </div>
        </div>
      </div>
    );
  }
);

ChatInput.displayName = "ChatInput";
