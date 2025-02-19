import { LayoutGrid, List } from "lucide-react"
import { Button } from "@/components/ui/button"
import { useLayout } from "@/contexts/LayoutContext"

export function LayoutToggle() {
  const { layout, toggleLayout } = useLayout();

  return (
    <Button
      variant="outline"
      size="icon"
      onClick={toggleLayout}
      className="ml-2"
    >
      {layout === 'grid' ? (
        <List className="h-4 w-4" />
      ) : (
        <LayoutGrid className="h-4 w-4" />
      )}
    </Button>
  );
} 