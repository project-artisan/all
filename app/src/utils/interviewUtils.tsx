import { Badge } from "@/components/ui/badge";
import { CheckCircle2, XCircle, AlertCircle, Clock } from "lucide-react";
import { AnswerState } from "@/types/interview";

export function getStatusBadge(status: AnswerState) {
  switch (status) {
    case 'PASS':
      return (
        <Badge variant="default" className="bg-green-500">
          <CheckCircle2 className="w-3 h-3 mr-1" />
          통과
        </Badge>
      );
    case 'FAIL':
      return (
        <Badge variant="destructive">
          <XCircle className="w-3 h-3 mr-1" />
          실패
        </Badge>
      );
    case 'SKIP':
      return (
        <Badge variant="secondary">
          <AlertCircle className="w-3 h-3 mr-1" />
          건너뜀
        </Badge>
      );
    default:
      return (
        <Badge variant="secondary">
          <Clock className="w-3 h-3 mr-1" />
          초기화
        </Badge>
      );
  }
}
